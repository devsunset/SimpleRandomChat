/*
 * @(#)LockActivity.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.tfb.fbtoast.FBToast;

import devsunset.simple.random.chat.modules.accountservice.AccountInfo;
import devsunset.simple.random.chat.modules.utilservice.Consts;

/**
 * <PRE>
 * SimpleRandomChat LockActivity
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */
public class LockActivity extends Activity implements OnOtpCompletionListener {
    private OtpView otpView;
    private String PUSH_CALL_FLAG = "N";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if("Y".equals(intent.getStringExtra("PUSH_CALL_FLAG"))){
            PUSH_CALL_FLAG = "Y";
        }

        if(!"Y".equals(AccountInfo.getAccountInfo(getApplicationContext()).get("SET_LOCK_YN"))){
            goNextIntent();
        }
        setContentView(R.layout.lock_activity);

        //screen capture disable
        if (Consts.SCREEN_CAPTURE_DISABLE && Build.VERSION.SDK_INT >= 11) {
            getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE, android.view.WindowManager.LayoutParams.FLAG_SECURE);
        }

        initializeUi();
        setListeners();
    }

    /**
     * Message Content Intent call
     */
    private void goNextIntent(){
        Intent intent = new Intent(this, MessageContent.class);
        intent.putExtra("PUSH_CALL_FLAG",PUSH_CALL_FLAG);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void initializeUi() {
        otpView = findViewById(R.id.otp_view);
    }

    private void setListeners() {
        otpView.setOtpCompletionListener(this);
    }

    @Override public void onOtpCompleted(String otp) {
        if((AccountInfo.getAccountInfo(getApplicationContext()).get("SET_LOCK_PWD")+"").equals(otp)){
            goNextIntent();
        }else{
            FBToast.infoToast(LockActivity.this,"Auth Fail",FBToast.LENGTH_SHORT);
            Intent intent = getIntent();
            intent.putExtra("PUSH_CALL_FLAG",PUSH_CALL_FLAG);
            finish();
            startActivity(intent);
        }
    }
}