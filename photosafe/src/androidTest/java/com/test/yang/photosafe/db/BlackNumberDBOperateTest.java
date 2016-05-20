package com.test.yang.photosafe.db;

import android.test.AndroidTestCase;

import com.test.yang.photosafe.model.BlackNumberInfo;

import java.util.List;

/**这是数据库操作测试类
 * Created by Administrator on 2016/5/19.
 */
public class BlackNumberDBOperateTest extends AndroidTestCase {

    public void testQueryAllBlackNumber() throws Exception {
        BlackNumberDBOperate blackNumberDBOperate=new BlackNumberDBOperate(getContext());
        List<BlackNumberInfo> blackNumberInfoLis=blackNumberDBOperate.queryAllBlackNumber();

    }
    public void testAddBlackNumber()throws Exception{
        BlackNumberDBOperate blackNumberDBOperate=new BlackNumberDBOperate(getContext());
        for(int i=0;i<=100;i++){
            blackNumberDBOperate.addBlackNumber("183764890"+i,i%3);
        }
    }
}