
package org.nodes.wms.core.stock.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.entity.StockDetail;
import org.nodes.wms.core.stock.core.vo.AccountsVo;
import org.nodes.wms.core.stock.core.vo.UnsafeStockSkuVO;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 库存 Mapper 接口
 *
 * @author pengwei
 * @since 2019-12-24
 */
@Primary
public interface StockMapper extends BaseMapper<Stock> {
	List<Stock> selectStockByLoc(Integer abc,List<Long> locIds,List<Long> skuIds);
	List<AccountsVo> getAccountsWithAsn(List<Long> skuIds, String startTime, String endTime);

	List<AccountsVo> getAccountsWithPick(List<Long> skuIds,String startTime,String endTime);


	List<UnsafeStockSkuVO> selectUnsafeStock();

	List<Stock> getStockListBySkuAndLoc(Long skuId,String locCode);

	List<StockDetail> getStockListBylpnAndbox(Long skuId, String locCode,String lpnCode,String boxCode);
}
