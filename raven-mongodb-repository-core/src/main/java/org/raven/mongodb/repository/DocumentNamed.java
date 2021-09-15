package org.raven.mongodb.repository;

import org.raven.commons.data.MemberFormatType;
import org.raven.commons.data.MemberFormatUtils;
import org.raven.commons.data.annotation.Contract;
import org.raven.commons.util.StringUtils;

import java.lang.annotation.Annotation;

/**
 * @author yi.liang
 * date 2021.07.28 22:23
 */
public abstract class DocumentNamed {

    public static String getNamed(Class<?> documentClass) {

//        MemberFormatType formatType = MemberFormatType.CamelCase;
        String name = null;
        for (final Annotation annotation : documentClass.getAnnotations()) {
            if (annotation instanceof Contract) {
//                formatType = ((Contract) annotation).formatType();
                name = ((Contract) annotation).value();
                break;
            }
        }

        name = StringUtils.isBlank(name) ? documentClass.getSimpleName() : name;
//        name = MemberFormatUtils.namingFormat(name, formatType);
        return name;
    }
}
