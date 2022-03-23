export const group = {
    label: "明细",
    column: [
        {
            prop: "detailList",
            type: "dynamic",
            span: 24,
            children: [
                {
                    prop: "billFieldName",
                    label: "订单字段",
                    type: "select",
                    // sortable: true,
                    width: 150,
                    dicUrl: '/api/blade-system/dict/dictionary?code=st_wellen_field',
                    props: {
                        label: 'dictValue',
                        value: 'dictKey'
                    },
                    rules: [{ required: true, message: "订单字段不能为空", trigger: "change" }],
                    change: (val, obj, scope, self) => {
                        let result = self.column[0].dicData.filter(item => {
                            return item.dictKey == val;
                        });
                        if (result && result.length > 0) {
                            let detail = result[0];
                            scope.row.billFieldName = detail.dictValue;
                            scope.row.billField = detail.dictKey;
                        }
                    }
                },
                {
                    prop: "operationName",
                    label: "操作符",
                    type: "select",
                    sortable: true,
                    dicUrl: "/api/blade-system/dict/dictionary?code=st_wellen_operation",
                    props: {
                        label: 'dictValue',
                        value: 'dictKey'
                    },
                    rules: [{ required: true, message: "操作符不能为空", trigger: "change" }],
                    change: (val, obj, scope, self) => {
                        let result = self.column[1].dicData.filter(item => {
                            return item.dictKey == val;
                        });
                        if (result && result.length > 0) {
                            let detail = result[0];
                            scope.row.operationName = detail.dictValue;
                            scope.row.operation = detail.dictKey;
                        }
                    }
                },
                {
                    prop: "value1",
                    label: "第一值",
                    type: "inputNumber",
                    width: 120,
                    min: 0,
                    rules: [{ type: "number", required: true, message: "换算倍率不能为空", trigger: "change,blur" }],
                },
                {
                    prop: "value2",
                    label: "第二值",
                    type: "inputNumber",
                    width: 120,
                    min: 0,
                    rules: [{ type: "number", required: true, message: "换算倍率不能为空", trigger: "change,blur" }],
                },
                {
                    prop: "criteriaName",
                    label: "和/或",
                    type: "select",
                    width: 150,
                    sortable: true,
                    dicUrl: "/api/blade-system/dict/dictionary?code=st_wellen_criteria",
                    props: {
                        label: 'dictValue',
                        value: 'dictKey'
                    },
                    rules: [{ required: true, message: "层级不能为空", trigger: "change" }],
                    change: (val, obj, scope, self) => {
                        let result = self.column[4].dicData.filter(item => {
                            return item.dictKey == val;
                        });
                        if (result && result.length > 0) {
                            let detail = result[0];
                            scope.row.criteriaName = detail.dictValue;
                            scope.row.criteria = detail.dictKey;
                        }
                    }
                },
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
