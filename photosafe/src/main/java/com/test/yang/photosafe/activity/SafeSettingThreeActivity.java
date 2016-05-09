package com.test.yang.photosafe.activity;

import android.content.Intent;
import android.os.Bundle;

import com.test.yang.photosafe.R;

/**这是手机防盗设置界面三
 * Created by Administrator on 2016/5/9.
 */
public class SafeSettingThreeActivity extends SafeSettingBaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_setting_three);

    }

    @Override
    public void nextActivitySubClass() {
        Intent nextActivity=new Intent(this,SafeSettingFourActivity.class);
        startActivity(nextActivity);
        finish();
    }

    @Override
    public void previousActivitySubClass() {
        Intent previousActivity=new Intent(this,SafeSettingTwoActivity.class);
        startActivity(previousActivity);
        finish();
    }
}
