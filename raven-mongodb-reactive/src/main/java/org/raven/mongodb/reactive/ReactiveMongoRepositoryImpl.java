package org.raven.mongodb.reactive;

import com.mongodb.WriteConcern;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.MongoOptions;
//import org.raven.mongodb.repository.spi.IdGenerationType;
import org.raven.mongodb.spi.ReactiveIdGenerator;
import org.raven.mongodb.spi.IdGeneratorProvider;
import org.raven.mongodb.spi.Sequence;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

/**
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 */
public class ReactiveMongoRepositoryImpl<TEntity extends Entity<TKey>, TKey>
        extends ReactiveMongoReadonlyRepositoryImpl<TEntity, TKey>
        implements ReactiveMongoRepository<TEntity, TKey> {

    private final ReactiveModifyOperationImpl<TEntity, TKey> operation;

    //#region constructor

    /**
     * constructor
     *
     * @param mongoSession ReactiveMongoSession
     */
    public ReactiveMongoRepositoryImpl(final ReactiveMongoSession mongoSession) {
        this(mongoSession, null, null, null);
    }

    /**
     * constructor
     *
     * @param mongoSession   ReactiveMongoSession
     * @param collectionName collectionName
     */
    public ReactiveMongoRepositoryImpl(final ReactiveMongoSession mongoSession, final String collectionName) {
        this(mongoSession, collectionName, null, null);
    }

    /**
     * constructor
     *
     * @param mongoSession ReactiveMongoSession
     * @param mongoOptions MongoOptions
     */
    public ReactiveMongoRepositoryImpl(final ReactiveMongoSession mongoSession, final MongoOptions mongoOptions) {
        this(mongoSession, null, mongoOptions.getSequence(), mongoOptions.getIdGeneratorProvider());
    }

    /**
     * constructor
     *
     * @param mongoSession   ReactiveMongoSession
     * @param mongoOptions   MongoOptions
     * @param collectionName collectionName
     */
    public ReactiveMongoRepositoryImpl(final ReactiveMongoSession mongoSession, final MongoOptions mongoOptions, final String collectionName) {
        this(mongoSession, collectionName, mongoOptions.getSequence(), mongoOptions.getIdGeneratorProvider());
    }

    /**
     * constructor
     *
     * @param mongoSession        ReactiveMongoSession
     * @param collectionName      collectionName
     * @param sequence            Sequence
     * @param idGeneratorProvider IdGeneratorProvider
     */
    public ReactiveMongoRepositoryImpl(final ReactiveMongoSession mongoSession, final String collectionName, final Sequence sequence
            , final IdGeneratorProvider<ReactiveIdGenerator<TKey>, MongoDatabase> idGeneratorProvider) {

        super(mongoSession, collectionName, sequence, idGeneratorProvider);

        operation = new ReactiveModifyOperationImpl<>(this, null);
    }

    //#endregion

    //#region update

    /**
     * 修改单条数据
     *
     * @param filter       filter
     * @param updateEntity entity
     * @param isUpsert     default false
     * @param hint         hint
     * @param writeConcern {{@link WriteConcern}}
     * @return UpdateResult
     */
    @Override
    public Mono<Long> updateOne(final Bson filter, final TEntity updateEntity, final boolean isUpsert, final Bson hint, final WriteConcern writeConcern) {

        return operation.updateOne(filter, updateEntity, isUpsert, hint, writeConcern);

    }

    //#endregion

    //#region findAndModify

    /**
     * 找到并更新
     *
     * @param filter   filter
     * @param entity   entity
     * @param isUpsert default false
     * @param sort     sort
     * @return Entity
     */
    @Override
    public Mono<TEntity> findOneAndUpdate(final Bson filter, final TEntity entity, final boolean isUpsert, final Bson sort, final Bson hint) {

        return operation.findOneAndUpdate(filter, entity, isUpsert, sort, hint);

    }

    //#endregion

    //region protected

//    protected Mono<InsertOneResult> doInsert(final TEntity entity, final WriteConcern writeConcern) {
//
//        Mono<TEntity> mono = Mono.just(entity);
//        if (entity.getId() == null && idGenerator != null) {
//
//            mono = idGenerator.generateId().map(x -> {
//                entity.setId(x);
//                return entity;
//
//            });
//
//        }
//
//        return mono.flatMap((e) -> {
//            callGlobalInterceptors(PreInsert.class, e, null);
//            return Mono.just(e);
//        }).flatMap((e) -> Mono.from(
//                super.getCollection(writeConcern).insertOne(e)
//        ));
//    }
//
//    protected Mono<InsertManyResult> doInsertBatch(final List<TEntity> entities, final WriteConcern writeConcern) {
//
//        Mono<List<TEntity>> mono = Mono.just(entities);
//
//        List<TEntity> entityStream = entities.stream().filter(x -> x.getId() == null).collect(Collectors.toList());
//        long count = entityStream.size();
//
//        if (count > 0 && idGenerator != null) {
//
//            mono = idGenerator.generateIdBatch(count).map(ids -> {
//
//                for (int i = 0; i < count; i++) {
//                    entityStream.get(i).setId(ids.get(i));
//                }
//
//                return entities;
//            });
//
//        }
//
//        return mono.flatMap((es) -> {
//            for (TEntity entity : es) {
//                callGlobalInterceptors(PreInsert.class, entity, null);
//            }
//            return Mono.just(es);
//        }).flatMap(es -> Mono.from(
//                super.getCollection(writeConcern).insertMany(es)
//        ));
//    }
//
//    protected Mono<UpdateResult> doUpdate(@NonNull final org.raven.mongodb.repository.UpdateOptions options,
//                                          final UpdateType updateType) {
//
//        if (options.filter() == null) {
//            options.filter(Filters.empty());
//        }
//
//        callGlobalInterceptors(PreUpdate.class, null, options);
//
//        if (updateType == UpdateType.ONE) {
//            return Mono.from(
//                    super.getCollection(options.writeConcern()).updateOne(options.filter(), options.update(),
//                            new com.mongodb.client.model.UpdateOptions()
//                                    .hint(options.hint())
//                                    .upsert(options.upsert())
//
//                    )
//            );
//        } else {
//            return Mono.from(
//                    super.getCollection(options.writeConcern()).updateMany(options.filter(), options.update(),
//                            new com.mongodb.client.model.UpdateOptions()
//                                    .hint(options.hint())
//                                    .upsert(options.upsert())
//
//                    )
//            );
//        }
//    }
//
//    protected Mono<TEntity> doFindOneAndUpdate(final org.raven.mongodb.repository.FindOneAndUpdateOptions options) {
//
//        if (options.filter() == null) {
//            options.filter(Filters.empty());
//        }
//
//        callGlobalInterceptors(PreUpdate.class, null, options);
//
//        return Mono.from(
//                super.getCollection().findOneAndUpdate(options.filter(), options.update(),
//                        new com.mongodb.client.model.FindOneAndUpdateOptions()
//                                .returnDocument(options.returnDocument())
//                                .upsert(options.upsert())
//                                .hint(options.hint())
//                                .sort(options.sort())
//                )
//        );
//    }
//
//    protected Mono<TEntity> doFindOneAndDelete(@NonNull final org.raven.mongodb.repository.FindOneAndDeleteOptions options) {
//
//        if (options.filter() == null) {
//            options.filter(Filters.empty());
//        }
//
//        return Mono.from(
//                super.getCollection().findOneAndDelete(options.filter(),
//                        new com.mongodb.client.model.FindOneAndDeleteOptions()
//                                .hint(options.hint())
//                                .sort(options.sort())
//                )
//        );
//    }
//
//    protected Mono<DeleteResult> doDeleteOne(final org.raven.mongodb.repository.DeleteOptions options) {
//        return Mono.from(
//                super.getCollection(options.writeConcern()).deleteOne(options.filter(),
//                        new com.mongodb.client.model.DeleteOptions()
//                                .hint(options.hint())
//                )
//        );
//    }
//
//    protected Mono<DeleteResult> doDeleteMany(final org.raven.mongodb.repository.DeleteOptions options) {
//        return Mono.from(
//                super.getCollection(options.writeConcern()).deleteMany(options.filter(),
//                        new com.mongodb.client.model.DeleteOptions()
//                                .hint(options.hint())
//                )
//        );
//    }
//
//    /**
//     * @param updateEntity Entity
//     * @param isUpsert     default false
//     * @return Update Bson
//     */
//    protected Mono<Bson> createUpdateBson(final TEntity updateEntity, final boolean isUpsert) {
//
//        BsonDocument bsDoc = entityInformation.toBsonDocument(updateEntity);
//        bsDoc.remove(BsonConstant.PRIMARY_KEY_NAME);
//
//        Bson update = new BsonDocument("$set", bsDoc);
//        if (isUpsert && idGenerator != null) {
//            return idGenerator.generateId().map(id ->
//                    Updates.combine(update, Updates.setOnInsert(BsonConstant.PRIMARY_KEY_NAME, id))
//            );
//        } else {
//            return Mono.just(update);
//        }
//
//    }

    //endregion

    @Override
    public ModifyProxy<TEntity, TKey, Mono<Optional<TKey>>, Mono<Map<Integer, TKey>>, Mono<Long>, Mono<TEntity>, Mono<Long>> modifyProxy() {
        return operation.modifyProxy();
    }

}
