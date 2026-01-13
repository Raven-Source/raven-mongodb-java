package org.raven.mongodb.spring.reactive.factory;

import lombok.extern.slf4j.Slf4j;
import org.raven.mongodb.reactive.ReactiveMongoSession;
import org.raven.mongodb.spring.common.factory.AbstractRepositoryFactoryBean;
import org.raven.mongodb.spring.reactive.proxy.ReactiveMongoRepositoryInvocationHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationHandler;

/**
 * FactoryBean that creates dynamic proxy instances for reactive MongoDB repository interfaces.
 * This factory is responsible for:
 * <ul>
 *   <li>Creating the underlying reactive repository implementation (ReactiveMongoRepositoryImpl or ReactiveMongoQueryRepositoryImpl)</li>
 *   <li>Generating a JDK dynamic proxy that delegates method calls to the implementation</li>
 *   <li>Injecting the ReactiveMongoSession dependency</li>
 * </ul>
 *
 * @author yi.liang
 * @since 3.1.0
 */
@Slf4j
public class ReactiveMongoRepositoryFactoryBean<T> extends AbstractRepositoryFactoryBean<T> {

    @Autowired
    private ReactiveMongoSession reactiveMongoSession;

    public ReactiveMongoRepositoryFactoryBean(Class<T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected void validateSession() {
        if (reactiveMongoSession == null) {
            throw new IllegalStateException("ReactiveMongoSession is not available. Please ensure ReactiveMongoSession bean is configured.");
        }
    }

    @Override
    protected Object createInvocationHandler() {
        return new ReactiveMongoRepositoryInvocationHandler(repositoryInterface, reactiveMongoSession);
    }

    @Override
    protected Object invokeHandler(Object proxy, Object handler, java.lang.reflect.Method method, Object[] args) throws Throwable {
        return ((InvocationHandler) handler).invoke(proxy, method, args);
    }
}
