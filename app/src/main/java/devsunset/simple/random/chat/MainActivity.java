/*
 * @(#)MainActivity.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tfb.fbtoast.FBToast;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.OnClick;
import devsunset.simple.random.chat.modules.accountservice.AccountInfo;
import devsunset.simple.random.chat.modules.dataservice.AppTalkThread;
import devsunset.simple.random.chat.modules.dataservice.DatabaseClient;
import devsunset.simple.random.chat.modules.httpservice.DataVo;
import devsunset.simple.random.chat.modules.httpservice.HttpConnectClient;
import devsunset.simple.random.chat.modules.httpservice.HttpConnectService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <PRE>
 * SimpleRandomChat Main
 * </PRE>
 * 
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */

public class MainActivity extends BaseActivity {

	HttpConnectService httpConnctService = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		ButterKnife.bind(this);

		//screen capture disable
		// 안드로이드 3.0 이상부터 실행
		if (Build.VERSION.SDK_INT >= 11) {
			getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE, android.view.WindowManager.LayoutParams.FLAG_SECURE);
			// getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
			// getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
		}

		FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
				.showThreadInfo(true)               // (Optional) Whether to show thread info or not. Default true
				.methodCount(2)                     // (Optional) How many method line to show. Default 2
				.methodOffset(5)                    // (Optional) Hides internal method calls up to offset. Default 5
				//.logStrategy(customLog)           // (Optional) Changes the log strategy to print out. Default LogCat
				.tag("___SIMPLE_RANDOM_CHAT____")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
				.build();

		Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
			@Override public boolean isLoggable(int priority, String tag) {
				return BuildConfig.DEBUG;
			}
		});


		httpConnctService = HttpConnectClient.getClient().create(HttpConnectService.class);

		//최초 설치 여부 확인
		if("-".equals(AccountInfo.getAccountInfo(this).get("APP_ID"))){

			// 최초 설정 여부 저장
			Locale systemLocale = getApplicationContext().getResources().getConfiguration().locale;
			String strCountry = systemLocale.getCountry();
			String strLanguage = systemLocale.getLanguage();

			HashMap<String,Object> myInfo = new HashMap<String,Object>();
			myInfo.put("APP_ID",UUID.randomUUID().toString());
			myInfo.put("APP_NUMBER",Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID));
			//myInfo.put("GENDER","M");
			//myInfo.put("APP_PHONE", getPhoneNumber(this.getApplicationContext()));
			myInfo.put("COUNTRY",strCountry);
			myInfo.put("LANG",strLanguage);
			myInfo.put("INITIALIZE","Y");
			AccountInfo.setAccountInfo(this,myInfo);

			// 서버에 계정 생성 호출
			httpConnctService.appInfoInit(AccountInfo.getAccountInfo(this)).enqueue(new Callback<DataVo>() {
				@Override
				public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
					if (response.isSuccessful()) {
						DataVo data = response.body();
						if (data != null) {
							Logger.d(data.getCALL_FUNCTION());
							Logger.d(data.getRESULT_CODE());
							Logger.d(data.getRESULT_MESSAGE());
							Logger.d(data.getRESULT_DATA());
							Logger.i("appInfoInit Success : "+data.getRESULT_MESSAGE());
							getNoticeProcess();
						}
					}else{
						Logger.e("appInfoInit : "+response.isSuccessful());
						FBToast.infoToast(MainActivity.this,getString(R.string.networkerror),FBToast.LENGTH_SHORT);
					}
				}

				@Override
				public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
					Logger.e(t.getMessage());
					FBToast.infoToast(MainActivity.this,getString(R.string.networkerror),FBToast.LENGTH_SHORT);
				}
			});

		}
	}

	@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
	public static String getPhoneNumber(Context context){
		String phoneNumber = "-";

		int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS);

		if(permissionCheck== PackageManager.PERMISSION_DENIED){
			// 권한 없음
		}else{
			TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			try {
				String tmpPhoneNumber = mgr.getLine1Number();
				phoneNumber = tmpPhoneNumber.replace("+82", "0");
			} catch (Exception e) {
				phoneNumber = "-";
			}
		}
		return phoneNumber;
	}


	private void getNoticeProcess(){
		//공지 사항 조회 처리
		httpConnctService.appNotice().enqueue(new Callback<DataVo>() {
			@Override
			public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
				if (response.isSuccessful()) {
					DataVo data = response.body();
					if (data != null) {
						Logger.d(data.getCALL_FUNCTION());
						Logger.d(data.getRESULT_CODE());
						Logger.d(data.getRESULT_MESSAGE());
						Logger.d(data.getRESULT_DATA());
						FBToast.successToast(MainActivity.this,data.getRESULT_DATA().toString(),FBToast.LENGTH_SHORT);
						//initActivity();
					}
				}else{
					FBToast.infoToast(MainActivity.this,getString(R.string.networkerror),FBToast.LENGTH_SHORT);
				}
			}

			@Override
			public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
				//initActivity();
				Logger.e(t.getMessage());
				FBToast.infoToast(MainActivity.this,getString(R.string.networkerror),FBToast.LENGTH_SHORT);
			}
		});
	}

	private void initActivity(){
		Intent intent = new Intent(this, LockActivity.class);
		startActivity(intent);
		finish();
	}

	@OnClick(R.id.btnGo)
	void onBtnGoClicked() {
		initActivity();
	}


	@OnClick(R.id.btnInitialize)
	void onBtnInitializeClicked() {
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("APP_ID","-");
		AccountInfo.setAccountInfo(this,params);
		FBToast.successToast(MainActivity.this,"Initialize : "+AccountInfo.getAccountInfo(this),FBToast.LENGTH_SHORT);
	}

	@OnClick(R.id.btnMessageRandomSend)
	void onBtnMessageRandomSendClicked() {

		httpConnctService.sendMessage(AccountInfo.getAccountInfo(this)).enqueue(new Callback<DataVo>() {
			@Override
			public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
				if (response.isSuccessful()) {
					DataVo data = response.body();
					if (data != null) {
						Logger.d(data.getCALL_FUNCTION());
						Logger.d(data.getRESULT_CODE());
						Logger.d(data.getRESULT_MESSAGE());
						Logger.d(data.getRESULT_DATA());
						FBToast.successToast(MainActivity.this,data.getRESULT_MESSAGE().toString(),FBToast.LENGTH_SHORT);
					}
				}else{
					FBToast.errorToast(MainActivity.this,"sendMessage : "+response.isSuccessful(),FBToast.LENGTH_SHORT);
				}
			}

			@Override
			public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
				Logger.e(t.getMessage());
				FBToast.errorToast(MainActivity.this,"sendMessage : "+t.getMessage(),FBToast.LENGTH_SHORT);
			}
		});
	}


	@OnClick(R.id.btnDBCreate)
	void onBtnDBCreateClicked() {
		saveDatabse();
	}

	private void saveDatabse() {

		class SaveTask extends AsyncTask<Void, Void, Void> {

			@Override
			protected Void doInBackground(Void... voids) {

				//creating a task
				AppTalkThread att = new AppTalkThread();
				att.setTALK_ID(UUID.randomUUID().toString());
				att.setTALK_TEXT("text___________: "+UUID.randomUUID().toString());

				//adding to database
				DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
						.AppTalkThreadDao().insert(att);

				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				FBToast.infoToast(MainActivity.this,"DB Create",FBToast.LENGTH_SHORT);
			}
		}

		SaveTask st = new SaveTask();
		st.execute();
	}

	@OnClick(R.id.btnDBRead)
	void onbtnDBReadClicked() {
		getDatabase();
	}

	private void getDatabase() {

		class GetTasks extends AsyncTask<Void, Void, List<AppTalkThread>> {

			@Override
			protected List<AppTalkThread> doInBackground(Void... voids) {
				List<AppTalkThread> taskList = DatabaseClient
						.getInstance(getApplicationContext())
						.getAppDataBase()
						.AppTalkThreadDao()
						.getAll();
				return taskList;
			}

			@Override
			protected void onPostExecute(List<AppTalkThread> appTalkThread) {
				String temp  ="";

				if(appTalkThread !=null && !appTalkThread.isEmpty()){
					for(int i=0;i<appTalkThread.size();i++){
						temp += appTalkThread.get(i).getTALK_TEXT();

						if(i > 5){
							break;
						}
					}
				}

				FBToast.infoToast(MainActivity.this,temp,FBToast.LENGTH_SHORT);

			}
		}

		GetTasks gt = new GetTasks();
		gt.execute();
	}
}
