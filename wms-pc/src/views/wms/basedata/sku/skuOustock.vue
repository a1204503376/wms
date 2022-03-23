<template>
    <editDialog ref="dialogEdit"
                title="物品出库设置"
                :visible="dialogEdit.visible"
                :isView="dialogEdit.isView"
                :isEdit="dialogEdit.isEdit"
                :group="dialogEdit.group"
                :dataSource="dialogEdit.dataSource"
                :width="dialogEdit.width"
                :root="this"
                :saveBtn="false"
                @callback="callback"
                @before-open="beforeOpen"
    ></editDialog>
</template>

<script>
    import editDialog from "@/components/nodes/editDialog";
    import {getList} from "@/api/wms/basedata/skuOutstock";
    import {getList as getZoneList} from "@/api/wms/warehouse/zone";
    import {getList as getLocationList} from "@/api/wms/warehouse/location";
    import {getList as getOutstockList} from "@/api/wms/strategy/outstock";
    import {getDetail as getSkuPackageDetail} from "@/api/wms/basedata/skupackage"

    export default {
        name: "skuOutstock",
        components: {
            editDialog
        },
        props: {
            visible: {type: Boolean, default: false},
            dataSource: {
                type: Object, default: function () {
                    return {
                        skuId: '',
                        woId: ''
                    }
                }
            }
        },
        watch: {
            visible: function (newValue, oldValue) {
                this.dialogEdit.visible = newValue;
            }
        },
        data() {
            return {
                name:'skuOutstock',
                dialogEdit: {
                    visible: false,
                    isView: false,
                    isEdit: true,
                    group: [
                        {
                            column: [
                                {
                                    prop: "outstockList",
                                    type: "dynamic",
                                    span: 24,
                                    saveUrl: '/api/wms/basedata/skuOutstock/submit',
                                    del: {
                                        url: '/api/wms/basedata/skuOutstock/remove',
                                        id: 'wsoId'
                                    },
                                    rowAdd: function (row, self) {
                                        row.skuId = self.dataSource.skuId;
                                        row.woId = self.dataSource.woId;
                                    },
                                    rowSaved: function(self) {
                                        self.loading = true;
                                        getOutstockList({
                                            skuId: self.dataSource.skuId
                                        }).then(res => {
                                            self.dialogEdit.dataSource = res.data.data;
                                        }).finally(() => {
                                            self.loading = false;
                                        });
                                    },
                                    children: [
                                        {
                                            label: "库房",
                                            prop: "whId",
                                            type: "select",
                                            dicUrl: "/api/wms/warehouse/warehouse/list",
                                            props: {
                                                label: "whName",
                                                value: "whId"
                                            },
                                            rules: [{required: true, message: "库房不能为空", trigger: "change"}],
                                            cascaderItem: [
                                                'ssoId',
                                                'firstSoZoneId',
                                                'qcZoneId',
                                                'unqualiflyZoneId'
                                            ],
                                            show: ['whName'],
                                            width: 150,
                                            change: function (val, obj, scope, self) {
                                                if (obj) {
                                                    scope.row.whName = obj.whName;
                                                }
                                                // 更新子级数据源
                                                if (this.cascader) {
                                                    let zoneSource = ['firstSoZoneId', 'qcZoneId', 'unqualiflyZoneId'];
                                                    this.cascader.forEach(item => {
                                                        if (item.prop === 'ssoId') {
                                                            // 更改分配策略的数据源
                                                            getOutstockList({
                                                                whId: val,
                                                            }).then(res => {
                                                                item.dicData = res.data.data;
                                                            });
                                                        } else if (zoneSource.includes(item.prop)) {
                                                            // 修改所有库区数据源
                                                            getZoneList({whId:val}).then(res => {
                                                                item.dicData = res.data.data;
                                                            });
                                                        }
                                                    });
                                                }
                                            }
                                        },
                                        {
                                            label: "分配策略",
                                            prop: "ssoId",
                                            type: "select",
                                            defaultIndex: 0,
                                            width: 150,
                                            props: {
                                                value: 'ssoId',
                                                label: 'ssoName'
                                            },
                                            show: ['ssoName'],
                                            clearable: true,
                                            change: function (val, obj, scope, self) {
                                                if (obj) {
                                                    scope.row.ssoName = obj.ssoName;
                                                } else {
                                                    scope.row.ssoName = '';
                                                }
                                            }
                                        },
                                        {
                                            label: "默认出库库区",
                                            prop: "firstSoZoneId",
                                            type: "select",
                                            width: '150px',
                                            cascaderItem: ['locId'],
                                            props: {
                                                value: 'zoneId',
                                                label: 'zoneName'
                                            },
                                            show: ['firstSoZoneName'],
                                            clearable: true,
                                            change: function (val, obj, scope, self) {
                                                if (obj) {
                                                    scope.row.firstSoZoneName = obj.zoneName;
                                                } else {
                                                    scope.row.firstSoZoneName = '';
                                                }
                                                if (!val) {
                                                    return;
                                                }
                                                if (this.cascader) {
                                                    this.cascader.forEach(item => {
                                                        if (item.prop === 'locId') {
                                                            getLocationList({zoneId:val}).then(res => {
                                                                item.dicData = res.data.data;
                                                            });
                                                        }
                                                    });
                                                }
                                            }
                                        },
                                        {
                                            label: "默认出库库位",
                                            prop: "locId",
                                            type: "select",
                                            width: '150px',
                                            props: {
                                                label: 'locCode',
                                                value: 'locId'
                                            },
                                            show: ['locCode'],
                                            clearable: true,
                                            change: function (val, obj, scope, self) {
                                                if (obj) {
                                                    scope.row.locCode = obj.locCode;
                                                } else {
                                                    scope.row.locCode = '';
                                                }
                                            }
                                        },
                                        {
                                            label: "默认出库包装",
                                            prop: "wspId",
                                            type: "selectSkuPackage",
                                            width: '150px',
                                            cascaderItem: ['skuLevel'],
                                            props: {
                                                label: 'wspName',
                                                value: 'wspId'
                                            },
                                            show: ['wspName'],
                                            clearable: true,
                                            change: function (val, obj, scope, self) {
                                                if (obj) {
                                                    scope.row.wspName = obj.wspName;
                                                } else {
                                                    scope.row.wspName = '';
                                                }
                                                if (!val) {
                                                    return;
                                                }
                                                if (this.cascader) {
                                                    this.cascader.forEach(item => {
                                                        // 获取包装层级
                                                        getSkuPackageDetail(val).then(res => {
                                                            let dicData = [];
                                                            res.data.data.skuPackageDetailVOList.forEach(v => {
                                                                dicData.push({
                                                                    label: v.skuLevelName,
                                                                    value: v.skuLevel
                                                                });
                                                            })
                                                            item.dicData = dicData;
                                                        });
                                                    });
                                                }
                                            }
                                        },
                                        {
                                            label: "默认出库层级",
                                            prop: "skuLevel",
                                            type: "select",
                                            width: '150px',
                                            show: ['skuLevelDesc'],
                                            clearable: true,
                                            change: function (val, obj, scope, self) {
                                                if (obj) {
                                                    scope.row.skuLevelDesc = obj.label;
                                                } else {
                                                    scope.row.skuLevelDesc = '';
                                                }
                                            }
                                        },
                                        {
                                            label: "质检库区",
                                            prop: "qcZoneId",
                                            type: "select",
                                            width: '150px',
                                            props: {
                                                value: 'zoneId',
                                                label: 'zoneName'
                                            },
                                            cascaderItem: ['qcLocId'],
                                            show: ['qcZoneName'],
                                            clearable: true,
                                            change: function (val, obj, scope, self) {
                                                if (obj) {
                                                    scope.row.qcZoneName = obj.zoneName;
                                                } else {
                                                    scope.row.qcZoneName = '';
                                                }
                                                if (!val){
                                                    return;
                                                }
                                                if (this.cascader) {
                                                    this.cascader.forEach(item => {
                                                        if (item.prop === 'qcLocId') {
                                                            getLocationList({zoneId:val}).then(res => {
                                                                item.dicData = res.data.data;
                                                            });
                                                        }
                                                    });
                                                }
                                            }
                                        },
                                        {
                                            label: "质检库位",
                                            prop: "qcLocId",
                                            type: "select",
                                            width: 150,
                                            props: {
                                                label: 'locCode',
                                                value: 'locId'
                                            },
                                            show: ['qcLocCode'],
                                            clearable: true,
                                            change: function (val, obj, scope, self) {
                                                if (obj) {
                                                    scope.row.qcLocCode = obj.locCode;
                                                } else {
                                                    scope.row.qcLocCode = '';
                                                }
                                            }
                                        },
                                        {
                                            label: "不合格品库区",
                                            prop: "unqualiflyZoneId",
                                            type: "select",
                                            width: '150px',
                                            cascaderItem: ['unqualiflyLocId'],
                                            props: {
                                                value: 'zoneId',
                                                label: 'zoneName'
                                            },
                                            show: ['unqualifiyZoneName'],
                                            clearable: true,
                                            change: function (val, obj, scope, self) {
                                                if (obj) {
                                                    scope.row.unqualifiyZoneName = obj.zoneName;
                                                } else {
                                                    scope.row.unqualifiyZoneName = '';
                                                }
                                                if (!val){
                                                    return;
                                                }
                                                if (this.cascader) {
                                                    this.cascader.forEach(item => {
                                                        if (item.prop === 'unqualiflyLocId') {
                                                            getLocationList({zoneId:val}).then(res => {
                                                                item.dicData = res.data.data;
                                                            });
                                                        }
                                                    });
                                                }
                                            }
                                        },
                                        {
                                            label: "不合格品库位",
                                            prop: "unqualiflyLocId",
                                            type: "select",
                                            width: '150px',
                                            props: {
                                                label: 'locCode',
                                                value: 'locId'
                                            },
                                            show: ['unqualifiyLocCode'],
                                            clearable: true,
                                            change: function (val, obj, scope, self) {
                                                if (obj) {
                                                    scope.row.unqualifiyLocCode = obj.locCode;
                                                } else {
                                                    scope.row.unqualifiyLocCode = '';
                                                }
                                            }
                                        },
                                        {
                                            label: "周转方式1",
                                            prop: "turnoverType1",
                                            type: "select",
                                            width: '150px',
                                            dicUrl: '/api/blade-system/dict/dictionary?code=' + this.$dict.turnoverType,
                                            props: {
                                                label: 'dictValue',
                                                value: 'dictKey'
                                            },
                                            show: ['turnoverType1Desc'],
                                            clearable: true,
                                            change: function (val, obj, scope, self) {
                                                if (obj) {
                                                    scope.row.turnoverType1Desc = obj.dictValue;
                                                } else {
                                                    scope.row.turnoverType1Desc = '';
                                                }
                                            }
                                        },
                                        {
                                            label: "周转类型1",
                                            prop: "turnoverItem1",
                                            type: "select",
                                            width: '150px',
                                            dicUrl: '/api/blade-system/dict/dictionary?code=' + this.$dict.turnoverItem,
                                            props: {
                                                label: 'dictValue',
                                                value: 'dictKey'
                                            },
                                            show: ['turnoverItem1Desc'],
                                            clearable: true,
                                            change: function (val, obj, scope, self) {
                                                if (obj) {
                                                    scope.row.turnoverItem1Desc = obj.dictValue;
                                                } else {
                                                    scope.row.turnoverItem1Desc = '';
                                                }
                                            }
                                        },
                                        {
                                            label: "周转方式2",
                                            prop: "turnoverType2",
                                            type: "select",
                                            width: '150px',
                                            dicUrl: '/api/blade-system/dict/dictionary?code=' + this.$dict.turnoverType,
                                            props: {
                                                label: 'dictValue',
                                                value: 'dictKey'
                                            },
                                            show: ['turnoverType2Desc'],
                                            clearable: true,
                                            change: function (val, obj, scope, self) {
                                                if (obj) {
                                                    scope.row.turnoverType2Desc = obj.dictValue;
                                                } else {
                                                    scope.row.turnoverType2Desc = '';
                                                }
                                            }
                                        },
                                        {
                                            label: "周转类型2",
                                            prop: "turnoverItem2",
                                            type: "select",
                                            width: '150px',
                                            dicUrl: '/api/blade-system/dict/dictionary?code=' + this.$dict.turnoverItem,
                                            props: {
                                                label: 'dictValue',
                                                value: 'dictKey'
                                            },
                                            show: ['turnoverItem2Desc'],
                                            clearable: true,
                                            change: function (val, obj, scope, self) {
                                                if (obj) {
                                                    scope.row.turnoverItem2Desc = obj.dictValue;
                                                } else {
                                                    scope.row.turnoverItem2Desc = '';
                                                }
                                            }
                                        },
                                        {
                                            label: "周转方式3",
                                            prop: "turnoverType3",
                                            type: "select",
                                            width: '150px',
                                            dicUrl: '/api/blade-system/dict/dictionary?code=' + this.$dict.turnoverType,
                                            props: {
                                                label: 'dictValue',
                                                value: 'dictKey'
                                            },
                                            show: ['turnoverType3Desc'],
                                            clearable: true,
                                            change: function (val, obj, scope, self) {
                                                if (obj) {
                                                    scope.row.turnoverType3Desc = obj.dictValue;
                                                } else {
                                                    scope.row.turnoverType3Desc = '';
                                                }
                                            }
                                        },
                                        {
                                            label: "周转类型3",
                                            prop: "turnoverItem3",
                                            type: "select",
                                            width: '150px',
                                            dicUrl: '/api/blade-system/dict/dictionary?code=' + this.$dict.turnoverItem,
                                            props: {
                                                label: 'dictValue',
                                                value: 'dictKey'
                                            },
                                            show: ['turnoverItem3Desc'],
                                            clearable: true,
                                            change: function (val, obj, scope, self) {
                                                if (obj) {
                                                    scope.row.turnoverItem3Desc = obj.dictValue;
                                                } else {
                                                    scope.row.turnoverItem3Desc = '';
                                                }
                                            }
                                        }
                                    ]
                                }
                            ]
                        }
                    ],
                    width: '80%',
                    dataSource: {}
                }
            }
        },
        created() {
        },
        methods: {
            callback(res) {
                this.$emit('callback', res);
            },
            beforeOpen(done, type, finish) {
                getList({
                    skuId: this.dataSource.skuId
                }).then(res => {
                    let dataSource = {
                        outstockList: res.data.data
                    }
                    this.dialogEdit.dataSource = dataSource;
                }).finally(() => {
                    done();
                });
            },
        }
    }
</script>

<style scoped>

</style>
