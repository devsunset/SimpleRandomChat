/*
 * @(#)MessageSend.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.orhanobut.logger.Logger;
import com.tfb.fbtoast.FBToast;

import java.util.HashMap;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import devsunset.simple.random.chat.modules.accountservice.AccountInfo;
import devsunset.simple.random.chat.modules.dataservice.AppTalkMain;
import devsunset.simple.random.chat.modules.dataservice.AppTalkThread;
import devsunset.simple.random.chat.modules.dataservice.DatabaseClient;
import devsunset.simple.random.chat.modules.httpservice.DataVo;
import devsunset.simple.random.chat.modules.httpservice.HttpConnectClient;
import devsunset.simple.random.chat.modules.httpservice.HttpConnectService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

	HttpConnectService httpConnctService = null;

	@BindView(R.id.chat_message)
	EditText chat_message;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		httpConnctService = HttpConnectClient.getClient().create(HttpConnectService.class);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.app_message_send, container, false);
		ButterKnife.bind(this, v);
		return v;
	}

	@OnClick(R.id.btnMessageRandomSend)
	void onBtnMessageRandomSendClicked() {
		String message = chat_message.getText().toString();

		if (chat_message.getText().toString().trim().length() == 0 ) {
			FBToast.infoToast(getContext(),"input message ",FBToast.LENGTH_SHORT);
			return;
		} else {
			FBToast.infoToast(getContext(),"send to message",FBToast.LENGTH_SHORT);
			chat_message.setText("");
		}

		HashMap<String,String> account = AccountInfo.getAccountInfo(getContext());
		HashMap<String,Object> params = new HashMap<String,Object>();
		String atxId = UUID.randomUUID().toString();
		String ctm = System.currentTimeMillis()+"";

		params.put("APP_KEY",account.get("APP_KEY"));
		params.put("ATX_ID",atxId);
		params.put("ATX_LOCAL_TIME",ctm);
		params.put("ATX_STATUS","F");
		params.put("FROM_APP_ID",account.get("APP_ID"));
		params.put("FROM_APP_KEY",account.get("APP_KEY"));
		params.put("FROM_COUNTRY",account.get("COUNTRY"));
		params.put("FROM_COUNTRY_NAME",account.get("COUNTRY_NAME"));
		params.put("FROM_GENDER",account.get("GENDER"));
		params.put("FROM_LANG",account.get("LANG"));
		params.put("LAST_TALK_TEXT",message);
		params.put("LAST_TALK_TEXT_IMAGE","");
		params.put("LAST_TALK_TEXT_VOICE","");
		params.put("TALK_TYPE","T");

		httpConnctService.sendMessage(params).enqueue(new Callback<DataVo>() {
			@Override
			public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
				if (response.isSuccessful()) {
					DataVo data = response.body();
					if (data != null) {
						if("S".equals(data.getRESULT_CODE())){

							AppTalkMain atm = new AppTalkMain();
							atm.setATX_ID(atxId);
							atm.setATX_LOCAL_TIME(ctm);
							atm.setATX_STATUS("F");
							atm.setFROM_APP_ID(account.get("APP_ID"));
							atm.setFROM_APP_KEY(account.get("APP_KEY"));
							atm.setFROM_COUNTRY(account.get("COUNTRY"));
							atm.setFROM_COUNTRY_NAME(account.get("COUNTRY_NAME"));
							atm.setFROM_GENDER(account.get("GENDER"));
							atm.setFROM_LANG(account.get("LANG"));
							atm.setLAST_TALK_TEXT(message);
							atm.setLAST_TALK_TEXT_IMAGE("");
							atm.setLAST_TALK_TEXT_VOICE("");
							atm.setTALK_TYPE("T");
							atm.setTO_APP_ID("APP_ID");
							atm.setTO_APP_KEY("APP_KEY");
							atm.setTO_COUNTRY("KR");
							atm.setTO_COUNTRY_NAME("South Korea");
							atm.setTO_GENDER("W");
							atm.setTO_LANG("ko");

							AppTalkThread att = new AppTalkThread();
							att.setATX_ID(atxId);
							att.setTALK_ACCESS_LOCAL_TIME(ctm);
							att.setTALK_APP_ID(account.get("APP_ID"));
							att.setTALK_ID(UUID.randomUUID().toString());
							att.setTALK_COUNTRY(account.get("COUNTRY"));
							att.setTALK_COUNTRY_NAME(account.get("COUNTRY_NAME"));
							att.setTALK_GENDER(account.get("GENDER"));
							att.setTALK_TEXT(message);
							att.setTALK_TEXT_IMAGE("");
							att.setTALK_TEXT_VOICE("");
							att.setTALK_TYPE("T");

							saveDatabse(atm,att);
						}
					}
				}
			}

			@Override
			public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
				Logger.e("sendMessage Error : "+t.getMessage());
			}
		});
	}

	private void saveDatabse(AppTalkMain atm, AppTalkThread att) {
		class SaveTask extends AsyncTask<Void, Void, Void> {
			@Override
			protected Void doInBackground(Void... voids) {
				DatabaseClient.getInstance(getActivity()).getAppDataBase()
						.AppTalkMainDao().insert(atm);

				DatabaseClient.getInstance(getActivity()).getAppDataBase()
						.AppTalkThreadDao().insert(att);
				return null;
			}
			@Override
			protected void onPostExecute(Void aVoid) {
			}
		}
		SaveTask st = new SaveTask();
		st.execute();
	}
}
