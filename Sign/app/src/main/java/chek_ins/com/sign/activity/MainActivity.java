package chek_ins.com.sign.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.FileNotFoundException;

import chek_ins.com.sign.R;
import chek_ins.com.sign.application.ExitApplication;
import chek_ins.com.sign.qr_codescan.MipcaActivityCapture;
import chek_ins.com.sign.service.BDLoactionListener;
import chek_ins.com.sign.utils.L;
import chek_ins.com.sign.utils.PublicData;
import chek_ins.com.sign.utils.T;
import chek_ins.com.sign.view.CircleImageView;

public class MainActivity extends Activity implements OnClickListener {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private TextView mTv_username, mTv_urlShow;
    private SharedPreferences preferences;
    private RelativeLayout mRl_sign, mRl_loaction, mRl_tool, mRl_scan;
    private LocationClient mLocationClient;
    private BDLocationListener mLocListener = null;
    public static CircleImageView mCIv_photo;
    private Handler handler;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExitApplication.getIntance().addActivity(this);
        setContentView(R.layout.activity_main);
        initLocation();
        initView();
        handler = new Handler();

    }

    private void initLocation() {
        preferences = this.getApplicationContext().getSharedPreferences(PublicData.spName, Context.MODE_APPEND);
        // 定位
        mLocationClient = new LocationClient(getApplicationContext());
        mLocListener = new BDLoactionListener(this);
        mLocationClient.registerLocationListener(mLocListener);

        // 设置定位参数
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000*60;//1min获取一次位置
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        //option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        //   option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        //   option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();


    }

    /**
     * 初始化界面view
     */
    private void initView() {
        preferences = this.getApplicationContext().getSharedPreferences(PublicData.spName, MODE_APPEND);
        mTv_username = (TextView) this.findViewById(R.id.tv_username);
        mTv_urlShow = (TextView) this.findViewById(R.id.tv_urlshow);
        mCIv_photo = (CircleImageView) this.findViewById(R.id.civ_photo);
        String photo = preferences.getString("photo", null);
        if (photo != null) {
            Uri uri = Uri.parse(photo);
            if (uri != null) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri));
                    mCIv_photo.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        mCIv_photo.setOnClickListener(this);
        String username = preferences.getString("username", null);
        if (username != null) {
            mTv_username.setText(username);
        } else {
            mTv_username.setText(" ");
        }
        mRl_loaction = (RelativeLayout) this.findViewById(R.id.rl_location);
        mRl_tool = (RelativeLayout) this.findViewById(R.id.rl_tool);
        mRl_scan = (RelativeLayout) this.findViewById(R.id.rl_scan);
        mRl_sign = (RelativeLayout) this.findViewById(R.id.rl_sign);

        mRl_sign.setOnClickListener(this);
        mRl_tool.setOnClickListener(this);
        mRl_loaction.setOnClickListener(this);
        mRl_scan.setOnClickListener(this);

    }

    /**
     * 接受扫描完返回的信息
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String result = bundle.getString("result");
                    if (result.contains("sign?id")) {
                        scan2Server(result);
                        mTv_urlShow.setVisibility(View.GONE);
                    } else {
                        mTv_urlShow.setVisibility(View.VISIBLE);
                        mTv_urlShow.setText(result);
                    }
                }
                break;
        }
    }

    /**
     * 扫描二维码之后请求服务器
     *
     * @param result
     */
    private void scan2Server(String result) {
        L.log(result);
        String[] r = result.split("[?]");
        String p[] = r[1].split("&");
        String id = p[0].split("=")[1];
        String uuid = p[1].split("=")[1];
        String time = p[2].split("=")[1];
        StringBuffer url = new StringBuffer();
        url.append(PublicData.IP);
        url.append(PublicData.Port);

        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        String userid = preferences.getString("num", null);
        params.addBodyParameter("userid", userid);
        params.addBodyParameter("cid", id);
        params.addBodyParameter("uuid", uuid);
        params.addBodyParameter("time", time);
        url.append(r[0]);
        L.log(url.toString());
        utils.send(HttpRequest.HttpMethod.POST, url.toString(), params, new RequestCallBack<String>() {
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                T.show(MainActivity.this, result);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                T.show(MainActivity.this, "网络不通");
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rl_scan://扫描二维码
                intent.setClass(MainActivity.this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                break;
            case R.id.rl_location: //定位
                mLocationClient.requestLocation();
                break;
            case R.id.rl_tool: //设置
                intent.setClass(this, ToolActivity.class);
                this.startActivity(intent);
                break;
            case R.id.rl_sign: //签到记录
                intent.setClass(this, RecordActivity.class);
                this.startActivity(intent);
                break;
            case R.id.civ_photo:
                intent.setClass(this, PhotoSelectActivity.class);
                this.startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                flag = !flag;
                T.show(this,"再按一次退出");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        flag = false;
                    }
                }, 2000);
                if (flag == false) {
                    ExitApplication.getIntance().exitActivity();
                }
                break;
        }

        return true;
    }
}
