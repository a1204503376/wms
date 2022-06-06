package org.nodes.wms.dao.basics.lpntype;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.dao.basics.lpntype.dto.input.DeleteLpnTypeRequest;
import org.nodes.wms.dao.basics.lpntype.dto.input.LpnTypePageQuery;
import org.nodes.wms.dao.basics.lpntype.dto.output.LpnTypeExcelResponse;
import org.nodes.wms.dao.basics.lpntype.dto.output.LpnTypePageResponse;
import org.nodes.wms.dao.basics.lpntype.entities.LpnType;

import java.util.HashMap;
import java.util.List;

/**
 * 容器管理 DAO 接口
 */
public interface LpnTypeDao {
	/**
	 * 容器分页查询
	 *
	 * @param page            分页对象
	 * @param lpnTypePageQuery 分页请求参数
	 * @return IPage<PageResponse>
	 */
	IPage<LpnTypePageResponse> selectPage(IPage<?> page, LpnTypePageQuery lpnTypePageQuery);

	/**
	 * 新增容器
	 * @param lpnType 容器实体
	 * @return 是否成功
	 */
	boolean insert(LpnType lpnType);

	/**
	 * 判断当前容器对象是否存在
	 * @param code 容器类型编码
	 * @return 是否成功
	 */
	boolean isExistCarrierCode(String code);

	/**
	 * 批量或者单个容器对象
	 * @param deleteRequest 删除容器请求对象
	 * @return 是否成功
	 */
	boolean delete(DeleteLpnTypeRequest deleteRequest);

	/**
	 * 获取容器 对象
	 * @param params 导出条件
	 * @return 容器集合
	 */
	List<LpnTypeExcelResponse> getLpnTypes(HashMap<String, Object> params);

	/**
	 * 根据id查询对应容器
	 * @param id 容器id
	 * @return 容器对象
	 */
	LpnType getById(Long id);

	/**
	 * 根据id修改对应容器
	 * @param lpnType 要修改的容器对象
	 * @return 是否成功
	 */
	boolean updateById(LpnType lpnType);

	/**
	 * 根据容器类型id获取容器类型实体
	 * @param id 容器类型id
	 * @return LpnType 容器类型实体
	 */
    LpnType getLpnTypeById(Long id);

	/**
	 * 根据容器类型编码获取容器类型实体
	 * @param code 容器类型编码
	 * @return  LpnType 容器类型实体
	 */
	LpnType getLpnTypeByCode(String code);
}
