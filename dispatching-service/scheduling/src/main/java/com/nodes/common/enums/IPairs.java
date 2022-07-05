package com.nodes.common.enums;

/**
 * 枚举公共接口
 *
 * @param <K> 枚举key的类型
 * @param <V> 枚举value的类型
 * @param <C> 具体的枚举类
 */
public interface IPairs<K, V, C extends Enum<?>> {

    /**
     * 返回枚举对象
     *
     * @return 具体的枚举对象
     */
    C get();

    /**
     * 返回枚举项的 key
     *
     * @return 具体的枚举key
     */
    K key();

    /**
     * 返回枚举项的 value
     *
     * @return 具体的枚举value
     */
    V value();
}
