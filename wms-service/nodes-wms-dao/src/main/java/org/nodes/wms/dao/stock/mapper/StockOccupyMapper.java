package org.nodes.wms.dao.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.stock.entities.StockOccupy;
import org.springframework.stereotype.Repository;

/**
 * 库存占用mapper接口
 **/
@Repository("stockOccupyRepository")
public interface StockOccupyMapper extends BaseMapper<StockOccupy> {

}
