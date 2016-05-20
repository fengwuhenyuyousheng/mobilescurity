package com.test.yang.photosafe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.test.yang.photosafe.R;
import com.test.yang.photosafe.db.BlackNumberDBOperate;
import com.test.yang.photosafe.model.BlackNumberInfo;
import com.test.yang.photosafe.tools.MyAsycnTaks;

import java.util.List;

/**这是通讯卫士黑名单界面
 * Created by Administrator on 2016/5/19.
 */
public class BlackRollActivity extends AppCompatActivity {

    private ListView mBlackNumberListView;
    private ProgressBar mLoadProgressBar;
    private Button mAddBlackNumberButton;
    private Button mDeleteAllBlackNumberButton;
    private List<BlackNumberInfo> mBlackNumberInfoList;
    private BlackNumberDBOperate mBlackNumberDBOperate;
    private MyBlackRollListViewAdapter mMyBlackRollListViewAdapter;
    private AlertDialog mAlertDialog;
    private int maxNumber=20;
    private int startIndex=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_black_roll);
        mBlackNumberListView= (ListView) findViewById(R.id.black_number_list_view);
        mLoadProgressBar= (ProgressBar) findViewById(R.id.load_progress_bar);
        mAddBlackNumberButton= (Button) findViewById(R.id.add_black_number_button);
        mDeleteAllBlackNumberButton= (Button) findViewById(R.id.delete_all_black_number_button);
        mBlackNumberDBOperate=new BlackNumberDBOperate(getApplicationContext());
        loadBlackNumberFromDB();
        scrollListener();
    }

    /**
     * 从数据库中异步加载数据
     */
    private void loadBlackNumberFromDB(){

        new MyAsycnTaks() {
            @Override
            public void preTask() {
                mLoadProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void doinTask() {
//                Log.d("BlackRollActivity","数据库查询开始");
                if(mBlackNumberInfoList==null) {
                    mBlackNumberInfoList = mBlackNumberDBOperate.querySubStepBlackNumber(maxNumber, startIndex);
//                Log.d("BlackRollActivity","数据库查询完成");
                }else{
                    mBlackNumberInfoList.addAll(mBlackNumberDBOperate.querySubStepBlackNumber(maxNumber, startIndex));
                }
        }

            @Override
            public void posTask() {

                if(mMyBlackRollListViewAdapter == null){
                    mMyBlackRollListViewAdapter=new MyBlackRollListViewAdapter();
                    mBlackNumberListView.setAdapter(mMyBlackRollListViewAdapter);
                }else
                {
                    mMyBlackRollListViewAdapter.notifyDataSetChanged();
                }
                mLoadProgressBar.setVisibility(View.INVISIBLE);
            }
        }.execute();
    }

    /**
     * 定义ListView滑动监听事件
     */
    private  void scrollListener(){
        mBlackNumberListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //scrollState : 滑动状态
                //SCROLL_STATE_IDLE : 空闲的状态
                //SCROLL_STATE_TOUCH_SCROLL : 缓慢滑动的状态
                //SCROLL_STATE_FLING : 快速滑动
                //当listview静止的时候判断界面显示的最后一个条目是否是查询数据的最后一个条目,是加载下一波数据,不是用户进行其他操作
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //获取界面显示最后一个条目
                    int position = mBlackNumberListView.getLastVisiblePosition();//获取界面显示最后一个条目,返回的时候条目的位置
                    //判断是否是查询数据的最后一个数据  20   0-19
                    if (position == mBlackNumberInfoList.size()-1) {
                        //加载下一波数据
                        //更新查询的其实位置   0-19    20-39
                        startIndex+=maxNumber;
                        loadBlackNumberFromDB();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    /**
     * 定义添加按钮点击事件
     */
    public void addBlackNumber(View v){
        //弹出对话框,让用户去添加黑名单
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_add_black_number, null);
        //初始化控件,执行添加操作
        final EditText addBlackNumberEditText= (EditText) view.findViewById(R.id.add_black_number_edit_text);
        final RadioGroup blackNumberModeRadioGroup = (RadioGroup) view.findViewById(R.id.black_number_mode_radio_group);
        Button ensureButton = (Button) view.findViewById(R.id.ensure_button);
        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);

        //设置按钮的点击事件
        ensureButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //添加黑名单号码操作
                //1.获取输入的黑名单号码
                String blackNumber = addBlackNumberEditText.getText().toString().trim();
                //2.判断获取的内容是否为空
                if (TextUtils.isEmpty(blackNumber)) {
                    Toast.makeText(getApplicationContext(), "请输入黑名单号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //3.获取拦截模式
                int mode = -1;
                int radioButtonId = blackNumberModeRadioGroup.getCheckedRadioButtonId();//获取选中的RadioButton的id
                switch (radioButtonId) {
                    case R.id.black_number_mode_call:
                        //电话拦截
                        mode = BlackNumberDBOperate.MODE_CALL;
                        break;
                    case R.id.black_number_mode_sms:
                        //短信拦截
                        mode = BlackNumberDBOperate.MODE_SMS;
                        break;
                    case R.id.black_number_mode_all:
                        //全部拦截
                        mode = BlackNumberDBOperate.MODE_ALL;
                        break;
                }
                //4.添加黑名单
                //1.添加到数据库
                mBlackNumberDBOperate.addBlackNumber(blackNumber, mode);
                //2.添加到界面显示
                //2.1添加到list集合中
                mBlackNumberInfoList.add(0, new BlackNumberInfo(blackNumber, mode));//location : 参数2要添加到位置,参数2:添加数据
                //2.2更新界面
                mMyBlackRollListViewAdapter.notifyDataSetChanged();
                //隐藏对话框
                mAlertDialog.dismiss();
            }
        });
        //设置取消按钮的点击事件
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //隐藏对话框
                mAlertDialog.dismiss();
            }
        });

        builder.setView(view);
        //builder.show();
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    /**
     * 自定义黑名单界面适配器
     */
    private class MyBlackRollListViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mBlackNumberInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return mBlackNumberInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final BlackNumberInfo blackNumberInfo = mBlackNumberInfoList.get(position);
            View view;
            ViewHolder viewHolder;
            if(convertView==null){
                view=View.inflate(getApplicationContext(),R.layout.item_black_roll,null);
                viewHolder=new ViewHolder();
                viewHolder.blackNumberText= (TextView) view.findViewById(R.id.black_number_text);
                viewHolder.blackNumberModeText= (TextView) view.findViewById(R.id.black_number_mode_text);
                viewHolder.blackNumberImageView= (ImageView) view.findViewById(R.id.black_number_image_view);
                view.setTag(viewHolder);
            }else{
                view=convertView;
                viewHolder= (ViewHolder) view.getTag();
            }
            viewHolder.blackNumberText.setText(blackNumberInfo.getBlackNumber());
            int blackNumberMode=blackNumberInfo.getBlackNumberMode();
            switch (blackNumberMode){
                case BlackNumberDBOperate.MODE_CALL:
                    viewHolder.blackNumberModeText.setText("电话拦截");
                    break;
                case BlackNumberDBOperate.MODE_SMS:
                    viewHolder.blackNumberModeText.setText("短信拦截");
                    break;
                case BlackNumberDBOperate.MODE_ALL:
                    viewHolder.blackNumberModeText.setText("电话、短信拦截");
                    break;
                default:
                    break;
            }
            viewHolder.blackNumberImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除黑名单操作
                    AlertDialog.Builder builder = new AlertDialog.Builder(BlackRollActivity.this);
                    builder.setMessage("您确认要删除黑名单号码:"+blackNumberInfo.getBlackNumber()+"?");
                    //设置确定和取消按钮
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //删除黑名单操作
                            //1.删除数据库中的黑名单号码
                            mBlackNumberDBOperate.deleteBlackNumber(blackNumberInfo.getBlackNumber());
                            //2.删除界面中已经显示黑名单号码
                            //2.1从存放有所有数据的list集合中删除相应的数据
                            mBlackNumberInfoList.remove(position);//删除条目对应位置的相应的数据
                            //2.2更新界面
                            mMyBlackRollListViewAdapter.notifyDataSetChanged();//更新界面
                            //3.隐藏对话框
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.show();
                }
            });
            return view;
        }
    }

    /**
     * 存放控件的容器
     */
    protected static class ViewHolder{
        TextView blackNumberText,blackNumberModeText;
        ImageView blackNumberImageView;
    }
}

