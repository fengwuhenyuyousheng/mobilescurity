package com.test.yang.photosafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.test.yang.photosafe.R;
import com.test.yang.photosafe.db.AddressDBOperate;

public class AddressService extends Service {

    private TelephonyManager telephonyManager;
    private MyPhoneStateListener myPhoneStateListener;
    private WindowManager windowManager;
    private TextView toastTextView;
    private View toastView;
    private MyOutCallReceiver myOutCallReceiver;
    private SharedPreferences sharedPreferences;
    private WindowManager.LayoutParams params;
    private int width;
    private int height;

    public AddressService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        //新建呼出电话广播接收者
        myOutCallReceiver=new MyOutCallReceiver();
        //设置接收的广播事件
        IntentFilter intentFilter=new IntentFilter("android.intent.action.NEW_OUTGOING_CALL");
        //注册广播
        registerReceiver(myOutCallReceiver, intentFilter);
        //获取电话管理者
        telephonyManager= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //新建电话监听事件
        myPhoneStateListener=new MyPhoneStateListener();
        //监听电话状态
        telephonyManager.listen(myPhoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);
        // 获取屏幕的宽度
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // windowManager.getDefaultDisplay().getWidth();
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        width = outMetrics.widthPixels;
        height = outMetrics.heightPixels;
    }
    private class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state){
                //空闲，挂断状态
                case TelephonyManager.CALL_STATE_IDLE :
                    hideToast();
                    break;
                //响铃状态
                case TelephonyManager.CALL_STATE_RINGING:
                    String queryAddress = AddressDBOperate.queryAddress(incomingNumber, getApplicationContext());
                    if (!TextUtils.isEmpty(queryAddress)) {
                        showToast(queryAddress);
                    }
                    break;
                //通话中状态
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
            }super.onCallStateChanged(state, incomingNumber);
        }
    }
    //服务关闭的时候,取消监听
    @Override
    public void onDestroy() {
        //当服务关闭的时候,取消监听操作
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        //注销广播接收者
        unregisterReceiver(myOutCallReceiver);
        super.onDestroy();
    }

    /**
     * 自定义Toast显示方法
     * @param queryAddress 要显示的号码归属地
     */
    private void showToast(String queryAddress){
        int[] backGround=new int[]{R.color.colorWhileSmoke,R.color.colorOrange,
                                    R.color.colorLightBlue,R.color.colorGrey,R.color.colorGreen};
        //获取WindowsManager
        windowManager= (WindowManager) getSystemService(WINDOW_SERVICE);
        //设置布局
        /*
        toastTextView=new TextView(getApplicationContext());
        toastTextView.setTextColor(Color.BLUE);
        toastTextView.setTextSize(25.0f);
        toastTextView.setText(queryAddress);
        */
        toastView= View.inflate(getApplicationContext(), R.layout.item_toast,null);
        toastTextView= (TextView) toastView.findViewById(R.id.toast_address_text);
        toastTextView.setText(queryAddress);
        toastView.setBackgroundResource(backGround[sharedPreferences.getInt("which",0)]);

        //设置Toast属性
        params=new WindowManager.LayoutParams();

        params.width= WindowManager.LayoutParams.WRAP_CONTENT;
        params.height= WindowManager.LayoutParams.WRAP_CONTENT;
        //不获取焦点
        params.flags= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE ;
        //不可以触摸
//        params.flags= WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        //保持在当前屏幕
        params.flags= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        //透明
        params.format= PixelFormat.TRANSLUCENT;
        //执行Toast的类型
        params.type= WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;

        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x=sharedPreferences.getInt("endX",120);
        params.y=sharedPreferences.getInt("endY",100);

        windowManager.addView(toastView,params);
    }

    /**
     * 定义归属地对话框监听事件
     */
    private void setTouch(){
        toastView.setOnTouchListener(new View.OnTouchListener() {
            private int startX;
            private int startY;

            //v : 当前的控件
            //event : 控件执行的事件
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //event.getAction() : 获取控制的执行的事件
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下的事件
                        System.out.println("按下了....");
                        //1.按下控件,记录开始的x和y的坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //移动的事件
                        System.out.println("移动了....");
                        //2.移动到新的位置记录新的位置的x和y的坐标
                        int newX = (int) event.getRawX();
                        int newY = (int) event.getRawY();
                        //3.计算移动的偏移量
                        int dX = newX-startX;
                        int dY = newY-startY;
                        //4.移动相应的偏移量,重新绘制控件
                        params.x+=dX;
                        params.y+=dY;
                        //控制控件的坐标不能移出外拨电话界面
                        if (params.x < 0) {
                            params.x = 0;
                        }
                        if (params.y < 0) {
                            params.y=0;
                        }
                        if (params.x > width-toastView.getWidth()) {
                            params.x = width-toastView.getWidth();
                        }
                        if (params.y > height - toastView.getHeight() - 25) {
                            params.y = height - toastView.getHeight() - 25;
                        }

                        windowManager.updateViewLayout(toastView, params);//更新windowmanager中的控件
                        //5.更新开始的坐标
                        startX=newX;
                        startY=newY;
                        break;
                    case MotionEvent.ACTION_UP:
                        //抬起的事件
                        System.out.println("抬起了....");
                        //保存控件的坐标,保存的是控件的坐标不是手指坐标
                        //获取控件的坐标
                        int x = params.x;
                        int y = params.y;

                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putInt("x", x);
                        edit.putInt("y", y);
                        edit.commit();
                        break;
                }
                //True if the listener has consumed the event, false otherwise.
                //true:事件消费了,执行了,false:表示事件被拦截了
                return true;
            }
        });
    }

    /**
     * 定义隐藏Toast方法
     */
    private void hideToast(){
        if (windowManager != null && toastTextView!= null) {
            windowManager.removeView(toastView);//移出控件
            windowManager= null;
            toastTextView=null;
        }

    }

    private class MyOutCallReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //得到发送广播时设置的 initialData 的数据(即电话号码)
            String number=getResultData();
            //查询号码归属地
            String queryAddress = AddressDBOperate.queryAddress(number, getApplicationContext());
            //3.判断号码归属地是否为空
            if (!TextUtils.isEmpty(queryAddress)) {
                //显示toast
                showToast(queryAddress);
            }

        }
    }

}
