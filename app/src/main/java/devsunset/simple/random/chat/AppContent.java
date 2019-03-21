/*
 * @(#)AppContent.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

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

public class AppContent extends FragmentActivity {


	private final int TAB_MESSAGE_SEND = 0;
	private final int TAB_MESSAGE_LIST = 1;
	private final int TAB_MESSAGE_SETTING = 2;

	FragmentManager fm;
	FragmentTransaction tran;


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

	public boolean boolBackKeyPressFlag = false;

	private AdView adView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_content);

		//screen capture disable
		if (Build.VERSION.SDK_INT >= 11) {
			getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE, android.view.WindowManager.LayoutParams.FLAG_SECURE);
		}

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

        setCurrentItem(TAB_MESSAGE_SEND);


		// Initialize the Mobile Ads SDK.
		MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

		// Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
		// values/strings.xml.
		adView = findViewById(R.id.ad_view);

		// Create an ad request. Check your logcat output for the hashed device ID to
		// get test ads on a physical device. e.g.
		// "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.build();

		// Start loading the ad in the background.
		adView.loadAd(adRequest);
	}



	// ---------------------------------------------------------------------------------------------
	// public methods
	// ---------------------------------------------------------------------------------------------
	public void setCurrentItem(int position) {
		setTab(position);
		fm = getSupportFragmentManager();
		tran = fm.beginTransaction();
		switch (position){
			case 0:
				tran.replace(R.id.main_frame, list.get(0));
				tran.commit();
				break;
			case 1:
				tran.replace(R.id.main_frame, list.get(1));
				tran.commit();
				break;
			case 2:
				tran.replace(R.id.main_frame, list.get(2));
				tran.commit();
				break;
		}

	}

	public void setTab(int position) {
		clearEnable();
		enableLine(position);
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
			View view = this.getCurrentFocus();
			if (view!= null) {
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
			break;
		case TAB_MESSAGE_SETTING:
			int enable3 = getResources().getColor(R.color.tab_enable3);
			vTab3.setBackgroundColor(enable3);
			tvTab3.setTextColor(enable3);
			View viewx = this.getCurrentFocus();
			if (viewx!= null) {
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(viewx.getWindowToken(), 0);
			}
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

	// ---------------------------------------------------------------------------------------------
	// Ads method
	// ---------------------------------------------------------------------------------------------
	/** Called when leaving the activity */
	@Override
	public void onPause() {
		if (adView != null) {
			adView.pause();
		}
		super.onPause();
	}

	/** Called when returning to the activity */
	@Override
	public void onResume() {
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
	}

	/** Called before the activity is destroyed */
	@Override
	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}
}