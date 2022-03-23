<template>
    <basic-container>
        <nodes-crud
            ref="table"
            :option="option"
            :data="data"
            :table-loading="loading"
            @on-load="onLoad"
            @selection-change="selectionChange"
        ></nodes-crud>
    </basic-container>
</template>
<script>
import {getPage} from "@/api/wms/log/countLog";
import {getParamValue} from "../../../util/param";

export default {
    name: "countLog",
    data() {
        return {
            loading: false,
            data: [],
            selectionList: [],
            dialogs: false, //搜索界面的显隐
            option: {
                newBtn: false,
                multiDelBtn: false,
                viewBtn: false,
                editBtn: false,
                delBtn: false,
                column: [
                    {
                        label: "库房",
                        prop: "whId",
                        width: 180,
                        sortable: true,
                        type: "select",
                        dicUrl: "/api/wms/warehouse/warehouse/list",
                        props: {label: "whName", value: "whId"},
                    },
                    {
                        label: "盘点单编码",
                        prop: "countBillNo",
                        width: 180,
                        sortable: true,
                        search: true,
                    },
                    {
                        label: "库位编码",
                        prop: "locCode",
                        width: 180,
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "任务分组编码",
                        prop: "taskGroup",
                        width: 180,
                        sortable: true,
                    },
                    {
                        label: "容器编码",
                        prop: "lpnCode",
                        width: 160,
                        search: true,
                    },
                    {
                        label: "物品编码",
                        prop: "skuCode",
                        width: 160,
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "物品名称",
                        prop: "skuName",
                        width: 160,
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "盘点数量",
                        prop: "countQty",
                        width: 100
                    },
                    {
                        label: "包装名称",
                        prop: "wspName",
                        width: 180,
                        sortable: true,
                    },
                    {
                        label: "计量单位名称",
                        prop: "wsuName",
                        width: 140,
                        sortable: true,
                    },
                    {
                        label: "层级",
                        prop: "skuLevel",
                        width: 120,
                        sortable: true,
                        type: "select",
                        dicUrl:
                            "/api/blade-system/dict/dictionary?code=" + this.$dict.skuLevel,
                        props: {
                            label: "dictValue",
                            value: "dictKey",
                        },
                    },
                    {
                        label: "换算倍率",
                        prop: "convertQty",
                        width: 100
                    },
                    {
                        label: "盘点序列号",
                        prop: "serialNumber",
                        width: 120
                    },
                    {
                        label: "盘点时间",
                        prop: "procTime",
                        width: 160,
                        sortable: true,
                        type: 'date-picker'
                    },
                    {
                        label: '盘点时间',
                        prop: "procTimeArray",
                        hide: true,
                        search: true,
                        type: 'date-picker'
                    },
                    {
                        label: "用户名称",
                        prop: "createUser",
                        width: 160,
                        sortable: true,
                        type: 'select-table-user',
                        dicUrl: '/api/blade-user/list',
                        props: {
                            value: 'id',
                            label: 'realName'
                        }
                    },
                    {
                        label: "盘点状态",
                        prop: "recordState",
                        width: 160,
                        search: true,
                        type: 'select',
                        dicUrl: '/api/blade-system/dict/dictionary?code=' + this.$dict.countRecordStatus,
                        props: {
                            label: 'dictValue',
                            value: 'dictKey'
                        }
                    },
                    {
                        label: "批次号",
                        prop: "lotNumber",
                        width: 180,
                        sortable: true,
                    },
                ]
            },
            searchForm: {
                countBillId: ""
            }
        };
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
        //默认渲染数据
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(
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
            this.$refs.table.toggleSelection();
        },
    }
};
</script>
<style lang="scss">
</style>
