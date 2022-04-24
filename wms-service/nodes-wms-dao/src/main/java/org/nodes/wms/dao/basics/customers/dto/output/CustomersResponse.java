package org.nodes.wms.dao.basics.customers.dto.output;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 客户表 返回前端视图类
 **/
@Data
public class CustomersResponse {
	/**
	 * 客户ID
	 */
	private Long id;
	/**
	 * 客户编码
	 */
	private String code;
	/**
	 * 客户名称
	 */
	private String name;
	/**
	 * 客户简称
	 */
	private String  simpleName;
	/**
	 * 货主ID
	 */
	private String  woId;
	/**
	 * 国家
	 */
	private String  country;
	/**
	 * 省
	 */
	private String  province;
	/**
	 * 城市
	 */
	private String  city;
	/**
	 * 街道
	 */
	private String  address;
	/**
	 * 邮编
	 */
	private String  zipCode;
	/**
	 * 备注
	 */
	private String  remark;
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
	/**
	 * 业务状态
	 */
	private Integer status;
}
