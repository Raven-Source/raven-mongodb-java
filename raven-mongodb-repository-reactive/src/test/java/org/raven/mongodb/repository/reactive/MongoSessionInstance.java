package org.raven.mongodb.repository.reactive;

import com.mongodb.WriteConcern;

public class MongoSessionInstance {

    public static final ReactiveMongoSession mongoSession = new DefaultReactiveMongoSession("mongodb://127.0.0.1:27017/"
            , "RepositoryTest", null, null);

    public static final ReactiveMongoSession mongoSession2 = new DefaultReactiveMongoSession("mongodb://127.0.0.1:27017/"
            , "RepositoryTest", WriteConcern.ACKNOWLEDGED, null);

    public static final ReactiveMongoSession mongoSession3 = new DefaultReactiveMongoSession("mongodb://127.0.0.1:27017/"
            , "RepositoryTest", null, null);
}
