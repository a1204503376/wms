package com.nodes.project.api.dto.agv;

import com.nodes.common.constant.AgvConstants;
import lombok.Data;

import java.util.Objects;

/**
 * AGV非主流返回对象
 */
@Data
public class AgvOtherResponse {
    /**
     * 结果
     * 200：成功，其他：失败
     */
    private String code;
    /**
     * 提示信息
     */
    private String msg;

    public boolean isSuccess() {
        return Objects.equals(code, AgvConstants.API_OTHER_RESULT_SUCCESS);
    }

    public boolean isFailed(AgvOtherResponse agvResponse) {
        return !Objects.equals(code, AgvConstants.API_OTHER_RESULT_SUCCESS);
    }
}
