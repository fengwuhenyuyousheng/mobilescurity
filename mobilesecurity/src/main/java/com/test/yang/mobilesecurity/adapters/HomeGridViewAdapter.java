package com.test.yang.mobilesecurity.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yang.mobilesecurity.R;

/**这是主界面GridView的自定义适配器
 * Created by Administrator on 2016/5/6.
 */
public class HomeGridViewAdapter extends BaseAdapter{

    int[] imageId = { R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
            R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
            R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings };
    String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理",
            "高级工具", "设置中心" };

    private Context mContext;

    public HomeGridViewAdapter(Context context){
        this.mContext=context;
    }

    @Override
    public int getCount() {
        return 9;
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
        View view=View.inflate(mContext,R.layout.item_home_gridview,null);
        ImageView ivImageView= (ImageView) view.findViewById(R.id.home_grid_view_image);
        TextView tvTextView= (TextView) view.findViewById(R.id.home_grid_view_text);
        ivImageView.setImageResource(imageId[position]);
        tvTextView.setText(names[position]);
        return view;
    }
}
