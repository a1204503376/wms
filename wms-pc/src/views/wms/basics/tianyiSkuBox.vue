<template>
    <div id="container">
        <nodes-master-page :show-expand-btn="false" v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="识别码" label-width="90px">
                            <el-input v-model.trim="form.params.id" :clearable="true" class="search-input"
                                placeholder="请输入识别码">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="物品名称" label-width="90px">
                            <el-input :clearable="true" placeholder="请输入物品名称" v-model="form.params.skuName">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="规格" label-width="90px">
                            <el-input :clearable="true" placeholder="请输入规格" v-model="form.params.skuSpec">
                            </el-input>
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
                    <el-table-column fixed type="selection" width="50">
                    </el-table-column>
                    <el-table-column fixed type="index" width="50">
                        <template slot="header">
                            #
                        </template>
                    </el-table-column>
                    <template v-for="(column, index) in table.columnList">
                        <el-table-column :formatter="formatterId" v-if="!column.hide" :key="index"
                            :show-overflow-tooltip="true" v-bind="column">
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
        <div v-if="columnShowHide.visible">
            <dialog-column v-bind="columnShowHide" @close="onColumnShowHide">
            </dialog-column>
        </div>
    </div>
</template>

<script>

import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import {listMixin} from "@/mixins/list";
import {exportExcel, page} from "@/api/wms/basics/tianyiSkuBox";
import fileDownload from "js-file-download";
import {ExcelExport} from 'pikaz-excel-js'
import fileUpload from "@/components/nodes/fileUpload";
import {nowDateFormat} from "@/util/date";
import NodesSkuByQuery from "@/components/wms/select/NodesSkuByQuery";
import NodesSkuSpec from "@/components/wms/select/NodesSkuSpec";

export default {
    name: "tianyiSkuBox",
    components: {
        NodesSkuSpec,
        NodesSkuByQuery,
        DialogColumn,
        NodesMasterPage,
        ExcelExport,
        fileUpload,
    },
    mixins: [listMixin],
    data() {
        return {
            form: {
                params: {
                    id: "",
                    skuName: "",
                    skuSpec: "",
                },
            },
            table: {
                columnList: [
                    {
                        prop: "id",
                        label: "识别码",
                        sortable: "custom"
                    },
                    {
                        prop: "skuName",
                        label: "物品名称",
                        sortable: "custom"
                    },
                    {
                        prop: "skuSpec",
                        label: "型号",
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
            }
        };
    },
    created() {
        this.getTableData();
    },
    methods: {
        getTableData() {
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
                id: "",
                skuName: "",
                skuSpec: "",
            }
        },
        exportData() {
            this.loading = true;
            exportExcel(this.form.params)
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, `箱贴识别码${nowDateFormat("yyyyMMddhhmmss")}.xlsx`);
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        onExportLocalData() {
            this.exportCurrentDataToExcel("箱贴识别码", "箱贴识别码")
        },
        formatterId(row, column, cellValue) {
            return (column.property === "id" && row.id < 10) ? '0' + cellValue.toString() : cellValue
        }
    },
};
</script>
