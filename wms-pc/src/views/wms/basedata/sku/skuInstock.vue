<template>
    <editDialog ref="dialogEdit"
                title="物品入库设置"
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
import {getList} from "@/api/wms/basedata/skuInstock";
import {getList as getZoneList} from "@/api/wms/warehouse/zone";
import {getList as getLocationList} from "@/api/wms/warehouse/location";
import {getList as getInstockList} from "@/api/wms/strategy/instock";

export default {
    name: "instock",
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
            name: 'skuInstock',
            dialogEdit: {
                visible: false,
                isView: false,
                isEdit: true,
                group: [
                    {
                        column: [
                            {
                                prop: "skuInstock",
                                type: "dynamic",
                                span: 24,
                                saveUrl: '/api/wms/basedata/skuInstock/submit',
                                del: {
                                    url: '/api/wms/basedata/skuInstock/remove',
                                    id: 'wsiId'
                                },
                                rowAdd: function (row, self) {
                                    row.skuId = self.dataSource.skuId;
                                    row.woId = self.dataSource.woId;
                                },
                                rowSaved: function (self) {
                                    self.loading = true;
                                    getInstockList().then(res => {
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
                                            'ssiId',
                                            'zoneId',
                                            'qcZoneId',
                                            'unqualiflyZoneId',
                                            'returnZoneId',
                                            'pickZoneId'
                                        ],
                                        show: ['whName'],
                                        width: 150,
                                        change: function (val, obj, scope, self) {
                                            if (obj) {
                                                scope.row.whName = obj.whName;
                                            }
                                            // 更新子级数据源
                                            if (this.cascader) {
                                                let skuInstockSource = [
                                                    'zoneId',
                                                    'qcZoneId',
                                                    'unqualiflyZoneId',
                                                    'returnZoneId',
                                                    'pickZoneId'
                                                ];
                                                this.cascader.forEach(item => {
                                                    if (item.prop === 'ssiId') {
                                                        // 更改上架策略的数据源
                                                        getInstockList({
                                                            whId: val,
                                                        }).then(res => {
                                                            item.dicData = res.data.data;
                                                        });
                                                    } else if (skuInstockSource.includes(item.prop)) {
                                                        // 修改所有库区数据源
                                                        getZoneList({whId: val}).then(res => {
                                                            item.dicData = res.data.data;
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    },
                                    {
                                        label: "上架策略",
                                        prop: "ssiId",
                                        type: "select",
                                        defaultIndex: 0,
                                        width: 150,
                                        props: {
                                            value: 'ssiId',
                                            label: 'ssiName'
                                        },
                                        show: ['ssiName'],
                                        clearable: true,
                                        change: function (val, obj, scope, self) {
                                            if (obj) {
                                                scope.row.ssiName = obj.ssiName;
                                            } else {
                                                scope.row.ssiName = '';
                                            }
                                        }
                                    },
                                    {
                                        label: "上架策略类型",
                                        prop: "ssiProcType",
                                        type: "select",
                                        width: '150px',
                                        props: {
                                            value: 'dictKey',
                                            label: 'dictValue'
                                        },
                                        dicUrl: '/api/blade-system/dict/dictionary?code=' + this.$dict.ssiProcType,
                                        show: ['ssiProcTypeName'],
                                        clearable: true,
                                        change: function (val, obj, scope, self) {
                                            if (obj) {
                                                scope.row.ssiProcTypeName = obj.dictValue;
                                            } else {
                                                scope.row.ssiProcTypeName = '';
                                            }
                                        }
                                    },
                                    {
                                        label: "上架库区",
                                        prop: "zoneId",
                                        type: "select",
                                        width: '150px',
                                        props: {
                                            label: 'zoneName',
                                            value: 'zoneId'
                                        },
                                        cascaderItem: ['locId'],
                                        show: ['zoneName'],
                                        clearable: true,
                                        change: function (val, obj, scope, self) {
                                            if (obj) {
                                                scope.row.zoneName = obj.zoneName;
                                            } else {
                                                scope.row.zoneName = '';
                                            }
                                            if (!val) {
                                                return;
                                            }
                                            if (this.cascader) {
                                                this.cascader.forEach(item => {
                                                    if (item.prop === 'locId') {
                                                        scope.row.locCode = "";
                                                        getLocationList({zoneId:val}).then(res => {
                                                            item.dicData = res.data.data;
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    },
                                    {
                                        label: "上架库位",
                                        prop: "locId",
                                        type: "select",
                                        width: '150px',
                                        show: ['locCode'],
                                        props: {
                                            label: 'locCode',
                                            value: 'locId'
                                        },
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
                                            if (!val) {
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
                                            if (!val) {
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
                                        label: "退货库区",
                                        prop: "returnZoneId",
                                        type: "select",
                                        width: '150px',
                                        props: {
                                            label: "zoneName",
                                            value: "zoneId"
                                        },
                                        cascaderItem: ['returnLocId'],
                                        show: ['returnZoneName'],
                                        clearable: true,
                                        change: function (val, obj, scope, self) {
                                            if (obj) {
                                                scope.row.returnZoneName = obj.zoneName;
                                            } else {
                                                scope.row.returnZoneName = '';
                                            }
                                            if (!val) {
                                                return;
                                            }
                                            if (this.cascader) {
                                                this.cascader.forEach(item => {
                                                    if (item.prop === 'returnLocId') {
                                                        getLocationList({zoneId:val}).then(res => {
                                                            item.dicData = res.data.data;
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    },
                                    {
                                        label: "退货库位",
                                        prop: "returnLocId",
                                        type: "select",
                                        width: '150px',
                                        props: {
                                            label: 'locCode',
                                            value: 'locId'
                                        },
                                        show: ['returnLocCode'],
                                        clearable: true,
                                        change: function (val, obj, scope, self) {
                                            if (obj) {
                                                scope.row.returnLocCode = obj.locCode;
                                            } else {
                                                scope.row.returnLocCode = '';
                                            }
                                        }
                                    },
                                    {
                                        label: "拣货货区",
                                        prop: "pickZoneId",
                                        type: "select",
                                        width: '150px',
                                        props: {
                                            label: "zoneName",
                                            value: "zoneId"
                                        },
                                        cascaderItem: ['pickLocId'],
                                        show: ['pickZoneName'],
                                        clearable: true,
                                        change: function (val, obj, scope, self) {
                                            if (obj) {
                                                scope.row.pickZoneName = obj.zoneName;
                                            } else {
                                                scope.row.pickZoneName = '';
                                            }
                                            if (!val) {
                                                return;
                                            }
                                            if (this.cascader) {
                                                this.cascader.forEach(item => {
                                                    if (item.prop === 'pickLocId') {
                                                        getLocationList({zoneId:val}).then(res => {
                                                            item.dicData = res.data.data;
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    },
                                    {
                                        label: "拣货货位",
                                        prop: "pickLocId",
                                        type: "select",
                                        width: '150px',
                                        props: {
                                            label: 'locCode',
                                            value: 'locId'
                                        },
                                        show: ['pickLocCode'],
                                        clearable: true,
                                        change: function (val, obj, scope, self) {
                                            if (obj) {
                                                scope.row.pickLocCode = obj.locCode;
                                            } else {
                                                scope.row.pickLocCode = '';
                                            }
                                        }
                                    },
                                    {
                                        label: "库房高储",
                                        prop: "highStorage",
                                        type: "inputnumber",
                                        width: '150px',
                                    },
                                    {
                                        label: "库房低储",
                                        prop: "lowStorage",
                                        type: "inputnumber",
                                        width: '150px',
                                    },
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
                skuId: this.dataSource.skuId,
            }).then(res => {
                let dataSource = {
                    skuInstock: res.data.data
                }
                this.dialogEdit.dataSource = dataSource;
            }).finally(() => {
                done();
            });
        }
    }
}
</script>

<style scoped>

</style>
