package org.raven.mongodb.repository;

import com.mongodb.WriteConcern;

public class MongoSessionInstance {

    public static final MongoSession mongoSession = new DefaultMongoSession("mongodb://127.0.0.1:27017/"
            , "RepositoryTest", null, null);

    public static final MongoSession mongoSession2 = new DefaultMongoSession("mongodb://127.0.0.1:27017/"
            , "RepositoryTest", WriteConcern.ACKNOWLEDGED, null);

    public static final MongoSession mongoSession3 = new DefaultMongoSession("mongodb://127.0.0.1:27017/"
            , "RepositoryTest", null, null);
}
