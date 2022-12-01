package org.raven.mongodb.repository;

import com.mongodb.WriteConcern;

public class MongoSessionInstance {

    private static final String url = "mongodb://127.0.0.1:27017/";

    public static final MongoSession mongoSession = new DefaultMongoSession(url
            , "RepositoryTest", null, null);

    public static final MongoSession mongoSession2 = new DefaultMongoSession(url
            , "RepositoryTest", WriteConcern.ACKNOWLEDGED, null);

    public static final MongoSession mongoSession3 = new DefaultMongoSession(url
            , "RepositoryTest", null, null);
}
