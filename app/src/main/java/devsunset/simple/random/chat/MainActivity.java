package devsunset.simple.random.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.HashMap;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.OnClick;
import devsunset.simple.random.chat.modules.AccountInfo;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toast.makeText(this, "INIT", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnCreateAccountInfo)
    void onBtnCreateAccountInfoClicked() {
        HashMap<String,Object> myInfo = new HashMap<String,Object>();
        myInfo.put("APP_ID",UUID.randomUUID().toString());
        AccountInfo accountInfo = new AccountInfo();
        Toast.makeText(this, "Create Account Info : "+accountInfo.setAccountInfo(this,myInfo), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnGetAccountInfo)
    void onBtnGetAccountInfoClicked() {
        AccountInfo accountInfo = new AccountInfo();
        Toast.makeText(this, "Get Account Info :"+accountInfo.getAccountInfo(this).toString(), Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btnAppInfoInit)
    void onBtnAppInfoInitClicked() {
        Toast.makeText(this, "appInfoInit", Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.btnAppInfoUpdate)
    void onBtnAppInfoUpdateClicked() {
        Toast.makeText(this, "appInfoUpdate", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnMessageRandomSend)
    void onBtnMessageRandomSendClicked() {
        Toast.makeText(this, "Message Random Send", Toast.LENGTH_SHORT).show();
    }
}
