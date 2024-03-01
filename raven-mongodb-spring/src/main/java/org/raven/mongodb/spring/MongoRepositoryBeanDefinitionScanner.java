package org.raven.mongodb.spring;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

public class MongoRepositoryBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

    public MongoRepositoryBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }
}
