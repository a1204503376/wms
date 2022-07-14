package com.nodes.project.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nodes.framework.web.domain.AjaxResult;
import com.nodes.project.api.domain.AgvException;
import com.nodes.project.api.dto.agv.AgvSyncExceptionRequest;
import com.nodes.project.api.mapper.AgvExceptionMapper;
import com.nodes.project.api.service.AgvExceptionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 针对表【agv_exception】的数据库操作Service实现
 */
@Service
public class AgvExceptionServiceImpl extends ServiceImpl<AgvExceptionMapper, AgvException>
        implements AgvExceptionService {

    @Override
    public AjaxResult syncException(AgvSyncExceptionRequest agvSyncExceptionRequest) {
        AgvException agvException = new AgvException();
        BeanUtils.copyProperties(agvSyncExceptionRequest, agvException);

        boolean flag = save(agvException);

        return flag
                ? AjaxResult.success()
                : AjaxResult.errorParams("更新AGV_EXCEPTION失败，参数：{}", agvSyncExceptionRequest);
    }
}




