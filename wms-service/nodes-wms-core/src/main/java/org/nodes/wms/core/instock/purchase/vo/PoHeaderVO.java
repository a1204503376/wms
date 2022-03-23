package org.nodes.wms.core.instock.purchase.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import org.nodes.wms.core.instock.purchase.entity.PoHeader;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

import java.util.List;

/**
 * 采购单头表视图实体类
 *
 * @author NodeX
 * @since 2021-05-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PoHeaderVO对象", description = "采购单头表")
public class PoHeaderVO extends PoHeader {
	private static final long serialVersionUID = 1L;
	/**
	 * 单据状态
	 */
	@ApiModelProperty(value = "单据状态名称")
	private String poBillStateName;
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
	 * 收货单明细
	 */
	@ApiModelProperty(value = "收货单明细")
	private List<PoDetailVO> detailList;

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
