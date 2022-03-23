package org.nodes.core.base.vo;

import lombok.Data;
import org.springblade.core.log.model.LogApi;

/**
 * @program: WmsCore
 * @description: API 日志VO类
 * @author: pengwei
 * @create: 2020-11-21 13:03
 **/
@Data
public class LogApiVo extends LogApi {

	private String strId;
}
