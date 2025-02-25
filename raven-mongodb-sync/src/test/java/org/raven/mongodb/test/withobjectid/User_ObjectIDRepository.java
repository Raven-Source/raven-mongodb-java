package org.raven.mongodb.test.withobjectid;

import org.bson.types.ObjectId;
import org.raven.mongodb.MongoRepositoryImpl;
import org.raven.mongodb.test.MongoSessionInstance;
import org.raven.mongodb.test.model.User_ObjectID;

public class User_ObjectIDRepository extends MongoRepositoryImpl<User_ObjectID, ObjectId> {
    public User_ObjectIDRepository() {
        super(MongoSessionInstance.mongoSession);
    }
}
