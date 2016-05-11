package chek_ins.com.sign.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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
import chek_ins.com.sign.utils.PublicData;
import chek_ins.com.sign.utils.T;

/**
 * Created by Administrator on 2016/4/26.
 */
public class ModifyPhoneInfoActivity extends Activity implements View.OnClickListener {

    private Button mBt_getPhoneId, mBt_sure;
    private EditText mEt_userid, mEt_pass, mEt_phonenum;
    private HttpUtils httpUtils;
    private String pid = null;
    private StringBuffer url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_modifyphoneinfo);
        mBt_getPhoneId = (Button) this.findViewById(R.id.bt_modifyinfo_phoneid);
        mBt_sure = (Button) this.findViewById(R.id.bt_modifyinfo_sure);
        mEt_pass = (EditText) this.findViewById(R.id.et_modifyinfo_pass);
        mEt_phonenum = (EditText) this.findViewById(R.id.et_modifyinfo_phonenum);
        mEt_userid = (EditText) this.findViewById(R.id.et_modifyinfo_userid);
        mBt_getPhoneId.setOnClickListener(this);
        mBt_sure.setOnClickListener(this);
        url = new StringBuffer();
        httpUtils = new HttpUtils();
        super.onCreate(savedInstanceState);
    }

    /**
     * 获取手机信息
     */
    private String initPhoneId() {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneId = tm.getDeviceId();
        return phoneId;


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_modifyinfo_phoneid:
                pid = initPhoneId();
                if (pid != null)
                    mBt_getPhoneId.setText(pid);
                break;

            case R.id.bt_modifyinfo_sure:
                url.append(PublicData.IP);
                url.append(PublicData.Port);
                url.append(PublicData.Websit);
                url.append(PublicData.forgetpass);
                String userid = mEt_userid.getText().toString();
                String pass = mEt_pass.getText().toString();
                String phoneNum = mEt_phonenum.getText().toString();
                if (userid == null || "".equals(userid)) {
                    T.show(this, "请输入账号");
                    return;
                }
                if (pass == null || "".equals(pass)) {
                    T.show(this, "请输入密码");
                    return;
                }
                RequestParams params = new RequestParams();
                params.addBodyParameter("type", "pinfo");
                params.addBodyParameter("userid", userid);
                params.addBodyParameter("pass", pass);
                if (phoneNum != null && "".equals(phoneNum)) {
                    params.addBodyParameter("phoneNum", phoneNum);
                }
                if (pid != null) {
                    params.addBodyParameter("pid", pid);
                }

                httpUtils.send(HttpRequest.HttpMethod.POST, url.toString(), params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        if ("1".equals(result)) {
                            T.show(ModifyPhoneInfoActivity.this.getApplicationContext(), "修改成功");
                            Intent intent = new Intent(ModifyPhoneInfoActivity.this, LoginActivity.class);
                            ModifyPhoneInfoActivity.this.startActivity(intent);
                            ModifyPhoneInfoActivity.this.finish();
                        }else  if("0".equals(result)){
                            AlertDialog.Builder builder = new AlertDialog.Builder(ModifyPhoneInfoActivity.this);
                            builder.setTitle("提示");
                            builder.setMessage("账号和密码输入有误");
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
                        T.show(ModifyPhoneInfoActivity.this.getApplicationContext(), "网络不通");
                    }
                });
                url.delete(0, url.length());
                break;

        }
    }
}
