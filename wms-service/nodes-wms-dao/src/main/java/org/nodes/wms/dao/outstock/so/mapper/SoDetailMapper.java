package org.nodes.wms.dao.outstock.so.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.outstock.so.dto.output.SoDetailEditResponse;
import org.nodes.wms.dao.outstock.so.dto.output.SoDetailForDetailResponse;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 收货单头表Mapper接口
 */
@Repository("soDetailRepository")
public interface SoDetailMapper extends BaseMapper<SoDetail> {

	/**
	 * 获取编辑时发货单明细信息
	 *
	 * @param soBillId: 发货单id
	 * @return List<SoDetailEditResponse> 发货单明细信息
	 */
    List<SoDetailEditResponse> selectSoDetailEditBySoBillId(Long soBillId);

	/**
	 * 查看明细：根据发货单id分页查询发货单明细信息
	 *
	 * @param soBillId: 发货单id
	 * @param page: 分页参数
	 * @return Page<SoDetailForDetailResponse> 发货单明细分页信息
	 */
    Page<SoDetailForDetailResponse> pageForSoDetailBySoBillId(IPage<?> page, @Param("soBillId") Long soBillId);
}
