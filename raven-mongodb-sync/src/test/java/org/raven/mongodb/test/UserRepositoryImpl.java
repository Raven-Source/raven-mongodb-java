package org.raven.mongodb.test;

import org.raven.mongodb.EntityInformation;
import org.raven.mongodb.MongoRepositoryImpl;
import org.raven.mongodb.test.model.User;

public class UserRepositoryImpl extends MongoRepositoryImpl<User, Long> {
    public UserRepositoryImpl() {
        super(MongoSessionInstance.mongoSession);

    }

    public EntityInformation<User, Long> getEntityInformation() {
        return super.entityInformation;
    }
}
