package org.bson.codecs.pojo;

import lombok.NonNull;
import org.bson.codecs.Codec;
import org.raven.mongodb.repository.codec.PojoCodecRegistry;
import org.raven.mongodb.repository.query.FieldNest;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author by yanfeng
 * date 2021/9/13 0:57
 */
public class ClassModelUtils {

    private ClassModelUtils() {
    }

    private final static String regex = "\\.";
    private final static Map<Class<?>, ClassModel<?>> mappedClassModel = new ConcurrentHashMap<>();

    /**
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> ClassModel<T> getClassModel(Class<T> clazz) {

        ClassModel<T> classModel = (ClassModel<T>) mappedClassModel.get(clazz);
        if (classModel == null) {

            Codec<T> codec = PojoCodecRegistry.CODEC_REGISTRY.get(clazz);
            if (codec instanceof AutomaticPojoCodec) {
                classModel = ((AutomaticPojoCodec<T>) codec).getClassModel();
                mappedClassModel.putIfAbsent(clazz, classModel);
            }

        }

        return classModel;
    }

    /**
     * @param entityClass
     * @param <T>
     * @return
     */
    public static <T> String getWriteName(final Class<T> entityClass, @NonNull final LinkedList<String> properties) {

        FieldNest fieldNest = FieldNest.empty();
        doWriteName(entityClass, fieldNest, properties);

        return fieldNest.build();
    }

    /**
     * @param entityClass
     * @param propertyName
     * @param <T>
     * @return
     */
    public static <T> String getWriteName(final Class<T> entityClass, @NonNull final String propertyName) {

        LinkedList<String> properties = new LinkedList<>(Arrays.asList(propertyName.split(regex)));
        return getWriteName(entityClass, properties);
    }


    /**
     * @param entityClass
     * @param resList
     * @param propertyNames
     * @param <T>
     */
    public static <T> void doWriteName(final Class<T> entityClass, final FieldNest resList, @NonNull final LinkedList<String> propertyNames) {

        String propertyName = propertyNames.poll();

        if (propertyName != null) {

            PropertyModel<?> propertyModel = getPropertyModel(entityClass, propertyName);
            if (propertyModel != null) {

                Class<?> propertyClass = propertyModel.getTypeData().getType();
                resList.link(propertyModel.getWriteName());

                if (propertyClass != null && propertyNames.size() > 0) {
                    doWriteName(propertyClass, resList, propertyNames);
                }

            } else {
                resList.link(propertyName);
            }
        }

    }


    private static <T> PropertyModel<?> getPropertyModel(final Class<T> entityClass, final String propertyName) {

        ClassModel<T> classModel = getClassModel(entityClass);
        if (classModel != null) {
            PropertyModel<?> propertyModel = classModel.getPropertyModel(propertyName);
            if (propertyModel != null) {
                return propertyModel;
            }
        }

        return null;
    }
}