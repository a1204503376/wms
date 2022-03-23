
package org.nodes.wms.core.instock.asn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.instock.asn.entity.AsnLpnDetail;
import org.nodes.wms.core.instock.asn.vo.AsnLpnDetailVO;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 *  入库容器明细Mapper 接口
 *
 * @author zhonglianshuai
 * @since 2020-02-14
 */
@Primary
public interface AsnLpnDetailMapper extends BaseMapper<AsnLpnDetail> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param lpnDetail
	 * @return
	 */
	List<AsnLpnDetailVO> selectLpnDetailPage(IPage page, AsnLpnDetailVO lpnDetail);

}
