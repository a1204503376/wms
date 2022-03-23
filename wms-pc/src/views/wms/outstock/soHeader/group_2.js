import {getSkuPackage as getSkuIncPackage} from "@/api/wms/basedata/skuInc";
import {getDetail as getSkuPackage} from "@/api/wms/basedata/skupackage";
import {getConfig as getSkuLotConfig} from "@/api/wms/basedata/skulot";

export const group = {
    label: '明细',
    column: [{
        prop: "detailList",
        type: "dynamic",
        span: 24,
        children: [
            {
                label: '行号',
                prop: 'soLineNo',
                type: 'text',
                default: function (data) {
                    let lastLineNo = 1;
                    if (data && data.length > 0) {
                        // 找到明细中最大的行号
                        let maxNo = 0;
                        data.forEach(item => {
                            let no = parseInt(item.soLineNo);
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
                label: '拣货状态',
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
                    value: 'skuId',
                    label: 'skuName'
                },
                show:['skuName'],
                searchData: {},
                beforeOpen: function (self, formData, scope) {
                    self.params.woId = formData.woId;
                },
                rules: [
                    {required: true, message: "物品不能为空", trigger: "change"}
                ],
                cascaderItem: ['wspId'],
                change: function (val, obj, scope, self) {
                    if (!obj) {
                        return;
                    }
                    if (scope && scope.row) {
                        scope.row.skuId = obj.skuId;
                        scope.row.skuCode = obj.skuCode;
                        scope.row.skuName = obj.skuName;
                        scope.row.isSn = obj.isSn === 0 ? '否' : '是';
                        scope.row.wspId = obj.wspId;
                        scope.row.skuType = obj.typeName;
                    }
                    // 获取该物品的供应商关联信息
                    getSkuIncPackage({
                        skuId: obj.skuId,
                        peId: self.table.formData.cId
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
                    });

                    if (!self || !self.formData) {
                        return;
                    }
                    // 设置批属性列名
                    if (self.formData.detailList.length === 0) {
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
                                        } else {
                                            column.type = 'text';
                                        }
                                        column.label = skuLot.skuLot;
                                        break;
                                    }
                                }
                            });
                        });
                    }
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
                show:['umName'],
                rules: [
                    {required: true, message: "计量单位不能为空", trigger: "blur"}
                ],
                change: function (val, obj, scope, self) {
                    if (!this.dicData || this.dicData.length == 0) {
                        return;
                    }
                    let detailList = this.dicData.filter(u => {
                        return u.wsuCode === val;
                    });
                    if (detailList && detailList.length > 0) {
                        let detail = detailList[0];
                        scope.row.umCode = detail.wsuCode;
                        scope.row.umName = detail.wsuName;
                        scope.row.wspdId = detail.wspdId;
                        scope.row.skuLevel = detail.skuLevel;
                        scope.row.skuLevelName = detail.skuLevelName;
                        scope.row.convertQty = detail.convertQty;
                    }
                }
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
                width: 120,
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
                label: '实出数量',
                width: 120,
                prop: 'scanQty',
                type: 'text',
                default: 0
            },
            {
                label: '剩余数量',
                width: 120,
                prop: 'surplusQty',
                type: 'text',
                default: 0
            },
            {
                label: 'SN管理',
                width: 120,
                prop: 'isSn',
                type: 'text'
            },
            {
                label: '指定SN出库',
                width: 120,
                prop: 'serialNumber',
                type: 'text'
            }
        ]
    }]
}
