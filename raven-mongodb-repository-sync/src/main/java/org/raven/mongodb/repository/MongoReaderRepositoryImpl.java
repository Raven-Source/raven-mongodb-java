package org.raven.mongodb.repository;

import com.mongodb.ReadPreference;
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
 * @param <TEntity>
 * @param <TKey>
 * @author yi.liang
 * @since JDK11
 */
public class MongoReaderRepositoryImpl<TEntity extends Entity<TKey>, TKey>
        extends AbstractMongoBaseRepository<TEntity, TKey>
        implements MongoReaderRepository<TEntity, TKey> {

    //#region constructor

    /**
     * constructor
     *
     * @param mongoSession        mongoSession
     * @param collectionName      collectionName
     * @param idGeneratorProvider idGeneratorProvider
     */
    public MongoReaderRepositoryImpl(final MongoSession mongoSession, final String collectionName, final Sequence sequence
            , final IdGeneratorProvider<IdGenerator<TKey>, MongoDatabase> idGeneratorProvider) {

        super(mongoSession, collectionName, sequence, idGeneratorProvider);
    }

    /**
     * constructor
     *
     * @param mongoSession mongoSession
     */
    public MongoReaderRepositoryImpl(final MongoSession mongoSession) {
        super(mongoSession);
    }

    /**
     * constructor
     *
     * @param mongoSession   mongoSession
     * @param collectionName collectionName
     */
    public MongoReaderRepositoryImpl(final MongoSession mongoSession, final String collectionName) {
        super(mongoSession, collectionName);
    }

    /**
     * constructor
     *
     * @param mongoSession mongoSession
     */
    public MongoReaderRepositoryImpl(final MongoSession mongoSession, final MongoOptions mongoOptions) {
        super(mongoSession, mongoOptions);
    }


    /**
     * constructor
     *
     * @param mongoSession   mongoSession
     * @param collectionName collectionName
     */
    public MongoReaderRepositoryImpl(final MongoSession mongoSession, final MongoOptions mongoOptions, final String collectionName) {
        super(mongoSession, mongoOptions, collectionName);
    }

    //#endregion

    /**
     * 数量
     *
     * @param filter 查询条件
     * @return
     */
    @Override
    public long count(final Bson filter) {
        return this.count(filter, (Bson) null, (ReadPreference) null);
    }

    /**
     * 数量
     *
     * @param filter         查询条件
     * @param hint           hint索引
     * @param readPreference 访问设置
     * @return
     */
    @Override
    public long count(final Bson filter, final Bson hint
            , final ReadPreference readPreference) {

        return this.count(filter, 0, 0, hint, readPreference);
    }

    /**
     * 数量
     *
     * @param filter         查询条件
     * @param limit          limit
     * @param skip           skip
     * @param hint           hint索引
     * @param readPreference 访问设置
     * @return
     */
    @Override
    public long count(final Bson filter, int limit, int skip, final Bson hint
            , final ReadPreference readPreference) {

        CountOptions options = (CountOptions) new CountOptions()
                .limit(limit)
                .skip(skip)
                .filter(filter)
                .hint(hint)
                .readPreference(readPreference);

        return this.count(options);
    }

    /**
     * 数量
     *
     * @param countOptions
     * @return
     */
    @Override
    public long count(final CountOptions countOptions) {
        return this.doCount(countOptions);
    }

    /**
     * 是否存在
     *
     * @param filter
     * @return
     */
    @Override
    public boolean exists(final Bson filter) {
        return this.exists(filter, null, null);
    }

    /**
     * 是否存在
     *
     * @param filter
     * @param hint
     * @param readPreference
     * @return
     */
    @Override
    public boolean exists(final Bson filter, final Bson hint
            , final ReadPreference readPreference) {

        Bson _filter = filter;
        if (_filter == null) {
            _filter = Filters.empty();
        }

        List<String> includeFields = new ArrayList<>(1);
        includeFields.add(BsonConstant.PRIMARY_KEY_NAME);

        return this.get(_filter, includeFields, null, hint, readPreference) != null;
    }

    /**
     * 是否存在
     *
     * @param existsOptions ExistsOptions
     * @return
     */
    @Override
    public boolean exists(final ExistsOptions existsOptions) {
        return this.exists(existsOptions.filter(), existsOptions.hint(), existsOptions.readPreference());
    }


    //region protected

    protected TEntity doFindOne(final FindOptions options) {
        return this.doFind(options).first();
    }


    protected List<TEntity> doFindList(final FindOptions options) {
        FindIterable<TEntity> result = doFind(options);

        ArrayList<TEntity> list = new ArrayList<TEntity>();
        for (TEntity entity : result) {
            list.add(entity);
        }

        return list;
    }

    protected FindIterable<TEntity> doFind(final FindOptions options) {

        if (options.filter() == null) {
            options.filter(Filters.empty());
        }

        Bson projection = null;
        if (options.includeFields() != null) {
            projection = BsonUtils.includeFields(options.includeFields());
        }

        callGlobalInterceptors(PreFind.class, null, options);

        FindIterable<TEntity> result = super.getCollection(options.readPreference()).find(options.filter(), entityInformation.getEntityType());
        result = super.findOptions(result, projection, options.sort(), options.limit(), options.skip(), options.hint());

        return result;
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

    @Override
    public FindProxy<TEntity, TKey, TEntity, List<TEntity>> findProxy() {
        return new FindProxy<>() {
            @Override
            protected EntityInformation<TEntity, TKey> getEntityInformation() {
                return MongoReaderRepositoryImpl.this.entityInformation;
            }

            @Override
            protected TEntity doFindOne(FindOptions options) {
                return MongoReaderRepositoryImpl.this.doFindOne(options);
            }

            @Override
            protected List<TEntity> doFindList(FindOptions options) {
                return MongoReaderRepositoryImpl.this.doFindList(options);
            }
        };
    }

    //endregion

}
