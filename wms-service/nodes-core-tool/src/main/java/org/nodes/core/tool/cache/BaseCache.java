package org.nodes.core.tool.cache;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * author: pengwei
 * date: 2021/5/25 13:21
 * description: BaseCache 缓存基类
 */
public class BaseCache<T> {

	IService<T> service;

	final Map<Serializable, T> cache = new HashMap<>();

	static BaseCache instance;

	public static BaseCache getInstance(){
		if (instance == null) {
			instance = new BaseCache();
			instance.service = SpringUtil.getBean(IService.class);
		}
		return instance;
	}

	public T getById(Serializable id) {
		T obj = cache.getOrDefault(id, null);
		if (Func.isEmpty(obj)) {
			obj = service.getById(id);
		}
		return obj;
	}

	public void saveOrUpdate(T entity) {
		Serializable id = this.getIdVal(entity);
		if (cache.containsKey(id)) {
			cache.replace(id, entity);
		} else {
			cache.put(id, entity);
		}
	}

	public void removeById(Serializable id) {
		cache.remove(id);
	}

	/**
	 * 移除 包装明细列表中指定的数据
	 *
	 * @param idList 包装明细主键ID集合
	 */
	public void removeByIds(Collection<? extends Serializable> idList) {
		if (Func.isEmpty(idList)) {
			return;
		}
		for (Serializable id : idList) {
			removeById(id);
		}
	}

	public Field getIdField(T entity) {
		List<Field> fieldList = new ArrayList();

		for(Class clazz = entity.getClass(); Func.isNotEmpty(clazz); clazz = clazz.getSuperclass()) {
			fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
		}

		Iterator var4 = fieldList.iterator();

		Field field;
		do {
			if (!var4.hasNext()) {
				return null;
			}

			field = (Field)var4.next();
		} while(((TableId[])field.getAnnotationsByType(TableId.class)).length <= 0);

		field.setAccessible(true);
		return field;
	}

	public Long getIdVal(T entity) {
		try {
			Field field = this.getIdField(entity);
			return field == null ? null : (Long)field.get(entity);
		} catch (IllegalAccessException var3) {
			return null;
		}
	}
}
