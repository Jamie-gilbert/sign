package chek_ins.com.sign.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import chek_ins.com.sign.R;
import chek_ins.com.sign.application.ExitApplication;
import chek_ins.com.sign.utils.L;
import chek_ins.com.sign.utils.PublicData;
import chek_ins.com.sign.utils.T;

/**
 * Created by Administrator on 2016/4/2.
 */
public class UpdatePassActivity extends Activity implements View.OnClickListener {

    private Button mBt_sure, mBt_cancel;
    private EditText mEt_num, mEt_pass, mEt_newpass, mEt_renewpass;
    private SharedPreferences preferences;
    private String username, newpass,oldpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_updatepass);
        super.onCreate(savedInstanceState);
        ExitApplication.getIntance().addActivity(this);
        initView();
    }

    /**
     * 初始化界面view
     */
    private void initView() {
        preferences = this.getApplicationContext().getSharedPreferences(PublicData.spName, MODE_APPEND);
        username = preferences.getString("num", null);

        mBt_cancel = (Button) this.findViewById(R.id.bt_update_cancel);
        mBt_sure = (Button) this.findViewById(R.id.bt_update_sure);
        mBt_cancel.setOnClickListener(this);
        mBt_sure.setOnClickListener(this);
        mEt_newpass = (EditText) this.findViewById(R.id.et_update_newpass);
        mEt_num = (EditText) this.findViewById(R.id.et_update_num);
        mEt_pass = (EditText) this.findViewById(R.id.et_update_pass);
        mEt_renewpass = (EditText) this.findViewById(R.id.et_update_repass);
        if (username != null) {
            mEt_num.setText(username);
        } else {
            T.show(this.getApplicationContext(), "用户未登录");
            this.finish();
        }
    }

    /**
     * 将账号和新的密码发送到服务器
     *
     * @param username
     * @param newPass
     */
    private void sendNewPass2Server(String username, String newPass) {
        newpass = newPass;
        HttpUtils httpUtils = new HttpUtils();
        StringBuffer url = new StringBuffer();

        url.append(PublicData.IP);
        url.append(PublicData.Port);
        url.append(PublicData.Websit);
        url.append(PublicData.updatepass);

        final RequestParams params = new RequestParams();
        params.addBodyParameter("userid", username);
        params.addBodyParameter("newpass", newPass);
        httpUtils.send(HttpRequest.HttpMethod.POST, url.toString(), params, new RequestCallBack<String>() {
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if ("-1".equals(result) || "0".equals(result)) {
                    T.show(UpdatePassActivity.this.getApplicationContext(), "修改密码失败，请重试");
                } else {
                    T.show(UpdatePassActivity.this.getApplicationContext(), "修改密码成功");
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("oldpass",oldpass);
                    editor.putString("pass", newpass);
                    editor.commit();
                    Intent intent = new Intent();
                    intent.setClass(UpdatePassActivity.this, LoginActivity.class);
                    ExitApplication.getIntance().exitActivity();
                    UpdatePassActivity.this.startActivity(intent);


                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                T.show(UpdatePassActivity.this.getApplicationContext(), "网络不通！");

            }
        });
    }

    /**
     * 对输入如的密码和新密码进行比较
     */
    private void sureClick() {
         oldpass = preferences.getString("pass", null);
        String pass = mEt_pass.getText().toString();
        String newpass = mEt_newpass.getText().toString();
        String repass = mEt_renewpass.getText().toString();
        if (oldpass != null) {
            if (pass != null) {
                if (oldpass.equals(pass)) {
                    if (newpass != null) {
                        if (repass != null) {
                            if (repass.equals(newpass)) {
                                L.log(repass + "__" + newpass);
                                sendNewPass2Server(username, repass);
                            } else {
                                T.show(this.getApplicationContext(), "两次输入的新密码不一致");
                            }
                        } else {
                            T.show(this.getApplicationContext(), "请再一次输入新密码");
                        }
                    } else {
                        T.show(this.getApplicationContext(), "请输入新密码");
                    }
                } else {
                    T.show(this.getApplicationContext(), "旧密码输入不正确");
                }
            } else {
                T.show(this.getApplicationContext(), "用户未登录");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_update_cancel:
                this.finish();
                break;
            case R.id.bt_update_sure:
                sureClick();
                break;
        }
    }
}
