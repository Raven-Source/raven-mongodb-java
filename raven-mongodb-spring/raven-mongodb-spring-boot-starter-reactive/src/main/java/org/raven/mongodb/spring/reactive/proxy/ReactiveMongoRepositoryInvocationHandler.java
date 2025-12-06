package org.raven.mongodb.spring.reactive.proxy;

import lombok.extern.slf4j.Slf4j;
import org.raven.mongodb.reactive.ReactiveMongoQueryRepository;
import org.raven.mongodb.reactive.ReactiveMongoQueryRepositoryImpl;
import org.raven.mongodb.reactive.ReactiveMongoRepository;
import org.raven.mongodb.reactive.ReactiveMongoRepositoryImpl;
import org.raven.mongodb.reactive.ReactiveMongoSession;
import org.raven.mongodb.spring.common.proxy.AbstractRepositoryInvocationHandler;

/**
 * InvocationHandler that delegates method calls from reactive repository interfaces to their implementations.
 * <p>
 * This handler creates the appropriate implementation (ReactiveMongoRepositoryImpl or ReactiveMongoQueryRepositoryImpl)
 * based on the repository interface type and delegates all method invocations to it.
 * All methods return reactive types (Mono/Flux) for non-blocking operations.
 * </p>
 *
 * @author yi.liang
 * @since 3.1.0
 */
@Slf4j
public class ReactiveMongoRepositoryInvocationHandler extends AbstractRepositoryInvocationHandler {

    private final ReactiveMongoSession reactiveMongoSession;

    public ReactiveMongoRepositoryInvocationHandler(Class<?> repositoryInterface, ReactiveMongoSession reactiveMongoSession) {
        super(repositoryInterface);
        this.reactiveMongoSession = reactiveMongoSession;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected Object createRepositoryImplementation() {
        log.debug("Creating reactive repository implementation for: {} with entityClass={}, keyClass={}",
                repositoryInterface.getName(), entityClass.getName(), keyClass.getName());

        // Determine if this is a full repository or query-only repository
        if (ReactiveMongoRepository.class.isAssignableFrom(repositoryInterface)) {
            return new ReactiveMongoRepositoryImpl(entityClass, keyClass, reactiveMongoSession, null, null);
        } else if (ReactiveMongoQueryRepository.class.isAssignableFrom(repositoryInterface)) {
            return new ReactiveMongoQueryRepositoryImpl(entityClass, keyClass, reactiveMongoSession, null, null);
        } else {
            throw new IllegalArgumentException(
                "Repository interface must extend ReactiveMongoRepository or ReactiveMongoQueryRepository: "
                + repositoryInterface.getName()
            );
        }
    }

    @Override
    protected String getRepositoryTypeName() {
        return "ReactiveMongoRepository";
    }
}
