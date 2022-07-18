package org.raven.mongodb.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.annotations.PreFind;
import org.raven.mongodb.repository.contants.BsonConstant;
import org.raven.mongodb.repository.spi.IdGenerator;
import org.raven.mongodb.repository.spi.IdGeneratorProvider;
import org.raven.mongodb.repository.spi.Sequence;

import java.util.ArrayList;
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

    //#region constructor

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
    }

    /**
     * constructor
     *
     * @param mongoSession MongoSession
     */
    public MongoReadonlyRepositoryImpl(final MongoSession mongoSession) {
        super(mongoSession);
    }

    /**
     * constructor
     *
     * @param mongoSession   MongoSession
     * @param collectionName collectionName
     */
    public MongoReadonlyRepositoryImpl(final MongoSession mongoSession, final String collectionName) {
        super(mongoSession, collectionName);
    }

    /**
     * constructor
     *
     * @param mongoSession MongoSession
     * @param mongoOptions MongoOptions
     */
    public MongoReadonlyRepositoryImpl(final MongoSession mongoSession, final MongoOptions mongoOptions) {
        super(mongoSession, mongoOptions);
    }


    /**
     * constructor
     *
     * @param mongoSession   MongoSession
     * @param mongoOptions   MongoOptions
     * @param collectionName collectionName
     */
    public MongoReadonlyRepositoryImpl(final MongoSession mongoSession, final MongoOptions mongoOptions, final String collectionName) {
        super(mongoSession, mongoOptions, collectionName);
    }

    //#endregion

//    /**
//     * 数量
//     *
//     * @param filter 查询条件
//     * @return count
//     */
//    @Override
//    public long count(final Bson filter) {
//        return this.count(filter, (Bson) null, (ReadPreference) null);
//    }
//
//    /**
//     * 数量
//     *
//     * @param filter         查询条件
//     * @param hint           hint索引
//     * @param readPreference 访问设置
//     * @return count
//     */
//    @Override
//    public long count(final Bson filter, final Bson hint
//            , final ReadPreference readPreference) {
//
//        return this.count(filter, 0, 0, hint, readPreference);
//    }
//
//    /**
//     * 数量
//     *
//     * @param filter         查询条件
//     * @param limit          limit
//     * @param skip           skip
//     * @param hint           hint索引
//     * @param readPreference 访问设置
//     * @return count
//     */
//    @Override
//    public long count(final Bson filter, int limit, int skip, final Bson hint
//            , final ReadPreference readPreference) {
//
//        CountOptions options = (CountOptions) new CountOptions()
//                .limit(limit)
//                .skip(skip)
//                .filter(filter)
//                .hint(hint)
//                .readPreference(readPreference);
//
//        return this.count(options);
//    }
//
//    /**
//     * 数量
//     *
//     * @param countOptions CountOptions
//     * @return count
//     */
//    @Override
//    public long count(final CountOptions countOptions) {
//        return this.doCount(countOptions);
//    }
//
//    /**
//     * 是否存在
//     *
//     * @param filter conditions
//     * @return exists
//     */
//    @Override
//    public boolean exists(final Bson filter) {
//        return this.exists(filter, null, null);
//    }
//
//    /**
//     * 是否存在
//     *
//     * @param filter         conditions
//     * @param hint           hint
//     * @param readPreference {{@link ReadPreference}}
//     * @return exists
//     */
//    @Override
//    public boolean exists(final Bson filter, final Bson hint
//            , final ReadPreference readPreference) {
//
//        Bson _filter = filter;
//        if (_filter == null) {
//            _filter = Filters.empty();
//        }
//
//        List<String> includeFields = new ArrayList<>(1);
//        includeFields.add(BsonConstant.PRIMARY_KEY_NAME);
//
//        return this.get(_filter, includeFields, null, hint, readPreference) != null;
//    }
//
//    /**
//     * 是否存在
//     *
//     * @param existsOptions ExistsOptions
//     * @return exists
//     */
//    @Override
//    public boolean exists(final ExistsOptions existsOptions) {
//        return this.exists(existsOptions.filter(), existsOptions.hint(), existsOptions.readPreference());
//    }


    //region ext

    @Override
    public <TResult> TResult findOne(FindOptions findOptions, Class<TResult> resultClass) {
        return doFindOne(findOptions, resultClass);
    }

    @Override
    public <TResult> List<TResult> findList(FindOptions findOptions, Class<TResult> resultClass) {
        return doFindList(findOptions, resultClass);
    }

    //#endregion

    //region protected

    protected TEntity doFindOne(final FindOptions options) {
        return this.doFindOne(options, entityInformation.getEntityType());
    }

    protected <TResult> TResult doFindOne(final FindOptions options, Class<TResult> resultClass) {
        return this.doFind(options, resultClass).first();
    }

    protected List<TEntity> doFindList(final FindOptions options) {
        return this.doFindList(options, entityInformation.getEntityType());
    }

    protected <TResult> List<TResult> doFindList(final FindOptions options, Class<TResult> resultClass) {
        FindIterable<TResult> result = doFind(options, resultClass);

        ArrayList<TResult> list = new ArrayList<>();
        for (TResult entity : result) {
            list.add(entity);
        }

        return list;
    }

    protected long doCount(final CountOptions options) {

        if (options.filter() == null) {
            options.filter(Filters.empty());
        }

        callGlobalInterceptors(PreFind.class, null, options);

        return super.getCollection(options.readPreference()).countDocuments(options.filter(),
                new com.mongodb.client.model.CountOptions()
                        .hint(options.hint())
                        .limit(options.limit())
                        .skip(options.skip())
        );
    }

    protected boolean doExists(final ExistsOptions options) {

        Bson _filter = options.filter();
        if (_filter == null) {
            _filter = Filters.empty();
        }

        List<String> includeFields = new ArrayList<>(1);
        includeFields.add(BsonConstant.PRIMARY_KEY_NAME);

        return this.findOne(_filter, includeFields, null, options.hint(), options.readPreference()) != null;
    }

    @Override
    public FindProxy<TEntity, TKey, TEntity, List<TEntity>, Long, Boolean> findProxy() {
        return proxy;
    }

    private final FindProxy<TEntity, TKey, TEntity, List<TEntity>, Long, Boolean> proxy =
            new FindProxy<>() {
                @Override
                protected EntityInformation<TEntity, TKey> getEntityInformation() {
                    return MongoReadonlyRepositoryImpl.this.entityInformation;
                }

                @Override
                protected TEntity doFindOne(FindOptions options) {
                    return MongoReadonlyRepositoryImpl.this.doFindOne(options);
                }

                @Override
                protected List<TEntity> doFindList(FindOptions options) {
                    return MongoReadonlyRepositoryImpl.this.doFindList(options);
                }

                @Override
                protected Long doCount(CountOptions options) {
                    return MongoReadonlyRepositoryImpl.this.doCount(options);
                }

                @Override
                protected Boolean doExists(ExistsOptions options) {
                    return MongoReadonlyRepositoryImpl.this.doExists(options);
                }
            };

    //endregion

}
