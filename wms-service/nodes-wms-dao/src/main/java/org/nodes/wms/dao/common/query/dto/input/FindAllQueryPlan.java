package org.nodes.wms.dao.common.query.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * @author nodes
 * 查询 查询方案请求对象
 */
@Data
public class FindAllQueryPlan implements Serializable {
	private static final long serialVersionUID = -7586571541937051208L;
	/**
	 * 页面路径
	 */
	private String pageUrl;
}
