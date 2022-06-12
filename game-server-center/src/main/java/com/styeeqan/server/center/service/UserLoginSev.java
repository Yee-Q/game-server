package com.styeeqan.server.center.service;

import com.styeeqan.server.dao.UserAccountDao;
import com.styeeqan.server.entity.UserAccount;
import com.styeeqan.server.common.http.request.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author yeeq
 * @date 2022/5/29
 */
@Service
public class UserLoginSev {

    @Autowired
    private UserAccountDao userAccountDao;

    public Optional<UserAccount> getUserAccountByOpenId(String openId) {
        return this.userAccountDao.findById(openId);
    }

    public void updateUserAccount(UserAccount userAccount) {
        this.userAccountDao.save(userAccount, userAccount.getOpenId());
    }

    public UserAccount login(LoginParam loginParam) {
        String openId = loginParam.getOpenId();
        // 将 openId 放入常量池
        openId = openId.intern();
        // 对 openId 加锁,防止用户注册多次
        synchronized (openId) {
            Optional<UserAccount> op = userAccountDao.findById(openId);
            UserAccount userAccount = null;
            // 用户不存在,继续注册
            userAccount = op.orElseGet(() -> this.register(loginParam));
            return userAccount;
        }
    }

    private UserAccount register(LoginParam loginParam) {
        Long userId = userAccountDao.getNextUserId();
        // 用 Redis 自增保证 userId 全局唯一
        UserAccount userAccount = new UserAccount();
        userAccount.setOpenId(loginParam.getOpenId());
        userAccount.setCreateTime(System.currentTimeMillis());
        userAccount.setUserId(userId);
        userAccountDao.save(userAccount, userAccount.getOpenId());
        return userAccount;
    }
}
