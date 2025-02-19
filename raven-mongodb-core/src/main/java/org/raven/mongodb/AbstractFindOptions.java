package org.raven.mongodb;

import com.mongodb.ReadPreference;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author by yanfeng
 * date 2021/9/22 21:17
 */
@Data
@Accessors(fluent = true)
@SuppressWarnings("unchecked")
public abstract class AbstractFindOptions<P extends AbstractFindOptions<P>> extends AbstractFilterOptions<P> implements CommandOptions {

    private ReadPreference readPreference;

    public P readPreference(ReadPreference readPreference) {
        this.readPreference = readPreference;
        return (P) this;
    }
}
