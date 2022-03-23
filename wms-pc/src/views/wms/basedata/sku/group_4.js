import { getDetail as getSkuPackage } from "@/api/wms/basedata/skupackage";
import { getDetail as getEnterpriseDetail } from "@/api/wms/basedata/enterprise";

export const group = {
    label: '企业',
    column: [
        {
            prop: "skuIncList",
            type: "dynamic",
            span: 24,
            children: [
                {
                    label: "企业",
                    prop: "peId",
                    type: "selectEnterprise",
                    search: {
                        woId: "woId",   //货主级联
                    },
                    props: {
                        label: 'enterpriseName'
                    },
                    show: ["enterpriseName"],
                    width: 200,
                    rules: [{ required: true, message: "供应商不能为空", trigger: "blur" }],
                    change: function (val, obj, scope, col) {
                        getEnterpriseDetail(val).then(res => {
                            scope.row.peId = res.data.data.peId;
                            scope.row.enterpriseCode = res.data.data.enterpriseCode
                            scope.row.enterpriseName = res.data.data.enterpriseName;
                        })
                    },
                },
                {
                    label: "包装",
                    prop: "wspId",
                    props: {
                        label: 'wspName',
                    },
                    show: ['wspName'],
                    type: "selectSkuPackage",
                    width: 150,
                    rules: [{ required: true, message: "包装不能为空", trigger: "blur" }],
                    change: function (val, obj, scope, self) {
                        getSkuPackage(val).then(res => {
                            scope.row.wspName = res.data.data.wspName;
                        })
                    },
                },
                {
                    label: '扩展字段1',
                    prop: 'attribute1',
                    maxlength: 200,
                    width: 120
                },
                {
                    label: '扩展字段2',
                    prop: 'attribute2',
                    maxlength: 200,
                    width: 120
                },
                {
                    label: '扩展字段3',
                    prop: 'attribute3',
                    maxlength: 200,
                    width: 120
                },
                {
                    label: '扩展字段4',
                    prop: 'attribute4',
                    maxlength: 200,
                    width: 120
                },
                {
                    label: '扩展字段5',
                    prop: 'attribute5',
                    maxlength: 200,
                    width: 120
                },
                {
                    label: '扩展字段6',
                    prop: 'attribute6',
                    maxlength: 200,
                    width: 120
                },
                {
                    label: '扩展字段7',
                    prop: 'attribute7',
                    maxlength: 200,
                    width: 120
                },
                {
                    label: '扩展字段8',
                    prop: 'attribute8',
                    maxlength: 200,
                    width: 120
                },
                {
                    label: '扩展字段9',
                    prop: 'attribute9',
                    maxlength: 200,
                    width: 120
                },
                {
                    label: '扩展字段10',
                    prop: 'attribute10',
                    maxlength: 200,
                    width: 120
                }
            ],
            delete: (form, row) => {
                if (!form['skuIncDeletedList']) {
                    form['skuIncDeletedList'] = [];
                }
                form['skuIncDeletedList'].push(row);
            }
        }
    ]
}
