import fileDownload from "js-file-download";
<template>
    <el-form ref="searchForm"
             v-model="form"
             :inline="true"
             label-position="right"
             label-width="60"
             size="mini">
        <nodes-master-page :configure="masterConfig" v-on="form.events">
            <template v-slot:searchFrom>
                <el-form-item label="承运商编码">
                    <el-input v-model="form.params.code" class="d-input"></el-input>
                </el-form-item>
                <el-form-item label="承运商名称">
                    <el-input v-model="form.params.name" class="d-input"></el-input>
                </el-form-item>
                <el-form-item label="承运商简称">
                    <el-input v-model="form.params.simpleName" class="d-input"></el-input>
                </el-form-item>
            </template>
            <template v-slot:expandSearch>
                <el-row type="flex">
                    <el-col :span="24">
                        <el-form-item label="创建日期">
                            <nodes-date-range v-model="form.params.createTimeDateRange"
                                              value-fomat="yyyy-MM-dd"></nodes-date-range>
                        </el-form-item>
                        <el-form-item label="修改日期">
                            <nodes-date-range v-model="form.params.updateTimeDateRange"></nodes-date-range>
                        </el-form-item>
                    </el-col>
                </el-row>

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
                    <el-button circle icon="el-icon-download" size="mini" @click="excelCarrier"></el-button>
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
                        label="是否启用" width="170">
                        <template slot-scope="scope">
                            <el-switch
                                v-model="scope.row.status"
                                active-color="#409EFF"
                                inactive-color="#ff4949"
                                active-text="启用"
                                inactive-text="未启用"
                                :active-value="1"
                                :inactive-value="-1"
                                @change="switchChange(scope.row)">
                            </el-switch>
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
    import fileDownload from "js-file-download";
    import {listMixin} from "@/mixins/list";
    // eslint-disable-next-line no-unused-vars
    import {
        getCarriersPage,
        // eslint-disable-next-line no-unused-vars
        newCarrier,
        // eslint-disable-next-line no-unused-vars
        deleteCarrier,
        excelCarrier,
        updateCarrier,
    } from "@/api/wms/basics/Carrier.js";

    export default {
        name: "carrier",
        components: {
            NodesSearchInput,
            NodesMasterPage,
            NodesDateRange
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
                            label: '承运商编码'
                        },
                        {
                            prop: 'name',
                            label: '承运商名称'
                        },
                        {
                            prop: 'simpleName',
                            label: '承运商简称'
                        },
                        {
                            prop: 'ownerName',
                            label: '货主编码'
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
            }

        },
        methods: {
            switchChange(row) {
                let parpams = {
                    id: row.id,
                    status: row.status
                }
                updateCarrier(parpams).then((res) => {
                    if (res.data.code == 200) {
                        this.$message({
                            type: "success",
                            message: res.data.msg
                        });
                        this.getTableData();
                    } else {
                        this.$message({
                            type: "error",
                            message: res.data.msg
                        });
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
                getCarriersPage(that.params, this.page).then((res) => {
                    this.page.total = res.data.data.total;
                    this.page.currentPage = res.data.data.pages;
                    this.page.current = res.data.data.current;
                    this.page.size = res.data.data.size;
                    this.table.data = res.data.data.records;
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
