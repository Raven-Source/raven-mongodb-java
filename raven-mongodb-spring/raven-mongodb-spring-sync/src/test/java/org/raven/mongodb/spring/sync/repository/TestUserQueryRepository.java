package org.raven.mongodb.spring.sync.repository;

import org.raven.mongodb.MongoQueryRepository;
import org.raven.mongodb.spring.sync.entity.TestUser;
import org.springframework.stereotype.Repository;

/**
 * Test query-only repository interface for sync operations.
 * This interface will be automatically implemented by the Spring integration.
 *
 * @author yi.liang
 * @since 3.1.0
 */
@Repository
public interface TestUserQueryRepository extends MongoQueryRepository<TestUser, Long> {
}
