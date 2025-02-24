package org.raven.mongodb.criteria;

import org.bson.conversions.Bson;

public interface CriteriaBuilder {
    Bson build();
}
