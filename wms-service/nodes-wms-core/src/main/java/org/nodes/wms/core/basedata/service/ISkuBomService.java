package org.nodes.wms.core.basedata.service;

import org.nodes.wms.core.basedata.entity.SkuBom;
import org.nodes.wms.core.basedata.vo.SkuBomVO;
import org.springblade.core.mp.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.tool.api.R;

/**
 * 物料清单 服务类
 *
 * @author NodeX
 * @since 2021-05-19
 */
public interface ISkuBomService<T> extends BaseService<SkuBom> {

	boolean saveOUpdate(SkuBom entity);

}
