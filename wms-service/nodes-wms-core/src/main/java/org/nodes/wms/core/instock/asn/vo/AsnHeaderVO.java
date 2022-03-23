package org.nodes.wms.core.instock.asn.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.instock.asn.entity.AsnHeader;

import java.util.List;

/**
 * 收货单返回VO
 *
 * @author zx
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "收货单返回VO", description = "收货单返回VO")
public class AsnHeaderVO extends AsnHeader {
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
	 * 收货单明细
	 */
	@ApiModelProperty(value = "收货单明细")
	private List<AsnDetailVO> asnDetailList;

	/**
	 * 供应商ID
	 */
	@ApiModelProperty(value = "供应商ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long sId;

	/**
	 * 箱号
	 */
	@ApiModelProperty(value = "箱号")
	private String boxCode;


	/**
	 * 明细单完成数
	 */
	@ApiModelProperty(value = "明细单完成数")
	private Integer finish;

	/**
	 * 明细单总数
	 */
	@ApiModelProperty(value = "明细单总数")
	private Integer count;

	/**
	 * 箱号规则
	 */
	@ApiModelProperty(value = "箱号规则")
	private Integer ruleCode;
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
