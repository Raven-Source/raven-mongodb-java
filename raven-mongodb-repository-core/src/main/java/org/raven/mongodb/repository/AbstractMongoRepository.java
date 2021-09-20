package org.raven.mongodb.repository;

import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.support.EntityInformationSupport;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author by yanfeng
 * date 2021/9/20 20:57
 */
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
}
