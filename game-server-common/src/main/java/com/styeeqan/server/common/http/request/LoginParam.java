package com.styeeqan.server.common.http.request;

import com.styeeqan.server.common.constant.CommonField;
import com.styeeqan.server.common.error.GameServerError;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

/**
 * @author yeeq
 * @date 2022/5/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginParam extends AbstractHttpRequestParam {

    private String openId;
    private String token;
    private String ip;

    @Override
    protected void haveError() {
        // 验证登陆参数
        if (StringUtils.isEmpty(openId)) {
            this.error = GameServerError.OPENID_IS_EMPTY;
        } else if (openId.length() > CommonField.OPEN_ID_LENGTH) {
            this.error = GameServerError.OPENID_LEN_ERROR;
        } else if (StringUtils.isEmpty(token)) {
            this.error = GameServerError.SDK_TOKEN_ERROR;
        } else if (token.length() > CommonField.TOKEN_LEN) {
            this.error = GameServerError.SDK_TOKEN_LEN_ERROR;
        }
    }
}
