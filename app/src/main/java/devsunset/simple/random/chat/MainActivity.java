package devsunset.simple.random.chat;

import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.OnClick;
import devsunset.simple.random.chat.modules.AccountInfo;
import devsunset.simple.random.chat.modules.Data;
import devsunset.simple.random.chat.modules.RetrofitExService;
import okhttp3.ResponseBody;
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


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitExService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitExService retrofitExService = retrofit.create(RetrofitExService.class);

        retrofitExService.getData("1").enqueue(new Callback<Data>() {
            @Override
            public void onResponse(@NonNull Call<Data> call, @NonNull Response<Data> response) {
                if (response.isSuccessful()) {
                    Data body = response.body();
                    if (body != null) {
                        Log.d("data.getUserId()", body.getUserId() + "");
                        Log.d("data.getId()", body.getId() + "");
                        Log.d("data.getTitle()", body.getTitle());
                        Log.d("data.getBody()", body.getBody());
                        Log.e("getData end", "======================================");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Data> call, @NonNull Throwable t) {

            }
        });

        retrofitExService.getData2("1").enqueue(new Callback<List<Data>>() {
            @Override
            public void onResponse(@NonNull Call<List<Data>> call, @NonNull Response<List<Data>> response) {
                if (response.isSuccessful()) {
                    List<Data> datas = response.body();
                    if (datas != null) {
                        for (int i = 0; i < datas.size(); i++) {
                            Log.e("data" + i, datas.get(i).getUserId() + "");
                        }
                        Log.e("getData2 end", "======================================");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Data>> call, @NonNull Throwable t) {

            }
        });

        HashMap<String, Object> input = new HashMap<>();
        input.put("userId", 1);
        input.put("title", "this is title!!");
        input.put("body", "this is body!");
        retrofitExService.postData(input).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(@NonNull Call<Data> call, @NonNull Response<Data> response) {
                if (response.isSuccessful()) {
                    Data body = response.body();
                    if (body != null) {
                        Log.d("data.getUserId()", body.getUserId() + "");
                        Log.d("data.getId()", body.getId() + "");
                        Log.d("data.getTitle()", body.getTitle()+"");
                        Log.d("data.getBody()", body.getBody()+"");
                        Log.e("postData end", "======================================");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Data> call, @NonNull Throwable t) {

            }
        });

        retrofitExService.putData(new Data(1, 1, "title", "body")).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(@NonNull Call<Data> call, @NonNull Response<Data> response) {
                if (response.isSuccessful()) {
                    Data body = response.body();
                    if (body != null) {
                        Log.d("data.getUserId()", body.getUserId() + "");
                        Log.d("data.getId()", body.getId() + "");
                        Log.d("data.getTitle()", body.getTitle());
                        Log.d("data.getBody()", body.getBody());
                        Log.e("putData end", "======================================");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Data> call, @NonNull Throwable t) {

            }
        });

        retrofitExService.patchData("1").enqueue(new Callback<Data>() {
            @Override
            public void onResponse(@NonNull Call<Data> call, @NonNull Response<Data> response) {
                if (response.isSuccessful()) {
                    Data body = response.body();
                    if (body != null) {
                        Log.d("data.getUserId()", body.getUserId() + "");
                        Log.d("data.getId()", body.getId() + "");
                        Log.d("data.getTitle()", body.getTitle());
                        Log.d("data.getBody()", body.getBody());
                        Log.e("patchData end", "======================================");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Data> call, @NonNull Throwable t) {

            }
        });

        retrofitExService.deleteData().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        Log.d("body", body.toString() + "");
                        Log.e("patchData end", "======================================");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        AccountInfo accountInfo = new AccountInfo();
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

        AccountInfo accountInfo = new AccountInfo();
        Toast.makeText(this, "Create Account Info : "+accountInfo.setAccountInfo(this,myInfo), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnGetAccountInfo)
    void onBtnGetAccountInfoClicked() {
        AccountInfo accountInfo = new AccountInfo();
        Toast.makeText(this, "Get Account Info :"+accountInfo.getAccountInfo(this).toString(), Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btnAppInfoInit)
    void onBtnAppInfoInitClicked() {
        Toast.makeText(this, "appInfoInit", Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.btnAppInfoUpdate)
    void onBtnAppInfoUpdateClicked() {
        Toast.makeText(this, "appInfoUpdate", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnMessageRandomSend)
    void onBtnMessageRandomSendClicked() {
        Toast.makeText(this, "Message Random Send", Toast.LENGTH_SHORT).show();
    }
}
