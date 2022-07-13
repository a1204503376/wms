package org.nodes.wms.biz.basics.skulot.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.skulot.SkuLotBiz;
import org.nodes.wms.dao.basics.skulot.SkuLotDao;
import org.nodes.wms.dao.basics.skulot.dto.input.FindAllSkuLotRequest;
import org.nodes.wms.dao.basics.skulot.dto.output.FindAllSkuLotResponse;
import org.nodes.wms.dao.basics.skulot.entities.SkuLot;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.stereotype.Service;

/**
 * 批属性获取Biz
 */
@Service
@RequiredArgsConstructor
public class SkuLotBizImpl implements SkuLotBiz {
	private final SkuLotDao skuLotDao;

	@Override
	public FindAllSkuLotResponse selectsAllSkuLotByWoId(FindAllSkuLotRequest request) {
		SkuLot lot = skuLotDao.getSkuLotByWoId(request.getWoId());
		return BeanUtil.copy(lot, FindAllSkuLotResponse.class);
	}
}
