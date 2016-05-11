package chek_ins.com.sign.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2016/4/5.
 */
public class NetUtils {
    public static boolean isNetConnected(Context context) {
        boolean flag ;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }
}
