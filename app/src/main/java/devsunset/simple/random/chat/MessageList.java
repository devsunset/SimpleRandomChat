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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import devsunset.simple.random.chat.modules.dataservice.AppTalkMain;
import devsunset.simple.random.chat.modules.dataservice.DatabaseClient;
import devsunset.simple.random.chat.modules.etcservice.MessageItem;
import devsunset.simple.random.chat.modules.etcservice.MyAdapter;


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

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.app_message_list, container, false);

		ButterKnife.bind(this, v);
		mRecyclerView.setHasFixedSize(true);
		mLayoutManager = new LinearLayoutManager(getActivity());
		mRecyclerView.setLayoutManager(mLayoutManager);
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
						mi.setDrawableId(R.drawable.woman);
						mi.setTALK_TEXT(appTalkMain.get(i).getLAST_TALK_TEXT());
						messageArrayList.add(mi);
					}
				}
				MyAdapter myAdapter = new MyAdapter(messageArrayList);
				mRecyclerView.setAdapter(myAdapter);
			}
		}
		GetTasks gt = new GetTasks();
		gt.execute();
	}
}
