export const group = {
    label: "维数",
    column: [
        {
            prop: "locLength",
            label: "长度",
            type: 'number',
            append: 'mm',
        },
        {
            prop: "locWide",
            label: "宽度",
            type: 'number',
            append: 'mm',
        },
        {
            prop: "locHigh",
            label: "高度",
            type: 'number',
            append: 'mm',
        },
        {
            prop: "capacity",
            label: "容量",
            type: 'number',
            append: 'mm³',
        },
        {
            prop: "loadWeight",
            label: "载重量",
            type: 'number',
            append: 'kg',
        },
        {
            prop: "locLevel",
            label: "货架层",
            maxlength: 50
        },
        {
            prop: "itemNum",
            label: "最大件数",
            type: 'inputNumber'
        },
        {
            prop: "trayNum",
            label: "最大托数",
            type: 'inputNumber'
        }
    ]
}