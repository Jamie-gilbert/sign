package chek_ins.com.sign.activity;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import chek_ins.com.sign.R;
import chek_ins.com.sign.application.ExitApplication;
import chek_ins.com.sign.utils.L;
import chek_ins.com.sign.utils.PublicData;
import chek_ins.com.sign.utils.ReceiverAsyncTask;
import chek_ins.com.sign.utils.SendAsyncTask;
import chek_ins.com.sign.utils.T;
import chek_ins.com.sign.utils.WifiUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/14.
 */
public class WifiActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Button mBt_createWifi, mBt_connectWifi;
    private WifiUtil wifiUtil;
    private String mSSID = "sign";
    private String mPASSWORD = "123456789";
    private int mTYPE = 3;
    public List<Map<String, String>> resultList;
    private ListView mLv_wifiscanResult;
    private SimpleAdapter adapter;
    private WifiScanRecevier recevier;
    public static ImageView mIv_wifiPciture;
    private static int Recevierflag = -1;//判断recevier是否绑定
    private SharedPreferences preferences;
    public static ProgressBar mPb_file;
    private Handler handler = new Handler();
    private boolean connectFlag = false;
    private  ProgressBar mPb_progress;
    private FrameLayout mFl_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wificonnection);
        super.onCreate(savedInstanceState);
        ExitApplication.getIntance().addActivity(this);
        preferences = this.getApplicationContext().getSharedPreferences(PublicData.spName, MODE_APPEND);
        mSSID = preferences.getString("num", mSSID);
        L.log(mSSID);
        mBt_createWifi = (Button) this.findViewById(R.id.bt_createWifiHot);
        mBt_connectWifi = (Button) this.findViewById(R.id.bt_connectWifi);
        mIv_wifiPciture = (ImageView) this.findViewById(R.id.iv_wifi_picture);
        mPb_file = (ProgressBar) this.findViewById(R.id.pb_file);
        mPb_progress= (ProgressBar) this.findViewById(R.id.pb_progress);
        mFl_show= (FrameLayout) this.findViewById(R.id.fl_show);
        mPb_file.setMax(100);
        mLv_wifiscanResult = (ListView) this.findViewById(R.id.lv_scanResult);
        resultList = new ArrayList<>();
        adapter = new SimpleAdapter(this, resultList, R.layout.item_result, new String[]{"ssid"}, new int[]{R.id.tv_item_result});
        mLv_wifiscanResult.setAdapter(adapter);
        wifiUtil = new WifiUtil(this);
        mBt_createWifi.setOnClickListener(this);
        mBt_connectWifi.setOnClickListener(this);
        mLv_wifiscanResult.setOnItemClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_createWifiHot://创建wifi热点，接受文件
                mFl_show.setVisibility(View.VISIBLE);
                mPb_progress.setVisibility(View.VISIBLE);
                final boolean flag = wifiUtil.openWifiAP(mSSID, mPASSWORD, mTYPE);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (flag) {

                            ReceiverAsyncTask receiverAsyncTask = new ReceiverAsyncTask(WifiActivity.this);
                            receiverAsyncTask.execute("11");
                        } else {
                            T.show(WifiActivity.this.getApplicationContext(), "接受文件失败，请重试");
                        }
                        mFl_show.setVisibility(View.GONE);
                        mPb_progress.setVisibility(View.GONE);
                    }
                }, 2000);
                break;
            case R.id.bt_connectWifi://扫描wifi热点，发送文件
                mFl_show.setVisibility(View.VISIBLE);
                mPb_progress.setVisibility(View.VISIBLE);
                wifiUtil.openWifi();
                wifiUtil.scanWifi();
                mLv_wifiscanResult.setVisibility(View.VISIBLE);
                mIv_wifiPciture.setVisibility(View.GONE);
                recevier = new WifiScanRecevier(this);
                this.registerReceiver(recevier, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                Recevierflag = 0;
                break;
        }
    }

    private void openFileManager() {
        mFl_show.setVisibility(View.GONE);
        mPb_progress.setVisibility(View.GONE);
        mLv_wifiscanResult.setVisibility(View.GONE);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        this.startActivityForResult(Intent.createChooser(intent, "请选择发送的文件"), 1);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String ssid = resultList.get(position).get("ssid");
        mFl_show.setVisibility(View.VISIBLE);
        mPb_progress.setVisibility(View.VISIBLE);
        connectWifiAP(ssid);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (connectFlag) {
                    openFileManager();
                } else {
                    T.show(WifiActivity.this.getApplicationContext(), "发送文件失败，请重试");
                }
            }
        }, 2000);

    }

    private void connectWifiAP(String ssid) {
        final String sid = ssid;
        synchronized (this) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (sid == null || "".equals(sid)) {
                        return;
                    }
                    WifiConfiguration configuration = wifiUtil.ConnectWifiConfiguration(sid, mPASSWORD, mTYPE);
                    connectFlag = wifiUtil.addNetWork(configuration);
                }
            }).start();
        }
    }

    /**
     * 扫描wifi的接收器
     */
    class WifiScanRecevier extends BroadcastReceiver {
        private WifiUtil wifiUtil;

        public WifiScanRecevier(Context context) {
            wifiUtil = new WifiUtil(context);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            mFl_show.setVisibility(View.GONE);
            mPb_progress.setVisibility(View.GONE);
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {

                List<ScanResult> results = wifiUtil.getScanResultList();
                if (results == null || results.size() == 0) {
                    return;
                }
                if (resultList != null && resultList.size() > 0) {
                    resultList.clear();
                }

                //加载扫描到的wifi热点
                for (ScanResult r : results) {
                    Map<String, String> map = new HashMap<>();
                    map.put("ssid", r.SSID);//wifi热点名称
                    map.put("bssid", r.BSSID);//wifi热点mac地址
                    resultList.add(map);
                }
                mIv_wifiPciture.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();


            }
            WifiActivity.this.unregisterReceiver(recevier);
            Recevierflag = 1;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String path = uri.getEncodedPath();
            sendFile(path);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendFile(String filePath) {
        String recevierIP = wifiUtil.int2IP(wifiUtil.getWifiDhcpInfo().serverAddress);
        SendAsyncTask sendAsyncTask = new SendAsyncTask(this, recevierIP, filePath);
        sendAsyncTask.execute("");
    }

    @Override
    protected void onDestroy() {
        if (Recevierflag == 0) {
            this.unregisterReceiver(recevier);
        }
        wifiUtil.closeWifi();
        wifiUtil.closeWifiAp();
        super.onDestroy();
    }
}
