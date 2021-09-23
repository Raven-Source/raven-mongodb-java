package org.raven.mongodb.repository;

import org.raven.commons.data.MemberFormatType;
import org.raven.commons.data.MemberFormatUtils;
import org.raven.commons.data.annotation.Contract;
import org.raven.commons.util.StringUtils;
import org.raven.mongodb.repository.util.AnnotationUtils;

import java.lang.annotation.Annotation;

/**
 * @author yi.liang
 * date 2021.07.28 22:23
 */
public abstract class DocumentNamed {

    public static String getNamed(Class<?> documentClass) {

        MemberFormatType formatType = MemberFormatType.CamelCase;
        String name = null;

        Contract contract = AnnotationUtils.findAnnotation(documentClass, Contract.class);
        if (contract != null) {
            formatType = contract.formatType();
            name = contract.value();
        }

        name = StringUtils.isBlank(name) ? MemberFormatUtils.namingFormat(documentClass.getSimpleName(), formatType) : name;
        return name;
    }
}
