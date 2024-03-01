package org.raven.mongodb.repository.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yi.liang
 * date 2021/9/13 0:33
 */
public class FieldNest {

    private List<String> fields;

    private static final String delimiter = ".";

    public FieldNest() {
        fields = new ArrayList<>();
    }

    public static FieldNest empty() {
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
        return String.join(delimiter, fields);
    }

}
