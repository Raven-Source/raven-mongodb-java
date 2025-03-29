package org.raven.mongodb;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoDatabase;
import org.raven.commons.data.Entity;
import org.raven.mongodb.criteria.FindOptions;
import org.raven.mongodb.operation.FindExecutor;
import org.raven.mongodb.spi.IdGenerator;
import org.raven.mongodb.spi.IdGeneratorProvider;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 只读数据仓储
 *
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 */
public class MongoQueryRepositoryImpl<TEntity extends Entity<TKey>, TKey>
        extends AbstractMongoBaseRepository<TEntity, TKey>
        implements MongoQueryRepository<TEntity, TKey> {

    private final SyncReadOperationImpl<TEntity, TKey> operation;

    //#region constructor

    /**
     * constructor
     *
     * @param mongoSession MongoSession
     */
    public MongoQueryRepositoryImpl(final MongoSession mongoSession) {
        this(mongoSession, null, (IdGeneratorProvider<IdGenerator<TKey>, MongoDatabase>) null);
    }

    /**
     * constructor
     *
     * @param mongoSession   MongoSession
     * @param collectionName collectionName
     */
    public MongoQueryRepositoryImpl(final MongoSession mongoSession, final String collectionName) {
        this(mongoSession, collectionName, null);
    }

    /**
     * constructor
     *
     * @param mongoSession MongoSession
     * @param mongoOptions MongoOptions
     */
    public MongoQueryRepositoryImpl(final MongoSession mongoSession, final MongoOptions mongoOptions) {
        this(mongoSession, null, mongoOptions.getIdGeneratorProvider());
    }

    /**
     * constructor
     *
     * @param mongoSession   MongoSession
     * @param mongoOptions   MongoOptions
     * @param collectionName collectionName
     */
    public MongoQueryRepositoryImpl(final MongoSession mongoSession, final MongoOptions mongoOptions, final String collectionName) {
        this(mongoSession, collectionName, mongoOptions.getIdGeneratorProvider());
    }

    /**
     * constructor
     *
     * @param mongoSession        MongoSession
     * @param collectionName      collectionName
     * @param idGeneratorProvider IdGeneratorProvider
     */
    public MongoQueryRepositoryImpl(final MongoSession mongoSession, final String collectionName
            , final IdGeneratorProvider<IdGenerator<TKey>, MongoDatabase> idGeneratorProvider) {

        super(mongoSession, collectionName, idGeneratorProvider);

        operation = new SyncReadOperationImpl<>(this, null);
    }

    //#endregion

    //region ext

    @Override
    public <TResult> TResult findOne(FindOptions findOptions, Class<TResult> resultClass) {
        return operation.findOne(findOptions, resultClass);
    }

    @Override
    public <TResult> List<TResult> findMany(FindOptions findOptions, Class<TResult> resultClass) {
        return operation.findMany(findOptions, resultClass);
    }

    //endregion

    //region protected

    @Override
    public SyncReadOperation<TEntity, TKey> findWithClientSession(@Nullable ClientSession clientSession) {
        if (clientSession == null) {
            return operation;
        } else {
            return operation.clone(clientSession);
        }
    }

    //endregion

    @Override
    public FindExecutor<TEntity, TKey, TEntity, List<TEntity>, Long, Boolean> findExecutor() {
        return operation.findExecutor();
    }

}
