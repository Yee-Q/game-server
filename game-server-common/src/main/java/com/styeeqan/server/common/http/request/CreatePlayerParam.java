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
public class CreatePlayerParam extends AbstractHttpRequestParam {

    /**
     * 分区id
     */
    private String zoneId;
    private String nickName;

    @Override
    protected void haveError() {
        // 验证登陆参数
        if (StringUtils.isEmpty(zoneId)) {
            this.error = GameServerError.ZONE_ID_IS_EMPTY;
        } else if (StringUtils.isEmpty(nickName)) {
            this.error = GameServerError.NICK_NAME_IS_EMPTY;
        } else if (nickName.length() < CommonField.NICK_NAME_MIN_LEN
                        || nickName.length() > CommonField.NICK_NAME_MAX_LEN) {
            this.error = GameServerError.NICK_NAME_LEN_ERROR;
        }
    }
}
