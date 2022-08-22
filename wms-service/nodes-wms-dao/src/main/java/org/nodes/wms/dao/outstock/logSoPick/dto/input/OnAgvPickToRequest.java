package org.nodes.wms.dao.outstock.logSoPick.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 接驳区拣货请求对象
 *
 * @author nodesc
 */
@Data
public class OnAgvPickToRequest implements Serializable {
	private static final long serialVersionUID = -2835186246027646361L;
	/**
	 * 库位ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;

	/**
	 * 库位编码
	 */
	private String locCode;

	/**
	 * 前端页面显示的库位视图
	 */
	private  String locCodeView;
}
