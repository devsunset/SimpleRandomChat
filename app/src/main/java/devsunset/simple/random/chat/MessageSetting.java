/*
 * @(#)MessageSetting.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;

import java.util.ArrayList;
import java.util.HashMap;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import butterknife.BindView;
import butterknife.ButterKnife;
import devsunset.simple.random.chat.modules.accountservice.AccountInfo;

/**
 * <PRE>
 * SimpleRandomChat MessageSetting
 * </PRE>
 * 
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */

public class MessageSetting extends Fragment {

	@BindView(R.id.toogleAlarm)
	ToggleSwitch toogleAlarm;

	@BindView(R.id.byeconfirm)
	LabeledSwitch switchByConfirm;

	@BindView(R.id.replymessagehide)
	LabeledSwitch switchReplyMessageHide;

	@BindView(R.id.lockPwd)
	LabeledSwitch switchLockPwd;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

	    View v = inflater.inflate(R.layout.app_message_setting, container, false);
		ButterKnife.bind(this, v);

		HashMap<String,String> account = AccountInfo.getAccountInfo(getContext());

		ArrayList<String> labels = new ArrayList<String>();
		labels.add("A");
		labels.add("B");
		labels.add("C");
		labels.add("D");
		toogleAlarm.setLabels(labels);


		if("Y".equals(account.get("SET_ALARM_YN"))){
			toogleAlarm.setCheckedTogglePosition(0);
		}else if("Y".equals(account.get("SET_ALARM_NOTI_YN"))){
			toogleAlarm.setCheckedTogglePosition(1);
		}else if("Y".equals(account.get("SET_ALARM_POPUP_YN"))){
			toogleAlarm.setCheckedTogglePosition(2);
		}else{
			toogleAlarm.setCheckedTogglePosition(3);
		}

		if("Y".equals(account.get("SET_BYE_CONFIRM_YN"))){
			switchByConfirm.setOn(true);
		}else{
			switchByConfirm.setOn(false);
		}

		if("Y".equals(account.get("SET_SEND_LIST_HIDE_YN"))){
			switchReplyMessageHide.setOn(true);
		}else{
			switchReplyMessageHide.setOn(false);
		}

		if("Y".equals(account.get("SET_LOCK_YN"))){
			switchLockPwd.setOn(true);
		}else{
			switchLockPwd.setOn(false);
		}

		toogleAlarm.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener(){
			@Override
			public void onToggleSwitchChangeListener(int position, boolean isChecked) {
				appInfoSetting();
			}
		});


		switchByConfirm.setOnToggledListener(new OnToggledListener() {
			@Override
			public void onSwitched(ToggleableView toggleableView, boolean isOn) {
				appInfoSetting();
			}
		});

		switchReplyMessageHide.setOnToggledListener(new OnToggledListener() {
			@Override
			public void onSwitched(ToggleableView toggleableView, boolean isOn) {
				appInfoSetting();
			}
		});

		switchLockPwd.setOnToggledListener(new OnToggledListener() {
			@Override
			public void onSwitched(ToggleableView toggleableView, boolean isOn) {
				if(isOn){
					Intent intent = new Intent(getActivity(), LockSetting.class);
					startActivity(intent);
				}else{
					HashMap<String,String> params = new HashMap<String,String>();
					params.put("SET_LOCK_YN","N");
					params.put("SET_LOCK_PWD","-");
					AccountInfo.setAccountInfo(getContext(),params);
				}
			}
		});

		return v;
	}


	@Override
	public void onResume() {
		super.onResume();  // Always call the superclass method first

		if("Y".equals(AccountInfo.getAccountInfo(getContext()).get("SET_LOCK_YN"))){
			switchLockPwd.setOn(true);
		}else{
			switchLockPwd.setOn(false);
		}
	}


	/**
	 * App Info Setting
	 */
	private void appInfoSetting(){
		HashMap<String,String> params = new HashMap<String,String>();
		int positionAlarm = toogleAlarm.getCheckedTogglePosition();

		if(positionAlarm == 0){
			params.put("SET_ALARM_YN","Y");
			params.put("SET_ALARM_NOTI_YN","N");
			params.put("SET_ALARM_POPUP_YN","N");
		}else if(positionAlarm == 1){
			params.put("SET_ALARM_YN","N");
			params.put("SET_ALARM_NOTI_YN","Y");
			params.put("SET_ALARM_POPUP_YN","N");
		}else if(positionAlarm == 2){
			params.put("SET_ALARM_YN","N");
			params.put("SET_ALARM_NOTI_YN","N");
			params.put("SET_ALARM_POPUP_YN","Y");
		}else{
			params.put("SET_ALARM_YN","N");
			params.put("SET_ALARM_NOTI_YN","N");
			params.put("SET_ALARM_POPUP_YN","N");
		}


		if(switchByConfirm.isOn()){
			params.put("SET_BYE_CONFIRM_YN","Y");
		}else{
			params.put("SET_BYE_CONFIRM_YN","N");
		}

		if(switchReplyMessageHide.isOn()){
			params.put("SET_SEND_LIST_HIDE_YN","Y");
		}else{
			params.put("SET_SEND_LIST_HIDE_YN","N");
		}

		AccountInfo.setAccountInfo(getContext(),params);
	}
}
