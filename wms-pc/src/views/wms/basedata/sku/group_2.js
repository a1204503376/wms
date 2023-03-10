export const group = {
    label:'保质期',
    column:[
        {
            prop:"skuTempUpperLimit",
            label:"温度上限",
            type:"inputNumber",
            hide:true
        },
        {
            prop:"skuTempLowerLimit",
            label:"温度下限",
            type:"inputNumber",
            hide:true
        },
        {
            prop:"qualityType",
            label:"保质期有无",
            type:"select",
            props:{label:"label",value:"value"},
            dicData:[
                {label:"有",value:"Y"},
                {label:"无",value:"N"},
            ],
            hide:true
        },
        {
            prop:"qualityDateType",
            label:"保质期类型",
            type:"select",
            hide:true,
            props:{label:"dictValue",value:"dictKey"},
            dicUrl: "/api/blade-system/dict/dictionary?code=quality_type",
            defaultIndex: 0,
        },
        {
            prop:"qualityHours",
            label:"保质期天数",
            type:"inputNumber",
        },
        {
            prop:"attribute3",
            label:"临期阈值(%)",
            type:"inputNumber",
            max:100,
            min:0
        },
        {
            prop:"qualityTransport",
            label:"几天内交货",
            type:"inputNumber",
            hide:true
        },
        {
            prop:"qualityBy",
            label:"几天内截至",
            type:"inputNumber",
            hide:true
        },
        {
            prop:"qualityBest",
            label:"几天内最佳",
            type:"inputNumber",
            hide:true
        },
    ],
}
