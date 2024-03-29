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
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelySaveStateHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import devsunset.simple.random.chat.modules.accountservice.AccountInfo;
import devsunset.simple.random.chat.modules.dataservice.DatabaseClient;
import devsunset.simple.random.chat.modules.etcservice.GenderAdapter;
import devsunset.simple.random.chat.modules.etcservice.GenderOption;
import devsunset.simple.random.chat.modules.httpservice.DataVo;
import devsunset.simple.random.chat.modules.httpservice.HttpConnectClient;
import devsunset.simple.random.chat.modules.httpservice.HttpConnectService;
import devsunset.simple.random.chat.modules.utilservice.Consts;
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

    @BindView(R.id.progress)
    ProgressBar progress;
    private HttpConnectService httpConnectService = null;
    private LovelySaveStateHandler saveStateHandler;
    private int INIT_CHECK = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        //screen capture disable
        if (Consts.SCREEN_CAPTURE_DISABLE && Build.VERSION.SDK_INT >= 11) {
            getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE, android.view.WindowManager.LayoutParams.FLAG_SECURE);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        httpConnectService = HttpConnectClient.getClient().create(HttpConnectService.class);
        saveStateHandler = new LovelySaveStateHandler();
        progress.setProgress(2);

        /*
            // 권한 획득 (PHONE NUMBER SKIP)
            Permissions.check(this, Manifest.permission.READ_PHONE_STATE, null, new PermissionHandler() {
                @Override
                public void onGranted() {
                    int step = 0;
                    String phoneNumber = "";
                    TelephonyManager telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    try {
                        if (telephony.getLine1Number() != null) {
                            phoneNumber = telephony.getLine1Number();
                            step = 1;
                        } else {
                            if (telephony.getSimSerialNumber() != null) {
                                phoneNumber = telephony.getSimSerialNumber();
                                step = 2;
                            }
                        }
                    } catch (SecurityException e) {
                        finish();
                    } catch (Exception e) {
                        finish();
                    }

                    if (step == 1 && phoneNumber.startsWith("+82")) {
                        phoneNumber = phoneNumber.replace("+82", "0");
                    }

                    if (step == 1) {
                        phoneNumber = PhoneNumberUtils.formatNumber(phoneNumber);
                    }

                    if (step == 0) {
                        phoneNumber = "XXX";
                    }

                    HashMap<String, String> myInfo = new HashMap<String, String>();
                    myInfo.put("APP_PHONE", phoneNumber);
                    myInfo.put("INITIALIZE", "Y");
                    AccountInfo.setAccountInfo(getApplicationContext(), myInfo);

                    //최초 설치 여부 확인
                    if ("-".equals(AccountInfo.getAccountInfo(getApplicationContext()).get("APP_ID"))) {
                        showGenderChoiceDialog();
                    } else {
                        getNoticeProcess();
                    }
                }

                @Override
                public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                    finish();
                }
            });
        */

        //최초 설치 여부 확인
        if ("-".equals(AccountInfo.getAccountInfo(getApplicationContext()).get("APP_ID"))) {
            showGenderChoiceDialog();
        } else {
            getNoticeProcess();
        }
    }

    /**
     * 성별 선택 팝업 처리
     */
    private void showGenderChoiceDialog() {
        ArrayAdapter<GenderOption> adapter = new GenderAdapter(this, loadGenderOptions());
        Dialog dlg = new LovelyChoiceDialog(this)
                .setTopColorRes(R.color.baseColor)
                .setTitle(R.string.gender_choice_title)
                .setInstanceStateHandler(2, saveStateHandler)
                .setIcon(R.drawable.welcome)
                .setMessage(R.string.gender_choice_message)
                .setItems(adapter, (position, item) -> setAppInitProcess(item.gender_value))
                .show();

        dlg.setOnDismissListener(dialog -> {
            if (INIT_CHECK == 0) {
                Toast.makeText(MainActivity.this, getString(R.string.gender_choice_message), Toast.LENGTH_SHORT).show();
                showGenderChoiceDialog();
            }
        });
    }

    /**
     * 성별 option 값 구성
     *
     * @return
     */
    private List<GenderOption> loadGenderOptions() {
        List<GenderOption> result = new ArrayList<>();
        String[] raw = getResources().getStringArray(R.array.gender);
        for (String op : raw) {
            String[] info = op.split("%");
            result.add(new GenderOption(info[1], info[0]));
        }
        return result;
    }

    /**
     * 최초 설치 프로세스 진행
     *
     * @param strGender
     */
    private void setAppInitProcess(String strGender) {
        INIT_CHECK = 1;
        progress.setProgress(3);

        // 최초 설정 여부 저장
        Locale systemLocale = getApplicationContext().getResources().getConfiguration().locale;
        String strCountry = systemLocale.getCountry();
        String strLanguage = systemLocale.getLanguage();

        HashMap<String, String> myInfo = new HashMap<String, String>();
        myInfo.put("APP_ID", Consts.IDS_PRIEFIX_UID + UUID.randomUUID().toString());
        myInfo.put("GENDER", strGender);
        myInfo.put("COUNTRY", strCountry);
        myInfo.put("LANG", strLanguage);
        myInfo.put("INITIALIZE", "Y");
        AccountInfo.setAccountInfo(getApplicationContext(), myInfo);

        // 서버에 계정 생성 호출
        httpConnectService.appInfoInit(AccountInfo.getAccountInfo(getApplicationContext())).enqueue(new Callback<DataVo>() {
            @Override
            public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
                if (response.isSuccessful()) {
                    DataVo data = response.body();
                    if (data != null) {
                        if ("S".equals(data.getRESULT_CODE())) {
                            getNoticeProcess();
                        } else {
                            Logger.e("appInfoInit Fail : " + data.getRESULT_MESSAGE());
                            Toast.makeText(MainActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                } else {
                    Logger.e("appInfoInit Fail : " + response.isSuccessful());
                    Toast.makeText(MainActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    finish();
                }
                progress.setProgress(4);
            }

            @Override
            public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
                Logger.e("appInfoInit Error : " + t.getMessage());
                Toast.makeText(MainActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    /**
     * 공지 사항 조회 처리
     */
    private void getNoticeProcess() {
        // random
        Random rnd = new Random();
        progress.setProgress(rnd.nextInt(7) + 1);

        SharedPreferences prefx = this.getSharedPreferences("NOTICE_ALL_FLAG", MODE_PRIVATE);
        long lastTime = Long.parseLong(prefx.getString("LAST_ACCESS_TIME", "1234567890"));
        long curTime = System.currentTimeMillis();
        long diffTime = (curTime - lastTime) / (1000 * 60 * 60);

        if (diffTime >= Consts.APP_ACCESS_TIME_PERIOD) {

            dataClean();

            httpConnectService.appNotice(AccountInfo.getAccountInfo(getApplicationContext())).enqueue(new Callback<DataVo>() {
                @Override
                public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
                    if (response.isSuccessful()) {
                        DataVo data = response.body();
                        if (data != null) {
                            // NOTICE 조회 시간 갱신
                            SharedPreferences.Editor editor = prefx.edit();
                            editor.putString("LAST_ACCESS_TIME", curTime + "");
                            editor.apply();
                            showInfoDialog(data.getRESULT_DATA());
                        } else {
                            showInfoDialog(null);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
                    Logger.e("getNoticeProcess Error : " + t.getMessage());
                    Toast.makeText(MainActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else {
            showInfoDialog(null);
        }
    }

    /**
     * Old Data Clean
     */
    private void dataClean() {

        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                String cttm = (System.currentTimeMillis() - (Consts.DATA_CLEAN_DAY * 24 * 60 * 60 * 1000)) + "";

                DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                        .AppTalkThreadDao().deleteByCttm(Consts.MESSAGE_STATUS_FIRST, cttm);

                DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                        .AppTalkMainDao().deleteByCttm(Consts.MESSAGE_STATUS_FIRST, cttm);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //SKIP
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }

    /**
     * 공지 사항 팝업 출력
     *
     * @param params
     */
    private void showInfoDialog(List<HashMap<String, Object>> params) {
        progress.setProgress(10);

        CharSequence cs = new StringBuffer(getString(R.string.notice_text));
        int noticeId = 1; // FIX

        SharedPreferences prefx = this.getSharedPreferences("ld_dont_show", MODE_PRIVATE);

        if (!prefx.getBoolean(String.valueOf(noticeId), false)) {
            Dialog dlg = new LovelyInfoDialog(this)
                    .setTopColorRes(R.color.baseColor)
                    .setIcon(R.drawable.welcome)
                    .setInstanceStateHandler(1, saveStateHandler)
                    .setNotShowAgainOptionEnabled(noticeId)
                    .setNotShowAgainOptionChecked(true)
                    .setTitle(R.string.info_title)
                    .setMessage(cs)
                    .show();

            dlg.setOnDismissListener(dialog -> {
                initActivity();
            });
        } else {
            initActivity();
        }
    }

    /*
        // NOTICE SKIP
        private void showInfoDialog(List<HashMap<String, Object>> params) {
            progress.setProgress(10);

            if (params != null && !params.isEmpty() && params.size() > 0 && !params.get(0).containsKey("EMPTY_DATA")) {
                CharSequence cs = new StringBuffer((String) params.get(0).get("NOTICE_TXT"));
                int noticeId = Integer.parseInt((String) params.get(0).get("NOTICE_ID"));

                SharedPreferences prefx = this.getSharedPreferences("ld_dont_show", this.MODE_PRIVATE);

                if (!prefx.getBoolean(String.valueOf(noticeId), false)) {
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
                } else {
                    initActivity();
                }
            } else {
                initActivity();
            }
        }
    */

    /**
     * Lock 화면으로 이동 처리
     */
    private void initActivity() {
        Intent intent = new Intent(this, LockActivity.class);
        startActivity(intent);
        finish();
    }
}
