<template>
    <div id="list">
        <nodes-master-page v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex" class="search-elRow">
                    <el-col :span="6">
                        <el-form-item label="收货单编码" label-width="90px">
                            <el-input
                                v-model="form.params.receiveNo"
                                :clearable="true"
                                class="search-input"
                                placeholder="请输入收货单编码">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="单据状态" label-width="90px">
                            <nodes-receive-bill-state
                                v-model="form.params.billStateList"
                                class="search-input">
                            </nodes-receive-bill-state>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="物品编码" label-width="90px">
                            <nodes-sku-by-query
                                v-model="form.params.skuIds"
                                class="search-input">
                            </nodes-sku-by-query>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="上游编码" label-width="90px">
                            <el-input
                                v-model="form.params.externalOrderNo"
                                :clearable="true"
                                class="search-input"
                                placeholder="请输入上游编码">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row type="flex">
                    <el-col v-if="false" :span="6">
                        <el-form-item label="ASN单编码" label-width="90px">
                            <el-input
                                v-model="form.params.asnBillNo"
                                :clearable="true"
                                class="search-input"
                                placeholder="请输入ASN单编码">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="仓库编码" label-width="90px">
                            <nodes-warehouse
                                v-model="form.params.whIds"
                                :multiple="true"
                                class="search-input">
                            </nodes-warehouse>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="上游创建人" label-width="90px">
                            <el-input
                                v-model="form.params.externalCreateUser"
                                :clearable="true"
                                class="search-input"
                                placeholder="请输入上游创建人">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="供应商编码" label-width="90px">
                            <el-input
                                v-model="form.params.supplierCode"
                                :clearable="true"
                                class="search-input"
                                placeholder="请输入供应商编码">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="创建日期" label-width="90px">
                            <nodes-date-range
                                v-model="form.params.createTimeDateRange">
                            </nodes-date-range>
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
            <template v-slot:tableTool>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="刷新"
                    effect="dark"
                    placement="top">
                    <el-button circle icon="el-icon-refresh" size="mini" @click="onRefresh"></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="显隐"
                    effect="dark"
                    placement="top">
                    <el-button circle icon="el-icon-s-operation" size="mini" @click="onColumnShowHide"></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="服务端导出"
                    effect="dark"
                    placement="top">
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
                    style="width: 100%"
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
                            min-width="150"
                            show-overflow-tooltip
                            v-bind="column">
                            <template v-if="column.prop === 'receiveNo'" v-slot="scope">
                                <el-link
                                    target="_blank"
                                    type="primary"
                                    @click="onViewDetails(scope.row.receiveId)">
                                    {{ scope.row.receiveNo }}
                                </el-link>
                            </template>
                        </el-table-column>
                    </template>
                    <el-table-column
                        fixed="right"
                        label="操作"
                        width="150">
                        <template v-slot="scope">
                            <el-button v-if="permissionObj.edit" size="small" type="text"
                                       @click="handleClick(scope.row)">编辑
                            </el-button>
                            <el-button v-if="permissionObj.close" size="small" type="text" @click="onClose(scope.row)">
                                关闭
                            </el-button>
                            <el-button v-if="permissionObj.receive" size="small" type="text"
                                       @click="onReceive(scope.row)">PC收货
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
import NodesAsnBillState from "@/components/wms/select/NodesAsnBillState";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import {close, exportFile, page, remove} from "@/api/wms/instock/receive";
import {listMixin} from "@/mixins/list";
import fileDownload from "js-file-download";
import NodesReceiveBillState from "../../../components/wms/select/NodesReceiveBillState";
import NodesSkuByQuery from "@/components/wms/select/NodesSkuByQuery";
import {ExcelExport} from 'pikaz-excel-js';
import {nowDateFormat} from "@/util/date";


export default {
    name: "list",
    components: {
        NodesSkuByQuery,
        NodesReceiveBillState,
        DialogColumn,
        NodesWarehouse,
        NodesAsnBillState,
        NodesMasterPage,
        NodesDateRange,
        ExcelExport
    },
    mixins: [listMixin],
    data() {
        return {
            form: {
                params: {
                    code: [],
                    receiveNo: '',
                    billStateList: [],
                    skuIds: [],
                    externalOrderNo: '',
                    asnBillNo: '',
                    whIds: [],
                    externalCreateUser: '',
                    supplierCode: '',
                    createTimeDateRange: '',
                }
            },
            billState: "",
            nums: {
                receiveIdList: [],
            },

            table: {
                columnList: [
                    {
                        prop: 'receiveNo',
                        label: '收货单编码',
                        sortable: 'custom'
                    },
                    {
                        prop: 'billStateDesc',
                        label: '单据状态',
                        sortable: 'custom',
                    },
                    {
                        prop: 'asnBillNo',
                        label: 'ASN单编码',
                        sortable: 'custom',
                        hide: true,
                    },
                    {
                        prop: 'externalOrderNo',
                        label: '上游编码',
                        sortable: 'custom',
                    },
                    {
                        prop: 'supplierCode',
                        label: '供应商编码',
                        sortable: 'custom',
                    },
                    {
                        prop: 'supplierName',
                        sortable: 'custom',
                        label: '供应商名称'
                    },
                    {
                        prop: 'whCode',
                        sortable: 'custom',
                        label: '仓库编码'
                    },
                    {
                        prop: 'createTime',
                        sortable: 'custom',
                        label: '创建日期'
                    },
                    {
                        prop: 'createUser',
                        sortable: 'custom',
                        label: '创建人'
                    },
                    {
                        prop: 'externalPreCreateDate',
                        sortable: 'custom',
                        label: '上游创建时间'
                    },
                    {
                        prop: 'externalCreateUser',
                        sortable: 'custom',
                        label: '上游创建人'
                    },
                    {
                        prop: 'updateUser',
                        sortable: 'custom',
                        label: '更新人'
                    },
                    {
                        prop: 'updateTime',
                        sortable: 'custom',
                        label: '更新时间'
                    },
                    {
                        prop: 'remark',
                        sortable: 'custom',
                        label: '消息'
                    },
                ]
            },
        }
    },
    computed: {
        permissionObj() {
            return {
                add: this.vaildData(this.permission.receive_add, false),
                delete: this.vaildData(this.permission.receive_delete, false),
                edit: this.vaildData(this.permission.receive_edit, false),
                close: this.vaildData(this.permission.receive_close, false),
                receive: this.vaildData(this.permission.receive_receive, false),
            }
        }
    },
    watch: {
        $route(to) {
            if (to.query && to.query.isRefresh === 'true') {
                this.getTableData();
            }
        }
    },
    created() {
        this.getTableData();
    },
    methods: {
        onExportLocalData() {
            this.exportCurrentDataToExcel("收货单", "收货单");
        },
        onViewDetails(id) {
            this.$router.push({
                name: '收货单详情',
                params: {
                    receiveId: id.toString()
                }
            });
        },
        onReceive(row) {
            let state = row.billState;
            if (state === "关闭") {
                this.$message.error('该收货单已关闭，不能进行收货');
                return
            }
            if (state === "全部收货") {
                this.$message.error('该收货单已全部收货，不能进行收货');
                return
            }
            if (state === "已取消") {
                this.$message.error('该收货单已取消，不能进行收货');
                return
            }

            this.$router.push({
                name: 'PC收货',
                params: {
                    receiveId: row.receiveId
                }
            });
        },
        getTableData() {
            page(this.page, this.form.params).then((res) => {
                let pageObj = res.data.data;
                this.table.data = pageObj.records;
                this.page.total = pageObj.total;
                this.handleRefreshTable();
            })
        },
        onAdd() {
            this.$router.push({
                name: '新增收货单',
                params: {
                    id: '0'
                }
            });
        },
        handleClick(row) {
            let state = row.billState;
            if (state !== "未收货") {
                this.$message.error('当前收货单不允许编辑');
                return
            }
            this.$router.push({
                name: '编辑收货单',
                params: {
                    receiveId: row.receiveId
                }
            });
        },
        onRemove() {
            this.$confirm("确定删除当前数据？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                this.$refs.table.selection.forEach(e => {
                    this.nums.receiveIdList.push(e.receiveId)
                })
                remove(this.nums).then(() => {
                    this.$message.success('删除成功');
                    this.getTableData();
                })
            })
        },
        onClose(row) {
            this.$confirm("确定关闭当前收货单？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                let state = row.billState;
                if (state === "关闭") {
                    this.$message.error('该收货单已关闭，请勿重复点击');
                    throw new Error('该收货单已关闭，请勿重复点击');
                }
                this.nums.receiveId = row.receiveId;
                close(this.nums)
                    .then(() => {
                        this.$message.success('关闭成功');
                        this.getTableData();
                    })
            })
        },

        exportData() {
            this.loading = true;
            exportFile(this.form.params)
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, `收货管理列表${nowDateFormat("yyyyMMddhhmmss")}.xlsx`);
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
