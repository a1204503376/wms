package org.nodes.wms.biz.basics.lpntype;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.dao.basics.lpntype.dto.input.*;
import org.nodes.wms.dao.basics.lpntype.dto.output.LpnTypeByIdResponse;
import org.nodes.wms.dao.basics.lpntype.dto.output.LpnTypePageResponse;
import org.nodes.wms.dao.basics.lpntype.dto.output.LpnTypeSelectResponse;
import org.nodes.wms.dao.basics.lpntype.entities.LpnType;
import org.nodes.wms.dao.basics.lpntype.enums.LpnTypeCodeEnum;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * 容器对象Biz
 */
public interface LpnTypeBiz {

	/**
	 * 分页查询
	 *
	 * @param query            分页参数
	 * @param lpnTypePageQuery 条件
	 * @return 容器集合对象
	 */
	IPage<LpnTypePageResponse> getPage(Query query, LpnTypePageQuery lpnTypePageQuery);

	/**
	 * 保存
	 *
	 * @param lpnTypeRequest 新增容器的参数
	 * @return 是/否
	 */
	boolean newLpnType(NewLpnTypeRequest lpnTypeRequest);

	/**
	 * 逻辑删除
	 *
	 * @param deleteRequest 删除容器对象id的集合
	 * @return 是否成功
	 */
	boolean remove(DeleteLpnTypeRequest deleteRequest);

	/**
	 * 导出Excel
	 *
	 * @param params   查询条件
	 * @param response 响应对象
	 */
	void exportExcel(HashMap<String, Object> params, HttpServletResponse response);

	/**
	 * 容器ById查询
	 *
	 * @param lpnTypeByIdRequest 容器ById请求对象
	 * @return 容器ById查询返回对象
	 */
	LpnTypeByIdResponse getLpnTypeById(LpnTypeByIdRequest lpnTypeByIdRequest);

	/**
	 * 根据id修改容器
	 *
	 * @param lpnTypeRequest 修改请求对象
	 * @return 是否成功
	 */
	boolean updateLpnTypeById(UpdateLpnTypeRequest lpnTypeRequest);

	/**
	 * 根据容器类型id获取容器类型实体
	 *
	 * @param id 容器类型id
	 * @return LpnType 容器类型实体
	 */
	LpnType findLpnTypeById(Long id);

	/**
	 * 根据容器类型编码获取容器类型实体
	 *
	 * @param code 容器类型编码
	 * @return LpnType 容器类型实体
	 */
	LpnType findLpnTypeByCode(String code);

	/**
	 * 获取容器类型下拉组件数据
	 *
	 * @return List<LpnTypeSelectResponse>
	 */
	List<LpnTypeSelectResponse> findLpnTypeSelectList();

	/**
	 * 解析箱型, 天宜定制功能
	 *
	 * @param boxCode 箱码
	 * @return
	 */
	LpnTypeCodeEnum parseBoxCode(String boxCode);

	/**
	 * 天宜定制：根据箱型获取容器列别
	 * @param boxCode
	 * @return
	 */
    LpnType findLpnTypeByBoxCode(String boxCode);
}
