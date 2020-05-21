package org.raven.mongodb.repository.objectID;

import org.bson.types.ObjectId;
import org.raven.mongodb.repository.MongoRepositoryImpl;

public class User_ObjectIDRepository extends MongoRepositoryImpl<User_ObjectID, ObjectId> {
    public User_ObjectIDRepository() {
        super("mongodb://127.0.0.1:27017/"
            , "RepositoryTest2", null, null, null, null);
    }
}
