package org.raven.mongodb.repository.spi;

import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author yi.liang
 * @since JDK11
 */
public interface ReactiveIdGenerator<TKey> {

    Mono<TKey> generateId();

    Mono<List<TKey>> generateIdBatch(long count);

    String name();

}
