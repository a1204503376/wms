package com.nodes.wms;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;
import com.nodes.wms.honeywell_scanner.HoneywellScannerPlugin;
import io.dcloud.feature.uniapp.UniSDKInstance;
import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.ui.action.AbsComponentData;
import io.dcloud.feature.uniapp.ui.component.AbsVContainer;
import io.dcloud.feature.uniapp.ui.component.UniComponent;
import io.dcloud.feature.uniapp.ui.component.UniComponentProp;

import java.util.HashMap;
import java.util.Map;

public class HoneywellScannerComponent
        extends UniComponent<TextView> {
    private String type;

    public HoneywellScannerComponent(UniSDKInstance instance, AbsVContainer parent, AbsComponentData basicComponentData) {
        super(instance, parent, basicComponentData);
        HoneywellScannerPlugin.registerWith(this);
    }

    @Override
    protected TextView initComponentHostView(Context context) {
        TextView textView = new TextView(context);
        textView.setTextSize(20);
        textView.setBackgroundColor(Color.GREEN);
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    @UniComponentProp(name = "type")
    public void setType(String type) {
        this.type = type;
//        getHostView().setText("type: " + type);
//        Map<String, Object> params = new HashMap<>();
//        Map<String, Object> number = new HashMap<>();
//        number.put("tel", type);
//        //目前uni限制 参数需要放入到"detail"中 否则会被清理
//        params.put("detail", number);
//        fireEvent("onScanning", params);
    }

    public Map<String, Object> onDecoded(String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("code", 0);
        params.put("scannerResult", code);
        getHostView().setText(code);
        return setParams(params);
    }

    public Map<String, Object> getFailedParams(String errorMsg) {
        Map<String, Object> params = new HashMap<>();
        params.put("code", 1);
        params.put("errorMsg", errorMsg);
        return setParams(params);
    }

    private Map<String, Object> setParams(Map<String, Object> detail) {
        Map<String, Object> params = new HashMap<>();
        //目前uni限制 参数需要放入到"detail"中 否则会被清理
        params.put("detail", detail);
        return params;
    }

    @UniJSMethod
    public void clearCode() {
        getHostView().setText("");
    }

    @Override
    public void onActivityResume() {
        super.onActivityResume();
    }

    @Override
    public void onActivityPause() {
        super.onActivityPause();
    }

    @Override
    public void onActivityDestroy() {
        super.onActivityDestroy();
    }
}
