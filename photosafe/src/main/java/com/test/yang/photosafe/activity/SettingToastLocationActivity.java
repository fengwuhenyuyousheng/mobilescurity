package com.test.yang.photosafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.yang.photosafe.R;

/**这是设置归属地提示框位置界面
 * Created by Administrator on 2016/5/17.
 */
public class SettingToastLocationActivity extends Activity {

    private LinearLayout mAddressToastLayout;
    private SharedPreferences mSharedPreferences;
    private TextView mTextViewTop;
    private TextView mTextViewBottom;
    private int mWidth;
    private int mHeight;
    long[] mHits = new long[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_address_location);
        mAddressToastLayout= (LinearLayout) findViewById(R.id.toast_address_view);
        mSharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        mTextViewTop= (TextView) findViewById(R.id.text_view_top);
        mTextViewBottom= (TextView) findViewById(R.id.text_view_bottom);
        WindowManager windowManager= (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics=new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mWidth=displayMetrics.widthPixels;
        mHeight=displayMetrics.heightPixels;
        initToastLocation();
        doubleClick();
        setTouch();
    }

    /**
     * 设置归属地对话框双击事件监听
     */
    private void doubleClick(){
        mAddressToastLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 *  src the source array to copy the content.   拷贝的原数组
                 srcPos the starting index of the content in src.  是从源数组那个位置开始拷贝
                 dst the destination array to copy the data into.  拷贝的目标数组
                 dstPos the starting index for the copied content in dst.  是从目标数组那个位置开始去写
                 length the number of elements to be copied.   拷贝的长度
                 */
                System.arraycopy(mHits,1,mHits,0,mHits.length-1);
                mHits[mHits.length-1]= SystemClock.uptimeMillis();
                if(mHits[0] >= (SystemClock.uptimeMillis()-500)){
                    //双击居中
                    int l = (mWidth - mAddressToastLayout.getWidth())/2;
                    int t = (mHeight -25- mAddressToastLayout.getHeight())/2;
                    mAddressToastLayout.layout(l, t, l+mAddressToastLayout.getWidth(), t+mAddressToastLayout.getHeight());
                    //保存控件的坐标
                    SharedPreferences.Editor edit = mSharedPreferences.edit();
                    edit.putInt("endX", l);
                    edit.putInt("endY", t);
                    edit.commit();
                }
            }
        });
    }

    /**
     * 设置归属地对话框画面监听事件
     */
    private void setTouch(){
        mAddressToastLayout.setOnTouchListener(new View.OnTouchListener() {
            private int startY=0;
            private int startX=0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX= (int) event.getRawX();
                        startY= (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int newX=(int) event.getRawX();
                        int newY=(int) event.getRawY();
                        int moveX=newX-startX;
                        int moveY=newY-startY;
                        int l=mAddressToastLayout.getLeft()+moveX;
                        int t=mAddressToastLayout.getTop()+moveY;
                        int r=l+mAddressToastLayout.getWidth();
                        int b=t+mAddressToastLayout.getHeight();
                        if (l < 0 || r > mWidth || t < 0 || b > mHeight - 25) {
                            break;
                        }
                        mAddressToastLayout.layout(l,t,r,b);
                        int top=mAddressToastLayout.getTop();
                        if(top>=(mHeight-25)/2){
                            mTextViewBottom.setVisibility(View.INVISIBLE);
                            mTextViewTop.setVisibility(View.VISIBLE);
                        }else{
                            mTextViewBottom.setVisibility(View.VISIBLE);
                            mTextViewTop.setVisibility(View.INVISIBLE);
                        }
                        startX=newX;
                        startY=newY;
                        break;
                    case MotionEvent.ACTION_UP:
                        int endX= (int) mAddressToastLayout.getX();
                        int endY= (int) mAddressToastLayout.getY();
                        SharedPreferences.Editor editor=mSharedPreferences.edit();
                        editor.putInt("endX",endX);
                        editor.putInt("endY",endY);
                        editor.commit();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 这是控件位置初始化操作
     */
    private void initToastLocation(){
        int x=mSharedPreferences.getInt("endX",10);
        int y=mSharedPreferences.getInt("endY",10);
        //获取父空间的属性
        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) mAddressToastLayout.getLayoutParams();
        params.leftMargin=x;
        params.topMargin=y;
        mAddressToastLayout.setLayoutParams(params);
        if(y>=(mHeight-25)/2){
            mTextViewBottom.setVisibility(View.INVISIBLE);
            mTextViewTop.setVisibility(View.VISIBLE);
        }else{
            mTextViewBottom.setVisibility(View.VISIBLE);
            mTextViewTop.setVisibility(View.INVISIBLE);
        }
    }
}
