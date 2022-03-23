const region = require('../../../../public/json/region.json');
export const group = {
    label: '常规',
    column: [
        {
            label: "区划编号",
            prop: "code",
            maxlength: 50,
            rules: [{required: true, message: "区划编号不能为空", trigger: "blur", pattern: /\S/,}]
        },
        {
            label: "区划名称",
            prop: "name",
            maxlength: 200,
            rules: [{required: true, message: "区划名称不能为空", trigger: "blur", pattern: /\S/,}]
        },
        {
            label: "货主简称",
            prop: "ownerNameS",
            maxlength: 100
        },
        {
            label: "国家",
            maxlength: 200,
            prop: "ownerCountry",
            type: "select",
            props: {label: "name", value: "name"},
            dicData: [
                {name: "中国"},
            ],
            cascaderItem:['ownerProvince'],
            change: (val, obj, col, data) => {
                if (val) {
                    group[0].column[4].dicData = region;
                } else {
                    group[0].column[4].dicData = null;
                }
            }
        },
        {
            label: "省份",
            maxlength: 200,
            prop: "ownerProvince",
            props: {label: "name", value: "name"},
            type: "select",
            cascaderItem:['ownerCity'],
            change: (val, obj, col, data) => {
                if (val) {
                    group[0].column[5].dicData = obj.city;
                } else {
                    group[0].column[5].dicData = null;
                }

            }
        },
        {
            label: "城市",
            prop: "ownerCity",
            maxlength: 200,
            type: "select",
            props: {label: "name", value: "name"},
        },
        {
            label: "邮编",
            prop: "ownerZipCode",
            maxlength: 7,
            rules: [
                {
                    pattern: /^[0-9]*[1-9][0-9]*$/,
                }
            ]
        }
    ]
}
