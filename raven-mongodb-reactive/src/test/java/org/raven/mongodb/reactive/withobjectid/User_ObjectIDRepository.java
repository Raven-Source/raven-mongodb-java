package org.raven.mongodb.reactive.withobjectid;

import org.bson.types.ObjectId;

import org.raven.mongodb.reactive.ReactiveMongoRepositoryImpl;
import org.raven.mongodb.model.User_ObjectID;
import org.raven.mongodb.reactive.MongoSessionInstance;

public class User_ObjectIDRepository extends ReactiveMongoRepositoryImpl<User_ObjectID, ObjectId> {
    public User_ObjectIDRepository() {
        super(MongoSessionInstance.mongoSession);
    }
}
