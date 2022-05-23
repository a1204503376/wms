package org.nodes.wms.dao.instock.receive.dto.output;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierSelectResponse;

import java.io.Serializable;
import java.util.List;

@Data
public class EditReceiveHeaderResponse implements Serializable {
	private static final long serialVersionUID = -9050702110418702134L;
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
	/**
	 * 收货单明细id集合
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private List<Long> receiveDetailIdList;
}
