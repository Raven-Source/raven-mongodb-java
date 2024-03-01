package org.raven.mongodb.repository.interceptors;

import org.raven.mongodb.repository.AbstractFindOptions;
import org.raven.mongodb.repository.DeleteOptions;
import org.raven.mongodb.repository.EntityInformation;
import org.raven.mongodb.repository.UpdateOptions;

/**
 * @author by yanfeng
 * date 2021/9/20 17:23
 */
public interface EntityInterceptor {

    default void preFind(final AbstractFindOptions options,
                         final EntityInformation<?, ?> entityInformation) {
    }

    default void preInsert(Object entity,
                           final EntityInformation<?, ?> entityInformation) {
    }

    default void preUpdate(final UpdateOptions options,
                           final EntityInformation<?, ?> entityInformation) {
    }

    default void preDelete(final DeleteOptions options,
                           final EntityInformation<?, ?> entityInformation) {
    }
}
