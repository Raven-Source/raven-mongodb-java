package org.raven.mongodb.repository.model;


import org.raven.commons.data.ValueType;

public enum Status implements ValueType<Integer> {
    Normal(1), Delete(-1);

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