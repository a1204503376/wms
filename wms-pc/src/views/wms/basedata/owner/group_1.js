import {getList} from "@/api/core/region";

const region = require('../../../../../public/json/region.json');
export const group = {
    label: '常规',
    column: [
        {
            label: "货主编码",
            prop: "ownerCode",
            maxlength: 50,
            rules: [{required: true, message: "货主编码不能为空", trigger: "blur", pattern: /\S/,}]
        },
        {
            label: "货主名称",
            prop: "ownerName",
            maxlength: 200,
            rules: [{required: true, message: "货主名称不能为空", trigger: "blur", pattern: /\S/,}]
        },
        {
            label: "货主简称",
            prop: "ownerNameS",
            maxlength: 100
        },
        // {
        //     label: "国家",
        //     maxlength: 200,
        //     prop: "ownerCountry",
        //     type: "select",
        //     props: {label: "name", value: "name"},
        //     dicData: [
        //         {name: "中国"},
        //     ],
        //     cascaderItem:['ownerProvince'],
        //     change: (val, obj, col, data) => {
        //         if (val) {
        //             group[0].column[4].dicData = region;
        //         } else {
        //             group[0].column[4].dicData = null;
        //         }
        //     }
        // },
        // {
        //     label: "省份",
        //     maxlength: 200,
        //     prop: "ownerProvince",
        //     props: {label: "name", value: "name"},
        //     type: "select",
        //     cascaderItem:['ownerCity'],
        //     change: (val, obj, col, data) => {
        //         if (val) {
        //             group[0].column[5].dicData = obj.city;
        //         } else {
        //             group[0].column[5].dicData = null;
        //         }
        //
        //     }
        // },
        // {
        //     label: "城市",
        //     prop: "ownerCity",
        //     maxlength: 200,
        //     type: "select",
        //     props: {label: "name", value: "name"},
        // },
        {
            prop: "ownerCountry",
            label: "国家",
            type: "select",
            dicUrl: "/api/blade-system/region/list?parentCode_equal=0",
            props: {
                label: "name",
                value: "name"
            },
            show: ['ownerCountry'],
            cascaderItem: ['ownerProvince'],
            change: (val, obj, col, data, self) => {
                if (col.cascader) {
                    col.cascader.forEach(item => {
                        if (item.prop === 'ownerProvince') {
                            if (obj) {
                                getList({
                                    parentCode_equal: obj.code
                                }).then(res => {
                                    item.dicData = res.data.data;
                                    if (item.dicData.length > 0) {
                                        data.ownerProvince = res.data.data[0].name;
                                    } else {
                                        data.ownerProvince = "";
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
            prop: "ownerProvince",
            label: "省份",
            type: "select",
            props: {
                label: "name",
                value: "name"
            },
            show: ['ownerProvince'],
            cascaderItem: ['ownerCity'],
            change: (val, obj, col, data) => {
                if (col.cascader) {
                    col.cascader.forEach(item => {
                        if (item.prop === 'ownerCity') {
                            if (obj) {
                                getList({
                                    parentCode_equal: obj.code
                                }).then(res => {
                                    item.dicData = res.data.data;
                                    if (item.dicData.length > 0) {
                                        data.ownerCity = res.data.data[0].name;
                                    } else {
                                        data.ownerCity = "";
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
            prop: "ownerCity",
            label: "城市",
            type: "select",
            props: {
                label: "name",
                value: "name"
            },
            show: ['ownerCity'],
        },
        {
            label: "邮编",
            prop: "ownerZipCode",
            maxlength: 7,
            rules: [
                {
                    pattern: /^[0-9]*[1-9][0-9]*$/,
                    message: '请输入数字'
                }
            ]
        }
    ]
}
