import { getTree } from "@/api/wms/basedata/skutype";

export const group = {
    column: [
        {
            label: "分类编码",
            prop: "typeCode",
            maxlength: 50,
            rules: [{
                required: true,
                message: "请输入物品分类编码",
                trigger: "blur"
            }]
        },
        {
            label: "分类名称",
            prop: "typeName",
            maxlength: 50,
            rules: [{
                required: true,
                message: "请输入物品分类名称",
                trigger: "blur"
            }]
        },

        {
            label: "货主",
            prop: "woId",
            type: "select",
            dicUrl: "/api/wms/basedata/owner/list",
            props: {
                label: "ownerName",
                value: "woId"
            },
            rules: [{
                required: true,
                message: "请选择货主",
                trigger: "blur"
            }],
            cascaderItem: ['typePreId'],
            change: function (val, obj, col, data) {
                console.log('111');
                getTree({ woId: val }).then(res => {
                    if (col.cascader) {
                        col.cascader.forEach(item => {
                            if (item.prop === 'typePreId') {
                                item.dicData = res.data.data;
                            }
                        });
                    }
                });
            }
        },
        {
            label: '上位分类',
            prop: 'typePreId',
            type: "select-tree",
            dicUrl: "/api/wms/basedata/skutype/tree",
            props: {
                label: "title",
                value: "id",
                children: "children"
            },
            clearable: true,
            show:['parentName']
        },
        {
            label: '绩效系数',
            prop: 'gradeNum',
            type: 'inputNumber',
            precision: 2
        },
        {
            label: "备注",
            prop: "typeRemark",
            maxlength: 300
        }
    ]
}
