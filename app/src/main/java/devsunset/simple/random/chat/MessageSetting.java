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
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import devsunset.simple.random.chat.modules.accountservice.AccountInfo;
import lib.kingja.switchbutton.SwitchMultiButton;

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
	SwitchMultiButton toogleAlarm;

	@BindView(R.id.newMessageRecived)
	LabeledSwitch switchNewMessageReceive;

	@BindView(R.id.byeconfirm)
	LabeledSwitch switchByConfirm;

	@BindView(R.id.replymessagehide)
	LabeledSwitch switchReplyMessageHide;

	@BindView(R.id.lockPwd)
	LabeledSwitch switchLockPwd;

	// newInstance constructor for creating fragment with arguments
	public static MessageSetting newInstance() {
		MessageSetting fragment = new MessageSetting();
		return fragment;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

	    View v = inflater.inflate(R.layout.message_setting, container, false);
		ButterKnife.bind(this, v);

		HashMap<String,String> account = AccountInfo.getAccountInfo(getContext());

		toogleAlarm.setText("A","B","C","D");

		if("Y".equals(account.get("SET_ALARM_YN"))){
			toogleAlarm.setSelectedTab(0);
		}else if("Y".equals(account.get("SET_ALARM_NOTI_YN"))){
			toogleAlarm.setSelectedTab(1);
		}else if("Y".equals(account.get("SET_ALARM_POPUP_YN"))){
			toogleAlarm.setSelectedTab(2);
		}else{
			toogleAlarm.setSelectedTab(3);
		}

		if ("Y".equals(account.get("SET_NEW_RECEIVE_YN"))) {
			switchNewMessageReceive.setOn(true);
		} else {
			switchNewMessageReceive.setOn(false);
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

		toogleAlarm.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
			@Override
			public void onSwitch(int position, String tabText) {
				appInfoSetting();
			}
		});

		switchNewMessageReceive.setOnToggledListener(new OnToggledListener() {
			@Override
			public void onSwitched(ToggleableView toggleableView, boolean isOn) {
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
					Intent intent = new Intent(getActivity(), LockSettingActivity.class);
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
		int positionAlarm = toogleAlarm.getSelectedTab();

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

		if (switchNewMessageReceive.isOn()) {
			params.put("SET_NEW_RECEIVE_YN", "Y");
		} else {
			params.put("SET_NEW_RECEIVE_YN", "N");
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

	/**
	 * Open Source License button click
	 */
	@OnClick(R.id.btnLicense)
	void onBtnLicenseClicked() {
		Intent intent = new Intent(getContext(), WebViewActivity.class);
		intent.putExtra("URL_ADDRESS","file:///android_asset/www/index.html");
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}

	/**
	 * Google License button click
	 */
	@OnClick(R.id.btnLicenseGoogle)
	void onBtnLicenseGoogleClicked() {
		// When the user selects an option to see the licenses:
		startActivity(new Intent(getContext(), OssLicensesMenuActivity.class));
	}
}
