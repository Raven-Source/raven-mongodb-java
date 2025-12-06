package org.raven.mongodb.spring.common.util;

/**
 * Utility class for checking repository interface types.
 *
 * @author yi.liang
 * @since 3.1.0
 */
public class RepositoryTypeChecker {

    /**
     * Checks if the given interface extends any of the specified base repository classes.
     *
     * @param repositoryInterface the interface to check
     * @param baseRepositoryClasses the base repository classes to check against
     * @return true if the interface extends any of the base classes
     */
    public static boolean isRepositoryInterface(Class<?> repositoryInterface, Class<?>... baseRepositoryClasses) {
        if (!repositoryInterface.isInterface()) {
            return false;
        }

        for (Class<?> baseClass : baseRepositoryClasses) {
            if (baseClass.isAssignableFrom(repositoryInterface)) {
                return true;
            }
        }

        return false;
    }
}
