export const group = {
    column: [
        {
            label: '协议号',
            prop: 'agreementNo',
            maxlength: 50,
            rules: [
                {required: true, message: "协议号不能为空", trigger: "blur"}
            ],
        },
        {
            label: '描述',
            prop: 'agreementDesc',
            maxlength: 200,
        },
        {
            label: '货主',
            prop: 'woId',
            type: 'select',
            props: {
                label: 'ownerName',
                value: 'woId'
            },
            dicUrl: '/api/wms/basedata/owner/list',
            rules: [
                {required: true, message: "货主不能为空", trigger: "blur"}
            ],
        },
        {
            label: '计费规则',
            prop: 'ruleHeaderId',
            type: 'select',
            props: {
                label: 'billingName',
                value: 'id'
            },
            dicUrl: '/api/wms/billing/rule-header/list',
            rules: [
                {required: true, message: "计费规则不能为空", trigger: "blur"}
            ],
            show:['ruleHeaderName']
        },
        {
            label: '生效日期',
            prop: 'effectiveDate',
            type: 'date'
        },
        {
            label: '解约日期',
            prop: 'terminationDate',
            type: 'date'
        },
        {
            label: '税率',
            prop: 'taxRate',
            type: 'inputNumber',
            precision: 2,
            min: 0
        },
        {
            label: '折扣',
            prop: 'discount',
            type: 'inputNumber',
            precision: 2,
            min: 0
        },
        {
            label: '备注',
            prop: 'remark',
            span: 24,
            type: 'textarea'
        },
        {
            label: '自动续约',
            prop: 'isAuto',
            type: 'check',
            text: '自动续约',
            offset: 16,
            default:true,
            show:['isAutoName']
        }
    ]
}
