package org.raven.mongodb.repository;

import org.raven.mongodb.repository.model.User;

public class UserRepositoryImpl extends MongoRepositoryImpl<User, Long> {
    public UserRepositoryImpl() {
        super(MongoSessionInstance.mongoSession);

    }

    public EntityInformation<User, Long> getEntityInformation() {
        return super.entityInformation;
    }
}
