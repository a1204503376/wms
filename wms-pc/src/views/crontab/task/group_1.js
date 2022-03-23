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
            label: "APP ID",
            prop: "appid",
            rules: [{
                required: true,
                message: "请输入0为外部调用",
                trigger: "blur"
            }]
        },
        {
            label: "url",
            prop: "url",
            rules: [{
                required: true,
                message: "请输入url,如果是内部调用，请填写path即可，http_method=0时，请填写绝对路径",
                trigger: "blur"
            }],
            maxlength: 200
        },
        {
            label: "请求方法",
            prop: "httpMethod",
            maxlength: 50
        },
        {
            label: "请求参数",
            prop: "params",
            maxlength: 2000
        },
    ]
}
