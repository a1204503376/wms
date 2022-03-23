import {getSkuPackage as getSkuIncPackage} from "@/api/wms/basedata/skuInc";
import {getDetail as getSkuPackage} from "@/api/wms/basedata/skupackage";
import {getConfig as getSkuLotConfig} from "@/api/wms/basedata/skulot";

export const group = {
    label: "明细",
    column: [{
        prop: "detailList",
        type: "dynamic",
        span: 24,
        children: [
            {
                label: '物品编码',
                prop: 'skuCode',
                width: 160
            },
            {
                label: '物品名称',
                prop: 'skuName',
                width: 200
            },
            {
                label: '包装',
                prop: 'wspName',
                width: 180
            },
            {
                label: '计划数量',
                prop: 'planQty',
                width: 100
            },
            {
                label: '清点数量',
                prop: 'aclQty',
                width: 100
            },
            {
                label: '计量单位',
                prop: 'wsuName',
                width: 100
            },
            {
                label: '容器编码',
                prop: 'lpnCode',
                width: 120
            }
        ],
        menuWidth: 120,
    }]
}
