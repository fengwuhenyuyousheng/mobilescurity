package com.test.yang.mobilesecurity.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 流文件工具类
 * Created by Administrator on 2016/5/4.
 */
public class StreamTools {
    /**
     * 根据传入的流文件转换为字符串
     * @param inputStream  InputStream 流文件
     * @return String 流文件转换的字符串response
     */
    public static String parseStreamUtil(InputStream inputStream) throws IOException {
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer response=new StringBuffer();
        String line=null;
        while ((line=reader.readLine())!=null){
            response.append(line);
        }
        reader.close();
//        Log.d("StreamTools",response.toString());
        return response.toString();

    }
}
