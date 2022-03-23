export const group = {
    label: "地址",
    column: [
        {
            prop: "addressList",
            type: "dynamic",
            span: 24,
            children: [
                {
                    prop: "addressType",
                    label: "地址类型",
                    type: "select",
                    dicUrl: "/api/blade-system/dict/dictionary?code=address_type",
                    show: ['addressTypeDesc'],
                    width: 200,
                    props: {
                        label: "dictValue",
                        value: "dictKey"
                    },
                    rules: [{type: "number", required: true, message: "请选择地址类型", trigger: "change"}],
                    change: (val, obj, scope, self) => {
                        if (obj) {
                            scope.row.addressType = obj.dictKey;
                            scope.row.addressTypeDesc = obj.dictValue;
                        }
                    },
                },
                {
                    prop: "address",
                    label: "详细地址",
                    rules: [{required: true, message: "请输入详细地址", trigger: "blur"}],
                    maxlength: 1000
                },
                {
                    prop: "longitude",
                    label: "经度",
                    type: "inputNumber",
                    width: 140
                },
                {
                    prop: "latitude",
                    label: "纬度",
                    type: "inputNumber",
                    width: 140
                },
                {
                    prop: "defaultFlag",
                    label: "默认",
                    type: "switch",
                    width: 120,
                    show: ['defaultFlagBooleanDesc'],
                    dicData: [
                        {label: "是", value: 1},
                        {label: "否", value: 0}
                    ],
                    default:function(data){
                        if (!data || data.length == 0) {
                            return 1;
                        } else {
                            let defaultRow = data.find(u=>u.defaultFlag== 1);
                            if (defaultRow) {
                                return 0;
                            } else {
                                return 1;
                            }
                        }
                    },
                    change:function(val, obj, scope, self) {
                        if (val) {
                            self.table.tableData.forEach(row=>{
                                row.defaultFlag = scope.row.$id == row.$id ? 1 : 0;
                            });
                        }
                    }
                }
            ],
            delete: (form, row) => {
                if (!form['addressDeletedList']) {
                    form['addressDeletedList'] = [];
                }
                form['addressDeletedList'].push(row);
            },
            saved: (row, data, index) => {
                let tableData = data;
                if (row.defaultFlag == '0') {

                } else {
                    tableData.forEach((v, i) => {
                        if (i != index) {
                            v.defaultFlag = '0'
                        }
                    })
                }
            }
        }
    ]
}
