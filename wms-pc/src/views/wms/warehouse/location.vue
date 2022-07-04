<template>
    <div id="location">
        <nodes-master-page :permission="permissionObj" v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex">
                    <el-col :span="8">
                        <el-form-item label="库位编码" label-width="90px">
                            <el-input
                                v-model.trim="form.params.locCode"
                                :clearable="true"
                                placeholder="请输入库位编码"
                            ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="所属库房" label-width="90px">
                            <nodes-warehouse
                                v-model="form.params.whIdList"
                                :multiple="true"
                            ></nodes-warehouse>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="所属库区" label-width="90px">
                            <nodes-zone
                                v-model="form.params.zoneIdList"
                                :multiple="true"
                                skuIdList
                            ></nodes-zone>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-slot:expandSearch>
                <el-row>
                    <el-col :span="6">
                        <el-form-item label="库位类型" label-width="90px">
                            <nodes-dictionary
                                v-model="form.params.locTypeList"
                                :multiple="true"
                                code="loc_type"
                                skuIdList
                            ></nodes-dictionary>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="库位种类" label-width="90px">
                            <nodes-dictionary
                                v-model="form.params.locCategoryList"
                                :multiple="true"
                                code="loc_category"
                                skuIdList
                            ></nodes-dictionary>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="库位处理" label-width="90px">
                            <nodes-dictionary
                                v-model="form.params.locHandlingList"
                                :multiple="true"
                                code="loc_handling"
                                skuIdList
                            ></nodes-dictionary>
                        </el-form-item>
                    </el-col>
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="使用状态" label-width="90px">
                                <nodes-dictionary
                                    v-model="form.params.locFlagList"
                                    :multiple="true"
                                    code="loc_flag"
                                    skuIdList
                                ></nodes-dictionary>
                            </el-form-item>
                        </el-col>
                    </el-row>
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
                    file-name="库位"
                    template-url="/api/wms/warehouse/location/export-template"
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
                        <el-table-column v-if="!column.hide && column.prop === 'locCode'" :key="index"
                                         show-overflow-tooltip
                                         v-bind="column" width="140">
                            <template v-slot="scope">
                                <el-link
                                    :underline="false"
                                    target="_blank"
                                    type="primary"
                                    @click="onView(scope.row)">{{ scope.row.locCode }}
                                </el-link>
                            </template>
                        </el-table-column>
                        <el-table-column
                            v-if="!column.hide && column.prop !== 'locCode' && column.prop !== 'status'"
                            :key="index"
                            min-width="140"
                            show-overflow-tooltip
                            v-bind="column"
                        ></el-table-column>
                        <el-table-column
                            v-if="!column.hide && column.prop === 'status'"
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column"
                        >
                            <template v-slot="{row}">
                                <el-tag :type="row.status === '是' ? 'success' : 'danger'"
                                        disable-transitions>{{ row.status }}
                                </el-tag>
                            </template>
                        </el-table-column>
                    </template>
                    <el-table-column align="center" fixed="right" label="操作" width="100">
                        <template v-slot="scope">
                            <el-button size="small" type="text" @click="onEdit(scope.row)">编辑</el-button>
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
import {exportFile, getPage, importFile, remove} from "@/api/wms/basics/location";
import fileDownload from "js-file-download";
import {ExcelExport} from 'pikaz-excel-js'
import fileUpload from "@/components/nodes/fileUpload";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import NodesZone from "@/components/wms/select/NodesZone";
import NodesDictionary from "@/components/wms/select/NodesDictionary";
import {nowDateFormat} from "@/util/date";

export default {
    name: "location",
    components: {
        NodesDictionary,
        NodesZone,
        NodesWarehouse,
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
                    locCode: '',
                    whIdList: [],
                    zoneIdList: [],
                    locTypeList: [],
                    locCategoryList: [],
                    locHandlingList: [],
                    locFlagList: []
                },
            },
            table: {
                columnList: [
                    {
                        prop: "locCode",
                        label: "库位编码",
                        sortable: "custom",
                    },
                    {
                        prop: "whName",
                        label: "所属库房",
                        sortable: "custom",
                    },
                    {
                        prop: "zoneName",
                        label: "所属库区",
                        sortable: "custom",
                    },
                    {
                        prop: "locType",
                        label: "库位类型",
                        sortable: "custom",
                    },
                    {
                        prop: "locCategory",
                        label: "库位种类",
                        sortable: "custom",
                    },
                    {
                        prop: "locHandling",
                        label: "库位处理",
                        sortable: "custom",
                    },
                    {
                        prop: "logicAllocation",
                        label: "路线顺序",
                        sortable: "custom",
                    },
                    {
                        prop: "locFlag",
                        label: "使用状态",
                        sortable: "custom",
                    },
                    {
                        prop: "abc",
                        label: "ABC分类",
                        sortable: "custom",
                    },
                    {
                        prop: "locColumn",
                        label: "货架列",
                        sortable: "custom",
                    },
                    {
                        prop: "locBank",
                        label: "货架排",
                        sortable: "custom",
                    },
                    {
                        prop: "putOrder",
                        label: "上架顺序",
                        sortable: "custom",
                    },
                    {
                        prop: "lpnTypeCode",
                        label: "适用的容器类型",
                        sortable: "custom",
                    },
                    {
                        prop: "status",
                        width: "80",
                        align: "center",
                        sortable: "custom",
                        label: "启用",
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
                search: this.vaildData(this.permission.location_search, false),
                add: this.vaildData(this.permission.location_add, false),
                delete: this.vaildData(this.permission.location_delete, false),
                import: this.vaildData(this.permission.location_import, false)
            }
        }
    },
    created() {
        this.getTableData();
    },
    methods: {
        getTableData() {
            getPage(this.page, this.form.params)
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
                locCode: '',
                whIdList: [],
                zoneIdList: [],
                locTypeList: [],
                locCategoryList: [],
                locHandlingList: [],
                locFlagList: []
            }
        },
        onRemove() {
            let rows = this.$refs.table.selection;
            if (rows.length <= 0) {
                this.$message.warning("警告，至少选择一条记录");
                return;
            }
            for (const i in rows) {
                let locCode = rows[i].locCode;
                let locTypeConstant = ["PICKTO", "INTRANSIT", "QC", "UNKNOWN", "STAGE", "PACK"];
                if (rows[i].locType === "虚拟" && locTypeConstant.includes(locCode.substring(locCode.lastIndexOf("-") + 1))) {
                    this.$message.error(`删除失败，库位[编码:${locCode}]是系统虚拟库位无法删除`);
                    return;
                }
            }
            this.$confirm("此操作将删除, 是否删除?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
                .then(() => {
                    let removeObj = {
                        idList: []
                    };
                    rows.forEach((item) => {
                        removeObj.idList.push(item.locId);
                    });
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
                    fileDownload(res.data, `库位${nowDateFormat("yyyyMMddhhmmss")}.xlsx`);
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        onExportLocalData() {
            this.exportCurrentDataToExcel("库位", "库位")
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
        onAdd() {
            this.$router.push({
                name: '新增库位',
                params: {
                    locId: '0'
                }
            });
        },
        onEdit(row) {
            this.$router.push({
                name: '编辑库位',
                params: {
                    locId: row.locId
                }
            })
        },
        onView(row) {
            this.$router.push({
                name: '库位详情',
                params: {
                    locId: row.locId
                }
            })
        },
    },
};
</script>
