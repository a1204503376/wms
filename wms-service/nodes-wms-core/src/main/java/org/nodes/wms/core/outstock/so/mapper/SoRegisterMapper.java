package org.nodes.wms.core.outstock.so.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.outstock.so.entity.SoRegister;
import org.nodes.wms.core.outstock.so.vo.SoRegisterVO;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 装车登记 Mapper 接口
 *
 * @author zhongls
 * @since 2020-05-07
 */
@Primary
public interface SoRegisterMapper extends BaseMapper<SoRegister> {

	/**
	 * 装车登记自定义分页
	 *
	 * @param page
	 * @param register
	 * @return
	 */
	List<SoRegisterVO> selectRegisterPage(IPage page, SoRegisterVO register);

}
