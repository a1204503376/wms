<template>
    <div id="logError">
        <nodes-master-page :showExpandBtn="false" v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="操作方式">
                            <el-input
                                placeholder="请输入操作方式"
                                v-model.trim="form.params.method" :clearable="true"
                                class="search-input">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="请求url">
                            <el-input
                                placeholder="请输入url"
                                v-model.trim="form.params.requestUri" :clearable="true"
                                class="search-input">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="创建日期">
                            <nodes-date-range v-model="form.params.createTimeDateRange"></nodes-date-range>
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
                    <el-button circle icon="el-icon-refresh" size="mini" @click="onRefresh"></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="显隐"
                    effect="dark"
                    placement="top"
                >
                    <el-button circle icon="el-icon-s-operation" size="mini" @click="onColumnShowHide"></el-button>
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
                        <el-button circle icon="el-icon-bottom" size="mini" @click="onExportLocalData"/>
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
                    row-key="receiveId"
                    size="mini"
                    @sort-change="onSortChange">
                    <el-table-column
                        fixed
                        type="selection"
                        width="50">
                    </el-table-column>
                    <template v-for="(column,index) in table.columnList">
                        <el-table-column
                            v-if="!column.hide"
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column"
                            width="150"
                        >
                        </el-table-column>
                    </template>
                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination
                    :current-page="page.current"
                    :page-size="page.size"
                    :page-sizes="[20, 50, 100]"
                    :total="page.total"
                    background
                    layout="total, sizes, prev, pager, next, jumper"
                    v-bind="page"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange">
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
import {exportFile, page} from "@/api/wms/log/logError";
import {listMixin} from "@/mixins/list";
import fileDownload from "js-file-download";

import {ExcelExport} from 'pikaz-excel-js';
import {nowDateFormat} from "@/util/date";

export default {
    name: "list",
    components: {
        NodesMasterPage,
        DialogColumn,
        NodesDateRange,
        ExcelExport
    },
    mixins: [listMixin],
    data() {
        return {
            form: {
                params: {
                    method: '',
                    requestUri: '',
                    createTimeDateRange: '',
                }
            },

            table: {
                columnList: [
                    {
                        prop: 'exceptionName',
                        label: '异常名称',
                        sortable: 'custom'
                    },
                    {
                        prop: 'message',
                        label: '异常信息',
                        sortable: 'custom',
                    },

                    {
                        prop: 'serverIp',
                        label: '服务器IP地址',
                        sortable: 'custom'
                    },
                    {
                        prop: 'env',
                        label: '系统环境',
                        sortable: 'custom',
                    },
                    {
                        prop: 'method',
                        label: '请求方式',
                        sortable: 'custom',
                    },
                    {
                        prop: 'requestUri',
                        sortable: 'custom',
                        label: '请求uri'
                    },
                    {
                        prop: 'userAgent',
                        sortable: 'custom',
                        label: '用户代理'
                    },
                    {
                        prop: 'stackTrace',
                        sortable: 'custom',
                        label: '堆栈'
                    },
                    {
                        prop: 'lineNumber',
                        sortable: 'custom',
                        label: '错误行数'
                    },
                    {
                        prop: 'methodClass',
                        sortable: 'custom',
                        label: '方法类'
                    },
                    {
                        prop: 'fileName',
                        sortable: 'custom',
                        label: '文件名'
                    },
                    {
                        prop: 'methodName',
                        sortable: 'custom',
                        label: '方法名'
                    },
                    {
                        prop: 'params',
                        sortable: 'custom',
                        label: '提交数据'
                    },
                    {
                        prop: 'createBy',
                        sortable: 'custom',
                        label: '创建者'
                    },
                    {
                        prop: 'createTime',
                        sortable: 'custom',
                        label: '创建时间'
                    },

                ]
            },
        }
    },
    created() {
        this.getTableData();
    },
    methods: {
        onExportLocalData() {
            this.exportCurrentDataToExcel("异常日志", "异常日志");
        },
        getTableData() {
            page(this.page, this.form.params)
                .then((res) => {
                    let pageObj = res.data.data;
                    this.table.data = pageObj.records;
                    this.page.total = pageObj.total;
                    this.handleRefreshTable();
                })
        },
        exportData() {
            this.loading = true;
            exportFile(this.form.params)
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, `异常日志列表${nowDateFormat("yyyyMMddhhmmss")}.xlsx`);
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        onReset() {
            this.form.params = {};
        },
    }
}
</script>
