package org.raven.mongodb;

import org.raven.commons.data.annotation.Contract;
import org.raven.commons.util.StringUtils;
import org.raven.mongodb.util.AnnotationUtils;

/**
 * @author yi.liang
 * date 2021.07.28 22:23
 */
public abstract class DocumentNamed {

    public static String getNamed(Class<?> documentClass) {

//        MemberFormatType formatType = MemberFormatType.CamelCase;
        String name = null;

        Contract contract = AnnotationUtils.findAnnotation(documentClass, Contract.class);
        if (contract != null) {
//            formatType = contract.formatType();
            name = contract.value();
        }

        name = StringUtils.isBlank(name) ? documentClass.getSimpleName() : name;
        return name;
    }
}
