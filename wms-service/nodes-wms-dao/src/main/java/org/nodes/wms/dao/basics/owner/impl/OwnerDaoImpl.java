package org.nodes.wms.dao.basics.owner.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.NullArgumentException;
import org.nodes.wms.dao.basics.owner.OwnerDao;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.owner.mapper.OwnerMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 货主 Dao实现类
 **/
@Repository
@RequiredArgsConstructor
public class OwnerDaoImpl extends BaseServiceImpl<OwnerMapper, Owner> implements OwnerDao {

	@Override
	public List<Owner> selectOwnerSelectResponseList() {
		LambdaQueryWrapper<Owner> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper
			.select(Owner::getWoId, Owner::getOwnerCode, Owner::getOwnerName);
		return super.baseMapper.selectList(queryWrapper);
	}

	@Override
	public Owner getById(Long woId) {
		if (Func.isEmpty(woId)) {
			throw new NullArgumentException("OwnerDaoImpl.getById方法的参数为空");
		}
		return super.getById(woId);
	}

	@Override
	public Owner getByCode(String ownerCode) {
		LambdaQueryWrapper<Owner> lambdaQueryWrapper = Wrappers.lambdaQuery();
		lambdaQueryWrapper.eq(Owner::getOwnerCode, ownerCode);
		return super.getOne(lambdaQueryWrapper);
	}

	@Override
	public Owner selectFirstOwner() {
		List<Owner> ownerList = list().stream().limit(1).collect(toList());
		return ownerList.get(0);
	}
}
