<template>
    <div id="crontabTask1">
        <nodes-master-page :permission="permissionObj" :show-expand-btn="false" v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="任务名称" label-width="90px">
                            <el-input
                                v-model.trim="form.params.crontabTaskName"
                                :clearable="true"
                                class="search-input" placeholder="请输入任务名称">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="url" label-width="90px">
                            <el-input
                                v-model.trim="form.params.url"
                                :clearable="true" class="search-input" placeholder="请输入url">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-slot:batchBtn>
                <el-button v-if="permissionObj.add" icon="el-icon-plus" size="mini" type="primary" @click="onAdd">新增
                </el-button>
                <el-button v-if="permissionObj.delete" :plain="true" icon="el-icon-delete" size="mini" type="danger"
                           @click="onRemove">删除
                </el-button>
            </template>
            <template v-slot:table>
                <el-table
                    ref="table"
                    :data="table.data"
                    :header-cell-style="{ 'text-align': 'center' }"
                    :height="height"
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
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column">
                        </el-table-column>
                    </template>
                    <el-table-column
                        label="运行状态"
                        prop="status"
                        width="150">
                        <template v-slot="{row}">
                            <el-tag :type="row.enabled === 1 ? 'success' : 'danger'">
                                {{ row.enabled === 1 ? '运行中' : '停止运行' }}
                            </el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column
                        fixed="right"
                        label="操作"
                        width="250">
                        <template slot-scope="scope">
                            <el-button v-if="permissionObj.edit" size="small" type="text"
                                       @click="handleClick(scope.row)">编辑
                            </el-button>
                            <el-button v-if="permissionObj.start" size="small" type="text" @click="onStart(scope.row)">
                                启动
                            </el-button>
                            <el-button v-if="permissionObj.stop" size="small" type="text" @click="onStop(scope.row)">
                                停止
                            </el-button>
                            <el-button v-if="permissionObj.execute" size="small" type="text"
                                       @click="onExecute(scope.row)">执行一次
                            </el-button>
                            <el-button v-if="permissionObj.log" size="small" type="text" @click="onLog(scope.row)">日志
                            </el-button>
                        </template>
                    </el-table-column>
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
    </div>
</template>

<script>

import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import {execute, getPage, remove, start, stop} from "@/api/crontab/task";
import {listMixin} from "@/mixins/list";


export default {
    name: "crontabTask1",
    components: {
        DialogColumn,
        NodesWarehouse,
        NodesMasterPage,
        NodesDateRange,
    },
    mixins: [listMixin],
    data() {
        return {
            form: {
                params: {
                    crontabTaskName: '',
                    url: '',
                }
            },
            table: {
                columnList: [
                    {
                        prop: 'crontabTaskName',
                        label: '任务名称',
                        width: 180,
                    },
                    {
                        prop: 'url',
                        label: 'url',
                        width: 280
                    },
                    {
                        prop: 'method',
                        width: 150,
                        label: '请求方法'
                    },
                    {
                        prop: 'cron',
                        label: 'cron表达式',
                        width: 250,
                    },
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
                add: this.vaildData(this.permission.crontabTask_add, false),
                delete: this.vaildData(this.permission.crontabTask_delete, false),
                edit: this.vaildData(this.permission.crontabTask_edit, false),
                start: this.vaildData(this.permission.crontabTask_start, false),
                stop: this.vaildData(this.permission.crontabTask_stop, false),
                execute: this.vaildData(this.permission.crontabTask_execute, false),
                log: this.vaildData(this.permission.crontabTask_log, false),
            }
        }
    },
    methods: {
        getTableData() {
            getPage(this.page.current, this.page.size, this.form.params).then(res => {
                const data = res.data.data;
                this.table.data = data.records;
                this.page.total = data.total;
                this.handleRefreshTable();
            });
        },
        onStop(row) {
            if (row.enabled === 0) {
                this.$message.error('该任务已停止，请勿重复点击');
                throw new Error('该任务已停止，请勿重复点击');
            }
            stop(row.id).then(() => {

                let a = row.status;
                this.$message.success(row.crontabTaskName + "停止成功");
                this.getTableData();
            });
        },
        onStart(row) {
            if (row.enabled === 1) {
                this.$message.error('该任务已启动，请勿重复点击');
                throw new Error('该任务已启动，请勿重复点击');
            }
            start(row).then(() => {
                this.$message.success(row.crontabTaskName + "启动成功");
                this.getTableData();
            });
        },
        onExecute(row) {
            execute(row).then(() => {
                this.$message.success(row.crontabTaskName + "执行成功");
            });
        },
        onAdd() {
            this.$router.push({
                name: '新增定时任务',
            });
        },
        onLog(row) {
            this.$router.push({
                name: '任务日志',
                params: {
                    id: row.id,
                    crontabTaskName: row.crontabTaskName
                }
            });
        },
        handleClick(row) {
            if (row.enabled === 1) {
                this.$message.error('编辑前请先停止任务');
                throw new Error('编辑前请先停止任务');
            }
            this.$router.push({
                name: '编辑定时任务',
                params: {
                    id: row.id
                }
            });
        },
        onRemove() {
            this.$confirm("确定删除当前数据？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                let ids = []
                this.$refs.table.selection.forEach(e => {
                    if (e.enabled === 1) {
                        this.$message.error(e.crontabTaskName + '已启动，请先停止任务');
                        throw new Error(e.crontabTaskName + '已启动，请先停止任务');
                    }
                    ids.push(e.id)
                })
                remove(ids)
                    .then(() => {
                        this.$message.success('删除成功');
                        this.getTableData();
                    })
            })
        },
        onReset() {
            this.form.params = {};
        },
    }
}
</script>
