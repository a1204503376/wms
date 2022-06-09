<template>
    <div id="list">
        <nodes-master-page :configure="masterConfig" :permission="permissionObj" v-on="form.events">
            <template v-slot:searchFrom>
                <el-form-item label="任务名称">
                    <el-input v-model="form.params.crontabTaskName" class="d-input"  :clearable="true" style="width: 150px"></el-input>
                </el-form-item>
                <el-form-item label="url">
                    <el-input v-model="form.params.url" class="d-input"  :clearable="true" style="width: 220px"></el-input>
                </el-form-item>
                <el-form-item label="请求方法">
                    <el-input v-model="form.params.method" class="d-input"  :clearable="true"></el-input>
                </el-form-item>
                <el-form-item label="cron表达式" >
                    <el-input v-model="form.params.cron" class="d-input" style="width: 180px"  :clearable="true"></el-input>
                </el-form-item>

            </template>
            <template v-slot:batchBtn>
                <el-button  icon="el-icon-plus" size="mini" type="primary" @click="onAdd">新增</el-button>
                <el-button  size="mini" type="danger" @click="onRemove" icon="el-icon-delete"
                            :plain="false">删除
                </el-button>
            </template>

            <template v-slot:table>
                <el-table
                    ref="table"
                    :data="table.data"
                    :header-cell-style="{ 'text-align': 'center' }"
                    border
                    highlight-current-row
                    size="mini"
                    row-key="receiveId"
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
                            v-bind="column"
                        >
                        </el-table-column>

                    </template>
                    <el-table-column label="运行状态"
                                     prop="status"
                                     width="150">
                        <template v-slot="{row}">
                            <el-tag :type="row.enabled === 1 ? 'success' : 'danger'"
                                    disable-transitions>{{
                                    row.enabled ===
                                    1 ? '运行中' : '停止运行'
                                }}
                            </el-tag>
                        </template>
                    </el-table-column>

                    <el-table-column
                        fixed="right"
                        label="操作"
                        width="180"
                    >
                        <template slot-scope="scope">
                            <el-button @click="handleClick(scope.row)" type="text" size="small" >编辑</el-button>
                            <el-button @click="onStart(scope.row)" type="text" size="small" >启动</el-button>
                            <el-button @click="onStop(scope.row)" type="text" size="small" >停止</el-button>
                            <el-button @click="onExecute(scope.row)" type="text" size="small" >执行一次</el-button>
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
import NodesAsnBillState from "@/components/wms/select/NodesAsnBillState";
import NodesInStoreMode from "@/components/wms/select/NodesInStoreMode";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import {getPage, start, remove,stop,execute} from "@/api/crontab/task";
import {listMixin} from "@/mixins/list";





export default {
    name: "list",
    components: {
        DialogColumn,
        NodesInStoreMode,
        NodesWarehouse,
        NodesAsnBillState,
        NodesMasterPage,
        NodesDateRange,
    },
    mixins: [listMixin],
    data() {
        return {
            masterConfig: {
                showExpandBtn: true,
                showPage: true
            },
            form: {
                params: {
                    crontabTaskName:'',
                    url:'',
                    method:'',
                    cron:'',
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
    methods: {
        getTableData() {
            getPage(this.page.current, this.page.size, this.form.params).then(res => {
                const data = res.data.data;
                this.table.data = data.records;
                this.page.total = data.total;
            });

        },
        onStop(row){
            if(row.enabled===0){
                this.$message.error('该任务已停止，请勿重复点击');
                throw new Error('该任务已停止，请勿重复点击');
            }
          stop(row.id) .then(() => {

              let a = row.status;
              this.$message.success(row.crontabTaskName+"停止成功");
              this.getTableData();
          });
        },
        onStart(row){
            if(row.enabled===1){
                this.$message.error('该任务已启动，请勿重复点击');
                throw new Error('该任务已启动，请勿重复点击');
            }
            start(row).then(() => {
                this.$message.success(row.crontabTaskName+"启动成功");
                this.getTableData();
            });
        },
        onExecute(row){
            execute(row).then(() => {
                this.$message.success(row.crontabTaskName+"执行成功");
            });
        },
        onAdd() {
            this.$router.push({
                name: '新增定时任务',
            });
        },
        handleClick(row){
            if(row.enabled===1){
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
                    if(e.enabled === 1){
                        this.$message.error(e.crontabTaskName+'已启动，请先停止任务');
                        throw new Error(e.crontabTaskName+'已启动，请先停止任务');
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
