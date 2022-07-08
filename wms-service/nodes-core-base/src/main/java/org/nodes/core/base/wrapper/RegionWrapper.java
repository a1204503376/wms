package org.nodes.core.base.wrapper;

import org.nodes.core.base.entity.Region;
import org.nodes.core.base.vo.RegionVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
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
