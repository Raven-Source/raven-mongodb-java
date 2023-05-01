package org.raven.mongodb.repository;

import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoDatabase;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.spi.IdGenerator;
import org.raven.mongodb.repository.spi.IdGeneratorProvider;
import org.raven.mongodb.repository.spi.Sequence;

import java.util.Map;

/**
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 */
public class MongoRepositoryImpl<TEntity extends Entity<TKey>, TKey>
        extends MongoReadonlyRepositoryImpl<TEntity, TKey>
        implements MongoRepository<TEntity, TKey> {

    private final SyncModifyOperationImpl<TEntity, TKey> operation;

    //#region constructor

    /**
     * constructor
     *
     * @param mongoSession mongoSession
     */
    public MongoRepositoryImpl(final MongoSession mongoSession) {
        this(mongoSession, null, null, null);
    }

    /**
     * constructor
     *
     * @param mongoSession   MongoSession
     * @param collectionName collectionName
     */
    public MongoRepositoryImpl(final MongoSession mongoSession, final String collectionName) {
        this(mongoSession, collectionName, null, null);
    }

    /**
     * constructor
     *
     * @param mongoSession MongoSession
     * @param mongoOptions MongoOptions
     */
    public MongoRepositoryImpl(final MongoSession mongoSession, final MongoOptions mongoOptions) {
        this(mongoSession, null, mongoOptions.getSequence(), mongoOptions.getIdGeneratorProvider());
    }

    /**
     * constructor
     *
     * @param mongoSession   MongoSession
     * @param mongoOptions   MongoOptions
     * @param collectionName collectionName
     */
    public MongoRepositoryImpl(final MongoSession mongoSession, final MongoOptions mongoOptions, final String collectionName) {
        this(mongoSession, collectionName, mongoOptions.getSequence(), mongoOptions.getIdGeneratorProvider());
    }

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

        operation = new SyncModifyOperationImpl<>(
                super::createUpdateBson,
                super::doInsert,
                super::doInsertBatch,
                super::doUpdate,
                super::doFindOneAndUpdate,
                super::doFindOneAndDelete,
                super::doDeleteOne,
                super::doDeleteMany,
                super.entityInformation,
                null);
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
    public Long updateOne(final Bson filter, final TEntity updateEntity, final boolean isUpsert, final Bson hint, final WriteConcern writeConcern) {

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

    public SyncModifyOperation<TEntity, TKey> modifyWithClientSession(ClientSession clientSession) {
        return operation.clone(clientSession);
    }

//    /**
//     * @param entity       Entity
//     * @param writeConcern {{@link WriteConcern}}
//     * @return {{@link InsertOneResult}}
//     */
//    protected InsertOneResult doInsert(final TEntity entity, final WriteConcern writeConcern) {
//        return operation.doInsert(entity, writeConcern);
//    }
//
//    protected InsertManyResult doInsertBatch(final List<TEntity> entities, final WriteConcern writeConcern) {
//        return operation.doInsertBatch(entities, writeConcern);
//    }
//
//    protected UpdateResult doUpdate(@NonNull final UpdateOptions options,
//                                    final UpdateType updateType) {
//        return operation.doUpdate(options, updateType);
//    }
//
//    protected TEntity doFindOneAndUpdate(final FindOneAndUpdateOptions options) {
//        return operation.doFindOneAndUpdate(options);
//    }
//
//    protected TEntity doFindOneAndDelete(@NonNull final FindOneAndDeleteOptions options) {
//        return operation.doFindOneAndDelete(options);
//    }
//
//    protected DeleteResult doDeleteOne(final DeleteOptions options) {
//        return operation.doDeleteOne(options);
//    }
//
//    protected DeleteResult doDeleteMany(final DeleteOptions options) {
//        return operation.doDeleteMany(options);
//    }

    //endregion

    @Override
    public ModifyProxy<TEntity, TKey, TKey, Map<Integer, TKey>, Long, TEntity, Long> modifyProxy() {
        return operation.proxy();
    }


}
