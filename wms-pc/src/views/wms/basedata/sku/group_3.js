import {getDetail as getSkuPackage} from "@/api/wms/basedata/skupackage";

export const group = {
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
                                            self.onChange(packageDetail.wsuId, packageDetail, item, scope, false);
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
                        let detail = undefined;
                        if (val) {
                            detail = this.dicData.find(u => u.wsuId == val);
                        } else {
                            detail = this.dicData && this.dicData.length > 0 ? this.dicData[0] : undefined;
                        }
                        if (detail) {
                            scope.row.wsuId = detail.wsuId;
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
                    beforeOpen: function (self, formData, scope) {
                        self.params.woId = formData.woId;
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
                        if (!val) {
                            return;
                        }
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
                        if (this.dicData) {
                            let detailList = this.dicData.filter(u => {
                                return u.wsuId === val;
                            });
                            if (detailList && detailList.length > 0) {
                                let detail = detailList[0];
                                scope.row.wsrepWsuName = detail.wsuName;
                            }
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
}
