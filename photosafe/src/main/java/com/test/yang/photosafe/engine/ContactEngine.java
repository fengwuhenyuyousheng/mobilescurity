package com.test.yang.photosafe.engine;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**这是查询工具类
 * Created by Administrator on 2016/5/10.
 */
public class ContactEngine {
    /**
     * 这是查询系统联系人方法
     * @param context
     * @return 查询到的联系人列表
     */
    public static List<HashMap<String,String>> getAllContactInfo(Context context){
        ArrayList<HashMap<String,String>> arrayList=new ArrayList<HashMap<String,String>>();
        //获取内容解析者
        ContentResolver contentResolver=context.getContentResolver();
        //获取内容提供者地址并生成查询地址
        Uri rawContactsUri=Uri.parse("content://com.android.contacts/raw_contacts");
        Uri viewDataUri=Uri.parse("content://com.android.contacts/data");
        //首先查询raw_contacts表，获得contact_id
        Cursor cursor=contentResolver.query(rawContactsUri,new String[]{"contact_id"},null,null,null);
        //解析cursor
        while (cursor.moveToNext()){
            //获取查询的contact_id
            String contact_id=cursor.getString(cursor.getColumnIndex("contact_id"));
            //根据contact_id查询view_data表中的数据
            Cursor c = contentResolver.query(viewDataUri, new String[]{"data1","mimetype"},
                    "raw_contact_id=?", new String[]{contact_id}, null);
            HashMap<String,String> hashMap=new HashMap<String,String>();
            //解析得到的c数据
            while (c.moveToNext()){
                String data1=c.getString(c.getColumnIndex("data1"));
                String mimetype=c.getString(c.getColumnIndex("mimetype"));
                if(mimetype.equals("vnd.android.cursor.item/phone_v2")){
                    hashMap.put("number",data1);
                }else if(mimetype.equals("vnd.android.cursor.item/name")){
                    hashMap.put("name",data1);
                }
            }
            //将数据添加到list集合中
            arrayList.add(hashMap);
            c.close();
        }
        cursor.close();
        return arrayList;
    }
}
