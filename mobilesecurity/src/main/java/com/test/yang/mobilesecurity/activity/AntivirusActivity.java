package com.test.yang.mobilesecurity.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.test.yang.mobilesecurity.R;
import com.test.yang.mobilesecurity.db.AntiVirusDBOperate;
import com.test.yang.mobilesecurity.tools.MD5Tools;

import java.util.ArrayList;
import java.util.List;

public class AntivirusActivity extends AppCompatActivity {

    private ImageView antivirusRadarImageView;
    private ImageView antivirusScannerImageView;
    private TextView antivirusTextView;
    private ProgressBar antivirusProgressBar;
    private LinearLayout antivirusSafeApkLinearLayout;
    private List<String> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antivirus);
        list=new ArrayList<String>();
        antivirusRadarImageView= (ImageView) findViewById(R.id.antivirus_radar_image);
        antivirusScannerImageView= (ImageView) findViewById(R.id.antivirus_scanner_image);
        antivirusTextView= (TextView) findViewById(R.id.antivirus_text_view);
        antivirusProgressBar= (ProgressBar) findViewById(R.id.antivirus_progress_bar);
        antivirusSafeApkLinearLayout= (LinearLayout) findViewById(R.id.antivirus_safe_apk);
        setAnimation();
    }

    /**
     * 设置雷达动画
     */
    private void setAnimation(){
        RotateAnimation rotateAnimation=new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        LinearInterpolator linearInterpolator=new LinearInterpolator();
        rotateAnimation.setInterpolator(linearInterpolator);
        antivirusScannerImageView.setAnimation(rotateAnimation);
        scanner();
    }

    /**
     * 设置扫描程序
     */
    private void scanner(){
        final PackageManager packageManager=getPackageManager();
        antivirusTextView.setText("正在初始化进程.......");

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<PackageInfo>  packageInfoList=packageManager.getInstalledPackages(PackageManager.GET_SIGNATURES);
                antivirusProgressBar.setMax(packageInfoList.size());
                int count=0;
                for(final PackageInfo packageInfo:packageInfoList){
                    SystemClock.sleep(200);
                    count++;
                    antivirusProgressBar.setProgress(count);
                    final String packageName=packageInfo.applicationInfo.loadLabel(packageManager).toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            antivirusTextView.setText("正在扫描:"+packageName);
                            Signature[] signatures=packageInfo.signatures;
                            String  charString=signatures[0].toCharsString();
                            String signatureString= MD5Tools.passwordMD5(charString);
                            boolean b= AntiVirusDBOperate.queryAddress(signatureString,getApplicationContext());
                            TextView textView = new TextView(getApplicationContext());
                            if (b) {
                                //有病毒
                                textView.setTextColor(Color.RED);
                                //将病毒的包名添加到list集合
                                list.add(packageInfo.packageName);
                            }else{
                                //没有病毒
                                textView.setTextColor(Color.BLACK);
                            }
                            textView.setText(packageName);
                            antivirusSafeApkLinearLayout.addView(textView, 0);
                        }
                    });
                }
                //5.扫描完成,显示扫描完成信息,同时旋转动画停止
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //10.根据是否有病毒进行显示操作
                        if (list.size() > 0) {
                            //有病毒
                            antivirusTextView.setText("扫描完成,发现" + list.size() + "个病毒");
                            //卸载病毒应用
                            AlertDialog.Builder builder = new AlertDialog.Builder(AntivirusActivity.this);
                            builder.setTitle("提醒!发现" + list.size() + "个病毒");
                            builder.setIcon(R.drawable.app_log_small);
                            builder.setMessage("是否卸载病毒应用!");
                            builder.setPositiveButton("卸载", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //卸载操作
                                    for (String packageName : list) {
                                        Intent intent = new Intent();
                                        intent.setAction("android.intent.action.DELETE");
                                        intent.addCategory("android.intent.category.DEFAULT");
                                        intent.setData(Uri.parse("package:" + packageName));
                                        startActivity(intent);
                                    }
                                    dialog.dismiss();
                                }
                            });
                            builder.setNegativeButton("取消", null);
                            builder.show();
                        }else {
                            //设置显示信息以及停止动画
                            antivirusTextView.setText("扫描完成,未发现病毒");
                            antivirusScannerImageView.clearAnimation();//移出动画
                        }
                    }
                });
             }
        }).start();
    }

}
