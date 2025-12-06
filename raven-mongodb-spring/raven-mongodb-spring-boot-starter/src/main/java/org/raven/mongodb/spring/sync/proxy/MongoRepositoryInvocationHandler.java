package org.raven.mongodb.spring.sync.proxy;

import lombok.extern.slf4j.Slf4j;
import org.raven.mongodb.MongoQueryRepository;
import org.raven.mongodb.MongoQueryRepositoryImpl;
import org.raven.mongodb.MongoRepository;
import org.raven.mongodb.MongoRepositoryImpl;
import org.raven.mongodb.MongoSession;
import org.raven.mongodb.spring.common.proxy.AbstractRepositoryInvocationHandler;

/**
 * InvocationHandler that delegates method calls from repository interfaces to their implementations.
 * <p>
 * This handler creates the appropriate implementation (MongoRepositoryImpl or MongoQueryRepositoryImpl)
 * based on the repository interface type and delegates all method invocations to it.
 * </p>
 *
 * @author yi.liang
 * @since 3.1.0
 */
@Slf4j
public class MongoRepositoryInvocationHandler extends AbstractRepositoryInvocationHandler {

    private final MongoSession mongoSession;

    /**
     * Constructs a new MongoRepositoryInvocationHandler.
     *
     * @param repositoryInterface the repository interface class
     * @param mongoSession the MongoSession instance
     */
    public MongoRepositoryInvocationHandler(Class<?> repositoryInterface, MongoSession mongoSession) {
        super(repositoryInterface);
        this.mongoSession = mongoSession;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected Object createRepositoryImplementation() {
        log.debug("Creating repository implementation for:  with entityClass={}, keyClass={}",
                repositoryInterface.getName(), entityClass.getName(), keyClass.getName());

        // Determine if this is a full repository or query-only repository
        if (MongoRepository.class.isAssignableFrom(repositoryInterface)) {
            return new MongoRepositoryImpl(entityClass, keyClass, mongoSession, null, null);
        } else if (MongoQueryRepository.class.isAssignableFrom(repositoryInterface)) {
            return new MongoQueryRepositoryImpl(entityClass, keyClass, mongoSession, null, null);
        } else {
            throw new IllegalArgumentException(
                "Repository interface must extend MongoRepository or MongoQueryRepository: "
                + repositoryInterface.getName()
            );
        }
    }

    @Override
    protected String getRepositoryTypeName() {
        return "MongoRepository";
    }
}
