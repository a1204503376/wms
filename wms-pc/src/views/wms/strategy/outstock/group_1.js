export const group = {
    label: '基础信息',
    column: [
        {
            label: "策略编码",
            prop: "ssoCode",
            maxlength: 50,
            rules: [
                {required: true, message: "策略编码不能为空", trigger: "blur", pattern: /\S/,}
            ],
        },
        {
            label: "策略名称",
            prop: "ssoName",
            maxlength: 200,
            rules: [
                {required: true, message: "策略名称不能为空", trigger: "blur", pattern: /\S/,}
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
                return data && data.outstockDetailVO && data.outstockDetailVO.length > 0;
            }
        }
    ]
}
