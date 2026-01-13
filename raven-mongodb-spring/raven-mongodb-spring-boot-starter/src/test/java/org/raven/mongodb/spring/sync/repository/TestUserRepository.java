package org.raven.mongodb.spring.sync.repository;

import org.raven.mongodb.MongoRepository;
import org.raven.mongodb.spring.sync.entity.TestUser;
import org.springframework.stereotype.Repository;

/**
 * Test repository interface for sync operations.
 * This interface will be automatically implemented by the Spring integration.
 *
 * @author yi.liang
 * @since 3.1.0
 */
@Repository
public interface TestUserRepository extends MongoRepository<TestUser, Long> {

    default TestUser findOneByName(String name) {
        return findOne(filter -> filter.eq(TestUser.Fields.name, name));
    }
}
