<template>
    <editDialog ref="dialogEdit"
                v-model="form"
                title="订单明细"
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
    import editDialog from "@/components/nodes/editDialog";

    let selectionList = [];

    export default {
        name: "soDetailPickPlan",
        props: {
            visible: {type: Boolean, default: false},
            dataSource: {
                type: Object, default: function () {
                    return {
                        soDetailList: [],
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
                    this.form = Object.assign({}, this.dataSource);
                },
                deep: true,
                immediate: true
            }
        },
        data() {
            return {
                form: {},
                isView: false,
                selectionList: [],
                group: [
                    {
                        label: '订单明细',
                        column: [
                            {
                                prop: "soDetailList",
                                type: "dynamic",
                                span: 24,
                                addBtn: false,
                                delBtn: false,
                                editBtn: false,
                                page: false,
                                selection: true,
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
                                ],
                                selectionChange(val, form, self) {
                                    selectionList = Object.assign([], val);
                                }
                            }
                        ]
                    }
                ]
            }
        },
        methods: {
            beforeOpen(done, type, finish) {
                done();
            },
            callback(data) {
                if (data.success) {
                    data.data = selectionList;
                }
                this.$emit('callback', data);
            }
        }
    }
</script>

<style scoped>

</style>
