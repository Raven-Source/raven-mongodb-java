package org.raven.mongodb.repository.reactive;

import com.mongodb.WriteConcern;

/**
 * @author by yanfeng
 * date 2021/10/31 22:24
 */
public class MongoSessionInstance {

    public static final ReactiveMongoSession mongoSession = new DefaultReactiveMongoSession("mongodb://127.0.0.1:27017/"
            , "RepositoryTest", null, null);

    public static final ReactiveMongoSession mongoSession2 = new DefaultReactiveMongoSession("mongodb://127.0.0.1:27017/"
            , "RepositoryTest", WriteConcern.ACKNOWLEDGED, null);

    public static final ReactiveMongoSession mongoSession3 = new DefaultReactiveMongoSession("mongodb://127.0.0.1:27017/"
            , "RepositoryTest", null, null);
}

