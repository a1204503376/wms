<template>
    <div id="logApi">
        <nodes-master-page :show-expand-btn="false" v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="日志标题">
                            <el-input
                                v-model.trim="form.params.title"
                                :clearable="true" class="search-input" placeholder="请输入日志标题">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="请求URI">
                            <el-input
                                v-model.trim="form.params.requestUri"
                                :clearable="true" class="search-input" placeholder="请输入请求URI">
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
                <el-table
                    ref="table"
                    :data="table.data"
                    :height="table.height"
                    border
                    highlight-current-row
                    size="mini"
                    style="width: 100%"
                    @sort-change="onSortChange">
                    <el-table-column
                        fixed
                        type="index"
                        width="50">
                        <template slot="header">
                            #
                        </template>
                    </el-table-column>
                    <template v-for="(column, index) in table.columnList">
                        <el-table-column
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column"
                            width="140">
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
import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import {listMixin} from "@/mixins/list";
import {exportFile, page} from "@/api/wms/log/logApi"
import fileDownload from "js-file-download";
import {ExcelExport} from 'pikaz-excel-js'
import fileUpload from "@/components/nodes/fileUpload";
import {nowDateFormat} from "@/util/date";


export default {
    name: "logApi",
    components: {
        DialogColumn,
        NodesSearchInput,
        NodesMasterPage,
        NodesDateRange,
        ExcelExport,
        fileUpload,
    },
    mixins: [listMixin],
    data() {
        return {
            form: {
                params: {
                    title: "",
                    requestUri: "",
                    createTimeDateRange: ["", ""],
                },
            },
            table: {
                columnList: [
                    {
                        prop: "title",
                        label: "日志标题",
                        sortable: "custom",
                    },
                    {
                        prop: "requestUri",
                        label: "请求Uri",
                        sortable: "custom",
                    },
                    {
                        prop: "method",
                        label: "操作方式",
                        sortable: "custom",
                    },
                    {
                        prop: "methodClass",
                        label: "方法类",
                        sortable: "custom",
                    },
                    {
                        prop: "methodName",
                        label: "方法名",
                        sortable: "custom",
                    },
                    {
                        prop: "serverHost",
                        label: "服务器名",
                        sortable: "custom",
                    },
                    {
                        prop: "serverIp",
                        label: "服务器ip地址",
                        sortable: "custom",
                    },
                    {
                        prop: "params",
                        label: "操作提交的数据",
                        sortable: "custom",
                    },
                    {
                        prop: "data",
                        label: "响应的数据",
                        sortable: "custom",
                    },
                    {
                        prop: "createBy",
                        label: "创建人",
                        sortable: "custom",
                    },
                    {
                        prop: "createTime",
                        label: "创建时间",
                        sortable: "custom",
                    },

                ],
            },
            fileUpload: {
                visible: false,
            }
        };
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
                requestUri: '',
                title: '',
                createTimeDateRange: ["", ""],
            }
        },
        exportData() {
            this.loading = true;
            exportFile(this.form.params)
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, `请求日志${nowDateFormat("yyyyMMddhhmmss")}.xlsx`);
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        onExportLocalData() {
            this.exportCurrentDataToExcel("请求日志", "请求日志")
        },
    },
};
</script>
