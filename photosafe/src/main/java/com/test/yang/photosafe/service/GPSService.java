package com.test.yang.photosafe.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;

import java.util.List;

/**
 * 这是获取GPS信息的服务
 */
public class GPSService extends Service {

    private SharedPreferences sp;
    private LocationManager locationManager;
    private MyLocationListener myLocationListener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences("config", MODE_PRIVATE);
        // 1.获取位置的管理者
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // 获取所有的定位方式
        // enabledOnly : true : 返回所有可用的定位方式
        List<String> providers = locationManager.getProviders(true);
        for (String string : providers) {
            System.out.println(string);
        }
        // 2.2获取最佳的定位方式
        Criteria criteria = new Criteria();
        criteria.setAltitudeRequired(true);
        // 设置是否可以定位海拔,true:可以定位海拔,一定返回gps定位
        // criteria : 设置定位的属性,决定使用什么定位方式的
        // enabledOnly : true : 定位可用的就返回
        String bestProvider = locationManager.getBestProvider(criteria, true);
        System.out.println("最佳的定位方式:" + bestProvider);
        // 3.定位
        myLocationListener = new MyLocationListener();
        // provider : 定位方式
        // minTime : 定位的最小时间间隔
        // minDistance : 定位的最小距离间隔
        // listener : LocationListener
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(bestProvider, 0, 0, myLocationListener);
    }

    private class MyLocationListener implements LocationListener{


        //当定位位置改变的时候调用
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();//获取纬度,平行
            double longitude = location.getLongitude();//获取经度
            //给安全号码发送一个包含经纬度坐标的短信
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(sp.getString("safe_number", ""), null, "longitude:"+longitude+"  latitude:"+latitude, null, null);
            //停止服务
            stopSelf();
        }

        //当定位状态改变的时候调用
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        //当定位可用的时候调用
        @Override
        public void onProviderEnabled(String provider) {

        }

        //当定位不可用的时候调用
        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
