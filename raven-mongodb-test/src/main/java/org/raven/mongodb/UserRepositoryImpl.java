package org.raven.mongodb;

import org.raven.mongodb.config.MongoSessionConstant;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl extends MongoRepositoryImpl<User, Long> {
    public UserRepositoryImpl(@Qualifier(MongoSessionConstant.MongoSession) MongoSession mongoSession) {
        super(mongoSession);

    }
}
