export const group = {
    label: '装车明细',
    column: [
        {
            prop: "truckDetailList",
            type: "dynamic",
            span: 24,
            children: [
                {
                    label: '出库单编码',
                    prop: 'soBillNo',
                    width:180
                },
                {
                    label: '波次编码',
                    prop: 'wellenNo',
                    width:160
                },
                {
                    label: '容器编码',
                    prop: 'lpnCode',
                    width:120
                },
                {
                    label: '物品编码',
                    prop: 'skuCode',
                    width:160
                },
                {
                    label: '物品名称',
                    prop: 'skuName',
                    width:200
                },
                {
                    label: '数量',
                    prop: 'stockQty'
                },
                {
                    label: '计量单位',
                    prop: 'umName'
                },
                {
                    label: '批次号',
                    prop: 'lotNumber',
                    width:180
                }
            ]
        }
    ]
}
