package org.nodes.wms.dao.instock.receive.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.wms.dao.instock.receive.ReceiveDetailLpnDao;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetailLpn;
import org.nodes.wms.dao.instock.receive.enums.ReceiveDetailStatusEnum;
import org.nodes.wms.dao.instock.receive.mapper.ReceiveDetailLpnMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * lpn表Dao实现类
 */
@Repository
public class ReceiveDetailLpnDaoImpl extends BaseServiceImpl<ReceiveDetailLpnMapper, ReceiveDetailLpn> implements ReceiveDetailLpnDao {

	@Override
	public List<ReceiveDetailLpn> getReceiveDetailLpnListByBoxCode(String boxCode) {
		return super.list(new LambdaQueryWrapper<ReceiveDetailLpn>().eq(ReceiveDetailLpn::getBoxCode, boxCode)
			.le(ReceiveDetailLpn::getScanQty,0));
	}

    @Override
    public ReceiveDetailLpn selectReceiveDetailLpnById(Long receiveDetailLpnId) {
        return super.getById(receiveDetailLpnId);
    }

    @Override
    public void updateReceiveDetailLpn(ReceiveDetailLpn lpn) {
        super.updateById(lpn);
    }

    @Override
    public ReceiveDetailLpn selectByReceiveDetailId(Long receiveDetailId) {
		LambdaQueryWrapper<ReceiveDetailLpn> queryWrapper = Wrappers.lambdaQuery(ReceiveDetailLpn.class);
		queryWrapper.eq(ReceiveDetailLpn::getReceiveDetailId, receiveDetailId);
        return super.getOne(queryWrapper);
    }

	@Override
	public boolean updateForCancelReceive(ReceiveDetailLpn receiveDetailLpn) {
		LambdaUpdateWrapper<ReceiveDetailLpn> updateWrapper = Wrappers.lambdaUpdate(ReceiveDetailLpn.class);
		updateWrapper
			.eq(ReceiveDetailLpn::getReceiveDetailId, receiveDetailLpn.getReceiveDetailId())
			.set(ReceiveDetailLpn::getDetailStatus,ReceiveDetailStatusEnum.NOT_RECEIPT)
			.set(ReceiveDetailLpn::getScanQty, BigDecimal.ZERO)
			.set(ReceiveDetailLpn::getReceiveHeaderId,"0")
			.set(ReceiveDetailLpn::getReceiveDetailId,"0");
		return super.update(updateWrapper);
	}
}
