import {getTree as getSkuTypeTree} from "@/api/wms/basedata/skutype";
import {getList as getSkuLotList} from "@/api/wms/basedata/skulot";

export const group = {
    label: '常规',
    column: [
        {
            prop: "woId",
            label: "货主",
            type: "select",
            // disabled: false,  //是否置灰
            dicUrl: "/api/wms/basedata/owner/list",
            rules: [{required: true, message: "货主不能为空", trigger: "blur", pattern: /\S/,}],
            defaultIndex: 0,
            props: {
                label: "ownerName",
                value: "woId"
            },
            cascaderItem: ['skuTypeId', "wslId", "skuIncList"],
            disabled: function (data) {
                return data && data.skuReplaceList && data.skuReplaceList.length > 0;
            },
            // change: (val, obj, col, data) => {
            //     if (val) {
            //         getSkuTypeTree({woId: val}).then(res => {
            //             if (col.cascader) {
            //                 col.cascader.forEach(item => {
            //                     if (item.prop === 'skuTypeId') {
            //                         item.dicData = res.data.data;
            //                     }
            //                 });
            //             }
            //         });
            //         getSkuLotList({woId: val}).then(res => {
            //             if (col.cascader) {
            //                 col.cascader.forEach(item => {
            //                     if (item.prop === 'wslId') {
            //                         item.dicData = res.data.data;
            //                     }
            //                 });
            //             }
            //         });
            //     } else {
            //         col.cascader.forEach(item=>{
            //             item.dicData = [];
            //         });
            //     }
            // }
        },
        {
            prop: "skuId",
            label: "物品",
            type: 'select-table-sku',
            disabled: false,  //是否置灰
            rules: [{required: true, message: "物品编码不能为空", trigger: "blur", pattern: /\S/,}],
            dicUrl: "/api/wms/basedata/sku/list",
            maxlength: 200,
            props: {
                label: "skuName",
                value: "skuId"
            },
        },
        {
            prop: "joinSkuId",
            label: "组合物品",
            type: "select-table-sku",
            disabled: false,  //是否置灰
            rules: [{required: true, message: "组合物品不能为空", trigger: "blur", pattern: /\S/,}],
            dicUrl: "/api/wms/basedata/sku/list",
            maxlength: 200,
            props: {
              label: "skuName",
              value: "skuId"
            },
            change(){
                console.log("1");
            }
        },
        {
            prop: "qty",
            label: "组合数量",
            type: "inputNumber",
            precision: "6",
            step:  "1",
            disabled: false,  //是否置灰
            rules: [{required: true, message: "组合数量不能为空", trigger: "blur", pattern: /\S/,}],
        },
        {
            prop: "skuRemark",
            label: "备注",
            type: "textarea",
            span: 16,
            maxlength: 200
        },
    ]
}
