package chek_ins.com.sign.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import chek_ins.com.sign.R;
import chek_ins.com.sign.application.ExitApplication;
import chek_ins.com.sign.utils.PublicData;

/**
 * Created by ggg on 2016/3/2.
 */
public class WelcomeActivty extends Activity {
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_welcome);
        super.onCreate(savedInstanceState);
        ExitApplication.getIntance().addActivity(this);
        preferences = this.getApplicationContext().getSharedPreferences(PublicData.spName, Activity.MODE_APPEND);
        delay();
    }

    private void delay() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String num = preferences.getString("num", null);
                String pass = preferences.getString("pass", null);
                String oldpass = preferences.getString("oldpass", null);
                Log.i("xxx", "num" + num + "pass" + pass);
                Intent intent = new Intent();
                if (num != null && pass != null) {
                    if (oldpass == null || (oldpass != null && oldpass.equals(pass))) {
                        intent.setClass(WelcomeActivty.this, MainActivity.class);
                    } else {
                        intent.setClass(WelcomeActivty.this, LoginActivity.class);

                    }


                } else {

                    intent.setClass(WelcomeActivty.this, LoginActivity.class);
                }
                WelcomeActivty.this.startActivity(intent);
                WelcomeActivty.this.finish();
            }
        }, 2000);
    }
}
