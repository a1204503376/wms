package org.nodes.wms.biz.lendreturn.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsAppConstant;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.ExceptionUtil;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.biz.lendreturn.LendReturnBiz;
import org.nodes.wms.biz.lendreturn.modular.LogLendReturnFactory;
import org.nodes.wms.dao.lendreturn.LogLendReturnDao;
import org.nodes.wms.dao.lendreturn.LogNoReturnDao;
import org.nodes.wms.dao.lendreturn.dto.input.LendReturnQuery;
import org.nodes.wms.dao.lendreturn.dto.input.LendReturnRequest;
import org.nodes.wms.dao.lendreturn.dto.input.LogLendReturnRequest;
import org.nodes.wms.dao.lendreturn.dto.output.LendReturnResponse;
import org.nodes.wms.dao.lendreturn.dto.output.NoReturnResponse;
import org.nodes.wms.dao.lendreturn.entities.LogLendReturn;
import org.nodes.wms.dao.lendreturn.entities.LogNoReturn;
import org.nodes.wms.dao.lendreturn.enums.LendReturnTypeEnum;
import org.springblade.core.tool.api.ResultCode;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
	@Resource
	private LendReturnBiz lendReturnBiz;

	/**
	 * 保存借出归还记录
	 */
	@Override
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
		lendReturnBiz.saveLogData(logNoReturnList,logLendReturnList);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveLogData(List<LogNoReturn> logNoReturnList, List<LogLendReturn> logLendReturnList) {
		// 如果借出量 == 归还量，物理删除记录
		List<LogNoReturn> deleteList = logNoReturnList.stream()
			.filter(d -> Func.notNull(d.getId())
				&& BigDecimalUtil.eq(d.getLendQty(), d.getReturnQty()))
			.collect(Collectors.toList());
		List<LogNoReturn> insertList = logNoReturnList.stream()
			.filter(d -> Func.isNull(d.getId()))
			.collect(Collectors.toList());
		List<LogNoReturn> updateList = logNoReturnList.stream()
			.filter(d -> Func.notNull(d.getId()))
			.collect(Collectors.toList());

		if (Func.isNotEmpty(deleteList)) {
			logNoReturnDao.removeByIds(deleteList);
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
}
