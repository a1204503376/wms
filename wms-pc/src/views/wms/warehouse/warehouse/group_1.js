import {getList} from "@/api/core/region";

const region = require('../../../../../public/json/region.json');
export const group = {
    label: "常规",
    column: [
        {
            prop: "whCode",
            label: "仓库编码",
            maxlength: 30,
            rules: [{required: true, message: "仓库编码不能为空", trigger: "blur", pattern: /\S/,}]
        },
        {
            prop: "whName",
            label: "仓库名称",
            maxlength: 50,
            rules: [{required: true, message: "仓库名称不能为空", trigger: "blur", pattern: /\S/,}]
        },
        {
            prop: "deptId",
            label: "所属机构",
            type: "select-tree",
            dicUrl: "/api/blade-system/dept/tree",
            props: {
                label: "title",
                value: "id",
                children: "children"
            },
            clearable: true,
            rules: [{required: true, message: "机构不能为空", trigger: "change"}],
            show:['deptName']
        },
        {
            prop: "zipCode",
            maxlength: 7,
            label: "邮编",
            rules: [
                {
                    pattern: /^[0-9]*[1-9][0-9]*$/,
                    message: '请输入数字'
                }
            ]
        },
        // {
        //     prop: "country",
        //     maxlength: 200,
        //     label: "国家",
        //     type: "select",
        //     props: {label: "name", value: "name"},
        //     dicData: [
        //         {name: "中国"},
        //     ],
        //     cascaderItem:['province'],
        //     change: (val, obj, col, data) => {
        //         if (val) {
        //             group.column[5].dicData = region;
        //         } else {
        //             group.column[5].dicData = null;
        //         }
        //     }
        // },
        // {
        //     prop: "province",
        //     maxlength: 200,
        //     label: "省份",
        //     props: {label: "name", value: "name"},
        //     type: "select",
        //     cascaderItem:['city'],
        //     change: (val, obj, col, data) => {
        //         if (val) {
        //             group.column[6].dicData = obj.city;
        //         } else {
        //             group.column[6].dicData = null;
        //         }
        //
        //     }
        // },
        // {
        //     prop: "city",
        //     maxlength: 200,
        //     label: "城市",
        //     type: "select",
        //     props: {label: "name", value: "name"},
        // },
        {
            prop: "country",
            label: "国家",
            type: "select",
            dicUrl: "/api/blade-system/region/list?parentCode_equal=0",
            props: {
                label: "name",
                value: "name"
            },
            show:['country'],
            cascaderItem: ['province'],
            change: (val, obj, col, data, self) => {
                if (col.cascader) {
                    col.cascader.forEach(item => {
                        if (item.prop === 'province') {
                            if (obj) {
                                getList({
                                    parentCode: obj.code
                                }).then(res => {
                                    item.dicData = res.data.data;
                                    if (item.dicData.length > 0) {
                                        data.province = res.data.data[0].name;
                                    } else {
                                        data.province = "";
                                    }
                                    self.selectChange(item.dicData.length > 0 ? item.dicData[0].code : undefined,
                                        item.dicData[0], item);
                                });
                            } else {
                                item.dicData = [];
                            }
                        }
                    });
                }
            }
        },
        {
            prop: "province",
            label: "省份",
            type: "select",
            props: {
                label: "name",
                value: "name"
            },
            show:['province'],
            cascaderItem: ['city'],
            change: (val, obj, col, data) => {
                if (col.cascader) {
                    col.cascader.forEach(item => {
                        if (item.prop === 'city') {
                            if (obj) {
                                getList({
                                    parentCode_equal: obj.code
                                }).then(res => {
                                    item.dicData = res.data.data;
                                    if (item.dicData.length > 0) {
                                        data.city = res.data.data[0].name;
                                    } else {
                                        data.city = "";
                                    }
                                });
                            } else {
                                item.dicData = [];
                            }
                        }
                    });
                }
            }
        },
        {
            prop: "city",
            label: "城市",
            type: "select",
            show:['city'],
            props: {
                label: "name",
                value: "name"
            },
        },

    ]
}
