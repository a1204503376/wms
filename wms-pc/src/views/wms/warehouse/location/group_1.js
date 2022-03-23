import { getList } from "@/api/wms/warehouse/zone.js";

export const group = {
    label: "常规",
    column: [
        {
            prop: "locCode",
            label: "库位编码",
            rules: [{ required: true, message: "库位编码不能为空", trigger: "blur", pattern: /\S/, }],
        },
        {
            prop: "whId",
            label: "库房",
            type: "select",
            dicUrl: "/api/wms/warehouse/warehouse/list",
            props: {
                label: "whName",
                value: "whId"
            },
            rules: [{ required: true, message: "请选择库房", trigger: "change" }],
            cascaderItem: ['zoneId'],
            change: (val, obj, col, data) => {
                if (col.cascader) {
                    col.cascader.forEach(item => {
                        if (item.prop === 'zoneId') {
                            if (val && val.length > 0) {
                                getList({
                                    whId: val
                                }).then(res => {
                                    item.dicData = res.data.data;
                                    if (item.dicData.length > 0) {
                                        data.zoneId = res.data.data[0].zoneId;
                                    } else {
                                        data.zoneId = "";
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
            prop: "zoneId",
            label: "库区",
            type: "select",
            props: {
                label: "zoneName",
                value: "zoneId"
            },
            rules: [{ required: true, message: "请选择库区", trigger: "change" }],
        },
        {
            prop: "locType",
            label: "应用类型",
            type: "select",
            dicUrl: "/api/blade-system/dict/dictionary?code=loc_type",
            props: {
                label: "dictValue",
                value: "dictKey"
            },
            show:['locTypeDesc']
        },
        {
            prop: "locCategory",
            label: "库位种类",
            type: "select",
            dicUrl: "/api/blade-system/dict/dictionary?code=loc_category",
            props: {
                label: "dictValue",
                value: "dictKey"
            },
            show:['locCategoryDesc']
        },
        // {
        //     prop: "lpnsortalLocation",
        //     label: "周转方式",
        //     type: "select",
        //     dicUrl: "/api/blade-system/dict/dictionary?code=turnover_item",
        //     props: {
        //         label: "dictValue",
        //         value: "dictKey"
        //     }
        // },
        // {
        //     prop: "interleavingSeq",
        //     label: "任务顺序",
        //     type: 'inputNumber'
        //     // rules: [
        //     //     {
        //     //         pattern:/^[0-9]*$/,
        //     //     }
        //     // ]
        // },
        {
            prop: "locHandling",
            label: "库位处理",
            type: "select",
            dicUrl: "/api/blade-system/dict/dictionary?code=loc_handling",
            props: {
                label: "dictValue",
                value: "dictKey"
            },
            show:['locHandlingDesc']
        },
        {
            prop: "checkDigit",
            label: "校验数位",
            maxlength: 50
            // rules: [
            //     {
            //         pattern:/^[0-9]*$/,
            //     }
            // ]
        },
        {
            prop: "logicAllocation",
            label: "线路顺序",
            type: 'inputNumber',
            max: 999,
            min: -999
            // rules: [
            //     {
            //         pattern:/^[0-9]*$/,
            //     }
            // ]
        },
        {
            prop: "locFlag",
            label: "使用状态",
            type: "select",
            dicUrl: "/api/blade-system/dict/dictionary?code=loc_flag",
            props: {
                label: "dictValue",
                value: "dictKey"
            },
            show:['locFlagDesc']
        },
        {
            prop: "abc",
            label: "ABC",
            type: "select",
            dicUrl: "/api/blade-system/dict/dictionary?code=abc",
            props: {
                label: "dictValue",
                value: "dictKey"
            },
            show:['abcDesc']
        },
        // {
        //     prop: "isActive",
        //     label: "是否启用",
        //     type: "radio",
        //     default: "Y",
        //     dicData: [
        //         {
        //             label: "是",
        //             value: "Y"
        //         },
        //         {
        //             label: "否",
        //             value: "N"
        //         }
        //     ]
        // }
    ]
}
