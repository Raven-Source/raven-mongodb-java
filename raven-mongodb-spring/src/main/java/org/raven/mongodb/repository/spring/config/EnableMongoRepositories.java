package org.raven.mongodb.repository.spring.config;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EnableMongoRepositories {
    String[] value() default {};
}
