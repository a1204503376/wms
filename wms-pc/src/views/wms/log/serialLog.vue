<template>
    <div id="serialLog">
        <nodes-master-page :show-expand-btn="false" v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="序列号" label-width="90px">
                            <el-input v-model.trim="form.params.serialNumberBegin" :clearable="true"
                                      style="width: 42%;">
                            </el-input>
                            -
                            <el-input v-model.trim="form.params.serialNumberEnd" :clearable="true" style="width: 42%;">
                            </el-input>
                            <el-tooltip placement="top">
                                <div slot="content">
                                    <span>1、只输入前者或后者，则模糊匹配输入值</span><br>
                                    <span>2、前者输入查找值，后者输入0，则查找大于输入值的序列号"</span><br>
                                    <span>3、后者输入查找值，前者输入0，则查找小于输入值的序列号"</span><br>
                                    <span>4、前者后者均输入查找值，则查找该范围内的序列号"</span>
                                </div>
                                <i class="el-icon-question" style="margin-left: 5px;height: 28px;line-height: 28px"></i>
                            </el-tooltip>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="批次" label-width="90px">
                            <el-input v-model.trim="form.params.lotNumber" :clearable="true" class="search-input"
                                      placeholder="请输入批次">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="序列号状态" label-width="90px">
                            <nodes-serial-state v-model="form.params.serialStateList" class="search-input">
                            </nodes-serial-state>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="入库日期" label-width="90px">
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
                <el-tooltip :enterable="false" class="item" content="全量导出" effect="dark" placement="top">
                    <el-button circle icon="el-icon-download" size="mini" @click="exportData"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="当前页导出" effect="dark" placement="top">
                    <excel-export :filename="exportExcelName" :sheet="exportExcelSheet"
                                  style="display: inline-block;margin-left: 10px">
                        <el-button circle icon="el-icon-bottom" size="mini" @click="onExportLocalData"/>
                    </excel-export>
                </el-tooltip>
            </template>
            <template v-slot:table>
                <el-table ref="table" :data="table.data" :height="height" border highlight-current-row size="mini"
                          style="width: 100%" @sort-change="onSortChange">
                    <el-table-column fixed type="selection" width="50">
                    </el-table-column>
                    <el-table-column fixed type="index" width="50">
                        <template slot="header">
                            #
                        </template>
                    </el-table-column>
                    <template v-for="(column, index) in table.columnList">
                        <el-table-column v-if="!column.hide" :key="index" show-overflow-tooltip v-bind="column">
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
            <dialog-column v-bind="columnShowHide" @close="onColumnShowHide">
            </dialog-column>
        </div>
    </div>
</template>

<script>

import {exportExcel, getPage} from "@/api/wms/log/serialLog";
import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import {listMixin} from "@/mixins/list";
import fileDownload from "js-file-download";
import {ExcelExport} from 'pikaz-excel-js'
import fileUpload from "@/components/nodes/fileUpload";
import {nowDateFormat} from "@/util/date";
import NodesSerialState from "@/components/wms/select/NodesSerialState";

export default {
    name: "serialLog",
    mixins: [listMixin],
    components: {
        NodesSerialState,
        DialogColumn,
        NodesSearchInput,
        NodesMasterPage,
        NodesDateRange,
        ExcelExport,
        fileUpload,
    },
    data() {
        return {
            form: {
                params: {
                    serialNumberBegin: '',
                    serialNumberEnd: '',
                    lotNumber: '',
                    createTimeDateRange: ['', ''],
                    serialStateList: []
                },
            },
            fileUpload: {
                visible: false,
            },
            table: {
                columnList: [
                    {
                        label: "序列号",
                        prop: "serialNumber",
                        sortable: "custom",
                    },
                    {
                        label: "物品编码",
                        prop: "skuCode",
                        sortable: "custom",
                    },
                    {
                        label: "物品名称",
                        prop: "skuName",
                        sortable: "custom",
                    },
                    {
                        label: "库位",
                        prop: "locCode",
                        sortable: "custom",
                    },
                    {
                        label: "箱码",
                        prop: "boxCode",
                        sortable: "custom",
                    },
                    {
                        label: "LPN",
                        prop: "lpnCode",
                        sortable: "custom",
                    },
                    {
                        label: "批次号",
                        prop: "lotNumber",
                        sortable: "custom",
                    },
                    {
                        label: "序列号状态",
                        prop: "serialState",
                        sortable: "custom",
                    },
                    {
                        label: "入库日期",
                        prop: "createTime",
                        sortable: "custom",
                    },
                ],
            },
        };
    },
    created() {
        this.getTableData();
    },
    methods: {
        getTableData() {
            getPage(this.page, this.form.params)
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
                serialNumberBegin: '',
                serialNumberEnd: '',
                lotNumber: '',
                createTimeDateRange: ['', ''],
                serialStateList: []
            }
        },
        exportData() {
            this.loading = true;
            exportExcel(this.form.params)
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, `序列号日志${nowDateFormat("yyyyMMddhhmmss")}.xlsx`);
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        onExportLocalData() {
            this.exportCurrentDataToExcel("序列号日志", "序列号日志")
        },
    },
};
</script>
<style lang="scss">

</style>
