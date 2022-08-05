
package org.nodes.wms.core.count.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.count.entity.CountHeader;
import org.nodes.wms.core.count.vo.CountHeaderVO;
import org.springblade.core.datascope.annotation.DataAuth;
import org.springblade.core.datascope.enums.DataScopeEnum;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 盘点单头表 Mapper 接口
 *
 * @author NodeX
 * @since 2020-01-02
 */
@Primary
public interface CountHeaderMapper1 extends BaseMapper<CountHeader> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param countHeader
	 * @return
	 */
	List<CountHeader> selectCountHeaderPage(IPage page, CountHeaderVO countHeader);

	@DataAuth(type = DataScopeEnum.OWN_DEPT_CHILD)
	CountHeader getCountHeaderByNo(String billNo);

}
