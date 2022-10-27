package org.nodes.wms.dao.instock.receive.dto.input;

import lombok.Data;
import org.nodes.wms.dao.basics.suppliers.dto.input.SupplierSelectRequest;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 新建收货单接收前端参数
 **/
@Data
public class NewReceiveHeaderRequest implements Serializable {
	private static final long serialVersionUID = -4429594116247260327L;
	/**
	 * 库房id
	 *
	 */
	private Long whId;

	/**
	 * 单据种类编码
	 */
	@NotNull(message = "单据类型不能为空")
	private String billTypeCd;

	/**
	 * 供应商下拉框对象
	 */
	private SupplierSelectRequest supplier;

	/**
	 * 归还人
	 */
	private String supplierContact;

	/**
	 * 入库方式
	 */
	private Integer inStoreType;
	/**
	 * 货主id
	 */
	private Long woId;
	/**
	 * 文件编码
	 */
	private String udf1;
	/**
	 * 备注
	 */
	private String remark;

}
