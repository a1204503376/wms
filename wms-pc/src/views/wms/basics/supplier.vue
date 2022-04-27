<template>
    <el-form ref="searchForm"
             v-model="searchForm"
             :inline="true"
             label-position="right"
             label-width="60"
             size="mini">
        <nodes-master-page :configure="masterConfig">
            <template v-slot:searchFrom>
                <el-form-item label="供应商名称">
                    <el-input v-model="searchForm.name"></el-input>
                </el-form-item>
                <el-form-item label="供应商编码">
                     <el-input v-model="searchForm.code"></el-input>
                </el-form-item>
            </template>
            <template v-slot:expandSearch>
                <el-row type="flex">
                    <el-col :span="24">
                        <el-form-item label="创建日期">
                            <nodes-date-range v-model="searchForm.createDateRange"></nodes-date-range>
                        </el-form-item>
                        <el-form-item label="更新日期">
                            <nodes-date-range v-model="searchForm.updateDateRange"></nodes-date-range>
                        </el-form-item>
                </el-col>
                </el-row>
            </template>
            <template v-slot:searchBtn>
                <el-button type="primary" @click="onSubmit">查找</el-button>
                <el-button @click="onReset">重置</el-button>
            </template>
            <template v-slot:batchBtn>
                <el-button size="mini" type="primary" @click="onRemove">删除</el-button>
                <el-button size="mini" type="primary">冻结</el-button>
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
                    content="本地导出"
                    effect="dark"
                    placement="top"
                >
                    <el-button circle icon="el-icon-bottom" size="mini"></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="服务端导出"
                    effect="dark"
                    placement="top"
                >
                    <el-button circle icon="el-icon-download" size="mini"></el-button>
                </el-tooltip>
            </template>
            <template v-slot:table>
                <el-table
                    ref="table"
                    :data="table.data"
                    :summary-method="getSummaries"
                    border
                    highlight-current-row
                    show-summary
                    size="mini"
                    style="width: 100%"
                    @sort-change="onSortChange">
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
                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination
                    :current-page="page.currentPage"
                    :hide-on-single-page="hideOnSinglePage"
                    :page-size="page.pageSize"
                    :page-sizes="[3,20, 50, 100]"
                    :total="page.total"
                    background
                    layout="total, sizes, prev, pager, next, jumper"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange">
                </el-pagination>
            </template>
        </nodes-master-page>
        <dialog-column
            v-bind="columnShowHide"
            @close="onColumnShowHide">
        </dialog-column>
    </el-form>
</template>

<script>


import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import NodesAsnBillState from "@/components/wms/select/NodesAsnBillState";
import NodesInStorageType from "@/components/wms/select/NodesInStorageType";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import {listMixin} from "@/mixins/list";
import {page} from "@/api/wms/basics/supplier"

export default {
    name: "supplier",
    components: {
        DialogColumn,
        NodesSearchInput,
        NodesInStorageType,
        NodesAsnBillState,
        NodesMasterPage,
        NodesDateRange
    },
    mixins: [listMixin],
    data() {
        return {
            masterConfig: {
                showExpandBtn: true
            },
            searchForm: {
                code: '',
                name: '',
                skuCode: '',
                // asnState: [10, 30, 20],
                // inStorageType: [20],
                createDateRange: ['2022-01-01', '2022-03-01'],
                updateDateRange: ['', '']
            },
            table: {
                columnList: [
                    {
                        prop: 'code',
                        label: '供应商编码',
                        // width: 300,
                        sortable: 'custom'
                    },
                    {
                        prop: 'name',
                        label: '供应商名称'
                    },
                    {
                        prop: 'simpleName',
                        label: '供应商简称'
                    },
                    {
                        prop: 'woId',
                        label: '货主'
                    },
                    {
                        prop: 'status',
                        label: '启用'
                    },
                    {
                        prop: 'remark',
                        label: '备注'
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
                        label: '更新时间'
                    },
                ]
            },
            page: {
                total: 0,
                pageSize: 3,
                currentPage: 1,
                ascs: "", //正序字段集合
                descs: "", //倒序字段集合
            },
        }
    },
    created() {
        
    },
    methods:{
         getTableData() {
            // API调用:post(this.searchFrom)
            page(this.page.currentPage,this.page.pageSize,this.searchForm)
            .then((res)=>{
                let pageObj = res.data.data;
                this.table.data = pageObj.records;
                this.page.total = pageObj.total;
            }).catch(()=>{

            })
        },
       onRefresh() {
            this.getTableData();
        },
        onSubmit() {
            this.getTableData();
        },
        onReset(){

        },
        onRemove(){
            console.log('批量删除了');
        },
        getSummaries(param) {
            const {columns, data} = param;
            const sums = [];
            columns.forEach((column, index) => {
                if (index < 2) {
                    sums[index] = '';
                }
                if (index === 2) {
                    sums[index] = '合计';
                    return;
                }
                const values = data.map(item => Number(item[column.property]));
                if (!values.every(value => isNaN(value))) {
                    sums[index] = values.reduce((prev, curr) => {
                        const value = Number(curr);
                        if (!isNaN(value)) {
                            return prev + curr;
                        } else {
                            return prev;
                        }
                    }, 0);
                    sums[index] += ' 元';
                } else {
                    sums[index] = '';
                }
            });

            return sums;
        },
    }
}
</script>
