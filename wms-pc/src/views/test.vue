<template>
    <basic-container>
        <nodes-table v-model="form"
                     :option="option"></nodes-table>
    </basic-container>
</template>
<script>
import nodesTable from "@/components/element-ui/table/index"
import {getDetail as getSkuPackage} from "@/api/wms/basedata/skupackage";
import {getConfig as getSkuLotConfig} from "@/api/wms/basedata/skulot";
import {getSkuPackage as getSkuIncPackage} from "@/api/wms/basedata/skuInc";
export default {
    name: "test",
    components: {
        nodesTable
    },
    data() {
        return {
            form: {},
            option:{
                height:660,
                column: [
                    {
                        label: '行号',
                        prop: 'asnLineNo',
                        type: 'text',
                        width:100,
                        default: function (data) {
                            let lastLineNo = 1;
                            if (data && data.length > 0) {
                                // 找到明细中最大的行号
                                let maxNo = 0;
                                data.forEach(item => {
                                    let no = parseInt(item.asnLineNo);
                                    if (no > maxNo) {
                                        maxNo = no;
                                    }
                                });
                                lastLineNo = maxNo + 1;
                            }
                            return ("0000000000000000" + lastLineNo).substr(-8);
                        }
                    },
                    {
                        label: '接收状态',
                        width: 80,
                        prop: 'detailStatusName',
                        type: 'text',
                        default: '未接收'
                    },
                    {
                        label: '物品编码',
                        width: 150,
                        prop: 'skuCode',
                        type: 'text'
                    },
                    {
                        label: '物品名称',
                        width: 200,
                        prop: 'skuId',
                        type: 'selectSku',
                        props: {
                            value: 'skuName',
                            label: 'skuName'
                        },
                        rules: [
                            {required: true, message: "物品不能为空", trigger: "change"}
                        ],
                        cascaderItem: ['wspId'],
                        searchData: {},
                        beforeOpen: function (self) {
                            this.searchData.woId = self._self.owner.woId;
                        },
                        change: function (val, obj, scope, self) {
                            if (!obj) {
                                return;
                            }
                            if (scope && scope.row) {
                                scope.row.skuId = obj.skuId;
                                scope.row.skuCode = obj.skuCode;
                                scope.row.isSn = obj.isSn === 0 ? '否' : '是';
                                scope.row.wspId = obj.wspId;
                                scope.row.skuType = obj.typeName;
                            }
                            // 获取该物品的供应商关联信息
                            getSkuIncPackage({
                                skuId: obj.skuId,
                            }).then(res => {
                                // 处理级联字段
                                if (this.cascader) {
                                    this.cascader.forEach(item => {
                                        if (item.prop === 'wspId') {
                                            // 触发包装改变的事件
                                            self.onChange(obj.wspId, res.data.data, item, scope);
                                        }
                                    });
                                }
                                if (!self || !self.owner) {
                                    return;
                                }
                                // 设置批属性列名
                                if (self.owner.asnDetailList.length === 0) {
                                    // 获取批属性配置
                                    getSkuLotConfig(obj.skuId).then(res => {
                                        res.data.data.forEach(skuLot => {
                                            for (let i = 0; i < self.column.length; i++) {
                                                let column = self.column[i];
                                                let name = 'skuLot' + skuLot.skuLotIndex;
                                                if (name === column.prop) {
                                                    // 设置批属性 隐藏/显示
                                                    if (skuLot.skuLotDisp === 0) {
                                                        column.hide = true;
                                                    } else {
                                                        column.hide = false;
                                                    }
                                                    // 设置是否必填
                                                    if (skuLot.skuLotMust === 1) {

                                                    } else {

                                                    }
                                                    // 设置组建类型
                                                    if (skuLot.skuTextType === 0) {
                                                        column.type = 'input';
                                                    } else if (skuLot.skuTextType === 1) {
                                                        column.type = 'date';
                                                        column.format = column.valueFormat = skuLot.skuLotMixMask;
                                                    } else {
                                                        column.type = 'text';
                                                    }
                                                    column.label = skuLot.skuLot;
                                                    column.maxlength = skuLot.skuLen;
                                                    break;
                                                }
                                            }
                                        });
                                    });
                                }
                            });
                        }
                    },
                    {
                        label: '物品分类',
                        width: 150,
                        prop: 'skuType',
                        type: 'text'
                    },
                    {
                        label: '包装',
                        width: 200,
                        prop: 'wspId',
                        type: 'selectSkuPackage',
                        disabled: true,
                        cascaderItem: ['umName', 'skuSpec'],
                        props: {
                            label: 'wspName',
                            value: 'wspId'
                        },
                        show: ['wspName'],
                        rules: [{required: true, message: "包装不能为空", trigger: "change"}],
                        change: function (val, obj, scope, self) {
                            if (!val) {
                                return;
                            }
                            getSkuPackage(val).then(res => {
                                const data = res.data.data;
                                scope.row.wspId = val;
                                scope.row.wspName = data.wspName;
                                scope.row.skuSpec = data.spec;
                                // 处理级联字段
                                if (this.cascader) {
                                    this.cascader.forEach(item => {
                                        if (item.prop === 'umName') {
                                            // 设置计量单位数据源
                                            item.dicData = data.skuPackageDetailVOList;
                                            if (item.dicData && item.dicData.length > 0) {
                                                // 设置默认包装单位
                                                let packageDetail = null;
                                                let result = null;
                                                if (!scope.row.umCode) {
                                                    result = item.dicData.filter(item => {
                                                        return item.skuLevel == "1";
                                                    });
                                                } else {
                                                    result = item.dicData.filter(item => {
                                                        return item.wsuCode === scope.row.umCode;
                                                    });
                                                }
                                                if (result && result.length > 0) {
                                                    packageDetail = result[0];
                                                } else {
                                                    packageDetail = item.dicData[0];
                                                }
                                                // 触发事件主要用于给默认值；为了不影响编辑的时候的数据绑定增加判断
                                                if (!scope.row[item.prop]) {
                                                    self.onChange(packageDetail.wsuCode, packageDetail, item, scope);
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    },
                    {
                        label: '计量单位',
                        width: 120,
                        prop: 'umName',
                        type: 'select',
                        props: {
                            label: 'wsuName',
                            value: 'wsuCode'
                        },
                        cascaderItem: ['skuLevelName'],
                        rules: [
                            {required: true, message: "计量单位不能为空", trigger: "blur"}
                        ],
                    },
                    {
                        label: '层级',
                        width: 120,
                        prop: 'skuLevelName',
                        type: 'text'
                    },
                    {
                        label: '规格',
                        width: 120,
                        prop: 'skuSpec',
                        type: 'text'
                    },
                    {
                        label: '计划数量',
                        width: 160,
                        prop: 'planQty',
                        type: 'inputNumber',
                        min: 1,
                        rules: [
                            {type: 'number', required: true, message: " ", trigger: "blur"}
                        ],
                        change: function (val, obj, scope, self) {
                            scope.row.surplusQty = val - scope.row.scanQty;
                        }
                    },
                    {
                        label: '实收数量',
                        prop: 'scanQty',
                        type: 'text',
                        default: 0
                    },
                    {
                        label: '剩余数量',
                        prop: 'surplusQty',
                        type: 'text',
                        default: 0
                    },
                    {
                        label: 'SN管理',
                        prop: 'isSn',
                        type: 'text'
                    }, {
                        label: "序列号导入状态",
                        width: 120,
                        prop: "importSnName",
                        type: "text"
                    }
                ],
            },
            group: [
                {
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
                            defaultIndex: 0,
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
                                    col.cascader.forEach(item => {
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
                            disabled: false,  //是否置灰
                            type: 'selectSkuPackage',
                            show: ['wspName'],
                            props: {
                                label: "wspName",
                            },
                            rules: [{required: true, message: "包装不能为空", trigger: "blur"}],
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
                            }
                        },
                        {
                            prop: "attribute2",
                            label: "箱号",
                            disabled: false,
                            clearable: true,
                            maxlength: 100
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
                            }
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
                },
                {
                    label: '保质期',
                    column: [
                        {
                            prop: "skuTempUpperLimit",
                            label: "温度上限",
                            type: "inputNumber",
                            hide: true
                        },
                        {
                            prop: "skuTempLowerLimit",
                            label: "温度下限",
                            type: "inputNumber",
                            hide: true
                        },
                        {
                            prop: "qualityType",
                            label: "保质期有无",
                            type: "select",
                            props: {label: "label", value: "value"},
                            dicData: [
                                {label: "有", value: "Y"},
                                {label: "无", value: "N"},
                            ],
                            hide: true
                        },
                        {
                            prop: "qualityDateType",
                            label: "保质期类型",
                            type: "select",
                            hide: true,
                            props: {label: "dictValue", value: "dictKey"},
                            dicUrl: "/api/blade-system/dict/dictionary?code=quality_type",
                            defaultIndex: 0,
                        },
                        {
                            prop: "qualityHours",
                            label: "保质期天数",
                            type: "inputNumber",
                        },
                        {
                            prop: "attribute3",
                            label: "临期阈值(%)",
                            type: "inputNumber",
                            max: 100,
                            min: 0
                        },
                        {
                            prop: "qualityTransport",
                            label: "几天内交货",
                            type: "inputNumber",
                            hide: true
                        },
                        {
                            prop: "qualityBy",
                            label: "几天内截至",
                            type: "inputNumber",
                            hide: true
                        },
                        {
                            prop: "qualityBest",
                            label: "几天内最佳",
                            type: "inputNumber",
                            hide: true
                        },
                    ],
                },
                {
                    label: '替代物品',
                    column: [
                        {
                            prop: "skuReplaceList",
                            type: "dynamic",
                            span: 24,
                            children: [
                                {
                                    label: "物品包装",
                                    prop: "wspId",
                                    type: "selectSkuPackage",
                                    width: '120px',
                                    props: {
                                        label: 'wspName',
                                    },
                                    show: ['wspName'],
                                    rules: [{required: true, message: "物品包装不能为空", trigger: "blur"}],
                                    cascaderItem: ["wsuId"],
                                    change: function (val, obj, scope, self) {
                                        getSkuPackage(val).then(res => {
                                            scope.row.wspName = res.data.data.wspName;
                                            // 处理级联字段
                                            if (this.cascader) {
                                                this.cascader.forEach(item => {
                                                    if (item.prop === 'wsuId') {
                                                        // 设置计量单位数据源
                                                        item.dicData = res.data.data.skuPackageDetailVOList;
                                                        if (item.dicData && item.dicData.length > 0) {
                                                            // 设置默认包装单位
                                                            let result = item.dicData.filter(item => {
                                                                return item.skuLevel == "1";
                                                            });
                                                            let packageDetail = null;
                                                            if (result && result.length > 0) {
                                                                packageDetail = result[0];
                                                            } else {
                                                                packageDetail = item.dicData[0];
                                                            }
                                                            self.onChange(packageDetail.wsuId, packageDetail, item, scope);
                                                        }
                                                    }
                                                });
                                            }
                                        })
                                    },
                                },
                                {
                                    label: "计量单位",
                                    prop: "wsuId",
                                    type: "select",
                                    props: {
                                        label: 'wsuName',
                                        value: 'wsuId'
                                    },
                                    rules: [
                                        {required: true, message: "计量单位不能为空", trigger: "blur"}
                                    ],
                                    show: ['wsuName'],
                                    change: function (val, obj, scope, self) {
                                        let detailList = this.dicData.filter(u => {
                                            return u.wsuId === val;
                                        });
                                        if (detailList && detailList.length > 0) {
                                            let detail = detailList[0];
                                            scope.row.wsuName = detail.wsuName;
                                        }
                                    }
                                },
                                {
                                    label: "物品数量",
                                    prop: "qty",
                                    type: "inputNumber",
                                    width: '150px',
                                    min: 1,
                                    rules: [
                                        {type: 'number', required: true, message: " ", trigger: "blur"}
                                    ],
                                },
                                {
                                    label: "替代物品",
                                    prop: "wsrepSkuId",
                                    type: "selectSku",
                                    width: '120px',
                                    props: {
                                        label: "wsrepSkuName"
                                    },
                                    searchData: {},
                                    beforeOpen: function (scope) {
                                        this.searchData.woId = scope._self.owner.woId;
                                    },
                                    show: ['wsrepSkuName'],
                                    rules: [{required: true, message: "替代物品不能为空", trigger: "blur"}],
                                    cascaderItem: ['wsrepWspId'],
                                    change: function (val, obj, scope, self) {
                                        if (obj) {
                                            if (scope && scope.row) {
                                                scope.row.wsrepSkuId = obj.skuId;
                                                scope.row.wsrepSkuCode = obj.skuCode;
                                                scope.row.wsrepSkuName = obj.skuName;
                                            }
                                            if (this.cascader) {
                                                this.cascader.forEach(item => {
                                                    if (item.prop === 'wsrepWspId') {
                                                        // 触发包装改变的事件
                                                        self.onChange(obj.wspId, null, item, scope);
                                                    }
                                                });
                                            }
                                        }
                                    },
                                }, {
                                    label: "替代包装",
                                    prop: "wsrepWspId",
                                    type: "selectSkuPackage",
                                    width: '120px',
                                    props: {
                                        label: 'wsrepWspName',
                                    },
                                    show: ['wsrepWspName'],
                                    rules: [{required: true, message: "替代包装不能为空", trigger: "blur"}],
                                    cascaderItem: ["wsrepWsuId"],
                                    change: function (val, obj, scope, self) {
                                        getSkuPackage(val).then(res => {
                                            scope.row.wsrepWspId = val;
                                            scope.row.wsrepWspName = res.data.data.wspName;
                                            // 处理级联字段
                                            if (this.cascader) {
                                                this.cascader.forEach(item => {
                                                    if (item.prop === 'wsrepWsuId') {
                                                        // 设置计量单位数据源
                                                        item.dicData = res.data.data.skuPackageDetailVOList;
                                                        if (item.dicData && item.dicData.length > 0) {
                                                            // 设置默认包装单位
                                                            let result = item.dicData.filter(item => {
                                                                return item.skuLevel == "1";
                                                            });
                                                            let packageDetail = null;
                                                            if (result && result.length > 0) {
                                                                packageDetail = result[0];
                                                            } else {
                                                                packageDetail = item.dicData[0];
                                                            }
                                                            self.onChange(packageDetail.wsuId, packageDetail, item, scope);
                                                        }
                                                    }
                                                });
                                            }
                                        })
                                    },
                                }, {
                                    label: "替代单位",
                                    prop: "wsrepWsuId",
                                    type: "select",
                                    //width: '100px',
                                    props: {
                                        label: 'wsuName',
                                        value: 'wsuId'
                                    },
                                    show: ['wsrepWsuName'],
                                    rules: [
                                        {required: true, message: "替代单位不能为空", trigger: "blur"}
                                    ],
                                    change: function (val, obj, scope, self) {
                                        let detailList = this.dicData.filter(u => {
                                            return u.wsuId === val;
                                        });
                                        if (detailList && detailList.length > 0) {
                                            let detail = detailList[0];
                                            scope.row.wsrepWsuName = detail.wsuName;
                                        }
                                    }
                                },
                                {
                                    label: "替代数量",
                                    prop: "wsrepQty",
                                    type: "inputNumber",
                                    width: '150px',
                                    min: 1,
                                    rules: [
                                        {type: 'number', required: true, message: " ", trigger: "blur"}
                                    ],
                                }
                            ],
                            delete: (form, row) => {
                                if (!form['skuReplaceDeletedList']) {
                                    form['skuReplaceDeletedList'] = [];
                                }
                                form['skuReplaceDeletedList'].push(row);
                            }
                        }
                    ],
                },
                {
                    label: '企业',
                    column: [
                        {
                            prop: "skuIncList",
                            type: "dynamic",
                            span: 24,
                            children: [
                                {
                                    label: "企业",
                                    prop: "peId",
                                    type: "selectEnterprise",
                                    search: {
                                        woId: "woId",   //货主级联
                                    },
                                    props: {
                                        label: 'enterpriseName'
                                    },
                                    show: ["enterpriseName"],
                                    width: 200,
                                    rules: [{required: true, message: "供应商不能为空", trigger: "blur"}],
                                    change: function (val, obj, scope, col) {
                                        getEnterpriseDetail(val).then(res => {
                                            scope.row.peId = res.data.data.peId;
                                            scope.row.enterpriseCode = res.data.data.enterpriseCode
                                            scope.row.enterpriseName = res.data.data.enterpriseName;
                                        })
                                    },
                                },
                                {
                                    label: "包装",
                                    prop: "wspId",
                                    props: {
                                        label: 'wspName',
                                    },
                                    show: ['wspName'],
                                    type: "selectSkuPackage",
                                    width: 150,
                                    rules: [{required: true, message: "包装不能为空", trigger: "blur"}],
                                    change: function (val, obj, scope, self) {
                                        getSkuPackage(val).then(res => {
                                            scope.row.wspName = res.data.data.wspName;
                                        })
                                    },
                                },
                                {
                                    label: '扩展字段1',
                                    prop: 'attribute1',
                                    maxlength: 200,
                                    width: 120
                                },
                                {
                                    label: '扩展字段2',
                                    prop: 'attribute2',
                                    maxlength: 200,
                                    width: 120
                                },
                                {
                                    label: '扩展字段3',
                                    prop: 'attribute3',
                                    maxlength: 200,
                                    width: 120
                                },
                                {
                                    label: '扩展字段4',
                                    prop: 'attribute4',
                                    maxlength: 200,
                                    width: 120
                                },
                                {
                                    label: '扩展字段5',
                                    prop: 'attribute5',
                                    maxlength: 200,
                                    width: 120
                                },
                                {
                                    label: '扩展字段6',
                                    prop: 'attribute6',
                                    maxlength: 200,
                                    width: 120
                                },
                                {
                                    label: '扩展字段7',
                                    prop: 'attribute7',
                                    maxlength: 200,
                                    width: 120
                                },
                                {
                                    label: '扩展字段8',
                                    prop: 'attribute8',
                                    maxlength: 200,
                                    width: 120
                                },
                                {
                                    label: '扩展字段9',
                                    prop: 'attribute9',
                                    maxlength: 200,
                                    width: 120
                                },
                                {
                                    label: '扩展字段10',
                                    prop: 'attribute10',
                                    maxlength: 200,
                                    width: 120
                                }
                            ],
                            delete: (form, row) => {
                                if (!form['skuIncDeletedList']) {
                                    form['skuIncDeletedList'] = [];
                                }
                                form['skuIncDeletedList'].push(row);
                            }
                        }
                    ]
                }
            ],
        };
    },
    computed: {},
    mounted() {

    },
    methods: {}
};
</script>
