package org.nodes.wms.core.stock.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.stock.core.entity.Serial;
import org.nodes.wms.core.stock.core.vo.SerialVO;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 序列号 Mapper 接口
 *
 * @author zx
 * @since 2020-02-21
 */
@Primary
public interface SerialMapper extends BaseMapper<Serial> {
	/**
	 * 从库存中查询序列号
	 * @param serial
	 * @return
	 */
	List<SerialVO> selectSerialFromStock(String lpnCode, String serialNumber, Long skuId);
}
