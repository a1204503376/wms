package org.nodes.wms.dao.outstock.so.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.NotSoPickPageQuery;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.NotSoPickExcelResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.NotSoPickPageResponse;
import org.nodes.wms.dao.outstock.so.dto.input.SoDetailAndStockRequest;
import org.nodes.wms.dao.outstock.so.dto.output.LineNoAndSkuSelectResponse;
import org.nodes.wms.dao.outstock.so.dto.output.PickByPcSoDetailResponse;
import org.nodes.wms.dao.outstock.so.dto.output.SoDetailEditResponse;
import org.nodes.wms.dao.outstock.so.dto.output.SoDetailForDetailResponse;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 收货单头表Mapper接口
 */
@Repository("soDetailRepository")
public interface SoDetailMapper extends BaseMapper<SoDetail> {

	/**
	 * 获取编辑时发货单明细信息
	 *
	 * @param soBillId: 发货单id
	 * @return List<SoDetailEditResponse> 发货单明细信息
	 */
	List<SoDetailEditResponse> selectSoDetailEditBySoBillId(Long soBillId);

	/**
	 * 查看明细：根据发货单id分页查询发货单明细信息
	 *
	 * @param soBillId: 发货单id
	 * @param page:     分页参数
	 * @return Page<SoDetailForDetailResponse> 发货单明细分页信息
	 */
	Page<SoDetailForDetailResponse> pageForSoDetailBySoBillId(IPage<?> page, @Param("soBillId") Long soBillId);

	/**
	 * 分页查询未发货记录
	 *
	 * @param page:               分页参数
	 * @param notSoPickPageQuery: 分页查询条件
	 * @return Page<NotSoPickPageResponse> 未发货记录分页对象
	 */
	Page<NotSoPickPageResponse> pageNotSoPick(IPage<Object> page, @Param("param") NotSoPickPageQuery notSoPickPageQuery);

	/**
	 * 根据 Query 中的条件查询未发货记录
	 *
	 * @param notSoPickPageQuery: 查询条件
	 * @return List<NotSoPickExcelResponse> 未发货记录
	 */
	List<NotSoPickExcelResponse> notSoPickListByQuery(@Param("param") NotSoPickPageQuery notSoPickPageQuery);

	/**
	 * 根据发货单id获取行号和物料编码集合
	 *
	 * @param soBillId 发货 单id
	 * @return 行号和物料编码集合
	 */
	List<LineNoAndSkuSelectResponse> selectLineNoAndSkuCodeById(@Param("soBillId") Long soBillId);

	/**
	 * 根据发货单id和行号获取明细数据
	 *
	 * @param soDetailAndStockRequest 包含发货单id和行号
	 * @return 明细数据
	 */
	PickByPcSoDetailResponse getPickByPcDetail(@Param("param") SoDetailAndStockRequest soDetailAndStockRequest);
}
