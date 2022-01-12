package org.raven.mongodb.repository.reactive;

import com.mongodb.WriteConcern;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoDatabase;
import lombok.NonNull;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.EntityInformation;
import org.raven.mongodb.repository.MongoOptions;
import org.raven.mongodb.repository.UpdateType;
import org.raven.mongodb.repository.annotations.PreInsert;
import org.raven.mongodb.repository.annotations.PreUpdate;
import org.raven.mongodb.repository.contants.BsonConstant;
import org.raven.mongodb.repository.spi.ReactiveIdGenerator;
import org.raven.mongodb.repository.spi.IdGeneratorProvider;
import org.raven.mongodb.repository.spi.Sequence;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 */
public class ReactiveMongoRepositoryImpl<TEntity extends Entity<TKey>, TKey>
        extends ReactiveMongoReaderRepositoryImpl<TEntity, TKey>
        implements ReactiveMongoRepository<TEntity, TKey> {


    //#region constructor

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
    }

    /**
     * constructor
     *
     * @param mongoSession ReactiveMongoSession
     */
    public ReactiveMongoRepositoryImpl(final ReactiveMongoSession mongoSession) {
        super(mongoSession);
    }

    /**
     * constructor
     *
     * @param mongoSession   ReactiveMongoSession
     * @param collectionName collectionName
     */
    public ReactiveMongoRepositoryImpl(final ReactiveMongoSession mongoSession, final String collectionName) {
        super(mongoSession, collectionName);
    }

    /**
     * constructor
     *
     * @param mongoSession ReactiveMongoSession
     * @param mongoOptions MongoOptions
     */
    public ReactiveMongoRepositoryImpl(final ReactiveMongoSession mongoSession, final MongoOptions mongoOptions) {
        super(mongoSession, mongoOptions);
    }

    /**
     * constructor
     *
     * @param mongoSession   ReactiveMongoSession
     * @param mongoOptions   MongoOptions
     * @param collectionName collectionName
     */
    public ReactiveMongoRepositoryImpl(final ReactiveMongoSession mongoSession, final MongoOptions mongoOptions, final String collectionName) {
        super(mongoSession, mongoOptions, collectionName);
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
    public Mono<UpdateResult> updateOne(final Bson filter, final TEntity updateEntity, final boolean isUpsert, final Bson hint, final WriteConcern writeConcern) {

        return createUpdateBson(updateEntity, isUpsert).flatMap(update ->
                this.updateOne(filter, update, isUpsert, hint, writeConcern)
        );

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

        return createUpdateBson(entity, isUpsert).flatMap(update ->
                this.findOneAndUpdate(filter, update, isUpsert, sort, hint)
        );

    }

    //#endregion

    //region protected

    protected Mono<InsertOneResult> doInsert(final TEntity entity, final WriteConcern writeConcern) {

        Mono<TEntity> mono = Mono.just(entity);
        if (entity.getId() == null) {

            mono = idGenerator.generateId().map(x -> {
                entity.setId(x);
                return entity;

            });

        }

        return mono.flatMap((e) -> {
            callGlobalInterceptors(PreInsert.class, e, null);
            return Mono.just(e);
        }).flatMap((e) -> Mono.from(
                super.getCollection(writeConcern).insertOne(e)
        ));
    }

    protected Mono<InsertManyResult> doInsertBatch(final List<TEntity> entities, final WriteConcern writeConcern) {

        Mono<List<TEntity>> mono = Mono.just(entities);

        List<TEntity> entityStream = entities.stream().filter(x -> x.getId() == null).collect(Collectors.toList());
        long count = entityStream.size();

        if (count > 0) {

            mono = idGenerator.generateIdBatch(count).map(ids -> {

                for (int i = 0; i < count; i++) {
                    entityStream.get(i).setId(ids.get(i));
                }

                return entities;
            });

        }

        return mono.flatMap((es) -> {
            for (TEntity entity : es) {
                callGlobalInterceptors(PreInsert.class, entity, null);
            }
            return Mono.just(es);
        }).flatMap(es -> Mono.from(
                super.getCollection(writeConcern).insertMany(es)
        ));
    }

    protected Mono<UpdateResult> doUpdate(@NonNull final org.raven.mongodb.repository.UpdateOptions options,
                                          final UpdateType updateType) {

        if (options.filter() == null) {
            options.filter(Filters.empty());
        }

        callGlobalInterceptors(PreUpdate.class, null, options);

        if (updateType == UpdateType.ONE) {
            return Mono.from(
                    super.getCollection(options.writeConcern()).updateOne(options.filter(), options.update(),
                            new com.mongodb.client.model.UpdateOptions()
                                    .hint(options.hint())
                                    .upsert(options.upsert())

                    )
            );
        } else {
            return Mono.from(
                    super.getCollection(options.writeConcern()).updateMany(options.filter(), options.update(),
                            new com.mongodb.client.model.UpdateOptions()
                                    .hint(options.hint())
                                    .upsert(options.upsert())

                    )
            );
        }
    }

    protected Mono<TEntity> doFindOneAndUpdate(final org.raven.mongodb.repository.FindOneAndUpdateOptions options) {

        if (options.filter() == null) {
            options.filter(Filters.empty());
        }

        callGlobalInterceptors(PreUpdate.class, null, options);

        return Mono.from(
                super.getCollection().findOneAndUpdate(options.filter(), options.update(),
                        new com.mongodb.client.model.FindOneAndUpdateOptions()
                                .returnDocument(options.returnDocument())
                                .upsert(options.upsert())
                                .hint(options.hint())
                                .sort(options.sort())
                )
        );
    }

    protected Mono<TEntity> doFindOneAndDelete(@NonNull final org.raven.mongodb.repository.FindOneAndDeleteOptions options) {

        if (options.filter() == null) {
            options.filter(Filters.empty());
        }

        return Mono.from(
                super.getCollection().findOneAndDelete(options.filter(),
                        new com.mongodb.client.model.FindOneAndDeleteOptions()
                                .hint(options.hint())
                                .sort(options.sort())
                )
        );
    }

    protected Mono<DeleteResult> doDeleteOne(final org.raven.mongodb.repository.DeleteOptions options) {
        return Mono.from(
                super.getCollection(options.writeConcern()).deleteOne(options.filter(),
                        new com.mongodb.client.model.DeleteOptions()
                                .hint(options.hint())
                )
        );
    }

    protected Mono<DeleteResult> doDeleteMany(final org.raven.mongodb.repository.DeleteOptions options) {
        return Mono.from(
                super.getCollection(options.writeConcern()).deleteMany(options.filter(),
                        new com.mongodb.client.model.DeleteOptions()
                                .hint(options.hint())
                )
        );
    }

    /**
     * @param updateEntity Entity
     * @param isUpsert     default false
     * @return Update Bson
     */
    protected Mono<Bson> createUpdateBson(final TEntity updateEntity, final boolean isUpsert) {

        BsonDocument bsDoc = entityInformation.toBsonDocument(updateEntity);
        bsDoc.remove(BsonConstant.PRIMARY_KEY_NAME);

        Bson update = new BsonDocument("$set", bsDoc);
        if (isUpsert) {
            return idGenerator.generateId().map(id ->
                    Updates.combine(update, Updates.setOnInsert(BsonConstant.PRIMARY_KEY_NAME, id))
            );
        } else {
            return Mono.just(update);
        }

    }

    //endregion

    @Override
    public ModifyProxy<TEntity, TKey, Mono<InsertOneResult>, Mono<InsertManyResult>, Mono<UpdateResult>, Mono<TEntity>, Mono<DeleteResult>> modifyProxy() {
        return new ModifyProxy<>() {
            @Override
            protected EntityInformation<TEntity, TKey> getEntityInformation() {
                return entityInformation;
            }

            @Override
            protected Mono<InsertOneResult> doInsert(TEntity entity, WriteConcern writeConcern) {
                return ReactiveMongoRepositoryImpl.this.doInsert(entity, writeConcern);
            }

            @Override
            protected Mono<InsertManyResult> doInsertBatch(List<TEntity> entities, WriteConcern writeConcern) {
                return ReactiveMongoRepositoryImpl.this.doInsertBatch(entities, writeConcern);
            }

            @Override
            protected Mono<UpdateResult> doUpdate(@NonNull org.raven.mongodb.repository.UpdateOptions options, UpdateType updateType) {
                return ReactiveMongoRepositoryImpl.this.doUpdate(options, updateType);
            }

            @Override
            protected Mono<TEntity> doFindOneAndUpdate(org.raven.mongodb.repository.FindOneAndUpdateOptions options) {
                return ReactiveMongoRepositoryImpl.this.doFindOneAndUpdate(options);
            }

            @Override
            protected Mono<TEntity> doFindOneAndDelete(@NonNull org.raven.mongodb.repository.FindOneAndDeleteOptions options) {
                return ReactiveMongoRepositoryImpl.this.doFindOneAndDelete(options);
            }

            @Override
            protected Mono<DeleteResult> doDeleteOne(org.raven.mongodb.repository.DeleteOptions options) {
                return ReactiveMongoRepositoryImpl.this.doDeleteOne(options);
            }

            @Override
            protected Mono<DeleteResult> doDeleteMany(org.raven.mongodb.repository.DeleteOptions options) {
                return ReactiveMongoRepositoryImpl.this.doDeleteMany(options);
            }

        };
    }

}
