package com.mk.manageuserpro.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = RequiredOnInputJapanLevelValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
@Documented
public @interface RequiredOnInputJapanLevel {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String requireField();

    @Target({TYPE})
    @Retention(RUNTIME)
    @interface List {
        RequiredOnInputJapanLevel[] value();
    }
}
