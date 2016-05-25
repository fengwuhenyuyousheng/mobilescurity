package com.test.yang.mobilesecurity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.test.yang.mobilesecurity.model.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**这是对黑名单数据库操作的类
 * Created by Administrator on 2016/5/18.
 */
public class BlackNumberDBOperate {

    public static final String DB_NAME="blackNumber.db";
    public static final int DB_VERSION=1;
    public static final int MODE_CALL=0;
    public static final int MODE_SMS=1;
    public static final int MODE_ALL=2;

    private BlackNumberDBHelper blackNumberDBHelper;

    public BlackNumberDBOperate(Context context){
        blackNumberDBHelper=new BlackNumberDBHelper(context,DB_NAME,null,DB_VERSION);
//        blackNumberDBHelper=new BlackNumberDBHelper(context);
    }

    /**
     * 添加黑名单
     * @param blackNumber
     * @param mode
     */
    public void addBlackNumber(String blackNumber, int mode){
        //1.获取数据库
        SQLiteDatabase database = blackNumberDBHelper.getWritableDatabase();
        //2.添加操作
        //ContentValues :　添加的数据
        ContentValues values = new ContentValues();
        values.put("black_number", blackNumber);
        values.put("mode", mode);
        database.insert(blackNumberDBHelper.DB_TABLE_NAME, null, values);
        //3.关闭数据库
        database.close();
    }
    /**
     * 更新黑名单的拦截模式
     */
    public void updateBlackNum(String blackNumber,int mode){
        //1.获取数据库
        SQLiteDatabase database = blackNumberDBHelper.getWritableDatabase();
        //2.更新操作
        ContentValues values = new ContentValues();
        values.put("mode", mode);
        //table : 表名
        //values : 要更新数据
        //whereClause : 查询条件  where blackNumber=blackNumber
        //whereArgs : 查询条件的参数
        database.update(blackNumberDBHelper.DB_TABLE_NAME, values, "black_number=?", new String[]{blackNumber});
        //3.关闭数据库
        database.close();
    }
    /**
     * 通过黑名单号码,查询黑名单号码的拦截模式
     */
    public int queryBlackNumberMode(String blackNumber){
        int mode=-1;
        //1.获取数据库
        SQLiteDatabase database = blackNumberDBHelper.getReadableDatabase();
        //2.查询数据库
        //table : 表名
        //columns : 查询的字段  mode
        //selection : 查询条件  where xxxx = xxxx
        //selectionArgs : 查询条件的参数
        //groupBy : 分组
        //having : 去重
        //orderBy : 排序
        Cursor cursor = database.query(blackNumberDBHelper.DB_TABLE_NAME, new String[]{"mode"}, "black_number=?", new String[]{blackNumber}, null, null, null);
        //3.解析cursor
        if (cursor.moveToNext()) {
            //获取查询出来的数据
            mode = cursor.getInt(0);
        }
        //4.关闭数据库
        cursor.close();
        database.close();
        return mode;
    }
    /**
     * 根据黑名单号码,删除相应的数据
     * @param blackNumber
     */

    public void deleteBlackNumber(String blackNumber){
        //1.获取数据库
        SQLiteDatabase database = blackNumberDBHelper.getWritableDatabase();
        //2.删除
        //table : 表名
        //whereClause : 查询的条件
        //whereArgs : 查询条件的参数
        database.delete(blackNumberDBHelper.DB_TABLE_NAME, "black_number=?", new String[]{blackNumber});
        //3.关闭数据库
        database.close();
    }

    /**
     * 查询所有的黑名单号码
     * @return 黑名单号码的List集合
     */
    public List<BlackNumberInfo> queryAllBlackNumber(){
        SQLiteDatabase database = blackNumberDBHelper.getWritableDatabase();
        Cursor cursor=database.query(blackNumberDBHelper.DB_TABLE_NAME,new String[]{"black_number","mode"},null,null,null,null,"_id desc");
        BlackNumberInfo blackNumberInfo=new BlackNumberInfo();
        List<BlackNumberInfo> blackNumberInfoList=new ArrayList<BlackNumberInfo>();
        while (cursor.moveToNext()){
            blackNumberInfo.setBlackNumber(cursor.getString(cursor.getColumnIndex("black_number")));
            blackNumberInfo.setBlackNumberMode(cursor.getInt(cursor.getColumnIndex("mode")));
            blackNumberInfoList.add(blackNumberInfo);
        }
        cursor.close();
        database.close();
//        Log.d("BlackNumberDBOperate","数据库长度"+blackNumberInfoList.size());
        return blackNumberInfoList;
    }

    /**
     * 分步查询黑名单的号码
     * @param maxNumber 查询的号码数量
     * @param startIndex 查询的开始位置
     * @return 查询的结果
     */
    public List<BlackNumberInfo> querySubStepBlackNumber(int maxNumber,int startIndex){
        SQLiteDatabase database = blackNumberDBHelper.getWritableDatabase();
        Cursor cursor=database.rawQuery("select black_number,mode from "+
                blackNumberDBHelper.DB_TABLE_NAME +" order by _id desc limit ? offset ?",
                new String[]{maxNumber+"",startIndex+""});
        List<BlackNumberInfo> blackNumberInfoList=new ArrayList<BlackNumberInfo>();
        while (cursor.moveToNext()){
            BlackNumberInfo blackNumberInfo=new BlackNumberInfo();
            blackNumberInfo.setBlackNumber(cursor.getString(cursor.getColumnIndex("black_number")));
            blackNumberInfo.setBlackNumberMode(cursor.getInt(cursor.getColumnIndex("mode")));
            blackNumberInfoList.add(blackNumberInfo);
        }
        cursor.close();
        database.close();
//        Log.d("BlackNumberDBOperate","数据库长度"+blackNumberInfoList.size());
        return blackNumberInfoList;
    }

}
