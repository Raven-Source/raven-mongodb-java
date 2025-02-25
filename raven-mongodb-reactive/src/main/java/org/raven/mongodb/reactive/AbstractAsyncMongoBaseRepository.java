package org.raven.mongodb.reactive;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.ClientSession;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import lombok.NonNull;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.*;
import org.raven.mongodb.annotation.PreDelete;
import org.raven.mongodb.annotation.PreFind;
import org.raven.mongodb.annotation.PreInsert;
import org.raven.mongodb.annotation.PreUpdate;
import org.raven.mongodb.contants.BsonConstant;
import org.raven.mongodb.criteria.*;
import org.raven.mongodb.spi.IdGeneratorProvider;
import org.raven.mongodb.spi.ReactiveIdGenerator;
import org.raven.mongodb.spi.Sequence;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 */
@SuppressWarnings({"unchecked"})
public abstract class AbstractAsyncMongoBaseRepository<TEntity extends Entity<TKey>, TKey>
        extends BaseRepository<TEntity, TKey>
        implements ReactiveMongoBaseRepository<TEntity> {

    protected ReactiveIdGenerator<TKey> idGenerator;

    protected ReactiveMongoSession mongoSession;

    protected MongoDatabase mongoDatabase;

    protected EntityInformation<TEntity, TKey> getEntityInformation() {
        return entityInformation;
    }

    /**
     * @return MongoDatabase
     */
    @Override
    public MongoDatabase getDatabase() {

        return mongoSession.getDatabase().withCodecRegistry(entityInformation.getCodecRegistry());
    }

    //#region constructor
    public AbstractAsyncMongoBaseRepository(final ReactiveMongoSession mongoSession, final String collectionName, final Sequence sequence
            , final IdGeneratorProvider<ReactiveIdGenerator<TKey>, MongoDatabase> idGeneratorProvider) {
        super(collectionName);

        this.mongoSession = mongoSession;
        this.mongoDatabase = mongoSession.getDatabase().withCodecRegistry(entityInformation.getCodecRegistry());

        this.idGenerator = idGeneratorProvider != null ?
                idGeneratorProvider.build(this.getCollectionName(),
                        sequence,
                        entityInformation.getEntityType(),
                        entityInformation.getIdType(),
                        this::getDatabase) :
                DefaultIdGeneratorProvider.Default.build(
                        this.getCollectionName(),
                        sequence,
                        entityInformation.getEntityType(),
                        entityInformation.getIdType(),
                        this::getDatabase);
    }


    public AbstractAsyncMongoBaseRepository(final ReactiveMongoSession mongoSession) {
        this(mongoSession, null, null, null);
    }

    public AbstractAsyncMongoBaseRepository(final ReactiveMongoSession mongoSession, final String collectionName) {
        this(mongoSession, collectionName, null, null);
    }

    @SuppressWarnings({"unchecked"})
    public AbstractAsyncMongoBaseRepository(final ReactiveMongoSession mongoSession, final MongoOptions mongoOptions) {
        this(mongoSession, null, mongoOptions.getSequence(), mongoOptions.getIdGeneratorProvider());
    }

    @SuppressWarnings({"unchecked"})
    public AbstractAsyncMongoBaseRepository(final ReactiveMongoSession mongoSession, final MongoOptions mongoOptions, final String collectionName) {
        this(mongoSession, collectionName, mongoOptions.getSequence(), mongoOptions.getIdGeneratorProvider());
    }

    //#endregion

    //#region getCollection

    /**
     * Get the collection based on the data type
     *
     * @return Collection
     */
    @Override
    public MongoCollection<TEntity> getCollection() {
        return getDatabase().getCollection(getCollectionName(), entityInformation.getEntityType());
    }

    /**
     * Get the collection based on the data type
     *
     * @param writeConcern WriteConcern
     * @return Collection
     * @see WriteConcern
     */
    @Override
    public MongoCollection<TEntity> getCollection(@Nullable final WriteConcern writeConcern) {
        MongoCollection<TEntity> collection = this.getCollection();
        if (writeConcern != null) {
            collection = collection.withWriteConcern(writeConcern);
        }
        return collection;
    }

    /**
     * Get the collection based on the data type
     *
     * @param readPreference ReadPreference
     * @return Collection
     * @see ReadPreference
     */
    @Override
    public MongoCollection<TEntity> getCollection(@Nullable final ReadPreference readPreference) {
        MongoCollection<TEntity> collection = this.getCollection();
        if (readPreference != null) {
            collection = collection.withReadPreference(readPreference);
        }
        return collection;
    }

    //#endregion

    /**
     * @param findPublisher findPublisher
     * @param projection    projection
     * @param sort          sort
     * @param limit         limit
     * @param skip          skip
     * @param hint          hint
     * @param <TResult>     TResult
     * @return FindIterable
     */
    protected <TResult> FindPublisher<TResult> findByOptions(final FindPublisher<TResult> findPublisher
            , final Bson projection, final Bson sort
            , final int limit, final int skip, final Bson hint) {

        FindPublisher<TResult> filter = findPublisher;
        if (projection != null) {
            filter = filter.projection(projection);
        }

        if (limit > 0) {
            filter = filter.limit(limit);
        }

        if (skip > 0) {
            filter = filter.skip(skip);
        }

        if (sort != null) {
            filter = filter.sort(sort);
        }

        if (hint != null) {
            filter = filter.hint(hint);
        }

        return filter;

    }

    protected <TResult> FindPublisher<TResult> doFind(@Nullable final ClientSession session
            , @NonNull final FindOptions options
            , final Class<TResult> resultClass) {

        if (options.filter() == null) {
            options.filter(Filters.empty());
        }

//        Bson projection = BsonUtils.projection(entityInformation.getEntityType(),
//                options.includeFields(),
//                options.excludeFields());

        callGlobalInterceptors(PreFind.class, null, options);

        FindPublisher<TResult> result;
        if (session == null) {
            result = getCollection(options.readPreference()).find(options.filter(), resultClass);
        } else {
            result = getCollection(options.readPreference()).find(session, options.filter(), resultClass);
        }
        result = findByOptions(result, options.projection(), options.sort(), options.limit(), options.skip(), options.hint());

        return result;
    }


    protected Mono<Long> doCount(@Nullable final ClientSession session
            , @NonNull final CountOptions options) {

        if (options.filter() == null) {
            options.filter(Filters.empty());
        }

        callGlobalInterceptors(PreFind.class, null, options);

        com.mongodb.client.model.CountOptions countOptions = new com.mongodb.client.model.CountOptions()
                .hint(options.hint())
                .limit(options.limit())
                .skip(options.skip());

        if (session == null) {
            return Mono.from(
                    getCollection(options.readPreference()).countDocuments(options.filter(), countOptions)
            );
        } else {
            return Mono.from(
                    getCollection(options.readPreference()).countDocuments(session, options.filter(), countOptions)
            );
        }
    }


    protected Mono<InsertOneResult> doInsert(@Nullable final ClientSession session
            , final TEntity entity
            , final WriteConcern writeConcern) {

        Mono<TEntity> mono = Mono.just(entity);
        if (entity.getId() == null && idGenerator != null) {

            mono = idGenerator.generateId().map(x -> {
                entity.setId(x);
                return entity;

            });

        }

        return mono.flatMap((e) -> {
            callGlobalInterceptors(PreInsert.class, e, null);
            return Mono.just(e);
        }).flatMap((e) -> {
            if (session == null) {
                return Mono.from(
                        getCollection(writeConcern).insertOne(e)
                );
            } else {
                return Mono.from(
                        getCollection(writeConcern).insertOne(session, e)
                );
            }
        });
    }

    protected Mono<InsertManyResult> doInsertBatch(@Nullable final ClientSession session
            , final List<TEntity> entities
            , final WriteConcern writeConcern) {

        Mono<List<TEntity>> mono = Mono.just(entities);

        List<TEntity> needIdEntities = entities.stream().filter(x -> x.getId() == null).collect(Collectors.toList());
        long count = needIdEntities.size();

        if (count > 0 && idGenerator != null) {

            mono = idGenerator.generateIdBatch(count).map(ids -> {

                for (int i = 0; i < count; i++) {
                    needIdEntities.get(i).setId(ids.get(i));
                }

                return entities;
            });

        }

        return mono.flatMap((es) -> {
            for (TEntity entity : es) {
                callGlobalInterceptors(PreInsert.class, entity, null);
            }
            return Mono.just(es);
        }).flatMap(es -> {

            if (session == null) {
                return Mono.from(
                        getCollection(writeConcern).insertMany(es)
                );
            } else {
                return Mono.from(
                        getCollection(writeConcern).insertMany(session, es)
                );
            }
        });
    }

    protected Mono<UpdateResult> doUpdate(@Nullable final ClientSession session
            , @NonNull final UpdateOptions options
            , final ExecuteType executeType) {

        if (options.filter() == null) {
            options.filter(Filters.empty());
        }

        callGlobalInterceptors(PreUpdate.class, null, options);

        com.mongodb.client.model.UpdateOptions updateOptions =
                new com.mongodb.client.model.UpdateOptions()
                        .hint(options.hint())
                        .upsert(options.upsert());

        if (executeType == ExecuteType.ONE) {

            if (session == null) {
                return Mono.from(
                        getCollection(options.writeConcern())
                                .updateOne(options.filter(), options.update(), updateOptions)
                );
            } else {
                return Mono.from(
                        getCollection(options.writeConcern())
                                .updateOne(session, options.filter(), options.update(), updateOptions)
                );
            }
        } else {
            if (session == null) {
                return Mono.from(
                        getCollection(options.writeConcern())
                                .updateMany(options.filter(), options.update(), updateOptions)
                );

            } else {
                return Mono.from(
                        getCollection(options.writeConcern())
                                .updateMany(session, options.filter(), options.update(), updateOptions)
                );

            }
        }
    }

    protected Mono<DeleteResult> doDelete(@Nullable final ClientSession session
            , @NonNull final DeleteOptions options
            , final ExecuteType executeType) {

        if (options.filter() == null) {
            options.filter(Filters.empty());
        }

        callGlobalInterceptors(PreDelete.class, null, options);

        com.mongodb.client.model.DeleteOptions deleteOptions =
                new com.mongodb.client.model.DeleteOptions()
                        .hint(options.hint());

        if (executeType == ExecuteType.ONE) {
            if (session == null) {

                return Mono.from(
                        getCollection(options.writeConcern())
                                .deleteOne(options.filter(), deleteOptions)
                );
            } else {

                return Mono.from(
                        getCollection(options.writeConcern())
                                .deleteOne(session, options.filter(), deleteOptions)
                );
            }
        } else {
            if (session == null) {

                return Mono.from(
                        getCollection(options.writeConcern())
                                .deleteMany(options.filter(), deleteOptions)
                );
            } else {

                return Mono.from(
                        getCollection(options.writeConcern())
                                .deleteMany(session, options.filter(), deleteOptions)
                );
            }
        }

    }

    protected Mono<TEntity> doFindOneAndUpdate(@Nullable final ClientSession session
            , @NonNull final FindOneAndUpdateOptions options) {

        if (options.filter() == null) {
            options.filter(Filters.empty());
        }

        callGlobalInterceptors(PreUpdate.class, null, options);

        com.mongodb.client.model.FindOneAndUpdateOptions findOneAndUpdateOptions =
                new com.mongodb.client.model.FindOneAndUpdateOptions()
                        .returnDocument(options.returnDocument())
                        .upsert(options.upsert())
                        .hint(options.hint())
                        .sort(options.sort());


        if (session == null) {

            return Mono.from(
                    getCollection(options.writeConcern())
                            .findOneAndUpdate(options.filter(), options.update(), findOneAndUpdateOptions)
            );
        } else {

            return Mono.from(
                    getCollection(options.writeConcern())
                            .findOneAndUpdate(session, options.filter(), options.update(), findOneAndUpdateOptions)
            );
        }
    }

    protected Mono<TEntity> doFindOneAndDelete(@Nullable final ClientSession session
            , @NonNull final FindOneAndDeleteOptions options) {

        if (options.filter() == null) {
            options.filter(Filters.empty());
        }

        callGlobalInterceptors(PreDelete.class, null, options);

        com.mongodb.client.model.FindOneAndDeleteOptions findOneAndDeleteOptions =
                new com.mongodb.client.model.FindOneAndDeleteOptions()
                        .hint(options.hint())
                        .sort(options.sort());

        if (session == null) {

            return Mono.from(
                    getCollection(options.writeConcern())
                            .findOneAndDelete(options.filter(), findOneAndDeleteOptions)
            );
        } else {

            return Mono.from(
                    getCollection(options.writeConcern())
                            .findOneAndDelete(session, options.filter(), findOneAndDeleteOptions)
            );
        }
    }

//    protected Mono<DeleteResult> doDeleteMany(@Nullable final ClientSession session
//            , @NonNull final DeleteOptions options) {
//
//        if (options.filter() == null) {
//            options.filter(Filters.empty());
//        }
//
//        com.mongodb.client.model.DeleteOptions deleteOptions =
//                new com.mongodb.client.model.DeleteOptions()
//                        .hint(options.hint());
//
//        if (session == null) {
//
//            return Mono.from(
//                    getCollection(options.writeConcern()).deleteMany(options.filter(), deleteOptions)
//            );
//        } else {
//
//            return Mono.from(
//                    getCollection(options.writeConcern()).deleteMany(session, options.filter(), deleteOptions)
//            );
//        }
//    }

    /**
     * @param updateEntity Entity
     * @param isUpsert     default false
     * @return Update Bson
     */
    protected Mono<Bson> createUpdateBson(final TEntity updateEntity, final boolean isUpsert) {

        BsonDocument bsDoc = entityInformation.toBsonDocument(updateEntity);
        bsDoc.remove(BsonConstant.PRIMARY_KEY_NAME);

        Bson update = new BsonDocument("$set", bsDoc);
        if (isUpsert && idGenerator != null) {
            return idGenerator.generateId().map(id ->
                    Updates.combine(update, Updates.setOnInsert(BsonConstant.PRIMARY_KEY_NAME, id))
            );
        } else {
            return Mono.just(update);
        }

    }

}
