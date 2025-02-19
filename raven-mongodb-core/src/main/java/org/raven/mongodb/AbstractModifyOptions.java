package org.raven.mongodb;

import com.mongodb.WriteConcern;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author by yanfeng
 * date 2021/9/22 21:17
 */
@Data
@Accessors(fluent = true)
@SuppressWarnings("unchecked")
public abstract class AbstractModifyOptions<P extends AbstractModifyOptions<P>> extends AbstractFilterOptions<P> implements CommandOptions {

    private WriteConcern writeConcern;

    public P writeConcern(WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
        return (P) this;
    }
}
