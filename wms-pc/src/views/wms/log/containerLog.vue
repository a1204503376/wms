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
import {getPage} from "@/api/wms/log/containerLog";
import {getParamValue} from "../../../util/param";

export default {
    name: "containerlLog",
    components: {},
    data() {
        return {
            titleText: "",
            dialogVisible: false,
            loading: false,
            isView: false,
            isShow: false,
            selectionList: [], //选中的数据
            data: [], //列表数据
            option: {
                newBtn: false,
                multiDelBtn: false,
                viewBtn: false,
                editBtn: false,
                delBtn: false,
                column: [
                    {
                        label: "单据编码",
                        prop: "asnBillNo",
                        width: 120,
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "序列号",
                        prop: "snDetailCode",
                        width: 120,
                        sortable: true,
                    },
                    {
                        label: "容器编码",
                        prop: "lpnCode",
                        width: 120,
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "库位编码",
                        prop: "locCode",
                        search: true,
                        sortable: true,
                        width: 120,
                    },
                    {
                        label: "数量",
                        prop: "aclQty"
                    },
                    {
                        label: "计量单位",
                        prop: "skuLevel",
                        width: 120,
                        sortable: true,
                        sortProp: 'skuLevel',
                        type: "select",
                        dicUrl:
                            "/api/blade-system/dict/dictionary?code=" + this.$dict.skuLevel,
                        props: {
                            label: "dictValue",
                            value: "dictKey",
                        },
                    },
                    {
                        label: "物品编码",
                        prop: "skuCode",
                        search: true,
                        hide: true,
                        sortable: true,
                        width: 120
                    },
                    {
                        label: "物品名称",
                        prop: "skuName",
                        width: 120,
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "批次号编号",
                        prop: "lotNumber",
                        width: 160,
                        sortable: true,
                    },
                    {
                        label: "用户名称",
                        prop: "createUser",
                        sortable: true,
                        width: 120,
                        type: 'select-table-user',
                        dicUrl: '/api/blade-user/list',
                        props: {
                            value: 'id',
                            label: 'realName'
                        }
                    },
                    {
                        label: "清点时间",
                        prop: "createTime",
                        width: 160,
                        sortable: true,
                        type: 'date-picker'
                    },
                ]
            }
        };
    },
    mounted(){
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
                var data = res.data.data;
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
        }
    }
};
</script>
<style lang="scss">
</style>
