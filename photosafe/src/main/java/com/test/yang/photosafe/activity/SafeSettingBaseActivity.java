package com.test.yang.photosafe.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

/**这是手机防盗界面的基本类
 * Created by Administrator on 2016/5/9.
 */
public abstract class SafeSettingBaseActivity extends AppCompatActivity {

    /**
     * 这是跳转下一界面的方法
     */
    public void nextActivity(View v){
        nextActivitySubClass();
    }

    /**
     * 这是返回上一界面的方法
     */
    public void previousActivity(View v){
        previousActivitySubClass();
    }

    /**
     * 这是需要让子类实现的跳转到下一界面的方法
     */
    public abstract void nextActivitySubClass();

    /**
     * 这是需要让子类实现的跳转到上一界面的方法
     */
    public abstract void previousActivitySubClass();

    /**
     * 统一处理返回按键事件
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断keycode是否是返回键的标示
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //true:是可以屏蔽按键的事件
            //return true;
            previousActivitySubClass();
        }
        return super.onKeyDown(keyCode, event);
    }
}
