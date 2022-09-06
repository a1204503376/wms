<template>
    <div id="taskDetail">
        <nodes-master-page v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="任务id" label-width="90px">
                            <el-input placeholder="请输入任务id" :clearable="true" class="search-input"
                                v-model.trim="form.params.taskId">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="单据编码" label-width="90px">
                            <el-input placeholder="请输入单据编码" :clearable="true" class="search-input"
                                v-model.trim="form.params.billNo">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="任务类型" label-width="90px">
                            <nodes-task-type class="search-input" v-model="form.params.taskTypeCdList" :multiple="true">
                            </nodes-task-type>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="任务状态" label-width="90px">
                            <nodes-task-state class="search-input" :default-value="true"
                                v-model="form.params.taskStateList" :multiple="true">
                            </nodes-task-state>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="物品编码" label-width="90px">
                            <nodes-sku-by-query class="search-input" v-model="form.params.skuIdList">
                            </nodes-sku-by-query>
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
                        <el-button circle icon="el-icon-bottom" size="mini" @click="onExportLocalData">
                        </el-button>
                    </excel-export>
                </el-tooltip>
            </template>
            <template v-slot:batchBtn>
                <el-button v-if="permissionObj.continue" size="mini" type="primary" @click="continueTask">继续执行
                </el-button>
                <el-button v-if="permissionObj.cancel" size="mini" type="primary" @click="cancelTask">取消任务
                </el-button>
            </template>
            <template v-slot:table>
                <el-table ref="table" :data="table.data" :height="table.height" border highlight-current-row size="mini"
                    style="width: 100%" @sort-change="onSortChange">
                    <el-table-column fixed type="selection" width="50">
                    </el-table-column>
                    <el-table-column fixed width="50" type="index">
                        <template slot="header">
                            #
                        </template>
                    </el-table-column>
                    <template v-for="(column, index) in table.columnList">
                        <el-table-column v-if="!column.hide" :key="index" show-overflow-tooltip v-bind="column"
                            width="150">
                            <template v-if="column.prop === 'taskState'" v-slot="scope">
                                <el-tag v-if="scope.row.taskState === '已下发' || scope.row.taskState === '开始执行'"
                                    type="success">{{ scope.row.taskState }}</el-tag>
                                <el-tag v-else-if="scope.row.taskState === '已完成' || scope.row.taskState === '已取消'"
                                    type="info">{{ scope.row.taskState }}</el-tag>
                                <el-tag v-else-if="scope.row.taskState === '未下发'" type="warning">{{ scope.row.taskState }}
                                </el-tag>
                                <el-tag v-else-if="scope.row.taskState === '异常中断中'" type="danger">{{ scope.row.taskState }}
                                </el-tag>
                                <el-tag v-else type="info">{{ scope.row.taskState }}</el-tag>
                            </template>
                        </el-table-column>
                    </template>
                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination :page-size="page.size" :page-sizes="[20, 50, 100]" :total="page.total" background
                    layout="total, sizes, prev, pager, next, jumper" @current-change="handleCurrentChange"
                    @size-change="handleSizeChange">
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
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
import NodesOwner from "@/components/wms/select/NodesOwner";
import NodesSku from "@/components/wms/select/NodesSku";

import fileDownload from "js-file-download";
import { listMixin } from "@/mixins/list";
import { cancelTask, continueTask, exportFile, getPage } from "@/api/wms/task/taskDetail";
import fileUpload from "@/components/nodes/fileUpload";
import { ExcelExport } from 'pikaz-excel-js';
import NodesSkuByQuery from "@/components/wms/select/NodesSkuByQuery";
import NodesTaskType from "@/components/wms/select/NodesTaskType";
import NodesTaskState from "@/components/wms/select/NodesTaskState";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import func from "@/util/func";
import { nowDateFormat } from "@/util/date";

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
                    taskStateList: [1, 2, 3, 4], // 默认未下发、已下发、开始执行、异常中断中
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
                        prop: 'remark',
                        label: '备注',
                        sortable: "custom",
                    },
                    {
                        prop: 'beginTime',
                        label: '开始执行时间',
                        sortable: "custom",
                    },
                    {
                        prop: 'closeTime',
                        label: '结束执行时间',
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
    computed: {
        permissionObj() {
            return {
                cancel: this.vaildData(this.permission.taskDetail_cancel, false),
                continue: this.vaildData(this.permission.taskDetail_continue, false)
            }
        }
    },
    methods: {
        onExportLocalData() {
            this.exportCurrentDataToExcel("工作任务", "工作任务");
        },
        exportData() {
            this.loading = true;
            exportFile(this.form.params)
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, `工作任务${nowDateFormat("yyyyMMddhhmmss")}.xlsx`);
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
                this.handleRefreshTable();
            });
        },
        cancelTask() {
            let rows = this.$refs.table.selection;
            if (func.isEmpty(rows)) {
                this.$message.error("请选择相应的任务");
                return;
            }

            for (let i in rows) {
                if (rows[i].taskState.trim() !== '异常中断中') {
                    this.$message.warning("只能选择异常中断中的任务进行操作");
                    return
                }
            }
            cancelTask(rows.map(x => x.taskId)).then(() => {
                this.$message.success("操作成功")
            })
        },
        continueTask() {
            let rows = this.$refs.table.selection;
            if (func.isEmpty(rows)) {
                this.$message.error("请选择相应的任务");
                return;
            }

            for (let i in rows) {
                if (rows[i].taskState.trim() !== '异常中断中') {
                    this.$message.warning("只能选择异常中断中的任务进行操作");
                    return
                }
            }
            continueTask(rows.map(x => x.taskId)).then(() => {
                this.$message.success("操作成功")
            })
        },
    }
}
</script>
