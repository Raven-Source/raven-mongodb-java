package org.raven.mongodb.operation;

import com.mongodb.ReadPreference;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.criteria.*;
import org.raven.mongodb.contants.BsonConstant;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author by yanfeng
 * date 2021/10/30 20:49
 */
public interface ReadOperation<TEntity extends Entity<TKey>, TKey, TSingleResult, TManyResult, TCountResult, TExistsResult> {

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
     * @param id         id
     * @param projection 查询字段
     * @return Result
     */
    default TSingleResult findOne(final TKey id, final Bson projection) {
        return this.findOne(id, projection, null);
    }

    /**
     * 根据id获取实体
     *
     * @param id             id
     * @param projection     查询字段
     * @param readPreference 访问设置
     * @return Result
     */
    default TSingleResult findOne(final TKey id, final Bson projection
            , final ReadPreference readPreference) {

        Bson filter = Filters.eq(BsonConstant.PRIMARY_KEY_NAME, id);

        return this.findOne(filter, projection, null, null, readPreference);
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
     * @param filter     查询条件
     * @param projection 查询字段
     * @return Result
     */
    default TSingleResult findOne(final Bson filter, final Bson projection) {
        return this.findOne(filter, projection, null);
    }

    /**
     * 根据条件获取实体
     *
     * @param filter     查询条件
     * @param projection 查询字段
     * @param sort       排序
     * @return Result
     */
    default TSingleResult findOne(final Bson filter, final Bson projection, final Bson sort) {
        return this.findOne(filter, projection, sort, null, null);

    }

    /**
     * 根据条件获取实体
     *
     * @param filter         查询条件
     * @param projection     查询字段
     * @param sort           排序
     * @param hint           hint索引
     * @param readPreference 访问设置
     * @return Result
     */
    default TSingleResult findOne(final Bson filter, final Bson projection, final Bson sort, final Bson hint
            , final ReadPreference readPreference) {

        FindOptions options = new FindOptions();
        options.filter(filter);
        options.hint(hint);
        options.projection(projection);
        options.readPreference(readPreference);
        options.limit(1);
        options.skip(0);
        options.sort(sort);

        return this.findOne(options);
    }

    /**
     * 根据条件获取实体
     *
     * @param filterExpression {{@link FilterBuilder}}
     * @return Result
     */
    default TSingleResult findOne(final FilterExpression<TEntity> filterExpression) {
        return this.findOne(filterExpression, null);
    }

    /**
     * 根据条件获取实体
     *
     * @param filterExpression     {{@link FilterBuilder}}
     * @param projectionExpression {{@link ProjectionBuilder}}
     * @return Result
     */
    default TSingleResult findOne(final FilterExpression<TEntity> filterExpression,
                                  final ProjectionExpression<TEntity> projectionExpression) {
        return this.findOne(filterExpression, projectionExpression, null);
    }

    /**
     * 根据条件获取实体
     *
     * @param filterExpression     {{@link FilterBuilder}}
     * @param projectionExpression {{@link ProjectionBuilder}}
     * @param sortExpression       {{@link SortBuilder}}
     * @return Result
     */
    default TSingleResult findOne(final FilterExpression<TEntity> filterExpression,
                                  final ProjectionExpression<TEntity> projectionExpression,
                                  final SortExpression<TEntity> sortExpression) {
        return this.findOne(filterExpression, projectionExpression, sortExpression, null);

    }

    /**
     * 根据条件获取实体
     *
     * @param filterExpression     {{@link FilterBuilder}}
     * @param projectionExpression {{@link ProjectionBuilder}}
     * @param sortExpression       {{@link SortBuilder}}
     * @param hintExpression       {{@link HintBuilder}}
     * @return Result
     */
    default TSingleResult findOne(final FilterExpression<TEntity> filterExpression,
                                  final ProjectionExpression<TEntity> projectionExpression,
                                  final SortExpression<TEntity> sortExpression,
                                  final HintExpression<TEntity> hintExpression) {
        return this.findOne(filterExpression, projectionExpression, sortExpression, hintExpression, null);

    }

    /**
     * 根据条件获取实体
     *
     * @param filterExpression     {{@link FilterBuilder}}
     * @param projectionExpression {{@link ProjectionBuilder}}
     * @param sortExpression       {{@link SortBuilder}}
     * @param hintExpression       {{@link HintBuilder}}
     * @param readPreference       {{@link ReadPreference}}
     * @return Result
     */
    default TSingleResult findOne(final FilterExpression<TEntity> filterExpression,
                                  final ProjectionExpression<TEntity> projectionExpression,
                                  final SortExpression<TEntity> sortExpression,
                                  final HintExpression<TEntity> hintExpression,
                                  final ReadPreference readPreference) {

        final FindOptions findOptions = createFindOptions(filterExpression, projectionExpression, sortExpression, 0, 0, hintExpression, readPreference);

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
     * @return Result
     */
    default TManyResult findAll() {
        return this.findMany((Bson) null);
    }

    /**
     * 根据条件获取获取列表
     *
     * @param filter 查询条件
     * @return Result
     */
    default TManyResult findMany(final Bson filter) {
        return this.findMany(filter, null);
    }

    /**
     * 根据条件获取获取列表
     *
     * @param filter     查询条件
     * @param projection 查询字段
     * @return Result
     */
    default TManyResult findMany(final Bson filter, final Bson projection) {
        return this.findMany(filter, projection, null);
    }

    /**
     * 根据条件获取获取列表
     *
     * @param filter     查询条件
     * @param projection 查询字段
     * @param sort       排序
     * @return Result
     */
    default TManyResult findMany(final Bson filter, final Bson projection, final Bson sort) {
        return this.findMany(filter, projection, sort, 0, 0);
    }

    /**
     * 根据条件获取获取列表
     *
     * @param filter         查询条件
     * @param projection     查询字段
     * @param sort           排序
     * @param limit          limit
     * @param skip           skip
     * @param hint           hint索引
     * @param readPreference 访问设置
     * @return Result
     */
    default TManyResult findMany(final Bson filter, final Bson projection, final Bson sort
            , final int limit, final int skip
            , final Bson hint
            , final @Nullable ReadPreference readPreference) {

        FindOptions options = new FindOptions();
        options.filter(filter);
        options.hint(hint);
        options.projection(projection);

        if (readPreference != null) {
            options.readPreference(readPreference);
        }
        options.limit(limit);
        options.skip(skip);
        options.sort(sort);

        return this.findMany(options);
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
     * @param filter     查询条件
     * @param projection 查询字段
     * @param sort       排序
     * @param limit      limit
     * @param skip       skip
     * @return Result
     */
    default TManyResult findMany(final Bson filter, final Bson projection, final Bson sort
            , final int limit, final int skip) {
        return this.findMany(filter, projection, sort, limit, skip, null, null);
    }

    default TManyResult findMany(final FilterExpression<TEntity> filterExpression) {
        return this.findMany(filterExpression, null);
    }

    default TManyResult findMany(final FilterExpression<TEntity> filterExpression,
                                 final ProjectionExpression<TEntity> projectionExpression) {
        return this.findMany(filterExpression, projectionExpression, null);
    }

    default TManyResult findMany(final FilterExpression<TEntity> filterExpression,
                                 final ProjectionExpression<TEntity> projectionExpression,
                                 final SortExpression<TEntity> sortExpression) {
        return this.findMany(filterExpression, projectionExpression, sortExpression, 0, 0);
    }

    default TManyResult findMany(final FilterExpression<TEntity> filterExpression,
                                 final ProjectionExpression<TEntity> projectionExpression,
                                 final SortExpression<TEntity> sortExpression,
                                 int limit, int skip) {
        return this.findMany(filterExpression, projectionExpression, sortExpression, limit, skip, null);
    }

    default TManyResult findMany(final FilterExpression<TEntity> filterExpression,
                                 final ProjectionExpression<TEntity> projectionExpression,
                                 final SortExpression<TEntity> sortExpression,
                                 int limit, int skip,
                                 final HintExpression<TEntity> hintExpression) {
        return this.findMany(filterExpression, projectionExpression, sortExpression, limit, skip, hintExpression, null);
    }

    default TManyResult findMany(final FilterExpression<TEntity> filterExpression,
                                 final ProjectionExpression<TEntity> projectionExpression,
                                 final SortExpression<TEntity> sortExpression,
                                 int limit, int skip,
                                 final HintExpression<TEntity> hintExpression,
                                 final ReadPreference readPreference) {

        final FindOptions findOptions = createFindOptions(filterExpression, projectionExpression, sortExpression, limit, skip, hintExpression, readPreference);

        return this.findMany(findOptions);

    }

    /**
     * 根据条件获取获取列表
     *
     * @param findOptions FindOptions
     * @return Result
     */
    default TManyResult findMany(final FindOptions findOptions) {
        return findExecutor().doFindMany(findOptions);
    }

    default FindOptions createFindOptions(
            FilterExpression<TEntity> filterExpression,
            ProjectionExpression<TEntity> projectionExpression,
            SortExpression<TEntity> sortExpression,
            int limit,
            int skip,
            HintExpression<TEntity> hintExpression,
            @Nullable ReadPreference readPreference) {

        final FindOptions findOptions = new FindOptions();

        if (!Objects.isNull(filterExpression)) {
            findOptions.filter(
                    filterExpression.toBson(findExecutor().getEntityType())
            );
        }

        if (!Objects.isNull(projectionExpression)) {
            findOptions.projection(
                    projectionExpression.toBson(findExecutor().getEntityType())
            );
        }

        if (!Objects.isNull(sortExpression)) {
            findOptions.sort(
                    sortExpression.toBson(findExecutor().getEntityType())
            );
        }

        if (limit > 0) {
            findOptions.limit(limit);
        }

        if (skip > 0) {
            findOptions.skip(skip);
        }

        if (!Objects.isNull(hintExpression)) {
            findOptions.hint(
                    hintExpression.toBson(findExecutor().getEntityType())
            );
        }

        if (readPreference != null) {
            findOptions.readPreference(readPreference);
        }
        return findOptions;
    }

    //#endregion

    FindExecutor<TEntity, TKey, TSingleResult, TManyResult, TCountResult, TExistsResult> findExecutor();


}
