package com.nodes.common.exception;

/**
 *
 */
public final class ApiServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     * <p>
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public ApiServiceException() {
    }

    public ApiServiceException(String message) {
        this.message = message;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public ApiServiceException setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ApiServiceException setMessage(String message) {
        this.message = message;
        return this;
    }
}
