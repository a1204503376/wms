
package org.nodes.wms.core.count.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.count.entity.CountRecord;
import org.nodes.wms.core.count.vo.CountRecordVO;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 盘点单记录表 Mapper 接口
 *
 * @author chz
 * @since 2020-02-20
 */
@Primary
public interface CountRecordMapper1 extends BaseMapper<CountRecord> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param countRecord
	 * @return
	 */
	List<CountRecordVO> selectCountRecordPage(IPage page, CountRecordVO countRecord);

}
