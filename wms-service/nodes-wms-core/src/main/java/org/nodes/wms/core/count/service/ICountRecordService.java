
package org.nodes.wms.core.count.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.count.dto.RandomCheckSubmitDTO;
import org.nodes.wms.core.count.dto.RandomCountDTO;
import org.nodes.wms.core.count.entity.CountRecord;
import org.nodes.wms.core.count.vo.CountRecordVO;
import org.nodes.wms.core.count.vo.RandomCountRltVO;
import org.springblade.core.mp.base.BaseService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 盘点单记录表 服务类
 *
 * @author chz
 * @since 2020-02-20
 */
public interface ICountRecordService extends BaseService<CountRecord> {

	/**
	 * 随机盘点 提交
	 * @param randomCountDTO 随机盘点参数
	 * @return 随机盘点返回结果 RandomCountRltVO
	 */
	RandomCountRltVO randomCountSubmit(RandomCountDTO randomCountDTO);
	/**
	 * 欣天新随机盘点 提交
	 * @param randomCheckSubmitVo 随机盘点参数
	 * @return 随机盘点返回结果 RandomCountRltVO
	 */
	boolean randomCheckSubmit(RandomCheckSubmitDTO randomCheckSubmitVo);
	/**
	 * 欣天新盘点任务 提交
	 * @param randomCheckSubmitVo 随机盘点参数
	 */
	boolean randomCheckTaskSubmit(RandomCheckSubmitDTO randomCheckSubmitVo);
	/**
	 * 单据盘点 提交
	 * @param randomCountDTO 单据盘点参数
	 * @return 单机盘点返回结果 RandomCountRltVO
	 */

	RandomCountRltVO billCountSubmit (RandomCountDTO randomCountDTO);

	/**
	 * 查询盘点记录数据	 type=1查询正常盘点数据，type=2查询随机盘点数据，type=3查询所有数据*
	 * @param countRecordVO
	 * @param type
	 * @return
	 */
	List<CountRecord> listAll(CountRecordVO countRecordVO,int type);

    void exportCountLogs(IPage<CountRecordVO> countRecordVOIPage, HttpServletResponse response);
}
