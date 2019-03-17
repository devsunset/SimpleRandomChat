/*
 * @(#)MessageSend.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

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


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.app_message_send, container, false);
		return v;
	}

	public static void messageBox(Context context, String msgTitle, String msgText) {
		View layout = View.inflate(context, R.layout.common_alert_dialog, null);
		layout.findViewById(R.id.commonListDialogContentsTextView).setVisibility(View.VISIBLE);

		((TextView) layout.findViewById(R.id.commonListDialogTitleTextView)).setText(msgTitle);
		((TextView) layout.findViewById(R.id.commonListDialogContentsTextView)).setText("");

		final AlertDialog _ab = new AlertDialog.Builder(context).show();
		_ab.getWindow().setBackgroundDrawable(new ColorDrawable(0x0000ff00));
		_ab.setContentView(layout);
		_ab.findViewById(R.id.commonListDialogOkButton).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_ab.dismiss();
			}
		});
	}

}
