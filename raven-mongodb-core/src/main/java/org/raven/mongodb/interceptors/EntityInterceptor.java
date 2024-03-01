package org.raven.mongodb.interceptors;

import org.raven.mongodb.AbstractFindOptions;
import org.raven.mongodb.DeleteOptions;
import org.raven.mongodb.EntityInformation;
import org.raven.mongodb.UpdateOptions;

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
