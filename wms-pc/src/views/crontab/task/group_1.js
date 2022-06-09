export const group = {
    column: [
        {
            label: "任务名",
            prop: "crontabTaskName",
            rules: [{
                required: true,
                message: "请输入任务名",
                trigger: "blur"
            }],
            maxlength: 200
        },
        {
            label: "url",
            prop: "url",
            rules: [{
                required: true,
                message: "请输入url",
                trigger: "blur"
            }],
            maxlength: 200
        },
        {
            label: "请求方法",
            prop: "method",
            maxlength: 50,
            rules: [{
                required: true,
                message: "请输入请求方法",
                trigger: "blur"
            }],
        },
        {
            label: "cron表达式",
            prop: "cron",
            maxlength: 2000,
            rules: [{
                required: true,
                message: "请输入cron表达式",
                trigger: "blur"
            }],
        },
    ]
}
