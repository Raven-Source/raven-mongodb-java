package org.raven.mongodb.repository.util;

import lombok.extern.slf4j.Slf4j;
import org.raven.mongodb.repository.annotations.EntityListeners;
import org.raven.mongodb.repository.interceptors.EntityInterceptor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yi.liang
 * date 2021/9/22 19:41
 */
@Slf4j
public class EntityInterceptorUtils {

    private final static Map<Class<?>, EntityInterceptor> entityInterceptorCached = new ConcurrentHashMap<>();

    private EntityInterceptorUtils() {
    }

    public static List<EntityInterceptor> getInterceptors(Class<?> entityClass) {

        List<EntityInterceptor> entityInterceptors = new LinkedList<>();

        EntityListeners entityListeners = AnnotationUtils.findAnnotation(entityClass, EntityListeners.class);
        if (entityListeners != null) {
            Class<? extends EntityInterceptor>[] classes = entityListeners.value();

            for (Class<? extends EntityInterceptor> clazz : classes) {

                EntityInterceptor entityInterceptor = entityInterceptorCached.get(clazz);

                if (entityInterceptor == null) {
                    try {
                        entityInterceptor = clazz.getDeclaredConstructor().newInstance();
                        entityInterceptorCached.putIfAbsent(clazz, entityInterceptor);
                    } catch (Exception ex) {
                        log.error(ex.getMessage(), ex);
                    }
                }

                entityInterceptors.add(entityInterceptor);
            }

        }

        return entityInterceptors;
    }

}
