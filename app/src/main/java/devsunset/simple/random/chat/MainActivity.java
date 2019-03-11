package devsunset.simple.random.chat;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.OnClick;
import devsunset.simple.random.chat.modules.AccountInfoService;
import devsunset.simple.random.chat.modules.DataVo;
import devsunset.simple.random.chat.modules.HttpConnectService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        AccountInfoService accountInfo = new AccountInfoService();
        if("-".equals(accountInfo.getAccountInfo(this).get("APP_ID"))){
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
        String strCountry = systemLocale.getCountry(); // KR
        String strLanguage = systemLocale.getLanguage(); // ko

        myInfo.put("COUNTRY",strCountry);
        myInfo.put("LANG",strLanguage);

        AccountInfoService accountInfo = new AccountInfoService();
        Toast.makeText(this, "Create Account Info : "+accountInfo.setAccountInfo(this,myInfo), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnGetAccountInfo)
    void onBtnGetAccountInfoClicked() {
        AccountInfoService accountInfo = new AccountInfoService();
        Toast.makeText(this, "Get Account Info :"+accountInfo.getAccountInfo(this).toString(), Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btnAppInfoInit)
    void onBtnAppInfoInitClicked() {

        Toast.makeText(this, "appInfoInit", Toast.LENGTH_SHORT).show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpConnectService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HttpConnectService httpConnctService = retrofit.create(HttpConnectService.class);

        AccountInfoService accountInfo = new AccountInfoService();

        httpConnctService.appInfoInit(accountInfo.getAccountInfo(this)).enqueue(new Callback<DataVo>() {
            @Override
            public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
                if (response.isSuccessful()) {
                    //Toast.makeText(this, "appInfoInit", Toast.LENGTH_SHORT).show();
                    DataVo data = response.body();
                    if (data != null) {
                        Log.d("appInfoInit", data.getCALL_FUNCTION() + "");
                        Log.d("appInfoInit", data.getRESULT_CODE() + "");
                        Log.d("appInfoInit", data.getRESULT_MESSAGE()+"");
                        Log.d("appInfoInit", data.getRESULT_DATA()+"");
                    }
                }else{
                    Log.d("appInfoInit", "response.isSuccessful() :"+response.isSuccessful());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
                //Toast.makeText(this, "appInfoInit Error", Toast.LENGTH_SHORT).show();
                Log.d("appInfoInit Error", "appInfoInit Error :"+t.getMessage());
            }
        });
    }

    @OnClick(R.id.btnAppInfoUpdate)
    void onBtnAppInfoUpdateClicked() {

        Toast.makeText(this, "appInfoUpdate", Toast.LENGTH_SHORT).show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpConnectService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HttpConnectService httpConnctService = retrofit.create(HttpConnectService.class);

        AccountInfoService accountInfo = new AccountInfoService();

        httpConnctService.appInfoUpdate(accountInfo.getAccountInfo(this)).enqueue(new Callback<DataVo>() {
            @Override
            public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
                if (response.isSuccessful()) {
                    //Toast.makeText(this, "appInfoUpdate", Toast.LENGTH_SHORT).show();
                    DataVo data = response.body();
                    if (data != null) {
                        Log.d("appInfoUpdate", data.getCALL_FUNCTION() + "");
                        Log.d("appInfoUpdate", data.getRESULT_CODE() + "");
                        Log.d("appInfoUpdate", data.getRESULT_MESSAGE()+"");
                        Log.d("appInfoUpdate", data.getRESULT_DATA()+"");
                    }
                }else{
                    Log.d("appInfoUpdate", "response.isSuccessful() :"+response.isSuccessful());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
                //Toast.makeText(this, "appInfoUpdate Error", Toast.LENGTH_SHORT).show();
                Log.d("appInfoUpdate Error", "appInfoInit Error :"+t.getMessage());
            }
        });
    }

    @OnClick(R.id.btnAppInfoRead)
    void onBtnAppInfoReadClicked() {
        Toast.makeText(this, "appInfoRead", Toast.LENGTH_SHORT).show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpConnectService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HttpConnectService httpConnctService = retrofit.create(HttpConnectService.class);

        AccountInfoService accountInfo = new AccountInfoService();

        httpConnctService.appInfoRead(accountInfo.getAccountInfo(this)).enqueue(new Callback<DataVo>() {
            @Override
            public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
                if (response.isSuccessful()) {
                    //Toast.makeText(this, "appInfoRead", Toast.LENGTH_SHORT).show();
                    DataVo data = response.body();
                    if (data != null) {
                        Log.d("appInfoRead", data.getCALL_FUNCTION() + "");
                        Log.d("appInfoRead", data.getRESULT_CODE() + "");
                        Log.d("appInfoRead", data.getRESULT_MESSAGE()+"");
                        Log.d("appInfoRead", data.getRESULT_DATA()+"");
                    }
                }else{
                    Log.d("appInfoRead", "response.isSuccessful() :"+response.isSuccessful());
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
                //Toast.makeText(this, "appInfoRead Error", Toast.LENGTH_SHORT).show();
                Log.d("appInfoRead Error", "appInfoRead Error :"+t.getMessage());
            }
        });
    }

    @OnClick(R.id.btnMessageRandomSend)
    void onBtnMessageRandomSendClicked() {
        Toast.makeText(this, "Message Random Send", Toast.LENGTH_SHORT).show();
    }
}
