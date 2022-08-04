package org.nodes.wms.core.timed;


import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.nodes.wms.dao.crontab.entity.CrontabTask;
import org.nodes.wms.biz.crontab.ICrontabTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * 定时任务控制器
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL +"task")
public class SysTaskController implements SchedulingConfigurer {

	protected static Logger logger = LoggerFactory.getLogger(SysTaskController.class);

	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

	private static Map<Long,ScheduledFuture<?>> scheduledFutureMap = new HashMap<>();


	private  final  ICrontabTaskService crontabTaskService;

	private final LogBiz logBiz;
	//从数据库里取得所有要执行的定时任务
	private List<CrontabTask> getAllTasks() throws Exception {
		return crontabTaskService.getCrontabTaskList();
	}
	static {
		threadPoolTaskScheduler.initialize();
	}
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		List<CrontabTask> tasks= null;
		try {
			tasks = getAllTasks();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("定时任务启动,预计启动任务数量="+tasks.size()+"; time="+sdf.format(new Date()));

		//校验数据
		checkDataList(tasks);

		//通过校验的数据执行定时任务
		int count = 0;
		if(tasks.size()>0) {
			for (CrontabTask task:tasks) {
				try {
					//taskRegistrar.addTriggerTask(getRunnable(task), getTrigger(task));
					start(task);
					count++;
				} catch (Exception e) {
					logger.error("定时任务启动错误:" + task.getUrl() + ";" + task.getMethod() + ";" + e.getMessage());
				}
			}
		}
		logger.info("定时任务实际启动数量="+count+"; time="+sdf.format(new Date()));
	}
	private  Runnable getRunnable(CrontabTask task){
		return new Runnable() {
			@Override
			public void run() {
				try {
					Object obj = SpringUtil.getBean(Class.forName(task.getUrl()));
					Method method = obj.getClass().getMethod(task.getMethod());
					method.invoke(obj);
				} catch (InvocationTargetException e) {
					logger.error("定时任务启动错误，反射异常:"+task.getUrl()+";"+task.getMethod()+";"+ e.getMessage());
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		};
	}


	private  Trigger getTrigger(CrontabTask task){
		return new Trigger() {
			@Override
			public Date nextExecutionTime(TriggerContext triggerContext) {
				//将Cron 0/1 * * * * ? 输入取得下一次执行的时间
				CronTrigger trigger = new CronTrigger(task.getCron());
				Date nextExec = trigger.nextExecutionTime(triggerContext);
				return nextExec;
			}
		};

	}

	private List<CrontabTask> checkDataList(List<CrontabTask> list) {
		String errMsg="";
		for(int i=0;i<list.size();i++){
			if(!checkOneData(list.get(i)).equalsIgnoreCase("success")){
				errMsg+=list.get(i).getCrontabTaskName()+";";
				list.remove(list.get(i));
				i--;
			};
		}
		if(!StringUtils.isBlank(errMsg)){
			errMsg="未启动的任务:"+errMsg;
			logger.error(errMsg);
		}
		return list;
	}

	public  String checkOneData(CrontabTask task){
		String result="success";
		Class cal= null;
		try {
			cal = Class.forName(task.getUrl());
			Object obj = SpringUtil.getBean(cal);
			Method method = obj.getClass().getMethod(task.getMethod());
			String cron=task.getCron();
			if(StringUtils.isBlank(cron)){
				result="定时任务启动错误，无cron:"+task.getCrontabTaskName();
				logger.error(result);
				throw new ServiceException(result);
			}
		} catch (ClassNotFoundException e) {
			result="定时任务启动错误，找不到类:"+task.getUrl()+ e.getMessage();
			logger.error(result);
			throw new ClassCastException(result);
		} catch (NoSuchMethodException e) {
			result="定时任务启动错误，找不到方法,方法必须是public:"+task.getUrl()+";"+task.getMethod()+";"+ e.getMessage();
			logger.error(result);
			throw new ServiceException(result);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}
	/**
	 * 启动定时任务
	 * @param task
	 */
	@PostMapping("/start")
	public  void start(@RequestBody  CrontabTask task){
		if(task.getEnabled() == 0) {
			checkOneData(task);
			crontabTaskService.startCrontabTask(task.getId());
		}
		ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.schedule(getRunnable(task),getTrigger(task));
		scheduledFutureMap.put(task.getId(),scheduledFuture);
		logger.info("启动定时任务" + task.getId() );
		logBiz.auditLog(AuditLogType.CRON_TASK,task.getId(),"启动任务");
	}

	/**
	 * 取消定时任务
	 * @param
	 */
	@GetMapping("/cancel")
	public void cancel(Long id){
		ScheduledFuture<?> scheduledFuture = scheduledFutureMap.get(id);
		if(scheduledFuture != null && !scheduledFuture.isCancelled()){
			scheduledFuture.cancel(Boolean.FALSE);
		}
		scheduledFutureMap.remove(id);
		crontabTaskService.stopCrontabTask(id);
		logBiz.auditLog(AuditLogType.CRON_TASK,id,"取消任务");
	}

	/**
	 * 执行一次
	 * @param task
	 */
	@PostMapping("/execute")
	public void execute(@RequestBody  CrontabTask task){
		try {
			Object obj = SpringUtil.getBean(Class.forName(task.getUrl()));
			Method method = obj.getClass().getMethod(task.getMethod());
			method.invoke(obj);
			logBiz.auditLog(AuditLogType.CRON_TASK,task.getId(),"执行一次");
		} catch (InvocationTargetException e) {
			logger.error("定时任务执行错误，反射异常:"+task.getUrl()+";"+task.getMethod()+";"+ e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}



}


