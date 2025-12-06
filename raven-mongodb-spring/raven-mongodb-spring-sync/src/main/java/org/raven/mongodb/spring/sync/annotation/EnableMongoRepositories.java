package org.raven.mongodb.spring.sync.annotation;

import org.raven.mongodb.spring.sync.registrar.MongoRepositoryRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enables scanning for MongoDB repository interfaces that extend MongoRepository or MongoQueryRepository.
 * Repositories will be automatically registered as Spring beans with dynamic proxy implementations.
 *
 * <p>Example usage:
 * <pre>
 * &#64;Configuration
 * &#64;EnableMongoRepositories(basePackages = "com.example.repository")
 * public class MongoConfig {
 * }
 * </pre>
 *
 * @author yi.liang
 * @since 3.1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MongoRepositoryRegistrar.class)
public @interface EnableMongoRepositories {

    /**
     * Base packages to scan for repository interfaces.
     * If not specified, scanning will occur from the package of the annotated configuration class.
     *
     * @return the base packages to scan
     */
    String[] basePackages() default {};

    /**
     * Type-safe alternative to {@link #basePackages()} for specifying the packages to scan.
     * The package of each class specified will be scanned.
     *
     * @return the base package classes
     */
    Class<?>[] basePackageClasses() default {};
}