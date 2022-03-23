package org.nodes.wms.core.warehouse.wrapper;

import com.sun.xml.bind.v2.model.core.ID;
import io.github.classgraph.json.Id;
import org.nodes.core.base.cache.SysCache;
import org.nodes.core.base.entity.Dept;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDeptService;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.core.basedata.entity.Enterprise;
import org.nodes.wms.core.common.cache.AddressCache;
import org.nodes.wms.core.common.cache.ContactsCache;
import org.nodes.wms.core.common.dto.AddressDTO;
import org.nodes.wms.core.common.dto.ContactsDTO;
import org.nodes.wms.core.common.entity.Address;
import org.nodes.wms.core.common.entity.Contacts;
import org.nodes.wms.core.common.service.IAddressService;
import org.nodes.wms.core.common.service.IContactsService;
import org.nodes.wms.core.common.wrapper.AddressWrapper;
import org.nodes.wms.core.common.wrapper.ContactsWrapper;
import org.nodes.wms.core.stock.core.vo.StockSkuMoveVO;
import org.nodes.wms.core.warehouse.dto.WarehouseDTO;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.excel.WarehouseExcel;
import org.nodes.wms.core.warehouse.vo.WarehouseVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.List;

/**
 * @author wangjw
 * @program 仓库封装方法
 * @description 仓库分类实体封装VO
 * @create 20191213
 */
public class WarehouseWrapper  extends BaseEntityWrapper<Warehouse, WarehouseVO> {
	/**
	 * 仓库封装
	 */
	public static WarehouseWrapper build() {

		return new WarehouseWrapper();
	}

	@Override
	public WarehouseVO entityVO(Warehouse warehouse) {
		WarehouseVO warehouseVO = BeanUtil.copy(warehouse, WarehouseVO.class);
		IAddressService addressService = SpringUtil.getBean(IAddressService.class);
		IContactsService contactsService = SpringUtil.getBean(IContactsService.class);
		if(Func.isNotEmpty(warehouseVO)){
			if (Func.isNotEmpty(warehouse.getStatus()) && warehouse.getStatus().equals(2)) {
				warehouseVO.setStatusName(StringPool.CHS_YES);
			} else {
				warehouseVO.setStatusName(StringPool.CHS_NO);
			}

			List<Address> addressList = addressService.list(Condition.getQueryWrapper(new Address())
				.lambda()
				.eq(Address::getDataId,warehouse.getWhId())
				.eq(Address::getAddressDataType, Warehouse.DATA_TYPE)
			);

			List<Contacts> contactsList = contactsService.list(Condition.getQueryWrapper(new Contacts())
			.lambda()
			.eq(Contacts::getDataId,warehouse.getWhId())
			.eq(Contacts::getContactsDataType,Warehouse.DATA_TYPE)
			);
			warehouseVO.setAddressList(
				AddressWrapper.build().listVO(addressList));
			warehouseVO.setContactsList(
				ContactsWrapper.build().listVO(contactsList));
			Dept dept = SysCache.getDept(warehouse.getDeptId());
			if (Func.isNotEmpty(dept)) {
				warehouseVO.setDeptName(dept.getDeptName());
			}
		}
		return warehouseVO;
	}
	public WarehouseDTO entityDTO(WarehouseExcel warehouseExcel) {
		WarehouseDTO warehouseDTO = BeanUtil.copy(warehouseExcel, WarehouseDTO.class);
		if (Func.isNotEmpty(warehouseDTO)) {
			// 地址
			AddressDTO address = BeanUtil.copy(warehouseExcel, AddressDTO.class);
			if (Func.isNotEmpty(address) && !NodesUtil.isAllNull(address)) {
				address.setAddressDataType(Warehouse.DATA_TYPE);
				if(Func.isNotEmpty(warehouseExcel)&&Func.isNotEmpty(warehouseExcel.getAddressTypeDesc())){
					Dict dict = DictCache.list(DictConstant.ADDRESS_TYPE).stream()
					.filter(item->{
						return Func.equals(item.getDictValue(), warehouseExcel.getAddressTypeDesc());
					}).findFirst().orElse(null);
					address.setAddressType(dict.getDictKey());
				}
				if(Func.isNotEmpty(warehouseExcel)&&Func.isNotEmpty(warehouseExcel.getAddressDefaultFlag())){
					if("是".equals(warehouseExcel.getAddressDefaultFlag())){
						address.setDefaultFlag(1);
					}else{
						address.setDefaultFlag(0);
					}
				}
				warehouseDTO.getAddressList().add(address);
			}
			// 联系人
			ContactsDTO contacts = BeanUtil.copy(warehouseExcel, ContactsDTO.class);
			if (Func.isNotEmpty(contacts)  && !NodesUtil.isAllNull(contacts)) {
				if(Func.isNotEmpty(warehouseExcel)&&Func.isNotEmpty(warehouseExcel.getAddressDefaultFlag())){
					if("是".equals(warehouseExcel.getAddressDefaultFlag())){
						contacts.setDefaultFlag(1);
					}else{
						contacts.setDefaultFlag(0);
					}
				}
				contacts.setContactsDataType(Warehouse.DATA_TYPE);
				warehouseDTO.getContactsList().add(contacts);
			}
		}
		return warehouseDTO;
	}
	/**
	 * 库房实体转库存移动VO
	 * @return
	 */
	public static StockSkuMoveVO warehouse2StockSkuMoveVO(Warehouse warehouse) {
		StockSkuMoveVO stockSkuMoveVO = new StockSkuMoveVO();
		stockSkuMoveVO.setWhId(warehouse.getWhId());
		stockSkuMoveVO.setWhCode(warehouse.getWhCode());
		stockSkuMoveVO.setWhName(warehouse.getWhName());
		return stockSkuMoveVO;
	}
	/**
	 * 实体转导出DTO
	 * @param warehouse
	 * @return
	 */
	public WarehouseExcel warehouseToExportDTO(Warehouse warehouse) {
		WarehouseExcel warehouseExcel = BeanUtil.copy(warehouse, WarehouseExcel.class);
		if(Func.isNotEmpty(warehouseExcel)){
			if(Func.isNotEmpty(warehouse.getDeptId())){
				Dept dept = SysCache.getDept(warehouse.getDeptId());
				if(Func.isNotEmpty(dept)){
					warehouseExcel.setDeptCode(dept.getDeptCode());
					warehouseExcel.setDeptName(dept.getDeptName());
				}
			}
		}
		return warehouseExcel;
	}
}
