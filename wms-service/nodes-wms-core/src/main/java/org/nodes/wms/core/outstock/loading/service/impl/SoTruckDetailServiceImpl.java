
package org.nodes.wms.core.outstock.loading.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.nodes.wms.core.basedata.cache.OwnerCache;
import org.nodes.wms.core.basedata.entity.Owner;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.outstock.loading.entity.SoTruckDetail;
import org.nodes.wms.core.outstock.loading.mapper.SoTruckDetailMapper;
import org.nodes.wms.core.outstock.loading.service.ISoTruckDetailService;
import org.nodes.wms.core.outstock.loading.vo.SoTruckDetailVO;
import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.core.outstock.so.service.ISoDetailService;
import org.nodes.wms.core.outstock.so.service.ISoHeaderService;
import org.nodes.wms.core.outstock.so.service.IWellenDetailService;
import org.nodes.wms.core.outstock.so.vo.SoDetailVO;
import org.nodes.wms.core.outstock.so.vo.SoHeaderVO;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 车次明细 服务实现类
 *
 * @author chz
 * @since 2020-03-03
 */
@Service
@Primary
@Transactional(propagation= Propagation.NESTED,isolation= Isolation.DEFAULT,rollbackFor=Exception.class)
public class SoTruckDetailServiceImpl<M extends SoTruckDetailMapper, T extends SoTruckDetail>
	extends BaseServiceImpl<SoTruckDetailMapper, SoTruckDetail>
	implements ISoTruckDetailService {

	@Autowired
	ISoHeaderService soHeaderService;
	@Autowired
	ISoDetailService soDetailService;

	@Override
	public SoTruckDetailVO getStruckDetail(String truckId) {
		SoTruckDetailVO soTruckDetailVO=new SoTruckDetailVO();
		List<SoHeaderVO> soHeaderVOList=new ArrayList<>();
		List<SoDetailVO> soDetailVOList=new ArrayList<>();
		//查询装车明细
		List<SoTruckDetail> soTruckDetailList=this.list(new QueryWrapper<SoTruckDetail>().lambda().eq(SoTruckDetail::getTruckId, Func.toLong(truckId)));
		//去除相同soBillId,因为有个容器编码不知道会不会重复，先去重，免得重复查询
		List<SoTruckDetail> soTruckDetailListDistinct = soTruckDetailList.stream().collect(
			Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getSoBillId()))),
				ArrayList::new));
		if(soTruckDetailListDistinct.size()<=0)throw new RuntimeException("没有装车信息明细");
		for (SoTruckDetail soTruckDetail:soTruckDetailListDistinct)
		{
			//查询出库单头
			List<SoHeader> soHeaderList=soHeaderService.list(new QueryWrapper<SoHeader>().lambda().
				eq(SoHeader::getSoBillId,soTruckDetail.getSoBillId()));
			if(soHeaderList.size()>0) {
				for (SoHeader soHeader : soHeaderList) {
					SoHeaderVO soHeaderVO = BeanUtil.copy(soHeader, SoHeaderVO.class);
					IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
					Owner owner = ownerService.getById(soHeader.getWoId());
					if (ObjectUtil.isNotEmpty(owner)) {
						soHeaderVO.setOwnerName(owner.getOwnerName());
					}
					soHeaderVOList.add(soHeaderVO);
				}
			}

		}
		//查询出库单明细
		if(soHeaderVOList.size()>0)
		{
			for (SoHeaderVO soHeaderVO:soHeaderVOList)
			{
				List<SoDetail> soDetailList=soDetailService.list(new QueryWrapper<SoDetail>().lambda().
					eq(SoDetail::getSoBillId,soHeaderVO.getSoBillId()));
				if(soDetailList.size()>0) {
					for (SoDetail soDetail : soDetailList) {
						SoDetailVO soDetailVO = BeanUtil.copy(soDetail, SoDetailVO.class);
						soDetailVOList.add(soDetailVO);
					}
				}
			}
		}
		soTruckDetailVO.setSoDetailVOList(soDetailVOList);
		soTruckDetailVO.setSoHeaderVOList(soHeaderVOList);
		return soTruckDetailVO;
	}

}
