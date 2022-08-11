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
                            <NodesStockStatus v-model="form.params.stockStatusList" :multiple="true"></NodesStockStatus>
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
                        <el-form-item label="货架列" label-width="90px">
                            <el-input v-model="form.params.locColumn" placeholder="请输入货架列"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="6">
                        <el-form-item label="库区类型" label-width="90px">
                            <nodes-dictionary
                                v-model="form.params.zoneTypeList"
                                :clearable="true"
                                :multiple="true" code="zone_type">
                            </nodes-dictionary>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="是否序列号" label-width="90px">
                            <el-select v-model="form.params.hasSerial" :clearable="true">
                                <el-option
                                    v-for="item in [{label: '是',value: 1},{label: '否',value: 0}]"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                                </el-option>
                            </el-select>
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
                <el-button size="mini" type="primary" @click="moveByPiece">
                    按件移动
                </el-button>
                <el-button size="mini" type="primary" @click="moveByBox">
                    按箱移动
                </el-button>
                <el-button size="mini" type="primary" @click="freeze">
                    库存冻结
                </el-button>
                <el-button size="mini" type="primary" @click="thaw">
                    库存解冻
                </el-button>
                <el-button icon="el-icon-plus" size="mini" type="primary" @click="showByBox">按箱显示
                </el-button>
                <el-button icon="el-icon-plus" size="mini" type="primary" @click="showByLpn">按LPN显示
                </el-button>
                <el-button icon="el-icon-plus" size="mini" type="primary" @click="showBySerial">按序列号显示
                </el-button>
                <el-button size="mini" type="primary" @click="print">
                    箱贴打印
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
                            v-if="!column.hide && index !==1"
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column"
                            width="130">
                            <template v-if="column.prop === 'hasSerial'" v-slot="scope">
                                <el-link
                                    v-if="scope.row.hasSerial === 1"
                                    :underline="false"
                                    target="_blank"
                                    type="primary"
                                    @click="showHasSerialView(scope.row.stockId)">是
                                </el-link>
                                {{ scope.row.hasSerial !== 1 ? '否' : '' }}
                            </template>
                        </el-table-column>
                        <el-table-column
                            v-if="!column.hide && index===1"
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column"
                            width="130"
                        >
                            <template slot-scope="scope">
                                <el-tag
                                    v-if="scope.row.stockStatus === '系统冻结' || scope.row.stockStatus === '冻结'"
                                    type="danger"
                                >
                                    {{ scope.row.stockStatus }}
                                </el-tag>
                                <el-tag
                                    v-else
                                    type="success"
                                >
                                    {{ scope.row.stockStatus }}
                                </el-tag>

                            </template>


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
                :append-to-body="true"
                :close-on-click-modal="false"
                :custom-class="'maxDialog'"
                :show-close="true"
                :title="dialog.title"
                :visible.sync="dialog.showDialog"
                @close="onClose">
                <div style="margin-top: 10px;">
                    <el-table
                        ref="multipleTable"
                        v-loading="dialog.loading"
                        :data="dialog.gridData"
                        :header-cell-style="{'background-color': '#fafafa'}"
                        :height="dialog.isMoveByBox ? 390 : 'auto'"
                        :max-height="dialog.isMoveByBox ? 400 : 'auto'"
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
                        :height="dialog.isMoveByBox ? 120 : 435"
                        :max-height="dialog.isMoveByBox ? 120 : 420"
                        border
                        size="medium">
                        <el-table-column
                            v-if="!dialog.isMoveByBox" prop="id"
                            width="50">
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
                                    :stock-id="dialog.gridData[0].stockId"
                                    size="medium"
                                    style="width: 100%"
                                    v-on:getSerialDataSource="getSerialDataSource">
                                </nodes-serial>
                            </template>
                        </el-table-column>
                        <el-table-column
                            v-if="!dialog.isMoveByBox"
                            label="库位"
                            width="180">
                            <template v-slot="scope">
                                <nodes-location
                                    v-model="scope.row['locId']"
                                    :source-loc-code="dialog.gridData[0].locCode"
                                    size="medium">
                                </nodes-location>
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
                                    placeholder="请输入目标LPN"
                                    size="medium">
                                </el-input>
                            </template>
                        </el-table-column>
                        <el-table-column
                            v-if="!dialog.isMoveByBox"
                            label="操作"
                            width="80">
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
        <template>
            <el-dialog :visible.sync="form1.popShow" append-to-body title="选择类型" width="800px">
                <el-form :model="form1">
                    <el-form-item v-if="form1.thawShow" label="解冻类型" label-width="120px">
                        <el-select v-model="form1.stockType" placeholder="请选择解冻类型">
                            <el-option label="整批次解冻" value="byBatch"></el-option>
                            <el-option label="库位解冻" value="byLoc"></el-option>
                            <el-option label="按箱解冻" value="byBox"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item v-if="form1.freezeShow" label="冻结类型" label-width="120px">
                        <el-select v-model="form1.stockType" placeholder="请选择冻结类型">
                            <el-option label="整批次冻结" value="byBatch"></el-option>
                            <el-option label="库位冻结" value="byLoc"></el-option>
                            <el-option label="按箱冻结" value="byBox"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="备注" label-width="120px">
                        <el-input v-model="form1.remark" style="width: 600px" type="textarea"></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="cancel">取 消</el-button>
                    <el-button type="primary" @click="submit">确 定</el-button>
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
import {
    exportFile,
    getStockDataByBoxCode,
    getStockDataByStockId,
    importFile,
    move,
    moveByBox,
    page,
    stockFrozen,
    stockUnFrozen
} from "@/api/wms/stock/stock";
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
import NodesDictionary from "@/components/wms/select/NodesDictionary";

export default {
    name: "customer",
    components: {
        NodesDictionary,
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
            dialogFormVisible: false,
            woId: "",
            form: {
                params: {
                    skuIds: [],
                    locIdList: [],
                    skuLot1: "",
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
                    locColumn: '',
                    hasSerial: '',
                    zoneTypeList: [],
                    receiveTimeDateRange: "",
                    lastInTimeDateRange: "",
                    lastOutTimeDateRange: "",
                }
            },
            form1: {
                popShow: false,
                thawShow: false,
                freezeShow: false,
                stockType: "",
                remark: ""
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
                        prop: "hasSerial",
                        label: "是否序列号管理",
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
                showDialog: false,
                loading: false,
                title: '',
                gridData: [],
                children: {
                    actualQty: 0,
                    actualQtyBak: 0,
                    min: 1,
                    max: 0,
                },
                isMoveByBox: false, //是否按箱移动，默认为false
                childrenData: [],
                serials: [], //序列号组件中所有的序列号
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
        submit() {
            if (this.form1.stockType === '') {
                this.$message.warning("请选择类型")
                return;
            }
            let StockThawAndFrozenDto = {};
            let remark = this.form1.remark
            if (this.form1.stockType === 'byBatch') {
                let skuLot1List = this.$refs.table.selection.map((row) => {
                    return row.skuLot1;
                });
                let newArr = skuLot1List.filter(i => i && i.trim())
                if (newArr.length === 0) {
                    this.$message.warning("所选记录批次为空,请选择其他类型")
                    return;
                }
                StockThawAndFrozenDto = {skuLot1List, remark}
            } else if (this.form1.stockType === 'byLoc') {
                let locIdList = this.$refs.table.selection.map((row) => {
                    return row.locId;
                });
                StockThawAndFrozenDto = {locIdList, remark}
            } else if (this.form1.stockType === 'byBox') {
                let boxCodeList = this.$refs.table.selection.map((row) => {
                    return row.boxCode;
                });
                let newArr = boxCodeList.filter(i => i && i.trim())
                if (newArr.length === 0) {
                    this.$message.warning("所选记录箱码为空,请选择其他类型")
                    return;
                }
                StockThawAndFrozenDto = {boxCodeList, remark}
            }
            if (this.form1.freezeShow) {
                this.cancel()
                stockFrozen(StockThawAndFrozenDto).then((res) => {
                    this.getTableData()
                    this.$message.success(res.data.msg);
                })
            }
            if (this.form1.thawShow) {
                this.cancel()
                stockUnFrozen(StockThawAndFrozenDto).then((res) => {
                    this.getTableData()
                    this.$message.success(res.data.msg);
                })
            }

        },
        thaw() {
            let rows = this.$refs.table.selection;
            if (rows.length === 0) {
                this.$message.warning("请选择一条记录进行解冻")
                return;
            }
            for (let item of rows) {
                if (item.stockStatus === '正常') {
                    this.$message.warning("物品:" + item.skuCode + " 不需要解冻,请重新选择")
                    return
                }
            }
            this.form1.popShow = true,
                this.form1.freezeShow = false
            this.form1.thawShow = true
        },
        freeze() {
            let rows = this.$refs.table.selection;
            if (rows.length === 0) {
                this.$message.warning("请选择一条记录进行冻结")
                return;
            }
            for (let item of rows) {
                if (item.stockStatus === '冻结') {
                    this.$message.warning("物品:" + item.skuCode + " 不需要冻结,请重新选择")
                    return
                }
            }
            this.form1.popShow = true,
                this.form1.thawShow = false
            this.form1.freezeShow = true
        },
        print() {
            alert("箱贴打印")
            this.navParams.get('userInfo');
            window.open(" http://10.168.3.106:6480/box.aspx")
        },
        cancel() {
            this.form1 = {
                popShow: false,
                thawShow: false,
                thawType: "",
                freezeShow: false,
                freezeType: "",
                remark: ""
            }
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
        showByLpn() {
            this.$router.push({
                name: '按LPN显示',
            });
        },
        showBySerial() {
            this.$router.push({
                name: '按序列号显示',
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
                this.dialog.childrenData.push({id: 1, targetLocId: [], targetLpnCode: ''});
            } else {
                this.dialog.title = '按件移动';
                await getStockDataByStockId(rows[0].stockId).then((res) => {
                    this.dialog.gridData.push(res.data.data);
                })
                this.dialog.children.actualQty = this.dialog.gridData[0].stockEnable;
            }
        },
        // ==========================已下是 dialog 中的方法=================================
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
                for (let i = 0; i < data.length; i++) {
                    if (i === 0) {
                        this.dialog.merge.mergedArray.push(1);
                        this.dialog.merge.mergedIndex = 0;
                    } else {
                        if (data[i].boxCode === data[i - 1].boxCode) {
                            this.dialog.merge.mergedArray[this.dialog.merge.mergedIndex] += 1;
                            this.dialog.merge.mergedArray.push(0);
                        } else {
                            this.dialog.merge.mergedArray.push(1);
                            this.dialog.merge.mergedIndex = i;
                        }
                    }
                }
            }
        },
        rowAdd() {
            let id = this.dialog.childrenData.length + 1;
            if (this.dialog.serials.length === 0) {
                this.dialog.children.min = 0;
                this.dialog.children.max = this.dialog.gridData[0].stockEnable;
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

        },
        getSerialDataSource(data) {
            this.dialog.serials = data;
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
            this.resetMerge()
        },
        resetMerge() {
            this.dialog.merge = {
                mergedArray: [],
                mergedIndex: 0,
            };
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
                    this.dialog.children.actualQty = 0;
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
            if (func.isEmpty(childrenData)) {
                this.$message.warning("请填写移动信息");
                return;
            }
            if (this.dialog.isMoveByBox) {
                if (func.isEmpty(childrenData[0].targetLocId)) {
                    this.$message.warning("请选择目标库位编码");
                    return;
                }
                let moveByBoxObj = {
                    boxCodeList: [...new Set(this.dialog.gridData.map(item => item.boxCode))],
                    targetLocId: this.dialog.childrenData[0].targetLocId,
                    targetLpnCode: this.dialog.childrenData[0].targetLpnCode,
                }
                moveByBox(moveByBoxObj).then((res) => {
                    this.onClose();
                    this.refreshTable();
                    this.$message.success(res.data.msg);
                });
            } else {
                for (const i in childrenData) {
                    if (this.dialog.serials.length !== 0 && childrenData[i].serials.length === 0) {
                        this.$message.warning(`第${childrenData[i].id}行，请选择序列号`);
                        return;
                    }
                    if (this.dialog.serials.length !== 0 && childrenData[i].serials.length !== childrenData[i].qty) {
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

                let locIdList = childrenData.map(item => item.locId);
                for (const i in locIdList) {
                    if (func.isEmpty(locIdList[i])) {
                        this.$message.error(`第${childrenData[i].id}行，请选择库位`)
                        return;
                    }
                }
                let moveObj = {
                    stockId: this.dialog.gridData[0].stockId,
                    stockMoveDataList: this.dialog.childrenData
                }
                move(moveObj).then((res) => {
                    this.onClose();
                    this.refreshTable();
                    this.$message.success(res.data.msg);
                })
            }
        },
        showHasSerialView(stockId) {
            this.$router.push({
                name: '序列号',
                params: {
                    stockId: stockId
                }
            });
        }
    },
};
</script>

<style lang="scss" scoped>

/deep/ .maxDialog {
    margin-right: 19px;
    width: 87.5%;
    margin-top: 14vh !important;
    height: 79%;
}

/deep/ .maxDialog .el-dialog__body {
    max-height: 82% !important;
}
</style>
