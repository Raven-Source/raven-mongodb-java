package org.raven.mongodb.repository;

/**
 * @author yi.liang
 * date 2021/9/20 17:31
 */
public interface EntityMetadata<TEntity> {

    Class<TEntity> getEntityType();

    String getEntityName();
}
