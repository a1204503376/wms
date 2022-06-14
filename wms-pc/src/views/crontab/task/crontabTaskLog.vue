
<template>
    <div id="list">
        <nodes-master-page :configure="masterConfig" :permission="permissionObj" v-on="form.events">
            <template v-slot:searchFrom>
                <el-form-item label="任务名称">
                    <el-input v-model="form.params.crontabTaskName" class="d-input"  :clearable="true"></el-input>
                </el-form-item>
                <el-form-item label="操作人账号">
                    <el-input v-model="form.params.userAccount" class="d-input"  :clearable="true"></el-input>
                </el-form-item>
                <el-form-item label="操作时间">
                    <nodes-date-range v-model="form.params.createTimeDateRange"></nodes-date-range>
                </el-form-item>
            </template>
            <template v-slot:table>               <el-table
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
import NodesInStoreMode from "@/components/wms/select/NodesInStoreMode";
import NodesSku from "@/components/wms/select/NodesSku";
import NodesLineNumber from "@/components/wms/table/NodesLineNumber";
import {editDetailMixin} from "@/mixins/editDetail";
// eslint-disable-next-line no-unused-vars
import {listMixin} from "@/mixins/list";
import {getLog} from "@/api/crontab/task";
import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
export default {
    props: {
        id: {type: String},
        crontabTaskName: {type: String}
    },
    name: "Log",
    components: {NodesDateRange,NodesMasterPage},
    mixins: [listMixin],
    data() {
        return {
            that:this,
            page: {
                total: 0,
                size: 20,
                current: 1,
                ascs: "", //正序字段集合
                descs: "", //倒序字段集合
            },
            form:{
                params:{
                    crontabTaskName:this.crontabTaskName,
                  userAccount:'',
                  createTimeDateRange:'',
                }
            },
            table: {
                columnList: [
                    {
                        prop: 'crontabTaskName',
                        label: '任务名称',
                        align: 'center'
                    },
                    {
                        prop: 'userAccount',
                        label: '操作人员账号',
                        align: 'center'
                    },
                    {
                        prop: 'userRealName',
                        label: '操作人员姓名',
                        align: 'center'
                    },
                    {
                        prop: 'log',
                        label: '操作内容',
                        // left/center/right
                        align: 'center'
                    },
                    {
                        prop: 'createTime',
                        label: '操作时间',
                        // left/center/right
                        align: 'center'
                    },


                ]
            },

        }
    },
    watch:{
        id(){
            this.getTableData()
        }
    },
    created() {
        this.getTableData();
    },
    methods: {
        getTableData() {
            getLog(this.form.params,this.page)
                .then((res) => {
                    const data = res.data.data;
                    this.table.data = data.records;
                    this.table.data.crontabTaskName = this.id;
                    this.page.total = data.total;
                })
        },
    }
 }
</script>

<style scoped>
.d-table-header-required:before {
    content: "*";
    color: #F56C6C;
    margin-right: 4px;
}
</style>
