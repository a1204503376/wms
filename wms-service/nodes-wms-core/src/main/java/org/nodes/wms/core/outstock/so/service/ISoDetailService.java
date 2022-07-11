
package org.nodes.wms.core.outstock.so.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.nodes.wms.core.outstock.so.dto.SoDetailDTO;
import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.dao.outstock.so.enums.SoDetailStateEnum;

import java.math.BigDecimal;

/**
 *  服务类
 *
 * @author NodeX
 * @since 2020-02-10
 */
public interface ISoDetailService extends IService<SoDetail> {
	boolean updateSoDetailQty(SoDetail soDetail, BigDecimal count);
	/**
	 * 修改订单明细状态
	 *
	 * @param soDetailId    订单明细ID
	 * @param soDetailState 明细状态
	 * @return 是否成功
	 */
	boolean updateState(Long soDetailId, SoDetailStateEnum soDetailState);
	/**
	 * 出库单明细保存
	 * @param soDetailDTO
	 * @return
	 */
	boolean save(SoDetailDTO soDetailDTO);
	/**
	 * 出库单明细保存
	 * @param soDetailDTO
	 * @return
	 */
	boolean saveByAllot(SoDetailDTO soDetailDTO);
}
