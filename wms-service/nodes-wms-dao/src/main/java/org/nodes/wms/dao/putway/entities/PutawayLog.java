package org.nodes.wms.dao.putway.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

import java.util.Date;

@Data
@TableName("putaway_log")
public class PutawayLog extends TenantEntity {
	/**
	 * 主键id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long apiId;
	/**
	 * 容器编码
	 */
	private String lpnCode;
	/**
	 * 目标库位编码
	 */
	private String targetLocCode;
	/**
	 * 上架策略id
	 */
	private Long ssiId;
	/**
	 * 上架策略编码
	 */
	private String ssiCode;
	/**
	 * 上架策略名称
	 */
	private String ssiName;
	/**
	 * 库房id
	 */
	private Long whId;
	/**
	 * 处理顺序
	 */
	private Integer ssidProcOrder;
	/**
	 * 上架计算代码
	 */
	private Integer instockFunction;
	/**
	 * 用户编码
	 */
	private String userCode;
	/**
	 * 用户名称
	 */
	private String userName;
	/**
	 * 执行时间
	 */
	private Date apiTime;
	/**
	 * 执行内容
	 */
	private String aplProcLog;
	/**
	 * 执行顺序
	 */
	private Integer executeOrder;
	/**
	 * 是否成功
	 */
	private Integer isSuccess;





}
