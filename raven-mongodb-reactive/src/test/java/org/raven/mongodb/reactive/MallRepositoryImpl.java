package org.raven.mongodb.reactive;

import org.raven.mongodb.test.model.Mall;

public class MallRepositoryImpl extends ReactiveMongoRepositoryImpl<Mall, String> {
    public MallRepositoryImpl() {
        super(MongoSessionInstance.mongoSession);

    }

    public static class MallRepositoryImpl2 extends ReactiveMongoRepositoryImpl<Mall, String> {
        public MallRepositoryImpl2() {
            super(MongoSessionInstance.mongoSession);

        }
    }

}

