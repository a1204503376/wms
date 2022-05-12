package org.nodes.wms.dao.basics.billType.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.basics.billType.dto.BillTypeSelectResponse;
import org.nodes.wms.dao.basics.billType.entities.BillType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 单据类型 Mapper 接口
 */
@Repository("billTypeRepository")
public interface BillTypeMapper extends BaseMapper<BillType> {

	/**
	 * 根据单据类型编码或者单据类型名称查询前10个单据类型信息
	 *
	 * @param ioType 类型模式
	 * @param billTypeCd 单据类型编码
	 * @param billTypeName 单据类型名称
	 * @return List<BillSelectResponse>
	 */
	List<BillTypeSelectResponse> listTop10ByBillTypeCdBillTypeName(
		@Param("ioType") String ioType,
		@Param("billTypeCd") String billTypeCd,
		@Param("billTypeName") String billTypeName);
}
