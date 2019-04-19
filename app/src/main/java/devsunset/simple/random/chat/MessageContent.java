/*
 * @(#)MessageContent.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.orhanobut.logger.Logger;

import devsunset.simple.random.chat.modules.eventbusservice.BusProvider;
import devsunset.simple.random.chat.modules.utilservice.Consts;

/**
 * <PRE>
 * SimpleRandomChat App Content Tab
 * </PRE>
 * 
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */
public class MessageContent extends FragmentActivity {

	ViewPager vpPager = null;

	private final static int TAB_MESSAGE_SEND = 0;
	private final static int TAB_MESSAGE_LIST = 1;
	private final static int TAB_MESSAGE_SETTING = 2;

	private RelativeLayout llTab1 = null;
	private RelativeLayout llTab2 = null;
	private RelativeLayout llTab3 = null;
	private View vTab1 = null;
	private View vTab2 = null;
	private View vTab3 = null;
	private ImageView tvTab1 = null;
	private ImageView tvTab2 = null;
	private ImageView tvTab3 = null;

	private boolean boolBackKeyPressFlag = false;

	FragmentPagerAdapter adapterViewPager;

	private AdView adView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_content);

		//screen capture disable
		if (Consts.SCREEN_CAPTURE_DISABLE && Build.VERSION.SDK_INT >= 11) {
			getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE, android.view.WindowManager.LayoutParams.FLAG_SECURE);
		}

		if (Build.VERSION.SDK_INT >= 21) {
			getWindow().setStatusBarColor(Color.BLACK);
		}

		vpPager = (ViewPager) findViewById(R.id.vpPager);
		adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
		vpPager.setAdapter(adapterViewPager);
		vpPager.setOffscreenPageLimit(3);
		vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{
				Log.d("ITPANGPANG","onPageScrolled : "+position);
			}

			@Override
			public void onPageSelected(int position)
			{
				setTab(position);
				setCurrentItem(position);
				if (position == 0) {
					try {
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.showSoftInput(getCurrentFocus(),0);
					}catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (position == 1) {
					//EventBus Call
					BusProvider.getInstance().post("LIST_REFRESH");
				}
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{
				Log.d("ITPANGPANG","onPageScrollStateChanged : "+state);
			}
		});

		llTab1 = findViewById(R.id.ll_tab1);
		llTab2 = findViewById(R.id.ll_tab2);
		llTab3 = findViewById(R.id.ll_tab3);
		
		vTab1 = findViewById(R.id.v_line_tab1);
		vTab2 = findViewById(R.id.v_line_tab2);
		vTab3 = findViewById(R.id.v_line_tab3);
		
		tvTab1 = findViewById(R.id.tv_tab1);
		tvTab2 = findViewById(R.id.tv_tab2);
		tvTab3 = findViewById(R.id.tv_tab3);

		llTab1.setOnClickListener(mOnClickListener);
		llTab2.setOnClickListener(mOnClickListener);
		llTab3.setOnClickListener(mOnClickListener);


		Intent intent = getIntent();
		if("Y".equals(intent.getStringExtra("PUSH_CALL_FLAG"))){
			setCurrentItem(TAB_MESSAGE_LIST);
		}else{
			setCurrentItem(TAB_MESSAGE_SEND);
		}

		// Initialize the Mobile Ads SDK.
		MobileAds.initialize(this, Consts.ADS_APP_ID);

		// Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
		// values/strings.xml.
		adView = findViewById(R.id.ad_view);

		// Create an ad request. Check your logcat output for the hashed device ID to
		// get test ads on a physical device. e.g.
		// "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice(Consts.ADS_TEST_ID)
				.addTestDevice(Consts.ADS_TEST_SUB_ID)
				.build();

		// Start loading the ad in the background.
		adView.loadAd(adRequest);
	}

	private void setTab(int position) {
		clearEnable();
		enableLine(position);
	}

	private void enableLine(int position) {
		switch (position) {
		case TAB_MESSAGE_SEND:
			int enable1 = getResources().getColor(R.color.tab_enable1);
			vTab1.setBackgroundColor(enable1);
			vTab1.setVisibility(View.VISIBLE);
			vTab2.setVisibility(View.GONE);
			vTab3.setVisibility(View.GONE);
			tvTab1.setBackgroundResource(R.drawable.ic_send_on);
			break;
		case TAB_MESSAGE_LIST:
			int enable2 = getResources().getColor(R.color.tab_enable2);
			vTab2.setBackgroundColor(enable2);
			vTab1.setVisibility(View.GONE);
			vTab2.setVisibility(View.VISIBLE);
			vTab3.setVisibility(View.GONE);
			tvTab2.setBackgroundResource(R.drawable.ic_list_on);
			break;
		case TAB_MESSAGE_SETTING:
			int enable3 = getResources().getColor(R.color.tab_enable3);
			vTab3.setBackgroundColor(enable3);
			vTab1.setVisibility(View.GONE);
			vTab2.setVisibility(View.GONE);
			vTab3.setVisibility(View.VISIBLE);
			tvTab3.setBackgroundResource(R.drawable.ic_settings_on);
			break;
		}
	}

	private void clearEnable() {
		int disableLine = getResources().getColor(R.color.tab_disable);

		vTab1.setBackgroundColor(disableLine);
		vTab2.setBackgroundColor(disableLine);
		vTab3.setBackgroundColor(disableLine);

		tvTab1.setBackgroundResource(R.drawable.ic_send_off);
		tvTab2.setBackgroundResource(R.drawable.ic_list_off);
		tvTab3.setBackgroundResource(R.drawable.ic_settings_off);
	}

	private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
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

    private void setCurrentItem(int position) {
        setTab(position);
        switch (position){
            case 0:
				vpPager.setCurrentItem(position);
                break;
            case 1:
				vpPager.setCurrentItem(position);
                break;
            case 2:
				vpPager.setCurrentItem(position);
                break;
        }
    }

	public  class MyPagerAdapter extends FragmentPagerAdapter {
		private  int NUM_ITEMS = 3;

		public MyPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		// Returns total number of pages
		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		// Returns the fragment to display for that page
		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case TAB_MESSAGE_SEND:
					return MessageSend.newInstance();
				case TAB_MESSAGE_LIST:
					return MessageList.newInstance();
				case TAB_MESSAGE_SETTING:
					return MessageSetting.newInstance();
				default:
					return null;
			}
		}

		// Returns the page title for the top indicator
		@Override
		public CharSequence getPageTitle(int position) {
			return "Page " + position;
		}

	}

	// ---------------------------------------------------------------------------------------------
	// back key press
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
	private final
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