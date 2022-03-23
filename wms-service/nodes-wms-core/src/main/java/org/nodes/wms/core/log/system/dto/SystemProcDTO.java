package org.nodes.wms.core.log.system.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.core.log.system.enums.ActionEnum;
import org.nodes.wms.core.log.system.enums.DataTypeEnum;
import org.nodes.wms.core.log.system.enums.PlatformEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author pengwei
 * @program WmsCore
 * @description 记录系统日志参数类
 * @create 20200310
 */
@Data
public class SystemProcDTO {

	/**
	 * 系统区分
	 */
	@ApiModelProperty("系统区分")
	private PlatformEnum platform;
	/**
	 * 业务种类（必填）
	 */
	@ApiModelProperty("业务种类")
	@NotNull(message = "业务种类为必填项，不允许为空！")
	private SystemProcTypeEnum procType;
	/**
	 * 数据项目类型
	 */
	@ApiModelProperty("数据项目类型")
	private DataTypeEnum dataType;
	/**
	 * 业务动作（必填）
	 */
	@ApiModelProperty("业务动作")
	@NotNull(message = "业务动作为必填项，不允许为空！")
	private ActionEnum action;
	/**
	 * 单据No
	 */
	@ApiModelProperty("单据No")
	private String billNo;
	/**
	 * 库位编码
	 */
	@ApiModelProperty("库位编码")
	private String locCode;
	/**
	 * 容器编码
	 */
	@ApiModelProperty("容器编码")
	private String lpnCode;
	/**
	 * 箱号
	 */
	@ApiModelProperty("箱号")
	private String boxCode;
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
	 * 操作数量
	 */
	@ApiModelProperty("操作数量")
	private BigDecimal operationQty;
	/**
	 * 操作单位
	 */
	@ApiModelProperty("操作单位")
	private String operationUnit;
	/**
	 * 库房ID
	 */
	@ApiModelProperty("库房ID")
	private Long whId;
	/**
	 * 备注
	 */
	@ApiModelProperty("备注")
	private String remark;
}
