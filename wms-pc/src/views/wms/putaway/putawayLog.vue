<template>
    <div id="taskDetail">
        <nodes-master-page v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex" class="search-elRow">
                    <el-col :span="6">
                        <el-form-item label="箱码" label-width="90px">
                            <el-input :clearable="true" class="search-input" placeholder="请输入箱码"
                                      v-model.trim="form.params.boxCode">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="容器编码" label-width="90px">
                            <el-input :clearable="true" class="search-input" placeholder="请输入容器编码"
                                      v-model.trim="form.params.lpnCode">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="物品编码" label-width="90px">
                            <nodes-sku-by-query v-model="form.params.skuIdList" class="search-input">
                            </nodes-sku-by-query>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="目标库位" label-width="90px">
                            <el-input :clearable="true" class="search-input" placeholder="请输入目标库位"
                                      v-model.trim="form.params.targetLocCode">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="生产批次" label-width="90px">
                            <el-input :clearable="true" class="search-input" placeholder="请输入生产批次" v-model.trim="form.params.skuLot1">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="规格型号" label-width="90px">
                            <el-input :clearable="true" class="search-input" placeholder="请输入规格型号" v-model.trim="form.params.skuLot2">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-slot:tableTool>
                <el-tooltip :enterable="false" class="item" content="刷新" effect="dark" placement="top">
                    <el-button @click="onRefresh" circle icon="el-icon-refresh" size="mini"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="显隐" effect="dark" placement="top">
                    <el-button @click="onColumnShowHide" circle icon="el-icon-s-operation" size="mini"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="全量导出" effect="dark" placement="top">
                    <el-button @click="exportData" circle icon="el-icon-download" size="mini"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="当前页导出" effect="dark" placement="top">
                    <excel-export :filename="exportExcelName" :sheet="exportExcelSheet"
                                  style="display: inline-block;margin-left: 10px">
                        <el-button @click="onExportLocalData" circle icon="el-icon-bottom" size="mini">
                        </el-button>
                    </excel-export>
                </el-tooltip>
            </template>
            <template v-slot:table>
                <el-table :data="table.data" :height="table.height" @sort-change="onSortChange" border
                          highlight-current-row ref="table" size="mini" style="width: 100%">
                    <el-table-column fixed type="selection" width="50">
                    </el-table-column>
                    <el-table-column fixed type="index" width="50">
                        <template slot="header">
                            #
                        </template>
                    </el-table-column>
                    <template v-for="(column, index) in table.columnList">
                        <el-table-column :key="index" show-overflow-tooltip v-bind="column" v-if="!column.hide"
                                         width="150">
                        </el-table-column>
                    </template>
                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination :page-size="page.size" :page-sizes="[20, 50, 100]" :total="page.total"
                               @current-change="handleCurrentChange" @size-change="handleSizeChange" background
                               layout="total, sizes, prev, pager, next, jumper">
                </el-pagination>
            </template>
        </nodes-master-page>
        <div v-if="columnShowHide.visible">
            <dialog-column @close="onColumnShowHide" v-bind="columnShowHide">
            </dialog-column>
        </div>
    </div>
</template>

<script>
import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
import fileDownload from "js-file-download";
import {listMixin} from "@/mixins/list";
import {getPage, exportFile} from "@/api/wms/putaway/putaway";
import fileUpload from "@/components/nodes/fileUpload";
import {ExcelExport} from 'pikaz-excel-js';
import NodesSkuByQuery from "@/components/wms/select/NodesSkuByQuery";
import {nowDateFormat} from "@/util/date";
import DialogColumn from "@/components/element-ui/crud/dialog-column.vue";

export default {
    name: "putawayLog",
    components: {
        DialogColumn,
        NodesSkuByQuery,
        NodesSearchInput,
        NodesMasterPage,
        fileUpload,
        ExcelExport
    },
    mixins: [listMixin],
    data() {
        return {
            form: {
                params: {
                    boxCode: '',
                    lpnCode: '',
                    skuIdList: [],
                    targetLocCode: '',
                    skuLot1: '',
                    skuLot2: '',
                }
            },
            table: {
                columnList: [
                    {
                        prop: 'lpnCode',
                        label: '容器编码',
                        sortable: "custom",
                    },
                    {
                        prop: 'targetLocCode',
                        label: '目标库位编码',
                        sortable: "custom",
                    },
                    {
                        prop: 'userCode',
                        label: '用户编码',
                        sortable: "custom",
                    },
                    {
                        prop: 'userName',
                        label: '用户名称',
                        sortable: "custom",
                    },
                    {
                        prop: 'aplTime',
                        label: '执行时间',
                        sortable: "custom",
                    },
                    {
                        prop: 'boxCode',
                        label: '箱码',
                        sortable: "custom",
                    },
                    {
                        prop: 'skuCode',
                        label: '物品编码',
                        sortable: "custom",
                    },
                    {
                        prop: 'skuName',
                        label: '物品名称',
                        sortable: "custom",
                    },
                    {
                        prop: 'qty',
                        label: '数量',
                        sortable: "custom",
                    },
                    {
                        prop: 'skuLot1',
                        label: '生产批次',
                        sortable: "custom",
                    },
                    {
                        prop: 'skuLot2',
                        label: '型号',
                        sortable: "custom",
                    }
                ]
            },
        }
    },
    created() {
        this.getTableData();
    },
    methods: {
        exportData() {
            this.loading = true;
            exportFile(this.form.params)
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, `上架记录${nowDateFormat("yyyyMMddhhmmss")}.xlsx`);
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        onExportLocalData() {
            this.exportCurrentDataToExcel("上架记录", "上架记录");
        },
        getTableData() {
            getPage(this.page, this.form.params).then((res) => {
                let pageObj = res.data.data;
                this.table.data = pageObj.records;
                this.page.total = pageObj.total;
                this.handleRefreshTable();
            });
        },
    }
}
</script>
