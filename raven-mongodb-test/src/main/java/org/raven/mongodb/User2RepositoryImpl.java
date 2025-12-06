package org.raven.mongodb;

import org.raven.mongodb.config.MongoSessionConstant;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class User2RepositoryImpl extends MongoRepositoryImpl<User2, Long> {

    public User2RepositoryImpl(@Qualifier(MongoSessionConstant.MongoSession) MongoSession mongoSession) {
        super(mongoSession, "User");

    }
}