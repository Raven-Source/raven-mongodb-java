package org.raven.mongodb.spi;

import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author yi.liang
 */
public interface ReactiveIdGenerator<TKey> {

    Mono<TKey> generateId();

    Mono<List<TKey>> generateIdBatch(long count);

    String name();

}
