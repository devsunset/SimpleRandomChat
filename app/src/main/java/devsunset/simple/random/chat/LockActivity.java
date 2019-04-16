/*
 * @(#)LockActivity.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;

import butterknife.BindView;
import butterknife.ButterKnife;
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
public class LockActivity extends Activity{
    private String PUSH_CALL_FLAG = "N";

    @BindView(R.id.pinview)
    Pinview pinview;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_activity);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if("Y".equals(intent.getStringExtra("PUSH_CALL_FLAG"))){
            PUSH_CALL_FLAG = "Y";
        }

        if("N".equals(AccountInfo.getAccountInfo(getApplicationContext()).get("SET_LOCK_YN"))){
            goNextIntent();
        }else{
            pinview.requestFocus();
        }

        //screen capture disable
        if (Consts.SCREEN_CAPTURE_DISABLE && Build.VERSION.SDK_INT >= 11) {
            getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE, android.view.WindowManager.LayoutParams.FLAG_SECURE);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinviewValue, boolean fromUser) {
                if((AccountInfo.getAccountInfo(getApplicationContext()).get("SET_LOCK_PWD")+"").equals(pinviewValue.getValue())){
                    goNextIntent();
                }else{
                    Toast.makeText(LockActivity.this,"Auth Fail",Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    intent.putExtra("PUSH_CALL_FLAG",PUSH_CALL_FLAG);
                    finish();
                    startActivity(intent);
                }
              }
        });
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
}