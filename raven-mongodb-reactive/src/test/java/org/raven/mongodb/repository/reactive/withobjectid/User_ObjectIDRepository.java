package org.raven.mongodb.repository.reactive.withobjectid;

import org.bson.types.ObjectId;

import org.raven.mongodb.repository.model.User_ObjectID;
import org.raven.mongodb.repository.reactive.MongoSessionInstance;
import org.raven.mongodb.repository.reactive.ReactiveMongoRepositoryImpl;

public class User_ObjectIDRepository extends ReactiveMongoRepositoryImpl<User_ObjectID, ObjectId> {
    public User_ObjectIDRepository() {
        super(MongoSessionInstance.mongoSession);
    }
}
