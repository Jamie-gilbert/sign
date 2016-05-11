package chek_ins.com.sign.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import chek_ins.com.sign.activity.WifiActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Administrator on 2016/4/15.
 */
public class ReceiverAsyncTask extends AsyncTask<String, Integer, String> {
    private ServerSocket serverSocket;
    private Socket socket;
    private Context context;
    private String fileName;
    private static final int SERVERPORT = 8191;
    private DataInputStream in;
    public ReceiverAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {//后台
        {

            try {
                serverSocket = new ServerSocket(SERVERPORT);
                socket = serverSocket.accept();
                in = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                byte[] data = new byte[1024];
                int len = in.read(data);
                String str[] = new String(data, 0, len, "utf-8").split(";");
                int size = Integer.parseInt(str[1]);
                Log.i("xxx", "ssss " + size);
                outputStream.write("start".getBytes("utf-8"));
                outputStream.flush();

                int receviersize = 0;
                int bufferSize = 20480; // 20K
                byte[] buf = new byte[bufferSize];
                String filePath = context.getFilesDir().getAbsolutePath() + File.separator + "sign" + File.separator + str[0];
                fileName = str[0];
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "sign" + File.separator + str[0];
                }
                File receiverFile = new File(filePath);
                if (!receiverFile.exists()) {
                    receiverFile.getParentFile().mkdirs();
                    receiverFile.createNewFile();
                }
                T.show(context, filePath);
                FileOutputStream out = new FileOutputStream(receiverFile);

                while ((len = in.read(buf, 0, buf.length)) != -1) {
                    receviersize += len;
                    float p = ((float) receviersize / (float) size) * 100;
                    out.write(buf, 0, len);
                    publishProgress((int) p);
                    Thread.sleep(500);

                }
                out.flush();
                outputStream.close();
                out.close();
                in.close();
                try {
                    socket.close();
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {

                try {
                    if (socket != null) {

                        socket.close();
                    }
                    if (serverSocket != null) {
                        serverSocket.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
        return null;
    }

    @Override
    protected void onPreExecute() {//开始线程
        WifiActivity.mPb_file.setVisibility(View.VISIBLE);
        WifiActivity.mIv_wifiPciture.setVisibility(View.GONE);
    }

    @Override
    protected void onPostExecute(String s) {//线程结束
        T.show(context.getApplicationContext(), fileName + "已接受");
        WifiActivity.mPb_file.setProgress(0);
        WifiActivity.mPb_file.setVisibility(View.GONE);
        WifiActivity.mIv_wifiPciture.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        //onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
        WifiActivity.mPb_file.setProgress(values[0]);
    }


}
