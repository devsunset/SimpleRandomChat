/*
 * @(#)MessageList.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * <PRE>
 * SimpleRandomChat MessageList
 * </PRE>
 * 
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */

public class MessageList extends Fragment {

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.app_message_list, container, false);
		return v;
	}
}
