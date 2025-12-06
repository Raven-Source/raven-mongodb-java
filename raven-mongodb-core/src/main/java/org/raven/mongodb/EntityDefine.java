package org.raven.mongodb;

public interface EntityDefine<TEntity> {

    /**
     * @return EntityInformation
     */
    EntityInformation<TEntity, ?> getEntityInformation();
}
