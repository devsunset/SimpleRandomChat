package devsunset.simple.random.chat;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;

import devsunset.simple.random.chat.modules.accountservice.AccountInfoService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    // 토큰 재생성
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "token = " + token);

        HashMap<String,Object> myInfo = new HashMap<String,Object>();
        myInfo.put("APP_KEY",token);
        AccountInfoService.setAccountInfo(this,myInfo);
    }
}