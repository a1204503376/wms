package org.nodes.wms.dao.basics.carrier.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 承运商返回前端视图类
 **/
@Data
public class CarrierDropDownResponse implements Serializable {
	private static final long serialVersionUID = -2879832294012470398L;
	/**
	 * 承运商编码
	 */
	private String code;
	/**
	 * 承运商名称
	 */
	private String name;
}
