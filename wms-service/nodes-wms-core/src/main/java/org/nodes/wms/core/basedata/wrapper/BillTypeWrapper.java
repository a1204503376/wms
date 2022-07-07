package org.nodes.wms.core.basedata.wrapper;

import org.nodes.wms.dao.basics.billType.entities.BillType;
import org.nodes.wms.core.basedata.vo.BillTypeVo;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * @author pengwei
 * @program WmsCore
 * @description 货主封装类
 * @create 20200220
 */
public class BillTypeWrapper extends BaseEntityWrapper<BillType, BillTypeVo> {

	public static BillTypeWrapper build() {
		return new BillTypeWrapper();
	}

	@Override
	public BillTypeVo entityVO(BillType entity) {
		BillTypeVo billTypeVo = BeanUtil.copy(entity, BillTypeVo.class);
		billTypeVo.setIoTypeDesc("I".equals(entity.getIoType())?"入库":"出库");
		return billTypeVo;
	}

}
