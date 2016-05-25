package com.test.yang.mobilesecurity.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/5/8.
 */
public class MD5Tools {
    public  static String passwordMD5(String password){
        StringBuilder sb=new StringBuilder();
        try {
            MessageDigest messageDigest=MessageDigest.getInstance("MD5");
            byte[] digest=messageDigest.digest(password.getBytes());
            for(int i=0;i<digest.length;i++){
                int result=digest[i];
                String hexString=Integer.toHexString(result)+1;
                if(hexString.length()<2){
                    sb.append("0");
                }
                sb.append(hexString);
            }
            return  sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
