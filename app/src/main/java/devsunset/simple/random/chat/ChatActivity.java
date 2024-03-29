/*
 * @(#)ChatActivity.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.orhanobut.logger.Logger;
import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import devsunset.simple.random.chat.modules.accountservice.AccountInfo;
import devsunset.simple.random.chat.modules.dataservice.AppTalkThread;
import devsunset.simple.random.chat.modules.dataservice.DatabaseClient;
import devsunset.simple.random.chat.modules.etcservice.CustomDialog;
import devsunset.simple.random.chat.modules.etcservice.MessageDetailAdapter;
import devsunset.simple.random.chat.modules.etcservice.MessageItem;
import devsunset.simple.random.chat.modules.eventbusservice.BusProvider;
import devsunset.simple.random.chat.modules.httpservice.DataVo;
import devsunset.simple.random.chat.modules.httpservice.HttpConnectClient;
import devsunset.simple.random.chat.modules.httpservice.HttpConnectService;
import devsunset.simple.random.chat.modules.utilservice.Consts;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * <PRE>
 * SimpleRandomChat ChatActivity
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */
public class ChatActivity extends Activity {

    public static int BLACK_LIST = 1;
    public static int BYE_SEND = 2;
    public static int HIDE = 3;
    private static String APP_ID = "";
    private static String ATX_ID = "";
    private static String ATX_STATUS = "";
    private static String MY_LANG = "";
    private static String TO_APP_KEY = "";
    private static boolean EXECUTE_ACTION = false;
    private static String strCountry = "";
    @BindView(R.id.btnBlackListSend)
    TextView btnBlackListSend;
    @BindView(R.id.btnHide)
    TextView btnHide;
    @BindView(R.id.replyArea)
    LinearLayout replyArea;
    @BindView(R.id.reply_message)
    EditText reply_message;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.korea_warnning_desc)
    LinearLayout korea_warnning_desc;
    private HttpConnectService httpConnectService = null;
    private RecyclerView.LayoutManager mLayoutManager;
    private KProgressHUD hud;
    // Ads Full Screen
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        ButterKnife.bind(this);

        //screen capture disable
        if (Consts.SCREEN_CAPTURE_DISABLE && Build.VERSION.SDK_INT >= 11) {
            getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE, android.view.WindowManager.LayoutParams.FLAG_SECURE);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        //Event Bus 등록
        BusProvider.getInstance().register(this);

        Locale systemLocale = getApplicationContext().getResources().getConfiguration().locale;
        strCountry = systemLocale.getCountry();

        if ("KR".equalsIgnoreCase(strCountry)) {
            korea_warnning_desc.setVisibility(View.VISIBLE);
        }

        httpConnectService = HttpConnectClient.getClient().create(HttpConnectService.class);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        APP_ID = AccountInfo.getAccountInfo(this).get("APP_ID");
        MY_LANG = AccountInfo.getAccountInfo(this).get("LANG");

        Intent intent = getIntent();
        ATX_ID = intent.getStringExtra("ATX_ID");
        ATX_STATUS = intent.getStringExtra("ATX_STATUS");
        TO_APP_KEY = intent.getStringExtra("REPLY_APP_KEY");

        getDatabase(ATX_ID);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, Consts.ADS_APP_ID);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(Consts.ADS_FULL_SCREEN_ID);
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice(Consts.ADS_TEST_ID).addTestDevice(Consts.ADS_TEST_SUB_ID).build());
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, Consts.ADS_APP_ID);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(Consts.ADS_FULL_SCREEN_ID);
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice(Consts.ADS_TEST_ID).addTestDevice(Consts.ADS_TEST_SUB_ID).build());
    }

    /**
     * message list search
     *
     * @param atxId
     */
    private void getDatabase(String atxId) {
        class GetTasks extends AsyncTask<Void, Void, List<AppTalkThread>> {
            @Override
            protected List<AppTalkThread> doInBackground(Void... voids) {
                List<AppTalkThread> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDataBase()
                        .AppTalkThreadDao()
                        .findByAtxId(atxId);
                return taskList;
            }

            @Override
            protected void onPostExecute(List<AppTalkThread> appTalkThread) {
                ArrayList<MessageItem> messageArrayList = new ArrayList<MessageItem>();
                if (appTalkThread != null && !appTalkThread.isEmpty()) {
                    for (int i = 0; i < appTalkThread.size(); i++) {
                        MessageItem mi = new MessageItem();
                        // ATX_ID
                        mi.setATX_ID(appTalkThread.get(i).getATX_ID());
                        // TALK_TEXT
                        mi.setTALK_TEXT(appTalkThread.get(i).getTALK_TEXT());
                        // TALK_TEXT_VOICE
                        mi.setTALK_TEXT_VOICE(appTalkThread.get(i).getTALK_TEXT_VOICE());
                        // TALK_TEXT_IMAGE
                        mi.setTALK_TEXT_IMAGE(appTalkThread.get(i).getTALK_TEXT_IMAGE());
                        // COUNTRY_NAME
                        mi.setCOUNTRY_NAME(appTalkThread.get(i).getTALK_COUNTRY_NAME());
                        // TALK_LANG
                        mi.setTALK_LANG(appTalkThread.get(i).getTALK_LANG());
                        // TALK_TYPE
                        mi.setTALK_TYPE((appTalkThread.get(i).getTALK_TYPE()));
                        // TALK_TARGET
                        if (APP_ID.equals(appTalkThread.get(i).getTALK_APP_ID())) {
                            mi.setTALK_TARGET("Me");
                        } else {
                            mi.setTALK_TARGET("");
                        }
                        // ATX_LOCAL_TIME
                        SimpleDateFormat dayTime = new SimpleDateFormat("MM-dd HH:mm");
                        String str = dayTime.format(new Date(Long.parseLong(appTalkThread.get(i).getTALK_LOCAL_TIME())));
                        mi.setATX_LOCAL_TIME(str);
                        // icon
                        if ("M".equals(appTalkThread.get(i).getTALK_GENDER())) {
                            if (APP_ID.equals(appTalkThread.get(i).getTALK_APP_ID())) {
                                mi.setDrawableId(R.drawable.man_me);
                            } else {
                                mi.setDrawableId(R.drawable.man);
                            }
                        } else {
                            if (APP_ID.equals(appTalkThread.get(i).getTALK_APP_ID())) {
                                mi.setDrawableId(R.drawable.woman_me);
                            } else {
                                mi.setDrawableId(R.drawable.woman);
                            }
                        }
                        messageArrayList.add(mi);
                    }

                    // 마지막 대화가 자신인 경우 숨기기
                    if (APP_ID.equals(appTalkThread.get(appTalkThread.size() - 1).getTALK_APP_ID())) {
                        replyArea.setVisibility(View.GONE);
                        btnHide.setVisibility(View.VISIBLE);
                    } else {
                        replyArea.setVisibility(View.VISIBLE);
                        btnHide.setVisibility(View.GONE);
                    }

                    if (Consts.MESSAGE_STATUS_DELETE.equals(ATX_STATUS)) {
                        replyArea.setVisibility(View.GONE);
                        btnHide.setVisibility(View.GONE);
                    }

                    if (appTalkThread.size() == 1 && APP_ID.equals(appTalkThread.get(0).getTALK_APP_ID())) {
                        replyArea.setVisibility(View.GONE);
                        btnBlackListSend.setVisibility(View.GONE);
                    } else if ("KR".equalsIgnoreCase(strCountry) && appTalkThread.size() >= 1) {
                        btnBlackListSend.setVisibility(View.VISIBLE);
                    }
                } else {
                    MessageItem mi = new MessageItem();
                    mi.setDrawableId(R.drawable.empty_message);
                    mi.setTALK_TEXT(getString(R.string.empty_message));
                    mi.setTALK_TARGET("NO_DATA");
                    messageArrayList.add(mi);

                    replyArea.setVisibility(View.GONE);
                    btnHide.setVisibility(View.GONE);
                    btnBlackListSend.setVisibility(View.GONE);
                }
                MessageDetailAdapter myAdapter = new MessageDetailAdapter(messageArrayList, MY_LANG);
                mRecyclerView.setAdapter(myAdapter);
                mRecyclerView.scrollToPosition(myAdapter.getItemCount() - 1);

            }
        }
        GetTasks gt = new GetTasks();
        gt.execute();
    }

    /**
     * black List send button click
     */
    @OnClick(R.id.btnBlackListSend)
    void onBtnBlackListClicked() {
        CustomDialog customDialog = new CustomDialog(ChatActivity.this);
        customDialog.callFunction(getString(R.string.black_list_message), getString(R.string.black_list_message_desc), BLACK_LIST, this);
    }

    /**
     * Black List 등록
     */
    private void blackListProcess() {

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(getResources().getColor(R.color.progress))
                .setAnimationSpeed(2)
                .show();

        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                        .AppTalkThreadDao().deleteByAtxId(ATX_ID);

                DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                        .AppTalkMainDao().deleteByAtxId(ATX_ID);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                HashMap<String, String> account = AccountInfo.getAccountInfo(getApplicationContext());
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("BLA_ID", Consts.IDS_PRIEFIX_BLA + UUID.randomUUID().toString());
                params.put("ATX_ID", ATX_ID);
                params.put("TALK_APP_ID", account.get("APP_ID"));
                params.put("TO_APP_KEY", TO_APP_KEY);

                httpConnectService.requstBlackList(params).enqueue(new Callback<DataVo>() {
                    @Override
                    public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
                        if (response.isSuccessful()) {
                            DataVo data = response.body();
                            if ("S".equals(data.getRESULT_CODE())) {
                                Logger.i("blackListSendProcess Success");
                            } else {
                                Logger.e("blackListSendProcess Fail");
                            }
                        } else {
                            Logger.e("blackListSendProcess Fail");
                        }
                        hud.dismiss();
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
                        hud.dismiss();
                        Logger.e("blackListSendProcess Fail");
                        finish();
                    }
                });
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }

    /**
     * Bye send button click
     */
    @OnClick(R.id.btnByeSend)
    void onBtnByeSendClicked() {

        if ("Y".equals(AccountInfo.getAccountInfo(getApplicationContext()).get("SET_BYE_CONFIRM_YN"))) {
            byeSendProcess();
        } else {
            CustomDialog customDialog = new CustomDialog(ChatActivity.this);
            customDialog.callFunction(getString(R.string.bye_message), getString(R.string.bye_message_desc), BYE_SEND, this);
        }
    }

    /**
     * 메시지 삭제
     */
    private void byeSendProcess() {

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(getResources().getColor(R.color.progress))
                .setAnimationSpeed(2)
                .show();

        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                        .AppTalkThreadDao().deleteByAtxId(ATX_ID);

                DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                        .AppTalkMainDao().deleteByAtxId(ATX_ID);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                HashMap<String, String> account = AccountInfo.getAccountInfo(getApplicationContext());
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("ATX_ID", ATX_ID);
                params.put("TALK_APP_ID", account.get("APP_ID"));
                params.put("TO_APP_KEY", TO_APP_KEY);

                httpConnectService.byeMessage(params).enqueue(new Callback<DataVo>() {
                    @Override
                    public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
                        if (response.isSuccessful()) {
                            DataVo data = response.body();
                            if ("S".equals(data.getRESULT_CODE())) {
                                Logger.i("byeSendProcess Success");
                            } else {
                                Logger.e("byeSendProcess Fail");
                            }
                        } else {
                            Logger.e("byeSendProcess Fail");
                        }
                        hud.dismiss();
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
                        hud.dismiss();
                        Logger.e("byeSendProcess Fail");
                        finish();
                    }
                });
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }

    /**
     * Hide button click
     */
    @OnClick(R.id.btnHide)
    void onBtnHideClicked() {
        if ("Y".equals(AccountInfo.getAccountInfo(getApplicationContext()).get("SET_BYE_CONFIRM_YN"))) {
            hideProcess();
        } else {
            CustomDialog customDialog = new CustomDialog(ChatActivity.this);
            customDialog.callFunction(getString(R.string.hide_message), getString(R.string.hide_message_desc), HIDE, this);
        }
    }

    /**
     * 메시지 숨기기
     */
    private void hideProcess() {

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(getResources().getColor(R.color.progress))
                .setAnimationSpeed(2)
                .show();

        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                        .AppTalkMainDao().updateStatus(Consts.MESSAGE_STATUS_HIDE, ATX_ID);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                hud.dismiss();
                finish();
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }

    /**
     * Custom Dialog return hahdler
     *
     * @param type
     */
    public void callReturn(int type) {
        if (type == BLACK_LIST) {
            blackListProcess();
        } else if (type == BYE_SEND) {
            byeSendProcess();
        } else if (type == HIDE) {
            hideProcess();
        }
    }

    /**
     * reply message send button click
     */
    @OnClick(R.id.btnReplyMessageSend)
    void onBtnReplyMessageSendClicked() {

        String message = reply_message.getText().toString();

        if (reply_message.getText().toString().trim().length() == 0) {
            Toast.makeText(this, getString(R.string.send_input), Toast.LENGTH_SHORT).show();
            return;
        } else {
            reply_message.setText("");
        }

        // 메시지 내용 최대 500자 제한
        if (message.length() >= 500) {
            message = message.substring(0, 500) + " ...";
        }

        if (EXECUTE_ACTION) {
            return;
        }

        EXECUTE_ACTION = true;

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(getResources().getColor(R.color.progress))
                .setAnimationSpeed(2)
                .show();

        HashMap<String, String> account = AccountInfo.getAccountInfo(this);
        String ctm = System.currentTimeMillis() + "";
        String talkId = UUID.randomUUID().toString();

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("ATX_ID", ATX_ID);
        params.put("TALK_APP_ID", account.get("APP_ID"));
        params.put("ATX_LOCAL_TIME", ctm);
        params.put("TALK_ID", Consts.IDS_PRIEFIX_TTD + talkId);
        params.put("TALK_COUNTRY", account.get("COUNTRY"));
        params.put("TALK_COUNTRY_NAME", account.get("COUNTRY_NAME"));
        params.put("TALK_GENDER", account.get("GENDER"));
        params.put("TALK_LANG", account.get("LANG"));
        params.put("TALK_TEXT", message);
        params.put("TALK_TEXT_IMAGE", "");
        params.put("TALK_TEXT_VOICE", "");
        params.put("TALK_TYPE", Consts.MESSAGE_TYPE_TEXT);

        params.put("APP_ID", account.get("APP_ID"));
        params.put("TO_APP_KEY", TO_APP_KEY);

        httpConnectService.replyMessage(params).enqueue(new Callback<DataVo>() {
            @Override
            public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
                if (response.isSuccessful()) {
                    DataVo data = response.body();
                    if (data != null) {
                        if ("S".equals(data.getRESULT_CODE())) {
                            AppTalkThread att = new AppTalkThread();
                            att.setATX_ID(ATX_ID);
                            att.setTALK_APP_ID(account.get("APP_ID"));
                            att.setTALK_LOCAL_TIME(ctm);
                            att.setTALK_ID("TTD_" + talkId);
                            att.setTALK_COUNTRY(account.get("COUNTRY"));
                            att.setTALK_COUNTRY_NAME(account.get("COUNTRY_NAME"));
                            att.setTALK_GENDER(account.get("GENDER"));
                            att.setTALK_LANG(account.get("LANG"));
                            att.setTALK_TEXT((String) params.get("TALK_TEXT"));
                            att.setTALK_TYPE(Consts.MESSAGE_TYPE_TEXT);
                            replyMessage(att);
                            Logger.i("replyMessage Success");
                        } else {
                            EXECUTE_ACTION = false;
                            hud.dismiss();
                            Logger.e("replyMessage Fail");
                            Toast.makeText(getApplicationContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
//                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                            inputMethodManager.hideSoftInputFromWindow(reply_message.getWindowToken(), 0);
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
                EXECUTE_ACTION = false;
                hud.dismiss();
                Logger.e("replyMessage Error : " + t.getMessage());
                Toast.makeText(getApplicationContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(reply_message.getWindowToken(), 0);
                finish();
            }
        });
    }

    /**
     * reply message db insert
     *
     * @param att
     */
    private void replyMessage(AppTalkThread att) {
        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {

                List<AppTalkThread> existDataSubCheck = DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                        .AppTalkThreadDao().findByTalkId(att.getTALK_ID());

                // TALK_ID 값이 이미 존재시 SKIP
                if (existDataSubCheck != null && !existDataSubCheck.isEmpty()) {
                    Logger.d("TALK_ID EXIST Skip...");
                } else {
                    DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                            .AppTalkMainDao().updateReplySend(Consts.MESSAGE_STATUS_REPLY, att.getTALK_LOCAL_TIME()
                            , att.getTALK_APP_ID(), att.getTALK_TEXT(), att.getTALK_TYPE(), att.getATX_ID());

                    DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                            .AppTalkThreadDao().insert(att);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                EXECUTE_ACTION = false;
                hud.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.send_result), Toast.LENGTH_SHORT).show();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(reply_message.getWindowToken(), 0);
                finish();
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }

    /**
     * attach button
     */
    @OnClick(R.id.btnAttach)
    void onBtnAttachClicked() {
        // 권한 획득
        // Multiple permissions:
        String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {


                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();

                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            // Code to be executed when an ad finishes loading.
                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            // Code to be executed when an ad request fails.
                            Intent intent = new Intent(getApplicationContext(), ChatUploadActivity.class);
                            intent.putExtra("ATX_ID", ATX_ID);
                            intent.putExtra("TO_APP_KEY", TO_APP_KEY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        }

                        @Override
                        public void onAdOpened() {
                            // Code to be executed when the ad is displayed.
                        }

                        @Override
                        public void onAdLeftApplication() {
                            // Code to be executed when the user has left the app.
                        }

                        @Override
                        public void onAdClosed() {
                            // Code to be executed when the interstitial ad is closed.
                            Intent intent = new Intent(getApplicationContext(), ChatUploadActivity.class);
                            intent.putExtra("ATX_ID", ATX_ID);
                            intent.putExtra("TO_APP_KEY", TO_APP_KEY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        }
                    });

                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                finish();
            }
        });
    }


    @OnClick(R.id.btnBack)
    void onBtnBackClicked() {
        finish();
    }

    @OnClick(R.id.tvBack)
    void onTvBackClicked() {
        finish();
    }

    @Override
    public void onDestroy() {
        // Always unregister when an object no longer should be on the bus.a
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    /**
     * Push 수신 시 목록 갱신 처리
     *
     * @param event
     */
    @Subscribe
    public void receiveMessageReloadMessageList(String event) {
        Logger.i("receiveMessageReloadMessageList process... ");
        getDatabase(ATX_ID);
    }
}
