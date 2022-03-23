export const group = {
    label: '基础信息',
    column: [
        {
            label: "策略编码",
            prop: "ssiCode",
            maxlength:50,
            rules: [
                {required: true, message: " ", trigger: "blur", pattern:/\S/,}
            ],
        },
        {
            label: "策略名称",
            prop: "ssiName",
            maxlength:200,
            rules: [
                {required: true, message: " ", trigger: "blur", pattern:/\S/,}
            ],
        },
        {
            label: "库房名称",
            prop: "whId",
            type: "select",
            props: {
                label: 'whName',
                value: 'whId',
            },
            dicUrl: "/api/wms/warehouse/warehouse/list",
            defaultIndex: 0,
            rules: [{required: true, message: "库房不能为空", trigger: "change, blur"}],
            disabled: function (data) {
                return data && data.instockDetailList && data.instockDetailList.length > 0;
            }
        }
    ]
}
