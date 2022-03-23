export const group = {
    label: '明细',
    column: [{
        prop: "detailList",
        type: "dynamic",
        span: 24,
        children: [
            {
                prop: 'skuCode',
                label: '物品编码',
                width: 160
            },
            {
                prop: 'skuName',
                label: '物品名称',
                width: 200
            },
            {
                prop: 'lotNumber',
                label: '批次号',
                width: 180
            },
            {
                prop: 'wspName',
                label: '包装',
                width: 180
            },
            {
                prop: 'pickRealQty',
                label: '拣货量',
                width: 100
            },
            {
                prop: 'wsuName',
                label: '计量单位',
                width: 100
            },
            {
                prop: 'locCode',
                label: '库位编码',
                width: 120
            }
        ]
    }]
}
