/*
 * @(#)MyFirebaseMessagingService.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat.modules.fcmservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.orhanobut.logger.Logger;
import com.tfb.fbtoast.FBCustomToast;

import java.util.HashMap;
import java.util.List;

import devsunset.simple.random.chat.LockActivity;
import devsunset.simple.random.chat.R;
import devsunset.simple.random.chat.modules.accountservice.AccountInfo;
import devsunset.simple.random.chat.modules.dataservice.AppTalkMain;
import devsunset.simple.random.chat.modules.dataservice.AppTalkThread;
import devsunset.simple.random.chat.modules.dataservice.DatabaseClient;
import devsunset.simple.random.chat.modules.eventbusservice.BusProvider;
import devsunset.simple.random.chat.modules.utilservice.Consts;
import devsunset.simple.random.chat.modules.utilservice.Util;

//import com.firebase.jobdispatcher.FirebaseJobDispatcher;
//import com.firebase.jobdispatcher.GooglePlayDriver;
//import com.firebase.jobdispatcher.Job;

/**
 * <PRE>
 * SimpleRandomChat MyFirebaseMessagingService
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // (developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        // Logger.d("From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        /*
            if (remoteMessage.getData().size() > 0) {
                // Logger.d("Message data payload: " + remoteMessage.getData());
                // Check if data needs to be processed by long running job
                if (true) {
                    // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                    scheduleJob();
                } else {
                    // Handle message within 10 seconds
                    handleNow();
                }
            }
        */

        // Check if message contains a notification payload.
        if (remoteMessage.getData() != null) {

            if (remoteMessage.getData().get("ATX_STATUS") != null && !"".equals(remoteMessage.getData().get("ATX_STATUS"))) {

                String ctm = System.currentTimeMillis() + "";

                // 최초 메시지 상태시 처리 (F : FROM , F->P  : TO )
                if ("F".equals(remoteMessage.getData().get("ATX_STATUS"))) {
                    String talkStatus;
                    HashMap<String, String> account = AccountInfo.getAccountInfo(getApplicationContext());

                    AppTalkMain atm = new AppTalkMain();
                    atm.setATX_ID(remoteMessage.getData().get("ATX_ID"));
                    atm.setATX_LOCAL_TIME(ctm);
                    if (account.get("APP_ID").equals(remoteMessage.getData().get("FROM_APP_ID"))) {
                        atm.setATX_STATUS(Consts.MESSAGE_STATUS_FIRST);
                        talkStatus = Consts.MESSAGE_STATUS_FIRST;
                    } else {
                        atm.setATX_STATUS(Consts.MESSAGE_STATUS_PROCEDDING);
                        talkStatus = Consts.MESSAGE_STATUS_PROCEDDING;
                    }
                    atm.setFROM_APP_ID(remoteMessage.getData().get("FROM_APP_ID"));
                    atm.setFROM_APP_KEY(remoteMessage.getData().get("FROM_APP_KEY"));
                    atm.setFROM_COUNTRY(remoteMessage.getData().get("FROM_COUNTRY"));
                    atm.setFROM_COUNTRY_NAME(remoteMessage.getData().get("FROM_COUNTRY_NAME"));
                    atm.setFROM_GENDER(remoteMessage.getData().get("FROM_GENDER"));
                    atm.setFROM_LANG(remoteMessage.getData().get("FROM_LANG"));
                    atm.setTALK_APP_ID(remoteMessage.getData().get("TALK_APP_ID"));
                    atm.setTALK_TEXT(remoteMessage.getData().get("TALK_TEXT"));
                    atm.setTALK_TYPE(remoteMessage.getData().get("TALK_TYPE"));
                    atm.setTO_APP_ID(remoteMessage.getData().get("TO_APP_ID"));
                    atm.setTO_APP_KEY(remoteMessage.getData().get("TO_APP_KEY"));
                    atm.setTO_COUNTRY(remoteMessage.getData().get("TO_COUNTRY"));
                    atm.setTO_COUNTRY_NAME(remoteMessage.getData().get("TO_COUNTRY_NAME"));
                    atm.setTO_GENDER(remoteMessage.getData().get("TO_GENDER"));
                    atm.setTO_LANG(remoteMessage.getData().get("TO_LANG"));

                    AppTalkThread att = new AppTalkThread();
                    att.setATX_ID(remoteMessage.getData().get("ATX_ID"));
                    att.setTALK_APP_ID(remoteMessage.getData().get("FROM_APP_ID"));
                    att.setTALK_LOCAL_TIME(ctm);
                    att.setTALK_ID(remoteMessage.getData().get("TALK_ID"));
                    att.setTALK_COUNTRY(remoteMessage.getData().get("FROM_COUNTRY"));
                    att.setTALK_COUNTRY_NAME(remoteMessage.getData().get("FROM_COUNTRY_NAME"));
                    att.setTALK_GENDER(remoteMessage.getData().get("FROM_GENDER"));
                    att.setTALK_LANG(remoteMessage.getData().get("FROM_LANG"));
                    att.setTALK_TEXT(remoteMessage.getData().get("TALK_TEXT"));
                    att.setTALK_TYPE(remoteMessage.getData().get("TALK_TYPE"));

                    saveFirstMessage(atm, att, "", talkStatus);

                    // Reply Message
                } else if (Consts.MESSAGE_STATUS_PROCEDDING.equals(remoteMessage.getData().get("ATX_STATUS"))) {

                    AppTalkThread att = new AppTalkThread();
                    att.setATX_ID(remoteMessage.getData().get("ATX_ID"));
                    att.setTALK_APP_ID(remoteMessage.getData().get("TALK_APP_ID"));
                    att.setTALK_LOCAL_TIME(ctm);
                    att.setTALK_ID(remoteMessage.getData().get("TALK_ID"));
                    att.setTALK_COUNTRY(remoteMessage.getData().get("TALK_COUNTRY"));
                    att.setTALK_COUNTRY_NAME(remoteMessage.getData().get("TALK_COUNTRY_NAME"));
                    att.setTALK_GENDER(remoteMessage.getData().get("TALK_GENDER"));
                    att.setTALK_LANG(remoteMessage.getData().get("TALK_LANG"));
                    att.setTALK_TEXT(remoteMessage.getData().get("TALK_TEXT"));
                    att.setTALK_TEXT_IMAGE(remoteMessage.getData().get("TALK_TEXT_IMAGE"));
                    att.setTALK_TEXT_VOICE(remoteMessage.getData().get("TALK_TEXT_VOICE"));
                    att.setTALK_TYPE(remoteMessage.getData().get("TALK_TYPE"));

                    saveReplyMessage(att);

                    // Delete Message
                } else if (Consts.MESSAGE_STATUS_DELETE.equals(remoteMessage.getData().get("ATX_STATUS"))) {

                    saveDeleteMessage(remoteMessage.getData().get("ATX_ID"));
                }
            } else {
                sendNotification(remoteMessage.getNotification().getBody());
            }
        } else {
            sendNotification(remoteMessage.getNotification().getBody());
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Logger.d("Refreshed token: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
        Logger.d("Long lived task is done.");
        // [START dispatch_job]
        /*
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
        */
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Logger.d("Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        HashMap<String, String> myInfo = new HashMap<String, String>();
        myInfo.put("APP_KEY", token);
        AccountInfo.setAccountInfo(this, myInfo);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    @SuppressWarnings("deprecation")
    private void sendNotification(String messageBody) {
        String channelId = getString(R.string.default_notification_channel_id);

        Intent intent = new Intent(this, LockActivity.class);
        intent.putExtra("PUSH_CALL_FLAG","Y");
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        Bitmap mLargeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_ic_notification);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if ("Y".equals(AccountInfo.getAccountInfo(getApplicationContext()).get("SET_ALARM_YN"))) {
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.ic_stat_ic_notification)
                            .setLargeIcon(mLargeIcon)
                            .setContentTitle(getString(R.string.default_received_message))
                            .setContentText(messageBody)
                            .setSound(defaultSoundUri)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setVibrate(new long[]{0L})
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        channelId,
                        NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{0L});
            }

            notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());

        } else if ("Y".equals(AccountInfo.getAccountInfo(getApplicationContext()).get("SET_ALARM_NOTI_YN"))) {
            Notification notification;
            NotificationCompat.Builder mBuilder;
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH);
                //Disabling vibration!
                notificationChannel.setVibrationPattern(new long[]{0});
                notificationChannel.enableVibration(true);
                notificationChannel.setSound(null, null);
                notificationManager.createNotificationChannel(notificationChannel);
                mBuilder = new NotificationCompat.Builder(this, channelId);
            } else {
                mBuilder = new NotificationCompat.Builder(this);
                mBuilder.setVibrate(new long[]{0L});
                mBuilder.setSound(null, 1);
            }

            mBuilder.setContentTitle(getString(R.string.default_received_message))
                    .setContentText(messageBody)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setLargeIcon(mLargeIcon);

            notification = mBuilder.build();
            notificationManager.notify(1, notification);
        }
    }

    /**
     * 최초 메시지 처리
     *
     * @param atm
     * @param att
     * @param message
     * @param talkStatus
     */
    private void saveFirstMessage(AppTalkMain atm, AppTalkThread att, String message, String talkStatus) {
        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                List<AppTalkMain> existDataCheck = DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                        .AppTalkMainDao().findByAtxId(atm.getATX_ID());

                // 방어코딩 서버에서 FROM - TO 동일한지 확인할수 있는 값이 없는 관계로 단말에서 예외처리
                if (existDataCheck != null && !existDataCheck.isEmpty()) {
                    Logger.d("First Message FROM == TO Skip...");
                } else {
                    DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                            .AppTalkMainDao().insert(atm);

                    DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                            .AppTalkThreadDao().insert(att);

                    if (Util.isAppIsInBackground(getApplicationContext()) && Consts.MESSAGE_STATUS_PROCEDDING.equals(talkStatus) && ("Y".equals(AccountInfo.getAccountInfo(getApplicationContext()).get("SET_ALARM_YN"))
                            || "Y".equals(AccountInfo.getAccountInfo(getApplicationContext()).get("SET_ALARM_NOTI_YN")))) {
                        sendNotification(""); // message
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (Consts.MESSAGE_STATUS_PROCEDDING.equals(talkStatus) &&
                        (   "Y".equals( AccountInfo.getAccountInfo(getApplicationContext()).get("SET_ALARM_YN"))
                         || "Y".equals( AccountInfo.getAccountInfo(getApplicationContext()).get("SET_ALARM_NOTI_YN"))
                         || "Y".equals( AccountInfo.getAccountInfo(getApplicationContext()).get("SET_ALARM_POPUP_YN"))
                        )){
                    FBCustomToast fbCustomToast = new FBCustomToast(getApplicationContext());
                    fbCustomToast.setMsg(getString(R.string.default_received_message));
                    fbCustomToast.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_launcher));
                    fbCustomToast.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_gradient));
                    fbCustomToast.setGravity(1);
                    fbCustomToast.show();
                }

                //EventBus Call
                BusProvider.getInstance().post("PUSH_RECEIVE");
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }

    /**
     * 답변 메시지 처리
     *
     * @param att
     */
    private void saveReplyMessage(AppTalkThread att) {
        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                List<AppTalkMain> existDataCheck = DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                        .AppTalkMainDao().findByAtxId(att.getATX_ID());

                // 단말에 해당 값이 존재 하는지 체크
                if (existDataCheck != null && !existDataCheck.isEmpty()) {

                    List<AppTalkThread> existDataSubCheck = DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                            .AppTalkThreadDao().findByTalkId(att.getTALK_ID());

                    // TALK_ID 값이 이미 존재시 SKIP
                    if (existDataSubCheck != null && !existDataSubCheck.isEmpty()) {
                        Logger.d("TALK_ID EXIST Skip...");
                    } else {
                        DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                                .AppTalkMainDao().updateReplySend(Consts.MESSAGE_STATUS_PROCEDDING, att.getTALK_LOCAL_TIME()
                                , att.getTALK_APP_ID(), att.getTALK_TEXT(), att.getTALK_TYPE(), att.getATX_ID());

                        DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                                .AppTalkThreadDao().insert(att);

                        if (Util.isAppIsInBackground(getApplicationContext()) && ("Y".equals(AccountInfo.getAccountInfo(getApplicationContext()).get("SET_ALARM_YN"))
                                || "Y".equals(AccountInfo.getAccountInfo(getApplicationContext()).get("SET_ALARM_NOTI_YN")))) {
                            sendNotification(""); // message
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if ((   "Y".equals( AccountInfo.getAccountInfo(getApplicationContext()).get("SET_ALARM_YN"))
                        || "Y".equals( AccountInfo.getAccountInfo(getApplicationContext()).get("SET_ALARM_NOTI_YN"))
                        || "Y".equals( AccountInfo.getAccountInfo(getApplicationContext()).get("SET_ALARM_POPUP_YN"))
                    )) {
                    FBCustomToast fbCustomToast = new FBCustomToast(getApplicationContext());
                    fbCustomToast.setMsg(getString(R.string.default_received_message));
                    fbCustomToast.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_launcher));
                    fbCustomToast.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_gradient));
                    fbCustomToast.setGravity(1);
                    fbCustomToast.show();
                }

                //EventBus Call
                BusProvider.getInstance().post("PUSH_RECEIVE");
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }


    /**
     * 삭제 메시지 처리
     *
     * @param atxId
     */
    private void saveDeleteMessage(String atxId) {
        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {

                List<AppTalkMain> existDataCheck = DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                        .AppTalkMainDao().findByAtxId(atxId);

                // 단말에 해당 값이 존재 하는지 체크
                if (existDataCheck != null && !existDataCheck.isEmpty()) {

                    if(Consts.MESSAGE_STATUS_FIRST.equals(existDataCheck.get(0).getATX_STATUS())){
                        DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                                .AppTalkThreadDao().deleteByAtxId(atxId);

                        DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                                .AppTalkMainDao().deleteByAtxId(atxId);
                    }else{
                        DatabaseClient.getInstance(getApplicationContext()).getAppDataBase()
                                .AppTalkMainDao().updateStatus(Consts.MESSAGE_STATUS_DELETE,atxId);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //EventBus Call
                BusProvider.getInstance().post("PUSH_RECEIVE");
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }
}