package com.styeeqan.server.gateway.error;

/**
 * @author yeeq
 * @date 2022/6/5
 */
public enum WebGatewayError {

    UNKNOWN(-1, "网关服务器未知道异常");

    private final int errorCode;
    private final String errorDesc;

    private WebGatewayError(int errorCode, String errorDesc) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getErrorDesc() {
        return this.errorDesc;
    }
}
