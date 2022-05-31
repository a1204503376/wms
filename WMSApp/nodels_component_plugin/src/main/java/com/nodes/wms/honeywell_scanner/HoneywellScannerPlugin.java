package com.nodes.wms.honeywell_scanner;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.nodes.wms.HoneywellScannerComponent;

public class HoneywellScannerPlugin implements ScannerCallBack {

    private static final String _METHOD_CHANNEL = "honeywellscanner";
    private static final String _SET_PROPERTIES = "setProperties";
    private static final String _START_SCANNER = "startScanner";
    private static final String _RESUME_SCANNER = "resumeScanner";
    private static final String _PAUSE_SCANNER = "pauseScanner";
    private static final String _STOP_SCANNER = "stopScanner";
    private static final String _ON_DECODED = "onDecoded";
    private static final String _ON_ERROR = "onError";

    private Handler handler;
    private HoneywellScannerComponent channel;
    private HoneywellScanner scanner;

    public HoneywellScannerPlugin(HoneywellScannerComponent component) {
        handler = new Handler(Looper.getMainLooper());
        channel = component;
        Context context = component.getContext();
        scanner = new HoneywellScannerBroadcasts(context);
        scanner.setScannerCallBack(this);
        scanner.startScanner();
    }

    /**
     * 插件注册
     */
    public static void registerWith(HoneywellScannerComponent registrar) {
        new HoneywellScannerPlugin(registrar);
    }

    /**
     * 当解码器成功解码代码时调用
     * <br>
     * 请注意，此方法始终在工作线程上调用
     *
     * @param code 封装图像中条形码的解码结果
     */
    @Override
    public void onDecoded(final String code) {
        handler.post(() -> channel.fireEvent(_ON_DECODED, channel.onDecoded(code)));
    }

    /**
     * 发生错误时调用
     * <br>
     * 请注意，此方法始终在工作线程上调用
     *
     * @param error 已抛出的异常
     */
    @Override
    public void onError(final Exception error) {
        handler.post(() -> channel.fireEvent(_ON_ERROR, channel.getFailedParams(error.getMessage())));
    }
}