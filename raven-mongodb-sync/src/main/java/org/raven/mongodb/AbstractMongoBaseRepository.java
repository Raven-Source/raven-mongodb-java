package org.raven.mongodb;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import lombok.NonNull;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.annotations.PreDelete;
import org.raven.mongodb.annotations.PreFind;
import org.raven.mongodb.annotations.PreInsert;
import org.raven.mongodb.annotations.PreUpdate;
import org.raven.mongodb.contants.BsonConstant;
import org.raven.mongodb.spi.IdGenerator;
import org.raven.mongodb.spi.IdGeneratorProvider;
import org.raven.mongodb.spi.Sequence;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 */
public abstract class AbstractMongoBaseRepository<TEntity extends Entity<TKey>, TKey>
        extends AbstractMongoRepository<TEntity, TKey>
        implements MongoBaseRepository<TEntity> {

    protected IdGenerator<TKey> idGenerator;

    protected MongoSession mongoSession;

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

    public AbstractMongoBaseRepository(final MongoSession mongoSession, final String collectionName, final Sequence sequence
            , final IdGeneratorProvider<IdGenerator<TKey>, MongoDatabase> idGeneratorProvider) {
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


    public AbstractMongoBaseRepository(final MongoSession mongoSession) {
        this(mongoSession, null, null, null);
    }

    public AbstractMongoBaseRepository(final MongoSession mongoSession, final String collectionName) {
        this(mongoSession, collectionName, null, null);
    }

    @SuppressWarnings({"unchecked"})
    public AbstractMongoBaseRepository(final MongoSession mongoSession, final MongoOptions mongoOptions) {
        this(mongoSession, null, mongoOptions.getSequence(), mongoOptions.getIdGeneratorProvider());
    }

    @SuppressWarnings({"unchecked"})
    public AbstractMongoBaseRepository(final MongoSession mongoSession, final MongoOptions mongoOptions, final String collectionName) {
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
    public MongoCollection<TEntity> getCollection(final WriteConcern writeConcern) {
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
    public MongoCollection<TEntity> getCollection(final ReadPreference readPreference) {
        MongoCollection<TEntity> collection = this.getCollection();
        if (readPreference != null) {
            collection = collection.withReadPreference(readPreference);
        }
        return collection;
    }

    //#endregion

    /**
     * @param findIterable findIterable
     * @param projection   projection
     * @param sort         sort
     * @param limit        limit
     * @param skip         skip
     * @param hint         hint
     * @param <TResult>    TResult
     * @return FindIterable
     */
    protected <TResult> FindIterable<TResult> findOptions(final FindIterable<TResult> findIterable,
                                                          @Nullable final Bson projection,
                                                          @Nullable final Bson sort,
                                                          final int limit,
                                                          final int skip,
                                                          @Nullable final Bson hint) {

        FindIterable<TResult> filter = findIterable;
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

    protected <TResult> FindIterable<TResult> doFind(@Nullable final ClientSession session
            , @NonNull final FindOptions options
            , final Class<TResult> resultClass) {

        if (options.filter() == null) {
            options.filter(Filters.empty());
        }

        Bson projection = BsonUtils.projection(entityInformation.getEntityType(),
                options.includeFields(), options.excludeFields());

        callGlobalInterceptors(PreFind.class, null, options);

        FindIterable<TResult> result;
        if (session == null) {
            result = getCollection(options.readPreference()).find(options.filter(), resultClass);
        } else {
            result = getCollection(options.readPreference()).find(session, options.filter(), resultClass);
        }
        result = findOptions(result, projection, options.sort(), options.limit(), options.skip(), options.hint());

        return result;
    }

    protected long doCount(@Nullable final ClientSession session
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
            return getCollection(options.readPreference()).countDocuments(options.filter(), countOptions);
        } else {
            return getCollection(options.readPreference()).countDocuments(session, options.filter(), countOptions);
        }

    }

    //region protected

    /**
     * @param session      session
     * @param entity       Entity
     * @param writeConcern {{@link WriteConcern}}
     * @return {{@link InsertOneResult}}
     */
    protected InsertOneResult doInsert(@Nullable final ClientSession session
            , final TEntity entity
            , final WriteConcern writeConcern) {

        if (entity.getId() == null && idGenerator != null) {
            TKey id = idGenerator.generateId();
            entity.setId(id);
        }
        callGlobalInterceptors(PreInsert.class, entity, null);

        if (session == null) {
            return getCollection(writeConcern).insertOne(entity);
        } else {
            return getCollection(writeConcern).insertOne(session, entity);
        }
    }

    protected InsertManyResult doInsertBatch(@Nullable final ClientSession session
            , final List<TEntity> entities
            , final WriteConcern writeConcern) {

        List<TEntity> needIdEntities = entities.stream().filter(x -> x.getId() == null).collect(Collectors.toList());
        long count = needIdEntities.size();

        if (count > 0 && idGenerator != null) {
            List<TKey> ids = idGenerator.generateIdBatch(count);

            for (int i = 0; i < count; i++) {
                needIdEntities.get(i).setId(ids.get(i));
            }
        }

        for (TEntity entity : entities) {
            callGlobalInterceptors(PreInsert.class, entity, null);
        }

        if (session == null) {
            return getCollection(writeConcern).insertMany(entities);
        } else {
            return getCollection(writeConcern).insertMany(session, entities);
        }
    }

    protected UpdateResult doUpdate(@Nullable final ClientSession session
            , @NonNull final UpdateOptions options
            , final UpdateType updateType) {

        if (options.filter() == null) {
            options.filter(Filters.empty());
        }

        callGlobalInterceptors(PreUpdate.class, null, options);

        com.mongodb.client.model.UpdateOptions updateOptions =
                new com.mongodb.client.model.UpdateOptions()
                        .hint(options.hint())
                        .upsert(options.upsert());

        if (updateType == UpdateType.ONE) {
            if (session == null) {
                return getCollection(options.writeConcern())
                        .updateOne(options.filter(), options.update(), updateOptions);
            } else {
                return getCollection(options.writeConcern())
                        .updateOne(session, options.filter(), options.update(), updateOptions);
            }
        } else {
            if (session == null) {
                return getCollection(options.writeConcern())
                        .updateMany(options.filter(), options.update(), updateOptions);
            } else {
                return getCollection(options.writeConcern())
                        .updateMany(session, options.filter(), options.update(), updateOptions);
            }
        }
    }

    protected TEntity doFindOneAndUpdate(@Nullable final ClientSession session
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
            return getCollection().findOneAndUpdate(options.filter(), options.update(),
                    findOneAndUpdateOptions
            );
        } else {
            return getCollection().findOneAndUpdate(session, options.filter(), options.update(),
                    findOneAndUpdateOptions
            );
        }
    }

    protected TEntity doFindOneAndDelete(@Nullable final ClientSession session
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
            return getCollection().findOneAndDelete(options.filter(),
                    findOneAndDeleteOptions
            );
        } else {
            return getCollection().findOneAndDelete(session, options.filter(),
                    findOneAndDeleteOptions
            );
        }
    }

    protected DeleteResult doDeleteOne(@Nullable final ClientSession session
            , @NonNull final DeleteOptions options) {

        if (options.filter() == null) {
            options.filter(Filters.empty());
        }

        callGlobalInterceptors(PreDelete.class, null, options);

        com.mongodb.client.model.DeleteOptions deleteOptions =
                new com.mongodb.client.model.DeleteOptions()
                        .hint(options.hint());

        if (session == null) {

            return getCollection(options.writeConcern()).deleteOne(options.filter(),
                    deleteOptions
            );
        } else {

            return getCollection(options.writeConcern()).deleteOne(session, options.filter(),
                    deleteOptions
            );
        }
    }

    protected DeleteResult doDeleteMany(@Nullable final ClientSession session
            , @NonNull final DeleteOptions options) {

        if (options.filter() == null) {
            options.filter(Filters.empty());
        }

        com.mongodb.client.model.DeleteOptions deleteOptions =
                new com.mongodb.client.model.DeleteOptions()
                        .hint(options.hint());

        callGlobalInterceptors(PreDelete.class, null, options);

        if (session == null) {

            return getCollection(options.writeConcern()).deleteMany(options.filter(),
                    deleteOptions
            );
        } else {

            return getCollection(options.writeConcern()).deleteMany(session, options.filter(),
                    deleteOptions
            );
        }
    }

    /**
     * createUpdateBson
     *
     * @param updateEntity Entity
     * @param isUpsert     isUpsert
     * @return Update Bson
     */
    protected Bson createUpdateBson(final TEntity updateEntity, final boolean isUpsert) {

        BsonDocument bsDoc = entityInformation.toBsonDocument(updateEntity);
        bsDoc.remove(BsonConstant.PRIMARY_KEY_NAME);

        Bson update = new BsonDocument("$set", bsDoc);
        if (isUpsert && idGenerator != null) {
            TKey id = idGenerator.generateId();
            update = Updates.combine(update, Updates.setOnInsert(BsonConstant.PRIMARY_KEY_NAME, id));
        }

        return update;

    }

    //endregion

}
