package chek_ins.com.sign.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.HttpUtils;
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
 * Created by Administrator on 2016/4/25.
 */
public class ForgetPassActivity extends Activity implements View.OnClickListener {
    private EditText mEt_num, mEt_phoneNum, mEt_code, mEt_newPass, mEt_reNewPass;
    private Button mBt_getCode, mBt_sure;
    private HttpUtils httpUtils;
    private StringBuffer url;
    private int t = 0;
    private CodeTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forgetpass);
        super.onCreate(savedInstanceState);
        ExitApplication.getIntance().addActivity(this);
        httpUtils = new HttpUtils();
        initView();
    }

    private void initView() {
        mEt_code = (EditText) this.findViewById(R.id.et_forgetpass_code);
        mEt_newPass = (EditText) this.findViewById(R.id.et_forgetpass_pass);
        mEt_num = (EditText) this.findViewById(R.id.et_forgetpass_num);
        mEt_phoneNum = (EditText) this.findViewById(R.id.et_forgetpass_phonenum);
        mEt_reNewPass = (EditText) this.findViewById(R.id.et_forgetpass_repass);
        mBt_getCode = (Button) this.findViewById(R.id.bt_getVerifyCode);
        mBt_sure = (Button) this.findViewById(R.id.bt_forgetpass_sure);
        url = new StringBuffer();
        mBt_getCode.setOnClickListener(this);
        mBt_sure.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_getVerifyCode:
//                timer = new CodeTimer();
//                timer.execute("11");
                String pnum = mEt_phoneNum.getText().toString();
                if (pnum == null || "".equals(pnum)) {
                    T.show(this, "请输入手机号");
                } else {
                    timer = new CodeTimer();
                    timer.execute("11");
                    url.append(PublicData.IP);
                    url.append(PublicData.Port);
                    url.append(PublicData.Websit);
                    url.append(PublicData.forgetpass);
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("pnum", pnum);
                    params.addBodyParameter("type", "getcode");
                    L.log(url.toString());
                    httpUtils.send(HttpRequest.HttpMethod.POST, url.toString(), params, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            L.log("success---");

                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            L.log("fail-----");
                        }
                    });
                    url.delete(0, url.length());
                }

                break;
            case R.id.bt_forgetpass_sure:
                String usernum = mEt_num.getText().toString();
                pnum = mEt_phoneNum.getText().toString();
                String newpass = mEt_newPass.getText().toString();
                String renewpass = mEt_reNewPass.getText().toString();
                String vercode = mEt_code.getText().toString();
                url.append(PublicData.IP);
                url.append(PublicData.Port);
                url.append(PublicData.Websit);
                url.append(PublicData.forgetpass);
                if (!renewpass.equals(newpass)) {
                    T.show(this, "两次输入的密码不一致");
                    return;
                }
                RequestParams params = new RequestParams();
                params.addBodyParameter("pnum", pnum);
                params.addBodyParameter("type", "vercode");
                params.addBodyParameter("newpass", newpass);
                params.addBodyParameter("vercode", vercode);
                params.addBodyParameter("usernum", usernum);
                L.log(pnum + "--" + newpass);
                httpUtils.send(HttpRequest.HttpMethod.POST, url.toString(), params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        L.log("aaaaaaaaaa");
                        String result = responseInfo.result;
                        if ("2".equals(result)) {
                            T.show(ForgetPassActivity.this.getApplicationContext(), "密码重置成功，请重新登录");
                            Intent intent = new Intent(ForgetPassActivity.this, LoginActivity.class);
                            ForgetPassActivity.this.startActivity(intent);
                            ForgetPassActivity.this.finish();
                        } else if ("0".equals(result)) {
                            T.show(ForgetPassActivity.this, "验证码不正确");
                        } else if ("3".equals(result)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPassActivity.this);
                            builder.setTitle("提示");
                            builder.setMessage("用户账号和手机号不匹配或者账号不存在");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            builder.create().show();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                    }
                });
                url.delete(0, url.length());
                break;
        }
    }

    class CodeTimer extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
            mBt_getCode.setTextColor(ForgetPassActivity.this.getResources().getColor(R.color.gray));
            mBt_getCode.setClickable(false);
            mBt_getCode.setEnabled(false);
            mBt_getCode.setTextSize(13);
            mBt_getCode.setText("60秒后重新获取");
            mEt_code.setVisibility(View.VISIBLE);
            mEt_reNewPass.setVisibility(View.VISIBLE);
            mEt_phoneNum.setVisibility(View.VISIBLE);
            mEt_newPass.setVisibility(View.VISIBLE);
            mBt_sure.setVisibility(View.VISIBLE);
            mEt_num.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            mBt_getCode.setEnabled(true);
            mBt_getCode.setClickable(true);
            mBt_getCode.setTextColor(ForgetPassActivity.this.getResources().getColor(R.color.black));
            mBt_getCode.setText("获得验证码");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mBt_getCode.setText(values[0] + "秒后重新获取");
        }

        @Override
        protected String doInBackground(String... params) {
            while (t < 60) {
                if (isCancelled()) {
                    return null;
                }
                try {
                    Thread.sleep(1000);
                    t++;
                    publishProgress(60 - t);
                } catch (InterruptedException e) {
                    if (e instanceof InterruptedException) {
                        L.log("已退出");
                    } else {
                        e.printStackTrace();
                    }

                }
            }
            t = 0;

            return null;
        }
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel(true);

        }
        super.onDestroy();
    }
}
