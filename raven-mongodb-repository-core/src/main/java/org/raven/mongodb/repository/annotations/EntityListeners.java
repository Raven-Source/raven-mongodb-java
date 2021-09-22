package org.raven.mongodb.repository.annotations;

import org.raven.mongodb.repository.interceptors.EntityInterceptor;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author yi.liang
 * date 2021/9/22 19:23
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface EntityListeners {

    /**
     * The callback listener classes
     */
    Class<? extends EntityInterceptor>[] value();
}
