/*
 * @(#)LockSettingActivity.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.app.Activity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.goodiebag.pinview.Pinview;
import com.tfb.fbtoast.FBToast;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import devsunset.simple.random.chat.modules.accountservice.AccountInfo;

/**
 * <PRE>
 * SimpleRandomChat LockSettingActivity
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */

public class LockSettingActivity extends Activity  {

    @BindView(R.id.pinview1)
    Pinview pinview1;

    @BindView(R.id.pinview2)
    Pinview pinview2;

    String password = "";
    String passwordConfirm = "";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_setting_activity);
        ButterKnife.bind(this);

        pinview1.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                password = pinview.getValue();
            }
        });

        pinview2.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {

                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(pinview2.getWindowToken(),0);

                passwordConfirm = pinview.getValue();
                HashMap<String,String> params = new HashMap<String,String>();
                if(!"".equals(password)
                        && password.length() == 4
                        && !"".equals(passwordConfirm)
                        && passwordConfirm.length() == 4
                        && password.equals(passwordConfirm) ){
                    params.put("SET_LOCK_YN","Y");
                    params.put("SET_LOCK_PWD",passwordConfirm);
                    AccountInfo.setAccountInfo(getApplicationContext(),params);
                    FBToast.successToast(getApplicationContext(),getString(R.string.passwordsuccess),FBToast.LENGTH_SHORT);
                    finish();
                }else{
                    params.put("SET_LOCK_YN","N");
                    params.put("SET_LOCK_PWD","-");
                    AccountInfo.setAccountInfo(getApplicationContext(),params);
                    FBToast.errorToast(getApplicationContext(),getString(R.string.passwordfail),FBToast.LENGTH_SHORT);
                    finish();
                }
            }
        });
    }
}