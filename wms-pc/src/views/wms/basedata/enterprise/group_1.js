import {getList} from "@/api/core/region";

const region = require('../../../../../public/json/region.json');
export const group = {
    label: "常规",
    column: [
        {
            prop: "enterpriseCode",
            label: "企业编码",
            rules: [{required: true, message: "企业编码不能为空", trigger: "blur", pattern: /\S/,}],
            maxlength: 50
        },
        {
            prop: "enterpriseName",
            label: "企业名称",
            rules: [{required: true, message: "企业名称不能为空", trigger: "blur", pattern: /\S/,}],
            maxlength: 300
        },
        {
            prop: "woId",
            label: "货主",
            type: "select",
            dicUrl: "/api/wms/basedata/owner/list",
            props: {
                label: "ownerName",
                value: "woId"
            },
            rules: [{required: true, message: "请选择货主", trigger: "change"}]
        },
        {
            prop: "enterpriseType",
            label: "企业类型",
            type: "select",
            multiple: true,
            dicUrl: "/api/blade-system/dict/dictionary?code=enterprise_type",
            props: {
                label: 'dictValue',
                value: 'dictKey'
            },
            rules: [{required: true, message: "请选择企业类型", trigger: "change"}],
            show:['enterpriseTypeDesc']
        },
        {
            prop: "enterpriseNameS",
            label: "企业简称",
            maxlength: 100
        },
        {
            prop: "zipCode",
            label: "邮编",
            maxlength: 7,
            rules: [
                {
                    pattern: /^[0-9]*[1-9][0-9]*$/,
                    message: '请输入数字'
                }
            ]
        },
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
                                    parentCode_equal: obj.code
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
