package org.nodes.wms.biz.stock.balance;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.instock.receiveLog.ReceiveLogBiz;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class BalanceTimmerTask {
	private final ReceiveLogBiz receiveLogBiz;

	public void run() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Calendar calendar1 = (Calendar) calendar.clone();
		//当前时间减去一个月，即一个月前的时间
		calendar1.add(Calendar.MONTH, -1);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime = simpleDateFormat.format(calendar.getTime());
		String endTime = simpleDateFormat.format(calendar1.getTime());
		receiveLogBiz.getReceiveLogList(startTime, endTime);
	}

}
