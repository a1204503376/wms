<template>
    <basic-container>
        <nodes-crud ref="table"
                    :option="option"
                    :data="data"
                    :table-loading="loading"
                    @on-load="onLoad"
        ></nodes-crud>
    </basic-container>
</template>

<script>
import {getPage} from "@/api/wms/log/truckStockLog";
import {getList as getZoneList} from "@/api/wms/warehouse/zone";

export default {
    name: "truckStockLog",
    data() {
        return {
            loading: false,
            data: [],
            option: {
                newBtn: false,
                multiDelBtn: false,
                viewBtn: true,
                editBtn: false,
                delBtn: false,
                menu: false,
                column: [
                    {
                        label: "车次编号",
                        prop: "truckCode",
                        width: 120,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "车牌号",
                        width: 120,
                        prop: "plateNumber",
                        sortable: true,
                        search: true
                    },
                    {
                        label: "司机",
                        prop: "driverName",
                        sortable: true,
                        search: true
                    },
                    {
                        label: "电话",
                        prop: "driverPhone",
                        width: 120
                    },
                    {
                        label: "库房",
                        prop: "whId",
                        search: true,
                        type: "select",
                        dicUrl: "/api/wms/warehouse/warehouse/list",
                        props: {
                            label: "whName",
                            value: "whId"
                        },
                        cascaderItem: ['zoneId'],
                        change: function (val, obj, col) {
                            if (!val) {
                                return;
                            }
                            getZoneList(val).then(res => {
                                if (col && col.cascader) {
                                    col.cascader.forEach(item => {
                                        if (item.prop === 'zoneId') {
                                            item.dataSource = res.data.data;
                                        }
                                    });
                                }
                            });
                        }
                    },
                     {
                        label: "库区",
                        prop: "zoneId",
                        overHidden: true,
                        search: true,
                        type: "select",
                        dataSource: [],
                         dicUrl: "/api/wms/warehouse/zone/list",
                        props: {
                            label: "zoneName",
                            value: "zoneId"
                        }
                    },
                    {
                        label: "库位",
                        prop: "locCode",
                        width: 100,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "容器",
                        prop: "lpnCode"
                    },
                    {
                        label: "物品编码",
                        prop: "skuCode",
                        width: 120,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "物品名称",
                        prop: "skuName",
                        width: 120,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "批次号",
                        prop: "lotNumber",
                        width: 120,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "数量",
                        prop: "stockQty",
                        sortable: true,
                    },
                    {
                        label: "单位",
                        prop: "umName",
                        sortable: true,
                    }
                ]
            }
        };
    },
    methods: {
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(
                page.currentPage,
                page.pageSize,
                Object.assign(params, this.query)
            ).then(res => {
                let data = res.data.data;
                page.total = data.total;
                this.data = data.records;
                this.loading = false;
            });
        }
    }
};
</script>

<style scoped>
</style>
