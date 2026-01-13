package org.raven.mongodb.spring.common.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * Abstract base class for repository factory beans.
 * <p>
 * This class provides common functionality for creating dynamic proxy instances
 * of repository interfaces. Subclasses need to provide the session object and
 * invocation handler.
 * </p>
 *
 * @param <T> the repository interface type
 * @author yi.liang
 * @since 3.1.0
 */
@Slf4j
public abstract class AbstractRepositoryFactoryBean<T> implements FactoryBean<T> {

    protected final Class<T> repositoryInterface;
    protected T repositoryProxy;

    public AbstractRepositoryFactoryBean(Class<T> repositoryInterface) {
        this.repositoryInterface = repositoryInterface;
    }

    @Override
    public T getObject() throws Exception {
        if (repositoryProxy == null) {
            repositoryProxy = createRepositoryProxy();
        }
        return repositoryProxy;
    }

    @Override
    public Class<?> getObjectType() {
        return repositoryInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * Creates a dynamic proxy for the repository interface.
     * Subclasses must implement this to provide the appropriate invocation handler.
     * @return the repository proxy
     */
    @SuppressWarnings("unchecked")
    protected T createRepositoryProxy() {
        validateSession();

        log.debug("Creating repository proxy for: {}", repositoryInterface.getName());

        Object handler = createInvocationHandler();

        return (T) Proxy.newProxyInstance(
            repositoryInterface.getClassLoader(),
            new Class<?>[]{repositoryInterface},
            (proxy, method, args) -> {
                try {
                    return invokeHandler(proxy, handler, method, args);
                } catch (Throwable throwable) {
                    throw throwable;
                }
            }
        );
    }

    /**
     * Validates that the session is available.
     * Subclasses should override this to check their specific session type.
     */
    protected abstract void validateSession();

    /**
     * Creates the invocation handler for the proxy.
     * Subclasses must implement this to provide the appropriate handler.
     *
     * @return the invocation handler
     */
    protected abstract Object createInvocationHandler();

    /**
     * Invokes the handler method.
     * This is a helper method to bridge the invocation handler interface.
     *
     * @param proxy the proxy instance
     * @param handler the invocation handler
     * @param method the method being invoked
     * @param args the method arguments
     * @return the result of the invocation
     * @throws Throwable if the invocation fails
     */
    protected abstract Object invokeHandler(Object proxy, Object handler, java.lang.reflect.Method method, Object[] args) throws Throwable;
}
