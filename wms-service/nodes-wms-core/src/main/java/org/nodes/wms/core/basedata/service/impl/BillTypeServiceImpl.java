package org.nodes.wms.core.basedata.service.impl;

import lombok.AllArgsConstructor;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.cache.BillTypeCache;
import org.nodes.wms.dao.basics.billType.entities.BillType;
import org.nodes.wms.core.basedata.mapper.BillTypeMapper;
import org.nodes.wms.core.basedata.service.IBillTypeService;
import org.nodes.wms.core.instock.asn.entity.AsnHeader;
import org.nodes.wms.core.instock.asn.service.IAsnHeaderService;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.core.outstock.so.service.ISoHeaderService;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 单据类型 服务实现类
 *
 * @author NodeX
 * @since 2019-12-24
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class BillTypeServiceImpl<M extends BillTypeMapper, T extends BillType>
	extends BaseServiceImpl<BillTypeMapper, BillType>
	implements IBillTypeService {

	@Autowired
	IAsnHeaderService asnHeaderService;
	@Autowired
	ISoHeaderService soHeaderService;

	@Override
	public boolean removeByIds(List<Long> ids) {
		List<BillType> billTypes = super.listByIds(ids);
		List<String> billTypeCds = NodesUtil.toList(billTypes, BillType::getBillTypeCd);
		long countAsn = asnHeaderService.list(Condition.getQueryWrapper(new AsnHeader())
			.lambda()
			.in(AsnHeader::getBillTypeCd, billTypeCds)
		).stream().count();
		if (countAsn > 0L) {
			throw new ServiceException("收货单中存在此类型单据，不允许删除");
		}
		long countSo = soHeaderService.list(Condition.getQueryWrapper(new SoHeader())
			.lambda()
			.in(SoHeader::getBillTypeCd, billTypeCds)
		).stream().count();
		if (countSo > 0L) {
			throw new ServiceException("发货单中存在此类型单据，不允许删除");
		}
		super.removeByIds(ids);
		return true;
	}

	@Override
	public boolean saveOrUpdate(BillType billType) {
		if (Func.isNotEmpty(billType.getIoType()) && Func.isNotEmpty(billType.getBillTypeCd())) {
			/*long cnt = super.count(Condition.getQueryWrapper(new BillType())
			.lambda()
			.eq(BillType::getBillTypeCd,billType.getBillTypeCd())
			.eq(BillType::getIoType,billType.getIoType())
			);
			if (cnt > 0L){
				throw new ServiceException("相同单据类型种类的编码为唯一值，请重新修改单据类型编码");
			}*/
			long count = super.count(Condition.getQueryWrapper(new BillType())
				.lambda()
				.ne(BillType::getBillTypeId, billType.getBillTypeId())
				.eq(BillType::getBillTypeCd, billType.getBillTypeCd())
			);
			if (count > 1) {
				throw new ServiceException("该编码已被占用，不允许重复");
			}
		}
		super.saveOrUpdate(billType);
		return true;
	}

}
