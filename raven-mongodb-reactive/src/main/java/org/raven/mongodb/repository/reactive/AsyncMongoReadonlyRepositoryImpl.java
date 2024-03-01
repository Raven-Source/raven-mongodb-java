//package org.raven.mongodb.repository.reactive;
//
//import com.mongodb.client.model.Filters;
//import com.mongodb.reactivestreams.client.MongoDatabase;
//import org.bson.BsonDocument;
//import org.bson.conversions.Bson;
//import org.raven.commons.data.Entity;
//import org.raven.mongodb.repository.*;
//import org.raven.mongodb.repository.annotations.PreFind;
//import org.raven.mongodb.repository.contants.BsonConstant;
//import org.raven.mongodb.repository.spi.IdGeneratorProvider;
//import org.raven.mongodb.repository.spi.ReactiveIdGenerator;
//import org.raven.mongodb.repository.spi.Sequence;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.concurrent.CompletableFuture;
//
///**
// * 只读数据仓储
// *
// * @param <TEntity> TEntity
// * @param <TKey>    TKey
// * @author yi.liang
// */
//public class AsyncMongoReadonlyRepositoryImpl<TEntity extends Entity<TKey>, TKey>
//        extends AbstractAsyncMongoBaseRepository<TEntity, TKey>
//        implements AsyncMongoReadonlyRepository<TEntity, TKey> {
//
//    //#region constructor
//
//    /**
//     * constructor
//     *
//     * @param mongoSession        ReactiveMongoSession
//     * @param collectionName      collectionName
//     * @param sequence            Sequence
//     * @param idGeneratorProvider IdGeneratorProvider
//     */
//    public AsyncMongoReadonlyRepositoryImpl(final ReactiveMongoSession mongoSession, final String collectionName, final Sequence sequence
//            , final IdGeneratorProvider<ReactiveIdGenerator<TKey>, MongoDatabase> idGeneratorProvider) {
//        super(mongoSession, collectionName, sequence, idGeneratorProvider);
//    }
//
//    /**
//     * constructor
//     *
//     * @param mongoSession ReactiveMongoSession
//     */
//    public AsyncMongoReadonlyRepositoryImpl(final ReactiveMongoSession mongoSession) {
//        super(mongoSession);
//    }
//
//    /**
//     * constructor
//     *
//     * @param mongoSession   ReactiveMongoSession
//     * @param collectionName collectionName
//     */
//    public AsyncMongoReadonlyRepositoryImpl(final ReactiveMongoSession mongoSession, final String collectionName) {
//        super(mongoSession, collectionName);
//    }
//
//    /**
//     * constructor
//     *
//     * @param mongoSession ReactiveMongoSession
//     * @param mongoOptions MongoOptions
//     */
//    public AsyncMongoReadonlyRepositoryImpl(final ReactiveMongoSession mongoSession, final MongoOptions mongoOptions) {
//        super(mongoSession, mongoOptions);
//    }
//
//
//    /**
//     * constructor
//     *
//     * @param mongoSession   ReactiveMongoSession
//     * @param mongoOptions   MongoOptions
//     * @param collectionName collectionName
//     */
//    public AsyncMongoReadonlyRepositoryImpl(final ReactiveMongoSession mongoSession, final MongoOptions mongoOptions, final String collectionName) {
//        super(mongoSession, mongoOptions, collectionName);
//    }
//
//    //#endregion
//
//    //region protected
//
//
//    protected CompletableFuture<TEntity> doFindOne(final FindOptions options) {
//        return Mono.from(
//                this.doFind(options).first()
//        ).toFuture();
//    }
//
//    protected CompletableFuture<List<TEntity>> doFindList(final FindOptions options) {
//        return Flux.from(
//                this.doFind(options)
//        ).collectList().toFuture();
//    }
//
//    protected CompletableFuture<Long> doCount(final CountOptions options) {
//
//        if (options.filter() == null) {
//            options.filter(Filters.empty());
//        }
//
//        callGlobalInterceptors(PreFind.class, null, options);
//
//        return Mono.from(
//                super.getCollection(options.readPreference()).countDocuments(options.filter(),
//                        new com.mongodb.client.model.CountOptions()
//                                .hint(options.hint())
//                                .limit(options.limit())
//                                .skip(options.skip())
//                )
//        ).toFuture();
//    }
//
//    protected CompletableFuture<Boolean> doExists(final ExistsOptions options) {
//
//        Bson _filter = options.filter();
//        if (_filter == null) {
//            _filter = new BsonDocument();
//        }
//
//        List<String> includeFields = new ArrayList<>(1);
//        includeFields.add(BsonConstant.PRIMARY_KEY_NAME);
//
//        return this.get(_filter, includeFields, null, options.hint(), options.readPreference()).thenApplyAsync(Objects::nonNull);
//    }
//
//    @Override
//    public FindProxy<TEntity, TKey, CompletableFuture<TEntity>, CompletableFuture<List<TEntity>>, CompletableFuture<Long>, CompletableFuture<Boolean>> findProxy() {
//        return proxy;
//    }
//
//    private final FindProxy<TEntity, TKey, CompletableFuture<TEntity>, CompletableFuture<List<TEntity>>, CompletableFuture<Long>, CompletableFuture<Boolean>> proxy =
//
//            new FindProxy<>() {
//                @Override
//                protected EntityInformation<TEntity, TKey> getEntityInformation() {
//                    return AsyncMongoReadonlyRepositoryImpl.this.entityInformation;
//                }
//
//                @Override
//                protected CompletableFuture<TEntity> doFindOne(FindOptions options) {
//                    return AsyncMongoReadonlyRepositoryImpl.this.doFindOne(options);
//                }
//
//                @Override
//                protected CompletableFuture<List<TEntity>> doFindList(FindOptions options) {
//                    return AsyncMongoReadonlyRepositoryImpl.this.doFindList(options);
//                }
//
//                @Override
//                protected CompletableFuture<Long> doCount(CountOptions options) {
//                    return AsyncMongoReadonlyRepositoryImpl.this.doCount(options);
//                }
//
//                @Override
//                protected CompletableFuture<Boolean> doExists(ExistsOptions options) {
//                    return AsyncMongoReadonlyRepositoryImpl.this.doExists(options);
//                }
//            };
//
//    //endregion
//}
