package tech.powerjob.worker.common.constants;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

/**
 * 任务状态，task_info 表中 status 字段的枚举值
 *
 * @author tjq
 * @since 2020/3/17
 */
@Getter
@AllArgsConstructor
public enum TaskStatus {

    WAITING_DISPATCH(1, "等待调度器调度"),
    DISPATCH_SUCCESS_WORKER_UNCHECK(2, "调度成功（但不保证worker收到）"),
    WORKER_RECEIVED(3, "worker接收成功，但未开始执行"),
    WORKER_PROCESSING(4, "worker正在执行"),
    WORKER_PROCESS_FAILED(5, "worker执行失败"),
    WORKER_PROCESS_SUCCESS(6, "worker执行成功");

    public static final Set<Integer> finishedStatus = Sets.newHashSet(WORKER_PROCESS_FAILED.value, WORKER_PROCESS_SUCCESS.value);

    private final int value;
    private final String des;

    public static TaskStatus of(int v) {
        for (TaskStatus taskStatus : values()) {
            if (v == taskStatus.value) {
                return taskStatus;
            }
        }
        throw new IllegalArgumentException("no TaskStatus match the value of " + v);
    }
}
