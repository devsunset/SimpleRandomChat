package devsunset.simple.random.chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;

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
        Toast.makeText(this, "Create Local Account Info", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnGetAccountInfo)
    void onBtnGetAccountInfoClicked() {
        Toast.makeText(this, "Get Local Account Info", Toast.LENGTH_SHORT).show();
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
