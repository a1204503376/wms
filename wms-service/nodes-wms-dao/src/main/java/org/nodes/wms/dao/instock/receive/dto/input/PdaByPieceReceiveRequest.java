package org.nodes.wms.dao.instock.receive.dto.input;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 按件页面收货请求对象
 **/
@Data
public class PdaByPieceReceiveRequest implements Serializable {
	/**
	 * 收货单id
	 */
	private Long receiveId;
	/**
	 * 收货单详情
	 */
	private Long receiveDetailId;
	/**
	 * 物料编码
	 */
	private String skuName;
	/**
	 * 物料名称
	 */
	private String skuCode;
	/**
	 * 型号
	 */
	private String skuLot2;
	/**
	 * 剩余数量
	 */
	private BigDecimal surplusQty;
	/**
	 * 生产批次
	 */
	private String skuLot1;
	/**
	 * 计划数量
	 */
	private BigDecimal planQty;
	/**
	 * 实收数量
	 */
	private BigDecimal scanQty;

	/**
	 * 计量单位
	 */
	private String wsuCode;
	/**
	 * 规格
	 */
	private String skuSpec;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 行号
	 */
	private String lineNo;
	/**
	 * 状态
	 */
	private Integer detailStatus;
	/**
	 * 是否序列号管理 （true：序列号管理  false：非序列号管理）
	 */
	private Boolean isSn;
	/**
	 * 箱码
	 */
	private String boxCode;
	/**
	 * 库位编码
	 */
	private String locCode;
	/**
	 * 序列号管理
	 */
	private List<String> serialNumberList;
	/**
	 * 库房编码
	 */
	private String whCode;
	/**
	 * 库房id
	 */
	private Long whId;
}
