package org.nodes.wms.core.outstock.sales.service;

import org.nodes.wms.core.outstock.sales.dto.SalesHeaderDTO;
import org.nodes.wms.core.outstock.sales.dto.SalesHeaderQueryDTO;
import org.nodes.wms.core.outstock.sales.entity.SalesHeader;
import org.nodes.wms.core.outstock.sales.vo.SalesHeaderVO;
import org.springblade.core.mp.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.support.Query;

import java.util.List;

/**
 * 销售单主表
 * 服务类
 *
 * @author NodeX
 * @since 2021-05-31
 */
public interface ISalesHeaderService extends BaseService<SalesHeader> {
	/**
	 * 获取销售单-列表
	 *
	 * @param salesHeaderQuery 查询条件
	 * @return 销售单-列表
	 */
	List<SalesHeaderVO> selectList(SalesHeaderQueryDTO salesHeaderQuery);

	/**
	 * 获取销售单-分页
	 *
	 * @param salesHeaderQuery 查询条件
	 * @param query            分页信息
	 * @return 销售单-分页
	 */
	IPage<SalesHeaderVO> selectPage(SalesHeaderQueryDTO salesHeaderQuery, Query query);

	/**
	 * 保存或修改 销售订单
	 *
	 * @param salesHeader 销售订单DTO
	 * @return 是否成功
	 */
	boolean saveOrUpdate(SalesHeaderDTO salesHeader);

	/**
	 * 销售订单-详细信息
	 *
	 * @param soBillId 销售订单ID
	 * @return 销售订单-详细信息
	 */
	SalesHeaderVO getDetail(Long soBillId);

	/**
	 * 创建出库单
	 *
	 * @param salesHeader 销售单
	 * @return 是否成功
	 */
	boolean createSo(SalesHeaderDTO salesHeader);
}
