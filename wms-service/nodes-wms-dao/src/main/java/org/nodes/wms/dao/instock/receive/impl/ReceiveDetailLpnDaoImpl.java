package org.nodes.wms.dao.instock.receive.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.nodes.wms.dao.instock.receive.ReceiveDetailLpnDao;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetailLpn;
import org.nodes.wms.dao.instock.receive.mapper.ReceiveDetailLpnMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * lpn表Dao实现类
 */
@Repository
public class ReceiveDetailLpnDaoImpl extends BaseServiceImpl<ReceiveDetailLpnMapper, ReceiveDetailLpn> implements ReceiveDetailLpnDao {

	@Override
	public List<ReceiveDetailLpn> getReceiveDetailLpnListByBoxCode(String boxCode) {
		return super.list(new LambdaQueryWrapper<ReceiveDetailLpn>().eq(ReceiveDetailLpn::getBoxCode, boxCode));
	}

    @Override
    public ReceiveDetailLpn selectReceiveDetailLpnById(Long receiveDetailLpnId) {
        return super.getById(receiveDetailLpnId);
    }

    @Override
    public void updateReceiveDetailLpn(ReceiveDetailLpn lpn) {
        super.updateById(lpn);
    }
}
