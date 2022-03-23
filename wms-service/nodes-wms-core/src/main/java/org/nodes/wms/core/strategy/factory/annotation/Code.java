package org.nodes.wms.core.strategy.factory.annotation;

import org.nodes.wms.core.strategy.enums.OutstockFuncEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 策略编码
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Code {
	OutstockFuncEnum value() default OutstockFuncEnum.UN_KNOWN;
}
