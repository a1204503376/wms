export const group = {
    label: "常规",
    column: [{
        prop: "wspName",
        label: "包装名称",
        rules: [{ required: true, message: "包装名称不能为空", trigger: "blur", pattern:/\S/, }],
        maxlength:200
    }, {
        prop: "palletBoxLevel",
        label: "每层箱数",
        type: "inputNumber",
        max:99999999999
    }, {
        prop: "palletLevel",
        label: "每托层数",
        type: "inputNumber",
        max:99999999999
    }, {
        prop: "lpnLength",
        label: "每托长度",
        type: "inputNumber"
    }, {
        prop: "lpnWidth",
        label: "每托宽度",
        type: "inputNumber"
    }, {
        prop: "lpnHeight",
        label: "每托高度",
        type: "inputNumber"
    }, {
        prop: "lpnWeight",
        label: "每托重量",
        type: "inputNumber"
    }]
}
