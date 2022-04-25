package org.nodes.wms.dao.basics.suppliers.dto.input;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.Date;

/**
 * 供应商单创建请求对象
 */
@Data
public class SuppliersRequest {
	/**
	 * 供应商编码
	 */
	private String code;

	/**
	 * 供应商名称
	 */
	private String name;

	/**
	 * 供应商简称
	 */
	private String simpleName;

	/**
	 * 货主id
	 */
	private Long woId;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 是否启用(0:启用,-1:未启用)
	 */
	private Integer status;
}
