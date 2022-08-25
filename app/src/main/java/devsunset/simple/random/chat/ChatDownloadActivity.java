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
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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

    private static String TALK_TYPE = "";
    private static String TALK_TEXT_VOICE = "";
    private static String TALK_TEXT_IMAGE = "";
    private final Handler myHandler = new Handler();
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
    @BindView(R.id.playArea)
    LinearLayout playArea;
    private KProgressHUD hud;
    private MediaPlayer mediaPlayer = null;
    private double startTime = 0;
    private double finalTime = 0;

    private TextView tx1, tx2;
    private SeekBar seekbar;
    private final Runnable UpdateSongTime = new Runnable() {
        public void run() {
            try {
                if (mediaPlayer != null) {
                    startTime = mediaPlayer.getCurrentPosition();
                    tx1.setText(String.format("%d sec", TimeUnit.MILLISECONDS.toSeconds((long) startTime)));
                    seekbar.setProgress((int) startTime);
                    myHandler.postDelayed(this, 100);
                }
            } catch (Exception e) {
                Logger.e("UpdateSongTime : " + e.getMessage());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_download_activity);
        ButterKnife.bind(this);

        //screen capture disable
        if (Consts.SCREEN_CAPTURE_DISABLE && Build.VERSION.SDK_INT >= 11) {
            getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE, android.view.WindowManager.LayoutParams.FLAG_SECURE);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        Intent intent = getIntent();
        TALK_TYPE = intent.getStringExtra("TALK_TYPE");
        TALK_TEXT_VOICE = intent.getStringExtra("TALK_TEXT_VOICE");
        TALK_TEXT_IMAGE = intent.getStringExtra("TALK_TEXT_IMAGE");

        if (Consts.MESSAGE_TYPE_VOICE.equals(TALK_TYPE)) {
            btnAudio.setVisibility(View.VISIBLE);
        } else {
            btnImage.setVisibility(View.VISIBLE);
            view_image.setVisibility(View.VISIBLE);
        }

        tx1 = findViewById(R.id.txtCurrentLength);
        tx2 = findViewById(R.id.textAllLength);
        seekbar = findViewById(R.id.seekBar);
        seekbar.setClickable(false);

        // 권한 획득
        // Multiple permissions:
        String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
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

    /**
     * directory check
     */
    private void dirCheck() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/.src_temp_tmp");
        if (!dir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }
    }

    /**
     * attach data search
     */
    private void getData() {

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setBackgroundColor(getResources().getColor(R.color.progress))
                .setAnimationSpeed(2)
                .show();

        String destSource;
        String fileName;

        if (Consts.MESSAGE_TYPE_VOICE.equals(TALK_TYPE)) {
            destSource = "audio/" + TALK_TEXT_VOICE;
            fileName = TALK_TEXT_VOICE;
        } else {
            destSource = "images/" + TALK_TEXT_IMAGE;
            fileName = TALK_TEXT_IMAGE;
        }

        long lastTime = Long.parseLong(fileName.substring(0, fileName.indexOf('_')));
        long curTime = System.currentTimeMillis();
        long diffTime = (curTime - lastTime) / (1000 * 60 * 60 * 24);

        if (diffTime > Consts.ATTACH_FILE_MAX_DAY_PERIOD) {
            hud.dismiss();
            loadingText.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), getString(R.string.down_file_error), Toast.LENGTH_SHORT).show();
        } else {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference gsReference = storage.getReferenceFromUrl("gs://src-server.appspot.com/" + destSource);

            if (Consts.MESSAGE_TYPE_VOICE.equals(TALK_TYPE)) {
                File localFile;
                try {
                    localFile = new File(Environment.getExternalStorageDirectory() + "/.src_temp_tmp/" + TALK_TEXT_VOICE);
                    gsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            try {
                                hud.dismiss();
                                loadingText.setVisibility(View.GONE);
                                replayText.setVisibility(View.VISIBLE);
                                playArea.setVisibility(View.VISIBLE);
                                onBtnAudioClicked();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), getString(R.string.down_file_error), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            hud.dismiss();
                            loadingText.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), getString(R.string.down_file_error), Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    loadingText.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), getString(R.string.down_file_error), Toast.LENGTH_SHORT).show();
                }
            } else {
                final long ONE_MEGABYTE = 1024 * 1024;
                gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        hud.dismiss();
                        btnImage.setVisibility(View.GONE);
                        loadingText.setVisibility(View.GONE);
                        // Convert bytes data into a Bitmap
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        view_image.setImageBitmap(bmp);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        hud.dismiss();
                        loadingText.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), getString(R.string.down_file_error), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    /**
     * temp file clear
     */
    private void fileClear() {
        File file = new File(Environment.getExternalStorageDirectory() + "/.src_temp_tmp");
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                //noinspection ResultOfMethodCallIgnored
                files[i].delete();
            }
        }
    }

    @OnClick(R.id.btnAudio)
    void onBtnAudioClicked() {
        if ((new File(Environment.getExternalStorageDirectory() + "/.src_temp_tmp/" + TALK_TEXT_VOICE)).exists()) {
            try {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }

                mediaPlayer = new MediaPlayer();

                FileInputStream MyFile = new FileInputStream(new File(Environment.getExternalStorageDirectory() + "/.src_temp_tmp/" + TALK_TEXT_VOICE));
                mediaPlayer.setDataSource(MyFile.getFD());
                mediaPlayer.prepare();
                mediaPlayer.start();

                finalTime = mediaPlayer.getDuration();
                startTime = mediaPlayer.getCurrentPosition();
                seekbar.setMax((int) finalTime);
                tx2.setText(String.format("%d sec", TimeUnit.MILLISECONDS.toSeconds((long) finalTime)));
                tx1.setText(String.format("%d sec", TimeUnit.MILLISECONDS.toSeconds((long) startTime)));
                seekbar.setProgress((int) startTime);
                myHandler.postDelayed(UpdateSongTime, 100);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.down_file_error), Toast.LENGTH_SHORT).show();
            }
        }
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
    protected void onDestroy() {
        super.onDestroy();

        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        } catch (Exception e) {
            Logger.e("mediaPlayer onDestory : " + e.getMessage());
        } finally {
            fileClear();
        }
    }
}