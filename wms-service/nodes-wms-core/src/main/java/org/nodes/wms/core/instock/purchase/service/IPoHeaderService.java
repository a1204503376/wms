package org.nodes.wms.core.instock.purchase.service;

import org.nodes.wms.core.instock.purchase.dto.PoHeaderDTO;
import org.nodes.wms.core.instock.purchase.dto.PoHeaderQueryDTO;
import org.nodes.wms.core.instock.purchase.entity.PoHeader;
import org.nodes.wms.core.instock.purchase.vo.PoHeaderVO;
import org.springblade.core.mp.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.support.Query;

import java.util.List;

/**
 * 采购单头表 服务类
 *
 * @author NodeX
 * @since 2021-05-31
 */
public interface IPoHeaderService extends BaseService<PoHeader> {

	/**
	 * 保存或修改 采购订单
	 *
	 * @param poHeader 采购订单DTO
	 * @return 是否成功
	 */
	boolean saveOrUpdate(PoHeaderDTO poHeader);

	/**
	 * 采购订单-列表
	 *
	 * @param poHeader 查询条件
	 * @return 采购订单-列表
	 */
	List<PoHeaderVO> selectList(PoHeaderQueryDTO poHeader);

	/**
	 * 采购订单-分页
	 *
	 * @param poHeader 查询条件
	 * @param query    分页信息
	 * @return 采购订单-分页
	 */
	IPage<PoHeaderVO> selectPage(PoHeaderQueryDTO poHeader, Query query);

	/**
	 * 采购订单-详细信息
	 *
	 * @param poBillId 采购订单ID
	 * @return 采购订单-详细信息
	 */
	PoHeaderVO getDetail(Long poBillId);

	/**
	 * 创建入库单
	 *
	 * @param poHeader 采购单
	 * @return 是否成功
	 */
	boolean createAsn(PoHeaderDTO poHeader);
}
