package org.raven.mongodb.criteria;

import com.mongodb.ReadPreference;
import lombok.*;
import lombok.experimental.Accessors;
import org.bson.BsonNull;
import org.bson.conversions.Bson;
import org.raven.mongodb.util.BsonUtils;

/**
 * @author by yanfeng
 * date 2021/9/22 21:17
 */
@Getter
@Accessors(fluent = true)
@SuppressWarnings("unchecked")
public abstract class BaseFindOptions<P extends BaseFindOptions<P>> extends BaseFilterOptions<P> implements CommandOptions {

    private ReadPreference readPreference;

    public P readPreference(ReadPreference readPreference) {
        this.readPreference = readPreference;
        return (P) this;
    }

    @Override
    public Bson toBson() {
        return BsonUtils.combine(
                super.toBson(),
                BsonUtils.combine("readPreference", readPreference != null ? readPreference.toDocument() : BsonNull.VALUE)
        );

    }
}
