package org.raven.mongodb.spring.reactive.repository;

import org.raven.mongodb.reactive.ReactiveMongoQueryRepository;
import org.raven.mongodb.spring.reactive.entity.TestUser;
import org.springframework.stereotype.Repository;

/**
 * Test query-only repository interface for reactive operations.
 * This interface will be automatically implemented by the Spring integration.
 *
 * @author yi.liang
 * @since 3.1.0
 */
@Repository
public interface TestUserQueryRepository extends ReactiveMongoQueryRepository<TestUser, Long> {
}
