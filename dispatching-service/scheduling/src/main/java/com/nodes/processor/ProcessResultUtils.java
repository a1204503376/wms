package com.nodes.processor;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.nodes.common.utils.StringUtils;
import tech.powerjob.worker.core.processor.ProcessResult;

/**
 * 执行器返回对象工具类
 */
public class ProcessResultUtils {

    public static ProcessResult success() {
        return success(StringPool.EMPTY);
    }

    public static ProcessResult success(String msg, Object... params) {
        return new ProcessResult(true, StringUtils.format(msg, params));
    }

    public static ProcessResult failed() {
        return failed(StringPool.EMPTY);
    }

    public static ProcessResult failed(String msg, Object... params) {
        return new ProcessResult(false, StringUtils.format(msg, params));
    }

    public static ProcessResult common(boolean flag, String msg) {
        return new ProcessResult(flag, msg);
    }
}
