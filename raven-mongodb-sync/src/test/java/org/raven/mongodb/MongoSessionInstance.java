package org.raven.mongodb;

import com.mongodb.WriteConcern;

public class MongoSessionInstance {

    private static final String url = "mongodb://127.0.0.1:27017/?minPoolSize=10&replicaSet=rs0";   //replicaSet=rs0

    public static final MongoSession mongoSession = new DefaultMongoSession(url
            , "RepositoryTest", null, null);

    public static final MongoSession mongoSession2 = new DefaultMongoSession(url
            , "RepositoryTest", WriteConcern.ACKNOWLEDGED, null);

    public static final MongoSession mongoSession3 = new DefaultMongoSession(url
            , "RepositoryTest", null, null);
}
