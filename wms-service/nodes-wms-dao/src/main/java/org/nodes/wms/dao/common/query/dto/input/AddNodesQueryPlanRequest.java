package org.nodes.wms.dao.common.query.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * @author nodes
 * 新增查询方案Dto
 */
@Data
public class AddNodesQueryPlanRequest implements Serializable {
	private static final long serialVersionUID = 8902235633059835658L;
	/**
	 * 方案名称
	 */
	private String name;

	/**
	 * 是否默认,1(默认)
	 */
	private Integer isDefault;

	/**
	 * 是否默认初始化结果,1(默认)
	 */
	private Integer isInitData;

	/**
	 * 页面路径
	 */
	private String pageUrl;

	/**
	 * 查询方案条件数据
	 */
	private String queryData;
}
