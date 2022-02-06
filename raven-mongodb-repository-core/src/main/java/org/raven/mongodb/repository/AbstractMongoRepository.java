package org.raven.mongodb.repository;

import lombok.extern.slf4j.Slf4j;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.annotations.PreUpdate;
import org.raven.mongodb.repository.annotations.PreFind;
import org.raven.mongodb.repository.annotations.PreInsert;
import org.raven.mongodb.repository.interceptors.EntityInterceptor;
import org.raven.mongodb.repository.support.EntityInformationSupport;

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
        Class<TEntity> entityClazz = (Class) params[0];
        Class<TKey> keyClazz = (Class) params[1];

        this.entityInformation = new EntityInformationSupport(entityClazz, keyClazz, collectionName);
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

            if (PreUpdate.class.equals(event)) {
                ei.preUpdate((UpdateOptions) options, entityInformation);
            } else if (PreFind.class.equals(event)) {
                ei.preFind((AbstractFindOptions) options, entityInformation);
            } else if (PreInsert.class.equals(event)) {
                ei.preInsert(entity, entityInformation);
            }
        }
    }
}
