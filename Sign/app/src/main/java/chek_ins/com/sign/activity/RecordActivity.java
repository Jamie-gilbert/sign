package chek_ins.com.sign.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import chek_ins.com.sign.R;
import chek_ins.com.sign.application.ExitApplication;
import chek_ins.com.sign.bo.RecordBo;
import chek_ins.com.sign.dao.RecordDao;
import chek_ins.com.sign.utils.L;
import chek_ins.com.sign.utils.NetUtils;
import chek_ins.com.sign.utils.PublicData;
import chek_ins.com.sign.utils.T;

/**
 * Created by Administrator on 2016/4/2.
 */
public class RecordActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private GridView mGv_course;
    private CourseGridAdapter adapter;
    private List<RecordBo> data;
    private SharedPreferences preferences;
    private RecordDao recordDao;
    private HorizontalScrollView scrollView;
    private EditText mEt_coursename, mEt_room, mEt_teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_record);
        super.onCreate(savedInstanceState);
        ExitApplication.getIntance().addActivity(this);
        recordDao = new RecordDao(this);
        mGv_course = (GridView) this.findViewById(R.id.gv_course);
        scrollView = (HorizontalScrollView) findViewById(R.id.home_scroll);
        mGv_course.setNumColumns(7);
        data = new ArrayList<>();

        for (int i = 0; i < 70; i++) {
            RecordBo bo = new RecordBo();
            bo.setCourseName(" ");
            bo.setAbsence(" ");
            bo.setVacation(" ");
            bo.setSign(" ");
            bo.setTime(" ");
            bo.setTeacher(" ");
            bo.setClassroom(" ");
            data.add(bo);
        }
        dataSort();

        adapter = new CourseGridAdapter(this);
        mGv_course.setAdapter(adapter);
        mGv_course.setOnItemClickListener(this);
        mGv_course.setOnItemLongClickListener(this);
        boolean flag = NetUtils.isNetConnected(this);

        if (flag) {
            initData();
        } else {
            T.show(this.getApplicationContext(), "请连接网络，获取最新签到记录！");
        }


    }

    /**
     * 获得用户的签到数据
     */
    private void initData() {
        preferences = this.getApplicationContext().getSharedPreferences(PublicData.spName, MODE_APPEND);
        HttpUtils utils = new HttpUtils();
        StringBuffer url = new StringBuffer();
        url.append(PublicData.IP);
        url.append(PublicData.Port);
        url.append(PublicData.Websit);
        url.append(PublicData.record);
        RequestParams params = new RequestParams();
        String num = preferences.getString("num", null);
        params.addBodyParameter("userid", num);
        utils.send(HttpRequest.HttpMethod.POST, url.toString(), params, new RequestCallBack<String>() {
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                Gson gson = new Gson();
                Type recordType = new TypeToken<List<RecordBo>>() {
                }.getType();
                List<RecordBo> bos = gson.fromJson(result, recordType);
                recordDao.updateRecord(bos);
                dataSort();
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(HttpException e, String s) {

                dataSort();
                adapter.notifyDataSetChanged();
                T.show(RecordActivity.this.getApplicationContext(), "网络不通");
            }
        });

    }

    /**
     * 给数据排序
     */
    private void dataSort() {
//从数据中获取记录
        List<RecordBo> bos = recordDao.getData();
        for (int i = 0; i < bos.size(); i++) {
            String time = bos.get(i).getTime();
            if (time != null) {
                int x = 0, y = 0;
                String strs[] = time.split(" ");
                if (strs[0].equals("周一")) {
                    x = 0;
                } else if (strs[0].equals("周二")) {
                    x = 1;
                } else if (strs[0].equals("周三")) {
                    x = 2;
                } else if (strs[0].equals("周四")) {
                    x = 3;
                } else if (strs[0].equals("周五")) {
                    x = 4;
                } else if (strs[0].equals("周六")) {
                    x = 5;
                } else if (strs[0].equals("周日")) {
                    x = 6;
                }

                if (strs[1].indexOf("1") >= 0) {
                    y = 0;
                    data.set(y * 7 + x, bos.get(i));
                }
                if (strs[1].indexOf("2") >= 0) {
                    y = 1;
                    data.set(y * 7 + x, bos.get(i));
                }
                if (strs[1].indexOf("3") >= 0) {
                    y = 2;
                    data.set(y * 7 + x, bos.get(i));
                }
                if (strs[1].indexOf("4") >= 0) {
                    y = 3;
                    data.set(y * 7 + x, bos.get(i));
                }
                if (strs[1].indexOf("5") >= 0) {
                    y = 4;
                    data.set(y * 7 + x, bos.get(i));
                }
                if (strs[1].indexOf("6") >= 0) {
                    y = 5;
                    data.set(y * 7 + x, bos.get(i));
                }
                if (strs[1].indexOf("7") >= 0) {
                    y = 6;
                    data.set(y * 7 + x, bos.get(i));
                }
                if (strs[1].indexOf("8") >= 0) {
                    y = 7;
                    data.set(y * 7 + x, bos.get(i));
                }
                if (strs[1].indexOf("9") >= 0) {
                    y = 8;
                    data.set(y * 7 + x, bos.get(i));
                }
                if (strs[1].indexOf("10") >= 0) {
                    y = 9;
                    data.set(y * 7 + x, bos.get(i));
                }
//                if (strs[1].indexOf("1") >= 0) {
//                    y = 0;
//                }else
//                if (strs[1].indexOf("3") >= 0) {
//                    y = 1;
//
//                }else
//                if (strs[1].indexOf("5") >= 0) {
//                    y = 2;
//                }else
//                if (strs[1].indexOf("7") >= 0) {
//                    y = 3;
//                }
//                else
//                if (strs[1].indexOf("9") >= 0) {
//                    y = 4;
//
//                }
//                data.set(y * 7 + x, bos.get(i));

            }
        }
        bos = recordDao.getModifyData();
        if (bos != null) {
            for (RecordBo bo : bos) {
                if (bo != null) {
                    String p = bo.getUserId();
                    data.set(Integer.parseInt(p), bo);
                }
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RecordBo bo = data.get(position);
        if (bo != null) {
            String sign = bo.getSign();
            String absence = bo.getAbsence();
            String vacation = bo.getVacation();
            StringBuffer buffer = new StringBuffer();
            if (" ".equals(sign) && " ".equals(absence) && " ".equals(vacation)) {
                buffer.append("童鞋，这节课你不需要签到");
            } else {
                buffer.append("出勤：");
                buffer.append(sign);
                buffer.append(" 请假：");
                buffer.append(vacation);
                buffer.append(" 缺勤：");
                buffer.append(absence);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(buffer.toString());
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
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        modifyCourse(position);
        return true;
    }

    /**
     * 修改签到记录表中的课程信息
     * @param position
     */
    private void modifyCourse(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_modifycourse, null);
        mEt_coursename = (EditText) view.findViewById(R.id.et_dilog_modify_coursename);
        mEt_teacher = (EditText) view.findViewById(R.id.et_dilog_modify_teacher);
        mEt_room = (EditText) view.findViewById(R.id.et_dilog_modify_room);
        RecordBo bo = data.get(position);
        if (bo != null) {
            String coursename = bo.getCourseName();
            String room = bo.getClassroom();
            String teacher = bo.getTeacher();
            if (coursename != null & !"".equals(coursename) && !" ".equals(coursename)) {
                mEt_coursename.setText(coursename);
            } else {
                mEt_coursename.setHint("课程");
            }
            if (room != null & !"".equals(room) && !" ".equals(room)) {
                mEt_room.setText(room);
            } else {
                mEt_room.setHint("教室");
            }
            if (teacher != null & !"".equals(teacher) && !" ".equals(teacher)) {
                mEt_teacher.setText(teacher);
            } else {
                mEt_teacher.setHint("教师姓名");
            }
        }
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String t = mEt_teacher.getText().toString();
                String r = mEt_room.getText().toString();
                String c = mEt_coursename.getText().toString();
                int flag = 1;
                if (t != null && !"".equals(t)) {
                    data.get(position).setTeacher(t);

                } else {
                    flag = 0;
                    T.show(RecordActivity.this.getApplicationContext(), "教师姓名不能为空");
                }
                if (r != null && !"".equals(r)) {
                    data.get(position).setClassroom(r);
                } else {
                    flag = 0;
                    T.show(RecordActivity.this.getApplicationContext(), "教室不能为空");
                }
                if (c != null && !"".equals(c)) {
                    data.get(position).setCourseName(c);
                } else {
                    flag = 0;
                    T.show(RecordActivity.this.getApplicationContext(), "课程名称不能为空");
                }
                if (flag == 1) {
                    recordDao.modifyRocurse(data.get(position), position);
                    dialog.dismiss();
                }
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();


    }

    class CourseGridAdapter extends BaseAdapter {


        private LayoutInflater mInflater;

        public CourseGridAdapter(Context context) {

            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_coursegrid, null);
                viewHolder.mTv_couseName = (TextView) convertView.findViewById(R.id.tv_coursename);
                viewHolder.mTv_teacher = (TextView) convertView.findViewById(R.id.tv_teacher);
                viewHolder.mTv_room = (TextView) convertView.findViewById(R.id.tv_room);
                viewHolder.mTv_time = (TextView) convertView.findViewById(R.id.tv_time);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            RecordBo bo = data.get(position);
            viewHolder.mTv_couseName.setText(bo.getCourseName());
            viewHolder.mTv_time.setText(bo.getTime());
            viewHolder.mTv_teacher.setText(bo.getTeacher());
            viewHolder.mTv_room.setText(bo.getClassroom());


            return convertView;
        }
    }

    class ViewHolder {
        public TextView mTv_couseName;
        public TextView mTv_time;
        public TextView mTv_teacher;
        public TextView mTv_room;

    }
}
