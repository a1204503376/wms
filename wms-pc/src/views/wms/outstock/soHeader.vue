<template>
    <div id="soHeader">
        <nodes-master-page :permission="permissionObj" v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex">
                    <el-col :span="8">
                        <el-form-item label="发货单编码" label-width="90px">
                            <el-input v-model.trim="form.params.soBillNo"
                                      :clearable="true"
                                      placeholder="请输入发货单编码">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="上游编码" label-width="90px">
                            <el-input
                                v-model.trim="form.params.orderNo"
                                :clearable="true"
                                placeholder="请输入上有编码"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="单据类型" label-width="90px">
                            <nodes-bill-type
                                v-model="form.params.billTypeCdList"
                                :multiple="true"
                                io-type="O"
                                placeholder="请选择"></nodes-bill-type>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-slot:expandSearch>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="单据状态" label-width="90px">
                            <nodes-so-bill-state v-model="form.params.soBillStateList"></nodes-so-bill-state>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="客户" label-width="90px">
                            <nodes-customer
                                v-model.trim="form.params.customerIdList"
                                :multiple="true"
                                placeholder="请输入客户编码或名称"></nodes-customer>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="仓库" label-width="90px">
                            <nodes-warehouse
                                v-model="form.params.whIdList"
                                :multiple="true"
                            ></nodes-warehouse>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="创建日期" label-width="90px">
                            <nodes-date-range v-model="form.params.createTimeDateRange"></nodes-date-range>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="创建人" label-width="90px">
                            <el-input
                                v-model.trim="form.params.createUser"
                                :clearable="true"
                                placeholder="请输入创建人"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-slot:batchBtn>
                <el-button v-if="permissionObj.add" icon="el-icon-plus" size="mini" type="primary" @click="onAdd">新增
                </el-button>
                <el-button v-if="permissionObj.delete" icon="el-icon-delete" plain size="mini" type="danger"
                           @click="onRemove">删除
                </el-button>
            </template>
            <template v-slot:tableTool>
                <el-tooltip :enterable="false" class="item" content="刷新" effect="dark" placement="top">
                    <el-button circle icon="el-icon-refresh" size="mini" @click="onRefresh"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="显隐" effect="dark" placement="top">
                    <el-button circle icon="el-icon-s-operation" size="mini" @click="onColumnShowHide"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="服务端导出" effect="dark" placement="top">
                    <el-button circle icon="el-icon-download" size="mini" @click="onExportData"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="本地导出" effect="dark" placement="top">
                    <excel-export :filename="exportExcelName" :sheet="exportExcelSheet"
                                  style="display: inline-block;margin-left: 10px">
                        <el-button circle icon="el-icon-bottom" size="mini" @click="onExportLocalData"/>
                    </excel-export>
                </el-tooltip>

            </template>
            <template v-slot:table>
                <el-table ref="table" :data="table.data" border highlight-current-row
                          size="mini" @sort-change="onSortChange">
                    <el-table-column fixed type="selection" width="50">
                    </el-table-column>
                    <template v-for="(column, index) in table.columnList">
                        <el-table-column v-if="!column.hide && column.prop === 'soBillNo'" :key="index"
                                         show-overflow-tooltip
                                         v-bind="column" width="150">
                            <template v-slot="scope">
                                <el-link
                                    :underline="false"
                                    target="_blank"
                                    type="primary"
                                    @click="onView(scope.row)">{{ scope.row.soBillNo }}
                                </el-link>
                            </template>
                        </el-table-column>
                        <el-table-column
                            v-if="!column.hide && column.prop !== 'soBillNo'"
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column"
                            width="150">
                        </el-table-column>
                    </template>
                    <el-table-column align="center" fixed="right" label="操作" width="180">
                    <el-table-column align="center" fixed="right" label="操作" width="150">
                        <template v-slot="scope">
                            <el-button size="small" type="text" @click="onEdit(scope.row)">编辑</el-button>
                            <el-button size="small" type="text" @click="onClose(scope.row)">关闭</el-button>
                            <el-button size="small" type="text" @click="onPick(scope.row)">PC拣货</el-button>
                            <el-button size="small" type="text" @click="onDistribute(scope.row)">分配</el-button>
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
            <dialog-column v-bind="columnShowHide" @close="onColumnShowHide">
            </dialog-column>
        </div>
    </div>
</template>

<script>


import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import {listMixin} from "@/mixins/list";
import fileDownload from "js-file-download";
import {ExcelExport} from 'pikaz-excel-js'
import NodesSoBillState from "@/components/wms/select/NodesSoBillState";
import {closeSoBill, exportData, getPage, remove} from "@/api/wms/outstock/soHeader"
import NodesCustomer from "@/components/wms/select/NodesCustomer";
import NodesBillType from "@/components/wms/select/NodesBillType";
import {nowDateFormat} from "@/util/date";


export default {
    name: "soHeader",
    components: {
        NodesBillType,
        NodesCustomer,
        NodesSoBillState,
        NodesWarehouse,
        DialogColumn,
        NodesMasterPage,
        NodesDateRange,
        ExcelExport
    },
    mixins: [listMixin],
    data() {
        return {
            form: {
                params: {
                    soBillNo: '',
                    orderNo: '',
                    billTypeCdList: [],
                    soBillStateList: [],
                    customerIdList: [],
                    whIdList: [],
                    createTimeDateRange: ['', ''],
                    createUser: ''
                }
            },
            table: {
                columnList: [
                    {
                        prop: 'soBillNo',
                        label: '发货单编码',
                        sortable: 'custom',
                    },
                    {
                        prop: 'orderNo',
                        label: '上游编码',
                        sortable: 'custom',
                    },
                    {
                        prop: 'billTypeName',
                        label: '单据类型',
                        sortable: 'custom',
                    },
                    {
                        prop: 'soBillState',
                        label: '单据状态',
                        sortable: 'custom',
                    },
                    {
                        prop: 'customerCode',
                        label: '客户编码',
                        sortable: 'custom',
                    },
                    {
                        prop: 'customerName',
                        label: '客户名称',
                        sortable: 'custom',
                    },
                    {
                        prop: 'whName',
                        label: '仓库编码',
                        sortable: 'custom',
                    },
                    {
                        prop: 'createTime',
                        label: '创建时间',
                        sortable: 'custom',
                    },
                    {
                        prop: 'createUser',
                        label: '创建人',
                        sortable: 'custom',
                    },
                    {
                        prop: 'soBillRemark',
                        label: '备注',
                        sortable: 'custom',
                    },
                ]
            },
        }
    },
    async created() {
        await this.getTableData();
    },
    watch: {
        $route(to) {
            if (to.query && to.query.isRefresh === 'true') {
                this.refreshTable();
            }
        }
    },
    computed: {
        permissionObj() {
            return {
                search: this.vaildData(this.permission.so_header_search, false),
                add: this.vaildData(this.permission.so_header_add, false),
                delete: this.vaildData(this.permission.so_header_delete, false),
                pickPlan: this.vaildData(this.permission.so_header_pickPlan, false)
            }
        }
    },
    methods: {
        async getTableData() {
            await getPage(this.page, this.form.params)
                .then((res) => {
                    let pageObj = res.data.data;
                    this.table.data = pageObj.records;
                    this.page.total = pageObj.total;
                })
        },
        refreshTable() {
            this.getTableData();
        },
        onRemove() {
            let rows = this.$refs.table.selection;
            if (rows.length <= 0) {
                this.$message({
                    message: "警告，至少选择一条记录",
                    type: "warning",
                });
                return;
            }
            this.$confirm("此操作将删除, 是否删除?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                let soBillIdList = rows.map(item => item.soBillId);
                remove(soBillIdList)
                    .then((res) => {
                        this.$message({
                            type: "success",
                            message: res.data.msg,
                        });
                        this.getTableData();
                    })
            })
        },
        onExportData() {
            this.loading = true;
            exportData(this.form.params)
                .then((res) => {
                    console.log(res);
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, `发货单${nowDateFormat("yyyyMMddhhmmss")}.xlsx`);
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        onReset() {
            this.form.params = {
                soBillNo: '',
                orderNo: '',
                billTypeCdList: [],
                soBillStateList: [],
                customerIdList: [],
                whIdList: [],
                createTimeDateRange: ['', ''],
                createUser: ''
            }
        },
        onExportLocalData() {
            this.exportCurrentDataToExcel("发货单", "发货单");
        },
        onAdd() {
            this.$router.push({
                name: '新增发货单',
                params: {
                    soBillId: '0'
                }
            })
        },
        onEdit(row) {
            if (row.soBillState.trim() !== '未出库') {
                this.$message.warning("操作失败，该发货单正在处理中");
                return;
            }
            this.$router.push({
                name: '编辑发货单',
                params: {
                    soBillId: row.soBillId
                }
            })
        },
        onClose(row) {
            closeSoBill(row.soBillId).then((res) => {
                this.$message.success(res.data.msg);
                this.refreshTable();
            })
        },
        onDistribute(row){
            this.$router.push({
                name: '分配',
                params: {
                    soBillId: row.soBillId
                }
            })
        },
        onView(row) {
            this.$router.push({
                name: '发货单详情',
                params: {
                    soBillId: row.soBillId
                }
            })
        },
        onPick(row) {
            this.$router.push({
                name: 'PC拣货',
                params: {
                    soBillId: row.soBillId
                }
            })

        },
    }
}
</script>
