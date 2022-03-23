<template>
    <nodes-crud
            ref="table"
            :option="option"
            :data="data"
            :table-loading="loading"
            @on-load="onLoad"
    ></nodes-crud>
</template>

<script>
    import {getList} from "@/api/wms/basedata/skulog"

    export default {
        name: "skuLog",
        data() {
            return {
                loading:false,
                data: [],
                option: {
                    newBtn: false,
                    multiDelBtn: false,
                    viewBtn: false,
                    editBtn: false,
                    delBtn: false,
                    search:false,
                    page:false,
                    column: [
                        {
                            prop: 'skuCode',
                            label: '物品编码',
                            sortable:true,
                        },
                        {
                            prop: 'skuName',
                            label: '物品名称',
                            sortable:true,
                        },
                        {
                            prop: 'updateTime',
                            label: '上次出库时间',
                            sortable:true,
                            type: 'date-picker'
                        },
                        {
                            prop:'outCount',
                            label:'出库次数',
                            sortable:true,
                        }
                    ]
                }
            }
        },
        methods:{
            onLoad(page, params = {}) {
                this.loading = true;
                getList().then(res => {
                    this.data = res.data.data;
                    this.loading = false;
                });
            },
        }
    }
</script>

<style scoped>

</style>
