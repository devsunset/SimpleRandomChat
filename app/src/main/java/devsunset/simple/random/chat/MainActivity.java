package devsunset.simple.random.chat;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

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

public class MainActivity extends AppCompatActivity {

    HttpConnectService httpConnctService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //screen capture disable
        // 안드로이드 3.0 이상부터 실행
        if (Build.VERSION.SDK_INT >= 11) {
            getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE, android.view.WindowManager.LayoutParams.FLAG_SECURE);
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
            // getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });

        if("-".equals(AccountInfoService.getAccountInfo(this).get("APP_ID"))){
            Toast.makeText(this, "INIT PRE", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "INIT NEXT", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.btnCreateAccountInfo)
    void onBtnCreateAccountInfoClicked() {
        HashMap<String,Object> myInfo = new HashMap<String,Object>();
        myInfo.put("APP_ID",UUID.randomUUID().toString());
        myInfo.put("APP_NUMBER",Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID));

        Locale systemLocale = getApplicationContext().getResources().getConfiguration().locale;
        String strCountry = systemLocale.getCountry();
        String strLanguage = systemLocale.getLanguage();

        myInfo.put("COUNTRY",strCountry);
        myInfo.put("LANG",strLanguage);

        Toast.makeText(this, "Create Account Info : "+AccountInfoService.setAccountInfo(this,myInfo), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnGetAccountInfo)
    void onBtnGetAccountInfoClicked() {
        Toast.makeText(this, "Get Account Info :"+AccountInfoService.getAccountInfo(this).toString(), Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btnAppInfoInit)
    void onBtnAppInfoInitClicked() {

        Toast.makeText(this, "appInfoInit", Toast.LENGTH_SHORT).show();

        httpConnctService = HttpConnectClient.getClient().create(HttpConnectService.class);

        httpConnctService.appInfoInit(AccountInfoService.getAccountInfo(this)).enqueue(new Callback<DataVo>() {
            @Override
            public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
                if (response.isSuccessful()) {
                    //Toast.makeText(this, "appInfoInit", Toast.LENGTH_SHORT).show();
                    DataVo data = response.body();
                    if (data != null) {
                        Logger.d(data.getCALL_FUNCTION());
                        Logger.d(data.getRESULT_CODE());
                        Logger.d(data.getRESULT_MESSAGE());
                        Logger.d(data.getRESULT_DATA());
                    }
                }else{
                    Logger.i("appInfoInit : "+response.isSuccessful());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
                //Toast.makeText(this, "appInfoInit Error", Toast.LENGTH_SHORT).show();
                Logger.e(t.getMessage());
            }
        });
    }

    @OnClick(R.id.btnAppInfoUpdate)
    void onBtnAppInfoUpdateClicked() {

        Toast.makeText(this, "appInfoUpdate", Toast.LENGTH_SHORT).show();

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
                    }
                }else{
                    Logger.i("appInfoUpdate : "+response.isSuccessful());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
                //Toast.makeText(this, "appInfoUpdate Error", Toast.LENGTH_SHORT).show();
                Logger.e(t.getMessage());
            }
        });
    }

    @OnClick(R.id.btnAppInfoRead)
    void onBtnAppInfoReadClicked() {
        Toast.makeText(this, "appInfoRead", Toast.LENGTH_SHORT).show();

        httpConnctService = HttpConnectClient.getClient().create(HttpConnectService.class);

        httpConnctService.appInfoRead(AccountInfoService.getAccountInfo(this)).enqueue(new Callback<DataVo>() {
            @Override
            public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
                if (response.isSuccessful()) {
                    //Toast.makeText(this, "appInfoRead", Toast.LENGTH_SHORT).show();
                    DataVo data = response.body();
                    if (data != null) {
                        Logger.d(data.getCALL_FUNCTION());
                        Logger.d(data.getRESULT_CODE());
                        Logger.d(data.getRESULT_MESSAGE());
                        Logger.d(data.getRESULT_DATA());
                    }
                }else{
                    Logger.i("appInfoRead : "+response.isSuccessful());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
                //Toast.makeText(this, "appInfoRead Error", Toast.LENGTH_SHORT).show();
                Logger.e(t.getMessage());
            }
        });
    }

    @OnClick(R.id.btnMessageRandomSend)
    void onBtnMessageRandomSendClicked() {
        Toast.makeText(this, "Message Random Send", Toast.LENGTH_SHORT).show();
    }
}
