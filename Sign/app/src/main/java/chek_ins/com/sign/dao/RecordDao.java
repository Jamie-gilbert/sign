package chek_ins.com.sign.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import chek_ins.com.sign.bo.RecordBo;

/**
 * Created by Administrator on 2016/4/5.
 */
public class RecordDao {
    DataBaseHelper helper;

    public RecordDao(Context context) {
        this.helper = new DataBaseHelper(context);
//        helper.onUpgrade(helper.getWritableDatabase(), 2, 2);
    }

    /**
     * 更新用户的签到记录
     *
     * @param bos
     */
    public void updateRecord(List<RecordBo> bos) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "delete from record";
        db.execSQL(sql);

        sql = "INSERT into record (userid,coursename,coursetime,sign,vacation,absence,room,teacher) VALUES(?,?,?,?,?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        for (RecordBo bo : bos) {
            statement.bindString(1, bo.getUserId());
            statement.bindString(2, bo.getCourseName());
            statement.bindString(3, bo.getTime());
            statement.bindString(4, bo.getSign());
            statement.bindString(5, bo.getVacation());
            statement.bindString(6, bo.getAbsence());
            statement.bindString(7, bo.getClassroom());
            statement.bindString(8,bo.getTeacher());
            statement.executeInsert();

        }
        db.setTransactionSuccessful();
        db.endTransaction();
        statement.close();
        db.close();
    }

    /**
     * 获取用户的签到记录
     *
     * @return
     */
    public List<RecordBo> getData() {
        String sql = "SELECT userid,coursename,coursetime,sign,vacation,absence,room,teacher FROM record";
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        List<RecordBo> bos = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                RecordBo bo = new RecordBo();
                String userid = cursor.getString(cursor.getColumnIndex("userid"));
                bo.setUserId(userid);
                String coursename = cursor.getString(cursor.getColumnIndex("coursename"));
                bo.setCourseName(coursename);
                String coursetime = cursor.getString(cursor.getColumnIndex("coursetime"));
                bo.setTime(coursetime);
                String sign = cursor.getString(cursor.getColumnIndex("sign"));
                bo.setSign(sign);
                String vacation = cursor.getString(cursor.getColumnIndex("vacation"));
                bo.setVacation(vacation);
                String absence = cursor.getString(cursor.getColumnIndex("absence"));
                bo.setAbsence(absence);
                String room = cursor.getString(cursor.getColumnIndex("room"));
                bo.setClassroom(room);
                String teacher=cursor.getString(cursor.getColumnIndex("teacher"));
                bo.setTeacher(teacher);
                bos.add(bo);
            }
            cursor.close();
        }
        db.close();
        return bos;
    }

    /**
     * 更新用户签到记录的课程信息修改
     *
     * @param bo
     * @param position
     */
    public void modifyRocurse(RecordBo bo, int position) {
        String sql = "DELETE FROM modifyrecord WHERE position=?";
        SQLiteDatabase db = helper.getWritableDatabase();
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        statement.bindString(1, position + "");
        statement.execute();
        db.setTransactionSuccessful();
        db.endTransaction();
        statement.close();
        sql = "INSERT into modifyrecord (coursename,coursetime,sign,vacation,absence,position,room,teacher) VALUES(?,?,?,?,?,?,?,?)";
        statement = db.compileStatement(sql);
        db.beginTransaction();
        statement.bindString(1, bo.getCourseName());
        statement.bindString(2, bo.getTime());
        statement.bindString(3, bo.getSign());
        statement.bindString(4, bo.getVacation());
        statement.bindString(5, bo.getAbsence());
        statement.bindString(6, position + "");
        statement.bindString(7, bo.getClassroom());
        statement.bindString(8,bo.getTeacher());
        statement.execute();
        db.setTransactionSuccessful();
        db.endTransaction();
        statement.close();
        db.close();

    }

    /**
     * 获取用户对签到记录的课程信息的修改 ，修改 ：userid属性对应该条记录在list的位置
     *
     * @return
     */
    public List<RecordBo> getModifyData() {
        String sql = "SELECT position,coursename,coursetime,sign,vacation,absence,room,teacher FROM modifyrecord";
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        List<RecordBo> bos = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                RecordBo bo = new RecordBo();
                String position = cursor.getString(cursor.getColumnIndex("position"));
                bo.setUserId(position);
                String coursename = cursor.getString(cursor.getColumnIndex("coursename"));
                bo.setCourseName(coursename);
                String coursetime = cursor.getString(cursor.getColumnIndex("coursetime"));
                bo.setTime(coursetime);
                String sign = cursor.getString(cursor.getColumnIndex("sign"));
                bo.setSign(sign);
                String vacation = cursor.getString(cursor.getColumnIndex("vacation"));
                bo.setVacation(vacation);
                String absence = cursor.getString(cursor.getColumnIndex("absence"));
                bo.setAbsence(absence);
                String room = cursor.getString(cursor.getColumnIndex("room"));
                bo.setClassroom(room);
                String teacher=cursor.getString(cursor.getColumnIndex("teacher"));
                bo.setTeacher(teacher);
                bos.add(bo);
            }
            cursor.close();
        }
        db.close();
        return bos;
    }
}
