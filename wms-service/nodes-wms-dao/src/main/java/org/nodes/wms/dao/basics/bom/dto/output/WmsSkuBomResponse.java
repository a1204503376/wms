package org.nodes.wms.dao.basics.bom.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 分页返回
 */
@Data
public class WmsSkuBomResponse implements Serializable {
	private static final long serialVersionUID = -1460219987226781283L;
	/**
	 * 主键id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	/**
	 * 货主编码
	 */
	private String ownerCode;
	/**
	 * 货主名称
	 */
	private String ownerName;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 物品名称
	 */
	private String skuName;
	/**
	 * 主单位编码
	 */
	private String wsuCode;
	/**
	 * 主单位名称
	 */
	private String wsuName;
	/**
	 * 组合物品ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long joinSkuId;
	/**
	 * 组合物品编码
	 */
	private String joinSkuCode;
	/**
	 * 组合物品名称
	 */
	private String joinSkuName;
	/**
	 * 组合数量
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private BigDecimal qty;
	/**
	 * 组合单位编码
	 */
	private String joinWsuCode;
	/**
	 * 组合单位名称
	 */
	private String joinWsuName;
	/**
	 * 更新人
	 */
	private String updateUser;
	/**
	 * 更新时间
	 */
	private Date updateTime;

}
