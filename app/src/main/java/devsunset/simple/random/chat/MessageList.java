/*
 * @(#)MessageList.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import devsunset.simple.random.chat.modules.accountservice.AccountInfo;
import devsunset.simple.random.chat.modules.dataservice.AppTalkMain;
import devsunset.simple.random.chat.modules.dataservice.DatabaseClient;
import devsunset.simple.random.chat.modules.etcservice.MessageItem;
import devsunset.simple.random.chat.modules.etcservice.MessageAdapter;


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

	@BindView(R.id.recycler_view)
	RecyclerView mRecyclerView;
	RecyclerView.LayoutManager mLayoutManager;
	public static String APP_ID = "";
	public static String APP_KEY = "";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.message_list, container, false);
		ButterKnife.bind(this, v);
		mRecyclerView.setHasFixedSize(true);
		mLayoutManager = new LinearLayoutManager(getActivity());
		mRecyclerView.setLayoutManager(mLayoutManager);
		APP_ID = AccountInfo.getAccountInfo(getActivity()).get("APP_ID");
		APP_KEY =  AccountInfo.getAccountInfo(getActivity()).get("APP_KEY");
		getDatabase();
		return v;
	}

	private void getDatabase() {
		class GetTasks extends AsyncTask<Void, Void, List<AppTalkMain>> {
			@Override
			protected List<AppTalkMain> doInBackground(Void... voids) {
				List<AppTalkMain> taskList = DatabaseClient
						.getInstance(getActivity())
						.getAppDataBase()
						.AppTalkMainDao()
						.getAll();
				return taskList;
			}
			@Override
			protected void onPostExecute(List<AppTalkMain> appTalkMain) {
				ArrayList<MessageItem> messageArrayList = new ArrayList<MessageItem>();
				if(appTalkMain !=null && !appTalkMain.isEmpty()){
					for(int i=0;i<appTalkMain.size();i++){

						MessageItem mi = new MessageItem();
						// ATX_ID
						mi.setATX_ID(appTalkMain.get(i).getATX_ID());
						// ATX_STATUS
						mi.setATX_STATUS(appTalkMain.get(i).getATX_STATUS());
						// TALK_TEXT
						mi.setTALK_TEXT(appTalkMain.get(i).getTALK_TEXT());
						// COUNTRY_NAME
						if(appTalkMain.get(i).getTALK_APP_ID().equals(appTalkMain.get(i).getFROM_APP_ID())){
							mi.setCOUNTRY_NAME(appTalkMain.get(i).getFROM_COUNTRY_NAME());
						}else{
							mi.setCOUNTRY_NAME(appTalkMain.get(i).getTO_COUNTRY_NAME());
						}
						// TALK_TARGET
						if(APP_ID.equals(appTalkMain.get(i).getTALK_APP_ID())){
							mi.setTALK_TARGET("You");
						}else{
							mi.setTALK_TARGET("");
						}
						// ATX_LOCAL_TIME
						SimpleDateFormat dayTime = new SimpleDateFormat("MM-dd HH:mm");
						String str = dayTime.format(new Date(Long.parseLong(appTalkMain.get(i).getATX_LOCAL_TIME())));
						mi.setATX_LOCAL_TIME(str);

						// icon
						if("P".equals(appTalkMain.get(i).getATX_STATUS())){
							mi.setDrawableId(R.drawable.new_message);
						}else if("D".equals(appTalkMain.get(i).getATX_STATUS())){
							mi.setDrawableId(R.drawable.trash);
						}else{
							if(appTalkMain.get(i).getTALK_APP_ID().equals(appTalkMain.get(i).getFROM_APP_ID())){
								if("M".equals(appTalkMain.get(i).getFROM_GENDER())){
									mi.setDrawableId(R.drawable.man);
								}else{
									mi.setDrawableId(R.drawable.woman);
								}
							}else{
								if("M".equals(appTalkMain.get(i).getTO_GENDER())){
									mi.setDrawableId(R.drawable.man);
								}else{
									mi.setDrawableId(R.drawable.woman);
								}
							}
						}

						if(APP_KEY.equals(appTalkMain.get(i).getFROM_APP_KEY())){
							mi.setREPLY_APP_KEY(appTalkMain.get(i).getTO_APP_KEY());
						}else{
							mi.setREPLY_APP_KEY(appTalkMain.get(i).getFROM_APP_KEY());
						}

						messageArrayList.add(mi);
					}
				}else{
					MessageItem mi = new MessageItem();
					mi.setDrawableId(R.drawable.empty_message);
					mi.setTALK_TEXT(getString(R.string.empty_message));
					mi.setTALK_TARGET("NO_DATA");
					messageArrayList.add(mi);
				}
				MessageAdapter myAdapter = new MessageAdapter(messageArrayList,true);
				mRecyclerView.setAdapter(myAdapter);
			}
		}
		GetTasks gt = new GetTasks();
		gt.execute();
	}


	@Override
	public void onResume() {
		super.onResume();  // Always call the superclass method first
		getDatabase();
	}
}
