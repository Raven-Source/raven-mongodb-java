package org.raven.mongodb.repository.reactive;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.operation.ModifyOperation;
import reactor.core.publisher.Mono;


/**
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 * @since JDK11
 */
public interface ReactiveMongoRepository<TEntity extends Entity<TKey>, TKey>
        extends ReactiveMongoReaderRepository<TEntity, TKey>
        , ModifyOperation<TEntity, TKey, Mono<InsertOneResult>, Mono<InsertManyResult>, Mono<UpdateResult>, Mono<TEntity>, Mono<DeleteResult>> {

}
