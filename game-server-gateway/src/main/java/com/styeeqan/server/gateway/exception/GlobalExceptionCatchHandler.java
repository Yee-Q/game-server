package com.styeeqan.server.gateway.exception;

import com.styeeqan.server.gateway.error.WebGatewayError;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 网关全局web错误异常捕获
 *
 * @author yeeq
 * @date 2022/6/5
 */
@Slf4j
public class GlobalExceptionCatchHandler extends DefaultErrorWebExceptionHandler {

    public GlobalExceptionCatchHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        // 获取捕获的异常信息
        Throwable error = super.getError(request);
        HashMap<String, Object> result = new HashMap<>();
        result.put("code", WebGatewayError.UNKNOWN.getErrorCode());
        result.put("data", WebGatewayError.UNKNOWN.getErrorDesc() + "," + error.getMessage());
        log.error("{}", WebGatewayError.UNKNOWN, error);
        return result;
    }

    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        return HttpStatus.SC_OK;
    }

    @Override
    protected RequestPredicate acceptsTextHtml() {
        return c -> false;
    }
}
