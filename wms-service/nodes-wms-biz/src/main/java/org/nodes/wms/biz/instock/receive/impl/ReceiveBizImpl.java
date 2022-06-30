package org.nodes.wms.biz.instock.receive.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.instock.receive.ReceiveBiz;
import org.nodes.wms.biz.instock.receive.modular.ReceiveFactory;
import org.nodes.wms.biz.stock.StockBiz;
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
import java.util.stream.Collectors;

/**
 * 收货单管理业务类
 */
@Service
@RequiredArgsConstructor
public class ReceiveBizImpl implements ReceiveBiz {
	private final ReceiveHeaderDao receiveHeaderDao;
	private final ReceiveDetailDao receiveDetailDao;
	private final SkuDao skuDao;
	private final ReceiveFactory receiveFactory;

	private final LogBiz logBiz;
	private final ReceiveDetailLpnDao receiveDetailLpnDao;
	private final StockBiz stockBiz;


	@Override
	public IPage<ReceiveHeaderResponse> getPage(ReceivePageQuery receivePageQuery, Query query) {
		IPage<ReceiveHeaderResponse> page = Condition.getPage(query);
		IPage<ReceiveHeaderResponse> responsePage = receiveHeaderDao.selectPage(page, receivePageQuery);
		List<ReceiveHeaderResponse> receiveHeaderResponseList = responsePage.getRecords();

		for (ReceiveHeaderResponse receiveHeaderResponse : receiveHeaderResponseList) {
			ReceiveHeaderStateEnum billState = receiveHeaderResponse.getBillState();
			String billStateDesc = billState.getDesc();
			receiveHeaderResponse.setBillStateDesc(billStateDesc);
		}

		responsePage.setRecords(receiveHeaderResponseList);
		return responsePage;
	}


	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public boolean remove(List<Long> receiveIdList) {
		for (Long receiveId : receiveIdList) {
			ReceiveHeader receiveHeader = receiveHeaderDao.selectBillStateById(receiveId);
			if (receiveHeader.getBillState().getCode() != 10) {
				throw new ServiceException("删除失败,单号：" + receiveHeader.getReceiveNo() + "已进行收货");
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
		//创建保存实体类
		ReceiveHeader receiveHeader = receiveFactory.createReceiveHeader(newReceiveRequest.getNewReceiveHeaderRequest());
		receiveHeaderDao.insert(receiveHeader);
		logBiz.auditLog(AuditLogType.RECEIVE_BILL, receiveHeader.getReceiveId(), "新建收货单");
		//获取明细集合
		List<NewReceiveDetailRequest> newReceiveDetailRequestList = newReceiveRequest.getNewReceiveDetailRequestList();
		//遍历保存
		for (NewReceiveDetailRequest newReceiveDetailRequest : newReceiveDetailRequestList) {
			ReceiveDetail receiveDetail = receiveFactory.createReceiveDetail(newReceiveDetailRequest, receiveHeader);
			receiveDetailDao.insert(receiveDetail);
		}
		return receiveHeader;
	}

	@Override
	public void exportExcel(ReceivePageQuery receivePageQuery, HttpServletResponse response) {
		List<ReceiveHeaderResponse> receiveHeaderResponseList = receiveHeaderDao.getReceiveHeaderResponseByQuery(receivePageQuery);
		for (ReceiveHeaderResponse receiveHeaderResponse : receiveHeaderResponseList) {
			String billState = receiveHeaderResponse.getBillState().getDesc();
			receiveHeaderResponse.setBillStateDesc(billState);
		}
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
		if (receiveHeader.getBillState().getCode() != 10) {
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
			ReceiveDetail receiveDetail = receiveFactory.createEditReceiveDetail(editReceiveDetailRequest, receiveHeader);
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
		response.setWsuCode(detail.getUmCode());
		Sku sku = skuDao.getById(detail.getSkuId());
		if (response != null) {
			response.setIsSn(false);
			if (Func.isNotEmpty(sku)) {
				if (sku.getIsSn() == BigDecimal.ZERO.intValue()) {
					response.setIsSn(false);
				}
			}
		}

		return response;
	}

	@Override
	public ReceiveDetailLpnPdaResponse getReceiveDetailLpnByBoxCode(String boxCode) {
		List<Stock> stockList = stockBiz.findStockByBoxCode(boxCode);
		if(Func.isNotEmpty(stockList)){
			throw new ServiceException("收货失败,该箱码已在库存中存在");
		}
		//根据箱码获取lpn实体集合
		List<ReceiveDetailLpn> receiveDetailLpnList = receiveDetailLpnDao.getReceiveDetailLpnListByBoxCode(boxCode);
		if (Func.isEmpty(receiveDetailLpnList)) {
			throw new ServiceException("没有搜索到该箱码");
		}
		ReceiveDetailLpnPdaResponse receiveDetailLpnPdaResponse = new ReceiveDetailLpnPdaResponse();
		BigDecimal i = new BigDecimal(0);
		int a = 1;
		List<ReceiveDetailLpnItemDto> receiveDetailLpnItemDtoList = new ArrayList<>();
		ReceiveDetailLpn receiveDetailLpn = receiveDetailLpnList.get(0);
		receiveDetailLpnPdaResponse.setLpnCode(receiveDetailLpn.getLpnCode());
		receiveDetailLpnPdaResponse.setBoxCode(receiveDetailLpn.getBoxCode());
		receiveDetailLpnPdaResponse.setSkuLot1(receiveDetailLpn.getSkuLot1());
		for (ReceiveDetailLpn item : receiveDetailLpnList) {
			//添加sku的dto到集合中
			ReceiveDetailLpnItemDto receiveDetailLpnItemDto = new ReceiveDetailLpnItemDto();
			//设置型号
			if (Func.isNotEmpty(item.getSkuSpec()) && a == 1) {
				receiveDetailLpnPdaResponse.setSkuLot2(item.getSkuSpec());
				a = 0;
			}
			receiveDetailLpnItemDto.setSkuCode(item.getSkuCode());
			receiveDetailLpnItemDto.setSkuName(item.getSkuName());
			receiveDetailLpnItemDto.setPlanQty(item.getPlanQty());
			receiveDetailLpnItemDto.setReceiveDetailId(item.getReceiveDetailId());
			receiveDetailLpnItemDto.setSkuId(item.getSkuId());
			receiveDetailLpnItemDto.setReceiveDetailLpnId(item.getId());
			receiveDetailLpnItemDtoList.add(receiveDetailLpnItemDto);
			//设置总数
			i = i.add(item.getPlanQty());
		}
		//设置sku的dto集合
		receiveDetailLpnPdaResponse.setReceiveDetailLpnItemDtoList(receiveDetailLpnItemDtoList);
		//设置总数
		receiveDetailLpnPdaResponse.setNum(i);
		return receiveDetailLpnPdaResponse;
	}

	@Override
	public void canReceive(Long receiveDetailId, BigDecimal receiveQty) {
		if (Func.isEmpty(receiveQty)) {
			throw new ServiceException("请输入数量");
		}
		ReceiveDetail detail = receiveDetailDao.getDetailByReceiveDetailId(receiveDetailId);
		ReceiveHeader receiveHeader = receiveHeaderDao.selectReceiveHeaderById(detail.getReceiveId());
		if (detail.getDetailStatus() == ReceiveDetailStatusEnum.NOT_RECEIPT || detail.getDetailStatus() == ReceiveDetailStatusEnum.PART) {
			if (receiveHeader.getBillState() == ReceiveHeaderStateEnum.NOT_RECEIPT || receiveHeader.getBillState() == ReceiveHeaderStateEnum.PART) {
				int isExit = detail.getPlanQty().compareTo(detail.getScanQty());
				if (isExit == BigDecimal.ZERO.intValue()) {
					throw new ServiceException("该单不可以收货，原因无可收的货物");
				} else {
					BigDecimal sumScanQty = detail.getScanQty().add(receiveQty);
					int compareTo = detail.getPlanQty().compareTo(sumScanQty);
					//如果数量不超过计划数量就复制给实收数量
					if (compareTo < BigDecimal.ZERO.intValue()) {
						throw new ServiceException("不能超收");
					}
				}
			} else {
				throw new ServiceException("该单不可以收货，原因收货单已经收货完成");
			}
		} else {
			throw new ServiceException("该单不可以收货，原因收货单明细已经收货完成");
		}
	}

	@Override
	public void updateReceiveDetail(Long receiveDetailId, BigDecimal receiveQty) {
		ReceiveDetail detail = receiveDetailDao.getDetailByReceiveDetailId(receiveDetailId);
		BigDecimal sumScanQty = detail.getScanQty().add(receiveQty);
		int compareTo = detail.getPlanQty().compareTo(sumScanQty);
		//如果数量不超过计划数量就复制给实收数量
		if (compareTo >= 0) {
			detail.setScanQty(sumScanQty);
			detail.setDetailStatus(ReceiveDetailStatusEnum.PART);
			if (compareTo == BigDecimal.ZERO.intValue()) {
				detail.setDetailStatus(ReceiveDetailStatusEnum.COMPLETED);
			}
		}
		BigDecimal surplusQty = detail.getPlanQty().subtract(sumScanQty);
		detail.setSurplusQty(surplusQty);
		receiveDetailDao.updateReceiveDetail(detail);
	}

	@Override
	public void updateReciveHeader(Long receiveDetailId) {
		ReceiveDetail detail = receiveDetailDao.getDetailByReceiveDetailId(receiveDetailId);
		List<ReceiveDetail> details = receiveDetailDao.selectReceiveDetailById(detail.getReceiveId());
		ReceiveHeader receiveHeader = receiveHeaderDao.selectReceiveHeaderById(detail.getReceiveId());
		List<ReceiveDetail> collect = details.stream().filter(item -> item.getDetailStatus().getCode().equals(ReceiveHeaderStateEnum.COMPLETED.getCode())).collect(Collectors.toList());
		if (details.size() == collect.size()) {
			receiveHeader.setBillState(ReceiveHeaderStateEnum.COMPLETED);
		} else {
			receiveHeader.setBillState(ReceiveHeaderStateEnum.PART);
		}
		receiveHeaderDao.updateReceiveHeader(receiveHeader);
	}

	@Override
	public void log(Long receiveHeaderId, Long receiveDetailId,
					BigDecimal qty, String skuLotNumber) {
		ReceiveHeader receiveHeader = receiveHeaderDao.selectReceiveHeaderById(receiveHeaderId);
		ReceiveDetail detail = receiveDetailDao.getDetailByReceiveDetailId(receiveDetailId);
		logBiz.auditLog(AuditLogType.INSTOCK, receiveHeaderId, receiveHeader.getReceiveNo(), String.format("%s收货%s,批次%s", detail.getLineNo(), qty, skuLotNumber));
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
		return receiveHeaderDao.pageNotReceiveDetail(
			Condition.getPage(query),
			notReceiveDetailPageQuery,
			ReceiveDetailStatusEnum.NOT_RECEIPT.getCode()
		);
	}

	@Override
	public void exportNotReceiveDetail(
		NotReceiveDetailPageQuery notReceiveDetailPageQuery, HttpServletResponse response) {
		List<NotReceiveDetailExcelResponse> notReceiveDetailList = receiveHeaderDao.getNotReceiveDetailListByQuery(
			notReceiveDetailPageQuery,ReceiveDetailStatusEnum.NOT_RECEIPT.getCode());
		ExcelUtil.export(
			response,
			"未收货明细",
			"未收货明细",
			notReceiveDetailList,NotReceiveDetailExcelResponse.class );
	}
}
