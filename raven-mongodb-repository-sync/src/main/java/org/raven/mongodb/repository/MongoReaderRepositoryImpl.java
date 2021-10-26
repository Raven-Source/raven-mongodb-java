package org.raven.mongodb.repository;

import com.mongodb.Function;
import com.mongodb.ReadPreference;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.annotations.PreFind;
import org.raven.mongodb.repository.contants.BsonConstant;
import org.raven.mongodb.repository.query.FieldNest;
import org.raven.mongodb.repository.query.FilterBuilder;
import org.raven.mongodb.repository.query.HintBuilder;
import org.raven.mongodb.repository.query.SortBuilder;
import org.raven.mongodb.repository.spi.IdGenerator;
import org.raven.mongodb.repository.spi.IdGeneratorProvider;
import org.raven.mongodb.repository.spi.Sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
     * @param mongoOptions   mongoOptions
     * @param collectionName collectionName
     */
    public MongoReaderRepositoryImpl(final MongoOptions mongoOptions, final String collectionName) {
        super(mongoOptions, collectionName);
    }

    //#endregion

    //#region get

    /**
     * 根据id获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TEntity get(final TKey id) {
        return this.get(id, null);
    }

    /**
     * 根据id获取实体
     *
     * @param id
     * @param includeFields 查询字段
     * @return
     */
    @Override
    public TEntity get(final TKey id, final List<String> includeFields) {
        return this.get(id, includeFields, null);
    }

    /**
     * 根据id获取实体
     *
     * @param id
     * @param includeFields  查询字段
     * @param readPreference 访问设置
     * @return
     */
    @Override
    public TEntity get(final TKey id, final List<String> includeFields
            , final ReadPreference readPreference) {

        Bson filter = Filters.eq(BsonConstant.PRIMARY_KEY_NAME, id);

        return this.get(filter, includeFields, null, null, readPreference);
    }

    /**
     * 根据条件获取实体
     *
     * @param filter 查询条件
     * @return
     */
    @Override
    public TEntity get(final Bson filter) {
        return this.get(filter, null);
    }

    /**
     * 根据条件获取实体
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @return
     */
    @Override
    public TEntity get(final Bson filter, final List<String> includeFields) {
        return this.get(filter, includeFields, null);
    }

    /**
     * 根据条件获取实体
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @param sort          排序
     * @return
     */
    @Override
    public TEntity get(final Bson filter, final List<String> includeFields, final Bson sort) {
        return this.get(filter, includeFields, sort, null, null);

    }

    /**
     * 根据条件获取实体
     *
     * @param filter         查询条件
     * @param includeFields  查询字段
     * @param sort           排序
     * @param hint           hint索引
     * @param readPreference 访问设置
     * @return
     */
    @Override
    public TEntity get(final Bson filter, final List<String> includeFields, final Bson sort, final Bson hint
            , final ReadPreference readPreference) {

        FindOptions options = new FindOptions();
        options.filter(filter);
        options.hint(hint);
        options.includeFields(includeFields);
        options.readPreference(readPreference);
        options.limit(1);
        options.skip(0);
        options.sort(sort);

        return this.get(options);
    }

    /**
     * 根据条件获取实体
     *
     * @param filterBuilder 查询条件
     * @return
     */
    @Override
    public TEntity get(final Function<FilterBuilder<TEntity>, Bson> filterBuilder) {
        return this.get(filterBuilder, null);
    }

    /**
     * 根据条件获取实体
     *
     * @param filterBuilder 查询条件
     * @return
     */
    @Override
    public TEntity get(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                       final Function<FieldNest, List<String>> fieldNestList) {
        return this.get(filterBuilder, fieldNestList, null);
    }

    /**
     * 根据条件获取实体
     *
     * @param filterBuilder 查询条件
     * @return
     */
    @Override
    public TEntity get(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                       final Function<FieldNest, List<String>> fieldNestList,
                       final Function<SortBuilder<TEntity>, Bson> sortBuilder) {
        return this.get(filterBuilder, fieldNestList, sortBuilder, null);

    }

    /**
     * 根据条件获取实体
     *
     * @param filterBuilder 查询条件
     * @return
     */
    @Override
    public TEntity get(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                       final Function<FieldNest, List<String>> fieldNestList,
                       final Function<SortBuilder<TEntity>, Bson> sortBuilder,
                       final Function<HintBuilder<TEntity>, Bson> hintBuilder) {
        return this.get(filterBuilder, fieldNestList, sortBuilder, hintBuilder, null);

    }

    /**
     * 根据条件获取实体
     *
     * @param filterBuilder 查询条件
     * @return
     */
    @Override
    public TEntity get(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                       final Function<FieldNest, List<String>> fieldNestList,
                       final Function<SortBuilder<TEntity>, Bson> sortBuilder,
                       final Function<HintBuilder<TEntity>, Bson> hintBuilder,
                       final ReadPreference readPreference) {

        final FindOptions findOptions = new FindOptions();
        if (!Objects.isNull(filterBuilder)) {
            findOptions.filter(
                    filterBuilder.apply(FilterBuilder.empty(entityInformation.getEntityType()))
            );
        }
        if (!Objects.isNull(fieldNestList)) {
            findOptions.includeFields(
                    fieldNestList.apply(FieldNest.empty())
            );
        }
        if (!Objects.isNull(sortBuilder)) {
            findOptions.sort(
                    sortBuilder.apply(SortBuilder.empty(entityInformation.getEntityType()))
            );
        }
        if (!Objects.isNull(hintBuilder)) {
            findOptions.hint(
                    hintBuilder.apply(HintBuilder.empty(entityInformation.getEntityType()))
            );
        }
        findOptions.readPreference(readPreference);

        return this.get(findOptions);
    }

    /**
     * 根据条件获取实体
     *
     * @param findOptions
     * @return
     */
    @Override
    public TEntity get(final FindOptions findOptions) {
        return this.doFind(findOptions).first();
    }

    //#endregion

    //#region getList

    /**
     * 根据条件获取获取列表
     *
     * @param filter 查询条件
     * @return
     */
    @Override
    public List<TEntity> getList(final Bson filter) {
        return this.getList(filter, null);
    }

    /**
     * 根据条件获取获取列表
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @return
     */
    @Override
    public List<TEntity> getList(final Bson filter, final List<String> includeFields) {
        return this.getList(filter, includeFields, null);
    }

    /**
     * 根据条件获取获取列表
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @param sort          排序
     * @return
     */
    @Override
    public List<TEntity> getList(final Bson filter, final List<String> includeFields, final Bson sort) {
        return this.getList(filter, includeFields, sort, 0, 0);
    }

    /**
     * 根据条件获取获取列表
     *
     * @param filter         查询条件
     * @param includeFields  查询字段
     * @param sort           排序
     * @param limit
     * @param skip
     * @param hint           hint索引
     * @param readPreference 访问设置
     * @return
     */
    @Override
    public List<TEntity> getList(final Bson filter, final List<String> includeFields, final Bson sort
            , final int limit, final int skip
            , final Bson hint
            , final ReadPreference readPreference) {

        FindOptions options = new FindOptions();
        options.filter(filter);
        options.hint(hint);
        options.includeFields(includeFields);
        options.readPreference(readPreference);
        options.limit(limit);
        options.skip(skip);
        options.sort(sort);

        return this.getList(options);
    }

    /**
     * 根据条件获取获取列表
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @param sort          排序
     * @param limit
     * @param skip
     * @return
     */
    @Override
    public List<TEntity> getList(final Bson filter, final List<String> includeFields, final Bson sort
            , final int limit, final int skip) {
        return this.getList(filter, includeFields, sort, limit, skip, null, null);
    }

    @Override
    public List<TEntity> getList(final Function<FilterBuilder<TEntity>, Bson> filterBuilder) {
        return this.getList(filterBuilder, null);
    }

    @Override
    public List<TEntity> getList(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                                 final Function<FieldNest, List<String>> fieldNestList) {
        return this.getList(filterBuilder, fieldNestList, null);
    }

    @Override
    public List<TEntity> getList(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                                 final Function<FieldNest, List<String>> fieldNestList,
                                 final Function<SortBuilder<TEntity>, Bson> sortBuilder) {
        return this.getList(filterBuilder, fieldNestList, sortBuilder, 0, 0);
    }

    @Override
    public List<TEntity> getList(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                                 final Function<FieldNest, List<String>> fieldNestList,
                                 final Function<SortBuilder<TEntity>, Bson> sortBuilder,
                                 int limit, int skip) {
        return this.getList(filterBuilder, fieldNestList, sortBuilder, limit, skip, null);
    }

    @Override
    public List<TEntity> getList(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                                 final Function<FieldNest, List<String>> fieldNestList,
                                 final Function<SortBuilder<TEntity>, Bson> sortBuilder,
                                 int limit, int skip,
                                 final Function<HintBuilder<TEntity>, Bson> hintBuilder) {
        return this.getList(filterBuilder, fieldNestList, sortBuilder, limit, skip, hintBuilder, null);
    }

    @Override
    public List<TEntity> getList(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                                 final Function<FieldNest, List<String>> fieldNestList,
                                 final Function<SortBuilder<TEntity>, Bson> sortBuilder,
                                 int limit, int skip,
                                 final Function<HintBuilder<TEntity>, Bson> hintBuilder,
                                 final ReadPreference readPreference) {

        final FindOptions findOptions = new FindOptions();
        if (!Objects.isNull(filterBuilder)) {
            findOptions.filter(
                    filterBuilder.apply(FilterBuilder.empty(entityInformation.getEntityType()))
            );
        }
        if (!Objects.isNull(fieldNestList)) {
            findOptions.includeFields(
                    fieldNestList.apply(FieldNest.empty())
            );
        }
        if (!Objects.isNull(sortBuilder)) {
            findOptions.sort(
                    sortBuilder.apply(SortBuilder.empty(entityInformation.getEntityType()))
            );
        }
        findOptions.limit(limit);
        findOptions.skip(skip);
        if (!Objects.isNull(hintBuilder)) {
            findOptions.hint(
                    hintBuilder.apply(HintBuilder.empty(entityInformation.getEntityType()))
            );
        }
        findOptions.readPreference(readPreference);

        return this.getList(findOptions);

    }

    /**
     * 根据条件获取获取列表
     *
     * @param options FindOptions
     * @return
     */
    @Override
    public List<TEntity> getList(final FindOptions options) {

        FindIterable<TEntity> result = doFind(options);

        ArrayList<TEntity> list = new ArrayList<TEntity>();
        for (TEntity entity : result) {
            list.add(entity);
        }

        return list;
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
        return this.count(filter, null, null);
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

    //endregion

}
