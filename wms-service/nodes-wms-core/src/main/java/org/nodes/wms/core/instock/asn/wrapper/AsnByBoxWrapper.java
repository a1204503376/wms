package org.nodes.wms.core.instock.asn.wrapper;

import org.springblade.core.log.exception.ServiceException;
import org.nodes.wms.core.instock.asn.dto.AsnHeaderBoxDTO;
import org.nodes.wms.core.instock.asn.vo.AsnHeaderBoxSubmitVO;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;

import java.math.BigDecimal;

/**
 * 按箱收货封装类
 * @Author zx
 * @Date 2020/6/24
 **/
public class AsnByBoxWrapper {

	public static AsnByBoxWrapper build() {
		return new AsnByBoxWrapper();
	}

	/**
	 * 入库非空校验
	 *
	 * @param asnDTO
	 */
	public void verifyInstock(AsnHeaderBoxSubmitVO boxSubmitVO) {
		//验证必填项
		if (Func.isEmpty(boxSubmitVO.getSkuCode())) throw new ServiceException("SKU编码不能为空");
		if (Func.isEmpty(boxSubmitVO.getScanQty()) || boxSubmitVO.getScanQty().compareTo(BigDecimal.ZERO) == 0 )  throw new ServiceException("请填入有效数量");
		if (Func.isEmpty(boxSubmitVO.getWsuName())) throw new ServiceException("单位不能为空");
		if (Func.isEmpty(boxSubmitVO.getLocCode())) throw new ServiceException("目标库位不能为空");
	}

	/**
	 * 按箱收货提交VO转DTO
	 * @param boxSubmitVO
	 * @return
	 */
	public AsnHeaderBoxDTO submitVOToDto(AsnHeaderBoxSubmitVO boxSubmitVO){
		return BeanUtil.copy(boxSubmitVO, AsnHeaderBoxDTO.class);
	}


}
