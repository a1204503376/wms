package com.nodes.project.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 供WMS调用
 *
 * @author dml
 */
@RestController
@RequestMapping("/wms")
public class WmsController {

    // 下发JOB
    // 取消JOB
    // 终止JOB


    // wms提供:
    // 回传状态的接口
    // 任务开始
    // 任务结束(正常)
    // 任务异常：返回AGV名称

    // 本系统提供给AGV的接口：
    // 订单开始
    // 订单结束(正常)
    // 订单异常：返回AGV名称

}
