/*
 * @(#)AppContent.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.annotation.SuppressLint;
import android.app.LauncherActivity.ListItem;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * <PRE>
 * SimpleRandomChat App Content Tab
 * </PRE>
 * 
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */

public class AppContent extends FragmentActivity{

	private final int TAB_MESSAGE_SEND = 0;
	private final int TAB_MESSAGE_LIST = 1;
	private final int TAB_MESSAGE_SETTING = 2;

	private RelativeLayout llTab1 = null;
	private RelativeLayout llTab2 = null;
	private RelativeLayout llTab3 = null;
	private View vTab1 = null;
	private View vTab2 = null;
	private View vTab3 = null;
	private TextView tvTab1 = null;
	private TextView tvTab2 = null;
	private TextView tvTab3 = null;

	private ArrayList<Fragment> list = null;
	private ListItem itemInfo = null;
	
	public boolean boolBackKeyPressFlag = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_content);

		llTab1 = (RelativeLayout) findViewById(R.id.ll_tab1);
		llTab2 = (RelativeLayout) findViewById(R.id.ll_tab2);
		llTab3 = (RelativeLayout) findViewById(R.id.ll_tab3);
		
		vTab1 = findViewById(R.id.v_line_tab1);
		vTab2 = findViewById(R.id.v_line_tab2);
		vTab3 = findViewById(R.id.v_line_tab3);
		
		tvTab1 = (TextView) findViewById(R.id.tv_tab1);
		tvTab2 = (TextView) findViewById(R.id.tv_tab2);
		tvTab3 = (TextView) findViewById(R.id.tv_tab3);

		initFragments();

		llTab1.setOnClickListener(mOnClickListener);
		llTab2.setOnClickListener(mOnClickListener);
		llTab3.setOnClickListener(mOnClickListener);
	}

	// ---------------------------------------------------------------------------------------------
	// public methods
	// ---------------------------------------------------------------------------------------------
	public void setCurrentItem(int position) {
		setTab(position);
	}

	public void setTab(int position) {
		clearEnable();
		enableLine(position);
	}

	public ListItem getItemInfo() {
		return this.itemInfo;
	}

	// ---------------------------------------------------------------------------------------------
	// private methods
	// ---------------------------------------------------------------------------------------------
	private void initFragments() {
		list = new ArrayList<Fragment>();
		list.add(new MessageSend());
		list.add(new MessageList());
		list.add(new MessageSetting());

		enableLine(TAB_MESSAGE_SEND);
	}

	private void enableLine(int position) {
		switch (position) {
		case TAB_MESSAGE_SEND:
			int enable1 = getResources().getColor(R.color.tab_enable1);
			vTab1.setBackgroundColor(enable1);
			tvTab1.setTextColor(enable1);
			break;
		case TAB_MESSAGE_LIST:
			int enable2 = getResources().getColor(R.color.tab_enable2);
			vTab2.setBackgroundColor(enable2);
			tvTab2.setTextColor(enable2);
			break;
		case TAB_MESSAGE_SETTING:
			int enable3 = getResources().getColor(R.color.tab_enable3);
			vTab3.setBackgroundColor(enable3);
			tvTab3.setTextColor(enable3);
			break;
		}
	}

	private void clearEnable() {
		int disableLine = getResources().getColor(R.color.tab_disable);
		int disableText = getResources().getColor(R.color.tab_text_disable);
		vTab1.setBackgroundColor(disableLine);
		vTab2.setBackgroundColor(disableLine);
		vTab3.setBackgroundColor(disableLine);
		tvTab1.setTextColor(disableText);
		tvTab2.setTextColor(disableText);
		tvTab3.setTextColor(disableText);
	}

	// ---------------------------------------------------------------------------------------------
	// Listener
	// ---------------------------------------------------------------------------------------------
	View.OnClickListener mOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_tab1:
				setCurrentItem(TAB_MESSAGE_SEND);
				break;
			case R.id.ll_tab2:
				setCurrentItem(TAB_MESSAGE_LIST);
				break;
			case R.id.ll_tab3:
				setCurrentItem(TAB_MESSAGE_SETTING);
				break;
			}
		}
	};


	// ---------------------------------------------------------------------------------------------
	// inner class
	// ---------------------------------------------------------------------------------------------
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if( keyCode == KeyEvent.KEYCODE_BACK ){
			if (!boolBackKeyPressFlag) {
				Toast.makeText(getBaseContext(), getResources().getString(R.string.main_back_finish), Toast.LENGTH_SHORT).show();
				boolBackKeyPressFlag = true;
				mKillBackKeyHandler.sendEmptyMessageDelayed(0, 1500);
				return true;
			}
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	@SuppressLint("HandlerLeak")
	Handler mKillBackKeyHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				boolBackKeyPressFlag = false;
			}
		}
	};

}