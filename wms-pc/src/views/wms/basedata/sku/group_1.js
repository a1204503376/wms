import {getTree as getSkuTypeTree} from "@/api/wms/basedata/skutype";
import {getList as getSkuLotList} from "@/api/wms/basedata/skulot";

export const group = {
    label: '常规',
    column: [
        {
            prop: "skuCode",
            label: "物品编码",
            disabled: false,   //是否置灰
            rules: [{required: true, message: "物品编码不能为空", trigger: "blur", pattern: /\S/,}],
            maxlength: 50
        },
        {
            prop: "skuName",
            label: "物品名称",
            disabled: false,  //是否置灰
            rules: [{required: true, message: "物品名称不能为空", trigger: "blur", pattern: /\S/,}],
            maxlength: 200
        },
        {
            prop: "skuNameS",
            label: "物品简称",
            disabled: false,  //是否置灰
            maxlength: 100
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
            cascaderItem: ['skuTypeId', "wslId", "skuIncList"],
            rules: [{required: true, message: "货主不能为空", trigger: "blur"}],
            disabled: function (data) {
                return data && data.skuReplaceList && data.skuReplaceList.length > 0;
            },
            change: (val, obj, col, data) => {
                if (val) {
                    getSkuTypeTree({woId: val}).then(res => {
                        if (col.cascader) {
                            col.cascader.forEach(item => {
                                if (item.prop === 'skuTypeId') {
                                    item.dicData = res.data.data;
                                }
                            });
                        }
                    });
                    getSkuLotList({woId: val}).then(res => {
                        if (col.cascader) {
                            col.cascader.forEach(item => {
                                if (item.prop === 'wslId') {
                                    item.dicData = res.data.data;
                                }
                            });
                        }
                    });
                } else {
                    col.cascader.forEach(item=>{
                        item.dicData = [];
                    });
                }
            }
        },
        {
            prop: "skuTypeId",
            label: "物品分类",
            disabled: false,  //是否置灰
            type: "select-tree",
            show:['typeName'],
            dicUrl: "/api/wms/basedata/skutype/tree",
            props: {
                label: "title",
                value: "id",
                children: "children"
            },
            clearable: true,
            rules: [{required: true, message: "物品分类不能为空", trigger: "blur"}],
        },
        {
            prop: "wspId",
            label: "包装",
            disabled: false,
            type: 'select-table-sku-package',
            props: {
                label: 'wspName',
                value: 'wspId'
            },
            show: ['wspName'],
            rules: [{ required: true, message: "包装不能为空", trigger: "change, blur" }],
        },
        {
            prop: "wslId",
            label: "批属性",
            disabled: false,  //是否置灰
            type: "select",
            search: {
                woId: "woId",   //货主级联
            },
            dicUrl: "/api/wms/basedata/skulot/list",
            props: {
                label: "skuLotName",
                value: "wslId"
            },
            show:['skuLotName'],
            rules: [{required: true, message: "批属性设置不能为空", trigger: "blur"}],
        },
        {
            prop: "wslvId",
            label: "属性验证",
            disabled: false,  //是否置灰
            type: "select",
            clearable: true,
            dicUrl: "/api/wms/basedata/skulotval/list",
            props: {
                label: "skuLotValName",
                value: "wslvId"
            },
            show:['skuLotValName'],
            rules: [{required: true, message: "批属性验证不能为空", trigger: "blur"}],
        },
        {
            prop: 'abc',
            label: 'ABC分类',
            type: 'select',
            dicUrl: '/api/blade-system/dict/dictionary?code=abc',
            props: {
                label: 'dictValue',
                value: 'dictKey'
            },
            show:['abcName']
        },
        {
            prop: "udf1",
            label: "箱型",
            disabled: false,
            clearable: true,
            maxlength: 100
        },
        {
            prop: "skuSpec",
            label: "型号",
            disabled: false,
            clearable: true,
            maxlength: 10
        },
        {
            prop: "skuVolume",
            label: "体积",
            type: "inputNumber",
        },
        {
            prop: "skuGrossWeight",
            label: "毛重",
            type: "inputNumber",
        },
        {
            prop: "skuNetWeight",
            label: "净重",
            type: "inputNumber",
        },
        {
            prop: "skuTareWeight",
            label: "皮重",
            type: "inputNumber",
        },
        {
            prop: "isSn",
            label: "序列号状态",
            type: "radio",
            dicData: [
                {
                    value: 1,
                    label: "是"
                },
                {
                    value: 0,
                    label: "否"
                }
            ],
            show:['isSnDesc'],
            default: 0
        },
        {
            prop: 'skuStorageType',
            label: '存货类型',
            type: 'select',
            dicUrl: '/api/blade-system/dict/dictionary?code=inventory_type',
            props: {
                value: 'dictKey',
                label: 'dictValue'
            },
            show:['inventoryTypeDesc']
        },
        {
            prop: "skuRemark",
            label: "说明",
            type: "textarea",
            span: 16,
            maxlength: 200
        },
        {
            prop: "skuBarcodeList",
            label: "物品条码",
            type: "textarea",
            placeholder: '多个条码请换行',
            span: 8,
            maxlength: 200
        },
    ]
}
