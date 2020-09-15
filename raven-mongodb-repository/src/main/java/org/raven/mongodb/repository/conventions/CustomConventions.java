package org.raven.mongodb.repository.conventions;

import org.bson.codecs.pojo.Convention;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static org.bson.codecs.pojo.Conventions.ANNOTATION_CONVENTION;
import static org.bson.codecs.pojo.Conventions.CLASS_AND_PROPERTY_CONVENTION;

/**
 * @author yi.liang
 * @since JDK1.8
 */
public final class CustomConventions {

    /**
     *
     */
    public static final Convention PROPERTY_FORMAT_CONVENTION = new ConventionPropertyFormatImpl();

    /**
     *
     */
    public static final List<Convention> DEFAULT_CONVENTIONS =
            unmodifiableList(asList(CLASS_AND_PROPERTY_CONVENTION, ANNOTATION_CONVENTION, PROPERTY_FORMAT_CONVENTION));


    /**
     *
     */
    private CustomConventions() {
    }
}
