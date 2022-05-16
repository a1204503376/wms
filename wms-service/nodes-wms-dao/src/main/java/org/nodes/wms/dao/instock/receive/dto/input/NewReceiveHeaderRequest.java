package org.nodes.wms.dao.instock.receive.dto.input;

import lombok.Data;

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
	 * 供应商ID
	 */
	private Long supplierId;
	/**
	 * 入库方式
	 */
	private Integer inStoreType;
	/**
	 * 货主id
	 */
	private Long woId;

	/**
	 * 备注
	 */
	private String remark;

}
