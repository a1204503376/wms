package org.nodes.core.tool.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.nodes.core.tool.utils.BigDecimalUtil;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author pengwei
 * @program WmsCore
 * @description 处理 BigDecimal 小数点位数
 * @since 2020-10-23
 */
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {

	@Override
	public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		if (value != null) {
			BigDecimal number = BigDecimalUtil.globeScale(value);
			gen.writeNumber(number);
		} else {
			gen.writeNumber(value);
		}
	}
}
