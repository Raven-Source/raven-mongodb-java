package org.raven.mongodb;

import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.commons.util.Args;
import org.raven.mongodb.annotation.PreDelete;
import org.raven.mongodb.annotation.PreFind;
import org.raven.mongodb.annotation.PreInsert;
import org.raven.mongodb.annotation.PreUpdate;
import org.raven.mongodb.criteria.*;
import org.raven.mongodb.interceptors.EntityInterceptor;
import org.raven.mongodb.operation.KeyFilter;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author by yanfeng
 * date 2021/9/20 20:57
 */
@Slf4j
public abstract class BaseRepository<TEntity extends Entity<TKey>, TKey> implements KeyFilter<TKey> {

    protected final EntityInformation<TEntity, TKey> entityInformation;
    protected final OperationLogger operationLogger;

    @SuppressWarnings({"unchecked"})
    protected BaseRepository(@Nullable final String collectionName) {

        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Class<TEntity> entityClazz = (Class<TEntity>) params[0];
        Class<TKey> keyClazz = (Class<TKey>) params[1];

        this.entityInformation = new EntityInformationSupport<>(entityClazz, keyClazz, collectionName);
        this.operationLogger = new OperationLogger();
    }

    /**
     * Collection Name
     *
     * @return Collection Name
     */
    public String getCollectionName() {
        return entityInformation.getCollectionName();
    }

    protected void callGlobalInterceptors(final Class<? extends Annotation> event,
                                          Object entity,
                                          final CommandOptions options) {
        for (final EntityInterceptor ei : entityInformation.getInterceptors()) {
            if (log.isDebugEnabled()) {
                log.debug("Calling interceptor method " + event.getSimpleName() + " on " + ei);
            }
            if (PreFind.class.equals(event)) {
                ei.preFind((BaseFindOptions<?>) options, entityInformation);
            } else if (PreInsert.class.equals(event)) {
                ei.preInsert(entity, entityInformation);
            } else if (PreUpdate.class.equals(event)) {
                ei.preUpdate((BaseUpdateOptions<?>) options, entityInformation);
            } else if (PreDelete.class.equals(event)) {
                ei.preDelete((BaseModifyOptions<?>) options, entityInformation);
            }
        }
    }

    @Override
    public Bson filterById(final TKey id) {
        Args.notNull(id, "entity.getId()");
        return Filters.eq(entityInformation.getIdName(), id);
    }

    protected FilterBuilder<TEntity> filterBuilder() {
        return FilterBuilder.create(entityInformation.getEntityType());
    }

    protected UpdateBuilder<TEntity> updateBuilder() {
        return UpdateBuilder.create(entityInformation.getEntityType());
    }

    protected SortBuilder<TEntity> sortBuilder() {
        return SortBuilder.create(entityInformation.getEntityType());
    }

    protected HintBuilder<TEntity> hintBuilder() {
        return HintBuilder.create(entityInformation.getEntityType());
    }

    protected ProjectionBuilder<TEntity> projectionBuilder() {
        return ProjectionBuilder.create(entityInformation.getEntityType());
    }
}
