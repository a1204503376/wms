package org.nodes.wms.core.instock.asn.service.impl;

import org.nodes.wms.core.instock.asn.entity.AsnDetail;
import org.nodes.wms.core.instock.asn.entity.Sn;
import org.nodes.wms.core.instock.asn.mapper.AsnDetailMapper;
import org.nodes.wms.core.instock.asn.mapper.SnMapper;
import org.nodes.wms.core.instock.asn.service.ISnService;
import org.nodes.wms.core.instock.asn.vo.SnVO;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 入库货品序列号 服务实现类
 *
 * @author NodeX
 * @since 2020-01-15
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SnServiceImpl<M extends SnMapper, T extends Sn>
	extends BaseServiceImpl<SnMapper, Sn>
	implements ISnService {

	@Autowired
	AsnDetailMapper asnDetailMapper;

	@Override
	public List<Sn> selectSnListBySku(SnVO snVO) {
		AsnDetail asnDetail = new AsnDetail();
		//单据id
		asnDetail.setAsnBillId(snVO.getAsnBillId());
		//物品编码
		asnDetail.setSkuCode(snVO.getSkuCode());
		List<AsnDetail> list = asnDetailMapper.selectList(Condition.getQueryWrapper(asnDetail));
		String asnDetailIds = "";
		for (AsnDetail detail : list) {
			asnDetailIds += detail.getAsnDetailId()+",";
		}
		asnDetailIds = asnDetailIds.substring(0,asnDetailIds.length()-1);
		Sn sn = new Sn();
		sn.setAsnBillId(snVO.getAsnBillId());
		sn.setSnStatus(snVO.getSnStatus());
		return super.list(Condition.getQueryWrapper(sn).lambda()
				.apply("asn_detail_id in ("+ asnDetailIds+")"));

	}
}
