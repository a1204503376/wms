
package org.nodes.wms.core.outstock.so.service;

import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.outstock.so.entity.Wellen;
import org.nodes.wms.core.outstock.so.entity.WellenDetail;
import org.nodes.wms.core.outstock.so.vo.WellenDetailVO;
import org.nodes.wms.core.outstock.so.vo.WellenSoHeaderVo;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 波次划分明细表 服务类
 *
 * @author pengwei
 * @since 2020-02-10
 */
public interface IWellenDetailService extends BaseService<WellenDetail> {

	/**
	 * 根据订单明细信息创建波次明细
	 * @param wellen 波次信息
	 * @param billDetail 订单明细信息
	 * @return 波次划分明细
	 */
	WellenDetailVO create(Wellen wellen, SoDetail billDetail);

	/**
	 * 获取波次明细集合
	 * @param billId 订单ID
	 * @return 波次明细集合
	 */
	List<WellenDetail> listByBillId(Long billId);
	/**
	 * 获取波次明细集合
	 * @param wellenId 波次ID
	 * @return 波次明细集合
	 */
	List<WellenDetail> listByWellenId(Long wellenId);

	List<WellenSoHeaderVo> getSoHeaderByWellenId(List<Long> wellenIds);
}
