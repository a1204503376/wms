package com.nodes.dml;

import android.util.Log;
import com.alibaba.fastjson.JSONObject;
import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class TestModule extends UniModule {

    private static final String TAG = "TestModule";

    @UniJSMethod(uiThread = false)
    public void add(JSONObject options, UniJSCallback callback) {
        Log.i(TAG, "add--" + options);
        Integer a = options.getInteger("a");
        Integer b = options.getInteger("b");
        if (callback != null) {
            JSONObject data = new JSONObject();
            data.put("code", "success");
            data.put("result", a + b);
            callback.invoke(data);
            //callback.invokeAndKeepAlive(data);
        }
    }
}
