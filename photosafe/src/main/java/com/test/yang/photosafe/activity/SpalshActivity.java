package com.test.yang.photosafe.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.test.yang.photosafe.R;
import com.test.yang.photosafe.tools.StreamTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * 这是启动界面
 */
public class SpalshActivity extends AppCompatActivity {
    protected static final int MESSAGE_ENTER_HOME=0;
    protected static final int MESSAGE_UPDATE_VERSION=1;
    protected static final int MESSAGE_FAIL_JSON =2 ;
    protected static final int MESSAGE_FAIL_NETWORK=3;
    protected static final int MESSAGE_FAIL_URL=4;
    private String mOldVersionName;
    private String mNewVersionName;
    private String mApkURL;
    private String mUpdateInfo;
    private Handler mHandler=new Handler(){
        public void handleMessage(Message message){
            switch(message.what){
                case MESSAGE_ENTER_HOME:
//                    Toast.makeText(getApplicationContext(),"跳转主界面",Toast.LENGTH_LONG).show();
                    jumpHomeActivity();
                    break;
                case MESSAGE_UPDATE_VERSION:
                    showDialog();
                    break;
                case  MESSAGE_FAIL_JSON:
                    Toast.makeText(getApplicationContext(),"解析文件失败",Toast.LENGTH_SHORT).show();
                    jumpHomeActivity();
                    break;
                case  MESSAGE_FAIL_NETWORK:
                    Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
                    jumpHomeActivity();
                    break;
                case  MESSAGE_FAIL_URL:
                    Toast.makeText(getApplicationContext(),"网络连接失败",Toast.LENGTH_SHORT).show();
                    jumpHomeActivity();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        mOldVersionName=getVersionName();
        TextView versionTextView = (TextView) findViewById(R.id.versions_text_view);
        if (versionTextView != null) {
            versionTextView.setText("版本号："+mOldVersionName);
        }
        SharedPreferences spConfig=getSharedPreferences("config",MODE_PRIVATE);
        if (spConfig.getBoolean("update",true)||spConfig==null) {
            getUpdateInfo();
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(2000);
                    jumpHomeActivity();
                }
            }).start();
        }
    }

    /**
     * 获取当前应用程序版本号
     * @return String 版本号字符串
     */
    private String getVersionName(){
        PackageManager pm=getPackageManager();
        try {
            PackageInfo pi=pm.getPackageInfo("com.test.yang.photosafe",0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 联网获取最新版本信息,并将结果message通过mHandler发送
     *
     */
    private void getUpdateInfo(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message=Message.obtain();
                long startTime=System.currentTimeMillis();
                try {
                    String address="http://192.168.31.19:8080/updateInfo.html";
                    URL url=new URL(address);
                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    int responseCode=connection.getResponseCode();
                    if (responseCode==200) {
                        InputStream in = connection.getInputStream();
                        String json = StreamTools.parseStreamUtil(in);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            mNewVersionName = jsonObject.getString("code");
                            mApkURL = jsonObject.getString("apkURL");
                            mUpdateInfo = jsonObject.getString("updateInfo");
                            String info = mNewVersionName + "," + mApkURL + "," + mUpdateInfo;
                            Log.d("updateinfo", info);
                            if(mOldVersionName.equals(mNewVersionName)){
                                message.what=MESSAGE_ENTER_HOME;
                            }else{
                                message.what=MESSAGE_UPDATE_VERSION;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Log.d("updateinfo", "解析文件失败");
                            message.what=MESSAGE_FAIL_JSON;
                        }
                    }else{
//                        Log.d("updateinfo", "网络连接失败");
                        message.what=MESSAGE_FAIL_NETWORK;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    message.what=MESSAGE_FAIL_URL;
                }finally{
                    long endTime=System.currentTimeMillis();
                    long spendTime=endTime-startTime;
                    if(spendTime<2000){
                        SystemClock.sleep(2000-spendTime);
                    }
                    mHandler.sendMessage(message);
                }
            }
        }).start();
    }
    /**
     * 弹出下载新版本对话框
     */
    protected void showDialog(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle("新版本:"+mNewVersionName);
        dialog.setIcon(R.drawable.app_log);
        dialog.setMessage(mUpdateInfo);
        dialog.setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"执行下载操作",Toast.LENGTH_SHORT).show();
                download();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                Toast.makeText(getApplicationContext(),"跳转主界面",Toast.LENGTH_SHORT).show();
                jumpHomeActivity();
            }
        });
        dialog.show();
    }

    /**
     * 下载最新版本的软件
     */
    protected void download(){
        final TextView downloadProgress= (TextView) findViewById(R.id.download_progress);
        HttpUtils httpUtils=new HttpUtils();
        httpUtils.download(mApkURL,
                "/storage/emulated/0/Download/photosafe-debug2.apk",
                new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                Toast.makeText(getApplicationContext(),"下载成功",Toast.LENGTH_SHORT).show();
                Log.d("update","下载成功");
                if(downloadProgress!=null) {
                    downloadProgress.setVisibility(View.GONE);
                }
                installAPK();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getApplicationContext(),"下载失败",Toast.LENGTH_SHORT).show();
                Log.d("update","下载失败");
            }
            @Override
            public void onLoading(long total, long current, boolean isUploading){
                if(downloadProgress!=null){
                    downloadProgress.setVisibility(View.VISIBLE);
                }
                downloadProgress.setText(current+"/"+total);
                Log.d("update","正在下载");

            }
        });
    }
    /**
     * 安装新版APK
     */
    protected void installAPK(){
        Uri installUri=Uri.fromFile(
                new File("/storage/emulated/0/Download/photosafe-debug2.apk"));
        Intent installIntent=new Intent(Intent.ACTION_VIEW);
        installIntent.setDataAndType(installUri,"application/vnd.android.package-archive");
        startActivityForResult(installIntent,0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        jumpHomeActivity();
    }
    /**
     * 跳转到主界面
     */
    protected void jumpHomeActivity(){
        Intent jumpHomeActivityIntent=new Intent(this,HomeActivity.class);
        startActivity(jumpHomeActivityIntent);
        finish();
    }
}
