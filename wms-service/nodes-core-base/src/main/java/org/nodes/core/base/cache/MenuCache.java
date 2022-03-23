package org.nodes.core.base.cache;

import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.nodes.core.base.entity.Menu;
import org.nodes.core.base.service.IMenuService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springblade.core.cache.constant.CacheConstant.MENU_CACHE;

/**
 * @author pengwei
 * @program WmsCore
 * @description 系统菜单缓存操作类
 * @since 2020-08-27
 */
public class MenuCache {

	static final String MENU_ID = "menu:id:";

	static final String MENU_LIST = "menu:list:";

	static IMenuService menuService;

	static {
		menuService = SpringUtil.getBean(IMenuService.class);
	}

	/**
	 * 获取菜单实体
	 *
	 * @param id 主键
	 * @return Param
	 */
	public static Menu getById(Long id) {
		return CacheUtil.get(MENU_CACHE, MENU_ID, id, () -> menuService.getById(id));
	}

	public static List<Menu> list() {
		return CacheUtil.get(MENU_CACHE, MENU_LIST, "all", () -> menuService.list());
	}
}
