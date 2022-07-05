package org.nodes.wms.dao.instock.receiveLog.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.NullArgumentException;
import org.nodes.wms.dao.instock.receiveLog.ReceiveLogDao;
import org.nodes.wms.dao.instock.receiveLog.dto.input.ReceiveLogPageQuery;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogExcelResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogIndexResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogPageResponse;
import org.nodes.wms.dao.instock.receiveLog.dto.output.ReceiveLogResponse;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.instock.receiveLog.mapper.ReceiveLogMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 清点记录Dao实现类
 */
@Repository
@RequiredArgsConstructor
public class ReceiveLogDaoImpl
	extends BaseServiceImpl<ReceiveLogMapper, ReceiveLog>
	implements ReceiveLogDao {
	@Override
	public List<ReceiveLogResponse> getReceiveLogList(Long receiveId) {
		return super.baseMapper.selectReceiveLogList(receiveId);
	}

    @Override
    public List<ReceiveLog> getReceiveLogListByIdList(List<Long> idList) {
		LambdaQueryWrapper<ReceiveLog> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.in(ReceiveLog::getId,idList);
        return super.list(queryWrapper);
    }

    @Override
	public List<ReceiveLogIndexResponse> getReceiveSkuQtyTop10() {
		return super.baseMapper.selectReceiveSkuQtyTop10();
	}

	@Override
	public Page<ReceiveLogPageResponse> page(IPage<?> page,
											 ReceiveLogPageQuery receiveLogPageQuery) {
		return super.baseMapper.page(page, receiveLogPageQuery);
	}

	@Override
	public List<ReceiveLogExcelResponse> getReceiveLogListByQuery(
			ReceiveLogPageQuery receiveLogPageQuery) {
		return super.baseMapper.selectReceiveLogListByQuery(receiveLogPageQuery);
	}

	@Override
	public boolean save(ReceiveLog receiveLog) {
		if(Func.isEmpty(receiveLog)){
			throw new NullArgumentException("ReceiveLogDaoImpl.save方法的参数为空");
		}
		return super.save(receiveLog);
	}

    @Override
    public boolean saveBatch(List<ReceiveLog> receiveLogList) {
		return super.saveBatch(receiveLogList);
	}
}
