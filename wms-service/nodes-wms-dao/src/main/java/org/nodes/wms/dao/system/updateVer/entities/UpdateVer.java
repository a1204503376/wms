package org.nodes.wms.dao.system.updateVer.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 实体类
 */
@Data
@TableName("sys_update_ver")
public class UpdateVer extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 系统版本ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long suvId;
	/**
	 * 版本号数值
	 */
	private Integer verNum;
	/**
	 * 版本号名称
	 */
	private String verName;
	/**
	 * 模块类型
	 */
	private String moduleType;
	/**
	 * 更新地址
	 */
	private String updateUrl;
	/**
	 * 更新备注
	 */
	private String updateRemark;
	/**
	 * 更新类型
	 */
	private String updateType;
}
