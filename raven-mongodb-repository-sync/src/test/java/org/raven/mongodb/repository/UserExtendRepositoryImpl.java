package org.raven.mongodb.repository;

import org.raven.mongodb.repository.model.UserExtend;

/**
 * @author by yanfeng
 * date 2021/9/23 21:05
 */
public class UserExtendRepositoryImpl extends MongoRepositoryImpl<UserExtend, Long> {
    public UserExtendRepositoryImpl() {
        super(MongoSessionInstance.mongoSession);

    }
}
