package com.test.yang.mobilesecurity.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.test.yang.mobilesecurity.service.WidgetService;

/**这是自定义桌面小控件
 * Created by Administrator on 2016/5/25.
 */
public class MyWidget extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Intent startWidgetService=new Intent(context,WidgetService.class);
        context.startService(startWidgetService);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Intent startWidgetService=new Intent(context,WidgetService.class);
        context.startService(startWidgetService);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Intent stopWidgetService=new Intent(context,WidgetService.class);
        context.stopService(stopWidgetService);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}
