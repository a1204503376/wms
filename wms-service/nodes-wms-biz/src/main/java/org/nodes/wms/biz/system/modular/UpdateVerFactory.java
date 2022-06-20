package org.nodes.wms.biz.system.modular;

import org.nodes.wms.dao.system.updateVer.dto.input.UpdateVerEditRequest;
import org.nodes.wms.dao.system.updateVer.entities.UpdateVer;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

/**
 * 系统版本工厂类
 **/
@Service
public class UpdateVerFactory {

	public UpdateVer createUpdateVer(UpdateVerEditRequest updateVerEditRequest){
		UpdateVer updateVer = new UpdateVer();
		Func.copy(updateVerEditRequest,updateVer);
		return updateVer;
	}
}
