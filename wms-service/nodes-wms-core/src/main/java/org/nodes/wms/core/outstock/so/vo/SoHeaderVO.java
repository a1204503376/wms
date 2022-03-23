
package org.nodes.wms.core.outstock.so.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.core.outstock.so.entity.WellenDetail;
import org.springblade.core.tool.utils.StringPool;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 出库单主表实体类
 *
 * @author zhonglianshuai
 * @since 2020-02-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "HeaderVO对象", description = "HeaderVO对象")
public class SoHeaderVO extends SoHeader {
	/**
	 * 客户ID
	 */
	@ApiModelProperty(value = "客户ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long cId;
	/**
	 * 承运商ID
	 */
	@ApiModelProperty(value = "承运商ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long expressId;
	/**
	 * 单据状态
	 */
	@ApiModelProperty(value = "单据状态名称")
	private String soBillStateName;
	/**
	 * 出库方式
	 */
	@ApiModelProperty(value = "出库方式名称")
	private String outstockTypeName;
	/**
	 * 同步状态
	 */
	@ApiModelProperty(value = "同步状态名称")
	private String syncStateName;
	/**
	 * 创建类型
	 */
	@ApiModelProperty(value = "创建类型名称")
	private String createTypeName;
	/**
	 * 货主名称
	 */
	@ApiModelProperty(value = "货主名称")
	private String ownerName;
	/**
	 * 单据类型名称
	 */
	@ApiModelProperty(value = "单据类型名称")
	private String billTypeName;
	/**
	 * 库房名称
	 */
	@ApiModelProperty(value = "库房名称")
	private String whName;
	/**
	 * 出库单明细
	 */
	@ApiModelProperty(value = "出库单明细")
	private IPage<SoDetailVO> soDetailIPage;
	/**
	 * 实际发货时间
	 */
	@DateTimeFormat(
		pattern = "yyyy-MM-dd"
	)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "实际发货时间")
	private Date outStockDateTime;

	/**
	 * 发货方式描述
	 */
	@ApiModelProperty(value = "发货方式描述")
	private String transportDesc;

	/**
	 * 库区类型描述
	 */
	@ApiModelProperty(value = "库区类型描述")
	private String zoneTypeDesc;

	/**
	 * 创建人名称
	 */
	@ApiModelProperty(value = "创建人名称")
	private String billCreator;

	/**
	 * 明细集合
	 */
	private List<SoDetailVO> detailList;

	/**
	 * 报表名称
	 */
	@ApiModelProperty("报表名称")
	private String reportFileName;

	/**
	 * 发运状态描述
	 */
	@ApiModelProperty("发运状态描述")
	private String shipStateDesc;

	/**
	 * 过账状态
	 */
	@ApiModelProperty("过账状态")
	private String postStateCd;
	/**
	 * 过账人
	 */
	@ApiModelProperty("过账人")
	private String postUserCd;

	/**
	 * 根據索引获取单据自定义字段
	 *
	 * @param index 索引（范围：1~10)
	 * @return 单据自定义字段
	 */
	public String getAttribute(int index) {
		switch (index) {
			case 1:
				return getAttribute1();
			case 2:
				return getAttribute2();
			case 3:
				return getAttribute3();
			case 4:
				return getAttribute4();
			case 5:
				return getAttribute5();
			case 6:
				return getAttribute6();
			case 7:
				return getAttribute7();
			case 8:
				return getAttribute8();
			case 9:
				return getAttribute9();
			case 10:
				return getAttribute10();
			default:
				return StringPool.EMPTY;
		}
	}
}
