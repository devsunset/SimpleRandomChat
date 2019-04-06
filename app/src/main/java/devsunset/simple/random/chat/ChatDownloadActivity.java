/*
 * @(#)ChatDownloadActivity.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.tfb.fbtoast.FBToast;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import devsunset.simple.random.chat.modules.utilservice.Consts;

/**
 * <PRE>
 * SimpleRandomChat ChatDownloadActivity
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */


public class ChatDownloadActivity extends Activity {

    @BindView(R.id.btnAudio)
    Button btnAudio;

    @BindView(R.id.btnImage)
    Button btnImage;

    @BindView(R.id.view_image)
    PhotoView view_image;

    @BindView(R.id.loadingText)
    TextView loadingText;

    @BindView(R.id.replayText)
    TextView replayText;


    public static String TALK_TYPE = "";
    public static String TALK_TEXT_VOICE = "";
    public static String TALK_TEXT_IMAGE = "";

    MediaPlayer mediaPlayer = null;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_download_activity);
        ButterKnife.bind(this);

        //screen capture disable
        if (Consts.SCREEN_CAPTURE_DISABLE && Build.VERSION.SDK_INT >= 11) {
            getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE, android.view.WindowManager.LayoutParams.FLAG_SECURE);
        }

        Intent intent = getIntent();
        TALK_TYPE = intent.getStringExtra("TALK_TYPE");
        TALK_TEXT_VOICE = intent.getStringExtra("TALK_TEXT_VOICE");
        TALK_TEXT_IMAGE = intent.getStringExtra("TALK_TEXT_IMAGE");

        if(Consts.MESSAGE_TYPE_VOICE.equals(TALK_TYPE)){
            btnAudio.setVisibility(View.VISIBLE);
        }else{
            btnImage.setVisibility(View.VISIBLE);
            view_image.setVisibility(View.VISIBLE);
        }

        // 권한 획득
        // Multiple permissions:
        String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                dirCheck();
                getData();
            }
            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                finish();
            }
        });
    }

    private void dirCheck() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/.src_temp_tmp");
        if(!dir.exists()){
            dir.mkdirs();
        }
    }

    public void getData(){

        String destSource = "";

        if(Consts.MESSAGE_TYPE_VOICE.equals(TALK_TYPE)){
            destSource = "audio/"+TALK_TEXT_VOICE;
        }else{
            destSource = "images/"+TALK_TEXT_IMAGE;
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl("gs://src-server.appspot.com/"+destSource);

        if(Consts.MESSAGE_TYPE_VOICE.equals(TALK_TYPE)){
            File localFile =  null;
            try{
                localFile = new File(Environment.getExternalStorageDirectory() + "/.src_temp_tmp/"+TALK_TEXT_VOICE);
                gsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        replayText.setVisibility(View.VISIBLE);
                        mediaPlayer = new MediaPlayer();
                        try{
                            loadingText.setVisibility(View.GONE);
                            FileInputStream MyFile = new FileInputStream(new File(Environment.getExternalStorageDirectory() + "/.src_temp_tmp/"+TALK_TEXT_VOICE));
                            mediaPlayer.setDataSource(MyFile.getFD());
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        }catch(Exception e) {
                            FBToast.infoToast(getApplicationContext(), getString(R.string.downfileerror), FBToast.LENGTH_SHORT);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        loadingText.setVisibility(View.GONE);
                        FBToast.infoToast(getApplicationContext(), getString(R.string.downfileerror), FBToast.LENGTH_SHORT);
                    }
                });

            }catch(Exception e){
                loadingText.setVisibility(View.GONE);
                FBToast.infoToast(getApplicationContext(), getString(R.string.downfileerror), FBToast.LENGTH_SHORT);
            }
        }else{
            final long ONE_MEGABYTE = 1024 * 1024;
            gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    btnImage.setVisibility(View.GONE);
                    loadingText.setVisibility(View.GONE);
                    // Convert bytes data into a Bitmap
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    view_image.setImageBitmap(bmp);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    loadingText.setVisibility(View.GONE);
                    FBToast.infoToast(getApplicationContext(), getString(R.string.downfileerror), FBToast.LENGTH_SHORT);
                }
            });
        }
    }

    public void fileClear(){
        File file = new File(Environment.getExternalStorageDirectory() + "/.src_temp_tmp");
        File[] files = file.listFiles();
        if(files !=null && files.length > 0){
            for( int i=0; i<files.length; i++){
                files[i].delete();
            }
        }
    }

    @OnClick(R.id.btnAudio)
    void onBtnAudioClicked() {
        if((new File(Environment.getExternalStorageDirectory() + "/.src_temp_tmp/"+TALK_TEXT_VOICE)).exists()){
            mediaPlayer = new MediaPlayer();
            try{
                loadingText.setVisibility(View.GONE);
                FileInputStream MyFile = new FileInputStream(new File(Environment.getExternalStorageDirectory() + "/.src_temp_tmp/"+TALK_TEXT_VOICE));
                mediaPlayer.setDataSource(MyFile.getFD());
                mediaPlayer.prepare();
                mediaPlayer.start();
            }catch(Exception e) {
                FBToast.infoToast(getApplicationContext(), getString(R.string.downfileerror), FBToast.LENGTH_SHORT);
            }
        }
    }

    @OnClick(R.id.btnBack)
    void onBtnBackClicked() {
        finish();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        fileClear();

        if(mediaPlayer !=null){
            mediaPlayer.release();
        }
    }
}