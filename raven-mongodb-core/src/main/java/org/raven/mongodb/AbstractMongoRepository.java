package org.raven.mongodb;

import lombok.extern.slf4j.Slf4j;
import org.raven.commons.data.Entity;
import org.raven.mongodb.annotations.PreDelete;
import org.raven.mongodb.annotations.PreFind;
import org.raven.mongodb.annotations.PreInsert;
import org.raven.mongodb.annotations.PreUpdate;
import org.raven.mongodb.interceptors.EntityInterceptor;
import org.raven.mongodb.query.*;
import org.raven.mongodb.support.EntityInformationSupport;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author by yanfeng
 * date 2021/9/20 20:57
 */
@Slf4j
public abstract class AbstractMongoRepository<TEntity extends Entity<TKey>, TKey> {

    protected final EntityInformation<TEntity, TKey> entityInformation;

    @SuppressWarnings({"unchecked"})
    protected AbstractMongoRepository(final String collectionName) {

        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Class<TEntity> entityClazz = (Class<TEntity>) params[0];
        Class<TKey> keyClazz = (Class<TKey>) params[1];

        this.entityInformation = new EntityInformationSupport<>(entityClazz, keyClazz, collectionName);
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
                                          final Options options) {
        for (final EntityInterceptor ei : entityInformation.getInterceptors()) {
            if (log.isDebugEnabled()) {
                log.debug("Calling interceptor method " + event.getSimpleName() + " on " + ei);
            }
            if (PreFind.class.equals(event)) {
                ei.preFind((AbstractFindOptions) options, entityInformation);
            } else if (PreInsert.class.equals(event)) {
                ei.preInsert(entity, entityInformation);
            } else if (PreUpdate.class.equals(event)) {
                ei.preUpdate((UpdateOptions) options, entityInformation);
            } else if (PreDelete.class.equals(event)) {
                ei.preDelete((DeleteOptions) options, entityInformation);
            }
        }
    }

    protected FilterBuilder<TEntity> filterBuilder() {
        return FilterBuilder.empty(entityInformation.getEntityType());
    }

    protected UpdateBuilder<TEntity> updateBuilder() {
        return UpdateBuilder.empty(entityInformation.getEntityType());
    }

    protected SortBuilder<TEntity> sortBuilder() {
        return SortBuilder.empty(entityInformation.getEntityType());
    }

    protected HintBuilder<TEntity> hintBuilder() {
        return HintBuilder.empty(entityInformation.getEntityType());
    }

    protected ProjectionBuilder<TEntity> projectionBuilder() {
        return ProjectionBuilder.empty(entityInformation.getEntityType());
    }
}
