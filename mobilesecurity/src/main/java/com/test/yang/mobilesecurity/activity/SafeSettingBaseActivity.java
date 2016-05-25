package com.test.yang.mobilesecurity.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**这是手机防盗界面向导的基本类
 * Created by Administrator on 2016/5/9.
 */
public abstract class SafeSettingBaseActivity extends AppCompatActivity {

    private GestureDetector gestureDetector;
    protected SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector=new GestureDetector(this,new MyGestureListener());
        sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

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
    /**这是左右滑动手势识别器监听
     * Created by Administrator on 2016/5/10.
     */
    public class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY){
            float startX=e1.getX();
            float endX=e2.getX();
            float startY=e1.getY();
            float endY=e2.getY();
            if(Math.abs(endY-startY)>50){
                return false;
            }
            if(endX-startX>50){
                previousActivitySubClass();
                return true;
            }
            if(startX-endX>50){
                nextActivitySubClass();
                return true;
            }
            return false;
        }
    }

}
