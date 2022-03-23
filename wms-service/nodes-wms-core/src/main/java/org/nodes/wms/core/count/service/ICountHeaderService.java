
package org.nodes.wms.core.count.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.count.entity.CountHeader;
import org.nodes.wms.core.count.enums.StockCountStateEnum;
import org.nodes.wms.core.count.vo.CountHeaderVO;
import org.nodes.wms.core.count.vo.CountRecordVO;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 盘点单头表 服务类
 *
 * @author NodeX
 * @since 2020-01-02
 */

public interface ICountHeaderService extends BaseService<CountHeader> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param countHeader
	 * @return
	 */
	IPage<CountHeader> selectCountHeaderPage(IPage<CountHeader> page, CountHeaderVO countHeader);

	/**
	 * 查询库位
	 *
	 * @param countHeader
	 * @return
	 */
	List<CountHeaderVO> selectLocation(CountHeaderVO countHeader);

	/**
	 * 增加盘点信息
	 *
	 * @param countHeader
	 * @return
	 */
	boolean add(CountHeaderVO countHeader);


	/**
	 * 查询详细
	 *
	 * @param countHeader
	 * @return
	 */
	CountHeaderVO getDetail(CountHeader countHeader);

	/**
	 * 查询详细
	 *
	 * @param countHeader
	 * @return
	 */
	CountHeaderVO getDetailPda(CountHeader countHeader);

	/**
	 * 获取盘点单单号
	 *
	 * @param
	 * @return
	 */
	String getCNo();

	/**
	 * 删除
	 *
	 * @param ids
	 * @return
	 */
	boolean removeByIdsNew(String ids);

	/**
	 * 分配任务
	 *
	 * @param countHeaderVO
	 * @return
	 */
	boolean countTask(CountHeaderVO countHeaderVO);

	/**
	 * 完成盘点单
	 *
	 * @param ids
	 * @return
	 */
	boolean completeTask(String ids);

	/**
	 * 完成库位
	 *
	 * @param ids
	 * @return
	 */
	CountHeaderVO completeTaskPda(Long[] ids);

	/**
	 * 生成差异报告
	 *
	 * @param ids
	 * @return
	 */
	boolean differenceReport(String ids);

	/**
	 * 创建差异单据
	 *
	 * @param ids
	 * @return
	 */
	boolean differenceOrder(String ids);

	/**
	 * 生成差异报告
	 *
	 * @param countRecordList
	 * @return
	 */
	boolean randomCountDifferenceReport(List<CountRecordVO> countRecordList);

	/**
	 * 盘点差异报告作废
	 *
	 * @param idList 盘点单主键集合
	 * @return 是否成功
	 */
	boolean reportCancel(List<Long> idList);

	/**
	 * 复盘
	 *
	 * @param ids 需要复盘的盘点单主键ID
	 * @return 新的盘点单单号
	 */
	String repeat(String ids);

	/**
	 * 盘点单批量作废
	 *
	 * @param ids
	 * @return
	 */
	boolean abolishCountHeader(String ids);

	/**
	 * 随机盘点作废
	 *
	 * @param ids
	 * @return
	 */
	boolean abolishRandomCount(String ids);

	/**
	 * 修改盘点单状态
	 *
	 * @param countHeader         盘点单表头
	 * @param stockCountStateEnum 盘点单状态
	 * @return 是否成功
	 */
	boolean updateBillState(CountHeader countHeader, StockCountStateEnum stockCountStateEnum);

	/**
	 * 循环盘点，单据生成
	 *
	 * @param whId       库房ID
	 * @param cycle      周期
	 * @param countBy    盘点方式
	 * @param abc        abc分类
	 * @param searchType 搜索方式
	 * @return
	 */
	boolean cycleCount(String whId, String cycle, String countBy, String abc, String searchType);

	CountHeader getCountHeaderByNo(String billNo);
}
