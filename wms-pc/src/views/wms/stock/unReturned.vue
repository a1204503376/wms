<template>
    <div id="unReturned">
        <nodes-master-page v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex" class="search-elRow">
                    <el-col :span="6">
                        <el-form-item label="借出人姓名" label-width="90px">
                            <el-input
                                v-model.trim="form.params.lendReturnName"
                                :clearable="true"
                                class="search-input"
                                placeholder="请输入借出人姓名">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="物品" label-width="90px">
                            <nodes-sku
                                v-model="form.params.skuIdList"
                                :multiple="true"
                                class="search-input"
                                placeholder="请选择">
                            </nodes-sku>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="创建日期" label-width="90px">
                            <nodes-date-range v-model="form.params.createTimeDateRange"></nodes-date-range>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="生产批次" label-width="90px">
                            <el-input
                                v-model.trim="form.params.skuLot1"
                                :clearable="true"
                                class="search-input"
                                placeholder="请输入生产批次">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="规格型号" label-width="90px">
                            <el-input
                                v-model.trim="form.params.skuLot2"
                                :clearable="true"
                                class="search-input"
                                placeholder="请输入规格型号">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-slot:batchBtn>
                <el-button
                    v-if="permissionObj.createReturnBill"
                    icon="el-icon-plus"
                    size="mini" type="primary"
                    @click="createReturnBill">创建归还单
                </el-button>
            </template>
            <template v-slot:tableTool>
                <el-tooltip :enterable="false" class="item" content="刷新" effect="dark" placement="top">
                    <el-button circle icon="el-icon-refresh" size="mini" @click="onRefresh"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="显隐" effect="dark" placement="top">
                    <el-button circle icon="el-icon-s-operation" size="mini" @click="onColumnShowHide"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="服务端导出" effect="dark" placement="top">
                    <el-button circle icon="el-icon-download" size="mini" @click="onExportData"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="本地导出" effect="dark" placement="top">
                    <excel-export :filename="exportExcelName" :sheet="exportExcelSheet"
                                  style="display: inline-block;margin-left: 10px">
                        <el-button circle icon="el-icon-bottom" size="mini" @click="onExportLocalData"/>
                    </excel-export>
                </el-tooltip>
            </template>
            <template v-slot:table>
                <el-table
                    ref="table"
                    :data="table.data"
                    :height="table.height" border highlight-current-row
                    size="mini" @sort-change="onSortChange">
                    <el-table-column
                        fixed
                        type="selection"
                        width="50">
                    </el-table-column>
                    <template v-for="(column, index) in table.columnList">
                        <el-table-column
                            v-if="!column.hide"
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column" width="150">
                        </el-table-column>
                    </template>
                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination
                    :current-page="page.current" :page-size="page.size" :page-sizes="[20, 50, 100]"
                    :total="page.total" background layout="total, sizes, prev, pager, next, jumper"
                    v-bind="page"
                    @size-change="handleSizeChange" @current-change="handleCurrentChange">
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
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import {listMixin} from "@/mixins/list";
import fileDownload from "js-file-download";
import {ExcelExport} from 'pikaz-excel-js'
import {exportData, page} from "@/api/wms/stock/unReturned"
import {nowDateFormat} from "@/util/date";
import NodesSku from "@/components/wms/select/NodesSkuByQuery";
import func from "@/util/func";

export default {
    name: "unReturned",
    components: {
        NodesSku,
        DialogColumn,
        NodesMasterPage,
        NodesDateRange,
        ExcelExport
    },
    mixins: [listMixin],
    data() {
        return {
            form: {
                params: {
                    lendReturnName: '',
                    skuIdList: [],
                    createTimeDateRange: [],
                    skuLot1: null,
                    skuLot2: null,
                }
            },
            table: {
                columnList: [
                    {
                        prop: 'lendReturnName',
                        label: '借出人姓名',
                        sortable: 'custom',
                    },
                    {
                        prop: 'skuCode',
                        label: '物品编码',
                        sortable: 'custom',
                    },
                    {
                        prop: 'skuName',
                        label: '物品名称',
                        sortable: 'custom',
                    },
                    {
                        prop: 'lendQty',
                        label: '借出数量',
                        sortable: 'custom',
                    },
                    {
                        prop: 'returnQty',
                        label: '归还数量',
                        sortable: 'custom',
                    },
                    {
                        prop: 'noReturnQty',
                        label: '未归还数量',
                        sortable: 'custom',
                    },
                    {
                        prop: 'wsuCode',
                        label: '计量单位编码',
                        sortable: 'custom',
                    },
                    {
                        prop: 'wsuName',
                        label: '计量单位名称',
                        sortable: 'custom',
                    },
                    {
                        prop: 'snCode',
                        label: '序列号',
                        sortable: 'custom',
                    },
                ]
            },
        }
    },
    created() {
        this.getTableData();
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
                createReturnBill: this.vaildData(this.permission.unReturned_createReturnBill, false),
            }
        }
    },
    methods: {
        getTableData() {
            page(this.page, this.form.params)
                .then((res) => {
                    let pageObj = res.data.data;
                    this.table.data = pageObj.records;
                    this.page.total = pageObj.total;
                    this.handleRefreshTable();
                })
        },
        refreshTable() {
            this.getTableData();
        },
        onExportData() {
            this.loading = true;
            exportData(this.form.params)
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, `未归还列表${nowDateFormat("yyyyMMddhhmmss")}.xlsx`);
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        onReset() {
            this.form.params = {
                lendReturnName: '',
                skuIdList: [],
                createTimeDateRange: [],
                skuLot1: null,
                skuLot2: null,
            }
        },
        onExportLocalData() {
            this.exportCurrentDataToExcel("未归还列表", "未归还列表");
        },
        createReturnBill() {
            let rows = this.$refs.table.selection;
            if (func.isEmpty(rows)) {
                this.$message.warning("至少选择一条记录创建");
                return;
            }
            this.$router.push({
                name: '创建归还单',
                params: {
                    unReturnedData: JSON.stringify(rows)
                }
            })
        }
    }
}
</script>
