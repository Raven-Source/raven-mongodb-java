package org.raven.mongodb.spring.sync.entity;

import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.raven.commons.data.AutoIncr;
import org.raven.commons.data.Entity;

/**
 * Test entity for sync repository tests.
 *
 * @author yi.liang
 * @since 3.1.0
 */
@FieldNameConstants
@Data
public class TestUser implements Entity<Long>, AutoIncr<Long> {

    private Long id;

    private String name;

    private String email;

    private Integer age;

    private String status;

    public TestUser() {
    }

    public TestUser(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.status = "ACTIVE";
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
