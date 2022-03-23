package org.nodes.wms.core.stock.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.stock.core.entity.SerialLog;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 序列号日志数据传输对象实体类
 *
 * @author zx
 * @since 2020-02-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SerialLogDTO extends SerialLog {
	private static final long serialVersionUID = 1L;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty("操作开始时间")
	private Date procTimeStart;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty("操作结束时间")
	private Date procTimeEnd;

}
