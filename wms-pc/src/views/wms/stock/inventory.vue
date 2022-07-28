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
                            <el-input v-model.trim="form.params.skuLot1" :clearable="true"
                                      placeholder="请输入生产批次"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="库位" label-width="90px">
                            <nodes-location v-model="form.params.locIdList" :multiple="true"></nodes-location>
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
                            <nodes-zone v-model="form.params.zoneIdList" :multiple="true"></nodes-zone>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="箱号" label-width="90px">
                            <el-input v-model.trim="form.params.boxCode" :clearable="true"
                                      placeholder="请输入箱号"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="lpn" label-width="90px">
                            <el-input v-model.trim="form.params.lpnCode" :clearable="true"
                                      placeholder="请输入lpn"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="6">
                        <el-form-item label="规格型号" label-width="90px">
                            <el-input v-model.trim="form.params.skuLot2" :clearable="true"
                                      placeholder="请输入规格型号"></el-input>
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
                            <el-input v-model.trim="form.params.skuLot4" :clearable="true"
                                      placeholder="请输入专用客户"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="钢背批次" label-width="90px">
                            <el-input v-model.trim="form.params.skuLot5" :clearable="true"
                                      placeholder="请输入钢背批次"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="6">
                        <el-form-item label="摩擦块批次" label-width="90px">
                            <el-input v-model.trim="form.params.skuLot6" :clearable="true"
                                      placeholder="请输入摩擦块批次"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="库房" label-width="90px">
                            <nodes-warehouse v-model="form.params.whIdList" :multiple="true"></nodes-warehouse>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="货主" label-width="90px">
                            <nodes-owner v-model="form.params.woId"></nodes-owner>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="入库时间" label-width="90px">
                            <nodes-date-range v-model="form.params.lastInTimeDateRange" style="width: 200px">
                            </nodes-date-range>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
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
                <el-button icon="el-icon-plus" size="mini" type="primary" @click="showByBox">按LPN显示
                </el-button>
                <el-button size="mini" type="primary" @click="moveByPiece">
                    按件移动
                </el-button>
                <el-button size="mini" type="primary" @click="moveByBox">
                    按箱移动
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
                    :summary-method="getSummaries"
                    border
                    highlight-current-row
                    row-key="id"
                    show-summary
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
        <dialog-column v-bind="columnShowHide" @close="onColumnShowHide"></dialog-column>
        <template>
            <el-dialog
                v-dialogDrag="true"
                :close-on-click-modal="false"
                :custom-class="dialog.isFullScreen ? 'maxDialog' : '' "
                :show-close="true"
                :visible.sync="dialog.showDialog"
                append-to-body
                @close="onClose">
                <span slot="title" class="dialog-footer">
                    <div class="icon">
                        <span>{{ dialog.title }}</span>
                        <el-button class="button_enlarge" type="text">
                            <i :class="dialog.isFullScreen ? 'icon-tuichuquanping' : 'icon-quanping'"
                               @click="enlarge">
                            </i>
                        </el-button>
                    </div>
                </span>
                <div style="margin-top: 10px;">
                    <el-table
                        ref="multipleTable"
                        v-loading="dialog.loading"
                        :data="dialog.gridData"
                        :header-cell-style="{'background-color': '#fafafa'}"
                        :span-method="dialogGridDataSpanMethod"
                        border
                        element-loading-spinner="el-icon-loading"
                        element-loading-text="数据正在加载中"
                        highlight-current-row
                        overflow="auto"
                        style="font-size: 14px"
                        width="100%">
                        <el-table-column label="箱码" property="boxCode" show-overflow-tooltip></el-table-column>
                        <el-table-column label="物品编码" property="skuCode" show-overflow-tooltip></el-table-column>
                        <el-table-column label="物品名称" property="skuName" show-overflow-tooltip></el-table-column>
                        <el-table-column label="批次号" property="lotNumber" show-overflow-tooltip></el-table-column>
                        <el-table-column label="可用库存" property="stockEnable" show-overflow-tooltip></el-table-column>
                        <el-table-column label="LPN" property="lpnCode" show-overflow-tooltip></el-table-column>
                        <el-table-column label="库区" property="zoneCode" show-overflow-tooltip></el-table-column>
                        <el-table-column label="库位编码" property="locCode" show-overflow-tooltip></el-table-column>
                    </el-table>
                    <el-table
                        :data="dialog.childrenData"
                        :height="
                            dialog.isMoveByBox ?  dialog.isFullScreen ? 435 : 100
                                                : dialog.isFullScreen ? 435 : 350"
                        border
                        size="medium">
                        <el-table-column prop="id" width="50">
                            <template slot="header">
                                <el-button
                                    circle
                                    icon="el-icon-plus"
                                    style="padding: 4px"
                                    type="primary"
                                    @click="rowAdd">
                                </el-button>
                            </template>
                        </el-table-column>
                        <el-table-column
                            v-if="!dialog.isMoveByBox"
                            label="转移数量"
                            width="160px">
                            <template v-slot="scope">
                                <el-input-number
                                    v-model="scope.row['qty']"
                                    :max="dialog.children.max" :min="dialog.children.min"
                                    size="medium"
                                    style="width: 100%" @change="qtyChange(scope.row)">
                                </el-input-number>
                            </template>
                        </el-table-column>
                        <el-table-column
                            v-if="!dialog.isMoveByBox"
                            label="序列号">
                            <template v-slot="scope">
                                <nodes-serial
                                    v-model="scope.row['serials']"
                                    :collapse="false"
                                    size="medium"
                                    :stock-id="dialog.gridData[0].stockId"
                                    style="width: 100%">
                                </nodes-serial>
                            </template>
                        </el-table-column>
                        <el-table-column
                            v-if="!dialog.isMoveByBox"
                            label="库位"
                            width="180">
                            <template v-slot="scope">
                                <nodes-location
                                    size="medium"
                                    v-model="scope.row['locId']">
                                </nodes-location>
                            </template>
                        </el-table-column>
                        <el-table-column
                            v-if="dialog.isMoveByBox"
                            label="目标箱码">
                            <template v-slot="scope">
                                <el-input
                                    v-model="scope.row['targetBoxCode']"
                                    size="medium">
                                </el-input>
                            </template>
                        </el-table-column>
                        <el-table-column
                            v-if="dialog.isMoveByBox"
                            label="目标库位">
                            <template v-slot="scope">
                                <nodes-location
                                    v-model="scope.row['targetLocId']"
                                    size="medium"
                                    style="width: 100%;">
                                </nodes-location>
                            </template>
                        </el-table-column>
                        <el-table-column
                            v-if="dialog.isMoveByBox"
                            label="目标LPN">
                            <template v-slot="scope">
                                <el-input
                                    v-model="scope.row['targetLpnCode']"
                                    size="medium">
                                </el-input>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" width="80">
                            <template v-slot="scope">
                                <el-button
                                    size="mini"
                                    type="danger"
                                    @click="dialogHandleRemove(scope.$index, scope.row)">删除
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
                <div slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="onSubmit">确定</el-button>
                    <el-button style="margin-left: 15px;" @click="dialog.showDialog = false">关 闭</el-button>
                </div>
            </el-dialog>
        </template>
    </div>
</template>
<script>

import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import {listMixin} from "@/mixins/list";
import {exportFile, getStockDataByBoxCode, getStockDataByStockId, move, moveByBox, importFile, page} from "@/api/wms/stock/stock";
import fileDownload from "js-file-download";
import {ExcelExport} from 'pikaz-excel-js';
import fileUpload from "@/components/nodes/fileUpload";
import NodesSku from "@/components/wms/select/NodesSkuByQuery";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import NodesOwner from "@/components/wms/select/NodesOwner";
import NodesStockStatus from "@/components/wms/select/NodesStockStatus";
import func from "@/util/func";
import NodesLocation from "@/components/wms/select/NodesLocation";
import "../../../../public/cdn/iconfont/avue/iconfont.css"
import NodesSerial from "@/components/wms/select/NodesSerial";
import NodesZone from "@/components/wms/select/NodesZone";


export default {
    name: "customer",
    components: {
        NodesZone,
        NodesSerial,
        NodesLocation,
        DialogColumn,
        NodesOwner,
        NodesSearchInput,
        NodesStockStatus,
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
                    locIdList: [],
                    skuLot1: "190904",
                    locCode: "",
                    stockStatusList: [],
                    zoneIdList: [],
                    boxCode: "",
                    lpnCode: "",
                    skuLot2: "",
                    skuLot4: "",
                    skuLot5: "",
                    skuLot6: "",
                    whIdList: [],
                    woId: "",
                    receiveTimeDateRange: "",
                    lastInTimeDateRange: "",
                    lastOutTimeDateRange: "",
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
            dialog: {
                isFullScreen: false,
                showDialog: false,
                loading: false,
                gridData: [],
                children: {
                    actualQty: 0,
                    actualQtyBak: 0,
                    min: 1,
                    max: 0,
                },
                isMoveByBox: false, //是否按箱移动，默认为false
                childrenData: [],
                serials: [], //序列号
                //合并对象
                merge: {
                    mergedArray: [],
                    mergedIndex: 0
                }
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
                name: '按箱显示',
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
        moveByPiece() {
            this.dialog.isMoveByBox = false;
            this.getStockDataByMove(this.dialog.isMoveByBox)
        },
        moveByBox() {
            this.dialog.isMoveByBox = true;
            this.getStockDataByMove(this.dialog.isMoveByBox)
        },
        getStockDataByMove(isMoveByBox) {
            let rows = this.$refs.table.selection;
            if (rows.length === 0) {
                this.$message.warning("请选择一条记录进行移动")
                return;
            }
            if (!isMoveByBox && rows.length > 1) {
                this.$message.warning("只能选择一条记录进行移动")
                return;
            }
            if (isMoveByBox) {
                for (const rowsKey in rows) {
                    if (func.isEmpty(rows[rowsKey].boxCode)) {
                        this.$message.warning(`库存[物品编码${rows[rowsKey].skuCode}]的箱码为空`);
                        return;
                    }
                }
            }
            this.dialog.showDialog = true;
            this.renderData(isMoveByBox, rows);
        },
        // 渲染dialog
        async renderData(isMoveByBox, rows) {
            if (isMoveByBox) {
                this.dialog.title = '按箱移动';
                let boxCodeList = [...new Set(rows.map(item => item.boxCode))]; //ES6展开语法将Set对象转换成数组（去重）
                await getStockDataByBoxCode(boxCodeList).then((res) => {
                    this.dialog.gridData = res.data.data;
                })
                this.merge(this.dialog.gridData);
            } else {
                this.dialog.title = '按件移动';
                await getStockDataByStockId(rows[0].stockId).then((res) => {
                    this.dialog.gridData.push(res.data.data);
                })
                this.dialog.children.actualQty = this.dialog.gridData[0].stockEnable;
            }
        },
        // ==========================已下是 dialog 中的方法=================================
        enlarge() {
            this.dialog.isFullScreen = !this.dialog.isFullScreen;
        },
        dialogGridDataSpanMethod({row, column, rowIndex, columnIndex}) {
            if (this.dialog.merge.mergedArray.length > 0) {
                if (column.label === '箱码') {
                    const _row = this.dialog.merge.mergedArray[rowIndex];
                    const _col = _row > 0 ? 1 : 0;
                    return {
                        rowspan: _row,
                        colspan: _col,
                    };
                }
            }
        },
        merge(data) {
            if (data.length > 0) {
                let mergedArray = this.dialog.merge.mergedArray;
                let mergedIndex = this.dialog.merge.mergedIndex;
                for (let i = 0; i < data.length; i++) {
                    if (i === 0) {
                        mergedArray.push(1);
                        mergedIndex = 0;
                    } else {
                        if (data[i].boxCode === data[i - 1].boxCode) {
                            mergedArray[mergedIndex] += 1;
                            mergedArray.push(0);
                        } else {
                            mergedArray.push(1);
                            mergedIndex = i;
                        }
                    }
                }
            }
        },
        rowAdd() {
            if (this.dialog.isMoveByBox ) {
                if(this.dialog.childrenData.length !== 0){
                    return;
                }
                this.dialog.childrenData.push({id: 1, targetBoxCode: '', targetLocId: [], targetLpnCode: ''});
            } else {
                let id = this.dialog.childrenData.length + 1;
                if (this.dialog.serials.length === 0) {
                    this.dialog.children.min = 0;
                    this.dialog.children.max = this.dialog.gridData[0].stockEnable;
                } else {
                    this.dialog.children.min = 1;
                    this.dialog.children.max = 1;
                }
                if (this.dialog.serials.length > 0 && id > this.dialog.serials.length) {
                    this.$message.warning(`转移数量不能超过序列号的数量！`);
                    return;
                }
                if (this.dialog.serials.length > 0 && id > this.dialog.gridData[0].stockEnable) {
                    this.$message.warning(`转移数量不能超过可用数量！`);
                    return;
                }
                let temp = this.dialog.childrenData.filter(x => x.qty === 0);
                if (temp.length > 0) {
                    this.$message.warning(`请先编辑第${temp[0].id}行的数据！`);
                    return;
                }
                this.qtyChange();
                this.dialog.childrenData.push({id: id, qty: this.dialog.children.actualQty, serials: [], locId: ''});
            }
        },
        dialogHandleRemove(index) {
            this.dialog.childrenData.splice(index, 1);
            this.dialog.childrenData.forEach((x, index) => {
                x.id = index + 1;
            });
            this.qtyChange();
        },
        onClose() {
            this.dialog.showDialog = false;
            this.dialog.gridData = [];
            this.dialog.children = {
                actualQty: 0,
                actualQtyBak: 0,
                min: 1,
                max: 0,
            };
            this.dialog.childrenData = [];
        },
        qtyChange(row) {
            let temp = this.dialog.childrenData.map(function (x) {
                return parseFloat(x.qty);
            });
            if (this.dialog.children.actualQtyBak === 0) {
                this.dialog.children.actualQtyBak = this.dialog.children.actualQty;
            }
            let sum = temp.reduce((n, m) => n + m, 0);
            if (sum > this.dialog.children.actualQtyBak) {
                this.$message.warning("转移数量超出可用数量！");
                setTimeout(() => {
                    let temp = this.dialog.childrenData.filter(x => x.id !== row.id).map(function (x) {
                        return parseFloat(x.qty);
                    });
                    let sum = temp.reduce((n, m) => n + m, 0);
                    row['qty'] = this.dialog.children.actualQtyBak - sum;
                    this.actualQty = 0;
                }, 1);
            } else {
                if (this.dialog.children.actualQtyBak !== 0) {
                    this.dialog.children.actualQty = this.dialog.children.actualQtyBak;
                }
                if (sum === 0) {
                    this.dialog.children.actualQty = this.dialog.children.actualQtyBak;
                } else {
                    this.dialog.children.actualQty -= sum;
                }
            }
        },
        onSubmit() {
            let childrenData = this.dialog.childrenData;
            if(func.isEmpty(childrenData)){
                this.$message.warning("请填写移动信息");
                return;
            }
            if (this.dialog.isMoveByBox){
                if (func.isEmpty(childrenData[0].targetBoxCode)){
                    this.$message.warning("请填写目标箱码");
                    return;
                }
                if (func.isEmpty(childrenData[0].targetLocId)){
                    this.$message.warning("请选择目标库位编码");
                    return;
                }
                if (func.isEmpty(childrenData[0].targetBoxCode)){
                    this.$message.warning("请填写目标LPN");
                    return;
                }
                let moveByBoxObj = {
                    boxCodeList: [...new Set(this.dialog.gridData.map(item => item.boxCode))],
                    targetBoxCode : this.dialog.childrenData[0].targetBoxCode,
                    targetLocId : this.dialog.childrenData[0].targetLocId,
                    targetLpnCode : this.dialog.childrenData[0].targetLpnCode,
                }
                moveByBox(moveByBoxObj).then((res)=>{

                });
            }else {
                for (const i in childrenData) {
                    if (childrenData[i].serials.length !== childrenData[i].qty) {
                        this.$message.warning(`第${childrenData[i].id}行，转移数量不等于选中的序列号数量`);
                        return;
                    }
                }
                let serials = childrenData.map(item => item.serials);
                if (serials.length > 1) {
                    for (let i = 0; i < serials.length - 1; i++) {
                        for (let j = i + 1; j < serials.length; j++) {
                            let temp = serials[j];
                            for (let k = 0; k < temp.length; k++) {
                                if (serials[i].find(x => x === temp[k])) {
                                    this.$message.error(`第${i + 1}行和第${j + 1}存在重复的序列号`);
                                    return;
                                }
                            }
                        }
                    }
                }

                let moveObj = {
                    stockId: this.dialog.gridData[0].stockId,
                    stockMoveDataList: this.dialog.childrenData
                }
                move(moveObj).then((res)=>{

                })
            }
        },
    },
};
</script>

<style lang="scss" scoped>

.button_enlarge {
    color: #909399;
    float: right;
    line-height: 22px;
    margin-right: 22px;
    font-size: 16px;
}

/deep/ .maxDialog {
    margin-right: 19px;
    width: 87.5%;
    margin-top: 14vh !important;
    height: 79%;
}

//:class="dialog.isFullScreen ? 'children_maxTable': 'children_minTable'"
/deep/ .maxDialog .el-dialog__body {
    max-height: 82% !important;
}
</style>
