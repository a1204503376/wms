
package org.nodes.wms.core.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.nodes.wms.core.system.entity.UpdateVer;
import org.nodes.wms.core.system.mapper.UpdateVerMapper;
import org.nodes.wms.core.system.service.IUpdateVerService;
import org.nodes.wms.core.system.vo.UpdateVerVO;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *  服务实现类
 *
 * @author NodeX
 * @since 2020-02-20
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class UpdateVerServiceImpl<M extends UpdateVerMapper, T extends UpdateVer>
	extends ServiceImpl<UpdateVerMapper, UpdateVer>
	implements IUpdateVerService {

}
