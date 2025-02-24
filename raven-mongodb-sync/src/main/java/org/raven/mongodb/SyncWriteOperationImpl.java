package org.raven.mongodb;

import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import lombok.NonNull;
import org.bson.BsonValue;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.criteria.DeleteOptions;
import org.raven.mongodb.criteria.FindOneAndDeleteOptions;
import org.raven.mongodb.criteria.FindOneAndUpdateOptions;
import org.raven.mongodb.criteria.UpdateOptions;
import org.raven.mongodb.operation.ModifyExecutor;
import org.raven.mongodb.util.BsonUtils;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncWriteOperationImpl<TEntity extends Entity<TKey>, TKey>
        implements SyncWriteOperation<TEntity, TKey> {

    private final AbstractMongoBaseRepository<TEntity, TKey> baseRepository;

    private final @Nullable ClientSession clientSession;

    public SyncWriteOperationImpl(AbstractMongoBaseRepository<TEntity, TKey> baseRepository,
                                  @Nullable ClientSession clientSession) {
        this.baseRepository = baseRepository;
        this.clientSession = clientSession;
    }

    protected SyncWriteOperationImpl<TEntity, TKey> clone(ClientSession clientSession) {
        return new SyncWriteOperationImpl<>(
                this.baseRepository,
                clientSession);
    }

    @Override
    public Long updateOne(Bson filter, TEntity updateEntity, boolean isUpsert, Bson hint, WriteConcern writeConcern) {

        Bson update = baseRepository.createUpdateBson(updateEntity, isUpsert);

        return updateOne(filter, update, isUpsert, hint, writeConcern);
    }

    @Override
    public TEntity findOneAndUpdate(Bson filter, TEntity entity, boolean isUpsert, Bson sort, Bson hint) {

        Bson update = baseRepository.createUpdateBson(entity, isUpsert);

        return this.findOneAndUpdate(filter, update, isUpsert, sort, hint);
    }

    /**
     * @param entity       Entity
     * @param writeConcern {{@link WriteConcern}}
     * @return {{@link InsertOneResult}}
     */
    protected InsertOneResult doInsert(final TEntity entity, final WriteConcern writeConcern) {
        return baseRepository.doInsert(this.clientSession, entity, writeConcern);
    }

    protected InsertManyResult doInsertBatch(final List<TEntity> entities, final WriteConcern writeConcern) {
        return baseRepository.doInsertBatch(this.clientSession, entities, writeConcern);
    }

    protected UpdateResult doUpdate(@NonNull final UpdateOptions options,
                                    final UpdateType updateType) {
        return baseRepository.doUpdate(this.clientSession, options, updateType);
    }

    protected TEntity doFindOneAndUpdate(final FindOneAndUpdateOptions options) {
        return baseRepository.doFindOneAndUpdate(this.clientSession, options);
    }

    protected TEntity doFindOneAndDelete(@NonNull final FindOneAndDeleteOptions options) {
        return baseRepository.doFindOneAndDelete(this.clientSession, options);
    }

    protected DeleteResult doDeleteOne(final DeleteOptions options) {
        return baseRepository.doDeleteOne(this.clientSession, options);
    }

    protected DeleteResult doDeleteMany(final DeleteOptions options) {
        return baseRepository.doDeleteMany(this.clientSession, options);
    }

    @Override
    public ModifyExecutor<TEntity, TKey, TKey, Map<Integer, TKey>, Long, TEntity, Long> modifyExecutor() {
        return modifyExecutor;
    }

    private final ModifyExecutor<TEntity, TKey, TKey, Map<Integer, TKey>, Long, TEntity, Long> modifyExecutor =
            new ModifyExecutor<TEntity, TKey, TKey, Map<Integer, TKey>, Long, TEntity, Long>() {

                @Override
                public Class<TEntity> getEntityType() {
                    return baseRepository.getEntityInformation().getEntityType();
                }

                @Override
                public TKey doInsert(TEntity entity, WriteConcern writeConcern) {
                    InsertOneResult insertOneResult = SyncWriteOperationImpl.this.doInsert(entity, writeConcern);
                    return insertOneResult.wasAcknowledged()
                            ? BsonUtils.convert(baseRepository.getEntityInformation().getIdType(), insertOneResult.getInsertedId())
                            : null;
                }

                @Override
                public Map<Integer, TKey> doInsertBatch(List<TEntity> entities, WriteConcern writeConcern) {
                    InsertManyResult insertManyResult = SyncWriteOperationImpl.this.doInsertBatch(entities, writeConcern);

                    Map<Integer, TKey> integerTKeyMap = new HashMap<>();
                    if (insertManyResult.wasAcknowledged()) {
                        for (Map.Entry<Integer, BsonValue> e : insertManyResult.getInsertedIds().entrySet()) {
                            integerTKeyMap.put(
                                    e.getKey(),
                                    BsonUtils.convert(baseRepository.getEntityInformation().getIdType(), e.getValue()))
                            ;
                        }
                    }
                    return integerTKeyMap;
                }

                @Override
                public Long doUpdate(UpdateOptions options, UpdateType updateType) {
                    UpdateResult updateResult = SyncWriteOperationImpl.this.doUpdate(options, updateType);
                    return updateResult.wasAcknowledged() ? updateResult.getModifiedCount() : 0;
                }

                @Override
                public TEntity doFindOneAndUpdate(FindOneAndUpdateOptions options) {
                    return SyncWriteOperationImpl.this.doFindOneAndUpdate(options);
                }

                @Override
                public TEntity doFindOneAndDelete(FindOneAndDeleteOptions options) {
                    return SyncWriteOperationImpl.this.doFindOneAndDelete(options);
                }

                @Override
                public Long doDeleteOne(DeleteOptions options) {
                    DeleteResult deleteResult = SyncWriteOperationImpl.this.doDeleteOne(options);
                    return deleteResult.wasAcknowledged() ? deleteResult.getDeletedCount() : 0;
                }

                @Override
                public Long doDeleteMany(DeleteOptions options) {
                    DeleteResult deleteResult = SyncWriteOperationImpl.this.doDeleteMany(options);
                    return deleteResult.wasAcknowledged() ? deleteResult.getDeletedCount() : 0;
                }
            };
}
