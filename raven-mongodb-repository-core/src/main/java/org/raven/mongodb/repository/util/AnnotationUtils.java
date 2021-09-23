package org.raven.mongodb.repository.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author yi.liang
 * date 2021/9/16 20:19
 */
public class AnnotationUtils {
    private AnnotationUtils() {
    }


    public static <A extends Annotation> A findAnnotation(Method method, Class<A> annotationType) {
        if (annotationType == null) {
            return null;
        } else {
            A annotation = method.getDeclaredAnnotation(annotationType);
            if (annotation != null) {
                return annotation;
            } else {
                Class<?> superclass = method.getDeclaringClass().getSuperclass();
                Method superMethod = null;
                try {
                    superMethod = superclass.getDeclaredMethod(method.getName(), method.getParameterTypes());
                } catch (Exception ignored) {
                }

                return superMethod != null && superclass != Object.class ? findAnnotation(superMethod, annotationType) : null;
            }
        }
    }

    public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType) {
        if (annotationType == null) {
            return null;
        } else {
            A annotation = clazz.getDeclaredAnnotation(annotationType);
            if (annotation != null) {
                return annotation;
            } else {
                Class<?> superclass = clazz.getSuperclass();
                return superclass != null && superclass != Object.class ? findAnnotation(superclass, annotationType) : null;
            }
        }
    }

    public static <A extends Annotation> List<A> findAllInheritanceAnnotation(Class<?> clazz, Class<A> annotationType) {

        List<A> annotations = new ArrayList<>();

        Function<Class<?>, A> supplier = (c) -> c.getDeclaredAnnotation(annotationType);

        if (annotationType != null) {

            while (clazz != null && clazz != Object.class) {

                A annotation = supplier.apply(clazz);
                if (annotation != null) {
                    annotations.add(annotation);
                }

                clazz = clazz.getSuperclass();
            }
        }

        return annotations;
    }


}
