package org.nodes.wms.core.basedata.wrapper;

import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.dto.OwnerDTO;
import org.nodes.wms.core.basedata.entity.BillType;
import org.nodes.wms.core.basedata.entity.Owner;
import org.nodes.wms.core.basedata.excel.OwnerExcel;
import org.nodes.wms.core.basedata.vo.BillTypeVo;
import org.nodes.wms.core.basedata.vo.OwnerVO;
import org.nodes.wms.core.common.dto.AddressDTO;
import org.nodes.wms.core.common.dto.ContactsDTO;
import org.nodes.wms.core.common.entity.Address;
import org.nodes.wms.core.common.entity.Contacts;
import org.nodes.wms.core.common.service.IAddressService;
import org.nodes.wms.core.common.service.IContactsService;
import org.nodes.wms.core.common.wrapper.AddressWrapper;
import org.nodes.wms.core.common.wrapper.ContactsWrapper;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.List;

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
