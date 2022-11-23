<template>
    <basic-container>
        <el-container id="container" v-loading="loading">
            <el-main style="overflow: hidden;overflow-y: scroll;height: 100%">
                <el-form label-position="right" label-width="120px" ref="form" size="mini"
                         style="margin-left:10px;margin-right:10px;">
                    <el-row>
                        <el-col :span="6">
                            <el-row :gutter="10" justify="end" type="flex">
                                <el-col :span="10">
                                    <el-form-item label="发货单编码：" label-width="100px">
                                        <span>{{ soHeader.soBillNo }}</span>
                                    </el-form-item>
                                </el-col>
                                <el-col :offset="4" :span="10">
                                    <el-form-item label="上游编码：" label-width="120px">
                                        <span>{{ soHeader.orderNo }}</span>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                        </el-col>
                        <el-col :span="6">
                        </el-col>
                        <el-col :span="12">
                            <el-row :gutter="10" justify="end" type="flex">
                                <el-col :span="4.8">
                                    <el-button @click="onRefresh" class="top_button" size="medium" type="primary">
                                        刷新显示整单分配情况
                                    </el-button>
                                </el-col>
                                <el-col :span="4.8">
                                    <el-button @click="onAssign" class="top_button" size="medium" type="primary">
                                        自动分配
                                    </el-button>
                                </el-col>
                                <el-col :span="4.8">
                                    <el-button @click="onCancelAll" class="top_button" size="medium" type="primary">
                                        全部取消
                                    </el-button>
                                </el-col>
                                <!--       <el-col :span="4.8">-->
                                <!--           <el-button class="top_button" size="medium"-->
                                <!--                      type="primary" @click="onCancel">-->
                                <!--               取消分配-->
                                <!--           </el-button>-->
                                <!--       </el-col>-->
                                <el-col :span="4.8">
                                    <el-button @click="onIssued" class="top_button" size="medium" type="primary">
                                        确认下发
                                    </el-button>
                                </el-col>
                                <el-col :span="4.8">
                                    <el-button @click="onCloseDistribution" class="top_button" size="medium">关 闭
                                    </el-button>
                                </el-col>
                            </el-row>
                        </el-col>
                    </el-row>
                    <el-row style="margin-bottom: 20px">
                        <el-col>
                            <el-tabs>
                                <el-tab-pane label="发货明细">
                                    <el-table :data="table.soDetailData" :height="table.soDetailHeight"
                                              :show-summary="true"
                                              :summary-method="summarySoDetail" @row-click="onRowClick" border
                                              highlight-current-row ref="table" size="mini">
                                        <template v-for="(column, index) in table.soDetailColumnList">
                                            <el-table-column :key="index" show-overflow-tooltip v-bind="column"
                                                             v-if="!column.hide">
                                            </el-table-column>
                                        </template>
                                        <el-table-column align="center" fixed="right" label="操作" width="100">
                                            <template v-slot="{ row }">
                                                <el-button @click.stop="onAdjust(row)" size="small" type="text">
                                                    调整
                                                </el-button>
                                            </template>
                                        </el-table-column>
                                    </el-table>
                                </el-tab-pane>
                            </el-tabs>
                        </el-col>
                    </el-row>
                    <el-row :gutter="10">
                        <el-col>
                            <template>
                                <el-tabs type="border-card">
                                    <el-tab-pane label="分配记录">
                                        <el-table :data="table.soPickPlanData" :height="table.soPickPlanHeight"
                                                  :show-summary="true" :span-method="objectSpanMethod"
                                                  :summary-method="summarySoPickPlan" border
                                                  highlight-current-row
                                                  ref="soPickPlanTable" size="mini">
                                            <el-table-column fixed type="selection" v-if="false" width="50">
                                            </el-table-column>
                                            <template v-for="(column, index) in table.soPickPlanColumnList">
                                                <el-table-column :key="index" show-overflow-tooltip v-bind="column"
                                                                 width="100">
                                                </el-table-column>
                                            </template>
                                        </el-table>
                                    </el-tab-pane>
                                </el-tabs>
                            </template>
                        </el-col>
                    </el-row>
                </el-form>
            </el-main>
        </el-container>
        <el-dialog :append-to-body="true" :close-on-click-modal="false" :fullscreen="true" :show-close="true"
                   :title="dialog.title" :visible.sync="dialog.dialogTableVisible" @close="closeDialog" top="0">
            <el-table :data="dialog.dialogData" :height="dialog.dialogTableHeight" :span-method="objectSpanMethod"
                      :summary-method="getSummaries" border highlight-current-row ref="dialogTable"
                      show-summary>
                <el-table-column label="箱码" prop="boxCode" width="120">
                    <template v-slot="{ row }">
                        <el-link :underline="false" @click="onShowBoxStock(row)" target="_blank" type="primary">{{
                                row.boxCode
                            }}
                        </el-link>
                    </template>
                </el-table-column>
                <el-table-column label="LPN" prop="lpnCode" width="120"></el-table-column>
                <el-table-column label="库位" prop="locCode" width="120">
                    <template v-slot="{ row }">
                        <el-link :underline="false" @click="onShowLocStock(row)" target="_blank" type="primary">{{
                                row.locCode
                            }}
                        </el-link>
                    </template>
                </el-table-column>
                <el-table-column label="库区" prop="zoneCode" width="120"></el-table-column>
                <el-table-column label="余额" prop="stockBalance"></el-table-column>
                <el-table-column label="可用量" prop="stockEnable"></el-table-column>
                <el-table-column label="本次分配量" prop="distributeQty" width="150">
                    <template v-slot="{ row }">
                        <el-input @input="val => changePickQty(val, row)" maxlength="9"
                                  oninput="value=value.replace(/[^\d]/g,'')"
                                  placeholder="请输入分配数量" size="medium" style="width: 100%"
                                  v-model.number="row.distributeQty">
                        </el-input>
                    </template>
                </el-table-column>
                <el-table-column label="物品编码" prop="skuCode" width="120"></el-table-column>
                <el-table-column label="库存状态" prop="stockStatus"></el-table-column>
                <el-table-column label="生产批次" prop="skuLot1" width="120"></el-table-column>
                <el-table-column label="规格型号" prop="skuLot2"></el-table-column>
                <el-table-column label="收货日期" prop="skuLot3" width="120"></el-table-column>
                <el-table-column label="专用客户" prop="skuLot4"></el-table-column>
                <el-table-column label="钢背批次" prop="skuLot5"></el-table-column>
                <el-table-column label="摩擦快批次" prop="skuLot6" width="150"></el-table-column>
                <el-table-column label="产品标识代码" prop="skuLot7" width="150"></el-table-column>
                <el-table-column label="适用速度等级" prop="skuLot8" width="150"></el-table-column>
            </el-table>
            <div class="dialog-footer" slot="footer">
                <el-button @click="onAdjustSubmit()" type="primary">确 定</el-button>
                <el-button @click="dialog.dialogTableVisible = false">取 消</el-button>
            </div>
            <!--      内层dialog      -->
            <el-dialog :append-to-body="true" :title="innerDialog.title" :visible.sync="innerDialog.showInnerDialog"
                       width="80%">
                <el-table :data="innerDialog.tableData" :height="innerDialog.tableHeight" :summary-method="getSummaries"
                          border highlight-current-row ref="dialogTable" show-overflow-tooltip
                          show-summary>
                    <template v-for="(column, index) in innerDialog.columnList">
                        <el-table-column :key="index" show-overflow-tooltip v-bind="column">
                        </el-table-column>
                    </template>
                </el-table>
                <div class="dialog-footer" slot="footer">
                    <el-button @click="innerDialog.showInnerDialog = false">关 闭</el-button>
                </div>
            </el-dialog>
        </el-dialog>

    </basic-container>
</template>

<script>
import {editMixin} from "@/mixins/edit";
import {
    automaticAssign,
    cancelAll,
    getAssignDistributeStock,
    getSoBillDataByDistribution,
    getSoPickPlanData,
    getStockByDistributeAdjust,
    issued,
    saveAssign
} from "@/api/wms/outstock/soHeader"
import func from "@/util/func";

export default {
    name: "distribution",
    mixins: [editMixin],
    props: {
        soBillId: {type: String, required: true},
    },
    data() {
        return {
            that: this,
            loading: false,
            soHeader: {
                soBillId: '',
                soBillNo: '',
                orderNo: ''
            },
            table: {
                soDetailColumnList: [
                    {
                        prop: 'soLineNo',
                        width: 50,
                        label: '行号'
                    },
                    {
                        prop: 'skuCode',
                        label: '物品编码'
                    },
                    {
                        prop: 'skuName',
                        label: '物品名称'
                    },
                    {
                        prop: 'skuSpec',
                        label: '物品规格'
                    },
                    {
                        prop: 'umCode',
                        label: '计量单位编码',
                    },
                    {
                        prop: 'pickPlanQty',
                        label: '分配量',
                        align: 'right'
                    },
                    {
                        prop: 'planQty',
                        label: '计划量',
                        align: 'right'
                    },
                    {
                        prop: 'surplusQty',
                        label: '剩余量',
                        align: 'right'
                    },
                    {
                        prop: 'skuLot1',
                        label: '生产批次',
                    },
                    {
                        prop: 'skuLot4',
                        label: '专用客户',
                    },
                ],
                soDetailData: [],
                soDetailHeight: 0,
                soPickPlanColumnList: [
                    {
                        prop: 'boxCode',
                        label: '箱码',
                    },
                    {
                        prop: 'zoneCode',
                        label: '库区',
                    },
                    {
                        prop: 'locCode',
                        label: '库位'
                    },
                    {
                        prop: 'lpnCode',
                        label: 'LPN'
                    },
                    {
                        prop: 'pickPlanQty',
                        label: '计划分配量'
                    },
                    {
                        prop: 'pickRealQty',
                        label: '已拣量'
                    },
                    {
                        prop: 'skuCode',
                        label: '物品编码'
                    },
                    {
                        prop: 'skuName',
                        label: '物品名称'
                    },
                    {
                        prop: 'stockStatus',
                        label: '库存状态'
                    },
                    {
                        prop: 'skuLot1',
                        label: '生产批次'
                    },
                    {
                        prop: 'skuLot2',
                        label: '规格型号'
                    },
                    {
                        prop: 'skuLot3',
                        label: '收货日期'
                    },
                    {
                        prop: 'skuLot4',
                        label: '专用客户'
                    },
                    {
                        prop: 'skuLot5',
                        label: '钢背批次'
                    },
                    {
                        prop: 'skuLot6',
                        width: 100,
                        label: '摩擦快批次'
                    },
                    {
                        prop: 'skuLot7',
                        width: 100,
                        label: '产品标识代码'
                    },
                    {
                        prop: 'skuLot8',
                        width: 100,
                        label: '适用速度等级'
                    },
                ],
                soPickPlanData: [],
                soPickPlanHeight: 0,
            },
            mergedCell: {
                mergedArray: [],
                mergedIndex: 0,
            },
            dialog: {
                dialogTableVisible: false,
                title: "",
                unDistributeQty: 0,
                pickStockBalance: 0,
                agvStockBalance: 0,
                dialogTableHeight: 0,
                dialogData: [
                    {
                        boxCode: '',
                        location: '',
                        stockBalance: 0,
                        stockEnable: 0,
                        distributeQty: 0,
                        lpn: '',
                        skuCode: '',
                        stockState: '',
                        skuLot1: '',
                    }
                ],
                currentRow: {
                    skuId: '',
                    skuCode: '',
                    skuName: '',
                    soDetailId: '',
                    surplusQty: '',
                }
            },
            innerDialog: {
                showInnerDialog: false,
                title: "",
                columnList: [
                    {
                        prop: 'skuCode',
                        label: '物品编码'
                    },
                    {
                        prop: 'skuName',
                        label: '物品名称'
                    },
                    {
                        prop: 'stockBalance',
                        label: '余额',
                        align: 'right'
                    },
                    {
                        prop: 'stockEnable',
                        label: '库存可用',
                        align: 'right'
                    },
                    {
                        prop: 'occupyQty',
                        label: '库存占用',
                        align: 'right'
                    },
                    {
                        prop: 'skuLot1',
                        label: '生产批次',
                    },
                    {
                        prop: 'skuLot2',
                        label: '规格型号'
                    },
                    {
                        prop: 'skuLot3',
                        label: '收货日期',
                    },
                    {
                        prop: 'skuLot4',
                        label: '专用客户',
                    },
                    {
                        prop: 'locCode',
                        label: '库位编码',
                    },
                    {
                        prop: 'lpnCode',
                        label: 'LPN',
                    },
                ],
                tableData: []
            }
        }
    },
    created() {
        window.addEventListener('resize', this.autoTableHeight);
        this.getTableData();
        this.getSoPickPlanData(this.soBillId);
    },
    watch: {
        soBillId() {
            this.refreshTable();
        },
        $route() {
            this.handleRefreshTable();
        }
    },
    methods: {
        autoTableHeight() {
            this.$nextTick(() => {
                let height = window.innerHeight;
                document.getElementById('container').style.height = `${height}px`;
                let tableHeight = height - 350;
                this.table.soDetailHeight = tableHeight * 0.4;
                this.table.soPickPlanHeight = tableHeight * 0.6;
                this.dialog.dialogTableHeight = height - 131;
                this.innerDialog.tableHeight = height - 350;
            })
            this.handleRefreshTable();
        },
        resetMerge(data) {
            this.mergedCell = {
                mergedArray: [],
                mergedIndex: 0,
            };
            this.merge(data);
        },
        closeDialog() {
            // 重置mergedCell对象,并重新赋值
            this.resetMerge(this.table.soPickPlanData);
        },
        objectSpanMethod({row, column, rowIndex, columnIndex}) {
            if (this.mergedCell.mergedArray.length > 0) {
                if (
                    column.label === '箱码' || column.label === '库位' ||
                    column.label === '库区' || column.label === 'LPN'
                ) {
                    const _row = this.mergedCell.mergedArray[rowIndex];
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
                        this.mergedCell.mergedArray.push(1);
                        this.mergedCell.mergedIndex = 0;
                    } else {
                        // 判断当前元素与上一元素是否相同
                        if (
                            data[i].boxCode === data[i - 1].boxCode
                            && data[i].locId === data[i - 1].locId
                            && data[i].zoneId === data[i - 1].zoneId
                            && data[i].lpnCode === data[i - 1].lpnCode
                        ) {
                            this.mergedCell.mergedArray[this.mergedCell.mergedIndex] += 1;
                            this.mergedCell.mergedArray.push(0);
                        } else {
                            this.mergedCell.mergedArray.push(1);
                            this.mergedCell.mergedIndex = i;
                        }
                    }
                }
            }
        },
        refreshTable() {
            this.getTableData();
            this.getSoPickPlanData(this.soBillId);
        },
        getTableData() {
            getSoBillDataByDistribution(this.soBillId).then((res) => {
                let data = res.data.data;
                this.soHeader.soBillId = data.soBillId;
                this.soHeader.soBillNo = data.soBillNo;
                this.soHeader.orderNo = data.orderNo;
                this.table.soDetailData = data.soDetailList;
                this.handleRefreshTable();
            })
        },
        async getSoPickPlanData(soBillId, soDetailId) {
            await getSoPickPlanData(soBillId, soDetailId).then((res) => {
                this.table.soPickPlanData = res.data.data;
                this.handleRefreshTable();
            })
            this.resetMerge(this.table.soPickPlanData);
        },
        summarySoPickPlan(param) {
            const {columns, data} = param;
            const sums = [];
            columns.forEach((column, index) => {
                if (index === 0) {
                    sums[index] = '合计';
                    return;
                }
                const values = data.map(item => Number(item[column.property]));
                if (column.property === 'pickPlanQty'
                    || column.property === 'pickRealQty') {
                    sums[index] = values.reduce((prev, curr) => {
                        const value = Number(curr);
                        if (!isNaN(value)) {
                            return prev + curr;
                        } else {
                            return prev;
                        }
                    }, 0);
                }
            });
            return sums;
        },
        summarySoDetail(param) {
            const {columns, data} = param;
            const sums = [];
            columns.forEach((column, index) => {
                if (index === 0) {
                    sums[index] = '合计';
                    return;
                }
                const values = data.map(item => Number(item[column.property]));
                if (column.property === 'pickPlanQty' || column.property === 'planQty' || column.property === 'surplusQty') {
                    sums[index] = values.reduce((prev, curr) => {
                        const value = Number(curr);
                        if (!isNaN(value)) {
                            return prev + curr;
                        } else {
                            return prev;
                        }
                    }, 0);
                }
            });
            return sums;
        },
        onRowClick(row) {
            this.getSoPickPlanData(this.soHeader.soBillId, row.soDetailId)
        },
        onRefresh() {
            this.getSoPickPlanData(this.soHeader.soBillId);
        },
        onAssign() {
            let soBillId = this.soHeader.soBillId;
            automaticAssign(soBillId).then((res) => {
                this.$message.success(res.data.msg);
                this.getTableData();
                this.getSoPickPlanData(soBillId);
            })
        },
        onIssued() {
            issued(this.soHeader.soBillId).then((res) => {
                this.$message.success(res.data.msg);
            })
        },
        onCancelAll() {
            let soBillId = this.soHeader.soBillId;
            cancelAll(soBillId).then((res) => {
                this.$message.success(res.data.msg);
                this.getTableData();
                this.getSoPickPlanData(soBillId);
            })
        },
        // onCancel() {
        //     let rows = this.$refs.soPickPlanTable.selection;
        //     if (func.isEmpty(rows)) {
        //         this.$message.warning("请选择一条分配记录");
        //         return
        //     }
        // },
        async onAdjust(row) {
            this.dialog.currentRow = {
                soDetailId: row.soDetailId,
                skuId: row.skuId,
                skuCode: row.skuCode,
                skuName: row.skuName,
                surplusQty: row.surplusQty
            }
            await getStockByDistributeAdjust(row.skuId, row.skuLot1, row.skuLot2, row.skuLot4, this.soHeader.soBillId)
                .then((res) => {
                    this.dialog.dialogData = res.data.data.stockSoPickPlanList;
                    this.dialog.dialogData.forEach(item => {
                        item.oldStockEnable = item.stockEnable;
                        item.oldPickQty = item.distributeQty
                    })
                    if (res.data.data.pickStockBalance > 0) {
                        this.dialog.pickStockBalance = res.data.data.pickStockBalance;
                    } else {
                        this.dialog.pickStockBalance = 0;
                    }
                    if (res.data.data.agvStockBalance > 0) {
                        this.dialog.agvStockBalance = res.data.data.agvStockBalance;
                    } else {
                        this.dialog.agvStockBalance = 0;
                    }

                })
            this.computeUnDistributeQty();
            this.setDialogTitle()
            // 重置mergedCell对象,并重新赋值
            this.resetMerge(this.dialog.dialogData);
            this.dialog.dialogTableVisible = true;
        },
        computeUnDistributeQty() {
            if (func.isNotEmpty(this.dialog.dialogData)) {
                let currentSkuPickPlanQty = this.dialog.dialogData
                    .filter(x => x.skuId === this.dialog.currentRow.skuId)
                    .map(y => Number(y.distributeQty))
                    .reduce((pre, cur) => pre + cur, 0)
                this.dialog.unDistributeQty = this.dialog.currentRow.surplusQty - currentSkuPickPlanQty;
            } else {
                this.dialog.unDistributeQty = this.dialog.currentRow.surplusQty
            }
        },
        changePickQty(val, row) {
            if (val > row.oldStockEnable) {
                this.$message.warning("分配量不能大于可用量");
                row.distributeQty = row.oldPickQty;
            }

            this.computeUnDistributeQty();
            this.setDialogTitle()
        },
        setDialogTitle() {
            this.dialog.title =
                `当前物品：${this.dialog.currentRow.skuCode} ${this.dialog.currentRow.skuName}        未分配：${this.dialog.unDistributeQty}        人工区库存：${this.dialog.pickStockBalance}        自动区库存：${this.dialog.agvStockBalance}`
        },
        onAdjustSubmit() {
            let dialogData = this.dialog.dialogData;
            if (func.isEmpty(dialogData)) {
                this.$message.warning("没有可保存的数据");
                this.dialog.dialogTableVisible = false;
                return;
            }
            for (const i in dialogData) {
                if (dialogData[i].distributeQty > dialogData[i].stockEnable) {
                    this.$message.warning(`第${Number(i) + 1}行，物品 ${dialogData[i].skuCode}，批次${dialogData[i].skuLot1} 的分配量不能大于可用量`);
                    return;
                }

            }
            let data = this.dialog.dialogData.filter(item => this.filterRowBySoPickPlan(item));
            let stockIdAndSoPickPlanQtyList = data.map(item => {
                return Object.assign({}, {'stockId': item.stockId, 'soPickPlanQty': item.distributeQty})
            })
            let soPickPlanList = [];
            if (func.isNotEmpty(this.dialog.dialogData)) {
                this.dialog.dialogData.map(item => item.soPickPlanList).forEach(value => {
                    if (func.isNotEmpty(value)) {
                        value.forEach(x => {
                            if (func.isNotEmpty(x)) {
                                soPickPlanList.push(x);
                            }
                        })
                    }
                })
            }
            saveAssign(this.soHeader.soBillId, this.dialog.currentRow.soDetailId, soPickPlanList, stockIdAndSoPickPlanQtyList)
                .then((res) => {
                    this.$message.success(res.data.msg);
                    this.getTableData();
                    this.getSoPickPlanData(this.soHeader.soBillId)
                    this.dialog.dialogTableVisible = false;
                })
        },
        // 过滤未填写本次分配量的行和分配量为0的行
        filterRowBySoPickPlan(row) {
            return !(func.isEmpty(row.distributeQty) || row.distributeQty === 0);
        },
        getSummaries(param) {
            const {columns, data} = param;
            const sums = [];
            columns.forEach((column, index) => {
                const values = data.map(item => Number(item[column.property]));
                if (!values.every(value => isNaN(value))) {
                    if (column.property === 'stockBalance'
                        || column.property === 'stockEnable'
                        || column.property === 'pickRealQty'
                        || column.property === 'occupyQty')
                        sums[index] = values.reduce((prev, curr) => {
                            const value = Number(curr);
                            if (!isNaN(value)) {
                                return prev + curr;
                            } else {
                                return prev;
                            }
                        }, 0);
                }
            });
            return sums;
        },
        // 分配调整dialog中的boxStock弹窗
        onShowBoxStock(row) {
            this.innerDialog.title = `箱码:${row.boxCode}`
            this.innerDialog.showInnerDialog = true;
            getAssignDistributeStock(row.boxCode, null, null).then(res => {
                this.innerDialog.tableData = res.data.data
            })
        },
        // 分配调整dialog中的locStock弹窗
        onShowLocStock(row) {
            this.innerDialog.title = `库位:${row.locCode}`
            this.innerDialog.showInnerDialog = true;
            getAssignDistributeStock(null, row.whId, row.locCode).then(res => {
                this.innerDialog.tableData = res.data.data
            })
        },
        onCloseDistribution() {
            this.onClose();
            this.$router.push({
                path: '/wms/outstock/soHeader',
                query: {
                    isRefresh: 'true'
                }
            })
        },
        createRowObj() {
            // 覆盖混入的方法
        },
        handleRefreshTable() {
            // 解决固定列错位的问题
            this.$nextTick(() => {
                if (this.$refs.table && this.$refs.table.doLayout) {
                    this.$refs.table.doLayout();
                }
                if (this.$refs.soPickPlanTable && this.$refs.soPickPlanTable.doLayout) {
                    this.$refs.soPickPlanTable.doLayout();
                }
            });
        },
    }
}
</script>

<style scoped>
.top_button {
    float: right;
}

/deep/ .el-dialog__body {
    max-height: 84% !important;
}
</style>
