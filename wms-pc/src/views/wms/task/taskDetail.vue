<template>
    <el-form :inline="true"
             label-position="right"
             label-width="60"
             ref="searchForm"
             size="mini"
             v-model="form">
        <nodes-master-page :permission="permissionObj" v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex">
                    <el-col :span="8">
                        <el-form-item label="任务id" label-width="90px">
                            <el-input v-model="form.params.taskId"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="单据编码" label-width="90px">
                            <el-input v-model="form.params.billNo"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="任务类型" label-width="90px">
                            <nodes-task-type v-model="form.params.taskTypeCdList" multiple="true"></nodes-task-type>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-slot:expandSearch>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="任务状态" label-width="90px">
                            <nodes-task-state v-model="form.params.taskStateList" multiple="true"></nodes-task-state>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="物品编码" label-width="90px">
                            <nodes-sku-by-query v-model="form.params.skuIdList"></nodes-sku-by-query>
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
                    <el-button @click="onRefresh" circle icon="el-icon-refresh" size="mini"></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="显隐"
                    effect="dark"
                    placement="top"
                >
                    <el-button @click="onColumnShowHide" circle icon="el-icon-s-operation" size="mini"></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="服务端导出"
                    effect="dark"
                    placement="top"
                >
                    <el-button @click="exportData" circle icon="el-icon-download" size="mini"></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="本地导出"
                    effect="dark"
                    placement="top"
                >
                    <excel-export :filename="exportExcelName" :sheet="exportExcelSheet"
                                  style="display: inline-block;margin-left: 10px">
                        <el-button @click="onExportLocalData" circle icon="el-icon-bottom" size="mini">
                        </el-button>
                    </excel-export>
                </el-tooltip>
            </template>
            <template v-slot:table>
                <el-table
                    :data="table.data"
                    @selection-change="selectionChange"
                    @sort-change="onSortChange"
                    border
                    highlight-current-row
                    ref="table"
                    size="mini"
                    style="width: 100%">
                    <el-table-column
                        fixed
                        type="selection"
                        width="50">
                    </el-table-column>
                    <el-table-column
                        fixed
                        sortable
                        type="index">
                        <template slot="header">
                            #
                        </template>
                    </el-table-column>
                    <template v-for="(column,index) in table.columnList">
                        <el-table-column
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column"
                            width="130"
                            v-if="!column.hide">
                        </el-table-column>
                    </template>
                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination
                    :page-size="page.size"
                    :page-sizes="[20, 50, 100]"
                    :total="page.total"
                    @current-change="handleCurrentChange"
                    @size-change="handleSizeChange"
                    background
                    layout="total, sizes, prev, pager, next, jumper"
                >
                </el-pagination>
            </template>
        </nodes-master-page>
        <dialog-column v-bind="columnShowHide" @close="onColumnShowHide"></dialog-column>
    </el-form>
</template>

<script>
import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
import NodesOwner from "@/components/wms/select/NodesOwner";
import NodesSku from "@/components/wms/select/NodesSku";

import fileDownload from "js-file-download";
import {listMixin} from "@/mixins/list";
// eslint-disable-next-line no-unused-vars
import {exportFile, getPage} from "@/api/wms/task/taskDetail.js";
import fileUpload from "@/components/nodes/fileUpload";
import {ExcelExport} from 'pikaz-excel-js';
import NodesSkuByQuery from "@/components/wms/select/NodesSkuByQuery";
import NodesTaskType from "@/components/wms/select/NodesTaskType";
import NodesTaskState from "@/components/wms/select/NodesTaskState";
import DialogColumn from "@/components/element-ui/crud/dialog-column";


export default {
    name: "carrier",
    components: {
        DialogColumn,
        NodesTaskState,
        NodesTaskType,
        NodesSkuByQuery,
        NodesSearchInput,
        NodesMasterPage,
        NodesDateRange,
        NodesOwner,
        NodesSku,
        fileUpload,
        ExcelExport
    },
    mixins: [listMixin],
    data() {
        return {
            form: {
                params: {
                    taskId: '',
                    billNo: '',
                    taskTypeCdList: [],
                    taskStateList: [],
                    skuIdList: [],
                }
            },
            table: {
                columnList: [
                    {
                        prop: 'taskId',
                        label: '任务Id',
                        sortable: "custom",
                    },
                    {
                        prop: 'billNo',
                        label: '单据编码',
                        sortable: "custom",
                    },
                    {
                        prop: 'taskTypeCd',
                        label: '任务类型',
                        sortable: "custom",
                    },
                    {
                        prop: 'taskState',
                        label: '任务状态',
                        sortable: "custom",
                    },
                    {
                        prop: 'skuCode',
                        label: '物品编码',
                        sortable: "custom",
                    },
                    {
                        prop: 'taskQty',
                        label: '数量',
                        sortable: "custom",
                    },
                    {
                        prop: 'scanQty',
                        label: '实际数量',
                        sortable: "custom",
                    },
                    {
                        prop: 'umCode',
                        label: '计量单位',
                        sortable: "custom",
                    },
                    {
                        prop: 'lot',
                        label: '批次号',
                        sortable: "custom",
                    },
                    {
                        prop: 'fromLocCode',
                        label: '来源库位',
                        sortable: "custom",
                    },
                    {
                        prop: 'toLocCode',
                        label: '目标库位',
                        sortable: "custom",
                    },
                    {
                        prop: 'boxCode',
                        label: '箱号',
                        sortable: "custom",
                    },
                    {
                        prop: 'lpnCode',
                        label: '托盘号',
                        sortable: "custom",
                    },
                    {
                        prop: 'allotTime',
                        label: '任务下发时间',
                        sortable: "custom",
                    }
                ]
            },
        }
    },

    created() {
        this.getTableData();
    },

    methods: {
        onExportLocalData() {
            this.exportCurrentDataToExcel("工作任务", "工作任务");
        },
        callbackFileUpload(res) {
            this.fileUpload.visible = false;
            if (!res.result) {
                return;
            }
            let param = this.getFormData(res);
            importData(param).then((res) => {
                this.$message.success(res.data.msg);
                this.getTableData();
            })
        },

        selectionChange(row) {
            this.selectionList = row;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.table.toggleSelection();
        },
        exportData() {
            this.loading = true;
            exportFile(this.form.params)
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
        getTableData() {
            getPage(this.page, this.form.params).then((res) => {
                let pageObj = res.data.data;
                this.table.data = pageObj.records;
                this.page.total = pageObj.total;
            });
        },

    }
}
</script>
