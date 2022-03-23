package org.nodes.core.base.service.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.nodes.core.base.mapper.UserMapper;
import org.springblade.core.log.model.LogApi;
import org.springblade.core.log.model.LogError;
import org.springblade.core.log.model.LogUsual;
import org.nodes.core.base.service.ILogApiService;
import org.nodes.core.base.service.ILogErrorService;
import org.nodes.core.base.service.ILogService;
import org.nodes.core.base.service.ILogUsualService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Blade.
 *
 * @author zhuangqian
 */
@Service
@AllArgsConstructor
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class LogServiceImpl implements ILogService {

	ILogUsualService usualService;
	ILogApiService apiService;
	ILogErrorService errorService;

	@Override
	public Boolean saveUsualLog(LogUsual log) {
		return usualService.save(log);
	}

	@Override
	public Boolean saveApiLog(LogApi log) {
		return apiService.save(log);
	}

	@Override
	public Boolean saveErrorLog(LogError log) {
		return errorService.save(log);
	}

}
