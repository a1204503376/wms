package org.nodes.wms.dao.basics.lpntype;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
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
	 * @return
	 */
	boolean insert(LpnType lpnType);

	/**
	 * 判断当前容器对象是否存在
	 * @param code
	 * @return
	 */
	boolean isExistCarrierCode(String code);

	/**
	 * 批量或者单个容器对象
	 * @param deleteRequest
	 * @return
	 */
	boolean delete(DeleteLpnTypeRequest deleteRequest);

	/**
	 * 获取容器 对象
	 * @param params 导出条件
	 * @return 容器集合
	 */
	List<LpnTypeExcelResponse> getLpnTypes(HashMap<String, Object> params);

}
