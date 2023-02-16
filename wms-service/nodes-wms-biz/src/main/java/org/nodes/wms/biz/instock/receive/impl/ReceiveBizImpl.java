package org.nodes.wms.biz.instock.receive.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsAppConstant;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.instock.receive.ReceiveBiz;
import org.nodes.wms.biz.instock.receive.modular.ReceiveFactory;
import org.nodes.wms.biz.lendreturn.LendReturnBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.basics.sku.SkuDao;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.common.log.dto.output.LogReceiveResponse;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.nodes.wms.dao.instock.receive.ReceiveDetailDao;
import org.nodes.wms.dao.instock.receive.ReceiveDetailLpnDao;
import org.nodes.wms.dao.instock.receive.ReceiveHeaderDao;
import org.nodes.wms.dao.instock.receive.dto.input.*;
import org.nodes.wms.dao.instock.receive.dto.output.*;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetail;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetailLpn;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.nodes.wms.dao.instock.receive.enums.ReceiveDetailStatusEnum;
import org.nodes.wms.dao.instock.receive.enums.ReceiveHeaderStateEnum;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.stock.dto.output.SerialSelectResponse;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 收货单管理业务类
 *
 * @author nodesc
 */
@Service
@RequiredArgsConstructor
public class ReceiveBizImpl implements ReceiveBiz {
	private final ReceiveHeaderDao receiveHeaderDao;
	private final ReceiveDetailDao receiveDetailDao;

	private final ReceiveDetailLpnDao receiveDetailLpnDao;
	private final ReceiveFactory receiveFactory;
	private final SkuDao skuDao;
	private final StockQueryBiz stockQueryBiz;
	private final LogBiz logBiz;
	private final LendReturnBiz lendReturnBiz;

	/**
	 * 实收量 == 计划量
	 * 并且，当前状态是部分收货或者未收货时
	 * 将状态变更为：已完成
	 */
	private static boolean validPartNotReceipt(ReceiveDetail detail) {
		System.out.println(BigDecimalUtil.eq(detail.getScanQty(), detail.getPlanQty()));
		return BigDecimalUtil.eq(detail.getScanQty(), detail.getPlanQty())
			&& (ReceiveDetailStatusEnum.PART == detail.getDetailStatus()
			|| ReceiveDetailStatusEnum.NOT_RECEIPT == detail.getDetailStatus());
	}

	/**
	 * 实际量 < 计划量
	 * 并且，当前状态是未收货、部分收货或者已完成时
	 * 将状态变更为：部分收货
	 */
	private static boolean validPartNotReceiptCompleted(ReceiveDetail detail) {
		System.out.println(BigDecimalUtil.le(detail.getScanQty(), detail.getPlanQty()));
		return BigDecimalUtil.le(detail.getScanQty(), detail.getPlanQty())
			&& (ReceiveDetailStatusEnum.PART == detail.getDetailStatus()
			|| ReceiveDetailStatusEnum.NOT_RECEIPT == detail.getDetailStatus()
			|| ReceiveDetailStatusEnum.COMPLETED == detail.getDetailStatus());
	}

	/**
	 * 剩余量 == 计划量
	 * 并且，当前状态是部分收货、已完成时
	 * 将状态变更为：未收货
	 */
	private static boolean validPartCompleted(ReceiveDetail detail) {
		System.out.println(BigDecimalUtil.eq(detail.getSurplusQty(), detail.getPlanQty()));
		return BigDecimalUtil.eq(detail.getSurplusQty(), detail.getPlanQty())
			&& (ReceiveDetailStatusEnum.PART == detail.getDetailStatus()
			|| ReceiveDetailStatusEnum.COMPLETED == detail.getDetailStatus());
	}

	@Override
	public IPage<ReceiveHeaderResponse> getPage(ReceivePageQuery receivePageQuery, Query query) {
		IPage<ReceiveHeaderResponse> page = Condition.getPage(query);
		return receiveHeaderDao.selectPage(page, receivePageQuery);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public boolean remove(List<Long> receiveIdList) {
		for (Long receiveId : receiveIdList) {
			ReceiveHeader receiveHeader = receiveHeaderDao.selectBillStateById(receiveId);
			if (receiveHeader.getBillState() != ReceiveHeaderStateEnum.NOT_RECEIPT) {
				throw new ServiceException(Func.format("当前状态不等于：{},不允许删除；单号：{}", ReceiveHeaderStateEnum.NOT_RECEIPT.getDesc(), receiveHeader.getReceiveNo()));
			}
			//获取关联明细集合
			List<Long> receiveDetailIdList = receiveDetailDao.selectDetailIdByReceiveId(receiveId);
			//删除明细
			receiveDetailDao.delete(receiveDetailIdList);
			logBiz.auditLog(AuditLogType.RECEIVE_BILL, receiveId, "删除收货单");
		}
		//删除头表
		return receiveHeaderDao.delete(receiveIdList);
	}

	@Override
	public ReceiveResponse getReceiveDetail(Long receiveId) {
		//查询收货单头表
		DetailReceiveHeaderResponse detailReceiveHeaderResponse = receiveHeaderDao.selectHeaderById(receiveId);
		if (Func.isNull(detailReceiveHeaderResponse)) {
			throw new ServiceException("查看明细失败，改收货单已不存在");
		}
		//设置单据状态描述
		detailReceiveHeaderResponse.setBillStateDesc(detailReceiveHeaderResponse.getBillState().getDesc());
		//设置入库方式描述
		detailReceiveHeaderResponse.setInStoreTypeDesc(detailReceiveHeaderResponse.getInStoreType().getDesc());
		//查询收货单头表关联明细
		List<DetailReceiveDetailResponse> detailList = receiveDetailDao.selectDetailById(receiveId);
		//拼接成一个返回对象
		ReceiveResponse receiveResponse = new ReceiveResponse();
		receiveResponse.setReceiveHeaderResponse(detailReceiveHeaderResponse);
		receiveResponse.setDetailList(detailList);
		return receiveResponse;
	}

	@Override
	public boolean editBillState(Long receiveId) {
		receiveHeaderDao.updateBillStateById(receiveId);
		ReceiveHeader receiveHeader = receiveHeaderDao.selectReceiveHeaderById(receiveId);
		lendReturnBiz.saveReturnLog(receiveHeader);
		logBiz.auditLog(AuditLogType.RECEIVE_BILL, receiveId, "关闭收货单");
		return true;
	}

	@Override
	public List<ReceiveHeader> getReceiveListByNonOrder(Long userId) {
		return receiveHeaderDao.selectReceiveListByNonOrder(userId);
	}

	@Override
	public void newReceiveHeader(ReceiveHeader receiveHeader) {
		receiveHeaderDao.saveReceiveHeader(receiveHeader);
	}

	@Override
	public String getReceiveDetailLinNo(Long receiveId) {
		return receiveDetailDao.selectReceiveDetailLinNo(receiveId);
	}

	@Override
	public ReceiveDetailLpn getReceiveDetailLpnById(Long receiveDetailLpnId) {
		return receiveDetailLpnDao.selectReceiveDetailLpnById(receiveDetailLpnId);
	}

	@Override
	public void newReceiveDetail(ReceiveDetail receiveDetail) {
		receiveDetailDao.saveOrUpdateReceiveDetail(receiveDetail);
	}

	@Override
	public void updateReceiveDetailLpn(ReceiveDetailLpn lpn) {
		receiveDetailLpnDao.updateReceiveDetailLpn(lpn);
	}

	@Override
	public ReceiveHeader getReceiveHeaderById(Long receiveHeaderId) {
		return receiveHeaderDao.selectReceiveHeaderById(receiveHeaderId);
	}

	@Override
	public PdaByPcsReceiveResponse checkByPcsReceive(Long receiveDetailId, Long receiveId) {
		PdaByPcsReceiveResponse response = new PdaByPcsReceiveResponse();
		//查询收货是否完成
		ReceiveDetail detail = receiveDetailDao.getDetailByReceiveDetailId(receiveDetailId);
		response.setCurrentReceivieIsAccomplish(false);
		if (detail.getDetailStatus() == ReceiveDetailStatusEnum.COMPLETED) {
			response.setCurrentReceivieIsAccomplish(true);
		}

		//查询当前全部单据收货是否完成
		ReceiveHeader receiveHeader = receiveHeaderDao.selectReceiveHeaderById(receiveId);
		response.setAllReceivieIsAccomplish(false);
		if (receiveHeader.getBillState() == ReceiveHeaderStateEnum.COMPLETED) {
			response.setAllReceivieIsAccomplish(true);
		}
		return response;
	}

	@Override
	@Transactional
	public ReceiveHeader newReceive(NewReceiveRequest newReceiveRequest) {
		if (Func.isNotEmpty(newReceiveRequest.getNewReceiveDetailRequestList().get(0).getBoxCode())) {
			if (receiveDetailLpnDao.getReceiveDetailLpnListByBoxCode(newReceiveRequest.getNewReceiveDetailRequestList().get(0).getBoxCode()).size() > 0
				|| stockQueryBiz.findEnableStockByBoxCode(newReceiveRequest.getNewReceiveDetailRequestList().get(0).getBoxCode()).size() > 0) {
				throw new ServiceException("创建收货单失败,该箱码已存在库存中或已经在再入库的收货单当中");
			}
		}

		//创建保存实体类
		ReceiveHeader receiveHeader = receiveFactory.createReceiveHeader(newReceiveRequest.getNewReceiveHeaderRequest());
		receiveHeaderDao.insert(receiveHeader);
		logBiz.auditLog(AuditLogType.RECEIVE_BILL, receiveHeader.getReceiveId(), "新建收货单");
		//获取明细集合
		List<NewReceiveDetailRequest> newReceiveDetailRequestList = newReceiveRequest.getNewReceiveDetailRequestList();
		int insertReceiveDetailLpn = 0;
		//遍历保存
		for (NewReceiveDetailRequest newReceiveDetailRequest : newReceiveDetailRequestList) {
			ReceiveDetail receiveDetail = receiveFactory.createReceiveDetail(newReceiveDetailRequest, receiveHeader);
			receiveDetailDao.insert(receiveDetail);
			if (newReceiveRequest.isFromLogSoPickOrLogNoReturn()) {
				ReceiveDetailLpn receiveDetailLpn = receiveFactory.createReceiveDetailLpn(receiveDetail, newReceiveDetailRequest);
				receiveDetailLpnDao.insert(receiveDetailLpn);
			}
		}
		return receiveHeader;
	}

	@Override
	public void exportExcel(ReceivePageQuery receivePageQuery, HttpServletResponse response) {
		List<ReceiveHeaderResponse> receiveHeaderResponseList = receiveHeaderDao.getReceiveHeaderResponseByQuery(receivePageQuery);
		ExcelUtil.export(response, "收货单", "收货单数据表", receiveHeaderResponseList, ReceiveHeaderResponse.class);
	}

	@Override
	public EditReceiveResponse getEditReceiveResponse(Long receiveId) {
		//查询收货单头表
		ReceiveHeader receiveHeader = receiveHeaderDao.selectReceiveHeaderById(receiveId);
		//创建返回编辑页面对象
		EditReceiveHeaderResponse receiveHeaderEditResponse = receiveFactory.createReceiveHeaderEditResponse(receiveHeader);
		//查询收货单头表关联明细
		List<ReceiveDetail> receiveDetailList = receiveDetailDao.selectReceiveDetailById(receiveId);
		//将明细实体转换成dto添加到集合中
		List<EditReceiveDetailResponse> editReceiveDetailResponseList = new ArrayList<>();
		for (ReceiveDetail receiveDetail : receiveDetailList) {
			EditReceiveDetailResponse editReceiveDetailResponse = receiveFactory.createReceiveDetailEditResponse(receiveDetail);
			editReceiveDetailResponseList.add(editReceiveDetailResponse);
		}
		//拼接成一个返回对象
		EditReceiveResponse editReceiveResponse = new EditReceiveResponse();
		editReceiveResponse.setReceiveHeaderResponse(receiveHeaderEditResponse);
		editReceiveResponse.setReceiveDetailResponseList(editReceiveDetailResponseList);
		return editReceiveResponse;
	}

	@Override
	@Transactional
	public String editReceive(EditReceiveRequest editReceiveRequest) {
		//查询收货单头表实体
		ReceiveHeader receiveHeader = receiveHeaderDao.selectReceiveHeaderById(editReceiveRequest.getEditReceiveHeaderRequest().getReceiveId());
		//判断当前收货单实体的收货状态
		if (!ReceiveHeaderStateEnum.NOT_RECEIPT.equals(receiveHeader.getBillState())) {
			throw new ServiceException("编辑失败，该收货单已进行收货");
		}
		String receiveNo = receiveHeader.getReceiveNo();
		//删除收货单明细集合
		if (Func.isNotEmpty(editReceiveRequest.getEditReceiveHeaderRequest().getReceiveDetailIdList())) {
			List<Long> receiveDetailIdList = editReceiveRequest.getEditReceiveHeaderRequest().getReceiveDetailIdList();
			receiveDetailDao.delete(receiveDetailIdList);
		}
		//创建收货单头表保存实体类
		receiveHeader = receiveFactory.createEditReceiveHeader(editReceiveRequest.getEditReceiveHeaderRequest());
		//创建收货单明细保存集合
		List<EditReceiveDetailRequest> editReceiveDetailRequestList = editReceiveRequest.getEditReceiveDetailRequestList();
		for (EditReceiveDetailRequest editReceiveDetailRequest : editReceiveDetailRequestList) {
			ReceiveDetail receiveDetail = receiveFactory.createEditReceiveDetail(editReceiveDetailRequest, receiveHeader, receiveNo);
			receiveDetailDao.saveOrUpdateReceiveDetail(receiveDetail);
		}
		receiveHeaderDao.updateReceive(receiveHeader);
		logBiz.auditLog(AuditLogType.RECEIVE_BILL, receiveHeader.getReceiveId(), receiveHeader.getReceiveNo(), "编辑收货单");
		return receiveNo;
	}

	@Override
	public Page<ReceiveHeaderPdaResponse> getReceiveListByReceiveNo(ReceivePdaQuery receivePdaQuery, Query query) {
		IPage<ReceiveHeader> page = Condition.getPage(query);
		return receiveHeaderDao.getReceiveList(receivePdaQuery, page);
	}

	@Override
	public List<DetailReceiveDetailPdaResponse> getDetailListByReceiveId(ReceiveDetailPdaQuery receiveDetailPdaQuery) {
		return receiveDetailDao.selectDetailListByReceiveId(receiveDetailPdaQuery);
	}

	@Override
	public List<LogReceiveResponse> getLogList(Long receiveId) {
		return logBiz.getLogByReceiveId(receiveId);
	}

	@Override
	public ReceiveDetailByReceiveIdPdaResponse selectDetailByReceiveDetailId(ReceiveDetailByReceiveIdPdaQuery receiveIdPdaQuery) {
		ReceiveDetail detail = receiveDetailDao.getDetailByReceiveDetailId(receiveIdPdaQuery.getReceiveDetailId());
		ReceiveDetailByReceiveIdPdaResponse response = BeanUtil.copy(detail, ReceiveDetailByReceiveIdPdaResponse.class);
		AssertUtil.notNull(response, "收货单明细返回对象为空");

		assert response != null;

		response.setWsuCode(detail.getUmCode());
		response.setIsSn(true);

		Sku sku = skuDao.getById(detail.getSkuId());
		if (Func.isNotEmpty(sku)) {
			if (Objects.equals(sku.getIsSn(), WmsAppConstant.FALSE_DEFAULT)) {
				response.setIsSn(false);
			}
		}
		return response;
	}

	@Override
	public ReceiveDetailLpnPdaResponse getReceiveDetailLpnByBoxCode(String boxCode) {
		List<Stock> stockList = stockQueryBiz.findEnableStockByBoxCode(boxCode);
		if (Func.isNotEmpty(stockList)) {
			throw new ServiceException("收货失败,该箱码已在库存中存在");
		}
		//根据箱码获取lpn实体集合
		List<ReceiveDetailLpn> receiveDetailLpnList = receiveDetailLpnDao.getReceiveDetailLpnListByBoxCode(boxCode);
		if (Func.isEmpty(receiveDetailLpnList)) {
			throw new ServiceException("没有搜索到该箱码");
		}
		ReceiveDetailLpnPdaResponse receiveDetailLpnPdaResponse = new ReceiveDetailLpnPdaResponse();
		if (receiveDetailLpnList.get(0).getReceiveDetailId() != 0) {
			List<Long> DetailIdList = receiveDetailLpnList.stream().map(ReceiveDetailLpn::getReceiveDetailId).collect(Collectors.toList());
			ReceiveDetail receiveDetail = receiveDetailDao.getDetailByReceiveDetailId(DetailIdList.get(0));
			if (Func.isNotEmpty(receiveDetail)) {
				receiveDetailLpnPdaResponse.setSkuLot4(receiveDetail.getSkuLot4());
			}

		} else {
			receiveDetailLpnPdaResponse.setSkuLot4(receiveDetailLpnList.get(0).getSkuLot4());
		}
		BigDecimal totalPlanQty = BigDecimal.ZERO;

		// 是否对批属性2进行过赋值操作
		boolean skuSpecFlag = true;
		List<ReceiveDetailLpnItemDto> receiveDetailLpnItemDtoList = new ArrayList<>();
		ReceiveDetailLpn receiveDetailLpn = receiveDetailLpnList.get(0);
		receiveDetailLpnPdaResponse.setLpnCode(receiveDetailLpn.getLpnCode());
		receiveDetailLpnPdaResponse.setBoxCode(receiveDetailLpn.getBoxCode());
		receiveDetailLpnPdaResponse.setSkuLot1(receiveDetailLpn.getSkuLot1());
		for (ReceiveDetailLpn item : receiveDetailLpnList) {
			//添加sku的dto到集合中
			ReceiveDetailLpnItemDto receiveDetailLpnItemDto = new ReceiveDetailLpnItemDto();
			//设置型号
			if (skuSpecFlag && Func.isNotEmpty(item.getSkuSpec())) {
				receiveDetailLpnPdaResponse.setSkuLot2(item.getSkuSpec());
				skuSpecFlag = false;
			}
			receiveDetailLpnItemDto.setSkuCode(item.getSkuCode());
			receiveDetailLpnItemDto.setSkuName(item.getSkuName());
			receiveDetailLpnItemDto.setPlanQty(item.getPlanQty());
			receiveDetailLpnItemDto.setReceiveDetailId(item.getReceiveDetailId());
			receiveDetailLpnItemDto.setReceiveDetailLpnId(item.getId());
			receiveDetailLpnItemDto.setSkuId(item.getSkuId());
			receiveDetailLpnItemDto.setSkuSpec(item.getSkuSpec());
			receiveDetailLpnItemDto.setSkuLot1(item.getSkuLot1());
			receiveDetailLpnItemDto.setSkuLot1(item.getSkuLot1());
			receiveDetailLpnItemDtoList.add(receiveDetailLpnItemDto);
			//设置总数
			totalPlanQty = totalPlanQty.add(item.getPlanQty());
		}
		//设置sku的dto集合
		receiveDetailLpnPdaResponse.setReceiveDetailLpnItemDtoList(receiveDetailLpnItemDtoList);
		//设置总数
		receiveDetailLpnPdaResponse.setNum(totalPlanQty);
		if (Func.isNotEmpty(receiveDetailLpnList.get(0).getReceiveDetailId())) {

		}
		return receiveDetailLpnPdaResponse;
	}

	@Override
	public void canReceive(ReceiveHeader receiveHeader,
						   ReceiveDetail detail,
						   BigDecimal receiveQty) {
		if (Func.isEmpty(receiveQty) || receiveQty.equals(BigDecimal.ZERO)) {
			throw new ServiceException("请输入数量");
		}
		if (WmsAppConstant.BILL_TYPE_SALE_RETURN.equals(receiveHeader.getBillTypeCd())
			&& Func.isEmpty(receiveHeader.getUdf1())) {
			throw new ServiceException("该单不可以收货，原因销售退回单的文件编码为空");
		}
		if (receiveHeader.getBillState() != ReceiveHeaderStateEnum.NOT_RECEIPT
			&& receiveHeader.getBillState() != ReceiveHeaderStateEnum.PART) {
			throw new ServiceException("该单不可以收货，原因收货单已经收货完成");
		}
		if (receiveHeader.getBillState() == ReceiveHeaderStateEnum.CLOSURE) {
			throw new ServiceException("该单不可以收货，原因收货单已经关闭");
		}
		if (detail.getDetailStatus() != ReceiveDetailStatusEnum.NOT_RECEIPT
			&& detail.getDetailStatus() != ReceiveDetailStatusEnum.PART) {
			throw new ServiceException("该单不可以收货，原因收货单明细已经收货完成");
		}
		if (BigDecimalUtil.eq(detail.getPlanQty(), detail.getScanQty())) {
			throw new ServiceException("该单不可以收货，原因无可收的货物");
		}
		// 当前实际量+本次收货量 > 计划量
		if (BigDecimalUtil.lt(detail.getPlanQty(), detail.getScanQty().add(receiveQty))) {
			throw new ServiceException("不能超收");
		}
	}

	@Override
	public void updateReceiveDetail(ReceiveDetail detail, BigDecimal receiveQty) {
		//给实收数量赋值
		BigDecimal scanQty = detail.getScanQty().add(receiveQty);
		detail.setScanQty(scanQty);
		//给剩余数量赋值
		BigDecimal surplusQty = detail.getPlanQty().subtract(scanQty);
		detail.setSurplusQty(surplusQty);
		ReceiveDetailStatusEnum find = null;
		if (validPartNotReceipt(detail)) {
			find = ReceiveDetailStatusEnum.COMPLETED;
		} else if (validPartCompleted(detail)) {
			find = ReceiveDetailStatusEnum.NOT_RECEIPT;
		} else if (validPartNotReceiptCompleted(detail)) {
			find = ReceiveDetailStatusEnum.PART;
		}
		if (find == null) {
			throw new ServiceException("更新收货单明细表时出现异常");
		}

		detail.setDetailStatus(find);
		receiveDetailDao.updateReceiveDetail(detail);
	}

	@Override
	public void updateReceiveHeader(ReceiveHeader receiveHeader, ReceiveDetail detail) {
		List<ReceiveDetail> details = receiveDetailDao.selectReceiveDetailById(detail.getReceiveId());
		List<ReceiveDetail> completed = details.stream()
			.filter(item -> item.getDetailStatus().getCode().equals(ReceiveHeaderStateEnum.COMPLETED.getCode()))
			.collect(Collectors.toList());
		List<ReceiveDetail> notReceipt = details.stream()
			.filter(item -> item.getDetailStatus().getCode().equals(ReceiveHeaderStateEnum.NOT_RECEIPT.getCode()))
			.collect(Collectors.toList());

		if (details.size() == completed.size()) {
			receiveHeader.setBillState(ReceiveHeaderStateEnum.COMPLETED);
		} else if (details.size() == notReceipt.size()) {
			receiveHeader.setBillState(ReceiveHeaderStateEnum.NOT_RECEIPT);
		} else {
			receiveHeader.setBillState(ReceiveHeaderStateEnum.PART);
		}
		receiveHeaderDao.updateReceiveHeader(receiveHeader);
	}

	@Override
	public void log(String logType, ReceiveHeader receiveHeader,
					ReceiveDetail detail, ReceiveLog receivelog) {
		logBiz.auditLog(AuditLogType.INSTOCK,
			receiveHeader.getReceiveId(),
			receiveHeader.getReceiveNo(),
			String.format("%s:[%s]行[%s]收[%s],批次[%s]",
				logType, detail.getLineNo(), detail.getSkuCode()
				, receivelog.getQty(), receivelog.getSkuLot1()));
	}

	@Override
	public ReceiveDetail getDetailByReceiveDetailId(Long receiveDetailId) {
		return receiveDetailDao.getDetailByReceiveDetailId(receiveDetailId);
	}

	@Override
	public ReceiveHeader selectReceiveHeaderById(Long receiveId) {
		return receiveHeaderDao.selectReceiveHeaderById(receiveId);
	}

	@Override
	public IPage<NotReceiveDetailResponse> pageNotReceiveDetail(
		Query query, NotReceiveDetailPageQuery notReceiveDetailPageQuery) {
		List<Integer> billStateList = new ArrayList<>();
		billStateList.add(ReceiveDetailStatusEnum.NOT_RECEIPT.getCode());
		billStateList.add(ReceiveDetailStatusEnum.PART.getCode());
		return receiveHeaderDao.pageNotReceiveDetail(
			Condition.getPage(query), notReceiveDetailPageQuery, billStateList);
	}

	@Override
	public void exportNotReceiveDetail(
		NotReceiveDetailPageQuery notReceiveDetailPageQuery, HttpServletResponse response) {
		List<Integer> detailStatusList = new ArrayList<>();
		detailStatusList.add(ReceiveDetailStatusEnum.NOT_RECEIPT.getCode());
		detailStatusList.add(ReceiveDetailStatusEnum.PART.getCode());
		List<NotReceiveDetailExcelResponse> notReceiveDetailList = receiveHeaderDao.getNotReceiveDetailListByQuery(
			notReceiveDetailPageQuery, detailStatusList);
		ExcelUtil.export(
			response,
			"未收货明细",
			"未收货明细",
			notReceiveDetailList, NotReceiveDetailExcelResponse.class);
	}

	@Override
	public ReceiveByPcResponse getReceiveByPcResponse(Long receiveId) {
		return receiveHeaderDao.getReceiveByPcResponse(receiveId);
	}

	@Override
	public ReceiveDetailByPcResponse getReceiveDetailByPcResponse(ReceiveByPcQuery receiveByPcQuery) {
		return receiveDetailDao.getReceiveDetailByPcResponse(receiveByPcQuery);
	}

	@Override
	public ReceiveDetailLpn getReceiveDetailLpnByDetailId(Long receiveDetailId) {
		return receiveDetailLpnDao.selectByReceiveDetailId(receiveDetailId);
	}

	@Override
	public boolean updateReceiveDetailLpnForCancelReceive(ReceiveDetailLpn receiveDetailLpn) {
		return receiveDetailLpnDao.updateForCancelReceive(receiveDetailLpn);
	}

	@Override
	public List<SerialSelectResponse> getSerialListByReceiveDetailId(Long receiveDetailId) {
		List<SerialSelectResponse> serialSelectResponseList = new ArrayList<>();
		String snCodes = receiveDetailDao.getDetailByReceiveDetailId(receiveDetailId).getSnCode();
		String[] snCodeArray = snCodes.split(",");
		for (String snCode : snCodeArray) {
			SerialSelectResponse serialSelectResponse = new SerialSelectResponse();
			serialSelectResponse.setSerialNumber(snCode);
			serialSelectResponseList.add(serialSelectResponse);
		}
		return serialSelectResponseList;
	}
}
