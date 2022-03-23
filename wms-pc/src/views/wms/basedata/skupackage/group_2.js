export const group = {
    label: "明细",
    column: [
        {
            prop: "skuPackageDetailVOList",
            type: "dynamic",
            span: 24,
            children: [
                {
                    prop: "wsuName",
                    label: "计量单位",
                    type: "select",
                    sortable: true,
                    width: 150,
                    dicUrl: "/api/wms/basedata/sku-um/list",
                    props: {
                        label: "wsuName",
                        value: "wsuId"
                    },
                    rules: [{ required: true, message: "计量单位不能为空", trigger: "change" }],
                    change: (val, obj, scope, self) => {
                        if (obj) {
                            scope.row.wsuName = obj.wsuName;
                            scope.row.wsuCode = obj.wsuCode;
                            scope.row.wsuId = obj.wsuId;
                        }
                        // 查找明细中，是否存在基础计量单位
                        let baseDetail = null;
                        if (self.table.data) {
                            baseDetail = self.table.data.find(u => {
                                return u.convertQty === 1;
                            });
                        }
                        if (baseDetail) {
                            scope.row.skuSpec = `1${scope.row.wsuName}-1${baseDetail.wsuName}`;
                        } else {
                            scope.row.skuSpec = `1${scope.row.wsuName}-1${scope.row.wsuName}`;
                        }
                    }
                },
                {
                    prop: "skuLevelName",
                    label: "层级",
                    type: "select",
                    width: 150,
                    sortable: true,
                    dicUrl: "/api/blade-system/dict/dictionary?code=sku_level",
                    props: {
                        label: "dictValue",
                        value: "dictKey"
                    },
                    show:['skuLevelName'],
                    rules: [{ required: true, message: "层级不能为空", trigger: "change" }],
                    change: (val, obj, scope, self) => {
                        if (obj) {
                            scope.row.skuLevelName = obj.dictValue;
                            scope.row.skuLevel = obj.dictKey;
                        }
                    }
                },
                {
                    prop: "convertQty",
                    label: "换算倍率",
                    type: "inputNumber",
                    width: 120,
                    min: 1,
                    rules: [{ type: "number", required: true, message: "换算倍率不能为空", trigger: "change,blur" }],
                    change: (val, obj, scope, self) => {
                        // 查找明细中，是否存在基础计量单位
                        let baseDetail = null;
                        if (self.table.data) {
                            baseDetail = self.table.data.find(u => {
                                return u.convertQty === 1;
                            });
                        }
                        if (baseDetail) {
                            scope.row.skuSpec = `1${scope.row.wsuName}-${val}${baseDetail.wsuName}`;
                        } else {
                            scope.row.skuSpec = `1${scope.row.wsuName}-${val}${scope.row.wsuName}`;
                        }
                    }
                },
                {
                    prop: "skuSpec",
                    label: "规格",
                    type: "text",
                    width: 100
                },
                {
                    prop: "lpnLength",
                    label: "长度(cm)",
                    type: "inputNumber",
                    width: 120,
                },
                {
                    prop: "lpnWidth",
                    label: "宽度(cm)",
                    type: "inputNumber",
                    width: 120,
                },
                {
                    prop: "lpnHeight",
                    label: "高度(cm)",
                    type: "inputNumber",
                    width: 120,
                },
                {
                    prop: "lpnWeight",
                    label: "重量(kg)",
                    type: "inputNumber",
                    width: 120,
                },
                {
                    prop: "filterValue",
                    label: "RFID筛选值",
                    width: 120,
                    maxlength: 50
                },
                {
                    prop: "indicatorDigit",
                    label: "RFID位数",
                    type: "inputNumber",
                    width: 120,
                },
                // {
                //     prop: 'attribute1',
                //     label: '箱号',
                //     width: 120
                // },
                {
                    prop: 'attribute2',
                    label: '皮重',
                    type: 'inputNumber',
                    width: 120
                },
                {
                    prop: 'attribute3',
                    label: '总重',
                    type: 'inputNumber',
                    width: 120
                }
            ],
            delete: (form, row) => {
                if (!form['pageDeletedList']) {
                    form['pageDeletedList'] = [];
                }
                form['pageDeletedList'].push(row);
            }
        }
    ]
}
