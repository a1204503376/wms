package org.nodes.wms.biz.stock;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.nodes.wms.dao.stock.StockDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 定时任务
 *
 * @author nodesc
 */
@Service
@RequiredArgsConstructor
public class DeleteStockByPeriods {
	private final StockQueryBiz stockQueryBiz;
	private final StockDao stockDao;
	private final LogBiz logBiz;

	/**
	 * 清理无效库存
	 */
	@Transactional(rollbackFor = Exception.class)
	public void clearInvalidStock() {
		stockDao.deleteStockByStockIdList();
		logBiz.auditLog(AuditLogType.CRON_TASK, "定时任务定时执行删除无效库存完成");
	}
}
