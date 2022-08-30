<template>
    <div id="carrier">
        <nodes-master-page v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex" class="search-elRow">
                    <el-col :span="6">
                        <el-form-item label="承运商编码" label-width="90px">
                            <el-input
                                v-model="form.params.code"
                                class="search-input" placeholder="请输入承运商编码">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="承运商名称" label-width="90px">
                            <el-input
                                v-model="form.params.name"
                                class="search-input" placeholder="请输入承运商名称">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="承运商简称" label-width="90px">
                            <el-input
                                v-model="form.params.simpleName"
                                class="search-input"
                                placeholder="请输入承运商简称">
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
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="修改日期" label-width="90px">
                            <nodes-date-range
                                v-model="form.params.updateTimeDateRange">
                            </nodes-date-range>
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
                    file-name="承运商"
                    template-url="/api/wms/carriers/export-template"
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
                    <el-button circle icon="el-icon-refresh" size="mini" @click="onRefresh"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="显隐" effect="dark" placement="top">
                    <el-button circle icon="el-icon-s-operation" size="mini" @click="onColumnShowHide"></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="服务端导出"
                    effect="dark"
                    placement="top"
                >
                    <el-button circle icon="el-icon-download" size="mini" @click="excelCarrier"></el-button>
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
                    size="mini"
                    style="width: 100%"
                    @sort-change="onSortChange"
                    @selection-change="selectionChange">
                    <el-table-column
                        fixed
                        type="selection"
                        width="50">
                    </el-table-column>
                    <el-table-column
                        fixed
                        type="index"
                        width="50">
                        <template slot="header">
                            #
                        </template>
                    </el-table-column>
                    <template v-for="(column,index) in table.columnList">
                        <el-table-column
                            v-if="!column.hide"
                            :key="index"
                            :width="130"
                            show-overflow-tooltip
                            v-bind="column">
                        </el-table-column>
                    </template>
                    <el-table-column
                        label="启用">
                        <template v-slot="scope">
                            <span v-if="scope.row.status>0">
                                <el-tag type="success">是</el-tag>
                            </span>
                            <span v-if="scope.row.status<0">
                                <el-tag type="danger">否</el-tag>
                            </span>
                        </template>
                    </el-table-column>
                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination
                    :page-size="page.size"
                    :page-sizes="[20, 50, 100]"
                    :total="page.total"
                    background
                    layout="total, sizes, prev, pager, next, jumper"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                >
                </el-pagination>
                <dialog-column v-bind="columnShowHide" @close="onColumnShowHide">
                </dialog-column>
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
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import fileDownload from "js-file-download";
import {listMixin} from "@/mixins/list";
import {ExcelExport} from 'pikaz-excel-js';
import {deleteCarrier, excelCarrier, getCarriersPage, importFile} from "@/api/wms/basics/Carrier.js";
import fileUpload from "@/components/nodes/fileUpload";
import {nowDateFormat} from "@/util/date";

export default {
    name: "carrier",
    components: {
        DialogColumn,
        NodesSearchInput,
        NodesMasterPage,
        NodesDateRange,
        ExcelExport,
        fileUpload
    },
    mixins: [listMixin],
    data() {
        return {
            selectionList: [],
            deleteParams: {
                list: []
            },
            masterConfig: {
                showExpandBtn: true
            },
            params: {},
            excelParams: {
                code: '',//承运商编码
                name: '',//承运商名称
                simpleName: '',//承运商简称
                createTimeDateRange: ['', ''],//创建时间开始 创建时间结束
                updateTimeDateRange: ['', ''],//更新时间开始 更新时间结束
            },
            form: {
                params: {
                    code: '',//承运商编码
                    name: '',//承运商名称
                    simpleName: '',//承运商简称
                    createTimeDateRange: ['', ''],//创建时间开始 创建时间结束
                    updateTimeDateRange: ['', ''],//更新时间开始 更新时间结束
                }
            },
            table: {
                columnList: [
                    {
                        prop: 'code',
                        sortable: 'custom',
                        label: '承运商编码'
                    },
                    {
                        prop: 'name',
                        label: '承运商名称',
                        sortable: 'custom',
                    },
                    {
                        prop: 'simpleName',
                        label: '承运商简称',
                        sortable: 'custom',
                    },
                    {
                        prop: 'ownerName',
                        label: '货主',
                        sortable: 'custom',
                    },
                    {
                        prop: 'remark',
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
                        label: '修改时间',
                        sortable: 'custom',
                    },
                    {
                        prop: 'updateUser',
                        label: '修改人',
                        sortable: 'custom',
                    }
                ]
            },
            fileUpload: {
                visible: false,
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
    created() {
        this.getTableData();
    },
    computed: {
        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
                ids.push(ele.id);
            });
            return ids.join(",");
        },
        codes() {
            let codes = [];
            this.selectionList.forEach(ele => {
                codes.push(ele.code);
            });
            return codes.join(",");
        },
        permissionObj() {
            return {
                add: this.vaildData(this.permission.carrier_add, false),
                delete: this.vaildData(this.permission.carrier_delete, false),
                import: this.vaildData(this.permission.carrier_import, false)
            }
        }

    },
    methods: {
        onExportLocalData() {
            this.exportCurrentDataToExcel("承运商", "承运商");
        },
        selectionChange(row) {
            this.selectionList = row;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.table.toggleSelection();
        },
        excelCarrier() {
            var that = this;
            that.excelParams.code = this.form.params.code;
            that.excelParams.name = this.form.params.name;
            that.excelParams.simpleName = this.form.params.simpleName;
            excelCarrier(that.excelParams).then((res) => {
                fileDownload(res.data, `承运商${nowDateFormat("yyyyMMddhhmmss")}.xlsx`);
            });
        },
        getTableData() {
            var that = this;
            that.params = this.form.params
            getCarriersPage(that.params, this.page).then((res) => {
                this.page.total = res.data.data.total;
                this.page.currentPage = res.data.data.pages;
                this.page.current = res.data.data.current;
                this.page.size = res.data.data.size;
                this.table.data = res.data.data.records;
                this.handleRefreshTable();
            });
        },
        refreshTable() {
            this.getTableData()
        },
        onAdd() {
            this.$router.push({
                name: '新增承运商',
                params: {
                    id: '0'
                }
            });
        },
        onRemove() {
            this.$confirm("确定删除供应商编码为" + this.codes + "的数据吗?", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(() => {
                this.deleteParams.list = this.ids.split(',');
                deleteCarrier(this.deleteParams.list).then((res) => {
                    if (res.data.code === 200) {
                        this.getTableData();
                        this.$message({
                            type: "success",
                            message: "操作成功!"
                        });
                    } else {
                        this.$message({
                            type: "error",
                            message: "操作失败!"
                        });
                    }
                });
            });
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
    }
}
</script>
