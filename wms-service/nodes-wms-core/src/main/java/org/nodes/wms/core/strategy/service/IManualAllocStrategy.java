package org.nodes.wms.core.strategy.service;

import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.strategy.dto.ManualAllocRequestDTO;

import java.util.List;

/**
 * 手动分配策略具体执行方式接口
 * @author wuhongjie
 * @date 2022/2/16 14:08
 */
public interface IManualAllocStrategy {
	List<Stock> execute(ManualAllocRequestDTO manualAllocRequestDTO);
}
