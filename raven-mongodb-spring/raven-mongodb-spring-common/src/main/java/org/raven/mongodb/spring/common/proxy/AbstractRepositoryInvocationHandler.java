package org.raven.mongodb.spring.common.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Abstract base class for repository invocation handlers.
 * <p>
 * This class provides common functionality for delegating method calls from
 * repository interfaces to their implementations. Subclasses need to provide
 * the actual repository implementation.
 * </p>
 *
 * @author yi.liang
 * @since 3.1.0
 */
@Slf4j
public abstract class AbstractRepositoryInvocationHandler implements InvocationHandler {

    protected final Class<?> repositoryInterface;
    protected Object repositoryImpl;
    protected final Class<?> entityClass;
    protected final Class<?> keyClass;

    public AbstractRepositoryInvocationHandler(Class<?> repositoryInterface) {
        this.repositoryInterface = repositoryInterface;

        // Extract generic type parameters from repository interface
        Class<?>[] genericTypes = extractGenericTypes(repositoryInterface);
        this.entityClass = genericTypes[0];
        this.keyClass = genericTypes[1];
    }

    /**
     * Initializes the repository implementation.
     * This method should be called by subclasses after their fields are initialized.
     */
    protected void initializeRepositoryImpl() {
        if (this.repositoryImpl == null) {
            this.repositoryImpl = createRepositoryImplementation();
        }
    }

    /**
     * Extracts generic type parameters (TEntity, TKey) from repository interface.
     *
     * @param repositoryInterface the repository interface
     * @return array containing [entityClass, keyClass]
     */
    @SuppressWarnings("unchecked")
    protected Class<?>[] extractGenericTypes(Class<?> repositoryInterface) {
        // Search through all generic interfaces to find the one with type parameters
        for (Type genericInterface : repositoryInterface.getGenericInterfaces()) {
            if (genericInterface instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
                Type[] typeArguments = parameterizedType.getActualTypeArguments();

                if (typeArguments.length >= 2) {
                    Class<?> entityClass = (Class<?>) typeArguments[0];
                    Class<?> keyClass = (Class<?>) typeArguments[1];

                    log.debug("Extracted generic types from {}: entityClass={}, keyClass={}",
                            repositoryInterface.getName(), entityClass.getName(), keyClass.getName());

                    return new Class<?>[]{entityClass, keyClass};
                }
            }
        }

        throw new IllegalArgumentException(
            "Cannot extract generic type parameters from repository interface: " + repositoryInterface.getName()
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Handle Object methods (toString, equals, hashCode)
        if (method.getDeclaringClass() == Object.class) {
            return handleObjectMethod(method, args);
        }

        // Lazy initialization of repository implementation
        initializeRepositoryImpl();

        // Handle default methods from interface
        if (method.isDefault()) {
            return invokeDefaultMethod(proxy, method, args);
        }

        // Delegate to the actual repository implementation
        try {
            return method.invoke(repositoryImpl, args);
        } catch (Exception e) {
            log.error("Error invoking method {} on repository {}", method.getName(), repositoryInterface.getName(), e);
            throw e.getCause() != null ? e.getCause() : e;
        }
    }

    /**
     * Invokes a default method from the repository interface.
     * Uses MethodHandles to properly invoke default interface methods.
     *
     * @param proxy the proxy instance
     * @param method the default method to invoke
     * @param args the method arguments
     * @return the result of the method invocation
     * @throws Throwable if the method invocation fails
     */
    private Object invokeDefaultMethod(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            Class<?> declaringClass = method.getDeclaringClass();

            // Use MethodHandles.privateLookupIn for Java 9+
            // Bind to proxy and then invoke with the original arguments
            MethodHandle methodHandle = MethodHandles.privateLookupIn(declaringClass, MethodHandles.lookup())
                    .unreflectSpecial(method, declaringClass)
                    .bindTo(proxy);

            // Invoke with arguments using invokeWithArguments for consistent behavior
            if (args == null || args.length == 0) {
                return methodHandle.invokeWithArguments();
            } else {
                return methodHandle.invokeWithArguments(args);
            }
        } catch (Throwable e) {
            log.error("Error invoking default method {} on repository {}",
                    method.getName(), repositoryInterface.getName(), e);
            throw e;
        }
    }

    /**
     * Creates the appropriate repository implementation based on the interface type.
     * Subclasses must implement this to provide the correct implementation.
     *
     * @return the repository implementation instance
     */
    protected abstract Object createRepositoryImplementation();

    /**
     * Gets the repository type name for logging and toString.
     *
     * @return the repository type name (e.g., "MongoRepository", "ReactiveMongoRepository")
     */
    protected abstract String getRepositoryTypeName();

    /**
     * Handles Object methods (toString, equals, hashCode).
     */
    protected Object handleObjectMethod(Method method, Object[] args) throws Throwable {
        String methodName = method.getName();

        switch (methodName) {
            case "toString":
                return toString();
            case "equals":
                return args[0] == this;
            case "hashCode":
                return System.identityHashCode(this);
            default:
                return method.invoke(this, args);
        }
    }

    @Override
    public String toString() {
        return getRepositoryTypeName() + "Proxy[" + repositoryInterface.getSimpleName() + "]";
    }
}
