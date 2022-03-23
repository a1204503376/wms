import {getDetail as getSkuDetail} from "@/api/wms/basedata/sku";
import {getDetail as getSkuPackage} from "@/api/wms/basedata/skupackage";

export const group = {
    label: "明细",
    column: [{
        prop: "detailList",
        type: "dynamic",
        span: 24,
        children: [
            {
                label: '物品名称',
                prop: 'skuId',
                type: 'selectSku',
                width: 160,
                props: {
                    label: 'skuName',
                    value: 'skuId',
                },
                show: ['skuName'],
                beforeOpen: function (self, formData, scope) {
                    self.params.woId = formData.woId;
                },
                rules: [
                    {required: true, message: "物品不能为空", trigger: "change"}
                ],
                searchData: {},
                cascaderItem: ['wspId'],
                change: function (val, obj, scope, self) {
                    if (obj) {
                        if (scope && scope.row) {
                            scope.row.skuName = obj.skuName;
                            scope.row.skuCode = obj.skuCode;
                            scope.row.isSn = obj.isSn === 0 ? '否' : '是';
                            scope.row.wspId = obj.wspId;
                        }
                        // 获取该物品的供应商关联信息
                        getSkuDetail(val).then(res => {
                            // 处理级联字段
                            if (this.cascader) {
                                this.cascader.forEach(item => {
                                    if (item.prop === 'wspId') {
                                        // 触发包装改变的事件
                                        self.onChange(obj.wspId, null, item, scope);
                                    }
                                });
                            }
                        });
                    }
                }
            },
            {
                label: '包装',
                prop: 'wspId',
                width: 160,
                type: 'selectSkuPackage',
                cascaderItem: ['umName', 'skuSpec'],
                props: {
                    value: 'wspId',
                    label: 'wspName'
                },
                disabled: true,
                show: ['wspName'],
                rules: [{required: true, message: "包装不能为空", trigger: "change"}],
                change: function (val, obj, scope, self) {
                    if (!val) {
                        return;
                    }
                    getSkuPackage(val).then(res => {
                        scope.row.wspId = val;
                        scope.row.wspName = res.data.data.wspName;
                        scope.row.skuSpec = res.data.data.spec;
                        // 处理级联字段
                        if (this.cascader) {
                            this.cascader.forEach(item => {
                                if (item.prop === 'umName') {
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
                                        self.onChange(packageDetail.wsuCode, packageDetail, item, scope);
                                    }
                                }
                            });
                        }
                    });
                }
            },
            {
                label: '计量单位',
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
                show: ['umName'],
                change: function (val, obj, scope, self) {
                    if (this.dicData) {
                        let detailList = this.dicData.filter(u => {
                            return u.wsuCode === val;
                        });
                        if (detailList && detailList.length > 0) {
                            let detail = detailList[0];
                            scope.row.umCode = detail.wsuCode;
                            scope.row.umName = detail.wsuName;
                            scope.row.wspdId = detail.wspdId;
                            // scope.row.skuSpec = detail.skuSpec;
                            scope.row.skuLevel = detail.skuLevel;
                            scope.row.skuLevelName = detail.skuLevelName;
                            scope.row.convertQty = detail.convertQty;
                        }
                    }
                }
            },
            {
                label: '层级',
                width: 100,
                prop: 'skuLevelName',
                type: 'text'
            },
            {
                label: '规格',
                prop: 'skuSpec',
                type: 'text'
            },
            {
                label: '计划数量',
                prop: 'allotPlanQty',
                type: 'inputNumber',
                min: 1,
                rules: [
                    {type: 'number', required: true, message: "计划数量不能为空", trigger: "blur"}
                ]
            }]
    }]
}
