package org.raven.mongodb.criteria;

import lombok.Getter;

/**
 * @author yi.liang
 * date 2021/9/13 21:18
 */
public enum Operator {

    AND("$and", "And"),
    OR("$or", "Or"),
    NOR("$nor", "Nor");

    @Getter
    private final String name;

    @Getter
    private final String toStringName;

    Operator(final String name, final String toStringName) {
        this.name = name;
        this.toStringName = toStringName;
    }
}
