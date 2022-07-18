package org.raven.mongodb.repository;

import com.mongodb.WriteConcern;
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
import org.raven.mongodb.repository.annotations.PreUpdate;
import org.raven.mongodb.repository.annotations.PreInsert;
import org.raven.mongodb.repository.contants.BsonConstant;
//import org.raven.mongodb.repository.spi.IdGenerationType;
import org.raven.mongodb.repository.spi.IdGenerator;
import org.raven.mongodb.repository.spi.IdGeneratorProvider;
import org.raven.mongodb.repository.spi.Sequence;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 */
public class MongoRepositoryImpl<TEntity extends Entity<TKey>, TKey>
        extends MongoReadonlyRepositoryImpl<TEntity, TKey>
        implements MongoRepository<TEntity, TKey> {

    //#region constructor

    /**
     * constructor
     *
     * @param mongoSession        mongoSession
     * @param collectionName      collectionName
     * @param sequence            Sequence
     * @param idGeneratorProvider idGeneratorProvider
     */
    public MongoRepositoryImpl(final MongoSession mongoSession, final String collectionName, final Sequence sequence
            , final IdGeneratorProvider<IdGenerator<TKey>, MongoDatabase> idGeneratorProvider) {
        super(mongoSession, collectionName, sequence, idGeneratorProvider);
    }

    /**
     * constructor
     *
     * @param mongoSession mongoSession
     */
    public MongoRepositoryImpl(final MongoSession mongoSession) {
        super(mongoSession);
    }

    /**
     * constructor
     *
     * @param mongoSession   MongoSession
     * @param collectionName collectionName
     */
    public MongoRepositoryImpl(final MongoSession mongoSession, final String collectionName) {
        super(mongoSession, collectionName);
    }

    /**
     * constructor
     *
     * @param mongoSession MongoSession
     * @param mongoOptions MongoOptions
     */
    public MongoRepositoryImpl(final MongoSession mongoSession, final MongoOptions mongoOptions) {
        super(mongoSession, mongoOptions);
    }

    /**
     * constructor
     *
     * @param mongoSession   MongoSession
     * @param mongoOptions   MongoOptions
     * @param collectionName collectionName
     */
    public MongoRepositoryImpl(final MongoSession mongoSession, final MongoOptions mongoOptions, final String collectionName) {
        super(mongoSession, mongoOptions, collectionName);
    }

    //#endregion

    //#region update

    /**
     * 修改单条数据
     *
     * @param filter       conditions
     * @param updateEntity TEntity
     * @param isUpsert     default false
     * @param hint         hint
     * @param writeConcern {{@link WriteConcern}}
     * @return UpdateResult
     */
    @Override
    public UpdateResult updateOne(final Bson filter, final TEntity updateEntity, final boolean isUpsert, final Bson hint, final WriteConcern writeConcern) {

        Bson update = createUpdateBson(updateEntity, isUpsert);

        return updateOne(filter, update, isUpsert, hint, writeConcern);
    }

    //#endregion

    //#region findAndModify

    /**
     * 找到并更新
     *
     * @param filter   conditions
     * @param entity   TEntity
     * @param isUpsert default false
     * @param sort     sort
     * @return Entity
     */
    @Override
    public TEntity findOneAndUpdate(final Bson filter, final TEntity entity, final boolean isUpsert, final Bson sort, final Bson hint) {

        Bson update = createUpdateBson(entity, isUpsert);

        return this.findOneAndUpdate(filter, update, isUpsert, sort, hint);
    }

    //#endregion

    //region protected

    /**
     * @param entity       Entity
     * @param writeConcern {{@link WriteConcern}}
     * @return {{@link InsertOneResult}}
     */
    protected InsertOneResult doInsert(final TEntity entity, final WriteConcern writeConcern) {

        if (entity.getId() == null && idGenerator != null) {
            TKey id = idGenerator.generateId();
            entity.setId(id);
        }
        callGlobalInterceptors(PreInsert.class, entity, null);

        return super.getCollection(writeConcern).insertOne(entity);
    }

    protected InsertManyResult doInsertBatch(final List<TEntity> entities, final WriteConcern writeConcern) {

        List<TEntity> entityStream = entities.stream().filter(x -> x.getId() == null).collect(Collectors.toList());
        long count = entityStream.size();

        if (count > 0 && idGenerator != null) {
            List<TKey> ids = idGenerator.generateIdBatch(count);

            for (int i = 0; i < count; i++) {
                entityStream.get(i).setId(ids.get(i));
            }
        }

        for (TEntity entity : entities) {
            callGlobalInterceptors(PreInsert.class, entity, null);
        }

        return super.getCollection(writeConcern).insertMany(entities);
    }

    protected UpdateResult doUpdate(@NonNull final UpdateOptions options,
                                    final UpdateType updateType) {

        if (options.filter() == null) {
            options.filter(Filters.empty());
        }

        callGlobalInterceptors(PreUpdate.class, null, options);

        if (updateType == UpdateType.ONE) {
            return super.getCollection(options.writeConcern()).updateOne(options.filter(), options.update(),
                    new com.mongodb.client.model.UpdateOptions()
                            .hint(options.hint())
                            .upsert(options.upsert())

            );
        } else {
            return super.getCollection(options.writeConcern()).updateMany(options.filter(), options.update(),
                    new com.mongodb.client.model.UpdateOptions()
                            .hint(options.hint())
                            .upsert(options.upsert())
            );
        }
    }

    protected TEntity doFindOneAndUpdate(final FindOneAndUpdateOptions options) {

        if (options.filter() == null) {
            options.filter(Filters.empty());
        }

        callGlobalInterceptors(PreUpdate.class, null, options);

        return super.getCollection().findOneAndUpdate(options.filter(), options.update(),
                new com.mongodb.client.model.FindOneAndUpdateOptions()
                        .returnDocument(options.returnDocument())
                        .upsert(options.upsert())
                        .hint(options.hint())
                        .sort(options.sort())
        );
    }

    protected TEntity doFindOneAndDelete(@NonNull final FindOneAndDeleteOptions options) {

        if (options.filter() == null) {
            options.filter(Filters.empty());
        }

        return super.getCollection().findOneAndDelete(options.filter(),
                new com.mongodb.client.model.FindOneAndDeleteOptions()
                        .hint(options.hint())
                        .sort(options.sort())
        );
    }

    protected DeleteResult doDeleteOne(final DeleteOptions options) {
        return super.getCollection(options.writeConcern()).deleteOne(options.filter(),
                new com.mongodb.client.model.DeleteOptions()
                        .hint(options.hint())
        );
    }

    protected DeleteResult doDeleteMany(final DeleteOptions options) {
        return super.getCollection(options.writeConcern()).deleteMany(options.filter(),
                new com.mongodb.client.model.DeleteOptions()
                        .hint(options.hint())
        );
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

    @Override
    public ModifyProxy<TEntity, TKey, InsertOneResult, InsertManyResult, UpdateResult, TEntity, DeleteResult> modifyProxy() {
        return proxy;
    }

    private final ModifyProxy<TEntity, TKey, InsertOneResult, InsertManyResult, UpdateResult, TEntity, DeleteResult> proxy =
            new ModifyProxy<>() {
                @Override
                protected EntityInformation<TEntity, TKey> getEntityInformation() {
                    return entityInformation;
                }

                @Override
                protected InsertOneResult doInsert(TEntity entity, WriteConcern writeConcern) {
                    return MongoRepositoryImpl.this.doInsert(entity, writeConcern);
                }

                @Override
                protected InsertManyResult doInsertBatch(List<TEntity> entities, WriteConcern writeConcern) {
                    return MongoRepositoryImpl.this.doInsertBatch(entities, writeConcern);
                }

                @Override
                protected UpdateResult doUpdate(@NonNull UpdateOptions options, UpdateType updateType) {
                    return MongoRepositoryImpl.this.doUpdate(options, updateType);
                }

                @Override
                protected TEntity doFindOneAndUpdate(FindOneAndUpdateOptions options) {
                    return MongoRepositoryImpl.this.doFindOneAndUpdate(options);
                }

                @Override
                protected TEntity doFindOneAndDelete(@NonNull FindOneAndDeleteOptions options) {
                    return MongoRepositoryImpl.this.doFindOneAndDelete(options);
                }

                @Override
                protected DeleteResult doDeleteOne(DeleteOptions options) {
                    return MongoRepositoryImpl.this.doDeleteOne(options);
                }

                @Override
                protected DeleteResult doDeleteMany(DeleteOptions options) {
                    return MongoRepositoryImpl.this.doDeleteMany(options);
                }
            };

}
