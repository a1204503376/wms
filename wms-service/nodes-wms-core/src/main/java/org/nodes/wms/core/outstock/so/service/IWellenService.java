
package org.nodes.wms.core.outstock.so.service;

import org.nodes.wms.core.stock.core.entity.StockOccupy;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.core.outstock.so.entity.Wellen;
import org.nodes.wms.core.outstock.so.enums.WellenStateEnum;
import org.nodes.wms.core.outstock.so.vo.WellenVO;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 波次划分表 服务类
 *
 * @author pengwei
 * @since 2020-02-10
 */
public interface IWellenService extends BaseService<Wellen> {

	/**
	 * 根据订单ID创建波次
	 *
	 * @param soHeaderList 订单主表信息集合
	 * @return 创建的波次信息
	 */
	List<WellenVO> create(List<SoHeader> soHeaderList, List<Long> soDetailList);

	/**
	 * 根据订单ID创建波次
	 * @param soHeader 订单主表信息
	 * @return 创建的波次信息
	 */
	WellenVO create(SoHeader soHeader, List<Long> soDetailList);

	/**
	 * 修改波次状态
	 * @param wellenId 波次ID
	 * @param wellenState 波次状态
	 */
	boolean updateState(Long wellenId, WellenStateEnum wellenState);

	/**
	 * 回滚波次信息
	 *
	 * @param billId 订单ID
	 * @return 占用的库存信息集合
	 */
	List<StockOccupy> rollback(Long billId);

	/**
	 * 获取波次详情
	 * @param wellen 封装波次
	 * @return
	 */
	WellenVO detail(Wellen wellen);
}
