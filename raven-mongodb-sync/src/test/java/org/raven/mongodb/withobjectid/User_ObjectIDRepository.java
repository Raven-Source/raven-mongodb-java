package org.raven.mongodb.withobjectid;

import org.bson.types.ObjectId;
import org.raven.mongodb.MongoRepositoryImpl;
import org.raven.mongodb.MongoSessionInstance;
import org.raven.mongodb.model.User_ObjectID;

public class User_ObjectIDRepository extends MongoRepositoryImpl<User_ObjectID, ObjectId> {
    public User_ObjectIDRepository() {
        super(MongoSessionInstance.mongoSession);
    }
}
