/*
 * @(#)LockActivity.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.tfb.fbtoast.FBToast;

import devsunset.simple.random.chat.modules.accountservice.AccountInfo;

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

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!"Y".equals(AccountInfo.getAccountInfo(getApplicationContext()).get("SET_LOCK_YN"))){
            goNextIntent();
        }
        setContentView(R.layout.lock_activity);
        initializeUi();
        setListeners();
    }

    private void goNextIntent(){
        Intent intent = new Intent(this, AppContent.class);
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
            finish();
            startActivity(intent);
        }
    }
}