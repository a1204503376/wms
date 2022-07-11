package org.nodes.wms.dao.outstock.so;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.outstock.so.dto.input.SoHeaderPageQuery;
import org.nodes.wms.dao.outstock.so.dto.output.SoHeaderEditResponse;
import org.nodes.wms.dao.outstock.so.dto.output.SoHeaderPageResponse;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;

/**
 * 发货单Dao接口
 **/
public interface SoHeaderDao {

	/**
	 * 分页
	 *
	 * @param page: 分页参数
	 * @param soHeaderPageQuery: 分页查询请求对象
	 * @return Page<SoHeaderPageResponse>
	 */
	Page<SoHeaderPageResponse> page(IPage<?> page, SoHeaderPageQuery soHeaderPageQuery);

	/**
	 * 新增或修改出库单头表信息
	 *
	 * @param soHeader: 出库单头表对象
	 * @return true: 新增或修改失败 false: 新增或修改失败
	 */
    boolean saveOrUpdateSoHeader(SoHeader soHeader);

	/**
	 * 获取编辑时出库单头表信息
	 *
	 * @param soBillId: 出库单id
	 * @return SoHeaderEditResponse 出库单编辑头表响应对象
	 */
	SoHeaderEditResponse getSoHeaderEditBySoBillId(Long soBillId);

}
