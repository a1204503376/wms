const region = require('../../../../../public/json/region.json');
export const group = {
    label: "",
    column: [
        {
            prop: "billTypeCd",
            label: "编码",
            rules: [{required: true, message: "单据类型编码不能为空", trigger: "blur", pattern: /\S/,}],
            maxlength: 50,
        },
        {
            prop: "billTypeName",
            label: "名称",
            rules: [{required: true, message: "单据类型名称不能为空", trigger: "blur", pattern: /\S/,}],
            maxlength: 50,
        },
        {
            prop: "ioType",
            label: "种类",
            type: "select",
            dicUrl: "/api/blade-system/dict/dictionary?code=io_type",
            props: {
               label: 'dictValue',
               value: 'dictKey'
            },
            rules: [{required: false, message: "请选择单据类型种类", trigger: "change"}],
            show:['ioTypeDesc']
        },
        // {
        //     prop: "isStart",
        //     label: "是否启用",
        //     type: "select",
        //     dicUrl: "/api/blade-system/dict/dictionary?code=status",
        //     props: {
        //         label: 'dictValue',
        //         value: 'dictKey'
        //     },
        //     rules: [{required: true, message: "请选择是否启用", trigger: "change"}],
        // },
    ]
}
