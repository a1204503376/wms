<template>
    <div id='stockByBox'>
        <nodes-master-page :permission="permissionObj" v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="物品编码" label-width="90px">
                            <nodes-sku
                                v-model="form.params.skuIds"
                                class="search-input">
                            </nodes-sku>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="生产批次" label-width="90px">
                            <el-input
                                v-model.trim="form.params.skuLot1"
                                :clearable="true" class="search-input"
                                placeholder="请输入生产批次">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="库位" label-width="90px">
                            <nodes-location
                                v-model="form.params.locIdList"
                                :multiple="true" class="search-input">
                            </nodes-location>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="库存状态" label-width="90px">
                            <NodesStockStatus
                                v-model="form.params.stockStatusList" class="search-input" :multiple="true">
                            </NodesStockStatus>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="库区" label-width="90px">
                            <nodes-zone
                                v-model="form.params.zoneIdList"
                                :multiple="true" class="search-input">
                            </nodes-zone>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="箱号" label-width="90px">
                            <el-input
                                v-model.trim="form.params.boxCode"
                                :clearable="true" class="search-input"
                                placeholder="请输入箱号">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="lpn" label-width="90px">
                            <el-input
                                v-model.trim="form.params.lpnCode"
                                :clearable="true" class="search-input"
                                placeholder="请输入lpn">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="规格型号" label-width="90px">
                            <el-input
                                v-model.trim="form.params.skuLot2"
                                :clearable="true" class="search-input"
                                placeholder="请输入规格型号">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="6">
                        <el-form-item label="收货时间" label-width="90px">
                            <nodes-date-range v-model="form.params.receiveTimeDateRange" style="width: 200px">
                            </nodes-date-range>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="专用客户" label-width="90px">
                            <el-input
                                v-model.trim="form.params.skuLot4"
                                :clearable="true" class="search-input"
                                placeholder="请输入专用客户">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="钢背批次" label-width="90px">
                            <el-input
                                v-model.trim="form.params.skuLot5"
                                :clearable="true" class="search-input"
                                placeholder="请输入钢背批次">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="摩擦块批次" label-width="90px">
                            <el-input
                                v-model.trim="form.params.skuLot6"
                                :clearable="true" class="search-input"
                                placeholder="请输入摩擦块批次">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="6">
                        <el-form-item label="库房" label-width="90px">
                            <nodes-warehouse
                                v-model="form.params.whIdList"
                                :multiple="true" class="search-input">
                            </nodes-warehouse>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="货主" label-width="90px">
                            <nodes-owner
                                v-model="form.params.woId"
                                class="search-input">
                            </nodes-owner>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="入库时间" label-width="90px">
                            <nodes-date-range v-model="form.params.lastInTimeDateRange" style="width: 200px">
                            </nodes-date-range>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="出库时间" label-width="90px">
                            <nodes-date-range v-model="form.params.lastOutTimeDateRange" style="width: 200px">
                            </nodes-date-range>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-slot:tableTool>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="刷新"
                    effect="dark"
                    placement="top"
                >
                    <el-button
                        circle
                        icon="el-icon-refresh"
                        size="mini"
                        @click="onRefresh"
                    ></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="显隐"
                    effect="dark"
                    placement="top"
                >
                    <el-button
                        circle
                        icon="el-icon-s-operation"
                        size="mini"
                        @click="onColumnShowHide"
                    ></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="服务端导出"
                    effect="dark"
                    placement="top"
                >
                    <el-button circle icon="el-icon-download" size="mini" @click="exportData"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="本地导出" effect="dark" placement="top">
                    <excel-export :filename="exportExcelName" :sheet="exportExcelSheet"
                                  style="display: inline-block;margin-left: 10px">
                        <el-button circle icon="el-icon-bottom" size="mini" @click="onExportLocalData">
                        </el-button>
                    </excel-export>
                </el-tooltip>
            </template>
            <template v-slot:table>
                <el-table
                    ref="table"
                    :data="table.data"
                    :height="table.height"
                    :row-class-name="tableRowClassName"
                    :span-method="arraySpanMethod"
                    border
                    highlight-current-row
                    row-key="id"
                    size="mini"
                    style="width: 100%"
                    @sort-change="onSortChange"
                >
                    <template v-for="(column, index) in table.columnList">
                        <el-table-column
                            v-if="!column.hide"
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column"
                            width="130"
                        >
                        </el-table-column>
                    </template>
                </el-table>
            </template>
        </nodes-master-page>
        <div v-if="columnShowHide.visible">
            <dialog-column
                v-bind="columnShowHide"
                @close="onColumnShowHide">
            </dialog-column>
        </div>
    </div>
</template>
<style>
.el-table .success-row {
    background: #eaeaea;
}
</style>

<script>

import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import {listMixin} from "@/mixins/list";
import {exportFile, page} from "@/api/wms/stock/stock";
import fileDownload from "js-file-download";
import {ExcelExport} from 'pikaz-excel-js';
import fileUpload from "@/components/nodes/fileUpload";
import NodesSku from "@/components/wms/select/NodesSkuByQuery";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import NodesOwner from "@/components/wms/select/NodesOwner";
import NodesStockStatus from "@/components/wms/select/NodesStockStatus";
import NodesLocation from "@/components/wms/select/NodesLocation";
import NodesZone from "@/components/wms/select/NodesZone";
import func from "@/util/func";
import {nowDateFormat} from "@/util/date";

export default {
    name: "stockByBox",
    components: {
        NodesZone,
        NodesLocation,
        DialogColumn,
        NodesOwner,
        NodesSearchInput,
        NodesStockStatus,
        NodesMasterPage,
        NodesDateRange,
        NodesWarehouse,
        NodesSku,
        ExcelExport,
        fileUpload,
    },
    mixins: [listMixin],
    data() {
        return {
            woId: "",
            form: {
                params: {
                    skuIds: [],
                    skuLot1: "",
                    locIdList: [],
                    stockStatusList: [],
                    zoneIdList: [],
                    boxCode: "",
                    lpnCode: "",
                    skuLot2: "",
                    skuLot4: "",
                    skuLot5: "",
                    skuLot6: "",
                    whIdList: [],
                    woId: "",
                    receiveTimeDateRange: "",
                    lastInTimeDateRange: "",
                    lastOutTimeDateRange: "",
                    isShowByBox: true
                }
            },
            deleteCustomerRequest: {
                ids: [],
            },
            pageSize: [20, 50, 100],
            table: {
                columnList: [
                    {
                        prop: "boxCode",
                        label: "箱码"
                    },
                    {
                        prop: "skuCode",
                        label: "物品编码"
                    },
                    {
                        prop: "stockStatus",
                        label: "库存状态"
                    },
                    {
                        prop: "skuLot1",
                        label: "生产批次"
                    },
                    {
                        prop: "stockBalance",
                        label: "库存余额"
                    },
                    {
                        prop: "stockEnable",
                        label: "库存可用"
                    },
                    {
                        prop: "occupyQty",
                        label: "库存占用"
                    },

                    {
                        prop: "wsuCode",
                        label: "计量单位"
                    },
                    {
                        prop: "locCode",
                        label: "库位编码"
                    },
                    {
                        prop: "lpnCode",
                        label: "LPN"
                    },
                    {
                        prop: "skuLot2",
                        label: "规格型号"
                    },
                    {
                        prop: "skuLot3",
                        label: "收货日期"
                    },
                    {
                        prop: "skuLot4",
                        label: "专用客户"
                    },
                    {
                        prop: "skuLot5",
                        label: "钢背批次"
                    },
                    {
                        prop: "skuLot6",
                        label: "摩擦块批次",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot7",
                        label: "产品标识代码"
                    },
                    {
                        prop: "skuLot8",
                        label: "是否CRCC验证"
                    },
                    {
                        prop: "whCode",
                        label: "库房"
                    },
                    {
                        prop: "ownerCode",
                        label: "货主编码"
                    },
                    {
                        prop: "ownerName",
                        label: "货主名称"
                    },
                    {
                        prop: "createUserName",
                        label: "收货人"
                    },
                    {
                        prop: "taskId",
                        label: "任务号"
                    },
                    {
                        prop: "lastInTime",
                        label: "入库时间"
                    },
                    {
                        prop: "lastOutTime",
                        label: "出库时间"
                    },
                ],
            },
            fileUpload: {
                visible: false,
            },
        };
    },
    watch: {
        $route(to) {
            if (to.query && to.query.isRefresh === 'true') {
                this.refreshTable();
            }
        }
    },
    methods: {
        getTableData() {
            let hasNoQuery = true
            for (let item in this.form.params) {
                if (item !== 'isShowByBox' && func.isNotEmpty(this.form.params[item])) {
                    hasNoQuery = false
                }
            }
            if (hasNoQuery) {
                this.$message.warning("至少输入一个查询条件")
                return
            }
            page(this.page, this.form.params)
                .then((res) => {
                    let pageObj = res.data.data;
                    if (pageObj.records.length === 0) {
                        this.$message.warning("没有搜索到符合查询条件的内容")
                        return
                    }
                    this.table.data.length = 0
                    this.page.total = pageObj.total;
                    this.handleRefreshTable();

                    let arr = pageObj.records
                    let balanceSum = 0
                    let enableSum = 0
                    let occupySum = 0
                    arr.forEach((item, index) => {
                        this.table.data.push(item)
                        balanceSum += item.stockBalance
                        enableSum += item.stockEnable
                        occupySum += item.occupyQty
                        if (index != arr.length - 1) {
                            if (item.boxCode != arr[index + 1].boxCode) {
                                let a = {
                                    boxCode: '合计',
                                    stockBalance: balanceSum,
                                    stockEnable: enableSum,
                                    occupyQty: occupySum
                                }
                                balanceSum = 0,
                                    enableSum = 0,
                                    occupySum = 0
                                this.table.data.push(a)
                            }
                        } else {
                            let a = {
                                boxCode: '合计',
                                stockBalance: balanceSum,
                                stockEnable: enableSum,
                                occupyQty: occupySum
                            }
                            balanceSum = 0,
                                enableSum = 0,
                                occupySum = 0
                            this.table.data.push(a)
                        }
                    })
                });
        },
        refreshTable() {
            this.getTableData();
        },
        exportData() {
            this.loading = true;
            exportFile(this.form.params)
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, `库存列表${nowDateFormat("yyyyMMddhhmmss")}.xlsx`);
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        onSubmit() {
            this.getTableData();
        },
        onExportLocalData() {
            this.exportCurrentDataToExcel("库存表", "库存表");
        },
        onReset() {
            this.form.params = {
                createTimeDateRange: "",
            }
            this.onChange(null);
        },
        onChange(val) {
            if (val == null) {
                this.dateRange = [];
            }
            this.$emit('dateRangeChange', val);
        },
        arraySpanMethod({row, column, rowIndex, columnIndex}) {
            if (columnIndex === 0) {
                const _row = this.getSpanArr(this.table.data).one[rowIndex]
                const _col = _row > 0 ? 1 : 0
                return {
                    rowspan: _row,
                    colspan: _col,
                }
            }
        },
        tableRowClassName({row}) {
            if (row.boxCode === '合计') {
                return 'success-row';
            }
        },
        getSpanArr(arr) {
            if (arr) {
                const spanOneArr = []
                let concatOne = 0
                arr.forEach((item, index) => {
                    if (index === 0) {
                        spanOneArr.push(1)
                    } else {
                        if (item.boxCode === arr[index - 1].boxCode && item.boxCode != '合计') {
                            spanOneArr[concatOne] += 1
                            spanOneArr.push(0)
                        } else {
                            spanOneArr.push(1)
                            concatOne = index
                        }
                    }
                })
                return {one: spanOneArr}
            }
        },
    },
};
</script>
