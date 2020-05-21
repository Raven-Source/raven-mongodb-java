package org.raven.mongodb.repository;

import com.mongodb.ReadPreference;
import org.bson.conversions.Bson;

/**
 * @author yi.liang
 * @since JDK1.8
 */
public class CountOptions extends com.mongodb.client.model.CountOptions {
    private Bson filter;
    private ReadPreference readPreference;

    public Bson getFilter() {
        return filter;
    }

    /**
     * @return ReadPreference
     */
    public ReadPreference getReadPreference() {
        return readPreference;
    }

    /**
     * @param readPreference
     * @return CountOptions
     */
    public CountOptions readPreference(ReadPreference readPreference) {
        this.readPreference = readPreference;
        return this;
    }

    /**
     * @param filter Bson
     * @return CountOptions
     */
    public CountOptions filter(Bson filter) {
        this.filter = filter;
        return this;
    }

    /**
     * @return CountOptions
     */
    public static CountOptions Empty() {
        return new CountOptions();
    }
}
