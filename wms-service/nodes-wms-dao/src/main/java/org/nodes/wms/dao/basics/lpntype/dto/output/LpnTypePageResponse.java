package org.nodes.wms.dao.basics.lpntype.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.basics.lpntype.enums.LpnTypeEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 容器管理返回对象
 */
@Data
public class LpnTypePageResponse implements Serializable {
	private static final long serialVersionUID = -5339953896430012558L;
	/**
	 * 主键ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	/**
	 * 容器类型编码
	 */
	private String code;
	/**
	 * 容器类型(1:箱,2:托)
	 */
	private LpnTypeEnum type;
	/**
	 * 容器编码生成规则
	 */
	private String lpnNoRule;
	/**
	 * 容器重量(KG)
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private BigDecimal weight;
	/**
	 * 创建人
	 */
	private String createUser;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新人
	 */
	private String updateUser;
	/**
	 * 更新时间
	 */
	private Date updateTime;

	public String getType() {
		return type.value();
	}
}
