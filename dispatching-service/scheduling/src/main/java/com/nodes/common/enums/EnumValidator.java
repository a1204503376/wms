package com.nodes.common.enums;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;

/**
 * 校验入参是否为指定enum的值的实现方法
 */
@Slf4j
public class EnumValidator implements ConstraintValidator<EnumValid, Object> {

    String validField;
    String methodName = "code";
    Class<?>[] cls; //枚举类

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        cls = constraintAnnotation.target();
        validField = constraintAnnotation.validField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value != null && value.toString().length() > 0 && cls.length > 0) {
            for (Class<?> cl : cls) {
                try {
                    if (cl.isEnum()) {
                        //枚举类验证
                        Object[] objs = cl.getEnumConstants();
                        Method method = cl.getMethod(methodName);
                        for (Object obj : objs) {
                            Object code = method.invoke(obj);
                            if (value.equals(code.toString())) {
                                return true;
                            }
                        }
                        Method codeMethod = cl.getMethod(validField);
                        for (Object obj : objs) {
                            Object code = codeMethod.invoke(obj);
                            if (value.toString().equals(code.toString())) {
                                return true;
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("枚举校验异常", e);
                }
            }
        } else {
            return true;
        }
        return false;
    }

}