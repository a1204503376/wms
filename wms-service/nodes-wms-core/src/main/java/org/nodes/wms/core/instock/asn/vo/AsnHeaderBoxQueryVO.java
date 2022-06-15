package org.nodes.wms.core.instock.asn.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.entity.SkuPackage;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
import org.nodes.wms.core.basedata.vo.SkuLotConfigVO;
import org.nodes.wms.core.instock.asn.entity.AsnDetail;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: WmsCore
 * @description: 按箱收货 箱码查询VO类
 * @author: pengwei
 * @create: 2020-12-03 14:04
 **/
@Data
public class AsnHeaderBoxQueryVO {

	/**
	 * 物品详细信息
	 */
	@ApiModelProperty("物品详细信息")
	private Sku sku;
	/**
	 * 扫描的入库明细(只有一条数据，避免手持改代码)
	 */
	@ApiModelProperty("扫描的入库明细")
	private List<AsnDetail> asnDetails;
	/**
	 * 入库明细的包装详细信息
	 */
	@ApiModelProperty("入库明细的包装详细信息")
	private SkuPackage skuPackage;
	/**
	 * 入库明细包装明细集合
	 */
	@ApiModelProperty("入库明细包装明细集合")
	private List<SkuPackageDetail> skuPackageDetails;
	/**
	 * 收货任务进度
	 */
	@ApiModelProperty("收货任务进度")
	private AsnDetailMinVO asnDetail;
	/**
	 * 物品批属性配置
	 */
	@ApiModelProperty("物品批属性配置")
	private List<SkuLotConfigVO> skuConfig;

	@ApiModelProperty("物品总计划数")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal totalPlanQty;

	@ApiModelProperty("物品当前收货数")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal totalScanQty;
}
