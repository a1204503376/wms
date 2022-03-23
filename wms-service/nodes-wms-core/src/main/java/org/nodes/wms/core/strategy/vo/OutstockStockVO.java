package org.nodes.wms.core.strategy.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.core.stock.core.entity.Stock;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengwei
 * @program WmsCore
 * @description 出库策略获取库存结果
 * @create 20200226
 */
@Data
public class OutstockStockVO {

	/**
	 * 明细批属性指定字符串集合
	 */
	@ApiModelProperty("明细批属性指定字符串集合")
	private List<String> soDetailSkuLotStrList = new ArrayList<>();
	/**
	 * 策略批属性指定字符串集合
	 */
	@ApiModelProperty("策略批属性指定字符串集合")
	private List<String> ssoSkuLotStrList = new ArrayList<>();
	/**
	 * 周转方式字符串集合
	 */
	@ApiModelProperty("周转方式字符串集合")
	private List<String> turnoverTypeStrList = new ArrayList<>();

	/**
	 * 获取到的库存信息集合
	 */
	@ApiModelProperty("获取到的库存信息集合")
	private List<Stock> stockList = new ArrayList<>();

	/**
	 * 获取库存执行脚本
	 */
	@ApiModelProperty("获取库存执行脚本")
	private String sqlCmd;
}
