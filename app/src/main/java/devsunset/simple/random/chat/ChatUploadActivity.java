/*
 * @(#)ChatUploadActivity.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.orhanobut.logger.Logger;
import com.tfb.fbtoast.FBToast;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import devsunset.simple.random.chat.modules.accountservice.AccountInfo;
import devsunset.simple.random.chat.modules.dataservice.AppTalkThread;
import devsunset.simple.random.chat.modules.dataservice.DatabaseClient;
import devsunset.simple.random.chat.modules.eventbusservice.BusProvider;
import devsunset.simple.random.chat.modules.httpservice.DataVo;
import devsunset.simple.random.chat.modules.httpservice.HttpConnectClient;
import devsunset.simple.random.chat.modules.httpservice.HttpConnectService;
import devsunset.simple.random.chat.modules.utilservice.Consts;
import devsunset.simple.random.chat.modules.utilservice.Util;
import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <PRE>
 * SimpleRandomChat ChatUploadActivity
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */
public class ChatUploadActivity extends Activity implements RewardedVideoAdListener {

    @BindView(R.id.tv_audio_desc)
    TextView tv_audio_desc;

    @BindView(R.id.compressed_image)
    ImageView compressedImageView;

    @BindView(R.id.compressed_size)
    TextView compressedSizeTextView;

    @BindView(R.id.uploadbuttonarea)
    LinearLayout uploadbuttonarea;

    @BindView(R.id.loadingpage)
    LinearLayout loadingpage;

    @BindView(R.id.uploadpage)
    LinearLayout uploadpage;

    private HttpConnectService httpConnectService = null;

    private File actualImage;
    private File compressedImage;

    private static final int PICK_AUDIO_REQUEST = 0;
    private static final int PICK_IMAGE_REQUEST = 1;

    private static  String AUDIO_FILE_NAME = "";
    private static  String IMAGE_FILE_NAME = "";

    private static String ATX_ID = "";
    private static String TO_APP_KEY = "";

    private static boolean EXECUTE_ACTION = false;

    private KProgressHUD hud;

    private RewardedVideoAd mRewardedVideoAd;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_upload_activity);
        ButterKnife.bind(this);

        //screen capture disable
        if (Consts.SCREEN_CAPTURE_DISABLE && Build.VERSION.SDK_INT >= 11) {
            getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE, android.view.WindowManager.LayoutParams.FLAG_SECURE);
        }

        httpConnectService = HttpConnectClient.getClient().create(HttpConnectService.class);

        Intent intent = getIntent();
        ATX_ID = intent.getStringExtra("ATX_ID");
        TO_APP_KEY = intent.getStringExtra("TO_APP_KEY");

        String ctm = System.currentTimeMillis() + "";
        String uid = UUID.randomUUID().toString();

        AUDIO_FILE_NAME = ctm+"_"+uid+".mp3";
        IMAGE_FILE_NAME = ctm+"_"+uid+".jpg";

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }

    @Override
    public void onRewarded(RewardItem reward) {
        //Toast.makeText(this, "onRewarded! currency: " + reward.getType() + "  amount: " +
        //        reward.getAmount(), Toast.LENGTH_SHORT).show();
        initContent();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        //Toast.makeText(this, "onRewardedVideoAdLeftApplication",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        //Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
        if(uploadbuttonarea.getVisibility() == View.GONE){
            FBToast.successToast(getApplicationContext(), getString(R.string.ads_noti_msg), FBToast.LENGTH_SHORT);
            finish();
        }
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        //Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        //Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {
        //Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        //Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {
        //Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
    }

    /**
     * init content
     */
    private void initContent(){
        cleaAudio();
        clearImage();
        fileClear();
        loadingpage.setVisibility(View.GONE);
        uploadbuttonarea.setVisibility(View.VISIBLE);
        uploadpage.setVisibility(View.VISIBLE);
    }

    /**
     * directory check
     */
    private void dirCheck() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/.src_temp_tmp");
        if(!dir.exists()){
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }
    }

    @OnClick(R.id.btnAudio)
    void onBtnAudioClicked() {
        if(uploadbuttonarea.getVisibility() == View.VISIBLE){
            dirCheck();
            cleaAudio();
            clearImage();

            String filePath = Environment.getExternalStorageDirectory() + "/.src_temp_tmp/"+AUDIO_FILE_NAME;
            int color = getResources().getColor(R.color.content_bg);
            AndroidAudioRecorder.with(this)
                    // Required
                    .setFilePath(filePath)
                    .setColor(color)
                    .setRequestCode(PICK_AUDIO_REQUEST)
                    // Optional
                    .setSource(cafe.adriel.androidaudiorecorder.model.AudioSource.MIC)
                    .setChannel(AudioChannel.MONO)
                    .setSampleRate(AudioSampleRate.HZ_8000)
                    .setAutoStart(true)
                    .setKeepDisplayOn(true)
                    // Start recording
                    .record();
        }
    }

    @OnClick(R.id.btnImage)
    void onBtnImageClicked() {
        if(uploadbuttonarea.getVisibility() == View.VISIBLE){
            dirCheck();
            cleaAudio();
            clearImage();

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Audio
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                cleaAudio();
                tv_audio_desc.setText("Audio File Size : "+String.format("%s", getReadableFileSize(new File(Environment.getExternalStorageDirectory() + "/.src_temp_tmp/"+AUDIO_FILE_NAME).length())));
            } else if (resultCode == RESULT_CANCELED) {
                cleaAudio();
            }
        }

        //Image
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data == null) {
                clearImage();
                FBToast.errorToast(getApplicationContext(), getString(R.string.faile_open_picture), FBToast.LENGTH_SHORT);
                return;
            }
            try {
                clearImage();
                actualImage = Util.from(this, data.getData());
                customCompressImage();
            } catch (IOException e) {
                clearImage();
                Logger.e("IOException : " + e.getMessage());
                FBToast.errorToast(getApplicationContext(), getString(R.string.faile_open_picture), FBToast.LENGTH_SHORT);
            }
        }
    }

    /**
     * image compress
     */
    private void customCompressImage() {
        if (actualImage == null) {
            clearImage();
            FBToast.infoToast(getApplicationContext(), getString(R.string.choose_picture), FBToast.LENGTH_SHORT);
        } else {
            // Compress image in main thread using custom Compressor
            try {
                // 이미지 사이즈 줄이기
                compressedImage = new Compressor(this)
                        .setMaxWidth(640)
                        .setMaxHeight(480)
                        .setQuality(75)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .setDestinationDirectoryPath(Environment.getExternalStorageDirectory() + "/.src_temp_tmp/")
                        .compressToFile(actualImage);

                // 이름 변경
                //noinspection ResultOfMethodCallIgnored
                compressedImage.renameTo(new File(Environment.getExternalStorageDirectory() + "/.src_temp_tmp/"+IMAGE_FILE_NAME));

                setCompressedImage();

            } catch (IOException e) {
                Logger.e("IOException : " + e.getMessage());
                FBToast.errorToast(getApplicationContext(), getString(R.string.faile_open_picture), FBToast.LENGTH_SHORT);
            }
        }
    }

    /**
     * audio init
     */
    private void cleaAudio() {
        tv_audio_desc.setText("Audio File Size : 0 B");
    }

    /**
     * image init
     */
    private void clearImage() {
        compressedImageView.setImageDrawable(null);
        compressedSizeTextView.setText("Image File Size : 0 B");
    }

    /**
     * image view
     */
    private void setCompressedImage() {
        compressedImageView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/.src_temp_tmp/"+IMAGE_FILE_NAME));
        compressedSizeTextView.setText(String.format("Image File Size : %s", getReadableFileSize(new File(Environment.getExternalStorageDirectory() + "/.src_temp_tmp/"+IMAGE_FILE_NAME).length())));
    }

    /**
     * get file data size
     * @param size
     * @return
     */
    private String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * temp file clear
     */
    private void fileClear(){
        dirCheck();

        File file = new File(Environment.getExternalStorageDirectory() + "/.src_temp_tmp");
        File[] files = file.listFiles();
        if(files !=null && files.length > 0){
            for( int i=0; i<files.length; i++){
                //noinspection ResultOfMethodCallIgnored
                files[i].delete();
            }
        }
    }

    @OnClick(R.id.btnCancel)
    void onBtnCancelClicked() {
            finish();
    }

    @OnClick(R.id.btnReplyAudioImageMessageSend)
    void onBtnReplyAudioImageMessageSendClicked() {

        // 파일 선택 여부 체크
        if("Audio File Size : 0 B".equals(tv_audio_desc.getText().toString()) && "Image File Size : 0 B".equals(compressedSizeTextView.getText().toString())){
            FBToast.infoToast(getApplicationContext(), getString(R.string.choose_file), FBToast.LENGTH_SHORT);
            return;
        }

        // Audio or Image 여부 체크
        boolean audioCheck;
        String childPath;

        if(!"Audio File Size : 0 B".equals(tv_audio_desc.getText().toString())){
            audioCheck = true;
            childPath = "audio";
        }else{
            audioCheck = false;
            childPath = "images";
        }

        String UPLOAD_FILE_NAME;

        // 파일 용량 체크
        if(audioCheck){
            String tmp = tv_audio_desc.getText().toString();
           if(tmp.contains("MB") || tmp.contains("GB") || tmp.contains("TB") ){
                    FBToast.errorToast(getApplicationContext(), getString(R.string.limit_file_size), FBToast.LENGTH_SHORT);
                    return;
           }
            UPLOAD_FILE_NAME = AUDIO_FILE_NAME;
        }else{
            //image size check skips
            //String tmp = compressedSizeTextView.getText().toString();
            //if(tmp.contains("MB") || tmp.contains("GB") || tmp.contains("TB")){
            //    FBToast.errorToast(getApplicationContext(), getString(R.string.limitfilesize), FBToast.LENGTH_SHORT);
            //    return;
            //}
            UPLOAD_FILE_NAME = IMAGE_FILE_NAME;
        }

        //FBToast.infoToast(getApplicationContext(), " EXECUTE_ACTION : "+EXECUTE_ACTION, FBToast.LENGTH_SHORT);

        if(EXECUTE_ACTION){
            return;
        }

        EXECUTE_ACTION = true;

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(getResources().getColor(R.color.progress))
                .setAnimationSpeed(2)
                .show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mountainsRef = storageRef.child(UPLOAD_FILE_NAME);
        StorageReference mountainImagesRef = storageRef.child(childPath+"/"+UPLOAD_FILE_NAME);
        //noinspection ResultOfMethodCallIgnored
        mountainsRef.getName().equals(mountainImagesRef.getName());
        //noinspection ResultOfMethodCallIgnored
        mountainsRef.getPath().equals(mountainImagesRef.getPath());

        Uri file = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/.src_temp_tmp/"+UPLOAD_FILE_NAME));
        StorageReference riversRef = storageRef.child(childPath+"/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        boolean finalAudioCheck = audioCheck;
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                EXECUTE_ACTION = false;
                hud.dismiss();
                FBToast.errorToast(getApplicationContext(), getString(R.string.network_error), FBToast.LENGTH_SHORT);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if(finalAudioCheck){
                    replyMessageSend(Consts.MESSAGE_TYPE_VOICE,AUDIO_FILE_NAME);
                }else{
                    replyMessageSend(Consts.MESSAGE_TYPE_IMAGE,IMAGE_FILE_NAME);
                }
            }
        });
    }

    /**
     * reply message send
     * @param type
     * @param fileName
     */
    private void replyMessageSend(String type,String fileName) {
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
        params.put("TALK_TEXT", "");
        params.put("TALK_TYPE", type);
        if(Consts.MESSAGE_TYPE_VOICE.equals(type)){
            params.put("TALK_TEXT_IMAGE", "");
            params.put("TALK_TEXT_VOICE", fileName);
        }else{
            params.put("TALK_TEXT_IMAGE", fileName);
            params.put("TALK_TEXT_VOICE", "");
        }

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
                            att.setTALK_TYPE((String) params.get("TALK_TYPE"));
                            att.setTALK_TEXT_IMAGE((String) params.get("TALK_TEXT_IMAGE"));
                            att.setTALK_TEXT_VOICE((String) params.get("TALK_TEXT_VOICE"));
                            replyMessage(att);
                            Logger.i("replyMessage Success");
                        } else {
                            Logger.e("replyMessage Fail");
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
                //EventBus Call
                BusProvider.getInstance().post("SERVICE_CALL");
                finish();
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }

    @OnClick(R.id.btnBackTop)
    void onBtnBackTopClicked() {
        finish();
    }

    @OnClick(R.id.btnBack)
    void onBtnBackClicked() {
        finish();
    }

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
        fileClear();
    }
}