package org.raven.mongodb.repository.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.raven.commons.data.MemberFormatType;
import org.raven.commons.data.annotation.Contract;
import org.raven.mongodb.repository.annotations.EntityListeners;
import org.raven.mongodb.repository.interceptors.EntityInterceptor;
import org.raven.mongodb.repository.interceptors.VersionedEntityInterceptor;

@Contract(formatType = MemberFormatType.PascalCase)
@FieldNameConstants
@Getter
@Setter
@EntityListeners({UserExtend.TestInterceptor.class, VersionedEntityInterceptor.class})
public class UserExtend extends User {
    String remark;

    public static class TestInterceptor implements EntityInterceptor {

    }
}


