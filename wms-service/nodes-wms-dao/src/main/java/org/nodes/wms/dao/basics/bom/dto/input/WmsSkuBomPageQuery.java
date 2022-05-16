package org.nodes.wms.dao.basics.bom.dto.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 分页参数
 **/
@Data
public class WmsSkuBomPageQuery implements Serializable {

	private static final long serialVersionUID = -4587386109231831126L;
	/**
	 * 组合物品编码
	 */
	private String joinSkuCode;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 货主ID
	 */
	private Long woId;
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
