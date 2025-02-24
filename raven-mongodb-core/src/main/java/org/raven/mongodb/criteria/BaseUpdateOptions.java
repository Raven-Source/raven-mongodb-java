package org.raven.mongodb.criteria;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.bson.BsonBoolean;
import org.bson.conversions.Bson;
import org.raven.mongodb.util.BsonUtils;

@Getter
@Accessors(fluent = true)
@SuppressWarnings("unchecked")
public abstract class BaseUpdateOptions<P extends BaseUpdateOptions<P>> extends BaseModifyOptions<P> {

    private Bson update;

    private boolean upsert;

    public P update(Bson update) {
        this.update = update;
        return (P) this;
    }

    public P upsert(boolean upsert) {
        this.upsert = upsert;
        return (P) this;
    }

    @Override
    public Bson toBson() {
        return BsonUtils.combine(
                super.toBson(),
                BsonUtils.combine("update", update),
                BsonUtils.combine("upsert", BsonBoolean.valueOf(upsert))
        );

    }
}
