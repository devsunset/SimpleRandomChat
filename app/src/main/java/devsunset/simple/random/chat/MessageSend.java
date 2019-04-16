/*
 * @(#)MessageSend.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.orhanobut.logger.Logger;


import java.util.HashMap;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import devsunset.simple.random.chat.modules.accountservice.AccountInfo;
import devsunset.simple.random.chat.modules.httpservice.DataVo;
import devsunset.simple.random.chat.modules.httpservice.HttpConnectClient;
import devsunset.simple.random.chat.modules.httpservice.HttpConnectService;
import devsunset.simple.random.chat.modules.utilservice.Consts;
import lib.kingja.switchbutton.SwitchMultiButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <PRE>
 * SimpleRandomChat MessageSend
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */
public class MessageSend extends Fragment {

    private HttpConnectService httpConnectService = null;

    @BindView(R.id.chat_message)
    EditText chat_message;

    @BindView(R.id.toogleTargetGender)
    SwitchMultiButton toogleTargetGender;

    @BindView(R.id.toogleTargetCountry)
    SwitchMultiButton toogleTargetCountry;

    // newInstance constructor for creating fragment with arguments
    public static MessageSend newInstance() {
        MessageSend fragment = new MessageSend();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpConnectService = HttpConnectClient.getClient().create(HttpConnectService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.message_send, container, false);
        ButterKnife.bind(this, v);

        //toogleTargetGender.setText(getString(R.string.all),getString(R.string.man),getString(R.string.woman));
        //toogleTargetCountry.setText(getString(R.string.local),getString(R.string.world));
        toogleTargetGender.setText("ALL","Man","Woman");
        toogleTargetCountry.setText("Local","World");

        HashMap<String, String> account = AccountInfo.getAccountInfo(getContext());

        if ("A".equals(account.get("SET_SEND_GENDER"))) {
            toogleTargetGender.setSelectedTab(0);
        } else if ("M".equals(account.get("SET_SEND_GENDER"))) {
            toogleTargetGender.setSelectedTab(1);
        } else {
            toogleTargetGender.setSelectedTab(2);
        }

        if ("L".equals(account.get("SET_SEND_COUNTRY"))) {
            toogleTargetCountry.setSelectedTab(0);
        } else {
            toogleTargetCountry.setSelectedTab(1);
        }

        toogleTargetGender.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                appInfoSetting();
            }
        });

        toogleTargetCountry.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                appInfoSetting();
            }
        });

        chat_message.postDelayed(new Runnable() {
            @Override
            public void run() {
                chat_message.requestFocus();
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(chat_message,0);
            }
        }, 300);

        return v;
    }

    /**
     * App Info Setting
     */
    private void appInfoSetting() {
        HashMap<String, String> params = new HashMap<String, String>();
        int positionGender = toogleTargetGender.getSelectedTab();
        int positioCountry = toogleTargetCountry.getSelectedTab();

        if (positionGender == 0) {
            params.put("SET_SEND_GENDER", "A");
        } else if (positionGender == 1) {
            params.put("SET_SEND_GENDER", "M");
        } else {
            params.put("SET_SEND_GENDER", "W");
        }

        if (positioCountry == 0) {
            params.put("SET_SEND_COUNTRY", "L");
        } else {
            params.put("SET_SEND_COUNTRY", "W");
        }

        AccountInfo.setAccountInfo(getContext(), params);
    }

    /**
     * message send button click
     */
    @OnClick(R.id.btnMessageRandomSend)
    void onBtnMessageRandomSendClicked() {

        SharedPreferences prefx = getActivity().getSharedPreferences("MESSAGE_SEND_PERIOD", getActivity().MODE_PRIVATE);

        String message = chat_message.getText().toString();

        if (chat_message.getText().toString().trim().length() == 0) {
            Toast.makeText(getContext(), getString(R.string.send_input), Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(getContext(), getString(R.string.send_result), Toast.LENGTH_SHORT).show();
            chat_message.setText("");
        }

        // 메시지 내용 최대 500자 제한
        if (message.length() >= 500) {
            message = message.substring(0, 500) + " ...";
        }

        long lastTime = Long.parseLong(prefx.getString("LAST_SEND_TIME", "1234567890"));
        long curTime = System.currentTimeMillis();
        long diffTime = (curTime - lastTime) / (1000);
        // 메시지 복사/붙이기로  호출 하는 내역 방지 -  (5초 내로 계속 보내면 SKIP 처리)
        if (diffTime < 5) {
            return;
        }

        SharedPreferences.Editor editor = prefx.edit();
        editor.putString("LAST_SEND_TIME", curTime + "");
        editor.apply();

        HashMap<String, String> account = AccountInfo.getAccountInfo(getContext());
        String ctm = System.currentTimeMillis() + "";

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("ATX_ID", Consts.IDS_PRIEFIX_ATX + UUID.randomUUID().toString());
        params.put("ATX_LOCAL_TIME", ctm);
        params.put("ATX_STATUS", Consts.MESSAGE_STATUS_FIRST);
        params.put("FROM_APP_ID", account.get("APP_ID"));
        params.put("FROM_APP_KEY", account.get("APP_KEY"));
        params.put("FROM_COUNTRY", account.get("COUNTRY"));
        params.put("FROM_COUNTRY_NAME", account.get("COUNTRY_NAME"));
        params.put("FROM_GENDER", account.get("GENDER"));
        params.put("FROM_LANG", account.get("LANG"));
        params.put("TALK_TEXT", message);
        params.put("TALK_TYPE", Consts.MESSAGE_TYPE_TEXT);
        params.put("TALK_ID", Consts.IDS_PRIEFIX_TTD + UUID.randomUUID().toString());
        params.put("TALK_APP_ID", account.get("APP_ID"));
        params.putAll(account);

        httpConnectService.sendMessage(params).enqueue(new Callback<DataVo>() {
            @Override
            public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
                if (response.isSuccessful()) {
                    DataVo data = response.body();
                    if (data != null) {
                        if ("S".equals(data.getRESULT_CODE())) {
                            Logger.i("sendMessage Success : ");
                        } else {
                            Logger.e("sendMessage Fail : ");
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
                Logger.e("sendMessage Error : " + t.getMessage());
            }
        });
    }
}
