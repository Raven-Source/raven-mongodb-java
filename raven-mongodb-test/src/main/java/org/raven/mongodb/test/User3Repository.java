package org.raven.mongodb.test;

import org.raven.mongodb.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface User3Repository extends MongoRepository<User3, Long> {
}
