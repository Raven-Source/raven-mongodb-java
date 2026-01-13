package org.raven.mongodb.spring.sync.factory;

import lombok.extern.slf4j.Slf4j;
import org.raven.mongodb.MongoSession;
import org.raven.mongodb.spring.common.factory.AbstractRepositoryFactoryBean;
import org.raven.mongodb.spring.sync.proxy.MongoRepositoryInvocationHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationHandler;

/**
 * FactoryBean that creates dynamic proxy instances for MongoDB repository interfaces.
 * This factory is responsible for:
 * <ul>
 *   <li>Creating the underlying repository implementation (MongoRepositoryImpl or MongoQueryRepositoryImpl)</li>
 *   <li>Generating a JDK dynamic proxy that delegates method calls to the implementation</li>
 *   <li>Injecting the MongoSession dependency</li>
 * </ul>
 *
 * @author yi.liang
 * @since 3.1.0
 */
@Slf4j
public class MongoRepositoryFactoryBean<T> extends AbstractRepositoryFactoryBean<T> {

    @Autowired
    private MongoSession mongoSession;

    /**
     * Constructs a new MongoRepositoryFactoryBean.
     *
     * @param repositoryInterface the repository interface class
     */
    public MongoRepositoryFactoryBean(Class<T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected void validateSession() {
        if (mongoSession == null) {
            throw new IllegalStateException("MongoSession is not available. Please ensure MongoSession bean is configured.");
        }
    }

    @Override
    protected Object createInvocationHandler() {
        return new MongoRepositoryInvocationHandler(repositoryInterface, mongoSession);
    }

    @Override
    protected Object invokeHandler(Object proxy, Object handler, java.lang.reflect.Method method, Object[] args) throws Throwable {
        return ((InvocationHandler) handler).invoke(proxy, method, args);
    }
}
