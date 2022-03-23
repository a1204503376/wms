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
import {getPage} from "@/api/wms/log/serialLog";

export default {
    name: "seriallog",
    components: {},
    data() {
        return {
            titleText: "",
            dialogVisible: false,
            loading: false,
            dialogs: false, //搜索界面的显隐
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
                        label: "操作时间",
                        prop: "updateTime",
                        width: 160,
                        sortable: true,
                        type: 'date-picker'
                    },
                    {
                        label: "序列号",
                        prop: "serialNumber",
                        width: 160,
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "物品编码",
                        prop: "skuCode",
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
                        label: "批次号",
                        prop: "lotNumber",
                        width: 160,
                        sortable: true,
                    },
                    {
                        label: "处理类型",
                        prop: "proType",
                        width: 160,
                        sortable: true,
                        type: "select",
                        dicUrl:
                            "/api/blade-system/dict/dictionary?code=" + this.$dict.proType,
                        props: {
                            label: "dictValue",
                            value: "dictKey",
                        },
                    },
                    {
                        label: "操作人",
                        prop: "updateUser",
                        width: 160,
                        sortable: true,
                        type: 'select-table-user',
                        dicUrl: '/api/blade-user/list',
                        props: {
                            value: 'id',
                            label: 'realName'
                        }
                    },
                ],
            },
        };
    },
    computed: {
        ids() {
            let ids = [];
            this.selectionList.forEach((ele) => {
                ids.push(ele.id);
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
            this.$refs.table.toggleSelection();
        },
    },
};
</script>
<style lang="scss">
</style>
