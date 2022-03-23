var visible = false;
export const setVisible = (val) => {
    visible = val;
}

export const group = {
    label: '差异报告',
    hide: function (type, form) {
        return type === 'view' && form && form.countReportVOList && form.countReportVOList.length > 0;
    },
    column: [
        {
            type: 'dynamic',
            prop: 'countReportVOList',
            span: 24,
            addBtn: false,
            delBtn: false,
            editBtn: false,
            page: false,
            selection: false,
            menu:false,
            children: [
                {
                    label: '序列号',
                    prop: 'serialNumber',
                    type: 'index'
                },
                {
                    prop: 'locCode',
                    label: '库位',
                },
                {
                    prop: 'lpnCode',
                    label: '容器编码',
                },
                {
                    prop: 'skuCode',
                    label: '物品编码',
                },
                {
                    prop: 'skuName',
                    label: '物品名称'
                },
                {
                    prop: 'wsuName',
                    label: '计量单位'
                },
                {
                    prop: 'systemLot',
                    label: '系统批次'
                },
                {
                    prop: 'wmsQty',
                    label: '系统数量'
                },
                {
                    prop: 'countLot',
                    label: '盘点批次'
                },
                {
                    prop: 'countQty',
                    label: '盘点数量'
                },
                {
                    prop: 'differentNum',
                    label: '差异数量'
                }
            ]
        }
    ]
}
