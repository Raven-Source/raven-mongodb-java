package org.raven.mongodb.repository.interceptors;

import org.raven.mongodb.repository.AbstractFindOptions;
import org.raven.mongodb.repository.EntityInformation;
import org.raven.mongodb.repository.UpdateOptions;

/**
 * @author by yi.liang
 * date 2021/9/20 17:23
 */
public interface EntityInterceptor {

    default void postUpdate(final UpdateOptions options,
                            final EntityInformation<?, ?> entityInformation) {

    }

    default void preFind(final AbstractFindOptions options,
                         final EntityInformation<?, ?> entityInformation) {

    }
}
