package com.test.yang.photosafe.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.test.yang.photosafe.R;
import com.test.yang.photosafe.db.DBOperate;

/**这是号码归属地查询界面
 * Created by Administrator on 2016/5/14.
 */
public class SearchSpaceActivity extends AppCompatActivity{

    private EditText searchSpaceEditText;
    private Button searchSpaceButton;
    private TextView searchSpaceText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_space);
        searchSpaceEditText= (EditText) findViewById(R.id.search_space_number_edit_text);
        searchSpaceButton= (Button) findViewById(R.id.search_space_button);
        searchSpaceText= (TextView) findViewById(R.id.search_space_text);
        searchSpaceEditText.addTextChangedListener(new TextWatcher() {
            //当文本变化之前调用
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            //文本变化时调用
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //获取输入框内容
                String number=searchSpaceEditText.getText().toString().trim();
                //查询号码归属地
                if(!TextUtils.isEmpty(number)){
                    String queryAddress = DBOperate.queryAddress(number, getApplicationContext());
                    if (!TextUtils.isEmpty(queryAddress)) {
                        searchSpaceText.setText(queryAddress);
                    }
                }
            }
            //当文本变化之后调用
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 点击开始查询归属地
     * @param v
     */
    public void searchSpace(View v){
            //1.获取输入的号码
            String phone = searchSpaceEditText.getText().toString().trim();
            //2.判断号码是否为空
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(getApplicationContext(), "请输入要查询号码", Toast.LENGTH_SHORT).show();
                return;
            }
            //3.根据号码查询号码归属地
            String queryAddress = DBOperate.queryAddress(phone, getApplicationContext());
            //4.判断查询的号码归属地是否为空
            if (!TextUtils.isEmpty(queryAddress)) {
                searchSpaceText.setText(queryAddress);
            }

    }
}
