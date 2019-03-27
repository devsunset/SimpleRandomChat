/*
 * @(#)MessageSend.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.orhanobut.logger.Logger;
import com.tfb.fbtoast.FBToast;

import java.util.HashMap;
import java.util.UUID;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import devsunset.simple.random.chat.modules.accountservice.AccountInfo;
import devsunset.simple.random.chat.modules.httpservice.DataVo;
import devsunset.simple.random.chat.modules.httpservice.HttpConnectClient;
import devsunset.simple.random.chat.modules.httpservice.HttpConnectService;
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

public class MessageSend extends Fragment{

	HttpConnectService httpConnctService = null;

	@BindView(R.id.chat_message)
	EditText chat_message;

	@BindView(R.id.toogleTargetGender)
	ToggleSwitch toogleTargetGender;

	@BindView(R.id.toogleTargetCountry)
	ToggleSwitch toogleTargetCountry;

	@BindView(R.id.newMessageRecived)
	LabeledSwitch switchNewMessageReceive;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		httpConnctService = HttpConnectClient.getClient().create(HttpConnectService.class);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.app_message_send, container, false);
		ButterKnife.bind(this, v);

		HashMap<String,String> account = AccountInfo.getAccountInfo(getContext());

		if("A".equals(account.get("SET_SEND_GENDER"))){
			toogleTargetGender.setCheckedTogglePosition(0);
		}else if("M".equals(account.get("SET_SEND_GENDER"))){
			toogleTargetGender.setCheckedTogglePosition(1);
		}else{
			toogleTargetGender.setCheckedTogglePosition(2);
		}

		if("L".equals(account.get("SET_SEND_COUNTRY"))){
			toogleTargetCountry.setCheckedTogglePosition(0);
		}else{
			toogleTargetCountry.setCheckedTogglePosition(1);
		}

		if("Y".equals(account.get("SET_NEW_RECEIVE_YN"))){
			switchNewMessageReceive.setOn(true);
		}else{
			switchNewMessageReceive.setOn(false);
		}

		toogleTargetGender.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener(){
			@Override
			public void onToggleSwitchChangeListener(int position, boolean isChecked) {
				appInfoSetting();
			}
		});

		toogleTargetCountry.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener(){
			@Override
			public void onToggleSwitchChangeListener(int position, boolean isChecked) {
				appInfoSetting();
			}
		});

		switchNewMessageReceive.setOnToggledListener(new OnToggledListener() {
			@Override
			public void onSwitched(ToggleableView toggleableView, boolean isOn) {
				appInfoSetting();
			}
		});

		return v;
	}

	/**
	 * App Info Setting
	 */
	private void appInfoSetting(){
		HashMap<String,String> params = new HashMap<String,String>();
		int positionGender = toogleTargetGender.getCheckedTogglePosition();
		int positioCountry = toogleTargetCountry.getCheckedTogglePosition();

		if(positionGender == 0){
			params.put("SET_SEND_GENDER","A");
		}else if(positionGender == 1){
			params.put("SET_SEND_GENDER","M");
		}else{
			params.put("SET_SEND_GENDER","W");
		}

		if(positioCountry == 0){
			params.put("SET_SEND_COUNTRY","L");
		}else{
			params.put("SET_SEND_COUNTRY","W");
		}

		if(switchNewMessageReceive.isOn()){
			params.put("SET_NEW_RECEIVE_YN","Y");
		}else{
			params.put("SET_NEW_RECEIVE_YN","N");
		}

		AccountInfo.setAccountInfo(getContext(),params);
	}

	/**
	 * message send button click
	 */
	@OnClick(R.id.btnMessageRandomSend)
	void onBtnMessageRandomSendClicked() {

		String message = chat_message.getText().toString();

		if (chat_message.getText().toString().trim().length() == 0 ) {
			FBToast.infoToast(getContext(),getString(R.string.sendinput),FBToast.LENGTH_SHORT);
			return;
		} else {
			FBToast.infoToast(getContext(),getString(R.string.sendresult),FBToast.LENGTH_SHORT);
			chat_message.setText("");
		}

		// 메세지 내용 최대 500자 제한
		if(message.length() >= 500){
			message = message.substring(0,500)+" ...";
		}

		HashMap<String,String> account = AccountInfo.getAccountInfo(getContext());
		String ctm = System.currentTimeMillis()+"";

        HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("ATX_ID","ATX_"+UUID.randomUUID().toString());
		params.put("ATX_LOCAL_TIME",ctm);
		params.put("ATX_STATUS","F");
		params.put("FROM_APP_ID",account.get("APP_ID"));
		params.put("FROM_APP_KEY",account.get("APP_KEY"));
		params.put("FROM_COUNTRY",account.get("COUNTRY"));
		params.put("FROM_COUNTRY_NAME",account.get("COUNTRY_NAME"));
		params.put("FROM_GENDER",account.get("GENDER"));
		params.put("FROM_LANG",account.get("LANG"));
		params.put("LAST_TALK_TEXT",message);
		params.put("TALK_TEXT_IMAGE","");
		params.put("TALK_TEXT_VOICE","");
		params.put("LAST_TALK_TYPE","T");
        params.put("TALK_ID","TTD_"+UUID.randomUUID().toString());
        params.putAll(account);

		httpConnctService.sendMessage(params).enqueue(new Callback<DataVo>() {
			@Override
			public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
				if (response.isSuccessful()) {
					DataVo data = response.body();
					if (data != null) {
						if("S".equals(data.getRESULT_CODE())){
							Logger.i("sendMessage Success : ");
						}else{
							Logger.e("sendMessage Fail : ");
						}
					}
				}
			}

			@Override
			public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
				Logger.e("sendMessage Error : "+t.getMessage());
			}
		});
	}
}
