package org.nodes.wms.dao.basics.warehouse.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.warehouse.SysAuthDao;
import org.nodes.wms.dao.basics.warehouse.entities.SysAuth;
import org.nodes.wms.dao.basics.warehouse.mapper.SysAuthMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * 仓库授权Dao接口实现类
 **/
@Repository
@RequiredArgsConstructor
public class SysAuthDaoImpl extends BaseServiceImpl<SysAuthMapper,SysAuth> implements SysAuthDao {

	@Override
	public void insertOne(SysAuth sysAuth) {
		super.save(sysAuth);
	}

	@Override
	public SysAuth getOne() {
		LambdaQueryWrapper<SysAuth> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.last("limit 1");
		return super.getOne(queryWrapper);
	}
}
