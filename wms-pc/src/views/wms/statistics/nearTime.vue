<template>
    <basic-container>
        <nodes-crud
            ref="table"
            :option="option"
            :data="data"
            :table-loading="loading"
            @on-load="onLoad"
            @selection-change="selectionChange"
        >
            <template v-slot:stock>
                库存汇总：{{ data[0] ? data[0].totalQty : 0 }}
            </template>
        </nodes-crud>
    </basic-container>
</template>
<script>
import {getPage} from "@/api/wms/statistics/stock";
import {getParamValue} from "../../../util/param";

export default {
    name: "nearTime",
    components: {},
    created() {
        const lotCount = getParamValue(this.$param.system.lotCount);
        // 获取批属性数量
        for (let i = 1; i <= lotCount; i++) {
            let skuLot = {
                label: "批属性" + i,
                prop: "skuLot" + i,
                hide: false,
                width: 120,
                search: true,
                sortable: true,
                placeholder: "支持模糊查询",
            };
            this.option.column.push(skuLot);
        }
    },
    data() {
        return {
            operation: false,
            operationButton: ["库存冻结/解冻", "批次冻结/解冻", "导出"],
            loading: false,
            isView: false,
            isShow: false,
            makeupSource: [],
            selectionList: [], //选中的数据
            data: [], //列表数据
            searchData: {},
            option: {
                newBtn: false,
                multiDelBtn: false,
                viewBtn: false,
                editBtn: false,
                delBtn: false,
                menu: false,
                search:false,
                column: [
                    {
                        label: "所属库房",
                        prop: "whId",
                        search: true,
                        type: "select",
                        dicUrl: "/api/wms/warehouse/warehouse/list",
                        props: {
                            label: "whName",
                            value: "whId",
                        },
                        cascaderItem: ["zoneId"],
                        change: function (val, obj, col) {
                            if (!val) {
                                return;
                            }
                            getSelectList(val).then((res) => {
                                if (col && col.cascader) {
                                    col.cascader.forEach((item) => {
                                        if (item.prop === "zoneId") {
                                            item.dataSource = res.data;
                                        }
                                    });
                                }
                            });
                        },
                    },
                    {
                        label: "货主",
                        prop: "woId",
                        overHidden: true,
                        search: true,
                        type: "select",
                        dicUrl: "/api/wms/basedata/owner/list",
                        props: {
                            label: "ownerName",
                            value: "woId",
                        },
                    },
                    {
                        label: "物品编码",
                        prop: "skuCode",
                        overHidden: true,
                        width: 140,
                        search: true,
                        sortable: true,
                        sortProp: "skuId",
                    },
                    {
                        label: "物品名称",
                        prop: "skuName",
                        overHidden: true,
                        width: 200,
                        search: true,
                        sortable: true,
                        sortProp: "skuId",
                    },
                    {
                        label: "库存数量",
                        prop: "stockQty",
                        sortable: true,
                        width: 120,
                    },
                    {
                        label: "有效期",
                        prop: "validTime",
                        sortable: true,
                        width: 100,
                        type: 'date-picker'
                    },
                    {
                        label: "剩余天数",
                        prop: "validDay",
                        width: 110,
                    },
                ],
            },
        };
    },
    computed: {
        ids() {
            let ids = [];
            this.selectionList.forEach((ele) => {
                ids.push(ele.stockId);
            });
            return ids.join(",");
        },
    },
    methods: {
        //默认渲染数据
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(
                page.currentPage,
                page.pageSize,
                Object.assign(params, this.query)
            ).then((res) => {
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
        },
    },
};
</script>
<style lang="scss">
</style>
