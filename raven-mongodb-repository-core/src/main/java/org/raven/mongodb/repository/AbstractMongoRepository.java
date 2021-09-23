package org.raven.mongodb.repository;

import lombok.extern.slf4j.Slf4j;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.annotations.PostUpdate;
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

    protected String collectionName;

    protected final EntityInformation<TEntity, TKey> entityInformation;

    @SuppressWarnings({"unchecked"})
    protected AbstractMongoRepository(final String collectionName) {

        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Class<TEntity> entityClazz = (Class) params[0];
        Class<TKey> keyClazz = (Class) params[1];

        this.entityInformation = new EntityInformationSupport(entityClazz, keyClazz);

        this.collectionName = collectionName == null || collectionName.isEmpty()
                ? entityInformation.getEntityName()
                : collectionName;
    }

    protected void callGlobalInterceptors(final Class<? extends Annotation> event,
                                          Object entity,
                                          final Options options) {
        for (final EntityInterceptor ei : entityInformation.getInterceptors()) {
            if (log.isDebugEnabled()) {
                log.debug("Calling interceptor method " + event.getSimpleName() + " on " + ei);
            }

            if (PostUpdate.class.equals(event)) {
                ei.postUpdate((UpdateOptions) options, entityInformation);
            } else if (PreFind.class.equals(event)) {
                ei.preFind((AbstractFindOptions) options, entityInformation);
            } else if (PreInsert.class.equals(event)) {
                ei.preInsert(entity, entityInformation);
            }
        }
    }
}
