package com.mk.manageuserpro.validator;

import com.mk.manageuserpro.model.JapanLevel;
import com.mk.manageuserpro.model.UserDetailJapan;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Date;

public class RequiredOnInputJapanLevelValidator implements ConstraintValidator<RequiredOnInputJapanLevel, UserDetailJapan> {

    private String requireFieldName;

    @Override
    public void initialize(final RequiredOnInputJapanLevel constraintAnnotation) {
        requireFieldName = constraintAnnotation.requireField();
    }

    @Override
    public boolean isValid(final UserDetailJapan value, final ConstraintValidatorContext context) {
        try {
            final Field japanLevelField = value.getClass().getDeclaredField("japanLevel");
            japanLevelField.setAccessible(true);

            final Field requireField = value.getClass().getDeclaredField(requireFieldName);
            requireField.setAccessible(true);

            final String japanLevel = ((JapanLevel) japanLevelField.get(value)).getCodeLevel();
            final Object requireFieldValue = requireField.get(value);
            return japanLevel == null || requireFieldValue != null;

        } catch (final Exception e) {
            e.printStackTrace();

            return false;
        }
    }
}

