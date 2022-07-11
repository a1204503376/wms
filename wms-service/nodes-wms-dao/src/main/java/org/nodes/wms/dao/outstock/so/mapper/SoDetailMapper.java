package org.nodes.wms.dao.outstock.so.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.outstock.so.dto.output.SoDetailEditResponse;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 收货单头表Mapper接口
 */
@Repository("soDetailRepository")
public interface SoDetailMapper extends BaseMapper<SoDetail> {

	/**
	 * 获取编辑时出库单明细信息
	 *
	 * @param soBillId: 出库单id
	 * @return List<SoDetailEditResponse> 出库单明细信息
	 */
    List<SoDetailEditResponse> selectSoDetailEditBySoBillId(Long soBillId);
}
