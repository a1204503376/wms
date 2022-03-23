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
import {getPage} from "@/api/wms/log/soPickLog";

export default {
    name: "sopick",
    data() {
        return {
            isView: false,
            isShow: true,
            advancedSearchBool: true, //是否高级搜索
            dialogs: false,           //搜索界面的显隐
            childTitle: "新增",
            selectionList: [], //选中的数据
            timer: null,
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
                        label: "单据编码",
                        prop: "soBillNo",
                        width: 180,
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "操作时间",
                        prop: "procTime",
                        width: 160,
                        sortable: true,
                        type: 'date-picker'
                    },
                    {
                        label: "库位编码",
                        prop: "locCode",
                        search: true,
                        width: 160,
                        sortable: true,
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
                        search: true,
                        width: 170,
                        sortable: true,
                    },
                    {
                        label: "序列号",
                        prop: "soDetailCode",
                        search: true,
                        width: 160,
                        sortable: true,
                    },
                    {
                        label: "批次号",
                        prop: "lotNumber",
                        search: true,
                        width: 170,
                        sortable: true,
                    },
                    {
                        label: "波次编号",
                        prop: "wellenNo",
                        width: 150,
                        sortable: true,
                    },
                    {
                        label: "拣货量",
                        prop: "pickRealQty",
                        width: 100,
                        sortable: true,
                    },
                    {
                        label: "包装名称",
                        prop: "wspName",
                        width: 150,
                        sortable: true,
                    },
                    {
                        label: "层级",
                        prop: "skuLevel",
                        width: 110,
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
                        label: "计量单位",
                        prop: "wsuName",
                        width: 110,
                        sortable: true,
                    },
                    {
                        label: "换算倍数",
                        prop: "convertQty",
                        width: 110,
                        sortable: true,
                    },
                    {
                        label: "容器编码",
                        prop: "lpnCode",
                        search: true,
                        width: 130,
                        sortable: true,
                    },
                    {
                        label: "目标容器",
                        prop: "targetLpnCode",
                        search: true,
                        width: 130
                    },

                    {
                        label: "库房名称",
                        prop: "whId",
                        width: 140,
                        sortable: true,
                        type: "select",
                        dicUrl: "/api/wms/warehouse/warehouse/list",
                        props: {
                            label: "whName",
                            value: "whId"
                        },
                    },
                    {
                        label: "用户名称",
                        prop: "createUser",
                        width: 110,
                        sortable: true,
                        type: 'select-table-user',
                        dicUrl: '/api/blade-user/list',
                        props: {
                            value: 'id',
                            label: 'realName'
                        }
                    }
                ]
            }
        };
    },
    methods: {
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(
                this.$refs.table.page.currentPage,
                this.$refs.table.page.pageSize,
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
