package org.raven.mongodb.repository;

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

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncModifyOperationImpl<TEntity extends Entity<TKey>, TKey>
        implements SyncModifyOperation<TEntity, TKey> {

    private final AbstractMongoBaseRepository<TEntity, TKey> baseRepository;

//    private final Function2<TEntity, Boolean, Bson> createUpdateBsonProxy;
//    private final Function3<ClientSession, TEntity, WriteConcern, InsertOneResult> doInsertProxy;
//    private final Function3<ClientSession, List<TEntity>, WriteConcern, InsertManyResult> doInsertBatchProxy;
//    private final Function3<ClientSession, UpdateOptions, UpdateType, UpdateResult> doUpdateProxy;
//    private final Function2<ClientSession, FindOneAndUpdateOptions, TEntity> doFindOneAndUpdateProxy;
//    private final Function2<ClientSession, FindOneAndDeleteOptions, TEntity> doFindOneAndDeleteProxy;
//    private final Function2<ClientSession, DeleteOptions, DeleteResult> doDeleteOneProxy;
//    private final Function2<ClientSession, DeleteOptions, DeleteResult> doDeleteManyProxy;
//
//    private final EntityInformation<TEntity, TKey> entityInformation;

    private final @Nullable ClientSession clientSession;

    public SyncModifyOperationImpl(AbstractMongoBaseRepository<TEntity, TKey> baseRepository,
                                   @Nullable ClientSession clientSession) {
        this.baseRepository = baseRepository;
        this.clientSession = clientSession;
    }

//    public SyncModifyOperationImpl(Function2<TEntity, Boolean, Bson> createUpdateBsonProxy,
//                                   Function3<ClientSession, TEntity, WriteConcern, InsertOneResult> doInsertProxy,
//                                   Function3<ClientSession, List<TEntity>, WriteConcern, InsertManyResult> doInsertBatchProxy,
//                                   Function3<ClientSession, UpdateOptions, UpdateType, UpdateResult> doUpdateProxy,
//                                   Function2<ClientSession, FindOneAndUpdateOptions, TEntity> doFindOneAndUpdateProxy,
//                                   Function2<ClientSession, FindOneAndDeleteOptions, TEntity> doFindOneAndDeleteProxy,
//                                   Function2<ClientSession, DeleteOptions, DeleteResult> doDeleteOneProxy,
//                                   Function2<ClientSession, DeleteOptions, DeleteResult> doDeleteManyProxy,
//                                   EntityInformation<TEntity, TKey> entityInformation,
//                                   @Nullable ClientSession clientSession) {
//
//        this.createUpdateBsonProxy = createUpdateBsonProxy;
//        this.doInsertProxy = doInsertProxy;
//        this.doInsertBatchProxy = doInsertBatchProxy;
//        this.doUpdateProxy = doUpdateProxy;
//        this.doFindOneAndUpdateProxy = doFindOneAndUpdateProxy;
//        this.doFindOneAndDeleteProxy = doFindOneAndDeleteProxy;
//        this.doDeleteOneProxy = doDeleteOneProxy;
//        this.doDeleteManyProxy = doDeleteManyProxy;
//        this.entityInformation = entityInformation;
//        this.clientSession = clientSession;
//
//    }

    protected SyncModifyOperationImpl<TEntity, TKey> clone(ClientSession clientSession) {
        return new SyncModifyOperationImpl<>(
                this.baseRepository,
                clientSession);
    }

//    protected SyncModifyOperationImpl<TEntity, TKey> clone(ClientSession clientSession) {
//        return new SyncModifyOperationImpl<>(
//                this.createUpdateBsonProxy,
//                this.doInsertProxy,
//                this.doInsertBatchProxy,
//                this.doUpdateProxy,
//                this.doFindOneAndUpdateProxy,
//                this.doFindOneAndDeleteProxy,
//                this.doDeleteOneProxy,
//                this.doDeleteManyProxy,
//                this.entityInformation,
//                clientSession);
//    }

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
    public ModifyProxy<TEntity, TKey, TKey, Map<Integer, TKey>, Long, TEntity, Long> modifyProxy() {
        return proxy;
    }

    private final ModifyProxy<TEntity, TKey, TKey, Map<Integer, TKey>, Long, TEntity, Long> proxy =
            new ModifyProxy<TEntity, TKey, TKey, Map<Integer, TKey>, Long, TEntity, Long>() {
                @Override
                public EntityInformation<TEntity, TKey> getEntityInformation() {
                    return baseRepository.getEntityInformation();
                }

                @Override
                public TKey doInsert(TEntity entity, WriteConcern writeConcern) {
                    InsertOneResult insertOneResult = SyncModifyOperationImpl.this.doInsert(entity, writeConcern);
                    return insertOneResult.wasAcknowledged()
                            ? BsonUtils.convert(baseRepository.getEntityInformation().getIdType(), insertOneResult.getInsertedId())
                            : null;
                }

                @Override
                public Map<Integer, TKey> doInsertBatch(List<TEntity> entities, WriteConcern writeConcern) {
                    InsertManyResult insertManyResult = SyncModifyOperationImpl.this.doInsertBatch(entities, writeConcern);

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
                public Long doUpdate(@NonNull UpdateOptions options, UpdateType updateType) {
                    UpdateResult updateResult = SyncModifyOperationImpl.this.doUpdate(options, updateType);
                    return updateResult.wasAcknowledged() ? updateResult.getModifiedCount() : 0;
                }

                @Override
                public TEntity doFindOneAndUpdate(FindOneAndUpdateOptions options) {
                    return SyncModifyOperationImpl.this.doFindOneAndUpdate(options);
                }

                @Override
                public TEntity doFindOneAndDelete(@NonNull FindOneAndDeleteOptions options) {
                    return SyncModifyOperationImpl.this.doFindOneAndDelete(options);
                }

                @Override
                public Long doDeleteOne(DeleteOptions options) {
                    DeleteResult deleteResult = SyncModifyOperationImpl.this.doDeleteOne(options);
                    return deleteResult.wasAcknowledged() ? deleteResult.getDeletedCount() : 0;
                }

                @Override
                public Long doDeleteMany(DeleteOptions options) {
                    DeleteResult deleteResult = SyncModifyOperationImpl.this.doDeleteMany(options);
                    return deleteResult.wasAcknowledged() ? deleteResult.getDeletedCount() : 0;
                }
            };
}
