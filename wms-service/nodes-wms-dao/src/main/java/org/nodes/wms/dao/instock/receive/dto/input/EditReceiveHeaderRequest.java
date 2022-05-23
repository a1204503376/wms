package org.nodes.wms.dao.instock.receive.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.basics.suppliers.dto.input.SupplierSelectRequest;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierSelectResponse;

import java.io.Serializable;
import java.util.List;

/**
 * 编辑收货单页面接收前端头表信息类
 */
@Data
public class EditReceiveHeaderRequest implements Serializable {
	private static final long serialVersionUID = 7097117273300642931L;
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
	private SupplierSelectRequest supplier;

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
	 * 移除收货单明细id集合
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private List<Long> receiveDetailIdList;
}
