package org.raven.mongodb.util;

import lombok.extern.slf4j.Slf4j;
import org.raven.mongodb.annotations.EntityListeners;
import org.raven.mongodb.interceptors.EntityInterceptor;

import java.util.*;
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

        Set<EntityInterceptor> entityInterceptors = new HashSet<>();

        List<EntityListeners> entityListeners = AnnotationUtils.findAllInheritanceAnnotation(entityClass, EntityListeners.class);

        for (EntityListeners entityListener : entityListeners) {

            Class<? extends EntityInterceptor>[] classes = entityListener.value();
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

        return new ArrayList<>(entityInterceptors);
    }

}
