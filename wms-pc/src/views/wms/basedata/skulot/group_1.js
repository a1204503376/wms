export const group = {
    label:'基本信息',
    column:[
        {
            prop:"skuLotCode",
            label:"编码",
            disabled:false,   //是否置灰
            rules:[{required: true, message: "批属性编码不能为空", trigger: "blur", pattern:/\S/,}],
            maxlength:50
        },
        {
            prop:"skuLotName",
            label:"名称",
            disabled:false,   //是否置灰
            rules:[{required: true, message: "批属性编码不能为空", trigger: "blur", pattern:/\S/,}],
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
            prop:"remark",
            label:"备注",
            span:16,
            maxlength:300
        },
    ]
}
