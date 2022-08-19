<template>
    <div id="serviceLog">
        <nodes-master-page v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="账号" label-width="90px">
                            <el-input
                                placeholder="请输入操作人员账号"
                                v-model.trim="form.params.userAccount"
                                class="search-input">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="名称" label-width="90px">
                            <el-input
                                placeholder="请输入操作人员名称"
                                v-model.trim="form.params.userRealName" class="search-input">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="单据编码" label-width="90px">
                            <el-input
                                placeholder="请输入单据编码"
                                v-model.trim="form.params.billNo" class="search-input">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="操作类型" label-width="90px">
                            <nodes-audit-log-type-state
                                class="search-input"
                                v-model="form.params.type" :multiple="true">
                            </nodes-audit-log-type-state>
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
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="刷新"
                    effect="dark"
                    placement="top"
                >
                    <el-button circle icon="el-icon-refresh" size="mini" @click="onRefresh"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="显隐" effect="dark" placement="top">
                    <el-button circle icon="el-icon-s-operation" size="mini" @click="onColumnShowHide"></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="本地导出"
                    effect="dark"
                    placement="top"
                >
                    <el-button circle icon="el-icon-bottom" size="mini"></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="服务端导出"
                    effect="dark"
                    placement="top"
                >
                    <el-button circle icon="el-icon-download" size="mini" @click="exportActionLists"></el-button>
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
                        width="50"
                        type="index">
                        <template slot="header">
                            #
                        </template>
                    </el-table-column>
                    <template v-for="(column,index) in table.columnList">
                        <el-table-column
                            v-if="!column.hide"
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column">
                        </el-table-column>
                    </template>
                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination
                    :page-size="page.size"
                    :page-sizes="[20, 50, 100]"
                    :total="page.total"
                    background
                    layout="total, sizes, prev, pager, next, jumper"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                >
                </el-pagination>
                <dialog-column v-bind="columnShowHide" @close="onColumnShowHide">
                </dialog-column>
            </template>
        </nodes-master-page>
    </div>
</template>

<script>

import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import NodesAuditLogTypeState from "@/components/wms/select/NodesAuditLogTypeState"
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import fileDownload from "js-file-download";
import {listMixin} from "@/mixins/list";
import {ExcelExport} from 'pikaz-excel-js';
import {exportActionLists, getLogActionLists} from "@/api/wms/log/serviceLog.js";
import fileUpload from "@/components/nodes/fileUpload";
import {nowDateFormat} from "@/util/date";

export default {
    name: "serviceLog",
    components: {
        DialogColumn,
        NodesSearchInput,
        NodesMasterPage,
        NodesDateRange,
        ExcelExport,
        fileUpload,
        NodesAuditLogTypeState
    },
    mixins: [listMixin],
    data() {
        return {
            params: {},
            excelParams: {
                userAccount: '',
                userRealName: '',
                billNo: '',
                type: [],
                createTimeDateRange: ['', ''],//更新时间开始 更新时间结束
            },
            form: {
                params: {
                    userAccount: '',
                    userRealName: '',
                    billNo: '',
                    type: [],
                    createTimeDateRange: ['', ''],//更新时间开始 更新时间结束
                }
            },
            table: {
                columnList: [
                    {
                        prop: 'userAccount',
                        label: '账号'
                    },
                    {
                        prop: 'userRealName',
                        label: '名称'
                    },
                    {
                        prop: 'type',
                        label: '操作类型'
                    },
                    {
                        prop: 'billId',
                        label: '单据ID'
                    },
                    {
                        prop: 'billNo',
                        label: '单据编码'
                    },
                    {
                        prop: 'log',
                        label: '操作内容'
                    },
                    {
                        prop: 'createTime',
                        label: '创建时间'
                    },
                    {
                        prop: 'createUser',
                        label: '创建人'
                    }
                ]
            },
            fileUpload: {
                visible: false,
            }
        }
    },
    created() {
        this.getTableData();
    },
    methods: {
        onExportLocalData() {
            this.exportCurrentDataToExcel("业务日志", "业务日志");
        },
        exportActionLists() {
            exportActionLists(this.form.params).then((res) => {
                fileDownload(res.data, `业务日志${nowDateFormat("yyyyMMddhhmmss")}.xlsx`);
            });
        },
        getTableData() {
            var that = this;
            that.params = that.form.params
            getLogActionLists(that.params, that.page).then((res) => {
                this.page.total = res.data.data.total;
                this.page.currentPage = res.data.data.pages;
                this.page.current = res.data.data.current;
                this.page.size = res.data.data.size;
                this.table.data = res.data.data.records;
                this.handleRefreshTable();
            });
        },
        refreshTable() {
            this.getTableData()
        },
    }
}
</script>
