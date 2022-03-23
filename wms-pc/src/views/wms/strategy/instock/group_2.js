import {getDictByCode} from "@/api/core/dict";
import {getListByZoneId} from "@/api/wms/warehouse/location";
import {getDetail as getSkuPackageDetail} from "@/api/wms/basedata/skupackage";
import {dict} from "@/constant/dict";
import {getList as getZoneList} from "@/api/wms/warehouse/zone";

export const group = {
    label: '策略信息',
    column: [
        {
            prop: "instockDetailList",
            type: "dynamic",
            span: 24,
            moveBtn: true,
            editBtn:true,
            children: [
                {
                    label: '执行顺序',
                    prop: 'ssidProcOrder',
                    type: 'index'
                },
                {
                    label: '上架计算代码',
                    prop: 'instockFunctionDesc',
                    width: 300,
                },
                {
                    label: '从库区类型',
                    prop: 'fromZoneTypeName',
                    width: 120
                },
                {
                    label: '从库区',
                    prop: 'fromZoneName',
                    width: 120,
                },
                {
                    label: '从库位',
                    prop: 'fromLocCode',
                    width: 120,
                },
                {
                    label: '至库区类型',
                    prop: 'toZoneTypeName',
                    width: 120
                },
                {
                    label: '至库区',
                    prop: 'toZoneName',
                    width: 120,
                },
                {
                    label: '至库位',
                    prop: 'toLocCode',
                    width: 120,
                },
                {
                    label: '重量合并',
                    prop: 'confMixWightDesc',
                    width: 120,
                },
                {
                    label: '体积合并',
                    prop: 'confMixVolumeDesc',
                    width: 120,
                },
                {
                    label: '箱数合并',
                    prop: 'confMixBoxCountDesc',
                    width: 120
                },
                {
                    label: '是否允许混放',
                    prop: 'skuMixDesc',
                    width: 120
                }
            ],
            column: [
                {
                    label: '基础信息',
                    column: [
                        {
                            label: '计算代码',
                            prop: 'instockFunction',
                            type: 'select',
                            props: {
                                label: 'dictValue',
                                value: 'dictKey'
                            },
                            dicUrl: '/api/blade-system/dict/dictionary?code=instock_function',
                            rules: [
                                {required: true, message: "请输入上架策略计算代码", trigger: "blur"}
                            ],
                            change: function (val, obj, col, data) {
                                if (obj) {
                                    data.instockFunctionDesc = obj.dictValue;
                                } else {
                                    data.instockFunctionDesc = '';
                                }
                            }
                        },
                        {
                            label: '混批状态',
                            prop: 'skuMix',
                            type: 'radio',
                            dicData: [
                                {value: 0, label: '不允许'},
                                {value: 1, label: '允许'}
                            ],
                            default: 1,
                            change: function (val, obj, col, data) {
                                if (obj) {
                                    data.skuMixDesc = obj.label;
                                } else {
                                    data.skuMixDesc = '';
                                }
                            }
                        },
                        {
                            label: '重量合并',
                            prop: 'confMixWight',
                            row: true,
                            type: 'radio',
                            dicData: [
                                {value: 0, label: '不限制'},
                                {value: 1, label: '限制'}
                            ],
                            default: 0,
                            change: function (val, obj, col, data) {
                                if (obj) {
                                    data.confMixWightDesc = obj.label;
                                } else {
                                    data.confMixWightDesc = '';
                                }
                            }
                        },
                        {
                            label: '体积合并',
                            prop: 'confMixVolume',
                            type: 'radio',
                            dicData: [
                                {value: 0, label: '不限制'},
                                {value: 1, label: '限制'}
                            ],
                            default: 0,
                            change: function (val, obj, col, data) {
                                if (obj) {
                                    data.confMixVolumeDesc = obj.label;
                                } else {
                                    data.confMixVolumeDesc = '';
                                }
                            }
                        },
                        {
                            label: '箱数合并',
                            prop: 'confMixBoxCount',
                            type: 'radio',
                            dicData: [
                                {value: 0, label: '不限制'},
                                {value: 1, label: '限制'}
                            ],
                            default: 0,
                            change: function (val, obj, col, data) {
                                if (obj) {
                                    data.confMixBoxCountDesc = obj.label;
                                } else {
                                    data.confMixBoxCountDesc = '';
                                }
                            }
                        },
                        {
                            label: '从库区类型',
                            prop: 'fromZoneType',
                            type: 'select',
                            dicUrl: "/api/blade-system/dict/dictionary?code=" + dict.zoneType,
                            props: {
                                label: "dictValue",
                                value: "dictKey"
                            },
                            cascaderItem: ['fromZoneId', 'fromLocId'],
                            change: function (val, obj, col, data, self) {
                                if (obj) {
                                    data.fromZoneTypeName = obj.dictValue;
                                } else {
                                    data.fromZoneTypeName = '';
                                }
                                getZoneList({zoneType: val, whId: self.form.owner.whId}).then(res => {
                                    let data = res.data.data;
                                    col.cascader.forEach(item => {
                                        if (item.prop === 'fromZoneId') {
                                            item.dicData = data;
                                        }
                                    });
                                });
                            }
                        },
                        {
                            label: '从库区',
                            prop: 'fromZoneId',
                            type: 'select',
                            dicUrl: '/api/wms/warehouse/zone/list',
                            search: {
                                whId: 'whId'
                            },
                            cascaderItem: ['fromLocId'],
                            props: {
                                label: 'zoneName',
                                value: 'zoneId'
                            },
                            clearable: true,
                            change: function (val, obj, col, data) {
                                if (obj) {
                                    data.fromZoneName = obj.zoneName;
                                } else {
                                    data.fromZoneName = '';
                                    data.fromLocCode = '';
                                }
                                if (!val) {
                                    return;
                                }
                                if (col.cascader) {
                                    col.cascader.forEach(item => {
                                        if (item.prop === 'fromLocId') {
                                            // 设置数据源
                                            getListByZoneId(val).then(res => {
                                                item.dicData = res.data.data;
                                            });
                                        }
                                    });
                                }
                            }
                        },
                        {
                            label: '从库位',
                            prop: 'fromLocId',
                            type: 'select',
                            props: {
                                label: 'locCode',
                                value: 'locId'
                            },
                            clearable: true,
                            change: function (val, obj, col, data) {
                                if (obj) {
                                    data.fromLocCode = obj.locCode;
                                } else {
                                    data.fromLocCode = '';
                                }
                            }
                        },
                        {
                            label: '至库区类型',
                            prop: 'toZoneType',
                            type: 'select',
                            dicUrl: "/api/blade-system/dict/dictionary?code=" + dict.zoneType,
                            props: {
                                label: "dictValue",
                                value: "dictKey"
                            },
                            cascaderItem: ['toZoneId', 'toLocId'],
                            change: function (val, obj, col, data, self) {
                                if (obj) {
                                    data.toZoneTypeName = obj.dictValue;
                                } else {
                                    data.toZoneTypeName = '';
                                }
                                getZoneList({zoneType: val, whId: self.form.owner.whId}).then(res => {
                                    let data = res.data.data;
                                    col.cascader.forEach(item => {
                                        if (item.prop === 'toZoneId') {
                                            item.dicData = data;
                                        }
                                    });
                                });
                            }
                        },
                        {
                            label: '至库区',
                            prop: 'toZoneId',
                            type: 'select',
                            dicUrl: '/api/wms/warehouse/zone/list',
                            search: {
                                whId: 'whId'
                            },
                            cascaderItem: ['toLocId'],
                            props: {
                                label: 'zoneName',
                                value: 'zoneId'
                            },
                            clearable: true,
                            change(val, obj, col, data) {
                                if (obj) {
                                    data.toZoneName = obj.zoneName;
                                } else {
                                    data.toZoneName = '';
                                    data.toLocCode = '';
                                }
                                if (!val) {
                                    return;
                                }
                                if (col.cascader) {
                                    col.cascader.forEach(item => {
                                        if (item.prop === 'toLocId') {
                                            // 设置数据源
                                            getListByZoneId(val).then(res => {
                                                item.dicData = res.data.data;
                                            });
                                        }
                                    });
                                }
                            }
                        },
                        {
                            label: '至库位',
                            prop: 'toLocId',
                            type: 'select',
                            props: {
                                label: 'locCode',
                                value: 'locId'
                            },
                            clearable: true,
                            change: function (val, obj, col, data) {
                                if (obj) {
                                    data.toLocCode = obj.locCode;
                                } else {
                                    data.toLocCode = '';
                                }
                            }
                        },
                        {
                            label: '排序类型',
                            prop: 'locSortType',
                            type: 'select',
                            props: {
                                label: 'dictValue',
                                value: 'dictKey'
                            },
                            clearable: true,
                            dicUrl: '/api/blade-system/dict/dictionary?code=loc_sort_type'
                        },
                        {
                            label: '排序方式',
                            prop: 'locSortMode',
                            type: 'select',
                            props: {
                                label: 'dictValue',
                                value: 'dictKey'
                            },
                            clearable: true,
                            dicUrl: '/api/blade-system/dict/dictionary?code=loc_sort_mode'
                        },
                        {
                            label: 'abc分类',
                            prop: 'abc',
                            type: 'select',
                            dicUrl: '/api/blade-system/dict/dictionary?code=abc',
                            props: {
                                label: 'dictValue',
                                value: 'dictKey'
                            }
                        }
                    ]
                },
                {
                    label: '执行条件',
                    column: [{
                        prop: "instockConfigList",
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
                                dicUrl: '/api/wms/basedata/billtype/list?ioType=I',
                                props: {
                                    label: 'billTypeName',
                                    value: 'billTypeCd'
                                },
                                show: ['billTypeIdDesc'],
                                rules: [
                                    {required: true, message: "请选择单据类型", trigger: "change"}
                                ],
                                change: function (val, obj, scope, self) {
                                    if (obj) {
                                        scope.row.billTypeIdDesc = obj.billTypeName;
                                    }
                                }
                            }
                        ],
                        delete: function (form, row) {
                            if (!form['instockConfigDeletedList']) {
                                form['instockConfigDeletedList'] = [];
                            }
                            form['instockConfigDeletedList'].push(row);
                        },
                        beforeAdd(owner, row, self) {
                            if (self && owner.instockConfigList) {
                                for (let index in owner.instockConfigList) {
                                    let instockConfig = owner.instockConfigList[index];

                                    if (instockConfig.skuTypeId == row.skuTypeId &&
                                        instockConfig.billTypeId == row.billTypeId &&
                                        instockConfig.nodesCurdRowId != row.nodesCurdRowId) {

                                        self.$message.warning(
                                            '物品种类[' +
                                            instockConfig.skuTypeIdDesc +
                                            '], 单据种类[' +
                                            instockConfig.billTypeIdDesc +
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
                    label: '批属性',
                    column: [{
                        prop: "instockConfigLotList",
                        type: "dynamic",
                        span: 24,
                        copyBtn: true,
                        children: [
                            {
                                label: '批属性索引',
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
                            if (!form['instockConfigLotDeletedList']) {
                                form['instockConfigLotDeletedList'] = [];
                            }
                            form['instockConfigLotDeletedList'].push(row);
                        }
                    }]
                },
            ],
            delete: function (form, row) {
                if (!form['instockDetailDeletedList']) {
                    form['instockDetailDeletedList'] = [];
                }
                form['instockDetailDeletedList'].push(row);
            }
        }
    ]
}
