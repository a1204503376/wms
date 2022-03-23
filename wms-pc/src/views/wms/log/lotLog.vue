<template>
    <basic-container>
        <nodes-crud
            ref="table"
            :option="option"
            :data="data"
            :table-loading="loading"
            :hide="false"
            @on-load="onLoad"
            @selection-change="selectionChange"
        >
        </nodes-crud>
    </basic-container>
</template>

<script>
import {getPage} from "@/api/wms/log/lotLog";
import {getParamValue} from "../../../util/param";

export default {
    name: "lotlog",
    components: {},
    data() {
        return {
            isView: false,
            isShow: true,
            dialogs: false, //搜索界面的显隐
            loading: false, //加载中界面
            childTitle: "新增",
            selectionList: [], //选中的数据
            query: {},
            data: [],
            option: {
                newBtn: false,
                multiDelBtn: false,
                viewBtn: false,
                editBtn: false,
                delBtn: false,
                column: [
                    {
                        label: "操作时间",
                        prop: "updateTime",
                        width: 160,
                        sortable: true,
                        type: 'date-picker'
                    },
                    {
                        label: "批次号",
                        prop: "lotNumber",
                        search: true,
                        width: 170,
                        sortable: true,
                    },
                    {
                        label: "物品编码",
                        prop: "skuCode",
                        search: true,
                        width: 130,
                        sortable: true,
                    },
                    {
                        label: "物品名称",
                        prop: "skuName",
                        search: true,
                        width: 170,
                        sortable: true,
                    },
                    {
                        label: "处理类型",
                        prop: "proType",
                        width: 110,
                        sortable: true,
                        sortProp: 'proType',
                        type: "select",
                        dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.proType,
                        props: {
                            label: "dictValue",
                            value: "dictKey"
                        }
                    },
                    {
                        label: "操作人",
                        prop: "updateUser",
                        width: 100,
                        sortable: true,
                        type: 'select-table-user',
                        dicUrl: '/api/blade-user/list',
                        props: {
                            value: 'id',
                            label: 'realName'
                        }
                    },
                    {
                        label: "仓库",
                        prop: "whId",
                        width: 140,
                        sortable: true,
                        sortProp: 'whId',
                        type: "select",
                        dicUrl: "/api/wms/warehouse/warehouse/list",
                        props: {
                            label: "whName",
                            value: "whId"
                        },
                    },
                ]
            }
        };
    },
    computed: {
        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
                ids.push(ele.id);
            });
            return ids.join(",");
        }
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
        }
    },
};
</script>
<style lang="scss">
</style>
