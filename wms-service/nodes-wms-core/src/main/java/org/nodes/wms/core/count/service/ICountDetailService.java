
package org.nodes.wms.core.count.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.stock.core.vo.CountSkuQtyVO;
import org.nodes.wms.core.count.entity.CountDetail;
import org.nodes.wms.core.count.vo.CountDetailVO;
import org.springblade.core.mp.base.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 盘点单明细表 服务类
 *
 * @author NodeX
 * @since 2020-01-02
 */
public interface ICountDetailService extends BaseService<CountDetail> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param countDetail
	 * @return
	 */
	IPage<CountDetailVO> selectCountDetailPage(IPage<CountDetailVO> page, CountDetailVO countDetail);
	/**
	 * 查询列表
	 *
	 * @param countDetail
	 * @return
	 */
	List<CountDetail> selectCountDetailList(CountDetail countDetail);
	/**
	 * 根据billidno删除详细信息
	 *
	 * @param ids
	 * @return
	 */
	boolean removeByIdsNew(String ids);

	/**
	 * 获取盘点明细集合
	 *
	 * @param countBillId 盘点单表头ID
	 * @return 盘点明细集合
	 */
	List<CountDetail> list(Long countBillId);

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
