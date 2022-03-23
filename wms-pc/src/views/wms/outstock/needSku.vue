<template>
    <basic-container>
        <nodes-crud
            ref="curd"
            v-model="form"
            :option="option"
            :data="data"
            :table-loading="loading"
            :permission="permissionList"
            @on-load="onLoad"
            @selection-change="selectionChange"
        >
            <template slot="menuLeft">
                <el-button type="primary" size="mini" @click="createTask">生成补货任务</el-button>
            </template>

            <template slot="billCount" slot-scope="row">
                <el-link type="primary" @click="clickBillCount(row)">
                    {{ row.billCount }}
                </el-link>
            </template>

            <template slot="wantage" slot-scope="row">
                <span v-if="row.wantage > 0" style="color:red;">{{ row.wantage }}</span>
                <span v-else>{{ row.wantage }}</span>
            </template>

            <template slot="isFlagDesc" slot-scope="row">
                <span v-if="row.wantage > 0" style="color:red;">{{ row.isFlagDesc }}</span>
                <span v-else>{{ row.isFlagDesc }}</span>
            </template>
        </nodes-crud>

        <bill-count
            :data="billCountDialog.data"
            :visible="billCountDialog.visible"
            @callback="callbackBillCount"></bill-count>

        <create-task
            :visible="createTaskDialog.visible"
            :dataSource="createTaskDialog.dataSource"
            @callback="callbackCreateTask"></create-task>
    </basic-container>
</template>

<script>
import {getNeedSku} from "@/api/wms/stock/transferheader"
import {getParamValue} from "../../../util/param";
import billCount from "./needSku/billCount";
import CreateTask from "./needSku/createTask";
import {mapGetters} from "vuex";
export default {
    name: "needSku",
    components: {CreateTask, billCount},
    mounted() {
        let lotCount = getParamValue(this.$param.system.lotCount);
        // 获取批属性数量
        for (let i = 1; i <= lotCount; i++) {
            let skuLot = {
                label: "批属性" + i,
                prop: "skuLot" + i,
                hide: false,
                width: 120,
                search: true,
                sortable: true,
            };
            this.option.column.push(skuLot);
        }
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                生成补货任务: this.vaildData(this.permission.needSku_createTask, false),
                订单详情: this.vaildData(this.permission.needSku_billCount, false),
            }
        },
    },
    data() {
        return {
            form: {},
            loading: false,
            data: [],
            selectionList: [],
            billCountDialog: {
                data: {},
                visible: false
            },
            createTaskDialog: {
                dataSource: [],
                visible: false
            },
            option: {
                search: false,
                page: false,
                column: [
                    {
                        prop: 'whId',
                        label: '库房',
                        width: 160,
                        search: true,
                        type: "select",
                        dicUrl: "/api/wms/warehouse/warehouse/list",
                        props: {
                            label: "whName",
                            value: "whId",
                        },

                    },
                    {
                        prop: 'woId',
                        label: '货主',
                        width: 160,
                        search: true,
                        type: "select",
                        dicUrl: "/api/wms/basedata/owner/list",
                        props: {
                            label: "ownerName",
                            value: "woId",
                        },
                    },
                    {
                        prop: 'skuCode',
                        label: '物品编码',
                        width: 160,
                    },
                    {
                        prop: 'skuName',
                        label: '物品名称',
                        width: 160,
                    },
                    {
                        prop: 'wspName',
                        label: '包装',
                        width: 160,
                    },
                    {
                        prop: 'spec',
                        label: '规格',
                        width: 120
                    },
                    {
                        prop: 'billCount',
                        label: '订单数量',
                        slot: true
                    },
                    {
                        prop: 'totalNeedQty',
                        label: '出库总量'
                    },
                    {
                        prop: 'sourceZoneQty',
                        label: '可出库总量',
                        width: 100
                    },
                    {
                        prop: 'wantage',
                        label: '缺货总量',
                        slot: true
                    },
                    {
                        prop: 'totalStockQty',
                        label: '库存总量'
                    },
                    {
                        prop: 'wsuName',
                        label: '计量单位'
                    },
                    {
                        prop: 'isFlagDesc',
                        label: '需要补货',
                        fixed: true,
                        slot: true
                    }
                ]
            }
        }
    },
    methods: {
        onLoad(page, params = {}) {
            this.loading = true;
            getNeedSku(
                page.currentPage,
                page.pageSize,
                Object.assign(params, this.query)
            ).then(res => {
                this.data = res.data.data;
                this.loading = false;
                this.selectionClear();
            });
        },
        selectionChange(list) {
            this.selectionList = list;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.curd.toggleSelection();
        },
        clickBillCount(row) {
            this.billCountDialog.data = Object.assign({}, row);
            this.billCountDialog.visible = true;
        },
        callbackBillCount(res) {
            this.billCountDialog.visible = res.visible;
        },
        callbackCreateTask(res) {
            this.createTaskDialog.visible = res.visible;
            if (res.result) {
                this.$refs.curd.onLoad();
            }
        },
        createTask() {
            if (!this.selectionList || this.selectionList.length == 0) {
                this.$message.warning("至少选择一个数据才能执行此操作！");
                return;
            }
            let dataSource = Object.assign([], this.selectionList);
            dataSource.forEach(item => {
                if (item.wantage > 0) {
                    if (item.wantage > item.totalStockQty) {
                        this.$set(item, 'repQty', item.totalStockQty ? item.totalStockQty : 0);
                    } else {
                        this.$set(item, 'repQty', item.wantage);
                    }
                } else {
                    this.$set(item, 'repQty', 0);
                }
            });
            this.createTaskDialog.dataSource = dataSource;
            this.createTaskDialog.visible = true;
        }
    }
}
</script>

<style scoped>

</style>
