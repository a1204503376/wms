package org.nodes.wms.dao.common.log.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息返回前端视图类
 */
@Data
public class LogMessageResponse implements Serializable {
	private static final long serialVersionUID = -8930966003867605814L;
	/**
	 * 消息id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	/**
	 * 仓库id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 消息内容
	 */
	private String log;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 是否已读
	 */
   private Integer readed;
}
