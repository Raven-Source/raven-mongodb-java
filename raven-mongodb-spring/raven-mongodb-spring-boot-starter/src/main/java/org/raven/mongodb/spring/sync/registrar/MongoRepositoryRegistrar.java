package org.raven.mongodb.spring.sync.registrar;

import org.raven.mongodb.MongoQueryRepository;
import org.raven.mongodb.MongoRepository;
import org.raven.mongodb.spring.common.registrar.AbstractRepositoryRegistrar;
import org.raven.mongodb.spring.sync.annotation.EnableMongoRepositories;
import org.raven.mongodb.spring.sync.factory.MongoRepositoryFactoryBean;

import java.lang.annotation.Annotation;

/**
 * Scans for MongoDB repository interfaces and registers them as Spring beans.
 * This registrar is triggered by the @EnableMongoRepositories annotation.
 *
 * @author yi.liang
 * @since 3.1.0
 */
public class MongoRepositoryRegistrar extends AbstractRepositoryRegistrar {

    @Override
    protected Class<? extends Annotation> getEnableAnnotationClass() {
        return EnableMongoRepositories.class;
    }

    @Override
    protected Class<?>[] getRepositoryBaseClasses() {
        return new Class<?>[]{MongoRepository.class, MongoQueryRepository.class};
    }

    @Override
    protected Class<?> getFactoryBeanClass() {
        return MongoRepositoryFactoryBean.class;
    }

    @Override
    protected String getRepositoryTypeName() {
        return "MongoDB";
    }
}
