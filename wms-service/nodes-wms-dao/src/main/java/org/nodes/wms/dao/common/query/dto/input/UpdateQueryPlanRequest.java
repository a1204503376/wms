package org.nodes.wms.dao.common.query.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * @author nodes
 * 修改查询方案请求对象
 */
@Data
public class UpdateQueryPlanRequest implements Serializable {
	private static final long serialVersionUID = -4466755898461133200L;
	/**
	 * ID
	 */
	private Long id;
	/**
	 * 是否默认
	 */
	private Integer isDefault;
	/**
	 *
	 */
	private String pageUrl;
}
