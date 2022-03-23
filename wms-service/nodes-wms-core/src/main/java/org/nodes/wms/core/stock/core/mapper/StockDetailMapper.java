package org.nodes.wms.core.stock.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.nodes.wms.core.stock.core.entity.StockDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.stock.core.vo.StockDetailVo;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 库存明细表 Mapper 接口
 *
 * @author NodeX
 * @since 2021-10-12
 */
@Primary
public interface StockDetailMapper extends BaseMapper<StockDetail> {

	List<StockDetailVo> selectStockDetailsByStockId(IPage page, @Param("stockId") Long stockId);
}
