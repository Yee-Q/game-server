package com.styeeqan.server.common.http.response;

import com.styeeqan.server.common.error.GameServerError;
import lombok.Data;

/**
 * @author yeeq
 * @date 2022/5/25
 */
@Data
public class ResponseEntity<T> {

    /**
     * 返回的消息码,如果消息正常返回,code==0,否则返回错误码
     */
    private int code;

    /**
     * 服务器返回的具体消息内容
     */
    private T data;

    /**
     * 当 code!= 0 时,表示错误的详细信息
     */
    private String errorMsg;

    private ResponseEntity() {

    }

    public static <T> ResponseEntity<T> build(T data, GameServerError error) {
        ResponseEntity<T> response = new ResponseEntity<>();
        response.setData(data);
        if (error != null && error.getCode() != 0) {
            response.setCode(error.getCode());
            response.setErrorMsg(error.getDesc());
        }
        return response;
    }
}
