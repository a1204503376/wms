package org.nodes.wms.biz.stock.balance;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.instock.receiveLog.ReceiveLogBiz;
import org.nodes.wms.biz.outstock.logSoPick.LogSoPickBiz;
import org.nodes.wms.biz.stock.factory.StockFactory;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.stock.StockBalanceDao;
import org.nodes.wms.dao.stock.entities.StockBalance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceTimerTask {
	private final ReceiveLogBiz receiveLogBiz;
	private final LogSoPickBiz logSoPickBiz;
	private final StockBalanceDao stockBalanceDao;

	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void run() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Calendar calendar1 = (Calendar) calendar.clone();
		//当前时间减去一个月，即一个月前的时间
		calendar1.add(Calendar.MONTH, -1);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime = simpleDateFormat.format(calendar1.getTime());
		String endTime = simpleDateFormat.format(calendar.getTime());
		List<ReceiveLog> receiveLogList = receiveLogBiz.getReceiveLogList(startTime, endTime);
		List<LogSoPick> logSoPickList = logSoPickBiz.getLogSoPickList(startTime, endTime);
		List<StockBalance> stockBalanceList = stockBalanceDao.getStockBalanceList(startTime, endTime);
		List<ReceiveLog> receiveLogList1 = new ArrayList<>();
		List<LogSoPick> logSoPickList1 = new ArrayList<>(logSoPickList);
		List<StockBalance> stockBalanceList1 = new ArrayList<>();
		StockFactory.AssigntoArray(receiveLogList, receiveLogList1, logSoPickList, logSoPickList1, stockBalanceList1);
		List<StockBalance> stockBalanceList2 = StockFactory.createStockBalanceList(stockBalanceList, stockBalanceList1);
		stockBalanceDao.savaStockBalanceBatch(stockBalanceList2);
	}

}
