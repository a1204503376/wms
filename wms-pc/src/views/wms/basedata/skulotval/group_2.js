export const group = {
    label: '批属性',
    column: [
        {
            prop: "skuLotList",
            type: "dynamic",
            span: 24,
            addBtn: false,
            delBtn: false,
            page: false,
            children: [
                {
                    label: "批属性名称",
                    prop: "skuLotValName",
                    width: "150px",
                    type: 'text'
                },
                {
                    label: "格式化规则",
                    prop: "skuLotMixMask",
                    width: "150px",
                    maxlength: 500
                },
                {
                    label: "生成掩码",
                    prop: "skuLotMask",
                    width: "150px",
                    show: ["skuLotMaskDesc"],
                    clearable: true,
                    type: "select",
                    dicUrl: "/api/blade-system/dict/dictionary?code=sku_lot_val",
                    props: {
                        label: "dictValue",
                        value: "dictKey"
                    },
                    change: function (val, obj, scope, self) {
                        if (obj) {
                            scope.row.skuLotMaskDesc = obj.dictValue
                        } else {
                            scope.row.skuLotMaskDesc = '';
                        }
                    }
                },
                {
                    label: "掩码生成规则",
                    width: "150px",
                    prop: "skuLotEditType",
                    show: ["skuLotEditDesc"],
                    clearable: true,
                    type: "select",
                    dicUrl: "/api/blade-system/dict/dictionary?code=sku_lot_val_ruler",
                    props: {
                        label: "dictValue",
                        value: "dictKey"
                    },
                    change: function (val, obj, scope, self) {
                        if (obj) {
                            scope.row.skuLotEditDesc = obj.dictValue
                        } else {
                            scope.row.skuLotEditDesc = '';
                        }
                    }
                },
                {
                    label: "长度上限",
                    prop: "skuLen",
                    width: "150px",
                    type: "inputNumber",
                    min: 0,
                    max: 99
                },
                {
                    label: "必填",
                    prop: "skuLotMust",
                    type: "switch",
                    width: "100px",
                    dicData: [
                        {
                            label: "是",
                            value: 1
                        },
                        {
                            label: "否",
                            value: 0
                        }
                    ],
                    default: 0
                },
                {
                    label: "可见",
                    prop: "skuLotDisp",
                    type: "switch",
                    width: "100px",
                    dicData: [
                        {
                            value: 1,
                            label: "是"
                        },
                        {
                            value: 0,
                            label: "否"
                        }
                    ],
                    default: 0
                },
                {
                    label: "允许混放",
                    prop: "skuLotMix",
                    type: "switch",
                    width: "100px",
                    dicData: [
                        {
                            value: 1,
                            label: "是"
                        },
                        {
                            value: 0,
                            label: "否"
                        }
                    ],
                    default: 1
                },
            ]
        }
    ]
}
