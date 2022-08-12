package com.nodes.project.api.dto.agv;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.nodes.project.api.enums.AgvTypeEnum;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * AGV同步状态枚举 自定义序列化
 */
public class AgvTypeEnumSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        AgvTypeEnum agvTypeEnum = (AgvTypeEnum) object;
        serializer.out.writeString(agvTypeEnum.getDesc());
    }
}
