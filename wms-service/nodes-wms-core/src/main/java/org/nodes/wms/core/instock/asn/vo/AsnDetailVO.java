package org.nodes.wms.core.instock.asn.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.core.basedata.vo.SkuLotConfigVO;
import org.nodes.wms.core.instock.asn.entity.AsnDetail;

import java.util.List;

/**
 * 收货单明细表视图实体类
 *
 * @author zx
 * @since 2019-12-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "DetailVO对象", description = "收货单明细表")
public class AsnDetailVO extends AsnDetail {

	/**
	 * 接收状态名称
	 */
	@ApiModelProperty("接收状态名称")
	private String detailStatusName;

	/**
	 * 包装明细ID
	 */
	@ApiModelProperty("包装明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspdId;

	/**
	 * 包装名称
	 */
	@ApiModelProperty("包装名称")
	private String wspName;

	/**
	 * 是否序列号管理
	 */
	@ApiModelProperty("是否序列号管理")
	private String isSn;

	/**
	 * 序列号导入状态
	 */
	@ApiModelProperty("序列号导入状态")
	private String importSnName;

	/**
	 * 层级名称
	 */
	@ApiModelProperty("层级名称")
	private String skuLevelName;

	/**
	 * 计划数量
	 */
	@ApiModelProperty("计划数量")
	private String planQtyName;

	/**
	 * 实际数量
	 */
	@ApiModelProperty("实际数量")
	private String scanQtyName;

	/**
	 * 剩余数量
	 */
	@ApiModelProperty("剩余数量")
	private String surplusQtyName;

	/**
	 * 批属性信息
	 */
	private List<SkuLotConfigVO> skuLotList;
	/**
	 * 包装明细列表
	 */
	private List<SkuPackageDetail> skuPackageDetails;


}
