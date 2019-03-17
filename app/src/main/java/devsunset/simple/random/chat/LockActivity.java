/*
 * @(#)LockActivity.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.tfb.fbtoast.FBToast;

/**
 * <PRE>
 * SimpleRandomChat LockActivity
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */


public class LockActivity extends AppCompatActivity implements View.OnClickListener,
		OnOtpCompletionListener {
	private OtpView otpView;
	private Button validateButton;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lock_activity);
		initializeUi();
		setListeners();
	}

	@Override public void onClick(View v) {
		if (v.getId() == R.id.validate_button) {
			FBToast.successToast(LockActivity.this,"otp",FBToast.LENGTH_SHORT);

			Intent intent = new Intent(this, AppContent.class);
			startActivity(intent);
			finish();
		}
	}

	private void initializeUi() {
		otpView = findViewById(R.id.otp_view);
		validateButton = findViewById(R.id.validate_button);
	}

	private void setListeners() {
		otpView.setOtpCompletionListener(this);
		validateButton.setOnClickListener(this);
	}

	@Override public void onOtpCompleted(String otp) {
		FBToast.successToast(LockActivity.this,"otp : "+otp,FBToast.LENGTH_SHORT);

		Intent intent = new Intent(this, AppContent.class);
		startActivity(intent);
		finish();
	}
}