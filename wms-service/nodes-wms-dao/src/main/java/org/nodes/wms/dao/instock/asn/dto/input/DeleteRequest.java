package org.nodes.wms.dao.instock.asn.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Asn单请求参数ID
 *
 * @author 彭永程
 * @date 2022-04-22 14:20
 **/
@Data
public class DeleteRequest {

	/**
	 * 主键id
	 */
	@NotNull(message = "Asn单id不能为空")
	private List<Long> asnBillId;
}
