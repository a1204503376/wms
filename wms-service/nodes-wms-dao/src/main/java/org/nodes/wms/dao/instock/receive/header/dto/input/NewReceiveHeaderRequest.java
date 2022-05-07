package org.nodes.wms.dao.instock.receive.header.dto.input;

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
	 * 库房编码
	 */
	private String whCode;
	/**
	 * 单据种类编码
	 */
	@NotNull(message = "单据类型不能为空")
	private String billTypeCd;
	/**
	 * 供应商编码
	 */
	private String sCode;

	/**
	 * 供应商名称
	 */
	private String sName;
	/**
	 * 入库方式
	 */
	private Integer instoreType;
	/**
	 * 货主id
	 */
	private Long woId;

	/**
	 * 货主编码
	 */
	private String ownerCode;
	/**
	 * 备注
	 */
	private String remark;

}
