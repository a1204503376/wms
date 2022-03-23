package org.nodes.wms.core.common.enums;

import org.springblade.core.tool.utils.StringUtil;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


/**
 * @desc 校验枚举值有效性
 *
 * @author zhumaer
 * @since 10/17/2017 3:13 PM
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValue.Validator.class)
public @interface EnumValue {

	String message() default "{custom.value.invalid}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	Class<? extends Enum<?>> enumClass();

	String enumMethod();

	class Validator implements ConstraintValidator<EnumValue, Object> {

		private Class<? extends Enum<?>> enumClass;
		private String enumMethod;

		@Override
		public void initialize(EnumValue enumValue) {
			enumMethod = enumValue.enumMethod();
			enumClass = enumValue.enumClass();
		}

		@Override
		public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
			if (value == null) {
				return Boolean.TRUE;
			}

			if (enumClass == null || enumMethod == null) {
				return Boolean.TRUE;
			}

			Class<?> valueClass = value.getClass();

			try {
				Method method = enumClass.getMethod(enumMethod, valueClass);
				if (!Boolean.TYPE.equals(method.getReturnType()) && !Boolean.class.equals(method.getReturnType())) {
					throw new RuntimeException(StringUtil.format("%s method return is not boolean type in the %s class", enumMethod, enumClass));
				}

				if(!Modifier.isStatic(method.getModifiers())) {
					throw new RuntimeException(StringUtil.format("%s method is not static method in the %s class", enumMethod, enumClass));
				}

				Boolean result = (Boolean)method.invoke(null, value);
				return result == null ? false : result;
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new RuntimeException(StringUtil.format("This %s(%s) method does not exist in the %s", enumMethod, valueClass, enumClass), e);
			}
		}

	}
}
