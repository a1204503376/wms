export const group = {
    label: "混放规则",
    column: [
        {
            prop: "locSkuMix",
            label: "混放货品",
            type: "select",
            dicData: [
                { label: "允许", value: "2" },
                { label: "不允许", value: "1" }
            ]
        },
        {
            prop: "locLotNoMix",
            label: "混放批号",
            type: "select",
            dicData: [
                { label: "允许", value: "2" },
                { label: "不允许", value: "1" }
            ]
        }
    ]
}