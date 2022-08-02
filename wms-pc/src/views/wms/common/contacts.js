export const group = {
    label: "联系人",
    column: [
        {
            prop: "contactsList",
            type: "dynamic",
            span: 24,
            children: [
                {
                    prop: "contactsName",
                    label: "联系人",
                    width: 200,
                    maxlength: 200,
                    rules: [{required: true, message: "联系人不能为空", trigger: "blur"}]
                },
                {
                    prop: "contactsEmail",
                    label: "邮箱",
                    maxlength: 200,
                    width: 200,
                    rules: [
                        {
                            pattern: /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/,
                        }
                    ]
                },
                {
                    prop: "contactsNumber",
                    label: "电话",
                    maxlength: 200,
                    width: 200,
                    rules: [
                        {
                            required: true,
                            pattern: /^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/,
                            trigger: 'blur',
                            message: '请输入正确电话'
                        }
                    ]
                },
                {
                    prop: "contactsFax",
                    label: "传真",
                    maxlength: 200,
                    rules: [
                        {
                            pattern: /^(\d{3,4}-)?\d{7,8}$/,
                        }
                    ]
                },
                {
                    prop: "defaultFlag",
                    label: "默认",
                    type: "switch",
                    width: 120,
                    dicData: [
                        {label: "是", value: 1},
                        {label: "否", value: 0}
                    ],
                    default: function (data) {
                        if (!data || data.length == 0) {
                            return 1;
                        } else {
                            let defaultRow = data.find(u => u.defaultFlag == 1);
                            if (defaultRow) {
                                return 0;
                            } else {
                                return 1;
                            }
                        }
                    },
                    change: function (val, obj, scope, self) {
                        if (val) {
                            self.table.tableData.forEach(row => {
                                row.defaultFlag = scope.row.$id == row.$id ? 1 : 0;
                            });
                        }
                    },
                    show: ['defaultFlagBooleanDesc']
                }
            ],
            delete: (form, row) => {
                if (!form['contactsDeletedList']) {
                    form['contactsDeletedList'] = [];
                }
                form['contactsDeletedList'].push(row);
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
