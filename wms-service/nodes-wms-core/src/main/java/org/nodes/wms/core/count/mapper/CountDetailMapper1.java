
package org.nodes.wms.core.count.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.stock.core.vo.CountSkuQtyVO;
import org.nodes.wms.core.count.entity.CountDetail;
import org.nodes.wms.core.count.vo.CountDetailVO;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Map;

/**
 * 盘点单明细表 Mapper 接口
 *
 * @author NodeX
 * @since 2020-01-02
 */
@Primary
public interface CountDetailMapper1 extends BaseMapper<CountDetail> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param countDetail
	 * @return
	 */
	List<CountDetailVO> selectCountDetailPage(IPage page, CountDetailVO countDetail);

	boolean removeByIdsNew(Long ids);

	/**
	 * 查询正在被占用的库位
	 * @param locIds
	 * @return
	 */
	List<CountDetail> selectOccupyCountDetailByLocCodes(List<Long> locIds);
	/**
	 * 查询正在被占用的物品
	 * @param skuIds
	 * @return
	 */
	List<CountDetail> selectOccupyCountDetailBySkuCodes(List<Long> skuIds);

	List<CountSkuQtyVO> querySkuQty(Map<String,String> map, List<Long> skuIds);
}
