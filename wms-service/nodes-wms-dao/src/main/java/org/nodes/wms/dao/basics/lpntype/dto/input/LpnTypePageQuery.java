package org.nodes.wms.dao.basics.lpntype.dto.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;

/**
 * 容器管理分页对象
 */
@Data
public class LpnTypePageQuery implements Serializable {

	private static final long serialVersionUID = 7506165517114244660L;
	/**
	 * 容器类型(1:箱,2:托)
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	Integer lpnType;
	/**
	 * 容器类型编码
	 */
	private String code;
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
