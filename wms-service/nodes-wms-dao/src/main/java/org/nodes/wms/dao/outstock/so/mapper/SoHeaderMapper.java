package org.nodes.wms.dao.outstock.so.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.outstock.so.dto.input.SoHeaderPageQuery;
import org.nodes.wms.dao.outstock.so.dto.output.SoHeaderEditResponse;
import org.nodes.wms.dao.outstock.so.dto.output.SoHeaderPageResponse;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.springframework.stereotype.Repository;

/**
 * 收货单头表Mapper接口
 */
@Repository("soHeaderRepository")
public interface SoHeaderMapper extends BaseMapper<SoHeader> {

	/**
	 * 获取ASN单的分页结果，支持明细相关字段查询
	 *
	 * @param page    分页参数
	 * @param soHeaderPageQuery 条件参数
	 * @return Page<SoHeaderPageResponse>
	 */
	Page<SoHeaderPageResponse> page(IPage<?> page, @Param("params")SoHeaderPageQuery soHeaderPageQuery);

	/**
	 * 获取编辑时出库单头表信息
	 *
	 * @param soBillId: 出库单id
	 * @return SoHeaderEditResponse 出库单编辑响应对象
	 */
	SoHeaderEditResponse selectSoHeaderEditBySoBillId(@Param("soBillId") Long soBillId);
}
