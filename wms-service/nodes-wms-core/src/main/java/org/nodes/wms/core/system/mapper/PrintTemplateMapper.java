package org.nodes.wms.core.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.system.entity.PrintTemplate;
import org.nodes.wms.core.system.vo.PrintTemplateVO;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 打印模板 Mapper 接口
 *
 * @author NodeX
 * @since 2020-04-21
 */
@Primary
public interface PrintTemplateMapper extends BaseMapper<PrintTemplate> {

	/**
	 * 打印模板自定义分页
	 *
	 * @param page
	 * @param printTemplate
	 * @return
	 */
	List<PrintTemplateVO> selectPrintTemplatePage(IPage page, PrintTemplateVO printTemplate);

}
