//package org.raven.mongodb.repository.reactive;
//
//import org.raven.mongodb.repository.operation.FindOperation;
//
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
///**
// * @param <TEntity> TEntity
// * @param <TKey>    TKey
// * @author yi.liang
// */
//public interface AsyncMongoReadonlyRepository<TEntity, TKey>
//        extends ReactiveMongoBaseRepository<TEntity>,
//        FindOperation<TEntity, TKey, CompletableFuture<TEntity>, CompletableFuture<List<TEntity>>, CompletableFuture<Long>, CompletableFuture<Boolean>> {
//}
