package com.test.yang.photosafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.test.yang.photosafe.R;

/**这是手机防盗设置界面四
 * Created by Administrator on 2016/5/9.
 */
public class SafeSettingFourActivity extends SafeSettingBaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_setting_four);

    }

    @Override
    public void nextActivitySubClass() {
        SharedPreferences.Editor editor=sharedPreferences.edit();
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
