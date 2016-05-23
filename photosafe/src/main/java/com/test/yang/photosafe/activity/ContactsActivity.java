package com.test.yang.photosafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.test.yang.photosafe.R;
import com.test.yang.photosafe.engine.ContactEngine;
import com.test.yang.photosafe.tools.MyAsycnTaks;

import java.util.HashMap;
import java.util.List;



/**这是联系人界面
 * Created by Administrator on 2016/5/10.
 */
public class ContactsActivity extends AppCompatActivity{

    //使用框架注解控件
    @ViewInject(R.id.contacts_list_view)
    private ListView contactsListView;
    @ViewInject(R.id.load_progress_bar)
    private ProgressBar loadProgressBar;

    private List<HashMap<String,String>> allContactsList;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_contacts);
        //通过框架初始化注解的控件
        ViewUtils.inject(this);

        new MyAsycnTaks(){

            @Override
            public void preTask() {
                loadProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void doInTask() {
                //获取联系人
                allContactsList= ContactEngine.getAllContactInfo(getApplicationContext());
                for(HashMap<String,String> hashMap:allContactsList){
                    Log.d("ContactEngine",hashMap.get("name")+"-----"+hashMap.get("number"));
                }
            }

            @Override
            public void posTask() {
                contactsListView.setAdapter(new MyContactsAdapter());
                loadProgressBar.setVisibility(View.GONE);
            }
        }.execute();




        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //将点击联系人的号码传递给设置安全号码界面
                Intent intent = new Intent();
                intent.putExtra("number", allContactsList.get(position).get("number"));
                //将数据传递给设置安全号码界面
                //设置结果的方法,会将结果传递给调用当前activity的activity
                setResult(RESULT_OK, intent);
                //移出界面
                finish();
            }
        });

    }
    private class MyContactsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return allContactsList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(getApplicationContext(),R.layout.item_contact,null);
            TextView contactName= (TextView) view.findViewById(R.id.contact_name_text);
            TextView contactNumber= (TextView) view.findViewById(R.id.contact_number_text);
            contactName.setText(allContactsList.get(position).get("name"));
            contactNumber.setText(allContactsList.get(position).get("number"));
            return view;
        }
    }
}
