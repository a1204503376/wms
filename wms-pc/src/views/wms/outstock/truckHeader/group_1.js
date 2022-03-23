export const group = {
    label:'装车信息',
    column:[
        {
            label: "车次编号",
            prop: "truckCode",
            width: 200,
            view: true
        },
        {
            label: "车次状态",
            prop: "truckStateDesc",
            width: 140,
            type: "select",
            dicUrl: "/blade-system/dict/dictionary?code=truck_state",
            props: {
                label: "dictValue",
                value: "dictKey"
            },
        },
        {
            label: "车牌号",
            prop: "plateNumber",
            search: true,
            searchLabelWidth: 50,
            width: 120,
        },
        {
            label: "司机",
            prop: "driverName",
            search: true,
            width: 140,
        },
        {
            label: "电话",
            prop: "driverPhone",
            width: 150
        },
        {
            label: "运单编号",
            prop: "truckHeaderWaybill",
            width: 200
        },
        {
            label: "装车时间",
            prop: "truckDate",
            width: 200,
            type: "date",
            format: "yyyy-MM-dd HH:mm:ss",
            valueFormat: "yyyy-MM-dd HH:mm:ss",
        },
        {
            label: "备注",
            prop: "truckRemark",
            span: 16,
            width: 220
        }
    ]
}
