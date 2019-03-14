package devsunset.simple.random.chat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
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
import java.util.Locale;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.OnClick;
import devsunset.simple.random.chat.modules.accountservice.AccountInfoService;
import devsunset.simple.random.chat.modules.httpservice.DataVo;
import devsunset.simple.random.chat.modules.httpservice.HttpConnectClient;
import devsunset.simple.random.chat.modules.httpservice.HttpConnectService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {

    HttpConnectService httpConnctService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //screen capture disable
        // 안드로이드 3.0 이상부터 실행
        if (Build.VERSION.SDK_INT >= 11) {
            getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE, android.view.WindowManager.LayoutParams.FLAG_SECURE);
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
            // getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }

        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(2)         // (Optional) How many method line to show. Default 2
                .methodOffset(5)        // (Optional) Hides internal method calls up to offset. Default 5
                //.logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("___SIMPLE_RANDOM_CHAT____")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });


        if("-".equals(AccountInfoService.getAccountInfo(this).get("APP_ID"))){
            FBToast.successToast(MainActivity.this,"INIT",FBToast.LENGTH_SHORT);
        }else{
            FBToast.successToast(MainActivity.this,"SETTING",FBToast.LENGTH_SHORT);

            httpConnctService = HttpConnectClient.getClient().create(HttpConnectService.class);

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
                        }
                    }else{
                        FBToast.errorToast(MainActivity.this,"appNotice : "+response.isSuccessful(),FBToast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
                    Logger.e(t.getMessage());
                    FBToast.errorToast(MainActivity.this,"appNotice : "+t.getMessage(),FBToast.LENGTH_SHORT);
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

    @OnClick(R.id.btnCreateAccountInfo)
    void onBtnCreateAccountInfoClicked() {
        Locale systemLocale = getApplicationContext().getResources().getConfiguration().locale;
        String strCountry = systemLocale.getCountry();
        String strLanguage = systemLocale.getLanguage();

        HashMap<String,Object> myInfo = new HashMap<String,Object>();
        myInfo.put("APP_ID",UUID.randomUUID().toString());
        myInfo.put("APP_NUMBER",Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID));
        //myInfo.put("APP_PHONE", getPhoneNumber(this.getApplicationContext()));
        myInfo.put("COUNTRY",strCountry);
        myInfo.put("LANG",strLanguage);

        FBToast.successToast(MainActivity.this,"Create Account Info : "+AccountInfoService.setAccountInfo(this,myInfo),FBToast.LENGTH_SHORT);
    }

    @OnClick(R.id.btnGetAccountInfo)
    void onBtnGetAccountInfoClicked() {
        FBToast.successToast(MainActivity.this,"Create Account Info : "+AccountInfoService.getAccountInfo(this),FBToast.LENGTH_SHORT);
    }

    @OnClick(R.id.btnAppInfoInit)
    void onBtnAppInfoInitClicked() {

        httpConnctService = HttpConnectClient.getClient().create(HttpConnectService.class);

        httpConnctService.appInfoInit(AccountInfoService.getAccountInfo(this)).enqueue(new Callback<DataVo>() {
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
                    Logger.i("appInfoInit : "+response.isSuccessful());
                    FBToast.errorToast(MainActivity.this,"appInfoInit : "+response.isSuccessful(),FBToast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
                Logger.e(t.getMessage());
                FBToast.errorToast(MainActivity.this,"appInfoInit : "+t.getMessage(),FBToast.LENGTH_SHORT);
            }
        });
    }

    @OnClick(R.id.btnAppInfoUpdate)
    void onBtnAppInfoUpdateClicked() {

        httpConnctService = HttpConnectClient.getClient().create(HttpConnectService.class);

        httpConnctService.appInfoUpdate(AccountInfoService.getAccountInfo(this)).enqueue(new Callback<DataVo>() {
            @Override
            public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
                if (response.isSuccessful()) {
                    //Toast.makeText(this, "appInfoUpdate", Toast.LENGTH_SHORT).show();
                    DataVo data = response.body();
                    if (data != null) {
                        Logger.d(data.getCALL_FUNCTION());
                        Logger.d(data.getRESULT_CODE());
                        Logger.d(data.getRESULT_MESSAGE());
                        Logger.d(data.getRESULT_DATA());
                        FBToast.successToast(MainActivity.this,data.getRESULT_MESSAGE().toString(),FBToast.LENGTH_SHORT);
                    }
                }else{
                    FBToast.errorToast(MainActivity.this,"appInfoUpdate : "+response.isSuccessful(),FBToast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
                Logger.e(t.getMessage());
                FBToast.errorToast(MainActivity.this,"appInfoUpdate : "+t.getMessage(),FBToast.LENGTH_SHORT);
            }
        });
    }

    @OnClick(R.id.btnMessageRandomSend)
    void onBtnMessageRandomSendClicked() {
        FBToast.successToast(MainActivity.this,"FCK KEY : "+AccountInfoService.getAccountInfo(this).get("APP_KEY"),FBToast.LENGTH_SHORT);
    }
}
