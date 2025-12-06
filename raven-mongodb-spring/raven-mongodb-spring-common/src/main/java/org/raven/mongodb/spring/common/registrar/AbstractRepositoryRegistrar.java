package org.raven.mongodb.spring.common.registrar;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Abstract base class for repository registrars.
 * <p>
 * This class provides common functionality for scanning and registering repository interfaces
 * as Spring beans. Subclasses need to specify the repository types to scan for and the
 * factory bean class to use.
 * </p>
 *
 * @author yi.liang
 * @since 3.1.0
 */
@Slf4j
public abstract class AbstractRepositoryRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     * Gets the annotation class that enables repository scanning.
     *
     * @return the enable annotation class
     */
    protected abstract Class<? extends Annotation> getEnableAnnotationClass();

    /**
     * Gets the repository base classes to scan for.
     *
     * @return array of repository base classes
     */
    protected abstract Class<?>[] getRepositoryBaseClasses();

    /**
     * Gets the factory bean class to use for creating repository instances.
     *
     * @return the factory bean class
     */
    protected abstract Class<?> getFactoryBeanClass();

    /**
     * Gets the repository type name for logging purposes.
     *
     * @return the repository type name (e.g., "MongoDB", "Reactive MongoDB")
     */
    protected abstract String getRepositoryTypeName();

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(getEnableAnnotationClass().getName());
        if (attributes == null) {
            return;
        }

        Set<String> basePackages = getBasePackages(metadata, attributes);

        if (basePackages.isEmpty()) {
            log.warn("No base packages specified for @{}, using configuration class package",
                getEnableAnnotationClass().getSimpleName());
            basePackages.add(ClassUtils.getPackageName(metadata.getClassName()));
        }

        log.info("Scanning for {} repositories in packages: {}", getRepositoryTypeName(), basePackages);

        ClassPathScanningCandidateComponentProvider scanner = createScanner();

        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = scanner.findCandidateComponents(basePackage);

            for (BeanDefinition candidate : candidates) {
                String beanClassName = candidate.getBeanClassName();
                if (beanClassName == null) {
                    continue;
                }

                try {
                    Class<?> repositoryInterface = Class.forName(beanClassName);

                    if (repositoryInterface.isInterface() && isTargetRepository(repositoryInterface)) {
                        registerRepositoryBean(registry, repositoryInterface);
                    }
                } catch (ClassNotFoundException e) {
                    log.error("Failed to load repository class: {}", beanClassName, e);
                }
            }
        }
    }

    /**
     * Creates a scanner configured to find repository interfaces.
     */
    protected ClassPathScanningCandidateComponentProvider createScanner() {
        ClassPathScanningCandidateComponentProvider scanner =
            new ClassPathScanningCandidateComponentProvider(false) {
                @Override
                protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                    AnnotationMetadata metadata = beanDefinition.getMetadata();
                    return metadata.isIndependent() && metadata.isInterface();
                }
            };

        for (Class<?> baseClass : getRepositoryBaseClasses()) {
            scanner.addIncludeFilter(new AssignableTypeFilter(baseClass));
        }

        return scanner;
    }

    /**
     * Checks if the given interface is a target repository interface.
     */
    protected boolean isTargetRepository(Class<?> repositoryInterface) {
        for (Class<?> baseClass : getRepositoryBaseClasses()) {
            if (baseClass.isAssignableFrom(repositoryInterface)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Registers a repository interface as a Spring bean using FactoryBean.
     */
    protected void registerRepositoryBean(BeanDefinitionRegistry registry, Class<?> repositoryInterface) {
        String beanName = StringUtils.uncapitalize(repositoryInterface.getSimpleName());

        if (registry.containsBeanDefinition(beanName)) {
            log.debug("Bean already registered: {}", beanName);
            return;
        }

        BeanDefinitionBuilder builder = BeanDefinitionBuilder
            .genericBeanDefinition(getFactoryBeanClass());

        builder.addConstructorArgValue(repositoryInterface);
        builder.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE);

        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());

        log.info("Registered {} repository: {} -> {}", getRepositoryTypeName(), beanName, repositoryInterface.getName());
    }

    /**
     * Extracts base packages from annotation attributes.
     */
    protected Set<String> getBasePackages(AnnotationMetadata metadata, Map<String, Object> attributes) {
        Set<String> basePackages = new HashSet<>();

        String[] packages = (String[]) attributes.get("basePackages");
        if (packages != null) {
            basePackages.addAll(Arrays.asList(packages));
        }

        Class<?>[] basePackageClasses = (Class<?>[]) attributes.get("basePackageClasses");
        if (basePackageClasses != null) {
            for (Class<?> clazz : basePackageClasses) {
                basePackages.add(ClassUtils.getPackageName(clazz));
            }
        }

        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(metadata.getClassName()));
        }

        return basePackages;
    }
}
