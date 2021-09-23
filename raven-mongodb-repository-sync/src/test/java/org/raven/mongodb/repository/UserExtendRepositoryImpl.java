package org.raven.mongodb.repository;

/**
 * @author by yanfeng
 * date 2021/9/23 21:05
 */
public class UserExtendRepositoryImpl extends MongoRepositoryImpl<UserExtend, Long> {
    public UserExtendRepositoryImpl() {
        super(MongoSessionInstance.mongoSession);

    }
}
