package org.nodes.wms.core.instock.asn.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.core.instock.asn.dto.AsnDTO;
import org.nodes.wms.core.instock.asn.dto.AsnHeaderDTO;
import org.nodes.wms.core.instock.asn.dto.AsnHeaderOrderDto;
import org.nodes.wms.core.instock.asn.entity.AsnHeader;
import org.nodes.wms.core.instock.asn.excel.AsnHeaderExcel;
import org.nodes.wms.core.instock.asn.excel.SnExcel;
import org.nodes.wms.core.instock.asn.vo.*;
import org.nodes.wms.core.strategy.vo.InstockExecuteVO;
import org.nodes.wms.dao.instock.asn.enums.AsnBillStateEnum;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收货单头表 服务类
 *
 * @author zx
 * @since 2019-12-12
 */
public interface IAsnHeaderService extends BaseService<AsnHeader> {

	AsnHeaderVO getDetail(Long asnBillId);

	List<AsnHeaderVO> selectList(AsnHeaderDTO asnHeader);

	IPage<AsnHeaderVO> selectPage(AsnHeaderDTO asnHeader, Query query);

	/**
	 * 获得上架信息
	 *
	 * @param inStockSubmitVO
	 * @return
	 */
	InstockExecuteVO queryStockByLpnCode(InStockSubmitVO inStockSubmitVO);

	/**
	 * 修改入库单状态
	 *
	 * @param asnBillId    入库单ID
	 * @param asnBillState 订单状态
	 * @return 当前入库单
	 */
	AsnHeader updateAsnBillState(Long asnBillId, AsnBillStateEnum asnBillState);

	/**
	 * 关闭收货单
	 *
	 * @param ids
	 * @return
	 */
	boolean finishAsnBill(String ids);

	/**
	 * 保存收货单头表及其明细表
	 *
	 * @param asnHeaderDTO
	 * @return
	 */
	boolean saveOrUpdate(AsnHeaderDTO asnHeaderDTO);

	/**
	 * 保存收货单头表及其明细表来自调拨
	 *
	 * @param asnHeaderDTO
	 * @return
	 */
	boolean saveOrUpdateByAllot(AsnHeaderDTO asnHeaderDTO);

	/**
	 * 获取入库单是否允许编辑
	 *
	 * @param asnHeaderId 入库单ID
	 * @return true:允许编辑， false:不允许编辑
	 */
	boolean canEdit(Long asnHeaderId);

	/**
	 * 批量取消订单
	 *
	 * @param asnBillIdList 入库单ID集合
	 * @return 是否成功
	 */
	boolean cancel(List<Long> asnBillIdList);

	/**
	 * 查询列表
	 *
	 * @param asnHeader
	 * @return
	 */
	List<AsnHeader> listForPDA(AsnHeader asnHeader);

	/**
	 * 获得完成的明细表
	 *
	 * @param asnBillId
	 * @return
	 */
	AsnDetailMinVO getFinishAsnDetail(Long asnBillId);

	/**
	 * 获得详情
	 *
	 * @param asnBillNo
	 * @param skuCode
	 * @return
	 */
	Map<String, Object> getAsnHeaderDetail(String asnBillNo, String skuCode);

	/**
	 * 按件收货 获取物品列表
	 *
	 * @param asnBillNo
	 * @param skuCode
	 * @return
	 */
	List<AsnSkuVO> getSkuListForInstock(String asnBillNo, String skuCode);

	/**
	 * 序列号是否属于收货单
	 *
	 * @param dto
	 * @return
	 */
	boolean instockHasSerial(AsnDTO dto);

	/**
	 * 提交上架信息
	 *
	 * @param inStockSubmitVO
	 * @return
	 */
	boolean submitPutaway(InStockSubmitVO inStockSubmitVO);

	/**
	 * 提交入库
	 *
	 * @param dto
	 * @return
	 */
	SubmitAsnHeaderVO submitAsnHeader(AsnDTO dto);

	/**
	 * 按托移动
	 *
	 * @param inStockSubmitVO
	 * @return
	 */
	boolean submitMove(InStockSubmitVO inStockSubmitVO);

	/**
	 * 获取收货订单信息
	 *
	 * @param asnBillNo
	 * @return
	 */
	AsnHeaderVO getAsnHeaderAndDetails(String asnBillNo);

	boolean submitAsnHeaderWithOrder(AsnHeaderOrderDto asnHeaderOrderDto);

	/**
	 * 验证Excel数据
	 *
	 * @param dataList 待验证的数据
	 */
	List<DataVerify> validSnExcel(List<SnExcel> dataList);

	/**
	 * 导入Excel数据
	 *
	 * @param dataVerifyList 待导入验证通过的数据
	 */
	boolean importSnData(List<DataVerify> dataVerifyList);

	void exportAsnExcel(HashMap<String, Object> params, HttpServletResponse response);

	List<DataVerify> validAsnExcel(List<AsnHeaderExcel> read);

	boolean importAsnData(List<DataVerify> dataVerifyList);

	/**
	 * 提交入库
	 *
	 * @param dto
	 * @return
	 */
	void submitAsnTray(AsnDTO dto);

	boolean submitPutawayNew(PutawayByTranSubmitVO putawayByTranSubmitVO);
}
