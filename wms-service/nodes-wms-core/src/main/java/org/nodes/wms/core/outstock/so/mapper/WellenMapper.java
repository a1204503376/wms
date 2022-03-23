
package org.nodes.wms.core.outstock.so.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.outstock.so.entity.Wellen;
import org.springframework.context.annotation.Primary;

/**
 * 波次划分表 Mapper 接口
 *
 * @author pengwei
 * @since 2020-02-10
 */
@Primary
public interface WellenMapper extends BaseMapper<Wellen> {

	/**
	 * 通过编码查询波次
	 * @param wellenNo
	 * @return
	 */
	Wellen selectWellenListByNo(String wellenNo);
}
