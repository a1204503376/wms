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
                        <el-form-item label="类型编码" label-width="90px">
                            <el-input v-model="form.params.code" class="d-input"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="容器类别" label-width="90px">
                            <nodes-lpn-type-state :multiple="false" v-model="form.params.lpnType">
                            </nodes-lpn-type-state>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="创建日期" label-width="90px">
                            <nodes-date-range v-model="form.params.createTimeDateRange"
                                              value-fomat="yyyy-MM-dd" style="width: 200px"></nodes-date-range>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-slot:expandSearch>
                <el-row type="flex">

                    <el-col :span="6">
                        <el-form-item label="修改日期" label-width="90px">
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
                <el-tooltip :enterable="false" class="item" content="显隐" effect="dark" placement="top">
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
                    <el-button circle icon="el-icon-download" size="mini" @click="excelLpnType"></el-button>
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
                <dialog-column v-bind="columnShowHide" @close="onColumnShowHide">
                </dialog-column>
            </template>
        </nodes-master-page>
    </el-form>
</template>

<script>


    import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
    import NodesDateRange from "@/components/wms/general/NodesDateRange";
    import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
    // eslint-disable-next-line no-unused-vars
    import NodesLpnTypeState from "@/components/wms/select/NodesLpnTypeState";
    import DialogColumn from "@/components/element-ui/crud/dialog-column";
    import fileDownload from "js-file-download";
    import {listMixin} from "@/mixins/list";
    // eslint-disable-next-line no-unused-vars
    import {
        // eslint-disable-next-line no-unused-vars
        getLpnTypePage,
        // eslint-disable-next-line no-unused-vars
        newLpnType,
        // eslint-disable-next-line no-unused-vars
        deleteLpnType,
        // eslint-disable-next-line no-unused-vars
        excelLpnType
    } from "@/api/wms/basics/LpnType.js";

    export default {
        name: "LpnType",
        components: {
            DialogColumn,
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
                masterConfig: {
                    showExpandBtn: true
                },
                params: {},
                excelParams: {
                    code: '',//承运商编码
                },
                form: {
                    params: {
                        code: '',//承运商编码
                        lpnType: '',
                        createTimeDateRange: ['', ''],//创建时间开始 创建时间结束
                        updateTimeDateRange: ['', ''],//更新时间开始 更新时间结束
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
                    codes.push(ele.code);
                });
                return codes.join(",");
            },
            permissionObj() {
                return {
                    search: this.vaildData(this.permission.lpnType_search, false),
                    add: this.vaildData(this.permission.lpnType_add, false),
                    delete: this.vaildData(this.permission.lpnType_delete, false)
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
            hideOnSinglePage() {
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
                    fileDownload(res.data, "容器管理信息.xlsx");
                });
            },
            getTableData() {
                var that = this;
                that.params = this.form.params;
                getLpnTypePage(that.params, this.page).then((res) => {
                    this.page.total = res.data.data.total;
                    this.page.currentPage = res.data.data.pages;
                    this.page.current = res.data.data.current;
                    this.page.size = res.data.data.size;
                    this.table.data = res.data.data.records;
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
        }
    }
</script>
