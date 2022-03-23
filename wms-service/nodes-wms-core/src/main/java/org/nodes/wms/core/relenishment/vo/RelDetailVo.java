package org.nodes.wms.core.relenishment.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.core.basedata.vo.SkuLotConfigVO;
import org.nodes.wms.core.relenishment.entity.RelDetail;
import org.nodes.wms.core.warehouse.entity.Zone;

import java.util.List;

/**
 * 补货单头表VO实体类
 *
 * @author whj
 * @since 2019-12-13
 */
@Data
@ApiModel(value = "RelHeaderVo对象", description = "补货单头表")
public class RelDetailVo extends RelDetail {
	private String relBillStateDesc;
	/**
	 * 从库房名称
	 */
	@ApiModelProperty(value = "从库房名称")
	private String fromWhName;
	/**
	 * 从库区名称
	 */
	@ApiModelProperty(value = "从库区名称")
	private String fromZoneName;
	/**
	 * 从库位名称
	 */
	@ApiModelProperty(value = "从库位名称")
	private String fromLocName;
	/**
	 * 从容器名称
	 */
	@ApiModelProperty(value = "从容器名称")
	private String fromLpnName;

	/**
	 * 至库房名称
	 */
	@ApiModelProperty(value = "至库房名称")
	private String toWhName;
	/**
	 * 至库区名称
	 */
	@ApiModelProperty(value = "至库区名称")
	private String toZoneName;
	/**
	 * 至库位名称
	 */
	@ApiModelProperty(value = "至库位名称")
	private String toLocName;
	/**
	 * 从至容器名称
	 */
	@ApiModelProperty(value = "至容器名称")
	private String toLpnName;
	/**
	 * 物品规格
	 */
	@ApiModelProperty(value = "物品规格")
	public String skuSpec;
	/**
	 * 货主名称
	 */
	@ApiModelProperty(value = "货主名称")
	public String ownerName;
	/**
	 * 包装名称
	 */
	@ApiModelProperty(value = "包装名称")
	public String wspName;
	/**
	 * 批属性
	 */
	@ApiModelProperty(value = "批属性")
	public List<SkuLotConfigVO> skuLotValList;
	/**
	 * 库区
	 */
	@ApiModelProperty(value = "库区")
	public List<Zone> zoneList;
}
