<template>
    <div id="logSoPick">
        <nodes-master-page v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex" class="search-elRow">
                    <el-col :span="6">
                        <el-form-item label="发货单编码" label-width="90px">
                            <el-input
                                v-model.trim="form.params.soBillNo" :clearable="true"
                                class="search-input"
                                placeholder="请输入发货单编码">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="单据类型" label-width="90px">
                            <nodes-bill-type
                                v-model="form.params.billTypeCdList"
                                :multiple="true"
                                class="search-input"
                                io-type="O">
                            </nodes-bill-type>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="物品编码" label-width="90px">
                            <nodes-sku
                                v-model="form.params.skuIdList"
                                class="search-input">
                            </nodes-sku>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="箱号" label-width="90px">
                            <el-input
                                v-model.trim="form.params.boxCode"
                                :clearable="true"
                                class="search-input"
                                placeholder="请输入箱号">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="LPN" label-width="90px">
                            <el-input
                                v-model.trim="form.params.lpnCode"
                                :clearable="true" class="search-input"
                                placeholder="请输入LPN">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="序列号" label-width="90px">
                            <el-input
                                v-model.trim="form.params.snCode"
                                :clearable="true" class="search-input"
                                placeholder="请输入序列号">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="拣货人" label-width="90px">
                            <el-input
                                v-model.trim="form.params.createUser"
                                :clearable="true" class="search-input"
                                placeholder="请输入拣货人">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="拣货时间" label-width="90px">
                            <nodes-date-range v-model="form.params.createTimeDateRange">
                            </nodes-date-range>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-slot:batchBtn>
                <el-button v-if="permissionObj.createReceiveBill"
                           size="mini" type="primary"
                           @click="createReceiveBill">创建收货单
                </el-button>
                <el-button v-if="permissionObj.cancelOutstock"
                           size="mini" type="primary"
                           @click="cancelOutstock">撤销拣货
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
                          :cell-style="cellStyle"
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
                            v-if="!column.hide "
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column"
                            min-width="150">
                            <template v-if="column.prop === 'snCode' || column.prop === 'soBillNo'"
                                      v-slot="scope">
                                <el-link
                                    v-if="column.prop === 'snCode'"
                                    :underline="false"
                                    type="primary"
                                    @click="openDialog(scope.row.snCode)">
                                    {{ scope.row.snCode }}
                                </el-link>
                                <el-link
                                    v-if="column.prop === 'soBillNo'"
                                    :underline="false"
                                    target="_blank"
                                    type="primary"
                                    @click="onDetail(scope.row.soBillId)">{{ scope.row.soBillNo }}
                                </el-link>
                            </template>
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
        <template>
            <el-dialog
                :append-to-body="true"
                :visible.sync="snCodeDialog"
                title="序列号列表"
                width="250px">
                <el-table
                    :border="true"
                    :data="snCodeList"
                    max-height="500">
                    <el-table-column align="center" fixed type="index">
                        <template slot="header">
                            #
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="序列号" prop="snCode" sortable width="200">
                        <template v-slot="scope">
                            {{ scope.row }}
                        </template>
                    </el-table-column>
                </el-table>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="snCodeDialog = false">关 闭</el-button>
                </div>
            </el-dialog>
        </template>
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
import {cancelOutstock, exportExcel, getPage} from "@/api/wms/outstock/logSoPick"
import fileDownload from "js-file-download";
import {ExcelExport} from 'pikaz-excel-js'
import {nowDateFormat} from "@/util/date";
import func from "@/util/func";
import NodesSku from "@/components/wms/select/NodesSkuByQuery";
import NodesBillType from "@/components/wms/select/NodesBillType";

export default {
    name: "logSoPick",
    components: {
        NodesBillType,
        NodesSku,
        DialogColumn,
        NodesSearchInput,
        NodesMasterPage,
        NodesDateRange,
        ExcelExport,
    },
    mixins: [listMixin],
    data() {
        return {
            snCodeDialog: false,
            snCodeList: [],
            form: {
                params: {
                    soBillNo: '',
                    skuIdList: [],
                    billTypeCdList: [],
                    boxCode: '',
                    lpnCode: '',
                    snCode: '',
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
                        label: "发货单类型",
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
                        prop: "pickRealQty",
                        label: "拣货量",
                        sortable: "custom"
                    },
                    {
                        prop: "wsuName",
                        label: "计量单位",
                        sortable: "custom"
                    },
                    {
                        prop: "boxCode",
                        label: "箱码",
                        sortable: "custom"
                    },
                    {
                        prop: "lpnCode",
                        label: "LPN",
                        sortable: "custom"
                    },
                    {
                        prop: "snCode",
                        label: "序列号",
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
                        prop: "createUserName",
                        label: "拣货人",
                        sortable: "custom"
                    },
                    {
                        prop: "createTime",
                        label: "拣货时间",
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
                cancelOutstock: this.vaildData(this.permission.logSoPick_cancelOutstock, false),
                createReceiveBill: this.vaildData(this.permission.logSoPick_createReceiveBill, false),
            }
        }
    },
    created() {
        this.getTableData();
    },
    methods: {
        openDialog(snCode) {
            this.snCodeList = snCode.split(',')
            this.snCodeDialog = true;
        },
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
                boxCode: '',
                lpnCode: '',
                snCode: '',
                createUser: '',
                createTimeDateRange: ['', ''],
            }
        },
        exportData() {
            this.loading = true;
            exportExcel(this.form.params)
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, `发货记录${nowDateFormat("yyyyMMddhhmmss")}.xlsx`);
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        onExportLocalData() {
            this.exportCurrentDataToExcel("发货记录", "发货记录")
        },
        cancelOutstock() {
            let rows = this.$refs.table.selection;
            if (func.isEmpty(rows)) {
                this.$message.warning("至少选择一条记录撤销");
                return;
            }
            let lsopIdList = rows.map(row => row.lsopId);
            for (const i in lsopIdList) {
                if (lsopIdList[i] < 0) {
                    this.$message.error("撤销失败，选择的记录中不允许有已撤销的记录")
                    return;
                }
            }
            this.$confirm("确定撤销选中的记录?", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                cancelOutstock(lsopIdList).then((res) => {
                    this.$message.success(res.data.msg);
                    this.getTableData();
                })
            });
        },
        createReceiveBill() {
            let rows = this.$refs.table.selection;
            if (func.isEmpty(rows)) {
                this.$message.warning("至少选择一条记录创建");
                return;
            }
            this.$router.push({
                name: '创建收货单',
                params: {
                    logSoPicks: JSON.stringify(rows)
                }
            })
        },
        cellStyle({row, column}) {
            if (row.pickRealQty < 0 && column.property === 'pickRealQty') {
                return "background-color: #FF6666"
            }
        },
        onDetail(soBillId) {
            console.log(soBillId);
            this.$router.push({
                name: '发货单详情',
                params: {
                    soBillId: soBillId
                }
            })
        },
    },
};
</script>
