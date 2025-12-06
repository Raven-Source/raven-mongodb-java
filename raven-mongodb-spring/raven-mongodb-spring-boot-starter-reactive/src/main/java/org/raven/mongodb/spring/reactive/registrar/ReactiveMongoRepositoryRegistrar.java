package org.raven.mongodb.spring.reactive.registrar;

import org.raven.mongodb.reactive.ReactiveMongoQueryRepository;
import org.raven.mongodb.reactive.ReactiveMongoRepository;
import org.raven.mongodb.spring.common.registrar.AbstractRepositoryRegistrar;
import org.raven.mongodb.spring.reactive.annotation.EnableReactiveMongoRepositories;
import org.raven.mongodb.spring.reactive.factory.ReactiveMongoRepositoryFactoryBean;

import java.lang.annotation.Annotation;

/**
 * Scans for reactive MongoDB repository interfaces and registers them as Spring beans.
 * This registrar is triggered by the @EnableReactiveMongoRepositories annotation.
 *
 * @author yi.liang
 * @since 3.1.0
 */
public class ReactiveMongoRepositoryRegistrar extends AbstractRepositoryRegistrar {

    @Override
    protected Class<? extends Annotation> getEnableAnnotationClass() {
        return EnableReactiveMongoRepositories.class;
    }

    @Override
    protected Class<?>[] getRepositoryBaseClasses() {
        return new Class<?>[]{ReactiveMongoRepository.class, ReactiveMongoQueryRepository.class};
    }

    @Override
    protected Class<?> getFactoryBeanClass() {
        return ReactiveMongoRepositoryFactoryBean.class;
    }

    @Override
    protected String getRepositoryTypeName() {
        return "Reactive MongoDB";
    }
}
