package org.raven.mongodb.repository.withobjectid;

import org.bson.types.ObjectId;
import org.raven.mongodb.repository.MongoRepositoryImpl;
import org.raven.mongodb.repository.MongoSessionInstance;
import org.raven.mongodb.repository.model.User_ObjectID;

public class User_ObjectIDRepository extends MongoRepositoryImpl<User_ObjectID, ObjectId> {
    public User_ObjectIDRepository() {
        super(MongoSessionInstance.mongoSession);
    }
}
