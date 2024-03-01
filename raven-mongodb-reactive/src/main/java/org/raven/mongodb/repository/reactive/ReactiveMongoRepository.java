package org.raven.mongodb.repository.reactive;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.operation.ModifyOperation;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;


/**
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 * @since JDK11
 */
public interface ReactiveMongoRepository<TEntity extends Entity<TKey>, TKey>
        extends ReactiveMongoReadonlyRepository<TEntity, TKey>
        , ModifyOperation<TEntity, TKey, Mono<Optional<TKey>>, Mono<Map<Integer, TKey>>, Mono<Long>, Mono<TEntity>, Mono<Long>> {

}
