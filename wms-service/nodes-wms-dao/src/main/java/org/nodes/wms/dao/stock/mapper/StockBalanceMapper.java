package org.nodes.wms.dao.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.stock.entities.StockBalance;

import java.util.List;

/**
 * 收发存库存mapper接口
 **/
public interface StockBalanceMapper extends BaseMapper<StockBalance> {
	/**
	 * 根据时间范围获取收发存库存集合
	 *
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return 收发存库存集合
	 */
	List<StockBalance> selectStockBalanceList(@Param("startTime") String startTime, @Param("endTime") String endTime);

	void deleteByData(String dataTime);
}

