package com.nodes.wms.honeywell_scanner;

import android.content.Context;

import java.util.Map;


public abstract class HoneywellScanner implements ScannerCallBack {
    protected final Context context;
    private ScannerCallBack scannerCallBack;

    public HoneywellScanner(Context context) {
        this.context = context;
    }

    public void setScannerCallBack(ScannerCallBack scannerCallBack) {
        this.scannerCallBack = scannerCallBack;
    }

    /**
     * 当解码器成功解码代码时调用
     * <br>
     * 请注意，此方法始终在工作线程上调用
     *
     * @param code 封装图像中条形码的解码结果
     */
    @Override
    public void onDecoded(String code) {
        if (scannerCallBack != null) scannerCallBack.onDecoded(code);
    }

    /**
     * 发生错误时调用
     * <br>
     * 请注意，此方法始终在工作线程上调用
     *
     * @param error 已抛出的异常
     */
    @Override
    public void onError(Exception error) {
        if (scannerCallBack != null) scannerCallBack.onError(error);
    }

    public abstract void setProperties(Map<String, Object> mapProperties);

    public abstract boolean startScanner();

    public abstract boolean resumeScanner();

    public abstract boolean pauseScanner();

    public abstract boolean stopScanner();
}