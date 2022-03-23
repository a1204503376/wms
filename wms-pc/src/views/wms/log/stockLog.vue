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
    import {getPage} from "@/api/wms/log/stockLog";
import {getParamValue} from "../../../util/param";
    export default {
        name: "productLog",
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
                    column: [
                        {
                            label: "业务类型",
                            prop: "proType",
                            sortable: true,
                            width: 120,
                            sortProp: 'proType',
                            type: "select",
                            dicUrl:
                                "/api/blade-system/dict/dictionary?code=" + this.$dict.proType,
                            props: {
                                label: "dictValue",
                                value: "dictKey",}
                        }, {
                            label: "库房",
                            prop: "whId",
                            search: true,
                            type: "select",
                            dicUrl: "/api/wms/warehouse/warehouse/list",
                            props: {
                                label: "whName",
                                value: "whId"
                            },
                        },
                        {
                            label: "库位",
                            prop: "locCode",
                            width: 140,
                            sortable: true,
                            search: true
                        },
                        {
                            label: "物品编码",
                            prop: "skuCode",
                            width: 160,
                            sortable: true,
                            search: true
                        },
                        {
                            label: "物品名称",
                            prop: "skuName",
                            width: 160,
                            sortable: true,
                            search: true
                        },
                        {
                            label: "容器",
                            prop: "lpnCode",
                            search: true
                        },
                        {
                            label: "库存数量",
                            prop: "stockQty"
                        },
                        {
                            label: '下架数量',
                            prop: 'pickQty',
                        },
                        {
                            label: '分配占用数量',
                            prop: 'occupyQty',
                            width: 120
                        },
                        {
                            label: '盘点占用数量',
                            prop: 'countOccupyQty',
                            width: 120
                        },
                        {
                            label: "批次",
                            prop: "lotNumber",
                            width: 160,
                            sortable: true,
                            search: true
                        },
                        {
                            label: "操作人",
                            width: 120,
                            prop: "updateUser",
                            sortable: true,
                            type: 'select-table-user',
                            dicUrl: '/api/blade-user/list',
                            props: {
                                value: 'id',
                                label: 'realName'
                            }
                        },
                        {
                            label: "操作时间",
                            prop: "updateTime",
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
        computed: {
            ids() {
                let ids = [];
                this.selectionList.forEach(ele => {
                    ids.push(ele.wlslId);
                });
                return ids.join(",");
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
            selectionChange(val) {
                this.selectionList = val;
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
