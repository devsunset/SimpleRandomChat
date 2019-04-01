/*
 * @(#)ChatActivity.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.orhanobut.logger.Logger;
import com.squareup.otto.Subscribe;
import com.tfb.fbtoast.FBToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import devsunset.simple.random.chat.modules.accountservice.AccountInfo;
import devsunset.simple.random.chat.modules.dataservice.AppTalkThread;
import devsunset.simple.random.chat.modules.dataservice.DatabaseClient;
import devsunset.simple.random.chat.modules.etcservice.MessageDetailAdapter;
import devsunset.simple.random.chat.modules.etcservice.MessageMainAdapter;
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

    HttpConnectService httpConnctService = null;

    @BindView(R.id.btnBlackListSend)
    Button btnBlackListSend;

    @BindView(R.id.btnHide)
    Button btnHide;

    @BindView(R.id.replyArea)
    LinearLayout replyArea;

    @BindView(R.id.reply_message)
    EditText reply_message;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    RecyclerView.LayoutManager mLayoutManager;

    public static String APP_ID = "";
    public static String ATX_ID = "";
    public static String ATX_STATUS = "";
    public static String TO_APP_KEY = "";

    NiftyDialogBuilder dialogBuilder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        ButterKnife.bind(this);

        //screen capture disable
        if (Build.VERSION.SDK_INT >= 11) {
            getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE, android.view.WindowManager.LayoutParams.FLAG_SECURE);
        }

        //Event Bus 등록
        BusProvider.getInstance().register(this);

        httpConnctService = HttpConnectClient.getClient().create(HttpConnectService.class);

        dialogBuilder = NiftyDialogBuilder.getInstance(this);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        APP_ID = AccountInfo.getAccountInfo(this).get("APP_ID");

        Intent intent = getIntent();
        ATX_ID = intent.getStringExtra("ATX_ID");
        ATX_STATUS = intent.getStringExtra("ATX_STATUS");
        TO_APP_KEY = intent.getStringExtra("REPLY_APP_KEY");

        getDatabase(ATX_ID);
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
                        // COUNTRY_NAME
                        mi.setCOUNTRY_NAME(appTalkThread.get(i).getTALK_COUNTRY_NAME());
                        // TALK_TARGET
                        if (APP_ID.equals(appTalkThread.get(i).getTALK_APP_ID())) {
                            mi.setTALK_TARGET("You");
                        } else {
                            mi.setTALK_TARGET("");
                        }
                        // ATX_LOCAL_TIME
                        SimpleDateFormat dayTime = new SimpleDateFormat("MM-dd HH:mm");
                        String str = dayTime.format(new Date(Long.parseLong(appTalkThread.get(i).getTALK_LOCAL_TIME())));
                        mi.setATX_LOCAL_TIME(str);
                        // icon
                        if ("M".equals(appTalkThread.get(i).getTALK_GENDER())) {
                            mi.setDrawableId(R.drawable.man);
                        } else {
                            mi.setDrawableId(R.drawable.woman);
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

                    if(Consts.MESSAGE_STATUS_DELETE.equals(ATX_STATUS)){
                        replyArea.setVisibility(View.GONE);
                        btnHide.setVisibility(View.GONE);
                    }

                    if(appTalkThread.size() == 1 && APP_ID.equals(appTalkThread.get(0).getTALK_APP_ID())){
                        replyArea.setVisibility(View.GONE);
                        btnBlackListSend.setVisibility(View.GONE);
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
                MessageDetailAdapter myAdapter = new MessageDetailAdapter(messageArrayList);
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
        dialogBuilder.withTitle(getString(R.string.black_list_message))
                .withTitleColor("#FFFFFF")                                  
                .withDividerColor("#11000000")                              
                .withMessage(getString(R.string.black_list_message_desc))
                .withMessageColor("#FFFFFFFF")
                .withDialogColor("#FFE74C3C")
                .withIcon(getResources().getDrawable(R.drawable.ic_launcher))
                .withDuration(700)
                .withEffect(Effectstype.Shake)                              
                .withButton1Text("CANCEL")
                .withButton2Text("OK")
                .isCancelableOnTouchOutside(true)
                //.setCustomView(R.layout.custom_view,v.getContext())
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        blackListSendProcess();
                    }
                })
                .show();
    }

    /**
     * Black List 등록
     */
    public void blackListSendProcess() {

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
                params.put("BLA_ID", Consts.IDS_PRIEFIX_BLA+UUID.randomUUID().toString());
                params.put("ATX_ID", ATX_ID);
                params.put("TALK_APP_ID", account.get("APP_ID"));
                params.put("TO_APP_KEY", TO_APP_KEY);

                httpConnctService.requstBlackList(params).enqueue(new Callback<DataVo>() {
                    @Override
                    public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
                        if (response.isSuccessful()) {
                            DataVo data = response.body();
                            if ("S".equals(data.getRESULT_CODE())) {
                                Logger.i("blackListSendProcess Success");
                            }else{
                                Logger.e("blackListSendProcess Fail");
                            }
                        }else{
                            Logger.e("blackListSendProcess Fail");
                        }

                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
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

        if("Y".equals(AccountInfo.getAccountInfo(getApplicationContext()).get("SET_BYE_CONFIRM_YN"))){
            byeSendProcess();
        }else{
            dialogBuilder.withTitle(getString(R.string.bye_message))
                    .withTitleColor("#FFFFFF")
                    .withDividerColor("#11000000")
                    .withMessage(getString(R.string.bye_message_desc))
                    .withMessageColor("#FFFFFFFF")
                    .withDialogColor("#009688")
                    .withIcon(getResources().getDrawable(R.drawable.ic_launcher))
                    .withDuration(500)
                    .withEffect(Effectstype.SlideBottom)
                    .withButton1Text("CANCEL")
                    .withButton2Text("OK")
                    .isCancelableOnTouchOutside(true)
                    //.setCustomView(R.layout.custom_view,v.getContext())
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    })
                    .setButton2Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            byeSendProcess();
                        }
                    })
                    .show();
        }
    }

    /**
     * 메세지 삭제
     */
    public void byeSendProcess() {

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

                httpConnctService.byeMessage(params).enqueue(new Callback<DataVo>() {
                    @Override
                    public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
                        if (response.isSuccessful()) {
                            DataVo data = response.body();
                            if ("S".equals(data.getRESULT_CODE())) {
                                Logger.i("byeSendProcess Success");
                            }else{
                                Logger.e("byeSendProcess Fail");
                            }
                        }else{
                            Logger.e("byeSendProcess Fail");
                        }

                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
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
        dialogBuilder.withTitle(getString(R.string.hide_message))
                .withTitleColor("#FFFFFF")                                  
                .withDividerColor("#11000000")                              
                .withMessage(getString(R.string.hide_message_desc))
                .withMessageColor("#FFFFFFFF")                                
                .withDialogColor("#3869b8")                                
                .withIcon(getResources().getDrawable(R.drawable.ic_launcher))
                .withDuration(500)
                .withEffect(Effectstype.Slidetop)                              
                .withButton1Text("CANCEL")                                   
                .withButton2Text("OK")                                       
                .isCancelableOnTouchOutside(true)                              
                //.setCustomView(R.layout.custom_view,v.getContext())       
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideProcess();
                    }
                })
                .show();
    }

    /**
     * 메세지 숨기기
     */
    public void hideProcess() {
        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                        .AppTalkMainDao().updateStatus(Consts.MESSAGE_STATUS_HIDE,ATX_ID);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                finish();
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }

    /**
     * reply message send button click
     */
    @OnClick(R.id.btnReplyMessageSend)
    void onBtnReplyMessageSendClicked() {

        String message = reply_message.getText().toString();

        if (reply_message.getText().toString().trim().length() == 0) {
            FBToast.infoToast(this, getString(R.string.sendinput), FBToast.LENGTH_SHORT);
            return;
        } else {
            reply_message.setText("");
        }

        // 메세지 내용 최대 500자 제한
        if (message.length() >= 500) {
            message = message.substring(0, 500) + " ...";
        }

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

        httpConnctService.replyMessage(params).enqueue(new Callback<DataVo>() {
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
                            Logger.e("replyMessage Fail");
                            FBToast.errorToast(getApplicationContext(), getString(R.string.networkerror), FBToast.LENGTH_SHORT);
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(reply_message.getWindowToken(), 0);
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
                Logger.e("replyMessage Error : " + t.getMessage());
                FBToast.errorToast(getApplicationContext(), getString(R.string.networkerror), FBToast.LENGTH_SHORT);
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
                FBToast.infoToast(getApplicationContext(), getString(R.string.sendresult), FBToast.LENGTH_SHORT);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(reply_message.getWindowToken(), 0);
                finish();
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }

    @Override
    public void onDestroy() {
        // Always unregister when an object no longer should be on the bus.a
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void receiveMessageReloadMessageList(String event) {
        Logger.i("receiveMessageReloadMessageList process... ");
        getDatabase(ATX_ID);
    }

}
