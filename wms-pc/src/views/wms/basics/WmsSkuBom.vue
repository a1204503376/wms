import fileDownload from "js-file-download";
<template>
    <el-form ref="searchForm"
             v-model="form"
             :inline="true"
             label-position="right"
             label-width="60"
             size="mini">
        <nodes-master-page :configure="masterConfig"  :permission="permissionObj" v-on="form.events">
            <template v-slot:searchFrom>
                <el-form-item label="组合物品编码">
                    <nodes-sku v-model="form.params.joinSkuCode"></nodes-sku>
                </el-form-item>
                <el-form-item label="物品编码">
                    <nodes-sku v-model="form.params.skuCode"></nodes-sku>
                </el-form-item>
            </template>
            <template v-slot:expandSearch>
                <el-row type="flex">
                    <el-col :span="24">
                        <el-form-item label="货主">
                            <nodes-owner v-model="form.params.woId" ></nodes-owner>
                        </el-form-item>
                        <el-form-item label="更新时间">
                            <nodes-date-range v-model="form.params.updateTimeDateRange"></nodes-date-range>
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
                    <el-button circle icon="el-icon-download" size="mini" @click="excelCarrier"></el-button>
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
    } from "@/api/wms/basics/WmsSkuBom.js";

    export default {
        name: "carrier",
        components: {
            NodesSearchInput,
            NodesMasterPage,
            NodesDateRange,
            NodesOwner,
            NodesSku
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
                    code: '',
                    name: '',
                    simpleName: '',
                },
                form: {
                    params: {
                        joinSkuCode:'',
                        skuCode:'',
                        woId:'',
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
                    search: this.vaildData(this.permission.supplier_search, false),
                    add: this.vaildData(this.permission.supplier_add, false),
                    delete: this.vaildData(this.permission.supplier_delete, false)
                }
            }

        },
        methods: {
            hideOnSinglePage() {
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
                    fileDownload(res.data, "物品分类.xlsx");
                });
            },
            getTableData() {
                var that = this;
                that.params = this.form.params
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
