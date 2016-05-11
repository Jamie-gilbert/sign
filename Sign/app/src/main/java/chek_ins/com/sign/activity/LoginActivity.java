package chek_ins.com.sign.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import chek_ins.com.sign.R;
import chek_ins.com.sign.application.ExitApplication;
import chek_ins.com.sign.bo.StudentBo;
import chek_ins.com.sign.utils.L;
import chek_ins.com.sign.utils.PublicData;
import chek_ins.com.sign.utils.T;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.lang.reflect.Type;

/**
 * Created by ggg on 2016/3/2.
 * 登陆界面
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private Button mBt_login, mBt_register, mBt_forgetPass;
    private EditText mEt_num;
    private EditText mEt_pass;
    private StringBuffer url = new StringBuffer();
    private SharedPreferences preferences;
    private String num, pass, phoneId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
        preferences = LoginActivity.this.getApplicationContext().getSharedPreferences(PublicData.spName, Activity.MODE_APPEND);
        ExitApplication.getIntance().addActivity(this);
        mBt_login = (Button) this.findViewById(R.id.bt_login);
        mBt_forgetPass = (Button) this.findViewById(R.id.bt_forgetPass);
        mBt_register = (Button) this.findViewById(R.id.bt_register);
        mEt_num = (EditText) this.findViewById(R.id.et_num);
        mEt_pass = (EditText) this.findViewById(R.id.et_pass);
        initPhoneId();
        String num = preferences.getString("num", null);
        String pass = preferences.getString("pass", null);
        if (num != null && pass != null) {
            mEt_pass.setText(pass);
            mEt_num.setText(num);
        }
        mBt_login.setOnClickListener(this);
        mBt_forgetPass.setOnClickListener(this);
        mBt_register.setOnClickListener(this);

    }


    /**
     * 点击登陆 ，请求服务器判断账号密码是否正确
     *
     * @return
     */
    private boolean login() {
        boolean b = false;
        url.append(PublicData.IP);
        url.append(PublicData.Port);
        url.append(PublicData.Websit);
        url.append(PublicData.login);
        url.append("?n=");
        url.append(num);
        url.append("&p=");
        url.append(pass);
        url.append("&pid=");
        url.append(phoneId);

        HttpUtils loginRequst = new HttpUtils();
        String str = url.toString();
        loginRequst.send(HttpRequest.HttpMethod.POST, str, new RequestCallBack<String>() {
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);

            }

            /**
             * 请求服务器成功
             * @param responseInfo
             */
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (result != null && !"".equals(result) && !"null".equals(result)) {
                    int flag = -1;
                    String s = ";;;";
                    //登陆成功
                    if (result.contains(s)) {
                        SharedPreferences.Editor editor = preferences.edit();
                        String str[] = result.split(s);
                        flag = Integer.parseInt(str[0]);
                        String jsonString = str[1];
                        Type studentType = new TypeToken<StudentBo>() {
                        }.getType();
                        Gson gson = new Gson();
                        StudentBo bo = gson.fromJson(jsonString, studentType);
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        intent.putExtra("username", bo.getUSERNAME());
                        intent.putExtra("classname", bo.getCLASSNAME());
                        editor.putString("num", num);
                        editor.putString("pass", pass);
                        editor.putString("oldpass", pass);
                        editor.putString("pid", phoneId);
                        editor.putString("username", bo.getUSERNAME());
                        editor.putString("classname", bo.getCLASSNAME());
                        editor.commit();
                        switch (flag) {
                            case 2:
                                T.show(LoginActivity.this.getApplicationContext(), "请即时修改登陆密码");
                                break;
                        }

                        LoginActivity.this.startActivity(intent);
                        LoginActivity.this.finish();
                    } else {
                        flag = Integer.parseInt(result);
                        switch (flag) {

                            case 1:
                                T.show(LoginActivity.this, "设备串号不统一，请使用自己的手机登陆，或更换设备串号");
                                break;

                            case 3:
                                T.show(LoginActivity.this, "用户账号密码不匹配");

                                break;

                        }
                    }

                }
            }

            /**
             * 请求服务器失败
             * @param e
             * @param s
             */
            @Override
            public void onFailure(HttpException e, String s) {
                T.show(LoginActivity.this, "网络不通");
            }
        });
        url.delete(0, url.length());
        return b;
    }

    /**
     * 获取手机信息
     */
    private void initPhoneId() {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        phoneId = tm.getDeviceId();


    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.bt_login:
                num = mEt_num.getText().toString();
                pass = mEt_pass.getText().toString();
                login();
//
//              intent.setClass(this,MainActivity.class);
//                this.startActivity(intent);
//                this.finish();
                break;
            case R.id.bt_register://修改设备串号
                intent.setClass(this, ModifyPhoneInfoActivity.class);
                this.startActivity(intent);
                break;
            case R.id.bt_forgetPass://忘记密码
                intent.setClass(this, ForgetPassActivity.class);
                this.startActivity(intent);
                break;
        }
    }
}
