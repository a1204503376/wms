package org.nodes.wms.core.instock.asn.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.instock.asn.entity.AsnHeader;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 收货单提交VO
 * @Author zx
 * @Date 2020/4/1
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "收货单提交VO", description = "收货单提交VO")
public class AsnHeaderDTO extends AsnHeader {


	/**
	 * 物品关键词
	 */
	@ApiModelProperty("物品关键词")
	private String skuKeyword;

	/**
	 * 供应商关键词
	 */
	@ApiModelProperty("供应商关键词")
	private String sKeyword;

	/**
	 * 会计科目编码
	 */
	@ApiModelProperty(value = "会计科目编码")
	private String saccCode;

	/**
	 * 供应商名称
	 */
	@ApiModelProperty(value = "供应商名称")
	private String sName;

	/**
	 * 预计到货时间范围
	 */
	@ApiModelProperty("预计到货时间范围")
	private String scheduledArrivalRange;

	/**
	 * 预计到货开始时间 mapper查询
	 */
	@ApiModelProperty("预计到货开始时间 mapper查询")
	private String scheduledArrivalStart;

	/**
	 * 预计到货结束时间 mapper查询
	 */
	@ApiModelProperty("预计到货结束时间 mapper查询")
	private String scheduledArrivalEnd;
	/**
	 * 实际到货时间范围
	 */
	@ApiModelProperty("实际到货时间范围")
	private String actualArrivalRange;

	/**
	 * 实际到货开始时间 mapper查询
	 */
	@ApiModelProperty("实际到货开始时间 mapper查询")
	private String actualArrivalStart;

	/**
	 * 实际到货结束时间 mapper查询
	 */
	@ApiModelProperty("实际到货结束时间 mapper查询")
	private String actualArrivalEnd;

	/**
	 * 单据完成时间范围
	 */
	@ApiModelProperty("单据完成时间范围")
	private String finishRange;

	/**
	 * 单据完成开始时间 mapper查询
	 */
	@ApiModelProperty("单据完成开始时间 mapper查询")
	private String finishStart;

	/**
	 * 单据完成结束时间 mapper查询
	 */
	@ApiModelProperty("单据完成结束时间 mapper查询")
	private String finishEnd;
	/**
	 * 收货单明细
	 */
	@ApiModelProperty(value = "收货单明细列表")
	private List<AsnDetailDTO> asnDetailList = new ArrayList<>();

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "预计到货时间")
	private Date scheduledArrivalDateCd;
}
