<template>
    <editDialog ref="dialogEdit"
                v-model="form"
                title="订单详情"
                :visible="visible"
                :isEdit="false"
                :isView="true"
                :group="group"
                :dataSource="form"
                width="80%"
                :root="$parent"
                @before-open="beforeOpen"
                @callback="callback"
    ></editDialog>
</template>

<script>
    import EditDialog from "@/components/nodes/editDialog";
    import {getNeedSkuDetail} from "@/api/wms/stock/transferheader";

    export default {
        name: "billCount",
        props: {
            visible: {type: Boolean, default: false},
            data: {type: Object, default: {}}
        },
        components: {EditDialog},
        watch: {
            dataSource: {
                handler: function (newVal, oldVal) {
                    this.form = {
                        detailList: newVal,
                    }
                },
                deep: true,
                immediate: true
            }
        },
        data() {
            return {
                form: {},
                dataSource: [],
                group: [
                    {
                        column: [
                            {
                                prop: "detailList",
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
                                        label: '客户',
                                        prop: 'cName',
                                        width: 160,
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
                                        label: '包装',
                                        prop: 'wspName',
                                        width: 160,
                                    },
                                    {
                                        label: '规格',
                                        prop: 'spec',
                                        width: 120
                                    },
                                    {
                                        label: '订单量',
                                        prop: 'qty',
                                        width: 100
                                    },
                                    {
                                        label: '单位',
                                        prop: 'wsuName'
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
        },
        methods: {
            beforeOpen(done, type, finish) {
                this.loading = true;
                getNeedSkuDetail(this.data).then(res => {
                    this.dataSource = res.data.data;
                }).finally(() => {
                    done();
                })
            },
            callback(res) {
                this.form = {};
                this.$emit('callback', res);
            },
        }
    }
</script>

<style scoped>

</style>
