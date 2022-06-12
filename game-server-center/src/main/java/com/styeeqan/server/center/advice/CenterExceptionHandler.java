package com.styeeqan.server.center.advice;

import com.alibaba.fastjson.JSONObject;
import com.styeeqan.server.common.error.GameServerError;
import com.styeeqan.server.common.exception.GameServerException;
import com.styeeqan.server.common.http.response.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yeeq
 * @date 2022/5/29
 */
@Slf4j
@ControllerAdvice
public class CenterExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<JSONObject> exceptionHandler(Throwable ex) {
        GameServerError error = null;
        if (ex instanceof GameServerException) {
            GameServerException gameServerException = (GameServerException) ex;
            error = gameServerException.getError();
            log.error("服务器异常:{}", ex.getMessage());
        } else {
            error = GameServerError.UNKNOWN;
            log.error("服务器异常", ex);
        }
        JSONObject data = new JSONObject();
        data.put("errorMsg", ex.getMessage());
        return ResponseEntity.build(data, error);
    }
}
