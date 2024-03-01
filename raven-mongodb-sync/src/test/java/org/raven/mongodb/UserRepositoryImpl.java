package org.raven.mongodb;

import org.raven.mongodb.model.User;

public class UserRepositoryImpl extends MongoRepositoryImpl<User, Long> {
    public UserRepositoryImpl() {
        super(MongoSessionInstance.mongoSession);

    }

    public EntityInformation<User, Long> getEntityInformation() {
        return super.entityInformation;
    }
}
