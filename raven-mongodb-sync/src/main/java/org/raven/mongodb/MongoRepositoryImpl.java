package org.raven.mongodb;

import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoDatabase;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.operation.ModifyExecutor;
import org.raven.mongodb.spi.IdGenerator;
import org.raven.mongodb.spi.IdGeneratorProvider;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 */
public class MongoRepositoryImpl<TEntity extends Entity<TKey>, TKey>
        extends MongoQueryRepositoryImpl<TEntity, TKey>
        implements MongoRepository<TEntity, TKey> {

    private final SyncWriteOperationImpl<TEntity, TKey> operation;

    //#region constructor

    /**
     * constructor
     *
     * @param mongoSession mongoSession
     */
    public MongoRepositoryImpl(final MongoSession mongoSession) {
        this(mongoSession, null, (IdGeneratorProvider<IdGenerator<TKey>, MongoDatabase>) null);
    }

    /**
     * constructor
     *
     * @param mongoSession   MongoSession
     * @param collectionName collectionName
     */
    public MongoRepositoryImpl(final MongoSession mongoSession, final String collectionName) {
        this(mongoSession, collectionName, null);
    }

    /**
     * constructor
     *
     * @param mongoSession MongoSession
     * @param mongoOptions MongoOptions
     */
    public MongoRepositoryImpl(final MongoSession mongoSession, final MongoOptions mongoOptions) {
        this(mongoSession, null, mongoOptions.getIdGeneratorProvider());
    }

    /**
     * constructor
     *
     * @param mongoSession   MongoSession
     * @param mongoOptions   MongoOptions
     * @param collectionName collectionName
     */
    public MongoRepositoryImpl(final MongoSession mongoSession, final MongoOptions mongoOptions, final String collectionName) {
        this(mongoSession, collectionName, mongoOptions.getIdGeneratorProvider());
    }

    /**
     * constructor
     *
     * @param mongoSession        mongoSession
     * @param collectionName      collectionName
     * @param idGeneratorProvider idGeneratorProvider
     */
    public MongoRepositoryImpl(final MongoSession mongoSession, final String collectionName
            , final IdGeneratorProvider<IdGenerator<TKey>, MongoDatabase> idGeneratorProvider) {
        super(mongoSession, collectionName, idGeneratorProvider);

        operation = new SyncWriteOperationImpl<>(this, null);
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

        return operation.updateOne(filter, updateEntity, isUpsert, hint, writeConcern);
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

        return operation.findOneAndUpdate(filter, entity, isUpsert, sort, hint);
    }

    //#endregion

    //region protected

    public SyncWriteOperation<TEntity, TKey> modifyWithClientSession(@Nullable ClientSession clientSession) {
        if (clientSession == null) {
            return operation;
        } else {
            return operation.clone(clientSession);
        }
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
    public ModifyExecutor<TEntity, TKey, TKey, Map<Integer, TKey>, Long, TEntity, Long> modifyExecutor() {
        return operation.modifyExecutor();
    }


}
