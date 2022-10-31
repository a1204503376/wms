<template>
    <div id="lpnType">
        <nodes-master-page :show-expand-btn="false" v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="类型编码" label-width="90px">
                            <el-input v-model="form.params.code" class="search-input" placeholder="请输入类型编码">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="容器类别" label-width="90px">
                            <nodes-lpn-type-state v-model="form.params.lpnType" class="search-input">
                            </nodes-lpn-type-state>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="创建日期" label-width="90px">
                            <nodes-date-range v-model="form.params.createTimeDateRange">
                            </nodes-date-range>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="修改日期" label-width="90px">
                            <nodes-date-range v-model="form.params.updateTimeDateRange">
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
                <el-tooltip :enterable="false" class="item" content="刷新" effect="dark" placement="top">
                    <el-button circle icon="el-icon-refresh" size="mini" @click="onRefresh"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="显隐" effect="dark" placement="top">
                    <el-button circle icon="el-icon-s-operation" size="mini" @click="onColumnShowHide"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="全量导出" effect="dark" placement="top">
                    <el-button circle icon="el-icon-download" size="mini" @click="excelLpnType"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="当前页导出" effect="dark" placement="top">
                    <excel-export :filename="exportExcelName" :sheet="exportExcelSheet"
                        style="display: inline-block;margin-left: 10px">
                        <el-button circle icon="el-icon-bottom" size="mini" @click="onExportLocalData">
                        </el-button>
                    </excel-export>
                </el-tooltip>
            </template>
            <template v-slot:table>
                <el-table ref="table" :data="table.data" :height="table.height" border class="table-fixed"
                    highlight-current-row size="mini" @sort-change="onSortChange" @selection-change="selectionChange">
                    <el-table-column fixed="left" type="selection" width="50">
                    </el-table-column>
                    <el-table-column fixed type="index" width="50">
                        <template slot="header">
                            #
                        </template>
                    </el-table-column>
                    <template v-for="(column,index) in table.columnList">
                        <el-table-column v-if="!column.hide" :key="index" :show-overflow-tooltip="true" v-bind="column">
                        </el-table-column>
                    </template>
                    <el-table-column fixed="right" label="操作" width="120">
                        <template v-slot="{row}">
                            <el-button size="mini" type="text" @click="onEdit(row)">编辑</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination :page-size="page.size" :page-sizes="[20, 50, 100]" :total="page.total" background
                    layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
                    @current-change="handleCurrentChange">
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
import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
import NodesLpnTypeState from "@/components/wms/select/NodesLpnTypeState";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import fileDownload from "js-file-download";
import {ExcelExport} from 'pikaz-excel-js';
import {listMixin} from "@/mixins/list";
import {deleteLpnType, excelLpnType, getLpnTypePage} from "@/api/wms/basics/LpnType.js";
import {nowDateFormat} from "@/util/date";

export default {
    name: "LpnType",
    components: {
        DialogColumn,
        ExcelExport,
        NodesSearchInput,
        NodesMasterPage,
        NodesDateRange,
        NodesLpnTypeState
    },
    mixins: [listMixin],
    data() {
        return {
            selectionList: [],
            deleteParams: {
                list: []
            },
            params: {},
            excelParams: {
                code: '',
                lpnType: '',
                createTimeDateRange: ['', ''],
                updateTimeDateRange: ['', ''],
            },
            form: {
                params: {
                    code: '',
                    lpnType: '',
                    createTimeDateRange: ['', ''],
                    updateTimeDateRange: ['', ''],
                }
            },
            table: {
                columnList: [
                    {
                        prop: 'code',
                        label: '类型编码'
                    },
                    {
                        prop: 'type',
                        label: '容器类别'
                    },
                    {
                        prop: 'lpnNoRule',
                        label: '编码规则'
                    },
                    {
                        prop: 'weight',
                        label: '容器重量（KG）'
                    },
                    {
                        prop: 'createTime',
                        label: '创建时间'
                    },
                    {
                        prop: 'createUser',
                        label: '创建人'
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
                add: this.vaildData(this.permission.lpnType_add, false),
                delete: this.vaildData(this.permission.lpnType_delete, false)
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
    methods: {
        onEdit(row) {
            this.$router.push({
                name: '编辑容器',
                params: {
                    id: row.id
                }
            });
        },
        onAdd() {
            this.$router.push({
                name: '新增容器',
                params: {
                    id: '0'
                }
            });
        },
        selectionChange(row) {
            this.selectionList = row;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.table.toggleSelection();
        },
        excelLpnType() {
            var that = this;
            that.excelParams.code = this.form.params.code;
            that.excelParams.lpnType = this.form.params.lpnType;
            excelLpnType(that.excelParams).then((res) => {
                fileDownload(res.data, `容器类别${nowDateFormat("yyyyMMddhhmmss")}.xlsx`);
            });
        },
        onExportLocalData() {
            this.exportCurrentDataToExcel("容器类别", "容器类别");
        },
        getTableData() {
            let that = this;
            that.params = this.form.params;
            getLpnTypePage(that.params, this.page).then((res) => {
                this.page.total = res.data.data.total;
                this.page.currentPage = res.data.data.pages;
                this.page.current = res.data.data.current;
                this.page.size = res.data.data.size;
                this.table.data = res.data.data.records;
                this.handleRefreshTable();
            });
        },
        onRemove() {
            this.$confirm("确定删除容器编码为" + this.codes + "的数据吗?", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(() => {
                this.deleteParams.list = this.ids.split(',');
                deleteLpnType(this.deleteParams.list).then((res) => {
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
    }
}
</script>

<style>

</style>
