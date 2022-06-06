package org.nodes.wms.dao.basics.billType.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.nodes.wms.dao.basics.billType.BillTypeDao;
import org.nodes.wms.dao.basics.billType.dto.BillTypeSelectResponse;
import org.nodes.wms.dao.basics.billType.entities.BillType;
import org.nodes.wms.dao.basics.billType.mapper.BillTypeMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 单据类型 DAO实现类
 */
@Repository
public class BillTypeDaoImpl
	extends BaseServiceImpl<BillTypeMapper, BillType>
	implements BillTypeDao {

	@Override
	public List<BillTypeSelectResponse>
    listByIoType(String ioType) {
		return super.baseMapper.listByIoType(ioType);
	}

    @Override
    public BillType getBillTypeById(Long billTypeId) {
	   return super.getById(billTypeId);
    }

	@Override
	public BillType getBillTypeByCode(String billTypeCd) {
		return super.getOne(new LambdaQueryWrapper<BillType>().eq(BillType::getBillTypeCd,billTypeCd));
	}
}
