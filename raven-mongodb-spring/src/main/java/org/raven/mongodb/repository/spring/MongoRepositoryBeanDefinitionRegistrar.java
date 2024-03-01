package org.raven.mongodb.repository.spring;

import lombok.NonNull;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

public class MongoRepositoryBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar,
        BeanClassLoaderAware, ResourceLoaderAware {

    private ResourceLoader resourceLoader;
    private ClassLoader beanClassLoader;

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata annotationMetadata
            , @NonNull BeanDefinitionRegistry registry) {


        registry.registerBeanDefinition("", new RootBeanDefinition());

    }
}
