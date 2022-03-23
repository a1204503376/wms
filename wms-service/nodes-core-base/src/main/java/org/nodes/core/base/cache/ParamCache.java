package org.nodes.core.base.cache;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.models.auth.In;
import org.nodes.core.base.entity.Param;
import org.nodes.core.base.enums.ParamEnum;
import org.nodes.core.base.service.IParamService;
import org.nodes.core.utils.TokenUtil;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springblade.core.cache.constant.CacheConstant.MENU_CACHE;
import static org.springblade.core.cache.constant.CacheConstant.PARAM_CACHE;

/**
 * 系统参数缓存
 *
 * @Author zx
 * @Date 2020/6/10
 **/
public class ParamCache {

	static final String PARAM_ID = "param:id:";

	static final String PARAM_KEY = "param:key:";

	static IParamService paramService;

	/**
	 * 批属性开放数量
	 */
	public static Integer LOT_COUNT = 30;

	public static Integer IS_OVER_VALUE = 0;

	static {
		paramService = SpringUtil.getBean(IParamService.class);
		reload();
	}

	public static void reload() {
		CacheUtil.clear(PARAM_CACHE);
		LOT_COUNT = Func.toInt(getValue(ParamEnum.SKU_LOT_COUNT.getKey()));
		IS_OVER_VALUE = Func.toInt(getValue(ParamEnum.ASN_IS_OVER.getKey()));
	}

	/**
	 * 获取参数实体
	 *
	 * @param id 主键
	 * @return
	 */
	public static Param getById(Long id) {
		return CacheUtil.get(PARAM_CACHE, PARAM_ID, id, () -> paramService.getById(id));
	}

	/**
	 * 根据参数key获取参数实体
	 *
	 * @param paramKey
	 * @return
	 */
	public static Param getOne(String paramKey) {
		return CacheUtil.get(PARAM_CACHE, PARAM_KEY, paramKey, () -> {
			return paramService.list(Condition.getQueryWrapper(new Param())
				.lambda()
				.eq(Param::getParamKey, paramKey)
			).stream().findFirst().orElse(null);
		});
	}

	/**
	 * 获取参数的值
	 *
	 * @param paramKey
	 * @return
	 */
	public static String getValue(String paramKey) {
		Param param = getOne(paramKey);
		return Func.isNotEmpty(param) ? param.getParamValue() : StringPool.EMPTY;
	}
}
