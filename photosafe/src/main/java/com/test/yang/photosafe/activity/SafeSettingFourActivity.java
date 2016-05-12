package com.test.yang.photosafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.test.yang.photosafe.R;

/**这是手机防盗设置界面四
 * Created by Administrator on 2016/5/9.
 */
public class SafeSettingFourActivity extends SafeSettingBaseActivity{

    private CheckBox safeProtectCheckBox;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_setting_four);
        editor=sharedPreferences.edit();
        safeProtectCheckBox= (CheckBox) findViewById(R.id.safe_protect_check_box);
        if(sharedPreferences.getBoolean("safe_protect",false)){
            safeProtectCheckBox.setText("你已经开启了防盗保护");
            safeProtectCheckBox.setChecked(true);
        }else{
            safeProtectCheckBox.setText("你已经关闭了防盗保护");
            safeProtectCheckBox.setChecked(false);
        }
        safeProtectCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    safeProtectCheckBox.setText("你已经开启了防盗保护");
                    safeProtectCheckBox.setChecked(true);
                    editor.putBoolean("safe_protect",true);
                }else{{
                    safeProtectCheckBox.setText("你已经关闭了防盗保护");
                    safeProtectCheckBox.setChecked(false);
                    editor.putBoolean("safe_protect",false);
                }}
                editor.commit();
            }
        });
    }

    @Override
    public void nextActivitySubClass() {
        editor.putBoolean("first_enter_safe",false);
        editor.commit();
        Intent nextActivity=new Intent(this,PhotoSafeActivity.class);
        startActivity(nextActivity);
        finish();
        overridePendingTransition(R.anim.next_enter_activity_one,R.anim.next_exit_activity_one);
    }

    @Override
    public void previousActivitySubClass() {
        Intent previousActivity=new Intent(this,SafeSettingThreeActivity.class);
        startActivity(previousActivity);
        finish();
        overridePendingTransition(R.anim.previous_enter_activity_one,R.anim.previous_exit_activity_one);

    }
}
