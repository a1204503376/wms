
package org.nodes.wms.core.stock.core.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.stock.core.entity.StockLog;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 库存日志视图实体类
 *
 * @author pengwei
 * @since 2020-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "StockLogVO对象", description = "库存日志")
public class StockLogVO extends StockLog {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("序列号状态名称")
	private String serialStateName;

	@ApiModelProperty("处理类型名称")
	private String proTypeName;

	@ApiModelProperty("库房名称")
	private String whName;

	@ApiModelProperty("操作人")
	private String updateUserName;

	/**
	 * 库区名称
	 */
	@ApiModelProperty("库区名称")
	private String zoneName;

	/**
	 * 货主名称
	 */
	@ApiModelProperty("货主名称")
	private String ownerName;

	/**
	 * 计量单位
	 */
	@ApiModelProperty("计量单位")
	private String wsuName;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
}
