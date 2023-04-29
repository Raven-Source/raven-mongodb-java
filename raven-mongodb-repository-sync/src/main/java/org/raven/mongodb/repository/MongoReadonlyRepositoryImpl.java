package org.raven.mongodb.repository;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoDatabase;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.spi.IdGenerator;
import org.raven.mongodb.repository.spi.IdGeneratorProvider;
import org.raven.mongodb.repository.spi.Sequence;

import java.util.List;

/**
 * 只读数据仓储
 *
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 */
public class MongoReadonlyRepositoryImpl<TEntity extends Entity<TKey>, TKey>
        extends AbstractMongoBaseRepository<TEntity, TKey>
        implements MongoReadonlyRepository<TEntity, TKey> {

    private final SyncFindOperationImpl<TEntity, TKey> operation;

    //#region constructor

    /**
     * constructor
     *
     * @param mongoSession MongoSession
     */
    public MongoReadonlyRepositoryImpl(final MongoSession mongoSession) {
        this(mongoSession, null, null, null);
    }

    /**
     * constructor
     *
     * @param mongoSession   MongoSession
     * @param collectionName collectionName
     */
    public MongoReadonlyRepositoryImpl(final MongoSession mongoSession, final String collectionName) {
        this(mongoSession, collectionName, null, null);
    }

    /**
     * constructor
     *
     * @param mongoSession MongoSession
     * @param mongoOptions MongoOptions
     */
    public MongoReadonlyRepositoryImpl(final MongoSession mongoSession, final MongoOptions mongoOptions) {
        this(mongoSession, null, mongoOptions.getSequence(), mongoOptions.getIdGeneratorProvider());
    }

    /**
     * constructor
     *
     * @param mongoSession   MongoSession
     * @param mongoOptions   MongoOptions
     * @param collectionName collectionName
     */
    public MongoReadonlyRepositoryImpl(final MongoSession mongoSession, final MongoOptions mongoOptions, final String collectionName) {
        this(mongoSession, collectionName, mongoOptions.getSequence(), mongoOptions.getIdGeneratorProvider());
    }

    /**
     * constructor
     *
     * @param mongoSession        MongoSession
     * @param collectionName      collectionName
     * @param sequence            Sequence
     * @param idGeneratorProvider IdGeneratorProvider
     */
    public MongoReadonlyRepositoryImpl(final MongoSession mongoSession, final String collectionName, final Sequence sequence
            , final IdGeneratorProvider<IdGenerator<TKey>, MongoDatabase> idGeneratorProvider) {

        super(mongoSession, collectionName, sequence, idGeneratorProvider);

        operation = new SyncFindOperationImpl<>(this::doFind, this::doCount, super.entityInformation, null);
    }

    //#endregion

    //region ext

    @Override
    public <TResult> TResult findOne(FindOptions findOptions, Class<TResult> resultClass) {
        return operation.findOne(findOptions, resultClass);
    }

    @Override
    public <TResult> List<TResult> findList(FindOptions findOptions, Class<TResult> resultClass) {
        return operation.findList(findOptions, resultClass);
    }

    //endregion

    //region protected

    public SyncFindOperation<TEntity, TKey> findWithClientSession(ClientSession clientSession) {
        return operation.clone(clientSession);
    }

//    protected TEntity doFindOne(final FindOptions options) {
//        return this.doFindOne(options, entityInformation.getEntityType());
//    }
//
//    protected <TResult> TResult doFindOne(final FindOptions options, Class<TResult> resultClass) {
//        return operation.doFindOne(options, resultClass);
//    }
//
//    protected List<TEntity> doFindList(final FindOptions options) {
//        return this.doFindList(options, entityInformation.getEntityType());
//    }
//
//    protected <TResult> List<TResult> doFindList(final FindOptions options, Class<TResult> resultClass) {
//        return operation.doFindList(options, resultClass);
//    }
//
//    protected long doCount(final CountOptions options) {
//        return operation.doCount(options);
//    }
//
//    protected boolean doExists(final ExistsOptions options) {
//        return operation.doExists(options);
//    }

    //endregion

    //region findProxy

    @Override
    public FindProxy<TEntity, TKey, TEntity, List<TEntity>, Long, Boolean> findProxy() {
        return operation.proxy();
    }

//    private final FindProxy<TEntity, TKey, TEntity, List<TEntity>, Long, Boolean> proxy =
//            new FindProxy<TEntity, TKey, TEntity, List<TEntity>, Long, Boolean>() {
//                @Override
//                public EntityInformation<TEntity, TKey> getEntityInformation() {
//                    return MongoReadonlyRepositoryImpl.this.entityInformation;
//                }
//
//                @Override
//                public TEntity doFindOne(FindOptions options) {
//                    return MongoReadonlyRepositoryImpl.this.doFindOne(options);
//                }
//
//                @Override
//                public List<TEntity> doFindList(FindOptions options) {
//                    return MongoReadonlyRepositoryImpl.this.doFindList(options);
//                }
//
//                @Override
//                public Long doCount(CountOptions options) {
//                    return MongoReadonlyRepositoryImpl.this.doCount(options);
//                }
//
//                @Override
//                public Boolean doExists(ExistsOptions options) {
//                    return MongoReadonlyRepositoryImpl.this.doExists(options);
//                }
//            };

    //endregion

}
