package org.nodes.core.base.wrapper;

import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.entity.Region;
import org.nodes.core.base.entity.Tenant;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.base.service.ITenantService;
import org.nodes.core.base.vo.DictVO;
import org.nodes.core.base.vo.RegionVO;
import org.nodes.core.constant.DictConstant;
import org.springblade.core.mp.base.BaseEntity;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springblade.core.tool.node.INode;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RegionWrapper extends BaseEntityWrapper<Region, RegionVO> {

	public static RegionWrapper build() {
		return new RegionWrapper();
	}
	@Override
	public RegionVO entityVO(Region region) {
		RegionVO regionVO = Objects.requireNonNull(BeanUtil.copy(region, RegionVO.class));
		return regionVO;
	}

	public List<RegionVO> listRegionVO(List<Region> list) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		List<RegionVO> collect = list.stream().map(region -> entityVO(region)).collect(Collectors.toList());
		stopWatch.stop();
		System.out.println("---->" + stopWatch.getTotalTimeSeconds());
		return collect;
	}
}
