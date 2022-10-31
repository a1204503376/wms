<template>
    <div id="serial">
        <nodes-master-page :showSearchForm="false" v-on="form.events">
            <template v-slot:tableTool>
                <el-tooltip :enterable="false" class="item" content="刷新" effect="dark" placement="top">
                    <el-button circle icon="el-icon-refresh" size="mini" @click="onRefresh"></el-button>
                </el-tooltip>
                <!--                <el-tooltip :enterable="false" class="item" content="显隐" effect="dark" placement="top">-->
                <!--                    <el-button circle icon="el-icon-s-operation" size="mini" @click="onColumnShowHide"></el-button>-->
                <!--                </el-tooltip>-->
                <el-tooltip :enterable="false" class="item" content="全量导出" effect="dark" placement="top">
                    <el-button circle icon="el-icon-download" size="mini" @click="exportData"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="当前页导出" effect="dark" placement="top">
                    <excel-export :filename="exportExcelName" :sheet="exportExcelSheet"
                        style="display: inline-block;margin-left: 10px">
                        <el-button circle icon="el-icon-bottom" size="mini" @click="onExportLocalData" />
                    </excel-export>
                </el-tooltip>
            </template>
            <template v-slot:table>
                <el-table ref="table" :data="table.data" :height="table.height" border highlight-current-row size="mini"
                    style="width: 100%" @sort-change="onSortChange">
                    <template v-for="(column, index) in table.columnList">
                        <el-table-column v-if="!column.hide" :key="index" show-overflow-tooltip v-bind="column">
                        </el-table-column>
                    </template>
                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination :current-page="page.current" :page-size="page.size" :page-sizes="[20, 50, 100]"
                    :total="page.total" background layout="total, sizes, prev, pager, next, jumper" v-bind="page"
                    @size-change="handleSizeChange" @current-change="handleCurrentChange">
                </el-pagination>
            </template>
        </nodes-master-page>
        <!--        <div v-if="columnShowHide.visible">-->
        <!--            <dialog-column v-bind="columnShowHide" @close="onColumnShowHide">-->
        <!--            </dialog-column>-->
        <!--        </div>-->
    </div>
</template>

<script>

import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import {listMixin} from "@/mixins/list";
import {exportSerial, pageSerial} from "@/api/wms/stock/stock";
import fileDownload from "js-file-download";
import {ExcelExport} from 'pikaz-excel-js'
import fileUpload from "@/components/nodes/fileUpload";
import {nowDateFormat} from "@/util/date";

export default {
    name: "serial",
    components: {
        DialogColumn,
        NodesMasterPage,
        ExcelExport,
        fileUpload,
    },
    props: {
        stockId: {type: String, required: true},
    },
    mixins: [listMixin],
    data() {
        return {
            table: {
                columnList: [
                    {
                        prop: "serialNumber",
                        label: "序列号",
                        sortable: "custom",
                    },
                    {
                        prop: "serialState",
                        sortable: "custom",
                        label: "序列号状态",
                    },
                    {
                        prop: "instockNumber",
                        sortable: "custom",
                        label: "入库次数",
                    },
                    {
                        prop: "skuCode",
                        sortable: "custom",
                        label: "物品编码",
                    },
                    {
                        prop: "skuName",
                        sortable: "custom",
                        label: "物品名称",
                    },
                    {
                        prop: "boxCode",
                        sortable: "custom",
                        label: "箱码",
                    },
                    {
                        prop: "lpnCode",
                        sortable: "custom",
                        label: "LPN",
                    }
                ],
            },
            fileUpload: {
                visible: false,
            }
        };
    },
    watch: {
        stockId() {
            this.refreshTable();
        }
    },
    created() {
        this.getTableData();
    },
    methods: {
        getTableData() {
            pageSerial({stockId: this.stockId}, this.page)
                .then((res) => {
                    let pageObj = res.data.data;
                    this.table.data = pageObj.records;
                    this.page.total = pageObj.total;
                })
        },
        refreshTable() {
            this.getTableData();
        },
        exportData() {
            this.loading = true;
            exportSerial({stockId: this.stockId})
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, `序列号${nowDateFormat("yyyyMMddhhmmss")}.xlsx`);
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        onExportLocalData() {
            this.exportCurrentDataToExcel("序列号", "序列号")
        },
        // 覆盖混入list.js中的方法
        getCrudColumnList() {

        }
    },
};
</script>
