package com.test.yang.photosafe.model;

/**这是SMS信息模型类
 * Created by Administrator on 2016/5/11.
 */
public class SMSInfo {

    private String smsAddress;
    private String smsBody;


    public String getSmsAddress() {
        return smsAddress;
    }

    public void setSmsAddress(String smsAddress) {
        this.smsAddress = smsAddress;
    }

    public String getSmsBody() {
        return smsBody;
    }

    public void setSmsBody(String smsBody) {
        this.smsBody = smsBody;
    }


}
