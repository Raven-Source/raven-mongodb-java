package org.raven.mongodb.test.model;


import org.raven.commons.data.ValueType;

public enum Status implements ValueType<Integer> {
    Delete(-1),
    Normal(1),
    Finish(3),
    Fail(4),
    ;

    private int value;

    Status(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

//    @Override
//    public String getName() {
//        return this.name();
//    }
}