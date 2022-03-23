<template>
    <basic-container>
        <nodes-crud
            ref="table"
            :option="option"
            :data="data"
            :table-loading="loading"
            :permission="permissionList"
            @on-load="onLoad"
            @selection-change="selectionChange"
        >
            <template slot="pickRealQty" slot-scope="row">
                <el-link type="primary" @click="showOccupyDialog(10,row.pickPlanId)">{{ row.pickRealQty }}</el-link>
            </template>
        </nodes-crud>
    </basic-container>
</template>
<script>
import {getList} from "@/api/wms/outstock/pickplan";
import {mapGetters} from "vuex";
import {getParamValue} from "@/util/param";

export default {
    name: "pickPlan",
    data() {
        return {
            loading: false,
            data: [],
            option: {
                newBtn: false,
                multiDelBtn: false,
                viewBtn: false,
                editBtn: false,
                delBtn: false,
                menu: false,
                column: [
                    {
                        label: "波次编码",
                        prop: "wellenNo",
                        search: true,
                        width: 160,
                        sortable: true,
                    },
                    {
                        label: '订单编码',
                        prop: 'soBillNo',
                        width: 200
                    },
                    {
                        label: "库位编码",
                        prop: "locCode",
                        search: true,
                        width: 160,
                        sortable: true,
                    },
                    {
                        label: "库房名称",
                        prop: "whId",
                        width: 160,
                        sortable: true,
                        search: true,
                        type: "select",
                        dicUrl: "/api/wms/warehouse/warehouse/list",
                        props: {
                            label: "whName",
                            value: "whId",
                        },
                    },
                    {
                        label: "物品编码",
                        prop: "skuCode",
                        search: true,
                        width: 160,
                        sortable: true,
                    },
                    {
                        label: "物品名称",
                        prop: "skuName",
                        width: 160,
                        sortable: true,
                    },
                    {
                        label: "被替代物品编码",
                        prop: "repSkuCode",
                        width: 160,
                        sortable: true,
                    },
                    {
                        label: "被替代物品名称",
                        prop: "repSkuName",
                        width: 160,
                        sortable: true,
                    },
                    {
                        label: "批次号",
                        prop: "lotNumber",
                        search: true,
                        width: 160,
                        sortable: true,
                    },
                    {
                        label: "容器编码",
                        prop: "lpnCode",
                        search: true,
                        width: 140
                    },
                    {
                        label: "包装",
                        prop: "wspName",
                        width: 180,
                        sortable: true,
                    },
                    {
                        label: "层级",
                        prop: "skuLevel",
                        width: 120,
                        sortable: true,
                        search: true,
                        type: "select",
                        dicUrl:
                            "/api/blade-system/dict/dictionary?code=" + this.$dict.skuLevel,
                        props: {
                            label: "dictValue",
                            value: "dictKey",
                        },
                    },
                    {
                        label: "计划量",
                        prop: "pickPlanQty"
                    },
                    {
                        label: "拣货量",
                        prop: "pickRealQty",
                    },
                    {
                        label: '计量单位',
                        prop: 'wsuName',
                        width: 120
                    },
                ]
            }
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                add: this.vaildData(this.permission.pickplan_add, false),
                view: this.vaildData(this.permission.pickplan_view, false),
                delete: this.vaildData(this.permission.pickplan_delete, false),
                edit: this.vaildData(this.permission.pickplan_edit, false),
            }
        },
    },
    created() {
        let lotCount = getParamValue(this.$param.system.lotCount);
        let column = this.option.column;
        for (let i = 1; i <= lotCount; i++) {
            let skuLot = {
                label: "物品批属性" + i,
                prop: "skuLot" + i,
                hide: false,
                width: 140,
                sortable: true,
                search: true,
                placeholder: '支持模糊查询',
            };
            column.push(skuLot)
        }
    },
    methods: {
        showOccupyDialog(type, pickPlanId) {
            this.sonPickPlanId = pickPlanId;
            this.types = type;
            this.dialogVisible = true;
        },
        //默认渲染数据
        onLoad(page, params = {}) {
            this.loading = true;
            getList(
                page.currentPage,
                page.pageSize,
                Object.assign(params, this.query)
            ).then(res => {
                const data = res.data.data;
                page.total = data.total;
                this.data = data.records;
                this.loading = false;
                this.selectionClear();
            });
        },
        //选中的数据
        selectionChange(list) {
            this.selectionList = list;
        },
        selectionClear() {
            this.selectionList = [];
        }
    }
};
</script>
<style lang="scss">
</style>
