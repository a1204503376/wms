package org.nodes.wms.core.warehouse.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.common.entity.Address;
import org.nodes.wms.core.common.entity.Contacts;
import org.nodes.wms.core.warehouse.entity.Warehouse;

import java.util.ArrayList;
import java.util.List;

/**
 * 仓库数据传输对象实体类
 *
 * @author Wangjw
 * @since 2019-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WarehouseDTO extends Warehouse {
	private static final long serialVersionUID = 1L;

	/**
	 * 仓库地址详情
	 */
	@ApiModelProperty(value = "仓库地址详情")
	private List<Address> addressList = new ArrayList<>();

	/**
	 * 仓库联系人详情
	 */
	@ApiModelProperty(value = "仓库联系人详情")
	private List<Contacts> contactsList = new ArrayList<>();

	/**
	 * 删除的地址集合
	 */
	@ApiModelProperty("删除的地址集合")
	private List<Address> addressDeletedList;

	/**
	 * 删除的联系人集合
	 */
	@ApiModelProperty("删除的联系人集合")
	private List<Contacts> contactsDeletedList;
}
