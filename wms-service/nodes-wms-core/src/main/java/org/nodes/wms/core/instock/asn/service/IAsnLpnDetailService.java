
package org.nodes.wms.core.instock.asn.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.nodes.wms.core.instock.asn.dto.AsnDTO;
import org.nodes.wms.core.instock.asn.entity.AsnLpnDetail;
import org.nodes.wms.core.instock.asn.vo.AsnLpnDetailVO;

import java.util.Map;

/**
 * 入库容器明细 服务类
 *
 * @author zhonglianshuai
 * @since 2020-02-14
 */
public interface IAsnLpnDetailService extends IService<AsnLpnDetail> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param lpnDetail
	 * @return
	 */
	IPage<AsnLpnDetailVO> selectLpnDetailPage(IPage<AsnLpnDetailVO> page, AsnLpnDetailVO lpnDetail);

	/**
	 * 获得详情
	 * @param asnLpnDetail
	 * @return
	 */
	Map<String, Object> getAsnLpnDetail(AsnLpnDetail asnLpnDetail);


	/**
	 * 生成LPN信息
	 * @param asnBillIds
	 * @return
	 */
	Boolean creatLpn(String asnBillIds);

	/**
	 * 提交入库
	 * @param dto
	 * @return
	 */
	Map<String, Object> submitAsnLpn(AsnDTO dto);


}
