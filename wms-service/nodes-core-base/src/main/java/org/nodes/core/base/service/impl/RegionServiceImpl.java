package org.nodes.core.base.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nodes.core.base.entity.Region;
import org.nodes.core.base.mapper.RegionMapper;
import org.nodes.core.base.mapper.UserMapper;
import org.nodes.core.base.service.IRegionService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
@Transactional(propagation = Propagation.NESTED,isolation = Isolation.DEFAULT,rollbackFor = Exception.class)
public class RegionServiceImpl <M extends RegionMapper, T extends Region> extends ServiceImpl<RegionMapper, Region> implements IRegionService{

}
