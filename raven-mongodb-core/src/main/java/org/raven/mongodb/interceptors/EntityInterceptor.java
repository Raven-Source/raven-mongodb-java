package org.raven.mongodb.interceptors;

import org.raven.mongodb.EntityInformation;
import org.raven.mongodb.criteria.*;

/**
 * @author by yanfeng
 * date 2021/9/20 17:23
 */
public interface EntityInterceptor {

    default void preFind(final BaseFindOptions<?> options,
                         final EntityInformation<?, ?> entityInformation) {
    }

    default void preInsert(Object entity,
                           final EntityInformation<?, ?> entityInformation) {
    }

    default void preUpdate(final BaseUpdateOptions<?> options,
                           final EntityInformation<?, ?> entityInformation) {
    }

    default void preDelete(final BaseModifyOptions<?> options,
                           final EntityInformation<?, ?> entityInformation) {
    }
}
