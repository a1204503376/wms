package org.nodes.wms.dao.instock.asn.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Asn单主键id 参数类
 *
 * @author 彭永程
 * @date 2022-04-25 14:44
 **/
@Data
public class AsnBillIdRequest implements Serializable {

	private static final long serialVersionUID = 4945960289253418906L;

	@NotNull(message = "Asn单id不能为空")
	private Long asnBillId;
}
