package org.nodes.wms.core.instock.inventory.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import org.nodes.wms.core.instock.asn.vo.ContainerLogVO;
import org.nodes.wms.core.instock.inventory.entity.AsnInventory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

import java.util.List;

/**
 * 收货清单头表视图实体类
 *
 * @author NodeX
 * @since 2021-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "AsnInventoryVO对象", description = "收货清单头表")
public class AsnInventoryVO extends AsnInventory {
	private static final long serialVersionUID = 1L;

	/**
	 * 单据状态
	 */
	@ApiModelProperty(value = "单据状态名称")
	private String asnBillStateName;
	/**
	 * 入库方式
	 */
	@ApiModelProperty(value = "入库方式名称")
	private String instoreTypeName;
	/**
	 * 同步状态
	 */
	@ApiModelProperty(value = "同步状态名称")
	private String syncStateName;

	/**
	 * 货主名称
	 */
	@ApiModelProperty(value = "货主名称")
	private String ownerName;
	/**
	 * 货主名称
	 */
	@ApiModelProperty(value = "单据类型名称")
	private String billTypeName;

	/**
	 * 库房名称
	 */
	@ApiModelProperty(value = "库房名称")
	private String whName;
	/**
	 * 创建人名称
	 */
	@ApiModelProperty(value = "创建人名称")
	private String createTypeName;
	/**
	 * 创建人名称
	 */
	@ApiModelProperty(value = "创建人名称")
	private String createUserName;


	/**
	 * 收货清单列表
	 */
	@ApiModelProperty(value = "收货清单列表")
	private List<ContainerLogVO> detailList;

	/**
	 * 供应商ID
	 */
	@ApiModelProperty(value = "供应商ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long sId;

	/**
	 * 过账方式描述
	 */
	@ApiModelProperty("过账方式描述")
	private String postStateCd;
	/**
	 * 过账人员描述
	 */
	@ApiModelProperty("过账人员描述")
	private String postUserCd;
}
