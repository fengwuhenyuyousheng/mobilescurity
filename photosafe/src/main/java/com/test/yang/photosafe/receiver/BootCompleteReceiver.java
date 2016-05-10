package com.test.yang.photosafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**这是监听手机状态类
 * Created by Administrator on 2016/5/10.
 */
public class BootCompleteReceiver extends BroadcastReceiver{

    private SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BootCompleteReceiver","手机重启了.........");
        sharedPreferences=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        String oldSIMNumber=sharedPreferences.getString("SIM","");
        TelephonyManager telephonyManager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String newSIMNumber=telephonyManager.getSimSerialNumber();
        if(!TextUtils.isEmpty(oldSIMNumber) && !TextUtils.isEmpty(newSIMNumber)){
            if(!newSIMNumber.equals(oldSIMNumber)){
                String safeNumber=sharedPreferences.getString("safe_number","");
                if (!TextUtils.isEmpty(safeNumber)) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(safeNumber, null, "我手机被盗了，请帮忙,谢谢", null, null);
                    Log.d(getClass().toString(),"发送报警短信给"+safeNumber);
                }
            }
        }

    }
}
