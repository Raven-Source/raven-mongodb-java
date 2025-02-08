package org.raven.mongodb;

import com.mongodb.ReadPreference;
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
public abstract class AbstractFindOptions<P extends AbstractFindOptions<P>> implements Options {

    private ReadPreference readPreference;
    private Bson hint;
    private Bson filter;

    public P readPreference(ReadPreference readPreference) {
        this.readPreference = readPreference;
        return (P) this;
    }

    public P hint(Bson hint) {
        this.hint = hint;
        return (P) this;
    }

    public P filter(Bson filter) {
        this.filter = filter;
        return (P) this;
    }

}
