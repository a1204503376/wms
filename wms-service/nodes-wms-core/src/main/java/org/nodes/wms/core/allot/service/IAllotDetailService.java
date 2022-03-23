
package org.nodes.wms.core.allot.service;

import org.nodes.wms.core.allot.entity.AllotDetail;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 调拨明细表 服务类
 *
 * @author Wangjw
 * @since 2020-02-10
 */
public interface IAllotDetailService extends BaseService<AllotDetail> {


	Boolean save(Long allotBillId, List<AllotDetail> detailList);

	Boolean update(Long allotBillId, List<AllotDetail> detailList);
}
