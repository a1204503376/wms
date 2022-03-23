package org.nodes.wms.core.outstock.so.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 二次分拣VO
 * @Author zx
 * @Date 2020/5/8
 **/
@Data
public class TwicePickVO {

	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;
	/**
	 * 容器编码
	 */
	@ApiModelProperty("容器编码")
	private String lpnCode;

	/**
	 * 落放人
	 */
	@ApiModelProperty("落放人")
	private String pickUserName;

	/**
	 * 落放时间
	 */
	@ApiModelProperty("落放时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime pickDateTime;

	/**
	 * 监护状态
	 */
	@ApiModelProperty("拣货状态")
	private Integer pickState;

	/**
	 * 单据信息
	 */
	@ApiModelProperty("单据信息")
	private List<TwicePickSoHeaderVO> twicePickSoHeader;

}
