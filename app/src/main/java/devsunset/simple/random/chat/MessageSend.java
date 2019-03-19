/*
 * @(#)MessageSend.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.tfb.fbtoast.FBToast;

import butterknife.ButterKnife;
import butterknife.OnClick;
import devsunset.simple.random.chat.modules.accountservice.AccountInfo;
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

		httpConnctService.sendMessage(AccountInfo.getAccountInfo(getContext())).enqueue(new Callback<DataVo>() {
			@Override
			public void onResponse(@NonNull Call<DataVo> call, @NonNull Response<DataVo> response) {
				if (response.isSuccessful()) {
					DataVo data = response.body();
					if (data != null) {
						Logger.d(data.getCALL_FUNCTION());
						Logger.d(data.getRESULT_CODE());
						Logger.d(data.getRESULT_MESSAGE());
						Logger.d(data.getRESULT_DATA());
						FBToast.successToast(getContext(),data.getRESULT_MESSAGE(),FBToast.LENGTH_SHORT);
					}
				}else{
					FBToast.errorToast(getContext(),"sendMessage : "+response.isSuccessful(),FBToast.LENGTH_SHORT);
				}
			}

			@Override
			public void onFailure(@NonNull Call<DataVo> call, @NonNull Throwable t) {
				Logger.e(t.getMessage());
				FBToast.errorToast(getContext(),"sendMessage : "+t.getMessage(),FBToast.LENGTH_SHORT);
			}
		});
	}
}
