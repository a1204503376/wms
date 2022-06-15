<template>
    <div id="supplier">
        <nodes-master-page :permission="permissionObj" v-on="form.events">
            <template v-slot:searchFrom>
                <el-form-item label="供应商编码">
                    <el-input v-model.trim="form.params.code" :clearable="true"></el-input>
                </el-form-item>
                <el-form-item label="供应商名称">
                    <el-input v-model.trim="form.params.name" :clearable="true"></el-input>
                </el-form-item>
            </template>
            <template v-slot:expandSearch>
                <el-row type="flex">
                    <el-col :span="24">
                        <el-form-item label="创建日期">
                            <nodes-date-range v-model="form.params.createTimeDateRange"></nodes-date-range>
                        </el-form-item>
                        <el-form-item label="更新日期">
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
                    file-name="供应商"
                    template-url="/api/wms/supplier/export-template"
                    @callback="callbackFileUpload"
                ></file-upload>
            </template>
            <template v-slot:tableTool>
                <el-tooltip :enterable="false" class="item" content="刷新" effect="dark" placement="top">
                    <el-button circle icon="el-icon-refresh" size="mini" @click="onRefresh"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="显隐" effect="dark" placement="top">
                    <el-button circle icon="el-icon-s-operation" size="mini" @click="onColumnShowHide"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="服务端导出" effect="dark" placement="top">
                    <el-button circle icon="el-icon-download" size="mini" @click="exportData"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="本地导出" effect="dark" placement="top">
                    <excel-export :filename="exportExcelName" :sheet="exportExcelSheet"
                                  style="display: inline-block;margin-left: 10px">
                        <el-button circle icon="el-icon-bottom" size="mini" @click="onExportLocalData"/>
                    </excel-export>
                </el-tooltip>
            </template>
            <template v-slot:table>
                <el-table ref="table"
                          :data="table.data"
                          border
                          highlight-current-row
                          size="mini"
                          style="width: 100%"
                          @sort-change="onSortChange">
                    <el-table-column
                        fixed
                        type="selection"
                        width="50">
                    </el-table-column>
                    <template v-for="(column, index) in table.columnList">
                        <el-table-column
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column">
                        </el-table-column>
                    </template>
                    <el-table-column label="启用"
                                     prop="status"
                                     width="100">
                        <template v-slot="{row}">
                            <el-tag :type="row.status === 1 ? 'success' : 'danger'"
                                    disable-transitions>{{
                                    row.status ===
                                    1 ? '是' : '否'
                                }}
                            </el-tag>
                        </template>
                    </el-table-column>
                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination :current-page="page.current" :page-size="page.size" :page-sizes="[20, 50, 100]"
                               :total="page.total" background layout="total, sizes, prev, pager, next, jumper"
                               v-bind="page"
                               @size-change="handleSizeChange" @current-change="handleCurrentChange">
                </el-pagination>
            </template>
        </nodes-master-page>
        <dialog-column v-bind="columnShowHide" @close="onColumnShowHide">
        </dialog-column>
    </div>
</template>

<script>
import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import {listMixin} from "@/mixins/list";
import {exportFile, importFile, page, remove} from "@/api/wms/basics/supplier";
import fileDownload from "js-file-download";
import {ExcelExport} from 'pikaz-excel-js'
import fileUpload from "@/components/nodes/fileUpload";


export default {
    name: "supplier",
    components: {
        DialogColumn,
        NodesSearchInput,
        NodesMasterPage,
        NodesDateRange,
        ExcelExport,
        fileUpload,
    },
    mixins: [listMixin],
    data() {
        return {
            form: {
                params: {
                    code: "",
                    name: "",
                    createTimeDateRange: ["", ""],
                    updateTimeDateRange: ["", ""],
                },
            },
            table: {
                columnList: [
                    {
                        prop: "code",
                        label: "供应商编码",
                        sortable: "custom",
                    },
                    {
                        prop: "name",
                        label: "供应商名称",
                    },
                    {
                        prop: "simpleName",
                        label: "供应商简称",
                    },
                    {
                        prop: "ownerName",
                        label: "货主",
                    },
                    {
                        prop: "remark",
                        label: "备注",
                    },
                    {
                        prop: "createTime",
                        width: 130,
                        label: "创建时间",
                    },
                    {
                        prop: "createUser",
                        label: "创建人",
                    },
                    {
                        prop: "updateTime",
                        width: 130,
                        label: "更新时间",
                    },
                ],
            },
            fileUpload: {
                visible: false,
            }
        };
    },
    watch: {
        $route(to) {
            if (to.query && to.query.isRefresh === 'true') {
                this.refreshTable();
            }
        }
    },
    computed: {
        permissionObj() {
            return {
                search: this.vaildData(this.permission.supplier_search, false),
                add: this.vaildData(this.permission.supplier_add, false),
                delete: this.vaildData(this.permission.supplier_delete, false),
                import: this.vaildData(this.permission.supplier_import, false)
            }
        }
    },
    created() {
        this.getTableData();
    },
    methods: {
        getTableData() {
            page(this.page, this.form.params)
                .then((res) => {
                    let pageObj = res.data.data;
                    this.table.data = pageObj.records;
                    this.page.total = pageObj.total;
                })
        },
        refreshTable() {
            this.getTableData();
        },
        onReset() {
            this.form.params = {
                name: '',
                code: '',
                createTimeDateRange: ["", ""],
                updateTimeDateRange: ["", ""]
            }
        },
        onRemove() {
            let rows = this.$refs.table.selection;
            if (rows.length <= 0) {
                this.$message.warning("警告，至少选择一条记录");
                return;
            }
            this.$confirm("此操作将删除, 是否删除?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
                .then(() => {
                    let removeObj = {
                        idList: rows.map(item=>item.id)
                    };
                    remove(removeObj)
                        .then((res) => {
                            this.$message({
                                type: "success",
                                message: res.data.msg,
                            });
                            this.getTableData();
                        })
                })
        },
        exportData() {
            this.loading = true;
            exportFile(this.form.params)
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, "供应商.xlsx");
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        onExportLocalData() {
            this.exportCurrentDataToExcel("供应商", "供应商")
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
        filterTag(value, row) {
            return row.status === value;
        },
        onAdd() {
            this.$router.push({
                name: '新增供应商',
                params: {
                    id: '0'
                }
            });
        },
    },
};
</script>
