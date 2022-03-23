import {getDictByCode} from "@/api/core/dict";
import {getListByZoneId} from "@/api/wms/warehouse/location";
import {getDetail as getSkuPackageDetail} from "@/api/wms/basedata/skupackage";
import {dict} from "@/constant/dict";
import {getList as getZoneList} from "@/api/wms/warehouse/zone";

export const group = {
    label: '策略信息',
    column: [
        {
            prop: "relenishmentDetailList",
            type: "dynamic",
            span: 24,
            moveBtn: true,
            children: [
                // {
                //     label: '执行顺序',
                //     prop: 'ssidProcOrder',
                //     type: "inputNumber",
                // },
                {
                        label: "从库区",
                        prop: "fromZoneId",
                        type: "select",
                        show: ['fromZoneName'],
                        dicUrl: "/api/wms/warehouse/zone/list",
                        search: {
                            whId: 'whId'
                        },
                        props: {
                            label: "zoneName",
                            value: "zoneId"
                        },
                        rules: [
                            {required: true, message: "请选择从库区！", trigger: "blur"}
                        ],
                        change: (val, obj, scope, self) => {
                            if (obj) {
                                scope.row.fromZoneName = obj.zoneName;
                                scope.row.fromZoneId = obj.zoneId;
                            }
                        }
                },
                {
                    label: '至库区',
                    width: 200,
                    prop: "toZoneId",
                    type: "select",
                    show: ['toZoneName'],
                    dicUrl: "/api/wms/warehouse/zone/list",
                    search: {
                        whId: 'whId'
                    },
                    props: {
                        label: "zoneName",
                        value: "zoneId"
                    },
                    rules: [
                        {required: true, message: "请选择至库区！", trigger: "blur"}
                    ],
                    change: (val, obj, scope, self) => {
                        if (obj) {
                            scope.row.toZoneName = obj.zoneName;
                            scope.row.toZoneId = obj.zoneId;
                        }
                    }
                },
                // {
                //     label: "物品层级",
                //     prop: "skuLevel",
                //     type: "select",
                //     show: ['skuLevelDesc'],
                //     dicUrl: "/api/blade-system/dict/dictionary?code=sku_level",
                //     props: {
                //         label: "dictValue",
                //         value: "dictKey"
                //     },
                //     change: (val, obj, scope, self) => {
                //         if (obj) scope.row.skuLevelDesc = obj.dictValue;
                //     }
                // }
            ],
            delete: function (form, row) {
                if (!form['relenishmentDetailDeletedList']) {
                    form['relenishmentDetailDeletedList'] = [];
                }
                form['relenishmentDetailDeletedList'].push(row);
            }
        }
    ]
}
