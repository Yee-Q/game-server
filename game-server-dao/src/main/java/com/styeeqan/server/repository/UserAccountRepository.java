package com.styeeqan.server.repository;

import com.styeeqan.server.entity.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author yeeq
 * @date 2022/5/25
 */
public interface UserAccountRepository extends MongoRepository<UserAccount, String> {

}
