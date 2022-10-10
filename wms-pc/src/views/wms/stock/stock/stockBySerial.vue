<template>
    <div id='inventory'>
        <nodes-master-page :permission="permissionObj" v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="序列号" label-width="90px">
                            <el-input
                                v-model.trim="form.params.serial"
                                :clearable="true" class="search-input"
                                placeholder="请输入序列号">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="序列号范围" label-width="90px">
                            <el-input
                                v-model.trim="form.params.serialBegin" :clearable="true"
                                style="width:98px">
                            </el-input>
                            -
                            <el-input
                                v-model.trim="form.params.serialEnd" :clearable="true"
                                style="width:98px">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="物品编码" label-width="90px">
                            <nodes-sku
                                v-model="form.params.skuIds"
                                class="search-input">
                            </nodes-sku>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="箱号" label-width="90px">
                            <el-input
                                v-model.trim="form.params.boxCode" :clearable="true" class="search-input"
                                placeholder="请输入箱号">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row type="flex">
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
                    border
                    highlight-current-row
                    row-key="id"
                    size="mini"
                    style="width: 100%"
                    @sort-change="onSortChange"
                >
                    <el-table-column fixed type="selection" width="50"></el-table-column>
                    <el-table-column fixed type="index">
                        <template slot="header"> #</template>
                    </el-table-column>
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
            <template v-slot:page>
                <el-pagination
                    :page-sizes="pageSize"
                    background
                    layout="total, sizes, prev, pager, next, jumper"
                    v-bind="page"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                >
                </el-pagination>
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

<script>

import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import {listMixin} from "@/mixins/list";
import {exportBySerial, pageBySerial} from "@/api/wms/stock/stock";
import fileDownload from "js-file-download";
import {ExcelExport} from 'pikaz-excel-js';
import fileUpload from "@/components/nodes/fileUpload";
import NodesSku from "@/components/wms/select/NodesSkuByQuery";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import NodesOwner from "@/components/wms/select/NodesOwner";
import NodesStockStatus from "@/components/wms/select/NodesStockStatus";
import NodesLocation from "@/components/wms/select/NodesLocation";
import NodesSerial from "@/components/wms/select/NodesSerial";
import NodesZone from "@/components/wms/select/NodesZone";
import func from "@/util/func";


export default {
    name: "stockBySerial",
    components: {
        NodesZone,
        NodesSerial,
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
            dialogFormVisible: false,
            form: {
                params: {
                    skuIds: [],
                    locIdList: [],
                    skuLot1: "",
                    boxCode: "",
                    serial: "",
                    serialBegin: "",
                    serialEnd: "",
                }
            },
            pageSize: [20, 50, 100],
            table: {
                columnList: [
                    {
                        prop: "serialNumber",
                        label: "序列号",
                        sortable: "custom",
                    },
                    {
                        prop: "skuCode",
                        label: "物品编码",
                        sortable: "custom",
                    },
                    {
                        prop: "skuName",
                        label: "物品名称",
                        sortable: "custom",
                    },
                    {
                        prop: "stockStatus",
                        label: "库存状态",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot1",
                        label: "生产批次",
                        sortable: "custom"
                    },
                    {
                        prop: "wsuCode",
                        label: "计量单位",
                        sortable: "custom"
                    },
                    {
                        prop: "locCode",
                        label: "库位编码",
                        sortable: "custom"
                    },
                    {
                        prop: "boxCode",
                        label: "箱码",
                        sortable: "custom"
                    },
                    {
                        prop: "lpnCode",
                        label: "LPN",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot2",
                        label: "规格型号",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot3",
                        label: "收货日期",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot4",
                        label: "专用客户",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot5",
                        label: "钢背批次",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot6",
                        label: "摩擦块批次",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot7",
                        label: "产品标识代码",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot8",
                        label: "适用速度等级",
                        sortable: "custom"
                    },
                    {
                        prop: "whCode",
                        label: "库房",
                        sortable: "custom"
                    },
                    {
                        prop: "ownerCode",
                        label: "货主编码",
                        sortable: "custom"
                    },
                    {
                        prop: "ownerName",
                        label: "货主名称",
                        sortable: "custom"
                    },
                    {
                        prop: "createUserName",
                        label: "收货人",
                        sortable: "custom"
                    },
                    {
                        prop: "lastInTime",
                        label: "入库时间",
                        sortable: "custom"
                    },
                    {
                        prop: "lastOutTime",
                        label: "出库时间",
                        sortable: "custom"
                    },
                ],
            },
        };
    },
    created() {
        this.getTableData();
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
            if (func.isNotEmpty(this.form.params.serial) && (func.isNotEmpty(this.form.params.serialBegin) || func.isNotEmpty(this.form.params.serialEnd))) {
                this.$message.warning(`序列号和序列号范围不能同时搜索,请重新输入`);
                return;
            }
            pageBySerial(this.page, this.form.params)
                .then((res) => {
                    let pageObj = res.data.data;
                    this.table.data = pageObj.records;
                    this.page.total = pageObj.total;
                    this.handleRefreshTable();

                    let currentSku = this.pageSize.indexOf(pageObj.total)
                    if (currentSku === -1) {
                        this.pageSize.push(pageObj.total)
                    }

                });
        },
        refreshTable() {
            this.getTableData();
        },
        exportData() {
            this.loading = true;
            exportBySerial(this.form.params)
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, "库存列表.xlsx");
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
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
    },
};
</script>

<style lang="scss" scoped>

.button_enlarge {
    color: #909399;
    float: right;
    line-height: 22px;
    margin-right: 22px;
    font-size: 16px;
}
</style>
