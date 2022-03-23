
package org.nodes.wms.core.outstock.so.service.impl;

import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.outstock.so.entity.Wellen;
import org.nodes.wms.core.outstock.so.entity.WellenDetail;
import org.nodes.wms.core.outstock.so.mapper.WellenDetailMapper;
import org.nodes.wms.core.outstock.so.service.IWellenDetailService;
import org.nodes.wms.core.outstock.so.vo.WellenDetailVO;
import org.nodes.wms.core.outstock.so.vo.WellenSoHeaderVo;
import org.nodes.wms.core.outstock.so.vo.WellenVO;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 波次划分明细表 服务实现类
 *
 * @author pengwei
 * @since 2020-02-10
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class WellenDetailServiceImpl<M extends WellenDetailMapper, T extends WellenDetail>
	extends BaseServiceImpl<WellenDetailMapper, WellenDetail>
	implements IWellenDetailService {


	/**
	 * 创建波次明细
	 * @param wellen 波次信息
	 * @param billDetail 订单明细信息
	 * @return 波次明细
	 */
	@Override
	public WellenDetailVO create(Wellen wellen, SoDetail billDetail) {

		WellenDetailVO detail = new WellenDetailVO();
		// 基础字段
		detail.setWellenId(wellen.getWellenId());
		detail.setSoBillId(billDetail.getSoBillId());
		detail.setSoBillNo(billDetail.getSoBillNo());
		detail.setSoDetailId(billDetail.getSoDetailId());
		detail.setBillTypeCd(billDetail.getBillTypeCd());
		detail.setCreateUser(wellen.getCreateUser());
		detail.setUpdateUser(wellen.getUpdateUser());
		// 扩展字段
		detail.setSoDetail(billDetail);
		detail.setWellenNo(wellen.getWellenNo());
		// 存储波次明细
		super.save(detail);

		return detail;
	}

	/**
	 * 获取波次明细集合
	 * @param billId 订单ID
	 * @return 波次明细集合
	 */
	@Override
	public List<WellenDetail> listByBillId(Long billId) {
		WellenDetail wellenDetailQuery = new WellenDetail();
		wellenDetailQuery.setSoBillId(billId);

		return super.list(Condition.getQueryWrapper(wellenDetailQuery));
	}
	/**
	 * 获取波次明细集合
	 * @param wellenId 波次ID
	 * @return 波次明细集合
	 */
	@Transactional()
	@Override
	public List<WellenDetail> listByWellenId(Long wellenId) {
		WellenDetail wellenDetailQuery = new WellenDetail();
		wellenDetailQuery.setWellenId(wellenId);

		return super.list(Condition.getQueryWrapper(wellenDetailQuery));
	}

	@Override
	public List<WellenSoHeaderVo> getSoHeaderByWellenId(List<Long> wellenIds) {
		return baseMapper.getSoHeaderByWellenIds(wellenIds);
	}


}
