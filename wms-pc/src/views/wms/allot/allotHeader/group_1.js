export const group = {
    label: "常规",
    column: [{
        prop: "sourceWhId",
        label: "调出库房",
        type: "select",
        dicUrl: "/api/wms/warehouse/warehouse/list",
        props: {
            label: "whName",
            value: "whId"
        },
        rules: [{required: true, message: "请选择调出库房", trigger: "change"}],
        show:['sourceWhName']
    },
        {
            prop: "sourceZoneType",
            label: "库区类型",
            type: "select",
            dicUrl: "/api/blade-system/dict/dictionary?code=zone_type",
            props: {
                label: "dictValue",
                value: "dictKey"
            },
            rules: [{required: true, message: "请选择库区类型", trigger: "change"}],
            show:['zoneName']
        },
        {
            prop: "targetWhId",
            label: "调入库房",
            type: "select",
            dicUrl: "/api/wms/warehouse/warehouse/list",
            props: {
                label: "whName",
                value: "whId"
            },
            change: (val, obj, col, data) => {
            },
            rules: [{required: true, message: "请选择调入库房", trigger: "change"}],
            show:['targetWhName']
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
            defaultIndex: 0,
            disabled: function (data) {
                return data && data.detailList && data.detailList.length > 0;
            },
            rules: [{required: true, message: "请选择货主", trigger: "change"}]
        }]
}
