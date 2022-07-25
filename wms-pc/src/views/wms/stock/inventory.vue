import fileDownload from "js-file-download";
<template>
    <div id='inventory'>
        <nodes-master-page :permission="permissionObj" v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex">
                    <el-col :span="8">
                        <el-form-item label="物品编码" label-width="90px">
                            <nodes-sku
                                v-model="form.params.skuIds">
                            </nodes-sku>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="生产批次" label-width="90px">
                            <el-input placeholder="请输入生产批次" v-model.trim="form.params.skuLot1"
                                      :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="库位" label-width="90px">
                            <el-input placeholder="请输入库位" v-model.trim="form.params.locCode"
                                      :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>

                </el-row>
            </template>
            <template v-slot:expandSearch>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="库存状态" label-width="90px">
                            <NodesStockStatus v-model="form.params.stockStatusList" multiple="true"></NodesStockStatus>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="库区" label-width="90px">
                            <el-input placeholder="请输入库区" v-model.trim="form.params.zoneCode"
                                      :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="箱号" label-width="90px">
                            <el-input placeholder="请输入箱号" v-model.trim="form.params.boxCode"
                                      :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="lpn" label-width="90px">
                            <el-input placeholder="请输入lpn" v-model.trim="form.params.lpnCode"
                                      :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="6">
                        <el-form-item label="规格型号" label-width="90px">
                            <el-input placeholder="请输入规格型号" v-model.trim="form.params.skuLot2"
                                      :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="收货时间" label-width="90px">
                            <nodes-date-range v-model="form.params.receiveTimeDateRange" style="width: 200px">
                            </nodes-date-range>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="专用客户" label-width="90px">
                            <el-input placeholder="请输入专用客户" v-model.trim="form.params.skuLot4"
                                      :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="钢背批次" label-width="90px">
                            <el-input placeholder="请输入钢背批次" v-model.trim="form.params.skuLot5"
                                      :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="6">
                        <el-form-item label="摩擦块批次" label-width="90px">
                            <el-input placeholder="请输入摩擦块批次" v-model.trim="form.params.skuLot6"
                                      :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="产品标识" label-width="90px">
                            <el-input placeholder="请输入产品标识" v-model.trim="form.params.skuLot7"
                                      :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="CRCC" label-width="90px">
                            <el-input placeholder="请输入CRCC" v-model.trim="form.params.skuLot8"
                                      :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="库房" label-width="90px">
                            <nodes-warehouse v-model="form.params.whIdList" :multiple="true"></nodes-warehouse>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="6">
                        <el-form-item label="货主" label-width="90px">
                            <nodes-owner v-model="form.params.woId"></nodes-owner>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="任务号" label-width="90px">
                            <el-input placeholder="请输入任务号" v-model.trim="form.params.taskId"
                                      :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="入库时间" label-width="90px">
                            <nodes-date-range v-model="form.params.lastInTimeDateRange" style="width: 200px">
                            </nodes-date-range>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="出库时间" label-width="90px">
                            <nodes-date-range v-model="form.params.lastOutTimeDateRange" style="width: 200px">
                            </nodes-date-range>
                        </el-form-item>
                    </el-col>

                </el-row>
            </template>
            <template v-slot:batchBtn>
                <el-button icon="el-icon-plus" size="mini" type="primary" @click="showByBox">按箱显示
                </el-button>
                <el-button icon="el-icon-upload2" plain size="mini"
                           @click="onUpload">导入
                </el-button>
                <file-upload
                    :visible="fileUpload.visible"
                    file-name="库存"
                    template-url="/api/wms/stock/export-template"
                    @callback="callbackFileUpload"
                ></file-upload>
            </template>
            <template v-slot:tableTool>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="刷新"
                    effect="dark"
                    placement="top"
                >
                    <el-button
                        circle
                        icon="el-icon-refresh"
                        size="mini"
                        @click="onRefresh"
                    ></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="显隐"
                    effect="dark"
                    placement="top"
                >
                    <el-button
                        circle
                        icon="el-icon-s-operation"
                        size="mini"
                        @click="onColumnShowHide"
                    ></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="服务端导出"
                    effect="dark"
                    placement="top"
                >
                    <el-button circle icon="el-icon-download" size="mini" @click="exportData"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="本地导出" effect="dark" placement="top">
                    <excel-export :filename="exportExcelName" :sheet="exportExcelSheet"
                                  style="display: inline-block;margin-left: 10px">
                        <el-button circle icon="el-icon-bottom" size="mini" @click="onExportLocalData">
                        </el-button>
                    </excel-export>
                </el-tooltip>
            </template>
            <template v-slot:table>
                <el-table
                    ref="table"
                    :data="table.data"
                    show-summary
                    :summary-method="getSummaries"
                    border
                    highlight-current-row
                    row-key="id"
                    size="mini"
                    style="width: 100%"
                    @sort-change="onSortChange"
                >
                    <el-table-column fixed type="selection" width="50"></el-table-column>
                    <el-table-column fixed type="index">
                        <template slot="header"> #</template>
                    </el-table-column>
                    <template v-for="(column, index) in table.columnList">
                        <el-table-column
                            v-if="!column.hide"
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column"
                            width="130"
                        >
                        </el-table-column>
                    </template>

                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination
                    :page-sizes="pageSize"
                    background
                    layout="total, sizes, prev, pager, next, jumper"
                    v-bind="page"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                >
                </el-pagination>
            </template>
        </nodes-master-page>
        <dialog-column v-bind="columnShowHide" @close="onColumnShowHide">
        </dialog-column>
    </div>
</template>

<script>

import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import NodesAsnBillState from "@/components/wms/select/NodesAsnBillState";
import NodesInStoreMode from "@/components/wms/select/NodesInStoreMode";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import {listMixin} from "@/mixins/list";
import {exportFile, importFile, page} from "@/api/wms/stock/stock";
import fileDownload from "js-file-download";
import {ExcelExport} from 'pikaz-excel-js';
import fileUpload from "@/components/nodes/fileUpload";
import NodesSku from "@/components/wms/select/NodesSkuByQuery";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import NodesOwner from "@/components/wms/select/NodesOwner";
import NodesStockStatus from "@/components/wms/select/NodesStockStatus";


export default {
    name: "customer",
    components: {
        DialogColumn,
        NodesOwner,
        NodesSearchInput,
        NodesStockStatus,
        NodesInStoreMode,
        NodesAsnBillState,
        NodesMasterPage,
        NodesDateRange,
        NodesWarehouse,
        NodesSku,
        ExcelExport,
        fileUpload,
    },
    mixins: [listMixin],
    data() {
        return {
            woId: "",
            form: {
                params: {
                    skuIds: [],
                    skuLot1: "",
                    locCode: "",
                    stockStatusList: [],
                    zoneCode: "",
                    boxCode: "",
                    lpnCode: "",
                    skuLot2: "",
                    skuLot4: "",
                    skuLot5: "",
                    skuLot6: "",
                    skuLot7: "",
                    skuLot8: "",
                    whIdList: [],
                    woId: "",
                    receiveTimeDateRange: "",
                    lastInTimeDateRange: "",
                    lastOutTimeDateRange: "",
                    taskId: ""
                }
            },
            deleteCustomerRequest: {
                ids: [],
            },
            pageSize: [20, 50, 100],
            table: {
                columnList: [
                    {
                        prop: "skuCode",
                        label: "物品编码",
                        sortable: "custom",
                    },
                    {
                        prop: "stockStatus",
                        label: "库存状态",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot1",
                        label: "生产批次",
                        sortable: "custom"
                    },
                    {
                        prop: "stockBalance",
                        label: "库存余额",
                        sortable: "custom"
                    },
                    {
                        prop: "stockEnable",
                        label: "库存可用",
                        sortable: "custom"
                    },
                    {
                        prop: "occupyQty",
                        label: "库存占用",
                        sortable: "custom"
                    },

                    {
                        prop: "wsuCode",
                        label: "计量单位",
                        sortable: "custom"
                    },
                    {
                        prop: "locCode",
                        label: "库位编码",
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
                        prop: "skuLot2",
                        label: "规格型号",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot3",
                        label: "收货日期",
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
                        label: "是否CRCC验证",
                        sortable: "custom"
                    },
                    {
                        prop: "whCode",
                        label: "库房",
                        sortable: "custom"
                    },
                    {
                        prop: "ownerCode",
                        label: "货主编码",
                        sortable: "custom"
                    },
                    {
                        prop: "ownerName",
                        label: "货主名称",
                        sortable: "custom"
                    },
                    {
                        prop: "createUserName",
                        label: "收货人",
                        sortable: "custom"
                    },
                    {
                        prop: "taskId",
                        label: "任务号",
                        sortable: "custom"
                    },
                    {
                        prop: "lastInTime",
                        label: "入库时间",
                        sortable: "custom"
                    },
                    {
                        prop: "lastOutTime",
                        label: "出库时间",
                        sortable: "custom"
                    },
                ],
            },
            fileUpload: {
                visible: false,
            },
        };
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
    methods: {
        getTableData() {
            page(this.page, this.form.params)
                .then((res) => {
                    let pageObj = res.data.data;
                    this.table.data = pageObj.records;
                    this.page.total = pageObj.total;

                    let currentSku = this.pageSize.indexOf(pageObj.total)
                    if (currentSku === -1) {
                        this.pageSize.push(pageObj.total)
                    }

                });
        },
        refreshTable() {
            this.getTableData();
        },
        exportData() {
            this.loading = true;
            exportFile(this.form.params)
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, "库存列表.xlsx");
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },

        onSubmit() {
            this.getTableData();

        },
        onExportLocalData() {
            this.exportCurrentDataToExcel("库存表", "库存表");
        },
        onReset() {
            this.form.params = {
                createTimeDateRange: "",
            }
            this.onChange(null);
            console.log('重置表单');
        },
        onChange(val) {
            if (val == null) {
                this.dateRange = [];

            }
            this.$emit('dateRangeChange', val);
        },
        showByBox() {
            this.$router.push({
                name: '新增客户',
                params: {
                    id: '0'
                }
            });
        },
        getSummaries(param) {
            const {columns, data} = param;
            const sums = [];
            columns.forEach((column, index) => {
                if (index === 0) {
                    sums[index] = '合计';
                    return;
                }
                if (index === 5 || index === 6 || index === 7) {
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

                    } else {
                        sums[index] = '';
                    }
                }
            });

            return sums;
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
    },
};
</script>
