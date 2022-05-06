package org.nodes.wms.dao.instock.asn.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Asn单请求参数ID
 **/
@Data
public class DeleteRequest implements Serializable {

	private static final long serialVersionUID = 3964587884943011146L;

	/**
	 * 主键id
	 */
	@NotNull(message = "Asn单id不能为空")
	private List<Long> asnBillIds;
}
