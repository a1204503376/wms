<template>
    <div class="erpstock">
        <nodes-crud ref="table"
                     :option="option"
                     :data="data"
                     :table-loading="loading"
                     @on-load="onLoad"
                     @selection-change="selectionChange">
            <template slot="diffQty" slot-scope="row">
                <span v-if="row.diffQty !== 0" style="color:red;">{{row.diffQty}}</span>
                <span v-else>{{row.diffQty}}</span>
                <!--                <span>{{scope}}</span>-->
            </template>
        </nodes-crud>
    </div>
</template>

<script>
    // import nodesCurd from "@/components/nodes/nodesCurd";
    import {erpCompare} from "@/api/wms/core/stock";

    export default {
        name: "erpStock",
        // components: {nodesCurd},
        data() {
            return {
                loading: false,
                data: [],
                selectionList: [],
                option: {
                    newBtn: false,
                    multiDelBtn: false,
                    viewBtn: false,
                    editBtn: false,
                    delBtn: false,
                    menu: false,
                    page: false,
                    column: [
                        {
                            label: '库房',
                            prop: 'whName',
                            width: 160,
                            sortable:true,
                        },
                        {
                            label: '货主',
                            prop: 'ownerName',
                            width: 160,
                            sortable:true,
                        },
                        {
                            label: '物品编码',
                            prop: 'skuCode',
                            width: 140,
                            sortable:true,
                        },
                        {
                            label: '物品名称',
                            prop: 'skuName',
                            width: 200,
                            sortable:true,
                        },
                        {
                            label: '包装',
                            prop: 'wspName',
                            width: 200,
                            sortable:true,
                        },
                        {
                            label: '层级',
                            prop: 'skuLevelDesc',
                            width:120,
                            sortable:true,
                        },
                        {
                            label: 'ERP库存量',
                            prop: 'stockQty',
                            width:120,
                            sortable:true,
                        },
                        {
                            label: 'WMS库存量',
                            prop: 'wmsStockQty',
                            width:130,
                            sortable:true,
                        },
                        {
                            label: '差异',
                            prop: 'diffQty',
                            slot: true,
                            width:100
                        }
                    ]
                }
            }
        },
        computed: {},
        methods: {
            onLoad(page, params = {}) {
                this.loading = true;
                erpCompare().then(res => {
                    this.data = res.data.data;
                    this.loading = false;
                    this.selectionClear();
                });
            },

            selectionChange(val) {
                this.selectionList = val;
            },
            selectionClear() {
                this.selectionList = [];
                this.$refs.table.toggleSelection();
            }
        }
    }
</script>

<style scoped>
.erpstock{
    height: 100%;
}
</style>
