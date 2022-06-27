package org.nodes.wms.biz.instock;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveDetailLpnPdaResponse;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class InStockBizImpl implements InStockBiz {

	@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public void receiveByBoxCode(ReceiveDetailLpnPdaResponse request) {
		// 判断业务参数（无单收货除外），是否可以正常收货、超收
		// 是否无单收货：判断当前用户是否有无单收货且未关闭的单据，如果有则用这个收货单（如果多个取最后一个）并新建收货单明细
		//             否则新建一个收货单
		// 如果无单收货，还需要更新receive_detail_lpn表中的receive_detail_id
		// 调用库存函数（begin：一条执行一次）
		// 生成清点记录
		// 更新收货单明细状态（end：一条执行一次）

		// 更新收货单（无单收货除外）状态
		// 记录业务日志
	}
}
