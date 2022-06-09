package org.nodes.wms.core.timed;

import org.springframework.stereotype.Component;

@Component
public class TaskDemo {

	public void  run(){
		System.out.println("1号定时任务执行中");
	}
	public void  run1(){
		System.out.println("2号定时任务执行中");
	}
	public void  run2(){
		System.out.println("3号定时任务执行中");
	}
	public void  run3(){
		System.out.println("4号定时任务执行中");

	}

}
