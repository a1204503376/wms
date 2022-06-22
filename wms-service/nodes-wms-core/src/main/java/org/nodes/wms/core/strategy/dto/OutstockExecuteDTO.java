package org.nodes.wms.core.strategy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.warehouse.entity.Location;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengwei
 * @program WmsCore
 * @description 分配策略执行结果VO对象实体类
 * @since 2020-11-04
 */
@Data
public class OutstockExecuteDTO {
	/**
	 * 波次编码
	 */
	@ApiModelProperty("波次编码")
	private Long wellenNo;

	/**
	 * 订单编码
	 */
	@ApiModelProperty("订单编码")
	private String soBillNo;

	/**
	 * 库房ID
	 */
	@ApiModelProperty("库房ID")
	private Long whId;

	/**
	 * 物品编码
	 */
	@ApiModelProperty("物品编码")
	private String skuCode;

	/**
	 * 物品名称
	 */
	@ApiModelProperty("物品名称")
	private String skuName;

	/**
	 * 可用库存量
	 */
	@ApiModelProperty("可用库存量")
	private BigDecimal stockQty;

	/**
	 * 分配占用量
	 */
	@ApiModelProperty("分配占用量")
	private BigDecimal occupyQty;

	/**
	 * 分配策略ID
	 */
	@ApiModelProperty("分配策略ID")
	private Long ssoId;

	/**
	 * 分配策略编码
	 */
	@ApiModelProperty("分配策略编码")
	private String ssoCode;

	/**
	 * 分配策略名称
	 */
	@ApiModelProperty("分配策略名称")
	private String ssoName;

	/**
	 * 分配策略处理顺序
	 */
	@ApiModelProperty("分配策略处理顺序")
	private Integer ssodProcOrder;

	/**
	 * 分配计算代码
	 */
	@ApiModelProperty("分配计算代码")
	private String outstockFunction;

	/**
	 * 分配计算代码描述
	 */
	@ApiModelProperty("分配计算代码描述")
	private String outstockFunctionDesc;

	/**
	 * 货源库区名称
	 */
	@ApiModelProperty("货源库区名称")
	private String sourceZoneName;

	/**
	 * 明细批次号
	 */
	@ApiModelProperty("明细批次号")
	private String detailLotNumber;

	/**
	 * 明细序列号
	 */
	@ApiModelProperty("明细序列号")
	private String detailSerialNumber;

	/**
	 * 明细批属性指定
	 */
	@ApiModelProperty("明细批属性指定")
	private String detailSkuLot;

	/**
	 * 策略批属性指定
	 */
	@ApiModelProperty("策略批属性指定")
	private String ssoSkuLot;

	/**
	 * 周转方式
	 */
	@ApiModelProperty("周转方式")
	private String turnoverType;

	/**
	 * 库存取得脚本
	 */
	@ApiModelProperty("库存取得脚本")
	private String sqlCmd;

	/**
	 * 获取到的库存集合
	 */
	@ApiModelProperty("获取到的库存集合")
	private List<Stock> stockList = new ArrayList<>();

	/**
	 * 是否成功
	 */
	@ApiModelProperty("是否成功")
	private Boolean success;

	/**
	 * 异常消息
	 */
	@ApiModelProperty("异常消息")
	private String exception;
	/**
	 * 用户编码
	 */
	@ApiModelProperty("用户编码")
	private String userCode;
	/**
	 * 用户名称
	 */
	@ApiModelProperty("用户名称")
	private String userName;
	/**
	 * 执行时间
	 */
	@ApiModelProperty("执行时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime executeTime;
	/**
	 * 货源库区内库位集合
	 */
	@ApiModelProperty("货源库区内库位集合")
	private List<Location> locationList;

	/**
	 * 重写 Exception 的 Set 方法（目的：已经有异常的情况下，就不提示后面的异常了）
	 * @param exception
	 */
	public void setException(String exception){
		if (Func.isEmpty(this.exception)) {
			this.exception = exception;
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("物品编码：" + this.skuCode + "; 物品名称：" + this.skuName + "\r\n");
		sb.append("分配策略：" + this.ssoName + "; 货源库区：" + this.sourceZoneName + "\r\n");
		sb.append("明细批次号：" + this.detailLotNumber + "; 明细序列号：" + this.detailSerialNumber + "\r\n");
		sb.append("明细.批属性指定：" + this.detailSkuLot + "\r\n");
		sb.append("策略.批属性指定：" + this.ssoSkuLot + "\r\n");
		sb.append("周转方式：" + this.turnoverType + "\r\n");
//		sb.append("库存取得脚本：" + this.sqlCmd + "\r\n");
		if (Func.isNotEmpty(this.exception)) {
			sb.append("异常消息：" + this.exception + "\r\n");
		}
		return sb.toString();
	}
}
