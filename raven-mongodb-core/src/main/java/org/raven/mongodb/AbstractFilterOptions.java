package org.raven.mongodb;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.conversions.Bson;

/**
 * @author by yanfeng
 * date 2021/9/22 21:17
 */
@Data
@Accessors(fluent = true)
@SuppressWarnings("unchecked")
public abstract class AbstractFilterOptions<P extends AbstractFilterOptions<P>> implements CommandOptions {

    private Bson filter;
    private Bson hint;

    public P filter(Bson filter) {
        this.filter = filter;
        return (P) this;
    }

    public P hint(Bson hint) {
        this.hint = hint;
        return (P) this;
    }

}
