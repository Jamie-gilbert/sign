package chek_ins.com.sign.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.lang.reflect.Type;

import chek_ins.com.sign.bo.LocationBo;
import chek_ins.com.sign.utils.L;
import chek_ins.com.sign.utils.PublicData;
import chek_ins.com.sign.utils.T;

/**
 * Created by Administrator on 2016/3/26.
 */
public class BDLoactionListener implements BDLocationListener {
    private SharedPreferences preferences = null;
    private Context context;

    public BDLoactionListener(Context context) {
        this.context = context;
        preferences = context.getApplicationContext().getSharedPreferences(PublicData.spName, Context.MODE_APPEND);
    }


    @Override
    public void onReceiveLocation(BDLocation loc) {
        // TODO Auto-generated method stub
        if (loc == null) {
            return;
        }
        LocationBo locationBo = new LocationBo();
        locationBo.setUSERID(preferences.getString("num", null));
        locationBo.setLATITUDE(loc.getLatitude() + "");
        locationBo.setLONTITUDE(loc.getLongitude() + "");
        locationBo.setRADIUS(loc.getRadius() + "");
        locationBo.setLOCALTIME(loc.getTime());
        locationBo.setALTITUDE(loc.getAltitude()+"");
        Type localtionType = new TypeToken<LocationBo>() {
        }.getType();
        Gson gson = new Gson();
        String data = gson.toJson(locationBo, localtionType);

        send2Server(data);
        StringBuffer sb = new StringBuffer(256);
        sb.append("time:");
        sb.append(loc.getTime());
        sb.append("\nerror code:");
        sb.append(loc.getLocType());
        sb.append("\nlatitude(纬度):");
        sb.append(loc.getLatitude());
        sb.append("\nlontitude(经度):");
        sb.append(loc.getLongitude());
        sb.append("\nradius:");
        sb.append(loc.getRadius());
        sb.append("\naltitude(海拔):");
        sb.append(loc.getAltitude());

        if (loc.getLocType() == BDLocation.TypeGpsLocation) {
            sb.append("\nspeed:");
            sb.append(loc.getSpeed());
            sb.append("\nsatellite:");
            sb.append(loc.getSatelliteNumber());
        } else if (loc.getLocType() == BDLocation.TypeNetWorkLocation) {
            sb.append("\naddr:");
            sb.append(loc.getAddrStr());
        }
        //如果是GPS定位，addr为null，只有当网络定位时，addr才有值
        T.show(context.getApplicationContext(), "定位类型：" + loc.getLocType() + ", 定位addr:" + loc.getAddrStr()+"海拔："+loc.getAltitude());
        L.log(sb.toString());
    }


    private void send2Server(String data) {
        HttpUtils httpUtils = new HttpUtils();
        StringBuffer buffer = new StringBuffer();
        buffer.append(PublicData.IP);
        buffer.append(PublicData.Port);
        buffer.append(PublicData.Websit);
        buffer.append(PublicData.location);

        RequestParams params = new RequestParams();
        params.addBodyParameter("data", data);
        httpUtils.send(HttpRequest.HttpMethod.POST, buffer.toString(), params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                Toast.makeText(context.getApplicationContext(), "succss0" + result, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }

}

