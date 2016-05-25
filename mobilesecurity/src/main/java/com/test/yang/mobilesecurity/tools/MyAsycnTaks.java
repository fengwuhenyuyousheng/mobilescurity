package com.test.yang.mobilesecurity.tools;

import android.os.Handler;
import android.os.Message;

/**这是自定义异步线程框架
 * Created by Administrator on 2016/5/11.
 */
public abstract  class MyAsycnTaks {

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            posTask();
        }
    };

    /**
     * 子线程执行前执行的方法
     */
    public abstract void preTask();

    /**
     * 子线程执行的方法
     */
    public abstract void doInTask();
    /**
     * 子线程执行后执行的方法
     */
    public abstract void posTask();

    /**
     * 这是运行子线程的方法
     */
    public void execute(){
        preTask();
        new Thread(){
            public void run(){
                doInTask();
                handler.sendEmptyMessage(0);
            }
        }.start();
    }
}
