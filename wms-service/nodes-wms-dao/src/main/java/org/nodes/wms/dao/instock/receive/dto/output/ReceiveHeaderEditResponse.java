package org.nodes.wms.dao.instock.receive.dto.output;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.basics.billType.dto.BillTypeSelectResponse;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierSelectResponse;
import org.nodes.wms.dao.basics.warehouse.dto.output.WarehouseResponse;

@Data
public class ReceiveHeaderEditResponse {
	/**
	 * 收货单主键id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long receiveId;
	/**
	 * 库房id
	 *
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;

	/**
	 * 单据类型
	 */
	 private String billTypeCd;

	/**
	 * 供应商下拉返回对象
	 */
	private SupplierSelectResponse supplier;

	/**
	 * 入库方式
	 */
	private Integer inStoreType;
	/**
	 * 货主id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long woId;

	/**
	 * 备注
	 */
	private String remark;
}
