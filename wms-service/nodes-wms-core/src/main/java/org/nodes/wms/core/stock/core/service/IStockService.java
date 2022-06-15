package org.nodes.wms.core.stock.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.stock.core.dto.*;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.stock.core.entity.StockDetail;
import org.nodes.wms.core.stock.core.vo.AccountsVo;
import org.nodes.wms.core.stock.core.vo.ErpStockVO;
import org.nodes.wms.core.stock.core.vo.StockSubtractVO;
import org.nodes.wms.core.stock.core.vo.StockVO;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.mp.support.Query;

import java.util.List;

/**
 * 库存 服务类
 *
 * @author pengwei
 * @since 2019-12-24
 */
public interface IStockService extends BaseService<Stock> {
	/**
	 * 库存查询
	 *
	 * @param stockQueryDTO 查询条件
	 * @return 库存集合
	 */
	List<Stock> list(StockQueryDTO stockQueryDTO);

	/**
	 * 获取库存-列表
	 *
	 * @param stockDTO 查询条件
	 * @return 库存-列表
	 */
	List<StockVO> selectList(StockDTO stockDTO);

	/**
	 * 获取库存-分页
	 *
	 * @param stockDTO 查询条件
	 * @param query    分页信息
	 * @return 库存-分页
	 */
	IPage<StockVO> selectPage(StockDTO stockDTO, Query query);

	/**
	 * 库存增加
	 *
	 * @param stockAdd 接口参数 StockAddDTO
	 * @return 增加后的库存信息
	 */
	Stock add(StockAddDTO stockAdd);

	/**
	 * 库存减少
	 *
	 * @param stockSubtract 接口参数 StockSubtractDTO
	 * @return 库存扣减返回值集合
	 */
	List<StockSubtractVO> subtract(StockSubtractDTO stockSubtract);

	/**
	 * 库存锁定
	 *
	 * @param stockIdList 库存ID集合
	 * @return 是否成功
	 */
	boolean lock(List<Long> stockIdList);

	/**
	 * 库存解锁
	 *
	 * @param stockIdList 库存id集合
	 * @return 是否成功
	 */
	boolean unlock(List<Long> stockIdList);

	boolean lockByStock(List<Long> stockIdList, String lockFlag);

	/**
	 * 按批次 锁定/解锁
	 *
	 * @param stockIdList 库存ID集合
	 * @return 是否成功
	 */
	boolean lockByLot(List<Long> stockIdList);

	List<Stock> selectStockByLoc(List<Long> locIds, Integer abc, List<Long> skuIds);

	/**
	 * 库存移动（按托）
	 */
	void stockMoveByLpn(StockMoveDTO stockMove);

	/**
	 * 库存移动（按件）
	 *
	 * @param stockMove 接口参数 StockMoveDTO
	 * @return 移动后得库存信息
	 */
	List<Stock> stockMove(StockMoveDTO stockMove);

	/**
	 * 获取库存的查询构造器
	 *
	 * @param stock 查询条件
	 * @return 查询构造器
	 */
	QueryWrapper<Stock> getQueryWrapper(StockDTO stock);

	List<AccountsVo> getAccountsWithAsn(List<Long> skuIds, String startTime, String endTime);

	List<AccountsVo> getAccountsWithPick(List<Long> skuIds, String startTime, String endTime);

	/**
	 * 获取wms库存与erp库存比对结果
	 *
	 * @return
	 */
	List<ErpStockVO> listErpCompare();

	List<StockSubtractVO> stockSubtract(Sku sku, StockSubtractDTO stockReduce);

	List<Stock> getStockListBySkuAndLoc(Long skuId,String locCode);

	List<StockDetail> getStockListBylpnAndbox(Long skuId, String locCode,String lpnCode,String boxCode);

}
