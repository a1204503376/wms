
package org.nodes.wms.core.allot.service;

import org.nodes.wms.core.allot.dto.AllotHeaderDTO;
import org.nodes.wms.core.allot.entity.AllotHeader;
import org.nodes.wms.core.allot.enums.AllotBillStateEnum;
import org.nodes.wms.core.allot.vo.AllotHeaderVO;
import org.springblade.core.mp.base.BaseService;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 调拨单头表 服务类
 *
 * @author Wangjw
 * @since 2020-01-23
 */
public interface IAllotHeaderService extends BaseService<AllotHeader> {

	/**
	 * 获取调拨单-详情
	 *
	 * @param allotBillId 调拨单ID
	 * @return 调拨单-详情
	 */
	AllotHeaderVO getDetail(Long allotBillId);

	/**
	 * 新增
	 *
	 * @param allotHeaderDTO AllotHeaderDTO 对象
	 * @return 是否成功
	 */
	boolean save(@Validated AllotHeaderDTO allotHeaderDTO);

	/**
	 * 修改
	 *
	 * @param allotHeaderDTO AllotHeaderDTO 对象
	 * @return 是否成功
	 */
	boolean updateById(@Validated AllotHeaderDTO allotHeaderDTO);

	/**
	 * 新增或修改调拨单
	 *
	 * @param allotHeaderDTO 调拨单
	 * @return 是否成功
	 */
	boolean saveOrUpdate(@Validated AllotHeaderDTO allotHeaderDTO);

	/**
	 * 调拨单-更新表头状态
	 *
	 * @param allotBillId        调拨单表头ID
	 * @param allotBillStateEnum 调拨单状态
	 * @return 是否成功
	 */
	boolean updateBillState(Long allotBillId, AllotBillStateEnum allotBillStateEnum);

	/**
	 * 调拨单-更新表头状态
	 *
	 * @param allotBillNo        调拨单表头编码
	 * @param allotBillStateEnum 调拨单状态
	 * @return 是否成功
	 */
	boolean updateBillState(String allotBillNo, AllotBillStateEnum allotBillStateEnum);

	/**
	 * 调拨单-更新表头状态
	 *
	 * @param allotHeader        调拨单表头
	 * @param allotBillStateEnum 调拨单状态
	 * @return 是否成功
	 */
	boolean updateBillState(AllotHeader allotHeader, AllotBillStateEnum allotBillStateEnum);

	/**
	 * 调拨单-取消
	 *
	 * @param allotBillIdList 调拨单表头ID集合
	 * @return 是否成功
	 */
	boolean cancel(List<Long> allotBillIdList);

	/**
	 * 判断调拨单是否允许编辑
	 *
	 * @param allotBillId 调拨单ID
	 * @return
	 */
	boolean canEdit(Long allotBillId);
}
