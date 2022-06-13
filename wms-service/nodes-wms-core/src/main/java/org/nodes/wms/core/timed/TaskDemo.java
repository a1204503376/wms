package org.nodes.wms.core.timed;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.crontab.ICrontabTaskService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskDemo {
	private final  ICrontabTaskService crontabTaskService;

	public void  run(){
		System.out.println("1号定时任务执行中");
		//  添加任务日志
		crontabTaskService.logOfCrontabTask("任务一","系统任务执行中");
	}
	public void  run1(){
		System.out.println("2号定时任务执行中");
	}
	public void  run2(){
		System.out.println("3号定时任务执行中");
		//  添加任务日志
		crontabTaskService.logOfCrontabTask("任务二","系统任务执行中");
	}
	public void  run3(){
		System.out.println("4号定时任务执行中");
		//  添加任务日志
		crontabTaskService.logOfCrontabTask("任务三","系统任务执行中");
	}

}
