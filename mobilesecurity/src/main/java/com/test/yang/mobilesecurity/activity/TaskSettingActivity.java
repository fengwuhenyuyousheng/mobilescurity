package com.test.yang.mobilesecurity.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.test.yang.mobilesecurity.R;
import com.test.yang.mobilesecurity.ui.SettingOptionsItem;

public class TaskSettingActivity extends AppCompatActivity {

    private SettingOptionsItem showSystemProgress;
    private SettingOptionsItem lockScreenClear;
    private SharedPreferences spConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_setting);
        showSystemProgress = (SettingOptionsItem) findViewById(R.id.show_system_progress);
        lockScreenClear = (SettingOptionsItem) findViewById(R.id.lock_screen_clear);
        spConfig = getSharedPreferences("config", MODE_PRIVATE);
        initUpdateOption();
        showSystemProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor edit = spConfig.edit();
                //更改状态
                //根据checkbox之前的状态来改变checkbox的状态
                if (showSystemProgress.getSettingCheckBoxisCheck()) {
                    //隐藏系统进程
                    showSystemProgress.setSettingCheckBox(false);
                    //保存状态
                    edit.putBoolean("show_system_progress", false);
                    //edit.apply();//保存到文件中,但是仅限于9版本之上,9版本之下保存到内存中的
                } else {
                    //显示系统进程
                    showSystemProgress.setSettingCheckBox(true);
                    //保存状态
                    edit.putBoolean("show_system_progress", true);
                }
                edit.commit();
            }
        });

        lockScreenClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor edit = spConfig.edit();
                //更改状态
                //根据checkbox之前的状态来改变checkbox的状态
                if (lockScreenClear.getSettingCheckBoxisCheck()) {
                    //关闭锁屏清理应用
                    lockScreenClear.setSettingCheckBox(false);
                    //保存状态
                    edit.putBoolean("lock_screen_clear", false);
                    //edit.apply();//保存到文件中,但是仅限于9版本之上,9版本之下保存到内存中的
                } else {
                    //打开锁屏清理应用
                    lockScreenClear.setSettingCheckBox(true);
                    //保存状态
                    edit.putBoolean("lock_screen_clear", true);
                }
                edit.commit();
            }
        });

    }

    /**
     * 选项显示初始化操作
     */
    private void initUpdateOption() {
        if (spConfig.getBoolean("show_system_progress", true) || spConfig == null) {
            showSystemProgress.setSettingCheckBox(true);
        } else {
            showSystemProgress.setSettingCheckBox(false);
        }
        if (spConfig.getBoolean("lock_screen_clear", true) || spConfig == null) {
            lockScreenClear.setSettingCheckBox(true);
        } else {
            lockScreenClear.setSettingCheckBox(false);
        }

    }
}
