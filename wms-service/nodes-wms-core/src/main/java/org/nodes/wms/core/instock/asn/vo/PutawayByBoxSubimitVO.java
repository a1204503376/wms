package org.nodes.wms.core.instock.asn.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.validation.Add;
import org.nodes.core.tool.validation.Pda;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 按箱上架提交VO
 * @Author zx
 * @Date 2020/6/29
 **/
@Data
public class PutawayByBoxSubimitVO {

	/**
	 * 库房ID
	 */
	@ApiModelProperty("库房ID")
	@NotNull(groups = {Add.class}, message = "库房ID不能为空")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;

	/**
	 * 物品编码
	 */
	@ApiModelProperty("物品编码")
	@NotNull(groups = {Add.class}, message = "物品编码不能为空")
	private String skuCode;
	/**
	 * 计量单位编码
	 */
	@ApiModelProperty("计量单位编码")
	private String wsuCode;
	/**
	 * 计量单位名称
	 */
	@ApiModelProperty("计量单位名称")
	private String wsuName;
	/**
	 * 库位编码
	 */
	@ApiModelProperty("库位编码")
	@NotNull(groups = {Add.class}, message = "库位编码不能为空")
	private String locCode;

	/**
	 * 物品数量
	 */
	@ApiModelProperty("物品数量")
	private BigDecimal qty;

	/**
	 * 目标库位编码
	 */
	@ApiModelProperty("目标库位编码")
	@NotNull(groups = {Add.class, Pda.class},message = "目标库位不能为空！")
	private String targetLocCode;

	@ApiModelProperty("扫描箱号列表")
	@NotNull(groups = {Pda.class},message = "扫描箱号不能为空！")
	private List<String> boxCodes = new ArrayList<>();
	/**
	 * 库存ID
	 */
	@ApiModelProperty("库存ID")
	@NotNull(groups = {Add.class})
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;

	@ApiModelProperty("容器编码")
	private String lpnCode;
	/**
	 * 原库位id
	 */
	private Long sourceLocId;
	/**
	 * 目标库位id
	 */
	private Long targetLocId;

}
