<template>
    <el-form :inline="true"
             label-position="right"
             label-width="60"
             ref="searchForm"
             size="mini"
             v-model="form">
        <nodes-master-page :configure="masterConfig" :permission="permissionObj" v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex">
                    <el-col :span="8">
                        <el-form-item label="单据编码" label-width="90px">
                            <el-input v-model="form.params.billNo"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="物品编码" label-width="90px">
                            <nodes-sku style="width: 200px" v-model="form.params.skuCode"></nodes-sku>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="库存状态" label-width="90px">
                            <el-input v-model="form.params.stockStatus"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-slot:expandSearch>
                <el-row type="flex">
                    <el-col :span="7">
                        <el-form-item label="托盘号" label-width="90px">
                            <el-input v-model="form.params.lpnCode"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="7">
                        <el-form-item label="箱号" label-width="90px">
                            <el-input v-model="form.params.boxCode"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="7">
                        <el-form-item label="任务执行者" label-width="90px">
                            <el-input v-model="form.params.executorUserCode"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row type="flex">
                    <el-col :span="8">
                        <el-form-item label="更新时间" label-width="90px">
                            <nodes-date-range style="width: 200px"
                                              v-model="form.params.updateTimeDateRange"></nodes-date-range>
                        </el-form-item>
                    </el-col>
                </el-row>

            </template>
            <template v-slot:tableTool>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="刷新"
                    effect="dark"
                    placement="top"
                >
                    <el-button @click="onRefresh" circle icon="el-icon-refresh" size="mini"></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="显隐"
                    effect="dark"
                    placement="top"
                >
                    <el-button @click="onColumnShowHide" circle icon="el-icon-s-operation" size="mini"></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="服务端导出"
                    effect="dark"
                    placement="top"
                >
                    <el-button @click="excel" circle icon="el-icon-download" size="mini"></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="本地导出"
                    effect="dark"
                    placement="top"
                >
                    <excel-export :filename="exportExcelName" :sheet="exportExcelSheet"
                                  style="display: inline-block;margin-left: 10px">
                        <el-button @click="onExportLocalData" circle icon="el-icon-bottom" size="mini">
                        </el-button>
                    </excel-export>
                </el-tooltip>
            </template>
            <template v-slot:table>
                <el-table
                    :data="table.data"
                    @selection-change="selectionChange"
                    @sort-change="onSortChange"
                    border
                    highlight-current-row
                    ref="table"
                    size="mini"
                    style="width: 100%">
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
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column"
                            v-if="!column.hide">
                        </el-table-column>
                    </template>
                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination
                    :page-size="page.size"
                    :page-sizes="[20, 50, 100]"
                    :total="page.total"
                    @current-change="handleCurrentChange"
                    @size-change="handleSizeChange"
                    background
                    layout="total, sizes, prev, pager, next, jumper"
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
    getPage,
    exportTaskDetail
} from "@/api/wms/task/taskDetail.js";
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
                    billNo: '',
                    skuCode: '',
                    stockStatus: '',
                    lpnCode: '',
                    boxCode: '',
                    updateTimeDateRange: ['', ''],//更新时间开始 更新时间结束
                }
            },
            table: {
                columnList: [
                    {
                        prop: 'typeCd',
                        label: '任务类别'
                    },
                    {
                        prop: 'procType',
                        label: '任务执行方式'
                    },
                    {
                        prop: 'taskState',
                        label: '任务状态'
                    },
                    {
                        prop: 'taskDetailStatus',
                        label: '任务明细状态'
                    },
                    {
                        prop: 'billNo',
                        label: '单据编码'
                    },
                    {
                        prop: 'billLineNo',
                        label: '单据行号'
                    },
                    {
                        prop: 'skuCode',
                        label: '物品编码'
                    },
                    {
                        prop: 'skuLevel',
                        label: '层级'
                    },
                    {
                        prop: 'umCode',
                        label: '计量单位编码'
                    },
                    {
                        prop: 'umName',
                        label: '计量单位名称'
                    },
                    {
                        prop: 'planQty',
                        label: '计划数量'
                    },
                    {
                        prop: 'scanQty',
                        label: '实际任务执行数量'
                    },
                    {
                        prop: 'stockStatus',
                        label: '库存状态'
                    },
                    {
                        prop: 'targetLocCode',
                        label: '目标库位编码'
                    },
                    {
                        prop: 'lpnCode',
                        label: '托盘号'
                    },
                    {
                        prop: 'boxCode',
                        label: '箱号'
                    },
                    {
                        prop: 'snCode',
                        label: '序列号'
                    },
                    {
                        prop: 'source',
                        label: '任务来源'
                    },
                    {
                        prop: 'executorUserCode',
                        label: '任务执行者'
                    },
                    {
                        prop: 'ownerName',
                        label: '货主名称'
                    },
                    {
                        prop: 'whName',
                        label: '库房名称'
                    },
                    {
                        prop: 'beginTime',
                        label: '任务执行开始时间'
                    },
                    {
                        prop: 'remark',
                        label: '备注'
                    },
                    {
                        prop: 'skuLot1',
                        label: '生产批次'
                    },
                    {
                        prop: 'skuLot2',
                        label: '规格型号'
                    },
                    {
                        prop: 'skuLot3',
                        label: '收货日期'
                    },
                    {
                        prop: 'skuLot4',
                        label: '专用客户'
                    },
                    {
                        prop: 'skuLot5',
                        label: '钢背批次'
                    },
                    {
                        prop: 'skuLot6',
                        label: '摩擦块批次'
                    },
                    {
                        prop: 'skuLot7',
                        label: '产品标识代码'
                    },
                    {
                        prop: 'skuLot8',
                        label: 'CRCC'
                    },
                    {
                        prop: 'createUser',
                        label: '创建人'
                    },
                    {
                        prop: 'createTime',
                        label: '创建时间'
                    },
                    {
                        prop: 'updateUser',
                        label: '修改人'
                    },
                    {
                        prop: 'updateTime',
                        label: '修改时间'
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
            if (func.isNotEmpty(this.form.params.skuCode)) {
                this.form.params.skuCode = this.form.params.skuCode.skuCode;
            }
            that.excelParams = this.form.params;
            exportTaskDetail(that.excelParams).then((res) => {
                console.log(res)
                fileDownload(res.data, "任务详情.xlsx");
            });
        },
        getTableData() {
            var that = this;
            if (func.isNotEmpty(this.form.params.skuCode)) {
                this.form.params.skuCode = this.form.params.skuCode.skuCode;
            }
            that.params = this.form.params
            getPage(that.params, this.page).then((res) => {
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
