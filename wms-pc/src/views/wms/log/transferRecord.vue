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
        </nodes-crud>
    </basic-container>
</template>

<script>
import {getPage} from "@/api/wms/log/transferRecord.js";
import {getParamValue} from "../../../util/param";

export default {
    name: "transferRecord",
    data() {
        return {
            isView: false,
            isShow: true,
            loading: false, //加载中界面
            selectionList: [], //选中的数据
            data: [], //列表数据
            option: {
                newBtn: false,
                multiDelBtn: false,
                viewBtn: false,
                editBtn: false,
                delBtn: false,
                menu: false,
                column: [
                    {
                        label: "单据编码",
                        prop: "transferCode",
                        width: 140,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "移动类型",
                        prop: 'transferType',
                        width: 120,
                        search: true,
                        type: "select",
                        sortProp: 'transferType',
                        dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.transferType,
                        props: {
                            label: "dictValue",
                            value: "dictKey"
                        }
                    },
                    {
                        label: "物品编码",
                        prop: "skuCode",
                        width: 160,
                        sortable: true,
                        sortProp: 'fromSkuId'
                    },
                    {
                        label: "物品名称",
                        prop: "skuName",
                        width: 120,
                        sortable: true
                    },
                    {

                        label: "包装",
                        prop: "wspName",
                        width: 120,
                        sortable: true,
                        sortProp: 'fromWspId'
                    },
                    {

                        label: "包装层级",
                        prop: "skuLevel",
                        width: 120,
                        sortable: true,
                        sortProp: 'fromSkuLevel',
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
                        width: 120,
                        sortable: true,
                    },
                    {
                        label: "原库区编码",
                        prop: "fromZoneCode",
                        width: 160,
                        sortable: true,
                    },
                    {
                        label: "原库区名称",
                        prop: "fromZoneName",
                        width: 160,
                        sortable: true,
                    },
                    {
                        label: "原货位",
                        prop: "fromLocCode",
                        width: 100,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "原容器编码",
                        prop: "fromLpn",
                        width: 160,
                        sortable: true
                    },
                    {

                        label: "数量",
                        prop: "qty",
                        width: 120
                    },

                    {
                        label: "目的库区编码",
                        prop: "toZoneCode",
                        width: 160,
                        sortable: true,
                    },
                    {
                        label: "目的库区名称",
                        prop: "toZoneName",
                        width: 160,
                        sortable: true,
                    },
                    {
                        label: "目的货位",
                        prop: "toLocCode",
                        width: 120,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "目的容器编码",
                        prop: "toLpn",
                        width: 140,
                        sortable: true
                    },
                    {
                        label: "用户名称",
                        prop: "createUser",
                        width: 120,
                        sortable: true,
                        type: 'select-table-user',
                        dicUrl: '/api/blade-user/list',
                        props: {
                            value: 'id',
                            label: 'realName'
                        }
                    },
                    {
                        label: "执行时间",
                        prop: "createTime",
                        width: 120,
                        sortable: true,
                        type: 'date-picker'
                    },
                    {
                        label: "批次号",
                        prop: "lotNumber",
                        width: 120,
                        sortable: true,
                    },
                ]
            },
        };
    },
    computed: {
        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
                ids.push(ele.whId);
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
        //选中的数据
        selectionChange(list) {
            this.selectionList = list;
        },
        selectionClear() {
            this.selectionList = [];
        },
        onLoad(page, params = {}) {
            this.dataLoading = true;
            getPage(this.$refs.table.page.currentPage,
                this.$refs.table.page.pageSize,
                Object.assign(params, this.query)).then(res => {
                const data = res.data.data;
                this.data = data.records;
                page.total = data.total;
                this.dataLoading = false;
            });
        },
    },

};
</script>
<style lang="scss">
</style>
