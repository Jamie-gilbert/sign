package chek_ins.com.sign.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.lang.reflect.Method;

import chek_ins.com.sign.R;
import chek_ins.com.sign.application.ExitApplication;
import dalvik.system.DexClassLoader;

/**
 * Created by Administrator on 2016/4/2.
 */
public class ToolActivity extends Activity implements View.OnClickListener {
    private Button mBt_updatepass, mBt_transferFile;
    private String apkFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_tool);
        super.onCreate(savedInstanceState);
        ExitApplication.getIntance().addActivity(this);
        apkFile = this.getFilesDir().getAbsolutePath();
        initView();
    }

    private void initView() {
        mBt_updatepass = (Button) this.findViewById(R.id.bt_update_pass);
        mBt_updatepass.setOnClickListener(this);
        mBt_transferFile = (Button) this.findViewById(R.id.bt_file_transfer);
        mBt_transferFile.setVisibility(View.VISIBLE);
        mBt_transferFile.setOnClickListener(this);
    }

    private void jugdeAPK() {
        StringBuffer wifiAPK = new StringBuffer();
        String className="";
        wifiAPK.append(apkFile);
        wifiAPK.append(File.separator);
        wifiAPK.append("wifi.apk");
        File file = new File(wifiAPK.toString());
        if (file.exists()) {
            mBt_transferFile.setVisibility(View.VISIBLE);
            mBt_transferFile.setOnClickListener(this);
            //加载apk
           // loadAPK(wifiAPK.toString(),className);
        } else {
            mBt_transferFile.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void loadAPK(String apkPath, String className) {

        ClassLoader classLoder = ClassLoader.getSystemClassLoader();
        DexClassLoader dexClassLoader = new DexClassLoader(apkPath, apkFile, null, classLoder);
        try {
            Class<?> clazz= dexClassLoader.loadClass(className);
            Method onstart=clazz.getMethod("onStart");
            Object object=clazz.newInstance();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.bt_update_pass:
                intent.setClass(this, UpdatePassActivity.class);
                this.startActivity(intent);
                break;
            case R.id.bt_file_transfer:
                intent.setClass(this, WifiActivity.class);
                this.startActivity(intent);
                break;
        }
    }
}
