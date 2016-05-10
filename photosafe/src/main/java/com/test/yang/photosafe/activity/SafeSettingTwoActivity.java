package com.test.yang.photosafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.test.yang.photosafe.R;
import com.test.yang.photosafe.ui.SettingOptionsItem;

/**这是手机防盗设置界面二
 * Created by Administrator on 2016/5/9.
 */
public class SafeSettingTwoActivity extends SafeSettingBaseActivity{

    private SettingOptionsItem bindSIMItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_setting_two);
        bindSIMItem= (SettingOptionsItem) findViewById(R.id.bind_sim_item);
        if(TextUtils.isEmpty(sharedPreferences.getString("SIM",""))){
            bindSIMItem.setSettingCheckBox(false);
        }else{
            bindSIMItem.setSettingCheckBox(true);
        }
        bindSIMItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                if(bindSIMItem.getSettingCheckBoxisCheck()){
                    editor.putString("SIM","");

                }else{
                    TelephonyManager telephonyManager= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String simSerialNumber=telephonyManager.getSimSerialNumber();
                    editor.putString("SIM",simSerialNumber);
                    bindSIMItem.setSettingCheckBox(true);
                }
                editor.commit();
            }
        });

    }

    @Override
    public void nextActivitySubClass() {
        Intent nextActivity=new Intent(this,SafeSettingThreeActivity.class);
        startActivity(nextActivity);
        finish();
        overridePendingTransition(R.anim.next_enter_activity_one,R.anim.next_exit_activity_one);
    }

    @Override
    public void previousActivitySubClass() {
        Intent previousActivity=new Intent(this,SafeSettingOneActivity.class);
        startActivity(previousActivity);
        finish();
        overridePendingTransition(R.anim.previous_enter_activity_one,R.anim.previous_exit_activity_one);
    }
}
