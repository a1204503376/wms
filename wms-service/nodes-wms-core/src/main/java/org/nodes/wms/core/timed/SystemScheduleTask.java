package org.nodes.wms.core.timed;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.nodes.core.enums.StatusEnum;
import org.nodes.wms.core.common.service.IContactsService;
import org.nodes.wms.core.crontab.cache.CrontabTaskCache;
import org.nodes.wms.core.crontab.cache.SchemeCache;
import org.nodes.wms.core.crontab.entity.CrontabTask;
import org.nodes.wms.core.crontab.entity.Scheme;
import org.nodes.wms.core.crontab.service.ICrontabTaskService;
import org.nodes.wms.core.crontab.service.ISchemeService;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.nodes.wms.core.crontab.cache.SchemeCache.SCHEME_CACHE;

/**
 * author: pengwei
 * date: 2021/6/28 10:06
 * description: SystemScheduleTask
 */
@Data
@Slf4j
@Configuration
public class SystemScheduleTask implements SchedulingConfigurer {

	@Override
	public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
//		scheduledTaskRegistrar.addTriggerTask(
//			() -> doBiz(),
//			triggerContext -> {
//				return new CronTrigger(this.getCron()).nextExecutionTime(triggerContext);
//			}
//		);
	}

	/**
	 * 默认：* * * * * ?
	 *
	 * @return cron * * * * * ?
	 */
	protected String getCron() {
		return "* * * * * ?";
	}

	/**
	 * 执行定时任务逻辑
	 */
	protected void doBiz() {
		ISchemeService schemeService = SpringUtil.getBean(ISchemeService.class);
		List<Scheme> schemeList = SchemeCache.list().stream().filter(u->{
			return Func.equals(u.getStatus(), StatusEnum.ON.getIndex());
		}).collect(Collectors.toList());
		if (Func.isEmpty(schemeList)) {
			return;
		}
		List<CrontabTask> taskList = CrontabTaskCache.list();
		if (Func.isEmpty(taskList)) {
			return;
		}
		LocalDateTime now = LocalDateTime.now();
		boolean has_update = false;
		for (Scheme scheme : schemeList) {
			// 找到关联的任务
			CrontabTask task = taskList.stream().filter(u -> {
				return u.getId().equals(scheme.getCrontabTaskId());
			}).findFirst().orElse(null);
			if (Func.isEmpty(task) || Func.isEmpty(task.getUrl())) {
				continue;
			}
			if (Func.isNotEmpty(scheme.getLastExecTime()) &&
				LocalDateTime.now().getDayOfYear() > scheme.getLastExecTime().getDayOfYear() + scheme.getDays()) {
				// 未达到执行时间
				continue;
			}
			if (Func.isNotEmpty(scheme.getMustExecTimes())) {
				if (scheme.getMustExecTimes() <= scheme.getTodayExecTimes()) {
					continue;
				}
			}
			try {
				Class clazz = Class.forName(task.getUrl());
				Method[] methods = clazz.getMethods();
				if (Func.isEmpty(methods)) {
					continue;
				}
				for (Method method : methods) {
					if (!method.getName().equals(task.getHttpMethod())) {
						continue;
					}
					List<String> paramList = new ArrayList(Func.toStrList(task.getParams()));
					paramList.add(0, String.valueOf(scheme.getWhId()));
					paramList.add(1, String.valueOf(now.plusDays(0 - scheme.getDays())));
					Object resultObj = method.invoke(SpringUtil.getBean(clazz), paramList.toArray());
					if (Func.toBoolean(resultObj, false)) {
						scheme.setTodayExecTimes(scheme.getTodayExecTimes() + 1);
						scheme.setLastExecTime(LocalDateTime.now());
						schemeService.updateById(scheme);
						has_update = true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error(String.valueOf(e.getStackTrace()));
			}
		}
		if (has_update) {
			CacheUtil.clear(SCHEME_CACHE);
		}
	}
}
