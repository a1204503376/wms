package org.nodes.wms.biz.lendreturn.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.lendreturn.dto.input.LogLendReturnRequest;
import org.nodes.wms.dao.lendreturn.entities.LogLendReturn;
import org.nodes.wms.dao.lendreturn.enums.LendReturnTypeEnum;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 借出归还工厂
 * 主要用于创建借出归还实体
 */
@Service
@RequiredArgsConstructor
public class LogLendReturnFactory {

	public List<LogLendReturn> createLendList(List<LogLendReturnRequest> logLendReturnRequestList){
		AssertUtil.notEmpty(logLendReturnRequestList,"借出请求对象集合为空");
		List<LogLendReturn> logLendReturnList = Func.copy(logLendReturnRequestList, LogLendReturn.class);
		logLendReturnList.forEach(this::setLendType);
		return logLendReturnList;
	}

	public List<LogLendReturn> createReturnList(List<LogLendReturnRequest> returnRequestList){
		AssertUtil.notEmpty(returnRequestList,"归还请求对象集合为空");
		List<LogLendReturn> logLendReturnList = Func.copy(returnRequestList, LogLendReturn.class);
		logLendReturnList.forEach(this::setReturnType);
		return logLendReturnList;
	}

	private void setReturnType(LogLendReturn logLendReturn) {
		logLendReturn.setType(LendReturnTypeEnum.RETURN);
	}

	private void setLendType(LogLendReturn logLendReturn) {
		logLendReturn.setType(LendReturnTypeEnum.LEND);
	}
}
