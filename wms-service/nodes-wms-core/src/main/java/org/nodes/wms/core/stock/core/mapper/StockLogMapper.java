
package org.nodes.wms.core.stock.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.stock.core.entity.StockLog;
import org.nodes.wms.core.stock.core.vo.LocRateRltVO;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 库存日志 Mapper 接口
 *
 * @author pengwei
 * @since 2020-02-14
 */
@Primary
public interface StockLogMapper extends BaseMapper<StockLog> {

	/**
	 * 查询指定时间内库存物品数量
	 *
	 * @param begin 开始时间
	 * @param end   结束时间
	 * @return 库存物品数量
	 */
	Integer selectSkuCount(LocalDateTime begin, LocalDateTime end);

	/**
	 * 查询指定时间内库位占用数量
	 *
	 * @param begin 开始时间
	 * @param end   结束时间
	 * @return 库位占用数量
	 */
	Integer selectLocCount(LocalDateTime begin, LocalDateTime end);

	List<LocRateRltVO> selectLocRate(LocalDateTime beginTime, LocalDateTime endTime);
}
