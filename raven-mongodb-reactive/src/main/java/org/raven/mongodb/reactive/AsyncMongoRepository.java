//package org.raven.mongodb.repository.reactive;
//
//import com.mongodb.client.result.DeleteResult;
//import com.mongodb.client.result.InsertManyResult;
//import com.mongodb.client.result.InsertOneResult;
//import com.mongodb.client.result.UpdateResult;
//import org.raven.commons.data.Entity;
//import org.raven.mongodb.repository.operation.ModifyOperation;
//
//import java.util.concurrent.CompletableFuture;
//
///**
// * @param <TEntity> TEntity
// * @param <TKey>    TKey
// * @author yi.liang
// */
//public interface AsyncMongoRepository<TEntity extends Entity<TKey>, TKey>
//        extends AsyncMongoReadonlyRepository<TEntity, TKey>
//        , ModifyOperation<TEntity, TKey, CompletableFuture<InsertOneResult>, CompletableFuture<InsertManyResult>, CompletableFuture<UpdateResult>, CompletableFuture<TEntity>, CompletableFuture<DeleteResult>> {
//}
