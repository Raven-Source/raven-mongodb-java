package org.raven.mongodb.conventions;

import org.bson.codecs.pojo.ClassModelBuilder;
import org.bson.codecs.pojo.Convention;
import org.bson.codecs.pojo.PropertyModelBuilder;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.raven.commons.data.MemberFormatType;
import org.raven.commons.data.MemberFormatUtils;
import org.raven.commons.data.annotation.Contract;
import org.raven.commons.data.annotation.Ignore;
import org.raven.commons.data.annotation.Member;
import org.raven.mongodb.util.AnnotationUtils;

import java.lang.annotation.Annotation;

/**
 * @author yi.liang
 */
final class ConventionPropertyFormatImpl implements Convention {

    /**
     * @param classModelBuilder
     */
    @Override
    public void apply(final ClassModelBuilder<?> classModelBuilder) {

        MemberFormatType formatType = MemberFormatType.CamelCase;

        Contract contract = AnnotationUtils.findAnnotation(classModelBuilder.getType(), Contract.class);
        if (contract != null) {
            formatType = contract.formatType();
        }

//        for (final Annotation annotation : classModelBuilder.getAnnotations()) {
//            if (annotation instanceof Contract) {
//                formatType = ((Contract) annotation).formatType();
//                break;
//            }
//        }

//        classModelBuilder.idGenerator(IdGenerators.OBJECT_ID_GENERATOR);

        for (PropertyModelBuilder<?> propertyModelBuilder : classModelBuilder.getPropertyModelBuilders()) {
//            propertyModelBuilder.propertySerialization(new CustomPropertySerialization<>());
            processPropertyAnnotations(classModelBuilder, propertyModelBuilder, formatType);
        }
    }

    /**
     * @param classModelBuilder
     * @param propertyModelBuilder
     * @param formatType
     */
    private void processPropertyAnnotations(final ClassModelBuilder<?> classModelBuilder,
                                            final PropertyModelBuilder<?> propertyModelBuilder,
                                            final MemberFormatType formatType) {

        Member readMember = null;
        for (Annotation annotation : propertyModelBuilder.getReadAnnotations()) {

            if (annotation instanceof BsonProperty) {
                break;
            } else if (annotation instanceof Member) {
                readMember = (Member) annotation;
            } else if (annotation instanceof Ignore) {
                propertyModelBuilder.readName(null);
            }
        }

        if (readMember != null && !"".equals(readMember.value())) {
            propertyModelBuilder.readName(readMember.value());
        } else {
            String name = propertyModelBuilder.getName();
            name = MemberFormatUtils.namingFormat(name, formatType);
            propertyModelBuilder.readName(name);
        }

        Member writeMember = null;
        for (Annotation annotation : propertyModelBuilder.getWriteAnnotations()) {

            if (annotation instanceof BsonProperty) {
                break;
            } else if (annotation instanceof Member) {
                writeMember = (Member) annotation;
            } else if (annotation instanceof Ignore) {
                propertyModelBuilder.writeName(null);
            }
        }

        if (writeMember != null && !"".equals(writeMember.value())) {
            propertyModelBuilder.writeName(writeMember.value());
        } else {
            String name = propertyModelBuilder.getName();
            name = MemberFormatUtils.namingFormat(name, formatType);
            propertyModelBuilder.writeName(name);
        }

    }
}
