export const group = {
    column: [
        {
            label: "库房",
            prop: "whId",
            rules: [{
                required: true,
                message: "请选择库房",
                trigger: "blur"
            }],
            type: 'select',
            dicUrl: "/api/wms/warehouse/warehouse/list",
            props: {
                label: "whName",
                value: "whId"
            },
        },
        {
            label: "任务",
            prop: "crontabTaskId",
            rules: [{
                required: true,
                message: "请选择任务",
                trigger: "blur"
            }],
            type: 'select',
            props: {
                label: 'crontabTaskName',
                value: 'id'
            },
            dicUrl: '/api/wms/crontab/task/list'
        },
        {
            label: "年",
            prop: "years",
            maxlength: 4,
            type: 'inputNumber',
            rules: [{
                required: true,
                message: "请输入年",
                trigger: "blur"
            }],
            min: -1,
            default: -1
        },
        {
            label: "月",
            prop: "months",
            type: 'inputNumber',
            rules: [{
                required: true,
                message: "请输入月",
                trigger: "blur"
            }],
            min: -1,
            default: -1
        },
        {
            label: "日",
            prop: "days",
            type: 'inputNumber',
            rules: [{
                required: true,
                message: "请输入日",
                trigger: "blur"
            }],
            min: -1,
            default: -1
        },
        {
            label: "周",
            prop: "weekdays",
            type: 'inputNumber',
            rules: [{
                required: true,
                message: "请输入周,0表示星期天，1表示周一，-1表示忽略",
                trigger: "blur"
            }],
            min: -1,
            default: -1
        },
        // {
        //     label: "时",
        //     prop: "hours",
        //     type: 'inputNumber',
        //     rules: [{
        //         required: true,
        //         message: "请输入时",
        //         trigger: "blur"
        //     }],
        //     min: -1,
        //     default: -1
        // },
        // {
        //     label: "分",
        //     prop: "minutes",
        //     type: 'inputNumber',
        //     rules: [{
        //         required: true,
        //         message: "请输入分",
        //         trigger: "blur"
        //     }],
        //     min: -1,
        //     default: -1
        // },
        // {
        //     label: "秒",
        //     prop: "seconds",
        //     type: 'inputNumber',
        //     rules: [{
        //         required: true,
        //         message: "请输入秒",
        //         trigger: "blur"
        //     }],
        //     min: -1,
        //     default: -1
        // },
        {
            label: '是否启用',
            prop: 'status',
            type: 'radio',
            dicData: [{ label: '不启用', value: 0 }, { label: '启用', value: 1 }],
            default: 1
        }
    ]
}
