package org.nodes.wms.biz.lendreturn.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsAppConstant;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.ExceptionUtil;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.biz.instock.receive.ReceiveBiz;
import org.nodes.wms.biz.instock.receiveLog.ReceiveLogBiz;
import org.nodes.wms.biz.lendreturn.LendReturnBiz;
import org.nodes.wms.biz.lendreturn.modular.LogLendReturnFactory;
import org.nodes.wms.biz.outstock.logSoPick.LogSoPickBiz;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.lendreturn.LogLendReturnDao;
import org.nodes.wms.dao.lendreturn.LogNoReturnDao;
import org.nodes.wms.dao.lendreturn.dto.input.LendReturnQuery;
import org.nodes.wms.dao.lendreturn.dto.input.LendReturnRequest;
import org.nodes.wms.dao.lendreturn.dto.input.LogLendReturnRequest;
import org.nodes.wms.dao.lendreturn.dto.output.LendReturnResponse;
import org.nodes.wms.dao.lendreturn.dto.output.NoReturnExcelResponse;
import org.nodes.wms.dao.lendreturn.dto.output.NoReturnResponse;
import org.nodes.wms.dao.lendreturn.entities.LogLendReturn;
import org.nodes.wms.dao.lendreturn.entities.LogNoReturn;
import org.nodes.wms.dao.lendreturn.enums.LendReturnTypeEnum;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.tool.api.ResultCode;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *借出归还 业务类
 */
@Service
@RequiredArgsConstructor
public class LendReturnBizImpl implements LendReturnBiz {

	private final LogLendReturnFactory logLendReturnFactory;
	private final LogLendReturnDao logLendReturnDao;
	private final LogNoReturnDao logNoReturnDao;
	private final ReceiveLogBiz receiveLogBiz;
	private final LogSoPickBiz logSoPickBiz;

	/**
	 * 保存借出归还记录
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveLog(LendReturnRequest lendReturnRequest) {
		List<LogLendReturn> logLendReturnList;
		List<LogLendReturnRequest> logLendReturnRequestList = lendReturnRequest.getLogLendReturnRequestList();
		boolean allMatchType = logLendReturnRequestList.stream().allMatch(d -> d.getType() == lendReturnRequest.getType());
		if (Boolean.FALSE == allMatchType){
			throw  ExceptionUtil.mpe("借出归还集合内的类型必须一致");
		}
		if (Func.equals(WmsAppConstant.BILL_TYPE_LEND,lendReturnRequest.getBillTypeCd())){
			 logLendReturnList = logLendReturnFactory.createLendList(logLendReturnRequestList);
		}else if (Func.equals(WmsAppConstant.BILL_TYPE_RETURN,lendReturnRequest.getBillTypeCd())){
			logLendReturnList = logLendReturnFactory.createReturnList(logLendReturnRequestList);
		}else {
			throw ExceptionUtil.mpe(ResultCode.PARAM_VALID_ERROR);
		}
		// 实时计算未归还报表数据
		boolean lendFlag = lendReturnRequest.getType() == LendReturnTypeEnum.LEND;
		// 同一个人，同一个物品（批属性相同）
		List<LogNoReturn> logNoReturnList = new ArrayList<>();
		for (LogLendReturnRequest logLendReturnRequest : logLendReturnRequestList) {
			LogNoReturn logNoReturn = logNoReturnDao.sameSku(logLendReturnRequest);
			if(Func.isNull(logNoReturn)){
				if (Boolean.FALSE == lendFlag){
					throw ExceptionUtil.mpe("归还时，没有对应的借出记录");
				}
				logNoReturn = Func.copy(logLendReturnRequest,LogNoReturn.class);
				// 保持新增状态
				assert logNoReturn != null;
				logNoReturn.setId(null);
				logNoReturn.setLendQty(logLendReturnRequest.getQty());
				logNoReturnList.add(logNoReturn);
			}else {
				// 相同物品数量累加
				LogNoReturn sameSku = getSameSku(logNoReturnList, logNoReturn);
				if (Func.isNull(sameSku)){
					setQtyAndSnCode(lendFlag, logLendReturnRequest, logNoReturn);
					logNoReturnList.add(logNoReturn);
				}else{
					setQtyAndSnCode(lendFlag, logLendReturnRequest, sameSku);
				}
			}
		}
		saveLogData(logNoReturnList,logLendReturnList);
	}

	private void saveLogData(List<LogNoReturn> logNoReturnList, List<LogLendReturn> logLendReturnList) {
		// 如果借出量 == 归还量，物理删除记录
		List<Long> deleteIdList = logNoReturnList.stream()
			.filter(d -> Func.notNull(d.getId())
				&& BigDecimalUtil.eq(d.getLendQty(), d.getReturnQty()))
			.map(LogNoReturn::getId)
			.collect(Collectors.toList());
		List<LogNoReturn> insertList = logNoReturnList.stream()
			.filter(d -> Func.isNull(d.getId()))
			.collect(Collectors.toList());
		List<LogNoReturn> updateList = logNoReturnList.stream()
			.filter(d -> Func.notNull(d.getId()))
			.collect(Collectors.toList());

 		if (Func.isNotEmpty(deleteIdList)) {
			logNoReturnDao.deleteByIdList(deleteIdList);
		}
		if (Func.isNotEmpty(insertList)) {
			logNoReturnDao.saveBatch(insertList, insertList.size());
		}
		if (Func.isNotEmpty(updateList)) {
			logNoReturnDao.updateBatchById(updateList, updateList.size());
		}
		logLendReturnDao.saveBatch(logLendReturnList, logLendReturnList.size());
	}

	@Override
	public Page<LendReturnResponse> pageLendReturn(Page<LogLendReturn> page, LendReturnQuery lendReturnQuery) {
		return logLendReturnDao.selectPage(page, lendReturnQuery);
	}

	@Override
	public Page<NoReturnResponse> pageNoReturn(Page<LogNoReturn> page, LendReturnQuery lendReturnQuery) {
		return logNoReturnDao.selectPage(page,lendReturnQuery);
	}

	@Override
	public void exportNoReturn(LendReturnQuery lendReturnQuery, HttpServletResponse response) {
		List<LogNoReturn> logNoReturnList = logNoReturnDao.listByQuery(lendReturnQuery);
		ExcelUtil.export(response,"","",Func.copy(logNoReturnList, NoReturnExcelResponse.class),NoReturnExcelResponse.class);
	}

    @Override
    public boolean removeBySoDetailId(Long soDetailId) {
        return logLendReturnDao.deleteBySoDetailId(soDetailId) && logNoReturnDao.deleteBySoDetailId(soDetailId);
    }

    private static void setQtyAndSnCode(boolean lendFlag, LogLendReturnRequest logLendReturnRequest, LogNoReturn logNoReturn) {
		if (lendFlag) {
			logNoReturn.setLendQty(logNoReturn.getLendQty().add(logLendReturnRequest.getQty()));
		}else {
			logNoReturn.setReturnQty(logNoReturn.getReturnQty().add(logLendReturnRequest.getQty()));
			if (Func.isNotBlank(logLendReturnRequest.getSnCode())) {
				if (Func.isBlank(logNoReturn.getSnCode())) {
					throw ExceptionUtil.mpe("参数存在序列号，但是借出记录的序列号为空");
				}
				String[] noReturnSnCodeArray = Func.splitTrim(logNoReturn.getSnCode(), StringPool.COMMA);
				String[] requestSnCodeArray = Func.splitTrim(logLendReturnRequest.getSnCode(), StringPool.COMMA);
				for (String s : requestSnCodeArray) {
					boolean anyMatch = Arrays.stream(noReturnSnCodeArray)
						.anyMatch(d -> Func.equals(d, s));
					if (Boolean.FALSE == anyMatch) {
						throw ExceptionUtil.mpe("当前请求的序列号：{}，不存在于借出记录内：{}", s, noReturnSnCodeArray);
					}
				}
				// 删除归还的序列号
				List<String> noReturnSnCodeList = Arrays.asList(noReturnSnCodeArray);
				noReturnSnCodeList.removeAll(Arrays.asList(requestSnCodeArray));
				logNoReturn.setSnCode(Func.join(noReturnSnCodeList,StringPool.COMMA));
			}
		}
	}

	private static LogNoReturn getSameSku(List<LogNoReturn> logNoReturnList, LogNoReturn finalLogNoReturn) {
		return logNoReturnList.stream()
			.filter(d -> Func.notNull(d.getId())
				&& Func.equals(d.getId(), finalLogNoReturn.getId()))
			.findFirst().orElse(null);
	}

	@Override
	public void saveReturnLog(ReceiveHeader receiveHeader) {
		AssertUtil.notNull(receiveHeader, "收货单头表为null");
		// 归还单关单时，保存归还记录
		if (WmsAppConstant.BILL_TYPE_RETURN.equals(receiveHeader.getBillTypeCd())) {
			List<ReceiveLog> receiveLogList = receiveLogBiz.findReceiveLogList(receiveHeader.getReceiveId());
			List<ReceiveLog> finalReceiveLogList = receiveLogList.stream()
				.filter(log -> BigDecimalUtil.gt(log.getQty(), BigDecimal.ZERO)
					&& (Func.isEmpty(log.getCancelLogId()) || Func.isBlank(log.getCancelLogId())))
				.collect(Collectors.toList());
			LendReturnRequest returnRequest = logLendReturnFactory.createReturnRequest(finalReceiveLogList);
			saveLog(returnRequest);
		}
	}

	@Override
	public void saveLendLog(SoHeader soHeader) {
		AssertUtil.notNull(soHeader, "发货单头表为null");
		// 借出单关单时，保存借出记录
		if (WmsAppConstant.BILL_TYPE_LEND.equals(soHeader.getBillTypeCd())) {
			// 发货单的全部拣货记录
			List<LogSoPick> logSoPickList = logSoPickBiz.findBySoHeaderId(soHeader.getSoBillId());
			// 有效的拣货记录
			List<LogSoPick> enableLogSoPickList = logSoPickList.stream()
				.filter(log -> BigDecimalUtil.gt(log.getPickRealQty(), BigDecimal.ZERO)
					&& (Func.isEmpty(log.getCancelLogId()) || Func.isBlank(log.getCancelLogId())))
				.collect(Collectors.toList());
			// 根据SkuId、批属性合并拣货记录
			List<LogSoPick> finalLogSoPickList = mergeLogSoPick(enableLogSoPickList);
			LendReturnRequest returnRequest = logLendReturnFactory.createLendRequest(finalLogSoPickList);
			saveLog(returnRequest);
		}
	}

	public List<LogSoPick> mergeLogSoPick(List<LogSoPick> enableLogSoPickList){
		List<LogSoPick> logSoPickList = new ArrayList<>();
		Map<Long, List<LogSoPick>> logSoPickMap = new HashMap<>();
		enableLogSoPickList.forEach(x -> {
			List<LogSoPick> logSoPicks = enableLogSoPickList.stream()
				.filter(y -> y.getSkuId().equals(x.getSkuId()) && SkuLotUtil.compareAllSkuLot(x, y))
				.collect(Collectors.toList());
			logSoPickMap.put(x.getSkuId(), logSoPicks);
		});

		for (Map.Entry<Long, List<LogSoPick>> entry : logSoPickMap.entrySet()) {
			List<LogSoPick> value = entry.getValue();
			LogSoPick logSoPick = value.stream().findFirst().orElse(null);
			if (Func.isNull(logSoPick)){
				continue;
			}
			BigDecimal pickSum = value.stream().map(LogSoPick::getPickRealQty)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
			logSoPick.setPickRealQty(pickSum);
			logSoPickList.add(logSoPick);
		}
		return logSoPickList;
	}
}
