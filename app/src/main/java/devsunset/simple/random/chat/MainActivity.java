/*
 * @(#)MainActivity.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.widget.ArrayAdapter;

import com.orhanobut.logger.Logger;
import com.tfb.fbtoast.FBToast;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelySaveStateHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import butterknife.ButterKnife;
import devsunset.simple.random.chat.modules.accountservice.AccountInfo;
import devsunset.simple.random.chat.modules.etcservice.GenderAdapter;
import devsunset.simple.random.chat.modules.etcservice.GenderOption;
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

public class MainActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {
	private final int MY_PERMISSIONS_REQUEST_CODE = 1;
	TelephonyManager telephonyManager;

	HttpConnectService httpConnctService = null;
	private LovelySaveStateHandler saveStateHandler;
	private  int INIT_CHECK = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		ButterKnife.bind(this);

		//screen capture disable
		if (Build.VERSION.SDK_INT >= 11) {
			getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE, android.view.WindowManager.LayoutParams.FLAG_SECURE);
		}

		httpConnctService = HttpConnectClient.getClient().create(HttpConnectService.class);
		saveStateHandler = new LovelySaveStateHandler();

		/*if (checkPermissions()) {
			startApplication();
			telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("msg");
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					setPermissions();
				}
			});
			builder.show();
		}*/

		//최초 설치 여부 확인
		if("-".equals(AccountInfo.getAccountInfo(getApplicationContext()).get("APP_ID"))){
			showGenderChoiceDialog();
		}else{
			getNoticeProcess();
		}
	}

	private void showGenderChoiceDialog() {
		ArrayAdapter<GenderOption> adapter = new GenderAdapter(this, loadGenderOptions());
		Dialog dlg = new LovelyChoiceDialog(this)
				.setTopColorRes(R.color.darkGreen)
				.setTitle(R.string.gender_choice_title)
				.setInstanceStateHandler(2, saveStateHandler)
				.setIcon(R.drawable.ic_assignment_white_36dp)
				.setMessage(R.string.gender_choice_message)
				.setItems(adapter, (position, item) -> 	setAppInitProcess(item.gender_value))
				.show();

		dlg.setOnDismissListener(dialog -> {
				if(INIT_CHECK == 0){
					FBToast.infoToast(MainActivity.this,getString(R.string.gender_choice_message),FBToast.LENGTH_SHORT);
					showGenderChoiceDialog();
				}
		});
	}

	private List<GenderOption> loadGenderOptions() {
		List<GenderOption> result = new ArrayList<>();
		String[] raw = getResources().getStringArray(R.array.gender);
		for (String op : raw) {
			String[] info = op.split("%");
			result.add(new GenderOption(info[1], info[0]));
		}
		return result;
	}

	private void setAppInitProcess(String strGender){
		INIT_CHECK  = 1;

		// 최초 설정 여부 저장
		Locale systemLocale = getApplicationContext().getResources().getConfiguration().locale;
		String strCountry = systemLocale.getCountry();
		String strLanguage = systemLocale.getLanguage();

		HashMap<String,String> myInfo = new HashMap<String,String>();
		myInfo.put("APP_ID",UUID.randomUUID().toString());
		myInfo.put("APP_NUMBER",Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID));
		myInfo.put("GENDER",strGender);
		//myInfo.put("APP_PHONE", getPhoneNumber(this.getApplicationContext()));
		myInfo.put("COUNTRY",strCountry);
		myInfo.put("LANG",strLanguage);
		myInfo.put("INITIALIZE","Y");
		AccountInfo.setAccountInfo(getApplicationContext(),myInfo);

		// 서버에 계정 생성 호출
		httpConnctService.appInfoInit(AccountInfo.getAccountInfo(getApplicationContext())).enqueue(new Callback<DataVo>() {
			@Override
			public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
				if (response.isSuccessful()) {
					DataVo data = response.body();
					if (data != null) {
						if("S".equals(data.getRESULT_CODE())){
							getNoticeProcess();
						}else{
							Logger.e("appInfoInit Fail : "+data.getRESULT_MESSAGE());
							FBToast.infoToast(MainActivity.this,getString(R.string.networkerror),FBToast.LENGTH_SHORT);
							finish();
						}
					}
				}else{
					Logger.e("appInfoInit Fail : "+response.isSuccessful());
					FBToast.infoToast(MainActivity.this,getString(R.string.networkerror),FBToast.LENGTH_SHORT);
					finish();
				}
			}

			@Override
			public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
				Logger.e("appInfoInit Error : "+t.getMessage());
				FBToast.infoToast(MainActivity.this,getString(R.string.networkerror),FBToast.LENGTH_SHORT);
				finish();
			}
		});
	}

    //공지 사항 조회 처리
	private void getNoticeProcess(){
		httpConnctService.appNotice(AccountInfo.getAccountInfo(getApplicationContext())).enqueue(new Callback<DataVo>() {
			@Override
			public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
				if (response.isSuccessful()) {
					DataVo data = response.body();
					if (data != null) {
						showInfoDialog(data.getRESULT_DATA());
					}else{
                        showInfoDialog(null);
                    }
				}else{
					FBToast.infoToast(MainActivity.this,getString(R.string.networkerror),FBToast.LENGTH_SHORT);
                    finish();
				}
			}
			@Override
			public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
				Logger.e("getNoticeProcess Error : "+t.getMessage());
				FBToast.infoToast(MainActivity.this,getString(R.string.networkerror),FBToast.LENGTH_SHORT);
                finish();
			}
		});
	}

	private void showInfoDialog(List<HashMap<String,Object>> params) {
	    if(params !=null && !params.isEmpty() && params.size() > 0 && !params.get(0).containsKey("EMPTY_DATA")){

			CharSequence cs = new StringBuffer((String)params.get(0).get("NOTICE_TXT"));
			int noticeId = Integer.parseInt((String)params.get(0).get("NOTICE_ID"));

			SharedPreferences prefx = this.getSharedPreferences("ld_dont_show", this.MODE_PRIVATE);
			prefx.getBoolean(String.valueOf(noticeId), false);

			if(!prefx.getBoolean(String.valueOf(noticeId), false)){
				Dialog dlg = new LovelyInfoDialog(this)
						.setTopColorRes(R.color.indigo)
						.setIcon(R.drawable.ic_info_outline_white_36dp)
						.setInstanceStateHandler(1, saveStateHandler)
						.setNotShowAgainOptionEnabled(noticeId)
						.setNotShowAgainOptionChecked(true)
						.setTitle(R.string.info_title)
						.setMessage(cs)
						.show();

				dlg.setOnDismissListener(dialog -> {
					initActivity();
				});
			}else{
				initActivity();
			}
        }else{
	    	initActivity();
		}
	}

	private void initActivity(){
		Intent intent = new Intent(this, LockActivity.class);
		startActivity(intent);
		finish();
	}

	/*
	private boolean checkPermissions() {
		if (ActivityCompat.checkSelfPermission(this,
				Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
			return false;
		}
		return true;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode != MY_PERMISSIONS_REQUEST_CODE) {
			return;
		}
		boolean isGranted = true;
		for (int result : grantResults) {
			if (result != PackageManager.PERMISSION_GRANTED) {
				isGranted = false;
				break;
			}
		}

		if (isGranted) {
			startApplication();
		} else {
			Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
		}
	}

	private void setPermissions() {
		ActivityCompat.requestPermissions(this, new String[]{
				Manifest.permission.READ_PHONE_STATE        }, MY_PERMISSIONS_REQUEST_CODE);
	}

	public void startApplication() {
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		//String deviceId = telephonyManager.getDeviceId();
	}*/

}
