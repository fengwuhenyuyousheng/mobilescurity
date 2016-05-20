package com.test.yang.photosafe.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.test.yang.photosafe.R;
import com.test.yang.photosafe.service.AddressService;
import com.test.yang.photosafe.service.CallSmsService;
import com.test.yang.photosafe.tools.RunningAddress;
import com.test.yang.photosafe.ui.SettingOptionsItem;
import com.test.yang.photosafe.ui.SettingToastStyleItem;

/**这是设置中心界面
 * Created by Administrator on 2016/5/6.
 */
public class SettingActivity extends AppCompatActivity{

    private SettingOptionsItem updateSettingItem;
    private SettingOptionsItem openAddressService;
    private SettingOptionsItem openCallSmsService;
    private SettingToastStyleItem setToastStyle;
    private SharedPreferences spConfig;
    private SettingToastStyleItem setToastLocation;
    private final String[] items={"半透明","活力橙","卫士蓝","金属灰","苹果绿"};

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_center);
        spConfig=getSharedPreferences("config",MODE_PRIVATE);
        updateSettingItem=(SettingOptionsItem)findViewById(R.id.update_option);
        openAddressService= (SettingOptionsItem) findViewById(R.id.open_address_service);
        openCallSmsService= (SettingOptionsItem) findViewById(R.id.open_call_sms_service);
        setToastStyle= (SettingToastStyleItem) findViewById(R.id.set_toast_style);
        setToastLocation= (SettingToastStyleItem) findViewById(R.id.set_toast_location);
        initUpdateOption();
        updateSettingItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor edit = spConfig.edit();
                //更改状态
                //根据checkbox之前的状态来改变checkbox的状态
                if (updateSettingItem.getSettingCheckBoxisCheck()) {
                    //关闭提示更新
                    updateSettingItem.setSettingCheckBox(false);
                    //保存状态
                    edit.putBoolean("update", false);
                    //edit.apply();//保存到文件中,但是仅限于9版本之上,9版本之下保存到内存中的
                }else{
                    //打开提示更新
                    updateSettingItem.setSettingCheckBox(true);
                    //保存状态
                    edit.putBoolean("update", true);
                }
                edit.commit();
            }
        });
        setToastStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出单选对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                //设置图标
                builder.setIcon(R.drawable.app_log_small);
                //设置标题
                builder.setTitle("归属地提示框风格");
                //设置单选框
                //items : 选项的文本的数组
                //checkedItem : 选中的选项
                //listener : 点击事件
                //设置单选框选中选项的回显操作
                builder.setSingleChoiceItems(items, spConfig.getInt("which", 0), new DialogInterface.OnClickListener(){
                    //which : 选中的选项索引值
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences.Editor edit = spConfig.edit();
                        edit.putInt("which", which);
                        edit.commit();
                        //1.设置自定义组合控件描述信息文本
                        setToastStyle.setToastStyleDescription(items[which]);//根据选中选项索引值从items数组中获取出相应文本,设置给描述信息控件
                        //2.隐藏对话框
                        dialog.dismiss();
                    }
                });
                //设置取消按钮
                builder.setNegativeButton("取消", null);//当点击按钮只是需要进行隐藏对话框的操作的话,参数2可以写null,表示隐藏对话框
                builder.show();
            }
        });
        setToastLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jumpToastLocationActivity=new Intent(SettingActivity.this,SettingToastLocationActivity.class);
                startActivity(jumpToastLocationActivity);
            }
        });

    }


    /**
     * 设置选项的初始化
     */
    private void initUpdateOption(){
        if(spConfig.getBoolean("update",true)||spConfig==null) {
            updateSettingItem.setSettingCheckBox(true);
        }else {
            updateSettingItem.setSettingCheckBox(false);
        }
        setToastStyle.setToastStyleTitle("归属地提示框风格");
        setToastStyle.setToastStyleDescription(items[spConfig.getInt("which", 0)]);
        setToastLocation.setToastStyleTitle("归属地提示框显示位置");
        setToastLocation.setToastStyleDescription("点击设置归属地提示框显示的位置");
    }

    @Override
    protected void onStart() {
        super.onStart();
        openAddress();
        openCallSms();
    }

    /**
     * 黑名单拦截
     */
    private void openCallSms(){
        SharedPreferences.Editor edit = spConfig.edit();
        if(RunningAddress.isRunningAddressService(
                "com.test.yang.photosafe.service.CallSmsService",getApplicationContext())){
            //开启电话归属地服务
            openCallSmsService.setSettingCheckBox(true);
            edit.putBoolean("openCallSmsService",true);
        }else{
            //关闭电话归属地服务
            openCallSmsService.setSettingCheckBox(false);
            edit.putBoolean("openCallSmsService",false);
        }
        openCallSmsService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,CallSmsService.class);
                SharedPreferences.Editor edit = spConfig.edit();
                //根据checkbox的状态设置描述信息的状态
                //isChecked() : 之前的状态
                if (openCallSmsService.getSettingCheckBoxisCheck()) {
                    //关闭提示
                    stopService(intent);
                    //更新checkbox的状态
                    openCallSmsService.setSettingCheckBox(false);
                    edit.putBoolean("openCallSmsService",false);
                }else{
                    //打开提示
                    startService(intent);
                    openCallSmsService.setSettingCheckBox(true);
                    edit.putBoolean("openCallSmsService",true);
                }
                edit.commit();
            }
        });
    }

    /**
     * 显示电话归属地
     */
    private void openAddress(){
        SharedPreferences.Editor edit = spConfig.edit();
        if(RunningAddress.isRunningAddressService(
                "com.test.yang.photosafe.service.AddressService",getApplicationContext())){
            //开启电话归属地服务
            openAddressService.setSettingCheckBox(true);
            edit.putBoolean("openAddressService",true);
        }else{
            //关闭电话归属地服务
            openAddressService.setSettingCheckBox(false);
            edit.putBoolean("openAddressService",false);
        }
        openAddressService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,AddressService.class);
                SharedPreferences.Editor edit = spConfig.edit();
                //根据checkbox的状态设置描述信息的状态
                //isChecked() : 之前的状态
                if (openAddressService.getSettingCheckBoxisCheck()) {
                    //关闭提示
                    stopService(intent);
                    //更新checkbox的状态
                    openAddressService.setSettingCheckBox(false);
                    edit.putBoolean("openAddressService",false);
                }else{
                    //打开提示
                    startService(intent);
                    openAddressService.setSettingCheckBox(true);
                    edit.putBoolean("openAddressService",true);
                }
                edit.commit();
            }
        });
    }
}
