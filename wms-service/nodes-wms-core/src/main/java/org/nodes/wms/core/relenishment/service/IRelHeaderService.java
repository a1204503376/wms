package org.nodes.wms.core.relenishment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.relenishment.dto.RelHeaderDTO;
import org.nodes.wms.core.relenishment.entity.RelHeader;
import org.nodes.wms.core.relenishment.vo.RelHeaderVo;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.mp.support.Query;

/**
 * 补货表头 服务类
 *
 * @author whj
 * @since 2019-12-24
 */
public interface IRelHeaderService extends BaseService<RelHeader> {

	IPage<RelHeaderVo> selectPage(RelHeaderDTO stockDTO, Query query);

	void createRelenishOrder(RelHeaderDTO relHeaderDTO);

    RelHeaderVo getDetail(Long relBillId);

    boolean saveOrUpdate(RelHeaderVo relHeaderVo);

	boolean canEdit(Long relBillId);

	boolean relTask(String ids);

	boolean removeDataByIds(String ids);

	boolean checkTask(String ids);
}
