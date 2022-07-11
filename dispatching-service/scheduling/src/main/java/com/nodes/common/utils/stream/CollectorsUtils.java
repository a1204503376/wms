package com.nodes.common.utils.stream;

import org.apache.commons.lang3.ObjectUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * java8流处理
 */
public class CollectorsUtils {

    /**
     * 将集合中对象的某个属性转为集合
     *
     * @param coll     集合
     * @param function 集合中对象的属性名称
     * @return 集合
     */
    @SuppressWarnings("unchecked")
    public static <R, T> List<R> toList(@Nullable Collection<?> coll, Function<T, R> function) {
        List<R> arrayList = new ArrayList<>();
        for (Object o : Objects.requireNonNull(coll)) {
            if (ObjectUtils.isEmpty(o)) {
                continue;
            }
            R value = function.apply((T) o);
            if (ObjectUtils.isNotEmpty(value) && !arrayList.contains(value)) {
                arrayList.add(value);
            }
        }

        return arrayList;
    }
}
