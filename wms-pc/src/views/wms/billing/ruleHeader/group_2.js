export const group = {
    label: '明细',
    column: [
        {
            prop: 'detailList',
            type: 'dynamic',
            span: 24,
            children: [
                {
                    label: '行号',
                    prop: 'lineNo',
                    type: 'text',
                    width: 50,
                    default: function (data) {
                        let lastLineNo = 1;
                        if (data.detailList && data.detailList.length > 0) {
                            // 找到明细中最大的行号
                            let maxNo = 0;
                            data.detailList.forEach(item => {
                                let no = parseInt(item.lineNo);
                                if (no > maxNo) {
                                    maxNo = no;
                                }
                            });
                            lastLineNo = maxNo + 1;
                        }
                        return ("00000000" + lastLineNo).substr(-8);
                    }
                },
                {
                    label: '计费项目',
                    prop: 'itemId',
                    type: 'select',
                    dicUrl: '/api/wms/billing/item/list',
                    props: {
                        label: 'name',
                        value: 'id'
                    },
                    show: ['itemName'],
                    rules: [
                        {required: true, message: "计费项目不能为空", trigger: "change"}
                    ],
                    change: function (val, obj, scope) {
                        if (obj) {
                            scope.row.itemCode = obj.code;
                            scope.row.itemName = obj.name;
                        } else {
                            scope.row.itemCode = null;
                            scope.row.itemName = null;
                        }
                    }
                },
                {
                    label: '金额',
                    prop: 'amount',
                    type: 'inputNumber',
                    rules: [
                        {type: 'number', required: true, message: "金额不能为空", trigger: "blur"}
                    ],
                },
                {
                    label: '计量单位',
                    prop: 'wsuId',
                    type: 'select',
                    dicUrl: '/api/wms/basedata/sku-um/list',
                    props: {
                        label: 'wsuName',
                        value: 'wsuId'
                    },
                    show: ['wsuName'],
                    rules: [
                        {required: true, message: "计量单位不能为空", trigger: "change"}
                    ],
                    change: function (val, obj, scope) {
                        if (obj) {
                            scope.row.wsuCode = obj.wsuCode;
                            scope.row.wsuName = obj.wsuName;
                        } else {
                            scope.row.wsuCode = null;
                            scope.row.wsuName = null;
                        }
                    }
                }
            ]
        }
    ]
}
