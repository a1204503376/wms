export const group = {
    label:'基本信息',
    column:[
        {
            prop:"skuLotValName",
            label:"验证名称",
            rules:[{required: true, message: "验证名称不能为空", trigger: "blur", pattern:/\S/,}],
            maxlength:180
        },
        {
            prop:"skuLotRemark",
            label:"验证说明",
            maxlength:200
        },
        {
            prop: "woId",
            label: "货主",
            type: "select",
            dicUrl: "/api/wms/basedata/owner/list",
            defaultIndex: 0,
            props: {
                label: "ownerName",
                value: "woId"
            },
            rules: [{required: true, message: "货主不能为空", trigger: "blur"}],
        },
        {
            prop: "whId",
            label: "库房",
            type: "select",
            dicUrl: "/api/wms/warehouse/warehouse/list",
            props: {
                label: "whName",
                value: "whId"
            },
            rules: [{ required: true, message: "库房不能为空", trigger: "change" }],
        },
    ]
}
