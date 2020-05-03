package com.jjs.miaosha.validate;

import com.jjs.miaosha.util.ValidatorUtil;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author jjs
 * @Version 1.0 2020/4/18
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (required) {
            return ValidatorUtil.isMobile(value);
        }
        if (StringUtils.isEmpty(value)) {
            return true;
        } else {
            return ValidatorUtil.isMobile(value);
        }
    }
}
