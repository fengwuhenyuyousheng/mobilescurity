package com.test.yang.mobilesecurity.db;

import android.test.AndroidTestCase;

/**这是数据库测试类
 * Created by Administrator on 2016/5/19.
 */
public class BlackNumberDBHelperTest extends AndroidTestCase {

    public void testBlackNumberDBHelper(){
        BlackNumberDBHelper blackNumberDBHelper=new BlackNumberDBHelper(getContext(),"blackNumber.db",null,1);
        blackNumberDBHelper.getWritableDatabase();
    }
}