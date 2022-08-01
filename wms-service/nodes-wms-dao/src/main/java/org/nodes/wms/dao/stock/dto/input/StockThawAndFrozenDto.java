package org.nodes.wms.dao.stock.dto.input;

import lombok.Data;

import java.util.List;

/**
 * 库存解冻和冻结dto
 */
@Data
public class StockThawAndFrozenDto {
	/**
	 * 生成批次号集合
	 */
	List<String> skuLot1List;
	/**
	 * 箱码集合
	 */
	List<String> boxCodeList;
	/**
	 * 库位编码集合
	 */
	List<String> locIdList;
	/**
	 * 备注
	 */
	String remark;
}

