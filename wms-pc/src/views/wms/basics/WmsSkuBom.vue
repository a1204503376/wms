import fileDownload from "js-file-download";
<template>
    <el-form ref="searchForm"
             v-model="form"
             :inline="true"
             label-position="right"
             label-width="60"
             size="mini">
        <nodes-master-page :configure="masterConfig" :permission="permissionObj" v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex">
                    <el-col :span="8">
                        <el-form-item label="组合编码" label-width="90px">
                            <nodes-sku v-model="form.params.joinSkuCode"></nodes-sku>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="物品编码" label-width="90px">
                            <nodes-sku v-model="form.params.skuCode" style="width: 200px"></nodes-sku>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="货主" label-width="90px">
                            <nodes-owner v-model="form.params.woId" style="width: 200px"></nodes-owner>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-slot:expandSearch>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="更新时间" label-width="90px">
                            <nodes-date-range v-model="form.params.updateTimeDateRange"
                                              style="width: 200px"></nodes-date-range>
                        </el-form-item>
                    </el-col>
                </el-row>

            </template>
            <template v-slot:batchBtn>
                <el-button v-if="permissionObj.add" icon="el-icon-plus" size="mini" type="primary" @click="onAdd">新增
                </el-button>
                <el-button v-if="permissionObj.delete" size="mini" type="primary" @click="onRemove">删除</el-button>
                <el-button v-if="permissionObj.import" icon="el-icon-upload2" plain size="mini"
                           @click="onUpload">导入
                </el-button>
                <file-upload
                    :visible="fileUpload.visible"
                    file-name="物料清单"
                    template-url="/api/wms/WmsSkuBom/export-template"
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
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="显隐"
                    effect="dark"
                    placement="top"
                >
                    <el-button circle icon="el-icon-s-operation" size="mini" @click="onColumnShowHide"></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="服务端导出"
                    effect="dark"
                    placement="top"
                >
                    <el-button circle icon="el-icon-download" size="mini" @click="excel"></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="本地导出"
                    effect="dark"
                    placement="top"
                >
                    <excel-export :filename="exportExcelName" :sheet="exportExcelSheet" style="display: inline-block;margin-left: 10px">
                        <el-button circle icon="el-icon-bottom" size="mini" @click="onExportLocalData">
                        </el-button>
                    </excel-export>
                </el-tooltip>
            </template>
            <template v-slot:table>
                <el-table
                    ref="table"
                    :data="table.data"
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
                        sortable
                        type="index">
                        <template slot="header">
                            #
                        </template>
                    </el-table-column>
                    <template v-for="(column,index) in table.columnList">
                        <el-table-column
                            v-if="!column.hide"
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column">
                        </el-table-column>
                    </template>
                    <el-table-column
                        fixed="right"
                        label="操作"
                        width="120">
                        <template v-slot="{row}">
                            <el-button size="mini" type="text" @click="onEdit(row)">编辑</el-button>
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
            </template>
        </nodes-master-page>
    </el-form>
</template>

<script>


import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
import NodesOwner from "@/components/wms/select/NodesOwner";
import NodesSku from "@/components/wms/select/NodesSku";

import fileDownload from "js-file-download";
import {listMixin} from "@/mixins/list";
// eslint-disable-next-line no-unused-vars
import {
    getWmsSkuBomPage,
    excel,
    deleteWmsSkuBom,
    importData
} from "@/api/wms/basics/WmsSkuBom.js";
import func from "@/util/func";
import fileUpload from "@/components/nodes/fileUpload";
import {ExcelExport} from 'pikaz-excel-js';

export default {
    name: "carrier",
    components: {
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
            fileUpload: {
                visible: false,
            },
            selectionList: [],

            deleteParams: {
                list: []
            },
            masterConfig: {
                showExpandBtn: true
            },
            params: {},
            excelParams: {
                code: '',
                name: '',
                simpleName: '',
            },
            form: {
                params: {
                    joinSkuCode: '',
                    skuCode: '',
                    woId: '',
                    createTimeDateRange: ['', ''],//创建时间开始 创建时间结束
                    updateTimeDateRange: ['', ''],//更新时间开始 更新时间结束
                }
            },
            table: {
                columnList: [
                    {
                        prop: 'joinSkuCode',
                        label: '组合物品编码'
                    },
                    {
                        prop: 'joinSkuName',
                        label: '组合物品名称'
                    },
                    {
                        prop: 'skuCode',
                        label: '物品编码'
                    },
                    {
                        prop: 'skuName',
                        label: '物品名称'
                    },
                    {
                        prop: 'qty',
                        label: '数量'
                    },
                    {
                        prop: 'wsuCode',
                        label: '物品单位编码'
                    },
                    {
                        prop: 'joinWsuCode',
                        label: '组合单位编码'
                    },
                    {
                        prop: 'ownerName',
                        label: '货主名称'
                    },
                    {
                        prop: 'updateTime',
                        label: '修改时间'
                    },
                    {
                        prop: 'updateUser',
                        label: '修改人'
                    }
                ]
            },
        }
    },

    activated() {
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
                codes.push(ele.joinSkuCode);
            });
            return codes.join(",");
        },
        permissionObj() {
            return {
                search: this.vaildData(this.permission.wmsSkuBom_search, false),
                add: this.vaildData(this.permission.wmsSkuBom_add, false),
                delete: this.vaildData(this.permission.wmsSkuBom_delete, false),
                import: this.vaildData(this.permission.wmsSkuBom_import, false)
            }
        }

    },
    methods: {
        onExportLocalData() {
            this.exportCurrentDataToExcel("物料清单", "物料清单");
        },
        callbackFileUpload(res) {
            this.fileUpload.visible = false;
            if (!res.result) {
                return;
            }
            let param = this.getFormData(res);
            importData(param).then((res) => {
                this.$message.success(res.data.msg);
                this.getTableData();
            })
        },
        onRemove() {
            this.$confirm("确定删除容器编码为" + this.codes + "的数据吗?", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(() => {
                this.deleteParams.list = this.ids.split(',');
                deleteWmsSkuBom(this.deleteParams.list).then((res) => {
                    if (res.data.code == 200) {
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
        onAdd() {
            this.$router.push({
                name: '新增物料',
                params: {
                    id: '0'
                }
            });
        },
        onEdit(row) {
            this.$router.push({
                name: '编辑物料',
                params: {
                    id: row.id
                }
            });
        },
        hideOnSinglePage() {
        },
        selectionChange(row) {
            this.selectionList = row;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.table.toggleSelection();
        },
        excel() {
            var that = this;
            that.excelParams = this.form.params;
            excel(that.excelParams).then((res) => {
                fileDownload(res.data, "物料清单.xlsx");
            });
        },
        getTableData() {
            var that = this;
            that.params = this.form.params
            if (func.isNotEmpty(this.form.params.joinSkuCode.skuCode)) {
                that.params.joinSkuCode = this.form.params.joinSkuCode.skuCode
            }
            // eslint-disable-next-line no-empty
            if (func.isNotEmpty(this.form.params.skuCode.skuCode)) {
                that.params.skuCode = this.form.params.skuCode.skuCode
            }
            getWmsSkuBomPage(that.params, this.page).then((res) => {
                this.page.total = res.data.data.total;
                this.page.currentPage = res.data.data.pages;
                this.page.current = res.data.data.current;
                this.page.size = res.data.data.size;
                this.table.data = res.data.data.records;
            });
        },

    }
}
</script>
