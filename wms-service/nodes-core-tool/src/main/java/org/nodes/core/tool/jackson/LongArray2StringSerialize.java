package org.nodes.core.tool.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 针对Long类型集合的序列化器
 */
@Slf4j
public class LongArray2StringSerialize extends JsonSerializer<List<Long>> {

	@Override
	public void serialize(List<Long> value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
		List<String> strList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(value)) {
			for (Long aLong : value) {
				strList.add(String.valueOf(aLong));
			}
		}
		jsonGenerator.writeObject(strList);
	}
}

