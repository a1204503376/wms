package org.nodes.wms.dao.instock.receive.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.NullArgumentException;
import org.nodes.wms.dao.instock.receive.dto.input.ReceivePdaQuery;
import org.nodes.wms.dao.instock.receive.dto.output.DetailReceiveHeaderResponse;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveHeaderPdaResponse;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.nodes.wms.dao.instock.receive.enums.ReceiveHeaderStateEnum;
import org.nodes.wms.dao.instock.receive.mapper.ReceiveHeaderMapper;
import org.nodes.wms.dao.instock.receive.ReceiveHeaderDao;
import org.nodes.wms.dao.instock.receive.dto.input.ReceivePageQuery;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveHeaderResponse;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收货单 DAO 实现类
 */
@Repository
@RequiredArgsConstructor
public class ReceiveHeaderDaoImpl extends BaseServiceImpl<ReceiveHeaderMapper, ReceiveHeader> implements ReceiveHeaderDao {
	private final ReceiveHeaderMapper receiveHeaderMapper;

	@Override
	public IPage<ReceiveHeaderResponse> selectPage(IPage<ReceiveHeaderResponse> page, ReceivePageQuery receivePageQuery) {
		return receiveHeaderMapper.getPage(page, receivePageQuery);
	}

	@Override
	public boolean delete(List<Long> receiveIdList) {
		return super.deleteLogic(receiveIdList);
	}

	@Override
	public DetailReceiveHeaderResponse selectHeaderById(Long receiveId) {
		return receiveHeaderMapper.selectHeaderById(receiveId);
	}

	@Override
	public boolean updateBillStateById(Long receiveDetailId) {
		LambdaUpdateWrapper<ReceiveHeader> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
		lambdaUpdateWrapper.set(ReceiveHeader::getBillState, 40);
		lambdaUpdateWrapper.eq(ReceiveHeader::getReceiveId, receiveDetailId);
		return super.update(lambdaUpdateWrapper);
	}

	@Override
	public boolean insert(ReceiveHeader receiveHeader) {
		return super.save(receiveHeader);
	}

	@Override
	public List<ReceiveHeaderResponse> getReceiveHeaderResponseByQuery(ReceivePageQuery receivePageQuery) {
		return receiveHeaderMapper.getReceiveHeaderResponseByQuery(receivePageQuery);
	}

	@Override
	public ReceiveHeader selectReceiveHeaderById(Long receiveId) {
		if (Func.isEmpty(receiveId)) {
			throw new NullArgumentException("ReceiveHeaderDaoImpl.selectReceiveHeaderById方法的参数为空");
		}
		return super.getById(receiveId);
	}

	@Override
	public void updateReceive(ReceiveHeader receiveHeader) {
		super.saveOrUpdate(receiveHeader);

	}

	@Override
	public ReceiveHeader selectBillStateById(Long receiveId) {
		LambdaQueryWrapper<ReceiveHeader> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.select(ReceiveHeader::getBillState, ReceiveHeader::getReceiveNo).eq(ReceiveHeader::getReceiveId, receiveId);
		return super.getOne(lambdaQueryWrapper);
	}

	@Override
	public Page<ReceiveHeaderPdaResponse> getReceiveList(ReceivePdaQuery receivePdaQuery, IPage<ReceiveHeader> page) {
		if (Func.isEmpty(receivePdaQuery.getNo())) {
			throw new ServiceException("必须输入收货单编码或上游编码");
		}
		return receiveHeaderMapper.getReceiveList(receivePdaQuery, page);
	}

	@Override
	public Boolean updateReceiveHeader(ReceiveHeader header) {
		return super.updateById(header);
	}

    @Override
    public List<ReceiveHeader> selectReceiveListByNonOrder(Long userId) {
		return super.list(new LambdaQueryWrapper<ReceiveHeader>()
			.eq(ReceiveHeader::getCreateUser,userId)
			.eq(ReceiveHeader::getInStoreType,20)
			.in(ReceiveHeader::getBillState,
				ReceiveHeaderStateEnum.NOT_RECEIPT.getCode(),ReceiveHeaderStateEnum.PART.getCode()));
    }

    @Override
    public void saveReceiveHeader(ReceiveHeader receiveHeader) {
        super.save(receiveHeader);
    }

}
