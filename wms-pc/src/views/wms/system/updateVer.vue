<template>
    <div id="updateVer">
        <nodes-master-page v-on="form.events" :showSearchForm="false">
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
                    <template v-for="(column, index) in table.columnList">
                        <el-table-column
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column">
                        </el-table-column>
                    </template>
                    <el-table-column fixed="right" label="操作" width="100">
                        <template v-slot="scope">
                            <el-button size="small" @click="onEdit(scope.row)" type="text">编辑</el-button>
                        </template>
                    </el-table-column>
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
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import {listMixin} from "@/mixins/list";
import {exportFile, page} from "@/api/wms/system/updateVer";
import fileDownload from "js-file-download";
import {ExcelExport} from 'pikaz-excel-js'
import fileUpload from "@/components/nodes/fileUpload";
import {nowDateFormat} from "@/util/date";


export default {
    name: "updateVer",
    components: {
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
                    suvId: "",
                    verNum: "",
                    verName: "",
                    updateUrl: ""
                },
            },
            table: {
                columnList: [
                    {
                        prop: "verNum",
                        label: "版本号数值",
                        sortable: "custom",
                    },
                    {
                        prop: "verName",
                        label: "版本号名称",
                    },
                    {
                        prop: "updateUrl",
                        label: "更新地址",
                    }
                ],
            },
            fileUpload: {
                visible: false,
            }
        };
    },
    watch: {
        $route(to) {
            if (to.query && to.query.isRefresh === 'true') {
                this.refreshTable();
            }
        }
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
        exportData() {
            this.loading = true;
            exportFile(this.form.params)
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, `系统版本${nowDateFormat("yyyyMMddhhmmss")}.xlsx`);
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        onExportLocalData() {
            this.exportCurrentDataToExcel("系统版本", "系统版本")
        },
        onEdit(row) {
            this.$router.push({
                name: '系统版本-编辑',
                params: {
                    suvId: row.suvId
                }
            });
        },
    },
};
</script>
