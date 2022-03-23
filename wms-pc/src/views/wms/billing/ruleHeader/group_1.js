export const group = {
    label: '常规',
    column: [
        {
            label: "计费编码",
            prop: "billingCode",
            maxlength: 50,
            rules: [
                {required: true, message: "计费编码不能为空", trigger: "blur"}
            ],
        },
        {
            label: "计费名称",
            prop: "billingName",
            maxlength: 200,
            rules: [
                {required: true, message: "计费编码不能为空", trigger: "blur"}
            ],
        },
        {
            label: "所属库房",
            prop: "whId",
            type: 'select',
            dicUrl: '/api/wms/warehouse/warehouse/list',
            props: {
                label: 'whName',
                value: 'whId'
            },
            rules: [
                {required: true, message: "所属库房不能为空", trigger: "blur"}
            ],
        },
        {
            label: "备注",
            prop: "remark",
            type: 'textarea',
            span: 24,
            maxlength: 500
        },
    ]
}
