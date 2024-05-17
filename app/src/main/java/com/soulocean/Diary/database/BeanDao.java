package com.soulocean.Diary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soulocean.Diary.constants.Constants;
import com.soulocean.Diary.entity.Bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * @author soulo
 */
public class BeanDao {
    private static final String TAG = "BeanDao";
    private static DataBaseHelper mHelper;
    private static BeanDao INSTANCE;
    private  SQLiteDatabase db;

    public static synchronized BeanDao getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new BeanDao();
            mHelper = new DataBaseHelper(context);
        }
        return INSTANCE;
    }

    private static String sqliteEscape(String keyWord) {
        if (keyWord == null) {
            return null;
        }
        keyWord = keyWord.replace("/", "//");
        keyWord = keyWord.replace("'", "''");
        keyWord = keyWord.replace("[", "/[");
        keyWord = keyWord.replace("]", "/]");
        keyWord = keyWord.replace("%", "/%");
        keyWord = keyWord.replace("&", "/&");
        keyWord = keyWord.replace("_", "/_");
        keyWord = keyWord.replace("(", "/(");
        keyWord = keyWord.replace(")", "/)");
        return keyWord;
    }

    public ArrayList<Bean> fuzzySearchByPattern(String pattern){
        db = mHelper.getWritableDatabase();
        Cursor cursor = db.query(Constants.TABLE_NAME_A, null, "content like ?", new String[]{"%"+pattern+"%"}, null, null, null);
        ArrayList<Bean> beanList = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String backgroundImage = cursor.getString(cursor.getColumnIndex("backgroundImage"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String remarks = cursor.getString(cursor.getColumnIndex("remarks"));
            String updateTime = cursor.getString(cursor.getColumnIndex("updateTime"));
            Bean bean = new Bean();
            bean.setId(id);
            bean.setName(name);
            bean.setBackgroundImage(backgroundImage);
            bean.setContent(content);
            bean.setRemarks(remarks);
            bean.setUpdateTime(updateTime);
            beanList.add(bean);
        }

        return beanList;
    }

    public void insert(Bean bean) {
        db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", bean.getId());
        values.put("name", bean.getName());
        values.put("backgroundImage", bean.getBackgroundImage());
        values.put("content", bean.getContent());
        values.put("remarks", bean.getRemarks());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        values.put("updateTime",simpleDateFormat.format(date));
        db.insert(Constants.TABLE_NAME_A, null, values);
        db.close();
        Log.d(TAG, "insert into table a success");
    }

    public void updateBeanByBean(Bean bean) {
        ContentValues values = new ContentValues();
        db = mHelper.getWritableDatabase();

        values.put("id", bean.getId());
        values.put("name", bean.getName());
        values.put("backgroundImage", bean.getBackgroundImage());
        values.put("content", bean.getContent());
        values.put("remarks", bean.getRemarks());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        values.put("updateTime", simpleDateFormat.format(date));

        db.update(Constants.TABLE_NAME_A, values, "id=" + bean.getId(), null);
    }

    public int deleteBeanById(int id) {
        return db.delete(Constants.TABLE_NAME_A, " id=" + id, null);
    }

    public Bean getBeanById(int id) {
        if (id == 0) {
            return null;
        }
        Cursor cursor = db.query(Constants.TABLE_NAME_A, null, " id=" + id, null, null, null, null);
        if (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String backgroundImage = cursor.getString(cursor.getColumnIndex("backgroundImage"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String remarks = cursor.getString(cursor.getColumnIndex("remarks"));
            String updateTime = cursor.getString(cursor.getColumnIndex("updateTime"));
            Bean bean = new Bean();
            bean.setId(id);
            bean.setName(name);
            bean.setBackgroundImage(backgroundImage);
            bean.setContent(content);
            bean.setRemarks(remarks);
            bean.setUpdateTime(updateTime);
            return bean;
        }
        return null;
    }

    public ArrayList<Bean> listBean() {
        ArrayList<Bean> listBean = new ArrayList<>();
        db = mHelper.getWritableDatabase();
        Cursor cursor = db.query(Constants.TABLE_NAME_A, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String backgroundImage = cursor.getString(cursor.getColumnIndex("backgroundImage"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String remarks = cursor.getString(cursor.getColumnIndex("remarks"));
            String updateTime = cursor.getString(cursor.getColumnIndex("updateTime"));
            Bean bean = new Bean();
            bean.setId(id);
            bean.setName(name);
            bean.setBackgroundImage(backgroundImage);
            bean.setContent(content);
            bean.setRemarks(remarks);
            bean.setUpdateTime(updateTime);
            listBean.add(bean);
        }
        cursor.close();
        return listBean;
    }

    public boolean tableHadData(String tableName) {
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        return cursor.moveToNext();
    }

    public void clearTable(String tableName) {
        String sql = "delete from " + tableName;
        //自增长ID为0
        String sql2 = "update sqlite_sequence SET seq = 0 where name ='" + tableName + "'";
        db.execSQL(sql);
        db.execSQL(sql2);
    }
}
