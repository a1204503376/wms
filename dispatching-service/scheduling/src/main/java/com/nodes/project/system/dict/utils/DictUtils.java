package com.nodes.project.system.dict.utils;

import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.nodes.common.constant.Constants;
import com.nodes.common.exception.ServiceException;
import com.nodes.common.utils.CacheUtils;
import com.nodes.common.utils.StringUtils;
import com.nodes.project.system.dict.domain.DictData;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * 字典工具类
 *
 * @author dml
 */
public class DictUtils {
    /**
     * 分隔符
     */
    public static final String SEPARATOR = ",";

    /**
     * 设置字典缓存
     *
     * @param key       参数键
     * @param dictDatas 字典数据列表
     */
    public static void setDictCache(String key, List<DictData> dictDatas) {
        CacheUtils.put(getCacheName(), getCacheKey(key), dictDatas);
    }

    /**
     * 获取字典缓存
     *
     * @param key 参数键
     * @return 字典数据列表
     */
    public static List<DictData> getDictCache(String key) {
        Object cacheObj = CacheUtils.get(getCacheName(), getCacheKey(key));
        if (StringUtils.isNotNull(cacheObj)) {
            return StringUtils.cast(cacheObj);
        }
        return null;
    }

    /**
     * 获取调度JOB类型配置
     * Key：code，Value：名称
     */
    public static Map<String, String> getSchedulingJobTypeMap() {
        List<DictData> dictCache = getDictCache(Constants.SCHEDULING_JOB_TYPE);
        if (ObjectUtils.isEmpty(dictCache)) {
            throw new ServiceException("获取调度JOB类型配置为空");
        }
        return SimpleQuery.list2Map(dictCache, DictData::getDictValue, DictData::getDictLabel);
    }

    /**
     * 获取调度JOB类型对应的优先级
     * Key：JOB类型，Value：优先级
     */
    public static Map<String, String> getSchedulingJobPriorityMap() {
        List<DictData> dictCache = getDictCache(Constants.SCHEDULING_JOB_PRIORITY);
        if (ObjectUtils.isEmpty(dictCache)) {
            throw new ServiceException("获取调度JOB类型对应的优先级为空");
        }
        return SimpleQuery.list2Map(dictCache, DictData::getDictLabel, DictData::getDictValue);
    }

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @return 字典标签
     */
    public static String getDictLabel(String dictType, String dictValue) {
        return getDictLabel(dictType, dictValue, SEPARATOR);
    }

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @return 字典值
     */
    public static String getDictValue(String dictType, String dictLabel) {
        return getDictValue(dictType, dictLabel, SEPARATOR);
    }

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @param separator 分隔符
     * @return 字典标签
     */
    public static String getDictLabel(String dictType, String dictValue, String separator) {
        StringBuilder propertyString = new StringBuilder();
        List<DictData> datas = getDictCache(dictType);

        if (StringUtils.containsAny(separator, dictValue) && StringUtils.isNotEmpty(datas)) {
            for (DictData dict : datas) {
                for (String value : dictValue.split(separator)) {
                    if (value.equals(dict.getDictValue())) {
                        propertyString.append(dict.getDictLabel()).append(separator);
                        break;
                    }
                }
            }
        } else {
            for (DictData dict : datas) {
                if (dictValue.equals(dict.getDictValue())) {
                    return dict.getDictLabel();
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 删除指定字典缓存
     *
     * @param key 字典键
     */
    public static void removeDictCache(String key) {
        CacheUtils.remove(getCacheName(), getCacheKey(key));
    }

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @param separator 分隔符
     * @return 字典值
     */
    public static String getDictValue(String dictType, String dictLabel, String separator) {
        StringBuilder propertyString = new StringBuilder();
        List<DictData> dictDataList = getDictCache(dictType);
        if (ObjectUtils.isEmpty(dictDataList)) {
            return "";
        }
        if (StringUtils.containsAny(separator, dictLabel) && StringUtils.isNotEmpty(dictDataList)) {
            for (DictData dict : dictDataList) {
                for (String label : dictLabel.split(separator)) {
                    if (label.equals(dict.getDictLabel())) {
                        propertyString.append(dict.getDictValue()).append(separator);
                        break;
                    }
                }
            }
        } else {
            for (DictData dict : dictDataList) {
                if (dictLabel.equals(dict.getDictLabel())) {
                    return dict.getDictValue();
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 清空字典缓存
     */
    public static void clearDictCache() {
        CacheUtils.removeAll(getCacheName());
    }

    /**
     * 获取cache name
     *
     * @return 缓存名
     */
    public static String getCacheName() {
        return Constants.SYS_DICT_CACHE;
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    public static String getCacheKey(String configKey) {
        return Constants.SYS_DICT_KEY + configKey;
    }
}
