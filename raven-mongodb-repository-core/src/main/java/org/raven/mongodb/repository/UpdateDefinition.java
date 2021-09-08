//package org.raven.mongodb.repository;
//
//import com.mongodb.assertions.Assertions;
//import com.mongodb.client.model.Updates;
//import lombok.NonNull;
//import org.bson.conversions.Bson;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
///**
// * @author by yi.liang
// * date 2021/9/4 18:34
// */
//public abstract class UpdateDefinition<TDocument> {
//
//    private Bson updateBson;
//
//    public UpdateDefinition<TDocument> combine(Bson... updates) {
//
//        return combine(Arrays.asList(updates));
//    }
//
//    public UpdateDefinition<TDocument> combine(@NonNull List<? extends Bson> updates) {
//        updates.add(updateBson);
//
//        this.update = new Updates.CompositeUpdate(updates);
//
//        return this;
//    }
//
//}
