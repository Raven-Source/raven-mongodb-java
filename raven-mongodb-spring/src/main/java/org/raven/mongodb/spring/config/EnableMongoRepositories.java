package org.raven.mongodb.spring.config;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EnableMongoRepositories {
    String[] value() default {};
}
