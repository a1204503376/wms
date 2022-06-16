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
                <el-col :span="24">
                <el-form-item label="操作人员账号">
                    <el-input v-model="form.params.userAccount" class="d-input"></el-input>
                </el-form-item>
                <el-form-item label="操作人员真实名称">
                    <el-input v-model="form.params.userRealName" class="d-input"></el-input>
                </el-form-item>
                <el-form-item label="单据编码">
                    <el-input v-model="form.params.billNo" class="d-input"></el-input>
                </el-form-item>
                <el-form-item label="操作类型">
                    <nodes-audit-log-type-state v-model="form.params.type" :multiple="true">
                    </nodes-audit-log-type-state>
                </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="创建日期">
                        <nodes-date-range v-model="form.params.createTimeDateRange"
                                          value-fomat="yyyy-MM-dd"></nodes-date-range>
                    </el-form-item>
                    <el-form-item label="修改日期">
                        <nodes-date-range v-model="form.params.updateTimeDateRange"></nodes-date-range>
                    </el-form-item>
                </el-col>
            </template>
            <template v-slot:batchBtn>
                <el-button v-if="permissionObj.add" icon="el-icon-plus" size="mini" type="primary" @click="onAdd">新增</el-button>
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
                    <el-button circle icon="el-icon-download" size="mini" @click="exportActionLists"></el-button>
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
    import NodesAuditLogTypeState from "@/components/wms/select/NodesAuditLogTypeState"
    import NodesDateRange from "@/components/wms/general/NodesDateRange";
    import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
    import DialogColumn from "@/components/element-ui/crud/dialog-column";
    import fileDownload from "js-file-download";
    import {listMixin} from "@/mixins/list";
    import {ExcelExport} from 'pikaz-excel-js';
    // eslint-disable-next-line no-unused-vars
    import {getLogActionLists,exportActionLists} from "@/api/wms/log/serviceLog.js";
    import fileUpload from "@/components/nodes/fileUpload";
    export default {
        name: "carrier",
        components: {
            DialogColumn,
            NodesSearchInput,
            NodesMasterPage,
            NodesDateRange,
            ExcelExport,
            fileUpload,
            NodesAuditLogTypeState
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
                    userAccount: '',
                    userRealName: '',
                    type: [],
                    billId: '',
                    billNo: '',
                    log: '',
                    createUser: '',
                    updateUser: '',
                },
                form: {
                    params: {
                        userAccount: '',
                        userRealName: '',
                        // type: '',
                        billId: '',
                        billNo: '',
                        log: '',
                        createUser: '',
                        updateUser: '',
                        createTimeDateRange: ['', ''],//创建时间开始 创建时间结束
                        updateTimeDateRange: ['', ''],//更新时间开始 更新时间结束
                    }
                },
                table: {
                    columnList: [
                        {
                            prop: 'userAccount',
                            label: '操作人员账号'
                        },
                        {
                            prop: 'userRealName',
                            label: '操作人员真实名称'
                        },
                        {
                            prop: '操作类型',
                            label: 'type'
                        },
                        {
                            prop: 'billId',
                            label: '单据ID'
                        },
                        {
                            prop: 'billNo',
                            label: '单据编码'
                        },
                        {
                            prop: 'log',
                            label: '操作内容'
                        },
                        {
                            prop:'createTime',
                            label:'创建时间'
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
                fileUpload: {
                    visible: false,
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
                    search: this.vaildData(this.permission.supplier_search, false),
                    add: this.vaildData(this.permission.supplier_add, false),
                    delete: this.vaildData(this.permission.supplier_delete, false),
                    import: this.vaildData(this.permission.supplier_import, false)
                }
            }

        },
        methods: {
            onExportLocalData() {
                this.exportCurrentDataToExcel("承运商表", "承运商表");
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
            exportActionLists() {
                exportActionLists(this.form.params).then((res) => {
                    fileDownload(res.data, "业务日志.xlsx");
                });
            },
            getTableData() {
                var that = this;
                that.params = that.form.params
                getLogActionLists(that.params, that.page).then((res) => {
                    this.page.total = res.data.data.total;
                    this.page.currentPage = res.data.data.pages;
                    this.page.current = res.data.data.current;
                    this.page.size = res.data.data.size;
                    this.table.data = res.data.data.records;
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
