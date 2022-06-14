package org.nodes.wms.core.warehouse.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.common.vo.AddressVO;
import org.nodes.wms.core.common.vo.ContactsVO;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;

import java.util.List;

/**
 * 仓库视图实体类
 *
 * @author Wangjw
 * @since 2019-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WarehouseVO对象", description = "仓库")
public class WarehouseVO extends Warehouse {
	private static final long serialVersionUID = 1L;

	/**
	 * 地址信息集合
	 */
	@ApiModelProperty("地址信息集合")
	private List<AddressVO> addressList;

	/**
	 * 联系人集合
	 */
	@ApiModelProperty("联系人集合")
	private List<ContactsVO> contactsList;

	/**
	 * 是否启用
	 */
	@ApiModelProperty("是否启用")
	private String statusName;

	/**
	 * 机构名称
	 */
	@ApiModelProperty("机构名称")
	private String deptName;
	/**
	 * 机构编码
	 */
	@ApiModelProperty("机构编码")
	private String deptCode;
}
