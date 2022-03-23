
package org.nodes.wms.core.outstock.so.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.nodes.wms.core.outstock.so.dto.SoHeaderDTO;
import org.nodes.wms.core.outstock.so.dto.SoHeaderQueryDTO;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.core.outstock.so.enums.SoBillStateEnum;
import org.nodes.wms.core.outstock.so.vo.SoHeaderVO;
import org.springblade.core.mp.support.Query;

import java.util.List;

/**
 * 出库单头表 服务类
 *
 * @author zhonglianshuai
 * @since 2020-02-10
 */
public interface ISoHeaderService extends IService<SoHeader> {

	/**
	 * 获取出库单-列表
	 *
	 * @param soHeaderQuery 查询条件
	 * @return 出库单-列表
	 */
	List<SoHeaderVO> selectList(SoHeaderQueryDTO soHeaderQuery);

	/**
	 * 获取出库单-分页
	 *
	 * @param soHeaderQuery 查询条件
	 * @param query         分页信息
	 * @return 出库单-分页
	 */
	IPage<SoHeaderVO> selectPage(SoHeaderQueryDTO soHeaderQuery, Query query);

	/**
	 * 获取出库单详情
	 *
	 * @param soBillId 出库单ID
	 * @return 出库单详细信息
	 */
	SoHeaderVO getDetail(Long soBillId);

	/**
	 * 保存出库单头表及其明细表
	 *
	 * @param soHeader
	 * @return
	 */
	boolean save(SoHeaderDTO soHeader);

	/**
	 * 新增与更新
	 *
	 * @param soHeader
	 * @return
	 */
	boolean saveOrUpdate(SoHeaderDTO soHeader);
	/**
	 * 新增与更新
	 *
	 * @param soHeader
	 * @return
	 */
	boolean saveOrUpdateByAllot(SoHeaderDTO soHeader);

	/**
	 * 更新出库单头表及其明细表
	 *
	 * @param soHeader
	 * @return
	 */
	boolean updateById(SoHeaderDTO soHeader);

	/**
	 * 修改订单状态（包括订单状态验证）
	 *
	 * @param billId      订单ID
	 * @param soBillState 订单状态
	 * @param isCompel    是否强制更新
	 */
	SoHeader updateBillState(Long billId, SoBillStateEnum soBillState, boolean isCompel);

	/**
	 * 修改订单状态（包括订单状态验证）
	 *
	 * @param soHeader    订单表头
	 * @param soBillState 订单状态
	 * @param isCompel    是否强制更新
	 */
	SoHeader updateBillState(SoHeader soHeader, SoBillStateEnum soBillState, boolean isCompel);

	/**
	 * 获取出库单是否允许编辑
	 *
	 * @param soHeaderId 出库单ID
	 * @return true:允许编辑， false:不允许编辑
	 */
	boolean canEdit(Long soHeaderId);

	/**
	 * 订单取消
	 *
	 * @param idList 出库单ID
	 * @return 是否成功
	 */
	boolean cancel(List<Long> idList);

	/**
	 * 完成订单
	 *
	 * @param idList 主键集合
	 * @return 是否成功
	 */
	boolean completed(List<Long> idList);

	/**
	 * 完成出库，直接扣除库存
	 *
	 * @param idList 订单ID集合
	 * @return 是否成功
	 */
	boolean completedOutstock(List<Long> idList);

//	boolean removeByIds(List<Long> idList);
}
