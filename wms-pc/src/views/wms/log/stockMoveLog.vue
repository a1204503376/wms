<template>
    <div id="stockMoveLog">
        <nodes-master-page v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="物品" label-width="90px">
                            <nodes-sku-by-query
                                class="search-input"
                                v-model="form.params.skuIdList"
                                :clearable="true">
                            </nodes-sku-by-query>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="单据编码" label-width="90px">
                            <el-input
                                class="search-input"
                                placeholder="请输入单据编码"
                                v-model.trim="form.params.sourceBillNo" :clearable="true">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="移动类型" label-width="90px">
                            <nodes-stock-log-type
                                v-model="form.params.logTypeList"
                                class="search-input"
                                :multiple="true">
                            </nodes-stock-log-type>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="箱码" label-width="90px">
                            <el-input
                                v-model.trim="form.params.boxCode"
                                :clearable="true"
                                class="search-input"
                                placeholder="请输入箱码">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="库位" label-width="90px">
                            <nodes-location
                                class="search-input"
                                v-model.trim="form.params.locIdList"
                                :clearable="true">
                            </nodes-location>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="库区" label-width="90px">
                            <nodes-zone
                                :multiple="true"
                                class="search-input"
                                v-model="form.params.zoneIdList">
                            </nodes-zone>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="生产批次" label-width="90px">
                            <el-input
                                v-model.trim="form.params.skuLot1"
                                class="search-input"
                                :clearable="true"
                                placeholder="请输入生产批次">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="创建人" label-width="90px">
                            <el-input
                                class="search-input"
                                v-model.trim="form.params.createUser"
                                :clearable="true"
                                placeholder="请输入创建人">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="创建日期" label-width="90px">
                            <nodes-date-range
                                v-model="form.params.createTimeDateRange">
                            </nodes-date-range>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-slot:tableTool>
                <el-tooltip :enterable="false" class="item" content="刷新" effect="dark" placement="top">
                    <el-button circle icon="el-icon-refresh" size="mini" @click="onRefresh"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="显隐" effect="dark" placement="top">
                    <el-button circle icon="el-icon-s-operation" size="mini" @click="onColumnShowHide"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="服务端导出" effect="dark" placement="top">
                    <el-button circle icon="el-icon-download" size="mini" @click="exportData"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="本地导出" effect="dark" placement="top">
                    <excel-export :filename="exportExcelName" :sheet="exportExcelSheet"
                                  style="display: inline-block;margin-left: 10px">
                        <el-button circle icon="el-icon-bottom" size="mini" @click="onExportLocalData"/>
                    </excel-export>
                </el-tooltip>
            </template>
            <template v-slot:table>
                <el-table ref="table"
                          :data="table.data"
                          :height="table.height"
                          border
                          highlight-current-row
                          size="mini"
                          style="width: 100%"
                          @sort-change="onSortChange">
                    <el-table-column
                        fixed
                        width="50"
                        type="index">
                        <template slot="header">
                            #
                        </template>
                    </el-table-column>
                    <template v-for="(column, index) in table.columnList">
                        <el-table-column
                            width="170"
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column">
                        </el-table-column>
                    </template>
                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination :current-page="page.current" :page-size="page.size" :page-sizes="[20, 50, 100]"
                               :total="page.total" background layout="total, sizes, prev, pager, next, jumper"
                               v-bind="page"
                               @size-change="handleSizeChange" @current-change="handleCurrentChange">
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
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import {listMixin} from "@/mixins/list";
import fileDownload from "js-file-download";
import {ExcelExport} from 'pikaz-excel-js'
import fileUpload from "@/components/nodes/fileUpload";
import {nowDateFormat} from "@/util/date";
import {exportExcel, page} from "@/api/wms/stock/stockLog"
import NodesSkuByQuery from "@/components/wms/select/NodesSkuByQuery";
import NodesStockLogType from "@/components/wms/select/NodesStockLogType";
import NodesLocation from "@/components/wms/select/NodesLocation";
import NodesZone from "@/components/wms/select/NodesZone";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";

export default {
    name: "stockLog",
    components: {
        NodesWarehouse,
        NodesZone,
        NodesLocation,
        NodesSkuByQuery,
        DialogColumn,
        NodesMasterPage,
        NodesDateRange,
        NodesStockLogType,
        ExcelExport,
        fileUpload,
    },
    mixins: [listMixin],
    data() {
        return {
            form: {
                params: {
                    skuIdList: [],
                    sourceBillNo: "",
                    logTypeList: [],
                    createUser: "",
                    createTimeDateRange: ["", ""],
                    locIdList: [],
                    zoneIdList: [],
                    whIdList: [],
                    boxCode: "",
                    skuLot1: "",
                    stockId: '',
                },
            },
            table: {
                columnList: [
                    {
                        prop: "logType",
                        label: "库存日志类型",
                        sortable: "custom",
                    },
                    {
                        prop: "sourceBillNo",
                        label: "来源的单据编码",
                        sortable: "custom"
                    },
                    {
                        prop: "currentStayStockQty",
                        label: "本次操作待上架数量",
                        sortable: "custom"
                    },
                    {
                        prop: "currentStockQty",
                        label: "本次操作上架数量",
                        sortable: "custom"
                    },
                    {
                        prop: "currentPickQty",
                        label: "本次操作待下架数量",
                        sortable: "custom"
                    },
                    {
                        prop: "currentOccupyQty",
                        label: "本次占用数量",
                        sortable: "custom"
                    },
                    {
                        prop: "occupyQty",
                        label: "占用数量",
                        sortable: "custom"
                    },
                    {
                        prop: "stockStatus",
                        label: "库存状态",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLevel",
                        label: "层级",
                        sortable: "custom"
                    },
                    {
                        prop: "wspName",
                        label: "包装名称",
                        sortable: "custom"
                    },
                    {
                        prop: "skuCode",
                        label: "物品编码",
                        sortable: "custom"
                    },
                    {
                        prop: "skuName",
                        label: "物品名称",
                        sortable: "custom"
                    },
                    {
                        prop: "stayStockQty",
                        label: "待上架数量(操作之前)",
                        sortable: "custom"
                    },
                    {
                        prop: "stockQty",
                        label: "上架数量(操作之前)",
                        sortable: "custom"
                    },
                    {
                        prop: "pickQty",
                        label: "下架数量(操作之前)",
                        sortable: "custom"
                    },
                    {
                        prop: "boxCode",
                        label: "箱号",
                        sortable: "custom"
                    },
                    {
                        prop: "lpnCode",
                        label: "托盘号",
                        sortable: "custom"
                    },
                    {
                        prop: "locCode",
                        label: "库位编码",
                        sortable: "custom"
                    },
                    {
                        prop: "zoneCode",
                        label: "库区编码",
                        sortable: "custom"
                    },
                    {
                        prop: "ownerName",
                        label: "货主",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot1",
                        label: "生产批次",
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
                        prop: "msg",
                        label: "消息",
                        sortable: "custom"
                    },
                    {
                        prop: "createUser",
                        label: "创建人",
                        sortable: "custom"
                    },
                    {
                        prop: "createTime",
                        label: "创建时间",
                        sortable: "custom"
                    },
                ],
            },
            fileUpload: {
                visible: false,
            },
        };
    },
    created() {
        this.getTableData();
    },
    methods: {
        getTableData() {
            this.form.params.stockId = this.$route.query.stockId;
            page(this.page, this.form.params)
                .then((res) => {
                    let pageObj = res.data.data;
                    this.table.data = pageObj.records;
                    this.page.total = pageObj.total;
                    this.handleRefreshTable();
                })
        },
        refreshTable() {
            this.getTableData();
        },
        onReset() {
            this.form.params = {
                skuIdList: [],
                sourceBillNo: "",
                logTypeList: [],
                createUser: "",
                createTimeDateRange: ["", ""],
                locIdList: [],
                zoneIdList: [],
                whIdList: [],
                boxCode: "",
                skuLot1: "",
                stockId: '',
            }
        },
        exportData() {
            this.loading = true;
            exportExcel(this.form.params)
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, `库存日志${nowDateFormat("yyyyMMddhhmmss")}.xlsx`);
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        onExportLocalData() {
            this.exportCurrentDataToExcel("库存日志", "库存日志")
        },
    },
};
</script>
