package org.nodes.wms.core.stock.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.stock.core.entity.StockPack;
import org.nodes.wms.core.stock.core.vo.StockPackVO;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 尾箱打包 Mapper 接口
 *
 * @author pengwei
 * @since 2020-07-07
 */
@Primary
public interface StockPackMapper extends BaseMapper<StockPack> {

	/**
	 * 尾箱打包自定义分页
	 *
	 * @param page
	 * @param stockPack
	 * @return
	 */
	List<StockPackVO> selectStockPackPage(IPage page, StockPackVO stockPack);

}
