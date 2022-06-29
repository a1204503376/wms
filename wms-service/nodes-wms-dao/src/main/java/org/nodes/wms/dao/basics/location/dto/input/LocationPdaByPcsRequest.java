package org.nodes.wms.dao.basics.location.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 查询库位的查询条件
 **/
@Data
public class LocationPdaByPcsRequest implements Serializable {
	private static final long serialVersionUID = 294694183834424313L;

	/**
	 * 库房id
	 */
	private Long whId;

	/**
	 * 库位编码
	 */
	private String locCode;
}
