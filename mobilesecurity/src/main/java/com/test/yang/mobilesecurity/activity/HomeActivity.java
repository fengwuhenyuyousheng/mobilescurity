package com.test.yang.mobilesecurity.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.test.yang.mobilesecurity.R;
import com.test.yang.mobilesecurity.adapters.HomeGridViewAdapter;
import com.test.yang.mobilesecurity.tools.MD5Tools;

/**这是主界面
 * Created by Administrator on 2016/5/4.
 */
public class HomeActivity extends AppCompatActivity {

    private GridView mHomeGridView;
    private int mSetPasswordSeeCount;
    private int mInputPasswordSeeCount;
    private AlertDialog alertDialog;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sp=getSharedPreferences("config",MODE_PRIVATE);
        mHomeGridView= (GridView) findViewById(R.id.home_grid_view);
        mHomeGridView.setAdapter(new HomeGridViewAdapter(getApplicationContext()));
        mHomeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        if(TextUtils.isEmpty(sp.getString("password",""))){
                            showSetPasswordDialog();
                        }else{
                            showInputPasswordDialog();
                        }
                        break;
                    case 1:
                        Intent jumpBlackRollActivity=new Intent(HomeActivity.this,BlackRollActivity.class);
                        startActivity(jumpBlackRollActivity);
                        break;
                    case 2:
                        Intent jumpSoftwareManagementActivity=new Intent(HomeActivity.this,SoftwareManagerActivity.class);
                        startActivity(jumpSoftwareManagementActivity);
                        break;
                    case 3:
                        Intent jumpTaskManagerActivity=new Intent(HomeActivity.this,TaskManagerActivity.class);
                        startActivity(jumpTaskManagerActivity);
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        Intent jumpSearchSpaceActivity=new Intent(HomeActivity.this,SearchSpaceActivity.class);
                        startActivity(jumpSearchSpaceActivity);
                        break;
                    case 8:
                        Intent jumpSettingActivity=new Intent(HomeActivity.this,SettingActivity.class);
                        startActivity(jumpSettingActivity);
                        break;
                    default:
                        break;
                }
            }
        });

    }
    /**
     * 建立设置密码对话框
     */
    protected void showSetPasswordDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View view=View.inflate(getApplicationContext(),R.layout.dialog_set_password,null);
        final EditText etSetPassword= (EditText) view.findViewById(R.id.set_password);
        final EditText etAffirmPassword= (EditText) view.findViewById(R.id.affirm_password);
        Button btCancel=(Button)view.findViewById(R.id.cancel_button);
        Button btEnsure= (Button) view.findViewById(R.id.ensure_button);
        ImageView ivSetPasswordSee= (ImageView) view.findViewById(R.id.set_passoword_see);
        ImageView ivAffirmPasswordSee= (ImageView) view.findViewById(R.id.affirm_password_see);
        ivSetPasswordSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSetPasswordSeeCount++;
                if(mSetPasswordSeeCount%2==1){
                    //显示密码
                    etSetPassword.setInputType(2);
                    etAffirmPassword.setInputType(2);
                }else{
                    //隐藏密码
                    etSetPassword.setInputType(129);
                    etAffirmPassword.setInputType(129);
                }
            }
        });
        ivAffirmPasswordSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSetPasswordSeeCount++;
                if(mSetPasswordSeeCount%2==1){
                    //显示密码
                    etSetPassword.setInputType(2);
                    etAffirmPassword.setInputType(2);
                }else{
                    //隐藏密码
                    etSetPassword.setInputType(129);
                    etAffirmPassword.setInputType(129);
                }
            }
        });
        btEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //得到一个去除空格的字符串
                String password=etSetPassword.getText().toString().trim();
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),"请输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                String affirmPassword=etAffirmPassword.getText().toString().trim();
                if(password.equals(affirmPassword)){
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString("password", MD5Tools.passwordMD5(password));
                    editor.commit();
                    alertDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"密码设置成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"两次密码输入不同",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.show();

    }
    /**
     * 建立输入密码对话框
     */
    protected void showInputPasswordDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View view=View.inflate(getApplicationContext(),R.layout.dialog_input_password,null);
        final EditText etInputPassword= (EditText) view.findViewById(R.id.affirm_password);
        Button btInputCancel=(Button)view.findViewById(R.id.cancel_button);
        Button btInputEnsure= (Button) view.findViewById(R.id.ensure_button);
        ImageView ivInputPassword= (ImageView) view.findViewById(R.id.input_password_see);
        ivInputPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputPasswordSeeCount++;
                if(mInputPasswordSeeCount%2==1){
                    //显示密码
                    etInputPassword.setInputType(2);
                }else{
                    //隐藏密码
                    etInputPassword.setInputType(129);
                }
            }
        });
        btInputEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPassword=etInputPassword.getText().toString().trim();
                if(TextUtils.isEmpty(inputPassword)){
                    Toast.makeText(getApplicationContext(),"请输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                String savePassword=sp.getString("password","");
                if(MD5Tools.passwordMD5(inputPassword).equals(savePassword)){
                    alertDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"密码正确",Toast.LENGTH_SHORT).show();
                    Intent jumpPhotoSafeActivity=new Intent(HomeActivity.this,PhotoSafeActivity.class);
                    startActivity(jumpPhotoSafeActivity);
                    return;
                }else{
                    Toast.makeText(getApplicationContext(),"密码不正确",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        btInputCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.show();
    }
}
