package org.nodes.wms.biz.stockManage.module;

import lombok.Data;
import org.nodes.wms.dao.stock.entities.Stock;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DevanningAction {
	private Stock stock;

	private BigDecimal qty;

	private List<String> serialNoList;
}
