package org.raven.mongodb.criteria;

import lombok.Getter;
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
public abstract class BaseFilterOptions<P extends BaseFilterOptions<P>> implements CommandOptions {

    private Bson filter;
    private Bson hint;

    public P filter(Bson bson) {
        this.filter = bson;
        return (P) this;
    }

    public P hint(Bson bson) {
        this.hint = bson;
        return (P) this;
    }

    public Bson toBson() {
        return BsonUtils.combine(
                BsonUtils.combine("filter", filter),
                BsonUtils.combine("hint", hint)
        );

    }

    @Override
    public String toString() {
        return toBson().toString();
    }


}
