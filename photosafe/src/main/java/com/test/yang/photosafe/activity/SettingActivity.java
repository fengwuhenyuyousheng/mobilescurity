package com.test.yang.photosafe.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.test.yang.photosafe.R;
import com.test.yang.photosafe.ui.SettingOptionsItem;

/**这是设置中心界面
 * Created by Administrator on 2016/5/6.
 */
public class SettingActivity extends AppCompatActivity{

    private SettingOptionsItem updateSettingItem;
    private SharedPreferences spConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_center);
        spConfig=getSharedPreferences("config",MODE_PRIVATE);
        updateSettingItem=(SettingOptionsItem)findViewById(R.id.update_option);
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
    }
}
