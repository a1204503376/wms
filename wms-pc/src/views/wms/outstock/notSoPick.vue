<template>
    <div id="notSoPick">
        <nodes-master-page :permission="permissionObj" v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex" class="search-elRow">
                    <el-col :span="6">
                        <el-form-item label="发货单编码" label-width="90px">
                            <el-input v-model.trim="form.params.soBillNo" :clearable="true" class="search-input"
                                      placeholder="请输入发货单编码">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="物品编码" label-width="90px">
                            <nodes-sku v-model="form.params.skuIdList" :clearable="true"
                                       class="search-input">
                            </nodes-sku>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="单据类型" label-width="90px">
                            <nodes-bill-type
                                v-model="form.params.billTypeCdList"
                                :clearable="true"
                                :multiple="true"
                                class="search-input"
                                io-type="O">
                            </nodes-bill-type>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="上游编码" label-width="90px">
                            <el-input v-model.trim="form.params.orderNo" :clearable="true" class="search-input"
                                      placeholder="请输入上游编码">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="创建人" label-width="90px">
                            <el-input v-model.trim="form.params.createUser" :clearable="true"
                                      placeholder="请输入创建人">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="创建时间" label-width="90px">
                            <nodes-date-range
                                v-model="form.params.createTimeDateRange">
                            </nodes-date-range>
                        </el-form-item>
                    </el-col>
                </el-row>
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
                          :height="table.height"
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
                    <el-table-column
                        fixed
                        width="50"
                        type="index">
                        <template slot="header">
                            #
                        </template>
                    </el-table-column>
                    <template v-for="(column, index) in table.columnList">
                        <el-table-column
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column"
                            min-width="150">
                        </el-table-column>
                    </template>
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
        <div v-if="columnShowHide.visible">
            <dialog-column
                v-bind="columnShowHide"
                @close="onColumnShowHide">
            </dialog-column>
        </div>
    </div>
</template>

<script>
import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import {listMixin} from "@/mixins/list";
import NodesLocation from "@/components/wms/select/NodesLocation";
import NodesOwner from "@/components/wms/select/NodesOwner";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import {exportExcel, getPage} from "@/api/wms/outstock/notSoPick"
import fileDownload from "js-file-download";
import {ExcelExport} from 'pikaz-excel-js'
import {nowDateFormat} from "@/util/date";
import NodesBillType from "@/components/wms/select/NodesBillType";
import NodesSku from "@/components/wms/select/NodesSkuByQuery";

export default {
    name: "notSoPick",
    components: {
        NodesSku,
        NodesBillType,
        NodesWarehouse,
        NodesOwner,
        NodesLocation,
        DialogColumn,
        NodesSearchInput,
        NodesMasterPage,
        NodesDateRange,
        ExcelExport,
    },
    mixins: [listMixin],
    data() {
        return {
            form: {
                params: {
                    soBillNo: '',
                    skuIdList: [],
                    billTypeCdList: [],
                    orderNo: '',
                    createUser: '',
                    createTimeDateRange: ['', ''],
                },
            },
            table: {
                columnList: [
                    {
                        prop: "soBillNo",
                        label: "发货单编码",
                        sortable: "custom",
                    },
                    {
                        prop: "billTypeName",
                        label: "单据类型",
                        sortable: "custom"
                    },
                    {
                        prop: "orderNo",
                        label: "上游编码",
                        sortable: "custom"
                    },
                    {
                        prop: "soLineNo",
                        label: "行号",
                        sortable: "custom"
                    },
                    {
                        prop: "skuCode",
                        label: "物品编码",
                        sortable: "custom"
                    },
                    {
                        prop: "skuName",
                        label: "物品名称",
                        sortable: "custom"
                    },
                    {
                        prop: "planQty",
                        label: "计划数量",
                        sortable: "custom"
                    },
                    {
                        prop: "scanQty",
                        label: "实收数量",
                        sortable: "custom"
                    },
                    {
                        prop: "surplusQty",
                        label: "剩余数量",
                        sortable: "custom"
                    },
                    {
                        prop: "umName",
                        label: "计量单位",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot1",
                        label: "生产批次",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot2",
                        label: "规格型号",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot3",
                        label: "发货日期",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot4",
                        label: "专用客户",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot5",
                        label: "钢背批次",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot6",
                        label: "摩擦块批次",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot7",
                        label: "产品标识代码",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot8",
                        label: "适用速度等级",
                        sortable: "custom"
                    },
                    {
                        prop: "createUser",
                        label: "创建人",
                        sortable: "custom"
                    },
                    {
                        prop: "createTime",
                        label: "创建时间",
                        sortable: "custom"
                    }
                ],
            },
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
                search: this.vaildData(this.permission.notSoPick_search, false),
                repeal: this.vaildData(this.permission.notSoPick_repeal, false),
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
                    this.handleRefreshTable();
                })
        },
        refreshTable() {
            this.getTableData();
        },
        onReset() {
            this.form.params = {
                soBillNo: '',
                skuIdList: [],
                billTypeCdList: [],
                orderNo: '',
                createUser: '',
                createTimeDateRange: ['', ''],
            }
        },
        exportData() {
            this.loading = true;
            exportExcel(this.form.params)
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, `未发货明细${nowDateFormat("yyyyMMddhhmmss")}.xlsx`);
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        onExportLocalData() {
            this.exportCurrentDataToExcel("未发货明细", "未发货明细")
        },
    },
};
</script>
