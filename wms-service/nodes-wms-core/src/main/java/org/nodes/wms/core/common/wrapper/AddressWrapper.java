package org.nodes.wms.core.common.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.wms.core.common.entity.Address;
import org.nodes.wms.core.common.vo.AddressVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;

import java.util.Objects;

/**
 * @author pengwei
 * @program WmsCore
 * @description 地址信息封装类
 * @create 20191206
 */
public class AddressWrapper extends BaseEntityWrapper<Address, AddressVO> {

	public static AddressWrapper build() {
		return new AddressWrapper();
	}

	@Override
	public AddressVO entityVO(Address entity) {
		AddressVO addressVO = Objects.requireNonNull(BeanUtil.copy(entity, AddressVO.class));
		if (Func.isNotEmpty(addressVO)) {
			addressVO.setAddressTypeDesc(DictCache.getValue(DictCodeConstant.ADDRESS_TYPE, addressVO.getAddressType()));
			if(Func.isNotEmpty(addressVO.getDefaultFlag())){
				addressVO.setDefaultFlagBoolean(addressVO.getDefaultFlag() == 0 ? false : true);
				addressVO.setDefaultFlagBooleanDesc(addressVO.getDefaultFlag() == 0 ? "否" : "是");
			}else{
				addressVO.setDefaultFlagBoolean(false);
				addressVO.setDefaultFlagBooleanDesc("否");
			}
		}
		return addressVO;
	}
}
