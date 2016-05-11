package chek_ins.com.sign.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import chek_ins.com.sign.activity.WifiActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Administrator on 2016/4/14.
 */
public class SendAsyncTask extends AsyncTask<String, Integer, String> {
    public Socket socket;
    public Context context;
    public boolean isRun = true;
    public static final int SOCKETPORT = 8191;
    private WifiUtil wifiUtil;
    private String filePath;
    private String fileName;
    private String serverIP;

    public SendAsyncTask(Context context, String serverIP, String filePath) {
        this.context = context;
        this.serverIP = serverIP;
        this.filePath = filePath;
        wifiUtil = new WifiUtil(context);
    }

    @Override
    protected String doInBackground(String... params) {
        {

            try {
                socket = new Socket(serverIP, SOCKETPORT);
                if (socket == null) {
                    return null;
                }

                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream((socket.getInputStream()));
                Log.i("xxx", filePath);
                FileInputStream read = new FileInputStream(new File(filePath));
                int size = read.available();
                fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
                out.write((fileName + ";" + size).getBytes("utf-8"));
                out.flush();
                byte[] data = new byte[1024];
                int len = in.read(data);
                String start = new String(data, 0, len, "utf-8");

                if (start.equals("start")) {
                    int sendSize = 0;
                    int bufferSize = 20480; // 20K
                    byte[] buf = new byte[bufferSize];
                    while ((len = read.read(buf, 0, buf.length)) != -1) {
                        sendSize += len;
                        float p = ((float) sendSize / (float) size) * 100;
                        out.write(buf, 0, len);
                        Thread.sleep(500);
                        publishProgress((int) p);

                    }
                    out.flush();
                }

                socket.shutdownOutput();
                out.close();
                in.close();
                read.close();
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        WifiActivity.mPb_file.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onPostExecute(String s) {
        T.show(context.getApplicationContext(), fileName + "已发送");
        WifiActivity.mPb_file.setVisibility(View.GONE);
        WifiActivity.mIv_wifiPciture.setVisibility(View.VISIBLE);
//        T
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

        WifiActivity.mPb_file.setProgress(values[0]);
    }


}
