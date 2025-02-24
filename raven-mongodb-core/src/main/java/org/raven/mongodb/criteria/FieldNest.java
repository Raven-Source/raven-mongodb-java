package org.raven.mongodb.criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yi.liang
 * date 2021/9/13 0:33
 */
public class FieldNest {

    private final List<String> fields;

    private static final String DELIMITER = ".";

    public FieldNest() {
        fields = new ArrayList<>();
    }

    public static FieldNest create() {
        return new FieldNest();
    }

    public FieldNest link(String... fields) {

        this.fields.addAll(Arrays.asList(fields));

        return this;
    }

    public List<String> getFields() {
        return fields;
    }

    public String build() {
        return String.join(DELIMITER, fields);
    }


    @Override
    public String toString() {
        return build();
    }

}
