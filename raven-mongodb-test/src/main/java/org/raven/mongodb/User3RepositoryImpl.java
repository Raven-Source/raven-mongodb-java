package org.raven.mongodb;

import org.raven.mongodb.config.MongoSessionConstant;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class User3RepositoryImpl extends MongoRepositoryImpl<User3, Long> {
    public User3RepositoryImpl(@Qualifier(MongoSessionConstant.MongoSession) MongoSession mongoSession) {
        super(mongoSession);

    }
}
