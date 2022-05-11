package org.nodes.wms.dao.basics.customer.dto.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户表 分页参数
 **/
@Data
public class CustomerPageQuery  implements Serializable{

	private static final long serialVersionUID = 5597654611681570858L;
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
	 * 创建时间开始
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date  createTimeBegin;
	/**
	 * 创建时间结束
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date createTimeEnd;
	/**
	 * 更新时间开始
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date updateTimeBegin;
	/**
	 * 更新时间结束
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date  updateTimeEnd;
}
