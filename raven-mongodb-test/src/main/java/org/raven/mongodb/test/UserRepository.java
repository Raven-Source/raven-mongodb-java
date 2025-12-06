package org.raven.mongodb.test;

import org.raven.mongodb.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {
}
