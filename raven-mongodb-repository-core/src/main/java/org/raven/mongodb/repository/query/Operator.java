package org.raven.mongodb.repository.query;

/**
 * @author by yanfeng
 * date 2021/9/13 21:18
 */
public enum Operator {

    AND("$and", "And"),
    OR("$or", "Or"),
    NOR("$nor", "Nor");

    private final String name;
    private final String toStringName;

    Operator(final String name, final String toStringName) {
        this.name = name;
        this.toStringName = toStringName;
    }
}