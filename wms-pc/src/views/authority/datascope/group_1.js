export const group = {
    label: '',
    column: [
        {
            prop: "dataScopeList",
            type: "dynamic",
            span: 24,
            moveBtn: false,
            editBtn:true,
            children: [
                {
                    prop: "scopeName",
                    label: "权限名称",
                    maxlength: 255,
                },
                {
                    prop: "resourceCode",
                    label: "权限编号",
                    maxlength: 255,
                },
                {
                    prop: "scopeColumn",
                    label: "权限字段",
                    maxlength: 255,
                },
                {
                    prop: "scopeType",
                    label: "规则类型",
                    type: "select",
                    props: {
                        label: "dictValue",
                        value: "dictKey"
                    },
                    show: ['scopeTypeName'],
                    rules: [{
                        type: 'number',
                        required: true,
                        message: "请输入通知类型",
                        trigger: "blur"
                    }],
                },
                {
                    prop: "scopeField",
                    label: "可见字段",
                    maxlength: 255,
                },
                {
                    prop: "scopeClass",
                    label: "权限类名",
                    maxlength: 500,
                },
                {
                    prop: "scopeValue",
                    label: "规则值",
                    maxlength: 2000,
                },
                {
                    prop: "remark",
                    label: "备注",
                    maxlength: 255,
                }
            ],
            column: [
                {
                    label: '',
                    column: [
                        {
                            prop: "scopeName",
                            label: "权限名称",
                            maxlength: 255,
                            rules: [{
                                required: true,
                                message: "请输入数据权限名称",
                                trigger: "blur"
                            }]
                        },
                        {
                            prop: "resourceCode",
                            label: "权限编号",
                            maxlength: 255,
                            rules: [{
                                required: true,
                                message: "请输入数据权限编号",
                                trigger: "blur"
                            }]
                        },
                        {
                            prop: "scopeColumn",
                            label: "权限字段",
                            maxlength: 255,
                            rules: [{
                                required: true,
                                message: "请输入数据权限编号",
                                trigger: "blur"
                            }]
                        },
                        {
                            prop: "scopeType",
                            label: "规则类型",
                            type: "select",
                            dicUrl: "/api/blade-system/dict/dictionary?code=data_scope_type",
                            props: {
                                label: "dictValue",
                                value: "dictKey"
                            },
                            show: ['scopeTypeName'],
                            rules: [{
                                type: 'number',
                                required: true,
                                message: "请输入通知类型",
                                trigger: "blur"
                            }],
                            change: function (val, obj, scope, self) {
                                if (obj) {
                                    self.scopeTypeName = obj.dictValue;
                                } else {
                                    self.scopeTypeName = "";
                                }
                            }
                        },
                        {
                            prop: "scopeField",
                            label: "可见字段",
                            maxlength: 255,
                        },
                        {
                            prop: "scopeValue",
                            label: "规则值",
                            maxlength: 2000,
                        },
                        {
                            prop: "scopeClass",
                            label: "权限类名",
                            span: 24,
                            maxlength: 500,
                        },
                        {
                            prop: "remark",
                            label: "备注",
                            maxlength: 255,
                            span: 24
                        }
                    ]
                }
            ],
            del: {
                id: 'id',
                url: '/api/blade-system/data-scope/remove'
            },
            beforeAdd(data, row) {
                row.menuId = data.menuId;
            }
        }
    ]
}
