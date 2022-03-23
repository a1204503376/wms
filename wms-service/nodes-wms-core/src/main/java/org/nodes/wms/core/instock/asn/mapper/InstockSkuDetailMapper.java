
package org.nodes.wms.core.instock.asn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.instock.asn.entity.InstockSkuDetail;
import org.nodes.wms.core.instock.asn.vo.InstockSkuDetailVO;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 *  上架策略物品明细 Mapper 接口
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Primary
public interface InstockSkuDetailMapper extends BaseMapper<InstockSkuDetail> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param instockSkuDetail
	 * @return
	 */
	List<InstockSkuDetailVO> selectInstockSkuDetailPage(IPage page, InstockSkuDetailVO instockSkuDetail);

	/**
	 * 根据上架策略明细ID查询物品明细
	 * @param ssidId 上架策略明细ID
	 * @return 物品明细集合
	 */
	List<InstockSkuDetail> selectBySsidId(Long ssidId);
}
