package com.styeeqan.server.center.controller;

import com.styeeqan.server.common.util.JwtUtil;
import com.styeeqan.server.center.service.PlayerSev;
import com.styeeqan.server.center.service.UserLoginSev;
import com.styeeqan.server.common.constant.CommonField;
import com.styeeqan.server.entity.Player;
import com.styeeqan.server.entity.UserAccount;
import com.styeeqan.server.common.error.GameServerError;
import com.styeeqan.server.common.http.request.CreatePlayerParam;
import com.styeeqan.server.common.http.request.LoginParam;
import com.styeeqan.server.common.http.response.LoginResult;
import com.styeeqan.server.common.http.response.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author yeeq
 * @date 2022/5/29
 */
@Slf4j
@RestController
@RequestMapping("user")
public class UserCon {

    @Autowired
    private UserLoginSev userLoginSev;

    @Autowired
    private PlayerSev playerSev;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("login")
    public ResponseEntity<LoginResult> login(@RequestBody LoginParam loginParam) {
        // 检查请求参数是否合法
        loginParam.checkParam();
        // 检测第三方 SDK 是否合法
        // ...
        // 执行登录操作
        log.info("开始执行登录操作");
        UserAccount userAccount = userLoginSev.login(loginParam);
        LoginResult loginResult = new LoginResult();
        loginResult.setUserId(userAccount.getUserId());
        // 使用 JWT 生成 token
        Map<String, String> payloadMap = new HashMap<>(2);
        payloadMap.put(CommonField.OPEN_ID, loginParam.getOpenId());
        payloadMap.put(CommonField.USER_ID, String.valueOf(userAccount.getUserId()));
        loginResult.setToken(jwtUtil.getToken(payloadMap));
        return ResponseEntity.build(loginResult, null);
    }

    @PostMapping("create/player")
    public ResponseEntity<UserAccount.ZonePlayerInfo> createPlayer(@RequestBody CreatePlayerParam param,
                                                                   HttpServletRequest request) {
        // 检查请求参数是否合法
        param.checkParam();
        // 获取 openId
        String openId = request.getHeader(CommonField.OPEN_ID);

        Optional<UserAccount> op = userLoginSev.getUserAccountByOpenId(openId);
        if (op.isEmpty()) {
            return ResponseEntity.build(null, GameServerError.UNKNOWN);
        }
        UserAccount userAccount = op.get();
        String zoneId = param.getZoneId();
        UserAccount.ZonePlayerInfo zoneInfo = userAccount.getPlayers().get(zoneId);
        // 如果没有角色就创建角色
        if (zoneInfo == null) {
            Player player = playerSev.createPlayer(param.getZoneId(), param.getNickName());
            zoneInfo = new UserAccount.ZonePlayerInfo(player.getPlayerId(), System.currentTimeMillis());
            userAccount.getPlayers().put(zoneId, zoneInfo);
            userLoginSev.updateUserAccount(userAccount);
        }
        return ResponseEntity.build(zoneInfo, null);
    }
}
