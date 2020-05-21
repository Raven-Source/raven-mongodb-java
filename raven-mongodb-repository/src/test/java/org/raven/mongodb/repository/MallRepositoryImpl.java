package org.raven.mongodb.repository;

import com.mongodb.WriteConcern;

public class MallRepositoryImpl extends MongoRepositoryImpl<Mall, String> {
    public MallRepositoryImpl() {
        super("mongodb://127.0.0.1:27017/"
            , "RepositoryTest2", null, WriteConcern.ACKNOWLEDGED, null, null);

    }

    public static class MallRepositoryImpl2 extends MongoRepositoryImpl<Mall, String> {
        public MallRepositoryImpl2() {
            super("mongodb://127.0.0.1:27017/"
                , "RepositoryTest2", null, WriteConcern.ACKNOWLEDGED, null, null);

        }
    }

}

