package org.nodes.wms.core.basedata.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.basedata.entity.SkuLog;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 物品操作记录表视图实体类
 *
 * @author pengwei
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SkuLogVO对象", description = "物品操作记录表")
public class SkuLogVO extends SkuLog {
	private static final long serialVersionUID = 1L;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
}
