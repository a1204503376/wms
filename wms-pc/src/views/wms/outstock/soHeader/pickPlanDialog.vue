<template>
    <editDialog ref="dialogEdit"
                v-model="form"
                title="拣货分配"
                :visible="visible"
                :isEdit="false"
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
    import {savePickPlan} from "@/api/wms/outstock/pickplan";
    import {getDetail as getParamDetail} from "@/api/core/param";
    import editDialog from "@/components/nodes/editDialog";

    export default {
        name: "pickPlanDialog",
        props: {
            visible: {type: Boolean, default: false},
            isWellen: {type: Boolean, default: false},
            dataSource: {
                type: Object, default: function () {
                    return {
                        otherStockList: [],
                        pickPlanDetailList: [],
                        pickPlanDetailErrorList: [],
                    };
                }
            }
        },
        components: {
            editDialog
        },
        watch: {
            dataSource: {
                handler: function (newVal, oldVal) {
                    this.form = Object.assign({}, newVal);
                    // if (this.form.otherStockList && this.form.otherStockList.length > 0) {
                    //     if (this.group.length == 1 && this.form.otherStockList.length > 0) {
                    //         this.group.push(this.otherStock);
                    //     } else if (this.group.length == 2) {
                    //         this.group.splice(this.group.length - 1, 1);
                    //     }
                    // }
                    if (this.form.pickPlanDetailList && this.form.pickPlanDetailList.length > 0) {
                        this.isView = false;
                    } else {
                        this.isView = true;
                    }
                },
                deep: true,
                immediate: true
            }
        },
        data() {
            return {
                form: {},
                isView: false,
                group: [
                    // {
                    //     label: '正常分配',
                    //     column: [
                    //         {
                    //             prop: "pickPlanDetailList",
                    //             type: "dynamic",
                    //             span: 24,
                    //             addBtn: true,
                    //             delBtn: true,
                    //             editBtn: true,
                    //             page: false,
                    //             selection: false,
                    //             menu: false,
                    //             children: [
                    //                 {
                    //                     label: '订单编码',
                    //                     prop: 'soBillNo',
                    //                     width: 180,
                    //                     type: 'text'
                    //                 },
                    //                 {
                    //                     label: '物品编码',
                    //                     prop: 'skuCode',
                    //                     width: 180,
                    //                     type: 'text'
                    //                 },
                    //                 {
                    //                     label: '物品名称',
                    //                     prop: 'skuName',
                    //                     width: 160,
                    //                     type: 'text'
                    //                 },
                    //                 {
                    //                     label: '计划数量',
                    //                     prop: 'planQty',
                    //                     width: 100,
                    //                     type: 'text'
                    //                 },
                    //                 {
                    //                     label: '分配数量',
                    //                     prop: 'pickPlanQty',
                    //                     width: 100
                    //                 },
                    //                 {
                    //                     label: '被替代物品编码',
                    //                     prop: 'repSkuCode',
                    //                     width: 180,
                    //                     type: 'text'
                    //                 },
                    //                 {
                    //                     label: '被替代物品编码',
                    //                     prop: 'repSkuName',
                    //                     width: 160,
                    //                     type: 'text'
                    //                 },
                    //                 {
                    //                     label: '计量单位',
                    //                     prop: 'wsuName',
                    //                     type: 'text'
                    //                 },
                    //                 {
                    //                     label: '分配策略',
                    //                     prop: 'ssoName',
                    //                     width: 120,
                    //                     type: 'text'
                    //                 },
                    //                 {
                    //                     label: '分配计算代码',
                    //                     prop: 'outstockFunctionDesc',
                    //                     width: 120,
                    //                     type: 'text'
                    //                 },
                    //                 {
                    //                     label: '货源库区',
                    //                     prop: 'sourceZoneName',
                    //                     width: 120,
                    //                     type: 'text'
                    //                 },
                    //                 {
                    //                     label: '订单明细批次号',
                    //                     prop: 'detailLotNumber',
                    //                     width: 120,
                    //                     type: 'text'
                    //                 },
                    //                 {
                    //                     label: '订单明细序列号',
                    //                     prop: 'detailSerialNumber',
                    //                     width: 120,
                    //                     type: 'text'
                    //                 },
                    //                 {
                    //                     label: '订单明细批属性',
                    //                     prop: 'detailSkuLot',
                    //                     width: 120,
                    //                     type: 'text'
                    //                 },
                    //                 {
                    //                     label: '策略批属性',
                    //                     prop: 'ssoSkuLot',
                    //                     width: 120,
                    //                     type: 'text'
                    //                 }
                    //             ]
                    //         }
                    //     ]
                    // },
                    {
                        label: '异常分配',
                        column: [
                            {
                                prop: "pickPlanDetailErrorList",
                                type: "dynamic",
                                span: 24,
                                addBtn: false,
                                delBtn: false,
                                editBtn: false,
                                page: false,
                                selection: false,
                                menu: false,
                                children: [
                                    {
                                        label: '订单编码',
                                        prop: 'soBillNo',
                                        width: 180
                                    },
                                    {
                                        label: '物品编码',
                                        prop: 'skuCode',
                                        width: 180
                                    },
                                    {
                                        label: '物品名称',
                                        prop: 'skuName',
                                        width: 160
                                    },
                                    {
                                        label: '计划数量',
                                        prop: 'planQty',
                                        width: 100
                                    },
                                    {
                                        label: '分配数量',
                                        prop: 'pickPlanQty',
                                        width: 100
                                    },
                                    {
                                        label: '缺货数量',
                                        prop: 'errorQty',
                                        width: 100
                                    },
                                    {
                                        label: '计量单位',
                                        prop: 'wsuName',
                                    },
                                    {
                                        label: '异常消息',
                                        prop: 'exception',
                                        width: 260
                                    },
                                    {
                                        label: '分配策略',
                                        prop: 'ssoName',
                                        width: 120
                                    },
                                    {
                                        label: '分配计算代码',
                                        prop: 'outstockFunctionDesc',
                                        width: 120
                                    },
                                    {
                                        label: '货源库区',
                                        prop: 'sourceZoneName',
                                        width: 120
                                    },
                                    {
                                        label: '订单明细批次号',
                                        prop: 'detailLotNumber',
                                        width: 120
                                    },
                                    {
                                        label: '订单明细序列号',
                                        prop: 'detailSerialNumber',
                                        width: 120
                                    },
                                    {
                                        label: '订单明细批属性',
                                        prop: 'detailSkuLot',
                                        width: 120
                                    },
                                    {
                                        label: '策略批属性',
                                        prop: 'ssoSkuLot',
                                        width: 120
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        label: '其他库存',
                        column: [
                            {
                                prop: "otherStockList",
                                type: "dynamic",
                                span: 24,
                                addBtn: false,
                                delBtn: false,
                                editBtn: false,
                                page: false,
                                selection: false,
                                menu: false,
                                children: [
                                    {
                                        label: "库存状态",
                                        prop: "stockStatusDesc",
                                    },
                                    {
                                        label: "所属库房",
                                        prop: "whName",
                                        width: 130,
                                        overHidden: true,
                                        sortable: true,
                                        sortProp: 'whId'
                                    },
                                    {
                                        label: "所属库区",
                                        prop: "zoneName",
                                        width: 150,
                                        overHidden: true,
                                        sortable: true,
                                        sortProp: 'zoneId'
                                    },
                                    {
                                        label: "库位编号",
                                        prop: "locCode",
                                        overHidden: true,
                                        width: 150,
                                        search: true,
                                        sortable: true,
                                        sortProp: 'locId'
                                    },
                                    {
                                        label: "货主",
                                        prop: "ownerName",
                                        overHidden: true,
                                        width: 120,
                                        sortable: true,
                                        sortProp: 'woId'
                                    },
                                    {
                                        label: "物品编码",
                                        prop: "skuCode",
                                        overHidden: true,
                                        width: 140,
                                        search: true,
                                        sortable: true,
                                        sortProp: 'skuId'
                                    },
                                    {
                                        label: "物品名称",
                                        prop: "skuName",
                                        overHidden: true,
                                        width: 200,
                                        search: true,
                                        sortable: true,
                                        sortProp: 'skuId'
                                    },
                                    {
                                        label: "包装",
                                        prop: "wspName",
                                        width: 180,
                                        sortable: true,
                                        sortProp: 'wspId',
                                        search: true
                                    },
                                    {
                                        label: "计量单位",
                                        prop: "wsuName",
                                        width: 120,
                                        overHidden: true,
                                        sortable: true,
                                        sortProp: 'skuLevel'
                                    },
                                  
                                    {
                                        label: "库存数量",
                                        prop: "stockQty",
                                        overHidden: true
                                    },
                                    {
                                        label: "下架数量",
                                        prop: "pickQty",
                                        overHidden: true
                                    },
                                    {
                                        label: "分配数量",
                                        prop: "occupyQty",
                                        overHidden: true,
                                        slot: true
                                    },
                                    {
                                        label: "批次号",
                                        prop: "lotNumber",
                                        overHidden: true,
                                        width: 170,
                                        search: true,
                                        sortable: true,
                                    },
                                    {
                                        label: "最近入库时间(库存数量增加时更新)",
                                        prop: "lastInTime",
                                        overHidden: true,
                                        width: 280,
                                        sortable: true,
                                    },
                                    {
                                        label: "最近出库时间(下架数量增加时更新)",
                                        prop: "lastOutTime",
                                        overHidden: true,
                                        width: 280,
                                        sortable: true,
                                    }
                                ]
                            }
                        ]
                    }
                ],
            }
        },
        mounted() {
            getParamDetail({paramKey: "account:lotCount"}).then(res=>{
                for (let i = 1; i <= res.data.paramValue; i++) {
                    let skuLot = {
                        label: "批属性" + i,
                        prop: "skuLot" + i,
                        width: 120,
                        maxlength: 200,
                    };
                    this.group[1].column[0].children.push(skuLot);
                }
            });
        },
        methods: {
            beforeOpen(done, type, finish) {
                done();
            },
            callback(data) {
                if (data.success) {
                    if (this.form.pickPlanDetailList.length === 0) {
                        this.$message.warning('保存明细不能为空！');
                        return;
                    }
                    if (this.form.pickPlanDetailErrorList.length > 0) {
                        this.$confirm("当前存在缺货物品，是否确认生成拣货计划？", {
                            confirmButtonText: "确定",
                            cancelButtonText: "取消",
                            type: "warning"
                        }).then(() => {
                            this.save(data);
                        }).catch(() => {
                            data.loading();
                        });
                    } else {
                        this.save(data);
                    }
                } else {
                    this.$emit('callback', data);
                }
            },
            save(data) {
                let param = {
                    isWellen: this.isWellen,
                    detailList: Object.assign([], this.form.pickPlanDetailList)
                };
                savePickPlan(param).then(res => {
                    this.$emit('callback', data);
                }).finally(() => {
                    data.loading();
                });
            }
        }
    }
</script>

<style scoped>

</style>
