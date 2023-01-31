package org.nodes.wms.dao.common.query.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

import java.io.Serializable;

/**
 * @author nodes
 * 查询方案
 */
@Data
@TableName("nodes_query_plan")
public class NodesQueryPlan extends TenantEntity implements Serializable {
	private static final long serialVersionUID = 6641307468424191570L;
	/**
	 * 主键ID
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

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
