import {getDictByCode} from "@/api/core/dict";
import {getListByZoneId} from "@/api/wms/warehouse/location";
import {getDetail as getSkuPackageDetail} from "@/api/wms/basedata/skupackage";

export const group = {
    label: '策略信息',
    column: [
        {
            prop: "outstockDetailVO",
            type: "dynamic",
            span: 24,
            moveBtn: true,
            editBtn:true,
            children: [
                {
                    label: '执行顺序',
                    prop: 'ssodProcOrder',
                    type: 'index'
                },
                {
                    label: '分配计算代码',
                    prop: 'outstockFunctionDesc',
                    width: 300,
                }
            ],
            column: [
                {
                    label: '基础信息',
                    column: [
                        {
                            label: '计算代码',
                            prop: 'outstockFunction',
                            type: 'select',
                            multiple: true,
                            props: {
                                label: 'dictValue',
                                value: 'dictKey'
                            },
                            dicUrl: '/api/blade-system/dict/dictionary?code=outstock_function',
                            default: [],
                            rules: [
                                {required: true, message: "请选择分配策略计算代码", trigger: "blur"}
                            ],
                            span: 16,
                            change: function (val, obj, col, data) {
                                if (!obj)
                                    return;
                                data.outstockFunctionDesc = '';
                                for (let i = 0; i < obj.length; i++) {
                                    data.outstockFunctionDesc += obj[i].dictValue;
                                    if (i < obj.length - 1) {
                                        data.outstockFunctionDesc += ',';
                                    }
                                }
                            }
                        },
                        {
                            label: '跨批次周转',
                            prop: 'acrossLot',
                            type: 'switch',
                            activeText: '启用',
                            inactiveText: '不启用',
                            activeValue: 1,
                            inactiveValue: 0,
                            default:function(){
                                return 1;
                            }
                        }
                    ]
                },
                {
                    label: '执行条件',
                    column: [{
                        prop: "outstockConfigList",
                        type: "dynamic",
                        span: 24,
                        copyBtn: true,
                        children: [
                            {
                                label: '物品种类',
                                prop: "skuTypeId",
                                type: "select-tree",
                                dicUrl: "/api/wms/basedata/skutype/tree",
                                props: {
                                    label: "title",
                                    value: "id",
                                    children: "children"
                                },
                                show: ['skuTypeIdDesc'],
                                clearable: true,
                                rules: [
                                    {required: true, message: "请选择物品分类", trigger: "change"}
                                ],
                                change: function (val, obj, scope, self) {
                                    if (obj) {
                                        scope.row.skuTypeIdDesc = obj.title;
                                        scope.row.skuTypeId = obj.id;
                                    }
                                }
                            },
                            {
                                label: '单据种类',
                                prop: 'billTypeCd',
                                type: 'select',
                                dicUrl: '/api/wms/basedata/billtype/list?ioType=O',
                                props: {
                                    label: 'billTypeName',
                                    value: 'billTypeCd'
                                },
                                show: ['billTypeCdDesc'],
                                rules: [
                                    {required: true, message: "请选择单据类型", trigger: "change"}
                                ],
                                change: function (val, obj, scope, self) {
                                    if (obj) {
                                        scope.row.billTypeCdDesc = obj.billTypeName;
                                    }
                                }
                            }
                        ],
                        delete: function (form, row) {
                            if (!form['outstockConfigDeletedList']) {
                                form['outstockConfigDeletedList'] = [];
                            }
                            form['outstockConfigDeletedList'].push(row);
                        },
                        beforeAdd(owner, row, self) {
                            if (self && owner.outstockConfigList) {
                                for (let index in owner.outstockConfigList) {
                                    let instockConfig = owner.outstockConfigList[index];

                                    if (instockConfig.skuTypeId == row.skuTypeId &&
                                        instockConfig.billTypeCd == row.billTypeCd) {

                                        self.$message.warning(
                                            '物品种类[' +
                                            instockConfig.skuTypeIdDesc +
                                            '], 单据种类[' +
                                            instockConfig.billTypeCdDesc +
                                            '] 已存在');
                                        return false;
                                    }
                                }
                            }
                            return true;
                        }
                    }],
                },
                {
                    label: '单据属性',
                    column: [{
                        prop: "detailConfigUdf",
                        type: "dynamic",
                        span: 24,
                        copyBtn: true,
                        children: [
                            {
                                label: '自定义属性',
                                prop: 'billUdfNumber',
                                type: 'select',
                                dicData: [],
                                show: ['billUdfNumberDesc'],
                                rules: [
                                    {type: 'number', required: true, message: "请选择自定义属性", trigger: "change"}
                                ],
                                change: function (val, obj, scope, self) {
                                    if (obj) {
                                        scope.row.billUdfNumberDesc = obj.label;
                                    }
                                }
                            },
                            {
                                label: '操作符',
                                prop: 'billUdfOperation',
                                type: 'select',
                                dicUrl: '/api/blade-system/dict/dictionary?code=bill_udf_operation',
                                props: {
                                    label: 'dictValue',
                                    value: 'dictKey'
                                },
                                show: ['billUdfOperationDesc'],
                                rules: [
                                    {type: 'number', required: true, message: "请选择操作符", trigger: "change"}
                                ],
                                change: function (val, obj, scope, self) {
                                    if (obj) {
                                        scope.row.billUdfOperationDesc = obj.dictValue;
                                    }
                                }
                            },
                            {
                                label: '自定义属性设定值1',
                                prop: 'billUdfVal1',
                                maxlength: 200,
                                width: 120
                            },
                            {
                                label: '自定义属性设定值2',
                                prop: 'billUdfVal2',
                                maxlength: 200,
                                width: 120
                            },
                            {
                                label: '自定义属性设定值3',
                                prop: 'billUdfVal3',
                                maxlength: 200,
                                width: 120
                            }
                        ],
                        delete: function (form, row) {
                            if (!form['detailConfigUdfDeletedList']) {
                                form['detailConfigUdfDeletedList'] = [];
                            }
                            form['detailConfigUdfDeletedList'].push(row);
                        }
                    }]
                },
                {
                    label: '批属性',
                    column: [{
                        prop: "detailConfigLot",
                        type: "dynamic",
                        span: 24,
                        copyBtn: true,
                        children: [
                            {
                                label: '批属性',
                                prop: 'skuLotNumber',
                                type: 'select',
                                dicData: [],
                                show: ['skuLotNumberDesc'],
                                rules: [
                                    {type: 'number', required: true, message: "请选择批属性", trigger: "change"}
                                ],
                                change: function (val, obj, scope, self) {
                                    if (obj) {
                                        scope.row.skuLotNumberDesc = obj.label;
                                    }
                                }
                            },
                            {
                                label: '批属性操作符',
                                prop: 'skuLotOperation',
                                type: 'select',
                                dicUrl: '/api/blade-system/dict/dictionary?code=sku_lot_operation',
                                props: {
                                    label: 'dictValue',
                                    value: 'dictKey'
                                },
                                show: ['skuLotOperationDesc'],
                                rules: [
                                    {type: 'number', required: true, message: "请选择操作符", trigger: "change"}
                                ],
                                change: function (val, obj, scope, self) {
                                    if (obj) {
                                        scope.row.skuLotOperationDesc = obj.dictValue;
                                    }
                                }
                            },
                            {
                                label: '批属性设定值1',
                                prop: 'skuLotVal1',
                                maxlength: 200
                            },
                            {
                                label: '批属性设定值2',
                                prop: 'skuLotVal2',
                                maxlength: 200
                            },
                            {
                                label: '批属性设定值3',
                                prop: 'skuLotVal3',
                                maxlength: 200
                            }
                        ],
                        delete: function (form, row) {
                            if (!form['detailConfigLotDeletedList']) {
                                form['detailConfigLotDeletedList'] = [];
                            }
                            form['detailConfigLotDeletedList'].push(row);
                        }
                    }]
                },
                {
                    label: "货源库区",
                    column: [{
                        prop: "outstockZoneDetail",
                        type: "dynamic",
                        span: 24,
                        copyBtn: true,
                        children: [
                            {
                                label: "库区",
                                prop: "zoneName",
                                type: "select",
                                // show: ['zoneName'],
                                dicUrl: "/api/wms/warehouse/zone/list",
                                search: {
                                    whId: 'whId'
                                },
                                props: {
                                    label: "zoneName",
                                    value: "zoneId"
                                },
                                rules: [
                                    {required: true, message: "请选择货源库区！", trigger: "blur"}
                                ],
                                change: (val, obj, scope, self) => {
                                    debugger
                                    if (obj) {
                                        scope.row.zoneName = obj.zoneName;
                                        scope.row.zoneId = obj.zoneId;
                                        scope.row.zoneType = obj.zoneType;
                                    }
                                }
                            },
                            {
                                label: "物品层级",
                                prop: "skuLevel",
                                type: "select",
                                show: ['skuLevelDesc'],
                                dicUrl: "/api/blade-system/dict/dictionary?code=sku_level",
                                props: {
                                    label: "dictValue",
                                    value: "dictKey"
                                },
                                change: (val, obj, scope, self) => {
                                    if (obj) scope.row.skuLevelDesc = obj.dictValue;
                                }
                            }
                        ],
                        delete: function (form, row) {
                            if (!form['outstockZoneDetailDeletedList']) {
                                form['outstockZoneDetailDeletedList'] = [];
                            }
                            form['outstockZoneDetailDeletedList'].push(row);
                        }
                    }]
                }
            ],
            delete: function (form, row) {
                if (!form['outstockDetailDeletedList']) {
                    form['outstockDetailDeletedList'] = [];
                }
                form['outstockDetailDeletedList'].push(row);
            }
        }
    ]
}
