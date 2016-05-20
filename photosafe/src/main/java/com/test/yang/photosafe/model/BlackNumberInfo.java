package com.test.yang.photosafe.model;

/**这是黑名单信息类
 * Created by Administrator on 2016/5/19.
 */
public class BlackNumberInfo {

    private String mBlackNumber;
    private int mBlackNumberMode;

    public BlackNumberInfo(){

    }

    public BlackNumberInfo(String blackNumber,int blackNumberMode){
        this.mBlackNumber=blackNumber;
        this.mBlackNumberMode=blackNumberMode;
    }


    public String getBlackNumber() {
        return mBlackNumber;
    }

    public void setBlackNumber(String mBlackNumber) {
        this.mBlackNumber = mBlackNumber;
    }

    public int getBlackNumberMode() {
        return mBlackNumberMode;
    }

    public void setBlackNumberMode(int mBlackNumberMode) {
        if(mBlackNumberMode>=0 && mBlackNumberMode<=2) {
            this.mBlackNumberMode = mBlackNumberMode;
        }else{
            this.mBlackNumberMode=0;
        }
    }
    public String toString(){
        return this.mBlackNumber+"-------"+this.mBlackNumberMode;
    }
}
