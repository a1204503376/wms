package org.nodes.wms.dao.basics.owner.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.owner.OwnerDao;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.owner.mapper.OwnerMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 货主 Dao实现类
 **/
@Repository
@RequiredArgsConstructor
public class OwnerDaoImpl implements OwnerDao {

	private final OwnerMapper ownerRepository;
	@Override
	public List<Owner> selectOwnerSelectResponseList() {
		LambdaQueryWrapper<Owner> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper
			.select(Owner::getWoId,Owner::getOwnerCode,Owner::getOwnerName);
		return ownerRepository.selectList(queryWrapper);
	}
}
