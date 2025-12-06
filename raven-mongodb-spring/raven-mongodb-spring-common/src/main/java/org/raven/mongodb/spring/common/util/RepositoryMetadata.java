package org.raven.mongodb.spring.common.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Utility class to extract entity and key types from repository interfaces.
 * <p>
 * This class analyzes the generic type parameters of repository interfaces
 * to determine the entity class and key class being used.
 * </p>
 *
 * @author yi.liang
 * @since 3.1.0
 */
public class RepositoryMetadata {

    private final Class<?> entityType;
    private final Class<?> keyType;

    /**
     * Constructs metadata by analyzing the repository interface.
     *
     * @param repositoryInterface the repository interface to analyze
     * @param baseRepositoryClass the base repository class to look for (e.g., MongoRepository.class)
     */
    public RepositoryMetadata(Class<?> repositoryInterface, Class<?> baseRepositoryClass) {
        Type[] genericInterfaces = repositoryInterface.getGenericInterfaces();

        Class<?> foundEntityType = null;
        Class<?> foundKeyType = null;

        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
                Type rawType = parameterizedType.getRawType();

                if (rawType == baseRepositoryClass || isAssignableFrom(baseRepositoryClass, rawType)) {
                    Type[] typeArguments = parameterizedType.getActualTypeArguments();
                    if (typeArguments.length >= 2) {
                        foundEntityType = (Class<?>) typeArguments[0];
                        foundKeyType = (Class<?>) typeArguments[1];
                        break;
                    }
                }
            }
        }

        if (foundEntityType == null || foundKeyType == null) {
            throw new IllegalArgumentException(
                "Cannot determine entity and key types for repository: " + repositoryInterface.getName()
                + ". Make sure it extends a repository interface with proper generic parameters."
            );
        }

        this.entityType = foundEntityType;
        this.keyType = foundKeyType;
    }

    /**
     * Checks if the base class is assignable from the raw type.
     */
    private boolean isAssignableFrom(Class<?> baseClass, Type rawType) {
        if (rawType instanceof Class) {
            return baseClass.isAssignableFrom((Class<?>) rawType);
        }
        return false;
    }

    /**
     * Gets the entity type.
     *
     * @return the entity class
     */
    public Class<?> getEntityType() {
        return entityType;
    }

    /**
     * Gets the key type.
     *
     * @return the key class
     */
    public Class<?> getKeyType() {
        return keyType;
    }

    @Override
    public String toString() {
        return "RepositoryMetadata{" +
            "entityType=" + entityType.getSimpleName() +
            ", keyType=" + keyType.getSimpleName() +
            '}';
    }
}
