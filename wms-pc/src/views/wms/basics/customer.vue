<template>
    <div id='customer'>
        <nodes-master-page v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="客户编码" label-width="90px">
                            <el-input
                                placeholder="请输入客户编码"
                                v-model.trim="form.params.code" class="search-input">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="客户名称" label-width="90px">
                            <el-input
                                placeholder="请输入客户名称"
                                    v-model.trim="form.params.name" class="search-input">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="客户简称" label-width="90px">
                            <el-input
                                placeholder="请输入客户简称"
                                v-model.trim="form.params.simpleName"
                                class="search-input">
                            </el-input>
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
                        <el-form-item label="更新日期" label-width="90px">
                            <nodes-date-range v-model="form.params.updateTimeDateRange"></nodes-date-range>
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
                <el-button v-if="permissionObj.import" icon="el-icon-upload2" plain size="mini"
                           @click="onUpload">导入
                </el-button>
                <file-upload
                    :visible="fileUpload.visible"
                    file-name="客户"
                    template-url="/api/wms/customer/export-template"
                    @callback="callbackFileUpload"
                ></file-upload>
            </template>
            <template v-slot:tableTool>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="刷新"
                    effect="dark"
                    placement="top"
                >
                    <el-button
                        circle
                        icon="el-icon-refresh"
                        size="mini"
                        @click="onRefresh"
                    ></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="显隐"
                    effect="dark"
                    placement="top"
                >
                    <el-button
                        circle
                        icon="el-icon-s-operation"
                        size="mini"
                        @click="onColumnShowHide"
                    ></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="服务端导出"
                    effect="dark"
                    placement="top"
                >
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
            <template v-slot:table>
                <el-table
                    ref="table"
                    :data="table.data"
                    :height="table.height"
                    border
                    highlight-current-row
                    row-key="id"
                    size="mini"
                    style="width: 100%"
                    @sort-change="onSortChange"
                >
                    <el-table-column fixed type="selection" width="50"></el-table-column>
                    <el-table-column fixed type="index" width="50">
                        <template slot="header"> #</template>
                    </el-table-column>
                    <template v-for="(column, index) in table.columnList">
                        <el-table-column
                            v-if="!column.hide"
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column"
                            width="150">
                            <template v-slot="{row}" v-if="column.prop === 'status'">
                                <el-tag :type="row.status === '启用' ? 'success' : 'danger'">
                                    {{row.status === '启用' ? '是' : '否'}}
                                </el-tag>
                            </template>
                        </el-table-column>
                    </template>
                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination
                    :page-sizes="[20, 50, 100]"
                    background
                    layout="total, sizes, prev, pager, next, jumper"
                    v-bind="page"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                >
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
import NodesInStoreMode from "@/components/wms/select/NodesInStoreMode";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import {listMixin} from "@/mixins/list";
import {exportFile, importFile, page, remove} from "@/api/wms/basics/customer";
import fileDownload from "js-file-download";
import {ExcelExport} from 'pikaz-excel-js';
import fileUpload from "@/components/nodes/fileUpload";


export default {
    name: "customer",
    components: {
        DialogColumn,
        NodesSearchInput,
        NodesInStoreMode,
        NodesAsnBillState,
        NodesMasterPage,
        NodesDateRange,
        ExcelExport,
        fileUpload,
    },
    mixins: [listMixin],
    data() {
        return {
            woId: "",
            form: {
                params: {
                    code: "",
                    name: "",
                    simpleName: "",
                    createTimeDateRange: "",
                    updateTimeDateRange: "",
                }
            },
            deleteCustomerRequest: {
                ids: [],
            },
            table: {
                columnList: [
                    {
                        prop: "code",
                        label: "客户编码",
                        width: 120,
                        sortable: "custom",
                    },
                    {
                        prop: "name",
                        label: "客户名称",
                        sortable: "custom",
                    },
                    {
                        prop: "simpleName",
                        label: "客户简称",
                        sortable: "custom",
                    },
                    {
                        prop: 'ownerName',
                        label: '货主',
                        sortable: "custom",
                    },
                    {
                        prop: "country",
                        label: "国家",
                        sortable: "custom",
                    },
                    {
                        prop: "province",
                        label: "省",
                        sortable: "custom",
                    },
                    {
                        prop: "city",
                        label: "城市",
                        sortable: "custom",
                    },
                    {
                        prop: "address",
                        label: "街道",
                        sortable: "custom",
                    },
                    {
                        prop: "zipCode",
                        label: "邮编",
                        sortable: "custom"
                    },
                    {
                        prop: "remark",
                        label: "备注",
                        sortable: "custom",
                    },
                    {
                        prop: "createTime",
                        label: "创建时间",
                        sortable: "custom",
                    },
                    {
                        prop: "createUser",
                        label: "创建人",
                        sortable: "custom",
                    },
                    {
                        prop: "updateTime",
                        label: "更新时间",
                        sortable: "custom",
                    },
                ],
            },
            fileUpload: {
                visible: false,
            }
        };
    },
    created() {
        this.getTableData();
    },
    computed: {
        permissionObj() {
            return {
                add: this.vaildData(this.permission.customer_add, false),
                delete: this.vaildData(this.permission.customer_delete, false),
                import: this.vaildData(this.permission.custome_import, false)
            }
        }
    },
    watch: {
        $route(to) {
            if (to.query && to.query.isRefresh === 'true') {
                this.refreshTable();
            }
        }
    },
    methods: {
        getTableData() {
            page(this.form.params, this.page)
                .then((res) => {
                    let pageObj = res.data.data;
                    this.table.data = pageObj.records;
                    this.page.total = pageObj.total;
                    this.handleRefreshTable();
                });
        },
        refreshTable() {
            this.getTableData();
        },
        exportData() {
            this.loading = true;
            exportFile(this.form.params)
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, "客户列表.xlsx");
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        onSubmit() {
            this.getTableData();

        },
        onExportLocalData() {
            this.exportCurrentDataToExcel("客户表", "客户表");
        },
        onReset() {
            this.form.params = {
                createTimeDateRange: "",
            }
            this.onChange(null);
        },
        onChange(val) {
            if (val == null) {
                this.dateRange = [];
            }
            this.$emit('dateRangeChange', val);
        },
        onAdd() {
            this.$router.push({
                name: '新增客户',
                params: {
                    id: '0'
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
                    this.deleteCustomerRequest.ids.push(e.id)
                })
                remove(this.deleteCustomerRequest)
                    .then(() => {
                        this.$message.success('删除成功');
                        this.getTableData();
                    })
                    .catch(() => {
                    });
            })
        },
        callbackFileUpload(res) {
            this.fileUpload.visible = false;
            if (!res.result) {
                return;
            }
            let param = this.getFormData(res);
            importFile(param).then((res) => {
                this.$message.success(res.data.msg);
                this.refreshTable();
            })
        },
    },
};
</script>
