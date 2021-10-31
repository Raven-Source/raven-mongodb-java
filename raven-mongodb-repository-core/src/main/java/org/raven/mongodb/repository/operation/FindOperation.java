package org.raven.mongodb.repository.operation;

import com.mongodb.Function;
import com.mongodb.ReadPreference;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.raven.mongodb.repository.EntityInformation;
import org.raven.mongodb.repository.FindOptions;
import org.raven.mongodb.repository.contants.BsonConstant;
import org.raven.mongodb.repository.query.FieldNest;
import org.raven.mongodb.repository.query.FilterBuilder;
import org.raven.mongodb.repository.query.HintBuilder;
import org.raven.mongodb.repository.query.SortBuilder;

import java.util.List;
import java.util.Objects;

/**
 * @author by yanfeng
 * date 2021/10/30 20:49
 */
public interface FindOperation<TEntity, TKey, GetResult, ListResult> {

    //#region get

    /**
     * 根据id获取实体
     *
     * @param id
     * @return
     */
    default GetResult get(final TKey id) {
        return this.get(id, null);
    }

    /**
     * 根据id获取实体
     *
     * @param id
     * @param includeFields 查询字段
     * @return
     */
    default GetResult get(final TKey id, final List<String> includeFields) {
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
    default GetResult get(final TKey id, final List<String> includeFields
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
    default GetResult get(final Bson filter) {
        return this.get(filter, null);
    }

    /**
     * 根据条件获取实体
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @return
     */
    default GetResult get(final Bson filter, final List<String> includeFields) {
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
    default GetResult get(final Bson filter, final List<String> includeFields, final Bson sort) {
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
    default GetResult get(final Bson filter, final List<String> includeFields, final Bson sort, final Bson hint
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
    default GetResult get(final Function<FilterBuilder<TEntity>, Bson> filterBuilder) {
        return this.get(filterBuilder, null);
    }

    /**
     * 根据条件获取实体
     *
     * @param filterBuilder 查询条件
     * @return
     */
    default GetResult get(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                          final Function<FieldNest, List<String>> fieldNestList) {
        return this.get(filterBuilder, fieldNestList, null);
    }

    /**
     * 根据条件获取实体
     *
     * @param filterBuilder 查询条件
     * @return
     */
    default GetResult get(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
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
    default GetResult get(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
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
    default GetResult get(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                          final Function<FieldNest, List<String>> fieldNestList,
                          final Function<SortBuilder<TEntity>, Bson> sortBuilder,
                          final Function<HintBuilder<TEntity>, Bson> hintBuilder,
                          final ReadPreference readPreference) {

        final FindOptions findOptions = new FindOptions();
        if (!Objects.isNull(filterBuilder)) {
            findOptions.filter(
                    filterBuilder.apply(FilterBuilder.empty(findProxy().getEntityInformation().getEntityType()))
            );
        }
        if (!Objects.isNull(fieldNestList)) {
            findOptions.includeFields(
                    fieldNestList.apply(FieldNest.empty())
            );
        }
        if (!Objects.isNull(sortBuilder)) {
            findOptions.sort(
                    sortBuilder.apply(SortBuilder.empty(findProxy().getEntityInformation().getEntityType()))
            );
        }
        if (!Objects.isNull(hintBuilder)) {
            findOptions.hint(
                    hintBuilder.apply(HintBuilder.empty(findProxy().getEntityInformation().getEntityType()))
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
    default GetResult get(final FindOptions findOptions) {
        return findProxy().doFindOne(findOptions);
    }

    //#endregion

    //#region getList

    /**
     * 根据条件获取获取列表
     *
     * @param filter 查询条件
     * @return
     */
    default ListResult getList(final Bson filter) {
        return this.getList(filter, null);
    }

    /**
     * 根据条件获取获取列表
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @return
     */
    default ListResult getList(final Bson filter, final List<String> includeFields) {
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
    default ListResult getList(final Bson filter, final List<String> includeFields, final Bson sort) {
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
    default ListResult getList(final Bson filter, final List<String> includeFields, final Bson sort
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
    default ListResult getList(final Bson filter, final List<String> includeFields, final Bson sort
            , final int limit, final int skip) {
        return this.getList(filter, includeFields, sort, limit, skip, null, null);
    }

    default ListResult getList(final Function<FilterBuilder<TEntity>, Bson> filterBuilder) {
        return this.getList(filterBuilder, null);
    }

    default ListResult getList(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                               final Function<FieldNest, List<String>> fieldNestList) {
        return this.getList(filterBuilder, fieldNestList, null);
    }

    default ListResult getList(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                               final Function<FieldNest, List<String>> fieldNestList,
                               final Function<SortBuilder<TEntity>, Bson> sortBuilder) {
        return this.getList(filterBuilder, fieldNestList, sortBuilder, 0, 0);
    }

    default ListResult getList(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                               final Function<FieldNest, List<String>> fieldNestList,
                               final Function<SortBuilder<TEntity>, Bson> sortBuilder,
                               int limit, int skip) {
        return this.getList(filterBuilder, fieldNestList, sortBuilder, limit, skip, null);
    }

    default ListResult getList(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                               final Function<FieldNest, List<String>> fieldNestList,
                               final Function<SortBuilder<TEntity>, Bson> sortBuilder,
                               int limit, int skip,
                               final Function<HintBuilder<TEntity>, Bson> hintBuilder) {
        return this.getList(filterBuilder, fieldNestList, sortBuilder, limit, skip, hintBuilder, null);
    }

    default ListResult getList(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                               final Function<FieldNest, List<String>> fieldNestList,
                               final Function<SortBuilder<TEntity>, Bson> sortBuilder,
                               int limit, int skip,
                               final Function<HintBuilder<TEntity>, Bson> hintBuilder,
                               final ReadPreference readPreference) {

        final FindOptions findOptions = new FindOptions();
        if (!Objects.isNull(filterBuilder)) {
            findOptions.filter(
                    filterBuilder.apply(FilterBuilder.empty(findProxy().getEntityInformation().getEntityType()))
            );
        }
        if (!Objects.isNull(fieldNestList)) {
            findOptions.includeFields(
                    fieldNestList.apply(FieldNest.empty())
            );
        }
        if (!Objects.isNull(sortBuilder)) {
            findOptions.sort(
                    sortBuilder.apply(SortBuilder.empty(findProxy().getEntityInformation().getEntityType()))
            );
        }
        findOptions.limit(limit);
        findOptions.skip(skip);
        if (!Objects.isNull(hintBuilder)) {
            findOptions.hint(
                    hintBuilder.apply(HintBuilder.empty(findProxy().getEntityInformation().getEntityType()))
            );
        }
        findOptions.readPreference(readPreference);

        return this.getList(findOptions);

    }

    /**
     * 根据条件获取获取列表
     *
     * @param findOptions FindOptions
     * @return
     */
    default ListResult getList(final FindOptions findOptions) {
        return findProxy().doFindList(findOptions);
    }

    //#endregion


    FindProxy<TEntity, TKey, GetResult, ListResult> findProxy();

    abstract class FindProxy<TEntity, TKey, GetResult, ListResult> {

        protected abstract EntityInformation<TEntity, TKey> getEntityInformation();

        protected abstract GetResult doFindOne(final FindOptions options);

        protected abstract ListResult doFindList(final FindOptions options);
    }

}