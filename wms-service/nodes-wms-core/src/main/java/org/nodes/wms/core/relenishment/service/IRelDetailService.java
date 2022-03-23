package org.nodes.wms.core.relenishment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.relenishment.dto.RelDetailDTO;
import org.nodes.wms.core.relenishment.entity.RelDetail;
import org.nodes.wms.core.relenishment.entity.RelHeader;
import org.nodes.wms.core.relenishment.vo.RelDetailVo;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.mp.support.Query;

/**
 * 补货明细 服务类
 *
 * @author whj
 * @since 2019-12-24
 */
public interface IRelDetailService extends BaseService<RelDetail> {

	IPage<RelDetailVo> selectPage(RelDetailDTO stockDTO, Query query);

	void createRelenishDetail(String stockIds, RelHeader relHeader);
	void createRelenishDetail1(String stockIds, RelHeader relHeader);
}
