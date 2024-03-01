package org.raven.mongodb.reactive;

import org.raven.commons.data.Entity;
import org.raven.mongodb.operation.ModifyOperation;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

public interface ReactiveModifyOperation<TEntity extends Entity<TKey>, TKey>
        extends ModifyOperation<TEntity, TKey, Mono<Optional<TKey>>, Mono<Map<Integer, TKey>>, Mono<Long>, Mono<TEntity>, Mono<Long>> {
}
