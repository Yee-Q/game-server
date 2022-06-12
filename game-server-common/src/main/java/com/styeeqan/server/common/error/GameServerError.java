package com.styeeqan.server.common.error;

/**
 * @author yeeq
 * @date 2022/5/25
 *
 * <p>1.错误码定义规则为5位数<p/>
 * <p>2.前两位表示业务场景,后三位表示错误码,例如:10001,10表示通用,001表示系统未知异常<p/>
 * <p>
 *     3.错误码列表:
 *     <li>10:通用</li>
 *     <li>11:游戏服务中心</li>
 * <p/>
 */
public enum GameServerError {

    /**
     * 游戏服务中心异常码
     */
    UNKNOWN(11001, "游戏服务中心未知异常"),
    OPENID_IS_EMPTY(11002, "openId 为空"),
    OPENID_LEN_ERROR(11003, "openId 长度错误"),
    SDK_TOKEN_ERROR(11004, "SDK token 错误"),
    SDK_TOKEN_LEN_ERROR(11005, "sdk token 长度错误"),
    NICKNAME_EXIST(11006, "昵称已存在"),
    ZONE_ID_IS_EMPTY(11007, "zoneId 为空"),
    NICK_NAME_IS_EMPTY(11008, "昵称为空"),
    NICK_NAME_LEN_ERROR(11009, "昵称长度必须介于2-10");

    private final int code;
    private final String desc;

    GameServerError(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
