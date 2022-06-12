package com.styeeqan.server.common.http.request;

import com.styeeqan.server.common.error.GameServerError;
import com.styeeqan.server.common.exception.GameServerException;

/**
 * @author yeeq
 * @date 2022/5/29
 */
public abstract class AbstractHttpRequestParam {

    protected GameServerError error;

    protected abstract void haveError();

    public void checkParam() {
        if (error != null) {
            throw new GameServerException.Builder(error).message("异常类:{}", this.getClass().getName()).build();
        }
    }
}
