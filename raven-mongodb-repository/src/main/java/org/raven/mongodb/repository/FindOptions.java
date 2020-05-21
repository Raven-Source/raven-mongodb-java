package org.raven.mongodb.repository;

import com.mongodb.ReadPreference;
import org.bson.BsonValue;
import org.bson.conversions.Bson;

import java.util.List;

/**
 * @author yi.liang
 * @since JDK1.8
 */
public class FindOptions {
    private ReadPreference readPreference;
    private BsonValue hint;
    private Bson filter;
    private List<String> includeFields;
    private Bson sort;
    private int limit;
    private int skip;

    public ReadPreference getReadPreference() {
        return readPreference;
    }

    public BsonValue getHint() {
        return hint;
    }

    public Bson getFilter() {
        return filter;
    }

    public List<String> getIncludeFields() {
        return includeFields;
    }

    public Bson getSort() {
        return sort;
    }

    public int getLimit() {
        return limit;
    }

    public int getSkip() {
        return skip;
    }

    public FindOptions readPreference(ReadPreference readPreference) {
        this.readPreference = readPreference;
        return this;
    }

    public FindOptions hint(BsonValue hint) {
        this.hint = hint;
        return this;
    }

    public FindOptions filter(Bson filter) {
        this.filter = filter;
        return this;
    }

    public FindOptions includeFields(List<String> includeFields) {
        this.includeFields = includeFields;
        return this;
    }

    public FindOptions sort(Bson sort) {
        this.sort = sort;
        return this;
    }

    public FindOptions limit(int limit) {
        this.limit = limit;
        return this;
    }

    public FindOptions skip(int skip) {
        this.skip = skip;
        return this;
    }

    /**
     *
     */
    public FindOptions() {
    }


    /**
     * @return
     */
    public static FindOptions Empty() {
        return new FindOptions();
    }

}
