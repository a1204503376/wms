<template>
    <div id="asnHeader">
        <nodes-master-page :permission="permissionObj" v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex">
                    <el-col :span="8">
                        <el-form-item label="ASN单编码" label-width="90px">
                            <el-input v-model.trim="form.params.asnBillNo"
                                      placeholder="请输入ASN单编码"
                                      :clearable="true">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="物品编码" label-width="90px">
                            <nodes-sku-by-query v-model="form.params.skuIdList" :multiple="true"></nodes-sku-by-query>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="状态" label-width="90px">
                            <nodes-asn-bill-state v-model="form.params.asnBillStateList"></nodes-asn-bill-state>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-slot:expandSearch>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="创建日期" label-width="90px">
                            <nodes-date-range v-model="form.params.createTimeDateRange"></nodes-date-range>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="供应商" label-width="90px">
                            <el-input placeholder="请输入供应商编码或名称" v-model.trim="form.params.supplier" :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="上游编码" label-width="90px">
                            <el-input placeholder="请输入上游编码" v-model.trim="form.params.externalOrderNo" :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="上游创建人" label-width="90px">
                            <el-input placeholder="请输入上游创建人" v-model.trim="form.params.externalCreateUser" :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="仓库" label-width="90px">
                            <nodes-warehouse
                                v-model="form.params.whIdList"
                                :multiple="true"
                            ></nodes-warehouse>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-slot:batchBtn>
                <el-button v-if="permissionObj.add" icon="el-icon-plus" size="mini" type="primary" @click="onAdd">新增</el-button>
                <el-button v-if="permissionObj.delete" icon="el-icon-delete" plain size="mini" type="danger" @click="onRemove">删除</el-button>
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
                        <el-table-column v-if="!column.hide && column.prop === 'asnBillNo'" :key="index" show-overflow-tooltip
                                         v-bind="column" width="150">
                            <template v-slot="scope">
                                <el-link
                                    :underline="false"
                                    target="_blank"
                                    type="primary"
                                    @click="onView(scope.row)">{{ scope.row.asnBillNo }}
                                </el-link>
                            </template>
                        </el-table-column>
                        <el-table-column
                            v-if="!column.hide && column.prop !== 'asnBillNo'"
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column"
                            width="150">
                        </el-table-column>
                    </template>
                    <el-table-column align="center" fixed="right" label="操作" width="100">
                        <template v-slot="scope">
                            <el-button size="small" type="text" @click="onEdit(scope.row)">编辑</el-button>
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
import NodesAsnBillState from "@/components/wms/select/NodesAsnBillState";
import NodesInStoreMode from "@/components/wms/select/NodesInStoreMode";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import NodesSku from "@/components/wms/select/NodesSku";
import NodesSkuByQuery from "@/components/wms/select/NodesSkuByQuery";
import {listMixin} from "@/mixins/list";
import {exportData, getPage, remove} from "@/api/wms/instock/asnHeader";
import fileDownload from "js-file-download";
import {ExcelExport} from 'pikaz-excel-js'


export default {
    name: "asnHeader",
    components: {
        NodesSku,
        NodesWarehouse,
        DialogColumn,
        NodesInStoreMode,
        NodesAsnBillState,
        NodesMasterPage,
        NodesDateRange,
        NodesSkuByQuery,
        ExcelExport
    },
    mixins: [listMixin],
    data() {
        return {
            form: {
                params: {
                    asnBillNo: '',
                    skuIdList: [],
                    asnBillStateList: [10, 20],
                    createTimeDateRange: ['', ''],
                    supplier: '',
                    externalOrderNo: '',
                    externalCreateUser: '',
                    whIdList: []
                }
            },
            table: {
                columnList: [
                    {
                        prop: 'asnBillNo',
                        label: 'ASN单编码',
                        sortable: 'custom',
                    },
                    {
                        prop: 'billTypeName',
                        label: '单据类型',
                        sortable: 'custom',
                    },
                    {
                        prop: 'asnBillStateValue',
                        label: '单据状态',
                        // align: "center",
                        sortable: 'custom',
                    },
                    {
                        prop: 'supplierCode',
                        label: '供应商编码',
                        sortable: 'custom',
                    },
                    {
                        prop: 'supplierName',
                        label: '供应商名称',
                        sortable: 'custom',
                    },
                    {
                        prop: 'externalOrderNo',
                        label: '上游编码',
                        sortable: 'custom',
                    },
                    {
                        prop: 'externalCreateUser',
                        label: '上游创建人',
                        sortable: 'custom',
                    },
                    {
                        prop: 'whName',
                        label: '仓库编码',
                        sortable: 'custom',
                    },
                    {
                        prop: 'asnBillRemark',
                        label: '备注',
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
                        prop: 'updateTime',
                        label: '更新时间',
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
                search: this.vaildData(this.permission.header_search, false),
                add: this.vaildData(this.permission.header_add, false),
                delete: this.vaildData(this.permission.header_delete, false)
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
            })
                .then(() => {
                    let removeObj = {
                        asnBillIds: []
                    };
                    rows.forEach((item) => {
                        removeObj.asnBillIds.push(item.asnBillId);
                    });
                    remove(removeObj)
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
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, "ASN单.xlsx");
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        onReset() {
            this.form.params.asnBillNo = ''
            this.form.params.skuIdList = []
            this.form.params.asnBillStateList = [10, 20]
            this.form.params.createTimeDateRange = ['', '']
            this.form.params.supplier = ''
            this.form.params.externalOrderNo = ''
            this.form.params.externalCreateUser = ''
            this.form.params.whIdList = []
        },
        onExportLocalData() {
            this.exportCurrentDataToExcel("ASN单", "ASN单");
        },
        onAdd() {
            this.$router.push({
                name: '新增ASN单',
                params: {
                    asnBillId: '0'
                }
            })
        },
        onEdit(row) {
            if (row.asnBillStateValue !== '未收货') {
                this.$message.warning("操作失败，改ASN单已收货");
                return;
            }
            this.$router.push({
                name: '编辑ASN单',
                params: {
                    asnBillId: row.asnBillId
                }
            })
        },
        onView(row) {
            this.$router.push({
                name: 'ASN单详情',
                params: {
                    asnBillId: row.asnBillId
                }
            })
        },
    }
}
</script>
