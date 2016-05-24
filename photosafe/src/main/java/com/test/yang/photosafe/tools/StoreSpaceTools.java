package com.test.yang.photosafe.tools;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**这是查询剩余空间工具类
 * Created by Administrator on 2016/5/24.
 */
public class StoreSpaceTools {

    /**
     * 这是获取ROM可用空间的方法
     * @return 返回可用空间KB数
     */
    public static long getAvailableROM(){
        File path= Environment.getDataDirectory();
        StatFs statFs=new StatFs(path.getPath());
        //获取每块的大小
        long blockSize=statFs.getBlockSize();
        //获取块的总数
        long totalBlocks=statFs.getBlockCount();
        //获取可用的块数目
        long availableBlocks=statFs.getAvailableBlocks();
        return availableBlocks*blockSize;
    }

    /**
     * 这是获取SD可用空间的方法
     * @return 返回可用空间KB数
     */
    public static long getAvailableSD(){
        File path=Environment.getExternalStorageDirectory();
        StatFs statFs=new StatFs(path.getPath());
        //获取每块的大小
        long blockSize=statFs.getBlockSize();
        //获取块的总数
        long totalBlocks=statFs.getBlockCount();
        //获取可用的块数目
        long availableBlocks=statFs.getAvailableBlocks();
        return availableBlocks*blockSize;
    }
}
