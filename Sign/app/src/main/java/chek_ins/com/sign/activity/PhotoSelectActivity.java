package chek_ins.com.sign.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

import chek_ins.com.sign.R;
import chek_ins.com.sign.application.ExitApplication;
import chek_ins.com.sign.utils.PublicData;
import chek_ins.com.sign.utils.T;

/**
 * Created by Administrator on 2016/4/24.
 */
public class PhotoSelectActivity extends Activity implements View.OnClickListener {
    private Button mBt_takePhoto, mBt_selectOicture;
    private static int GETPICTURE = 1, TAKEPHOTO = 2, TAKECUT = 3;
    private File tempFile;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoselect);
        ExitApplication.getIntance().addActivity(this);
        preferences=this.getApplicationContext().getSharedPreferences(PublicData.spName,MODE_APPEND);

        mBt_takePhoto = (Button) this.findViewById(R.id.bt_takePhoto);
        mBt_selectOicture = (Button) this.findViewById(R.id.bt_selectPicture);
        mBt_selectOicture.setOnClickListener(this);
        mBt_takePhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_selectPicture:
                Intent getPicture = new Intent(Intent.ACTION_GET_CONTENT);
                getPicture.setType("image/*");
                this.startActivityForResult(getPicture, GETPICTURE);
                break;
            case R.id.bt_takePhoto:
                String filePath = this.getFilesDir().getAbsolutePath();
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "sign" + File.separator + "tempfile" + File.separator;
                }
                tempFile = new File(filePath, initPhotoName());
                if (!tempFile.exists()) {
                    tempFile.getParentFile().mkdirs();
                }
                Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                this.startActivityForResult(takePhoto, TAKEPHOTO);
                break;
        }
    }

    /**
     * 用时间作为照片的名称
     *
     * @return
     */
    private String initPhotoName() {
        Date date = new Date();
        return date.getTime() + ".jpg";
    }

    /**
     * 图片剪辑
     * @param uri
     */
    private void cutPicture(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", true);
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //输出图片的尺寸
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        this.startActivityForResult(intent, TAKECUT);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            switch (requestCode) {
                case 1://从 相册中选取
                    if (data != null) {
                        uri = data.getData();
                        if (uri != null) {
                            cutPicture(uri);
                        }
                    }
                    break;
                case 2: //拍照
                    uri = Uri.fromFile(tempFile);
                    cutPicture(uri);
                    break;
                case 3://图片剪辑
                    if (data != null) {
                        uri = data.getData();
                        if (uri != null) {
                            try {
                                Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri));
                                MainActivity.mCIv_photo.setImageBitmap(bitmap);
                                SharedPreferences.Editor editor=preferences.edit();
                                editor.putString("photo",uri.toString());
                                editor.commit();


                                this.finish();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
