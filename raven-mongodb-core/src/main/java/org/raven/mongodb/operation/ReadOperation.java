package org.raven.mongodb.operation;

import com.mongodb.Function;
import com.mongodb.ReadPreference;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.raven.mongodb.CountOptions;
import org.raven.mongodb.contants.BsonConstant;
import org.raven.mongodb.ExistsOptions;
import org.raven.mongodb.FindOptions;
import org.raven.mongodb.criteria.FieldNest;
import org.raven.mongodb.criteria.FilterBuilder;
import org.raven.mongodb.criteria.HintBuilder;
import org.raven.mongodb.criteria.SortBuilder;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * @author by yanfeng
 * date 2021/10/30 20:49
 */
public interface ReadOperation<TEntity, TKey, TSingleResult, TListResult, TCountResult, TExistsResult> {

    //#region get

    /**
     * 根据id获取实体
     *
     * @param id ID
     * @return Result
     */
    default TSingleResult findOne(final TKey id) {
        return this.findOne(id, null);
    }

    /**
     * 根据id获取实体
     *
     * @param id            id
     * @param includeFields 查询字段
     * @return Result
     */
    default TSingleResult findOne(final TKey id, final List<String> includeFields) {
        return this.findOne(id, includeFields, null);
    }

    /**
     * 根据id获取实体
     *
     * @param id             id
     * @param includeFields  查询字段
     * @param readPreference 访问设置
     * @return Result
     */
    default TSingleResult findOne(final TKey id, final List<String> includeFields
            , final ReadPreference readPreference) {

        Bson filter = Filters.eq(BsonConstant.PRIMARY_KEY_NAME, id);

        return this.findOne(filter, includeFields, null, null, readPreference);
    }

    /**
     * 根据条件获取实体
     *
     * @param filter 查询条件
     * @return Result
     */
    default TSingleResult findOne(final Bson filter) {
        return this.findOne(filter, null);
    }

    /**
     * 根据条件获取实体
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @return Result
     */
    default TSingleResult findOne(final Bson filter, final List<String> includeFields) {
        return this.findOne(filter, includeFields, null);
    }

    /**
     * 根据条件获取实体
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @param sort          排序
     * @return Result
     */
    default TSingleResult findOne(final Bson filter, final List<String> includeFields, final Bson sort) {
        return this.findOne(filter, includeFields, sort, null, null);

    }

    /**
     * 根据条件获取实体
     *
     * @param filter         查询条件
     * @param includeFields  查询字段
     * @param sort           排序
     * @param hint           hint索引
     * @param readPreference 访问设置
     * @return Result
     */
    default TSingleResult findOne(final Bson filter, final List<String> includeFields, final Bson sort, final Bson hint
            , final ReadPreference readPreference) {

        FindOptions options = new FindOptions();
        options.filter(filter);
        options.hint(hint);
        options.includeFields(includeFields);
        options.readPreference(readPreference);
        options.limit(1);
        options.skip(0);
        options.sort(sort);

        return this.findOne(options);
    }

    /**
     * 根据条件获取实体
     *
     * @param filterBuilder {{@link FilterBuilder}}
     * @return Result
     */
    default TSingleResult findOne(final Function<FilterBuilder<TEntity>, Bson> filterBuilder) {
        return this.findOne(filterBuilder, null);
    }

    /**
     * 根据条件获取实体
     *
     * @param filterBuilder {{@link FilterBuilder}}
     * @param fieldNestList {{@link FieldNest}}
     * @return Result
     */
    default TSingleResult findOne(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                                  final Function<FieldNest, List<String>> fieldNestList) {
        return this.findOne(filterBuilder, fieldNestList, null);
    }

    /**
     * 根据条件获取实体
     *
     * @param filterBuilder {{@link FilterBuilder}}
     * @param fieldNestList {{@link FieldNest}}
     * @param sortBuilder   {{@link SortBuilder}}
     * @return Result
     */
    default TSingleResult findOne(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                                  final Function<FieldNest, List<String>> fieldNestList,
                                  final Function<SortBuilder<TEntity>, Bson> sortBuilder) {
        return this.findOne(filterBuilder, fieldNestList, sortBuilder, null);

    }

    /**
     * 根据条件获取实体
     *
     * @param filterBuilder {{@link FilterBuilder}}
     * @param fieldNestList {{@link FieldNest}}
     * @param sortBuilder   {{@link SortBuilder}}
     * @param hintBuilder   {{@link HintBuilder}}
     * @return Result
     */
    default TSingleResult findOne(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                                  final Function<FieldNest, List<String>> fieldNestList,
                                  final Function<SortBuilder<TEntity>, Bson> sortBuilder,
                                  final Function<HintBuilder<TEntity>, Bson> hintBuilder) {
        return this.findOne(filterBuilder, fieldNestList, sortBuilder, hintBuilder, null);

    }

    /**
     * 根据条件获取实体
     *
     * @param filterBuilder  {{@link FilterBuilder}}
     * @param fieldNestList  {{@link FieldNest}}
     * @param sortBuilder    {{@link SortBuilder}}
     * @param hintBuilder    {{@link HintBuilder}}
     * @param readPreference {{@link ReadPreference}}
     * @return Result
     */
    default TSingleResult findOne(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                                  final Function<FieldNest, List<String>> fieldNestList,
                                  final Function<SortBuilder<TEntity>, Bson> sortBuilder,
                                  final Function<HintBuilder<TEntity>, Bson> hintBuilder,
                                  final ReadPreference readPreference) {

        final FindOptions findOptions = createFindOptions(filterBuilder, fieldNestList, sortBuilder, 0, 0, hintBuilder, readPreference);

        return this.findOne(findOptions);
    }

    /**
     * 根据条件获取实体
     *
     * @param findOptions 查询条件
     * @return Result
     */
    default TSingleResult findOne(final FindOptions findOptions) {
        return findExecutor().doFindOne(findOptions);
    }

    //#endregion

    //#region getList

    /**
     * 根据条件获取获取列表
     *
     * @param filter 查询条件
     * @return Result
     */
    default TListResult findList(final Bson filter) {
        return this.findList(filter, null);
    }

    /**
     * 根据条件获取获取列表
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @return Result
     */
    default TListResult findList(final Bson filter, final List<String> includeFields) {
        return this.findList(filter, includeFields, null);
    }

    /**
     * 根据条件获取获取列表
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @param sort          排序
     * @return Result
     */
    default TListResult findList(final Bson filter, final List<String> includeFields, final Bson sort) {
        return this.findList(filter, includeFields, sort, 0, 0);
    }

    /**
     * 根据条件获取获取列表
     *
     * @param filter         查询条件
     * @param includeFields  查询字段
     * @param sort           排序
     * @param limit          limit
     * @param skip           skip
     * @param hint           hint索引
     * @param readPreference 访问设置
     * @return Result
     */
    default TListResult findList(final Bson filter, final List<String> includeFields, final Bson sort
            , final int limit, final int skip
            , final Bson hint
            , final @Nullable ReadPreference readPreference) {

        FindOptions options = new FindOptions();
        options.filter(filter);
        options.hint(hint);
        options.includeFields(includeFields);

        if (readPreference != null) {
            options.readPreference(readPreference);
        }
        options.limit(limit);
        options.skip(skip);
        options.sort(sort);

        return this.findList(options);
    }


    /**
     * 数量
     *
     * @param filter 查询条件
     * @return count
     */
    default TCountResult count(Bson filter) {
        return this.count(filter, null, null);
    }

    /**
     * 数量
     *
     * @param filter         查询条件
     * @param hint           hint索引
     * @param readPreference 访问设置
     * @return count
     */
    default TCountResult count(Bson filter, Bson hint
            , @Nullable ReadPreference readPreference) {

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
     * @return count
     */
    default TCountResult count(Bson filter, int limit, int skip, Bson hint
            , @Nullable ReadPreference readPreference) {

        CountOptions options = (CountOptions) new CountOptions()
                .limit(limit)
                .skip(skip)
                .filter(filter)
                .hint(hint);

        if (readPreference != null) {

            options.readPreference(readPreference);
        }

        return this.count(options);
    }


    /**
     * 数量
     *
     * @param countOptions CountOptions
     * @return count
     */
    default TCountResult count(CountOptions countOptions) {
        return findExecutor().doCount(countOptions);
    }

    /**
     * 是否存在
     *
     * @param filter conditions
     * @return exists
     */
    default TExistsResult exists(Bson filter) {
        return this.exists(filter, null, null);
    }

    /**
     * 是否存在
     *
     * @param filter         conditions
     * @param hint           hint
     * @param readPreference ReadPreference
     * @return exists
     */
    default TExistsResult exists(Bson filter, Bson hint
            , @Nullable ReadPreference readPreference) {
        ExistsOptions options = (ExistsOptions) new ExistsOptions()
                .filter(filter)
                .hint(hint);

        if (readPreference != null) {

            options.readPreference(readPreference);
        }

        return this.exists(options);
    }

    /**
     * 是否存在
     *
     * @param existsOptions ExistsOptions
     * @return exists
     */
    default TExistsResult exists(ExistsOptions existsOptions) {
        return findExecutor().doExists(existsOptions);
    }

    /**
     * 根据条件获取获取列表
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @param sort          排序
     * @param limit         limit
     * @param skip          skip
     * @return Result
     */
    default TListResult findList(final Bson filter, final List<String> includeFields, final Bson sort
            , final int limit, final int skip) {
        return this.findList(filter, includeFields, sort, limit, skip, null, null);
    }

    default TListResult findList(final Function<FilterBuilder<TEntity>, Bson> filterBuilder) {
        return this.findList(filterBuilder, null);
    }

    default TListResult findList(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                                 final Function<FieldNest, List<String>> fieldNestList) {
        return this.findList(filterBuilder, fieldNestList, null);
    }

    default TListResult findList(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                                 final Function<FieldNest, List<String>> fieldNestList,
                                 final Function<SortBuilder<TEntity>, Bson> sortBuilder) {
        return this.findList(filterBuilder, fieldNestList, sortBuilder, 0, 0);
    }

    default TListResult findList(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                                 final Function<FieldNest, List<String>> fieldNestList,
                                 final Function<SortBuilder<TEntity>, Bson> sortBuilder,
                                 int limit, int skip) {
        return this.findList(filterBuilder, fieldNestList, sortBuilder, limit, skip, null);
    }

    default TListResult findList(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                                 final Function<FieldNest, List<String>> fieldNestList,
                                 final Function<SortBuilder<TEntity>, Bson> sortBuilder,
                                 int limit, int skip,
                                 final Function<HintBuilder<TEntity>, Bson> hintBuilder) {
        return this.findList(filterBuilder, fieldNestList, sortBuilder, limit, skip, hintBuilder, null);
    }

    default TListResult findList(final Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                                 final Function<FieldNest, List<String>> fieldNestList,
                                 final Function<SortBuilder<TEntity>, Bson> sortBuilder,
                                 int limit, int skip,
                                 final Function<HintBuilder<TEntity>, Bson> hintBuilder,
                                 final ReadPreference readPreference) {

        final FindOptions findOptions = createFindOptions(filterBuilder, fieldNestList, sortBuilder, limit, skip, hintBuilder, readPreference);

        return this.findList(findOptions);

    }

    /**
     * 根据条件获取获取列表
     *
     * @param findOptions FindOptions
     * @return Result
     */
    default TListResult findList(final FindOptions findOptions) {
        return findExecutor().doFindList(findOptions);
    }

    default FindOptions createFindOptions(
            Function<FilterBuilder<TEntity>, Bson> filterBuilder,
            Function<FieldNest, List<String>> fieldNestList,
            Function<SortBuilder<TEntity>, Bson> sortBuilder,
            int limit,
            int skip,
            Function<HintBuilder<TEntity>, Bson> hintBuilder,
            @Nullable ReadPreference readPreference) {

        final FindOptions findOptions = new FindOptions();

        if (!Objects.isNull(filterBuilder)) {
            findOptions.filter(
                    filterBuilder.apply(FilterBuilder.create(findExecutor().getEntityInformation().getEntityType()))
            );
        }

        if (!Objects.isNull(fieldNestList)) {
            findOptions.includeFields(
                    fieldNestList.apply(FieldNest.create())
            );
        }

        if (!Objects.isNull(sortBuilder)) {
            findOptions.sort(
                    sortBuilder.apply(SortBuilder.create(findExecutor().getEntityInformation().getEntityType()))
            );
        }

        if (limit > 0) {
            findOptions.limit(limit);
        }

        if (skip > 0) {
            findOptions.skip(skip);
        }

        if (!Objects.isNull(hintBuilder)) {
            findOptions.hint(
                    hintBuilder.apply(HintBuilder.create(findExecutor().getEntityInformation().getEntityType()))
            );
        }

        if (readPreference != null) {
            findOptions.readPreference(readPreference);
        }
        return findOptions;
    }

    //#endregion


    FindExecutor<TEntity, TKey, TSingleResult, TListResult, TCountResult, TExistsResult> findExecutor();



}
