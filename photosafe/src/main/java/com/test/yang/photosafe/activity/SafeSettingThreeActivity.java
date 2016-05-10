package com.test.yang.photosafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.test.yang.photosafe.R;

/**这是手机防盗设置界面三
 * Created by Administrator on 2016/5/9.
 */
public class SafeSettingThreeActivity extends SafeSettingBaseActivity{

    private EditText safeNumberEditText;
    private Button saveSafeNumber;
    private Button chooseContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_setting_three);
        safeNumberEditText= (EditText) findViewById(R.id.safe_number_edittext);
        safeNumberEditText.setText(sharedPreferences.getString("safe_number",""));
        chooseContact= (Button) findViewById(R.id.choose_contact_button);
        saveSafeNumber= (Button) findViewById(R.id.save_safe_number_button);
        saveSafeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String safeNumber=safeNumberEditText.getText().toString().trim();
                if(!TextUtils.isEmpty(safeNumber)){
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("safe_number",safeNumber);
                    editor.commit();
                }else{
                    Toast.makeText(SafeSettingThreeActivity.this,"请输入安全号码",Toast.LENGTH_SHORT).show();
                }
            }
        });

        chooseContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jumpContactsActivity=new Intent(SafeSettingThreeActivity.this,ContactsActivity.class);
                startActivityForResult(jumpContactsActivity,0);
            }
        });

    }

    @Override
    public void nextActivitySubClass() {
        Intent nextActivity=new Intent(this,SafeSettingFourActivity.class);
        startActivity(nextActivity);
        finish();
        overridePendingTransition(R.anim.next_enter_activity_one,R.anim.next_exit_activity_one);
    }

    @Override
    public void previousActivitySubClass() {
        Intent previousActivity=new Intent(this,SafeSettingTwoActivity.class);
        startActivity(previousActivity);
        finish();
        overridePendingTransition(R.anim.previous_enter_activity_one,R.anim.previous_exit_activity_one);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null) {
            //接受选择联系人界面传递过来的数据,null.方法      参数为null
            String number = data.getStringExtra("number");
            //将获取到的号码,设置给安全号码输入框
            safeNumberEditText.setText(number);
        }
    }
}
