package org.nodes.wms.dao.basics.carrier.dto.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 承运商表 分页参数
 **/
@Data
public class CarrierPageQuery implements Serializable {

	private static final long serialVersionUID = 3575200749615041207L;
	/**
	 * 承运商编码
	 */
	private String code;
	/**
	 * 承运商名称
	 */
	private String name;
	/**
	 * 承运商简称
	 */
	private String  simpleName;
	/**
	 * 创建时间开始
	 */
	@JsonFormat(pattern = DateUtil.PATTERN_DATE)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
	private Date createTimeBegin;
	/**
	 * 创建时间结束
	 */
	@JsonFormat(pattern = DateUtil.PATTERN_DATE)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
	private Date createTimeEnd;
	/**
	 * 更新时间开始
	 */
	@JsonFormat(pattern = DateUtil.PATTERN_DATE)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
	private Date  updateTimeBegin;
	/**
	 * 更新时间结束
	 */
	@JsonFormat(pattern = DateUtil.PATTERN_DATE)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
	private Date  updateTimeEnd;
}
