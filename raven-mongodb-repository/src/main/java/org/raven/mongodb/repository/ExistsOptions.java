package org.raven.mongodb.repository;

import com.mongodb.ReadPreference;
import org.bson.BsonValue;
import org.bson.conversions.Bson;

/**
 * @author yi.liang
 * @since JDK1.8
 */
public class ExistsOptions {
    private ReadPreference readPreference;
    private BsonValue hint;
    private Bson filter;

    public ReadPreference getReadPreference() {
        return readPreference;
    }

    public BsonValue getHint() {
        return hint;
    }

    public Bson getFilter() {
        return filter;
    }


    public ExistsOptions readPreference(ReadPreference readPreference) {
        this.readPreference = readPreference;
        return this;
    }

    public ExistsOptions hint(BsonValue hint) {
        this.hint = hint;
        return this;
    }

    public ExistsOptions filter(Bson filter) {
        this.filter = filter;
        return this;
    }

    /**
     * @return
     */
    public static ExistsOptions Empty() {
        return new ExistsOptions();
    }
}
