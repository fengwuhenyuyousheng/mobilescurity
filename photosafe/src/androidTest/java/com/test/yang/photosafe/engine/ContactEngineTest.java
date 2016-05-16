package com.test.yang.photosafe.engine;

import android.test.AndroidTestCase;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/5/12.
 */
public class ContactEngineTest extends AndroidTestCase {


    public void testGetAllContactInfo() throws Exception {
        List<HashMap<String,String>> allContactsList= ContactEngine.getAllContactInfo(getContext());
        for(HashMap<String,String> hashMap:allContactsList){
            Log.d("ContactEngineTest",hashMap.get("name")+"-----"+hashMap.get("number"));
        }
    }
}