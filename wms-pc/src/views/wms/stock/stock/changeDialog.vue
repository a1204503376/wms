<template>
    <editDialog ref="dialogEdit"
                v-model="form"
                title="内部转移"
                :visible="visible"
                :isEdit="true"
                :isView="isView"
                :group="group"
                :dataSource="form"
                width="80%"
                :root="$parent"
                @before-open="beforeOpen"
                @callback="callback"
    ></editDialog>
</template>

<script>
import {internalTransfer} from "@/api/wms/core/stock";
import {getParamValue} from "@/util/param";
import editDialog from "@/components/nodes/editDialog";
import {getList as getZoneList} from "@/api/wms/warehouse/zone";
import {getListByZoneId} from "@/api/wms/warehouse/location";
import {getDetail as getSkuPackage} from "@/api/wms/basedata/skupackage";
import fileDownload from "js-file-download";

export default {
    name: "changeDialog",
    props: {
        visible: {type: Boolean, default: false},
        dataSource: {
            type: Object, default: function () {
                return {
                    stockList: [],
                };
            }
        }
    },
    watch: {
        dataSource: {
            handler: function (newVal, oldVal) {
                this.form = Object.assign({}, newVal);
            },
            deep: true,
            immediate: true
        }
    },
    components: {
        editDialog
    },
    data() {
        return {
            form: {},
            isView: false,
            group: [
                {
                    label: '库存信息',
                    column: [
                        {
                            prop: "stockList",
                            type: "dynamic",
                            span: 24,
                            addBtn: false,
                            delBtn: false,
                            editBtn: true,
                            page: false,
                            selection: false,
                            menu: true,
                            children: [
                                {
                                    label: '库存状态',
                                    prop: 'stockStatusDesc',
                                    type: 'text',
                                    fixed: true
                                },
                                {
                                    label: "所属库房",
                                    prop: "whName",
                                    width: 130,
                                    type: 'text',
                                    overHidden: true,
                                },
                                {
                                    label: "所属库区",
                                    prop: "zoneName",
                                    width: 150,
                                    overHidden: true,
                                    type: 'text',
                                    // cascaderItem: ['locCode'],
                                    // change: function (val, obj, scope, self) {
                                    //     getListByZoneId(scope.row.zoneId).then(res => {
                                    //         // 处理级联字段
                                    //         if (this.cascader) {
                                    //             this.cascader.forEach(item => {
                                    //                 if (item.prop === 'locCode') {
                                    //                     item.dicData = res.data.data;
                                    //                 }
                                    //             });
                                    //         }
                                    //     });
                                    // }
                                },
                                {
                                    label: "库位编号",
                                    prop: "locCode",
                                    overHidden: true,
                                    type: 'select',
                                    width: 150,
                                    dicUrl: "/api/wms/warehouse/location/list",
                                    props: {
                                        label: 'locCode',
                                        value: 'locId'
                                    },
                                    change: function (val, obj, scope, self) {
                                        let detailList = this.dicData.filter(u => {
                                            return u.locId === val;
                                        });
                                        if (detailList && detailList.length > 0) {
                                            let detail = detailList[0];
                                            scope.row.locId = detail.locId;
                                            scope.row.locCode = detail.locCode;
                                        }
                                    }
                                },
                                {
                                    label: "容器编码",
                                    prop: "lpnCode",
                                    overHidden: true,
                                    width: 120,
                                    type: 'text',
                                },
                                {
                                    label: "货主",
                                    prop: "ownerName",
                                    overHidden: true,
                                    type: "select",
                                    width: 200,
                                    dicUrl: "/api/wms/basedata/owner/list",
                                    props: {
                                        label: "ownerName",
                                        value: "woId"
                                    },
                                    change: function (val, obj, scope, self) {
                                        let detailList = this.dicData.filter(u => {
                                            return u.woId === val;
                                        });
                                        if (detailList && detailList.length > 0) {
                                            let detail = detailList[0];
                                            scope.row.woId = detail.woId;
                                            scope.row.ownerName = detail.ownerName;
                                        }
                                    }
                                },
                                {
                                    label: "物品编码",
                                    prop: "skuCode",
                                    overHidden: true,
                                    width: 140,
                                    type: 'text',
                                },
                                {
                                    label: "物品名称",
                                    prop: "skuName",
                                    overHidden: true,
                                    width: 200,
                                    type: 'selectSku',
                                    props: {
                                        value: 'skuName',
                                        label: 'skuName'
                                    },
                                    searchData: {},
                                    beforeOpen: function (self, formData, scope) {
                                        self.params.woId = formData.woId;
                                    },
                                    change: function (val, obj, scope, self) {
                                        if (!obj) {
                                            return;
                                        }
                                        if (scope && scope.row) {
                                            scope.row.skuId = obj.skuId;
                                            scope.row.skuCode = obj.skuCode;
                                        }
                                    }
                                },
                                {
                                    label: "包装",
                                    prop: "wspId",
                                    show: ['wspName'],
                                    width: 180,
                                    type: 'selectSkuPackage',
                                    props: {
                                        label: 'wspName',
                                        value: 'wspId'
                                    },
                                    cascaderItem: ['wsuName'],
                                    change: function (val, obj, scope, self) {
                                        if (!val) {
                                            return;
                                        }
                                        getSkuPackage(val).then(res => {
                                            const data = res.data.data;
                                            scope.row.wspId = val;
                                            scope.row.wspName = data.wspName;
                                            // 处理级联字段
                                            if (this.cascader) {
                                                this.cascader.forEach(item => {
                                                    if (item.prop === 'wsuName') {
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
                                    label: "计量单位",
                                    prop: "wsuName",
                                    width: 120,
                                    overHidden: true,
                                    type: 'select',
                                    props: {
                                        label: 'wsuName',
                                        value: 'wsuCode'
                                    },
                                    change: function (val, obj, scope, self) {
                                        let detailList = this.dicData.filter(u => {
                                            return u.wsuCode === val;
                                        });
                                        if (detailList && detailList.length > 0) {
                                            let detail = detailList[0];
                                            scope.row.wsuCode = detail.wsuCode;
                                            scope.row.wsuName = detail.wsuName;
                                            scope.row.wspdId = detail.wspdId;
                                            scope.row.skuLevel = detail.skuLevel;
                                        }
                                    }
                                },
                                {
                                    label: "库存数量",
                                    prop: "stockQty",
                                    overHidden: true,
                                    width: 120,
                                },
                                {
                                    label: "下架数量",
                                    prop: "pickQty",
                                    overHidden: true,
                                    width: 120,
                                    type: 'text'
                                },
                                {
                                    label: "分配数量",
                                    prop: "occupyQty",
                                    overHidden: true,
                                    width: 120,
                                    type: 'text'
                                },
                                {
                                    label: "盘点占用数量",
                                    prop: "countOccupyQty",
                                    overHidden: true,
                                    width: 120,
                                    type: 'text'
                                },
                                {
                                    label: "批次号",
                                    prop: "lotNumber",
                                    overHidden: true,
                                    width: 170,
                                    search: true,
                                    sortable: true,
                                    type: 'text',
                                },
                                {
                                    label: '批次状态',
                                    prop: 'lotStatusDesc',
                                    slot: true,
                                    type: 'text',
                                },
                            ]
                        }
                    ]
                },
            ],
        }
    },
    mounted() {
        let lotCount = getParamValue(this.$param.system.lotCount);
        for (let i = 1; i <= lotCount; i++) {
            let skuLot = {
                label: "批属性" + i,
                prop: "skuLot" + i,
                width: 200,
                maxlength: 200,
            };
            this.group[0].column[0].children.push(skuLot);
        }
    },
    methods: {
        beforeOpen(done, type, finish) {
            done();
        },
        callback(data) {
            if (data.success) {
                this.$confirm("修改后将会生成对应出入库单据，是否确认修改？", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    this.save(data);
                }).catch(() => {
                    data.loading();
                });

            } else {
                this.$emit('callback', data);
            }
        },
        save(data) {
            console.log(data.data.stockList)
            internalTransfer(data.data.stockList).then(res => {
                this.$message.success('操作成功');
            }).catch(() => {
                this.$message.error("操作失败")
            }).finally(() => {
                this.loading = false;
                this.$emit('callback', data);
            });

        }
    }
}
</script>

<style scoped>

</style>
