
package org.nodes.wms.core.outstock.so.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.outstock.so.entity.WellenDetail;
import org.nodes.wms.core.outstock.so.vo.WellenSoHeaderVo;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 波次划分明细表 Mapper 接口
 *
 * @author pengwei
 * @since 2020-02-10
 */
@Primary
public interface WellenDetailMapper extends BaseMapper<WellenDetail> {
	List<WellenSoHeaderVo> getSoHeaderByWellenIds(List<Long> wellenIds);
}
