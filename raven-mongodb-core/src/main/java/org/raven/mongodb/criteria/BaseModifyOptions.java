package org.raven.mongodb.criteria;

import com.mongodb.WriteConcern;
import lombok.*;
import lombok.experimental.Accessors;
import org.bson.conversions.Bson;
import org.raven.mongodb.util.BsonUtils;

/**
 * @author by yanfeng
 * date 2021/9/22 21:17
 */
@Getter
@Accessors(fluent = true)
@SuppressWarnings("unchecked")
public abstract class BaseModifyOptions<P extends BaseModifyOptions<P>> extends BaseFilterOptions<P> {

    private WriteConcern writeConcern;

    public P writeConcern(WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
        return (P) this;
    }


    @Override
    public Bson toBson() {
        return BsonUtils.combine(
                super.toBson(),
                BsonUtils.combine("writeConcern",  writeConcern.asDocument())
        );

    }
}
