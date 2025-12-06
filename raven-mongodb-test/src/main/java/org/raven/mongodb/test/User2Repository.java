package org.raven.mongodb.test;

import org.raven.mongodb.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface User2Repository extends MongoRepository<User2, Long> {

}