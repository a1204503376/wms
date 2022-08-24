<template>
    <basic-container>
        <el-container id="container" v-loading="loading">
            <el-main style="overflow: hidden;overflow-y: scroll;height: 100%">
                <el-form ref="form"
                         label-position="right"
                         label-width="120px"
                         size="mini"
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
                                    <el-button class="top_button" size="medium"
                                               type="primary" @click="onRefresh">
                                        刷新
                                    </el-button>
                                </el-col>
                                <el-col :span="4.8">
                                    <el-button class="top_button" size="medium"
                                               type="primary" @click="onAssign">
                                        自动分配
                                    </el-button>
                                </el-col>
                                <el-col :span="4.8">
                                    <el-button class="top_button" size="medium"
                                               type="primary" @click="onCancelAll">
                                        全部取消
                                    </el-button>
                                </el-col>
                                <!--                                <el-col :span="4.8">-->
                                <!--                                    <el-button class="top_button" size="medium"-->
                                <!--                                               type="primary" @click="onCancel">-->
                                <!--                                        取消分配-->
                                <!--                                    </el-button>-->
                                <!--                                </el-col>-->
                                <el-col :span="4.8">
                                    <el-button class="top_button" size="medium"
                                               type="primary" @click="onIssued">
                                        确认下发
                                    </el-button>
                                </el-col>
                                <el-col :span="4.8">
                                    <el-button class="top_button"
                                               size="medium" @click="onClose">关 闭
                                    </el-button>
                                </el-col>
                            </el-row>
                        </el-col>
                    </el-row>
                    <el-row style="margin-bottom: 20px">
                        <el-col>
                            <el-tabs>
                                <el-tab-pane label="发货明细">
                                    <el-table
                                        ref="table"
                                        :data="table.soDetailData"
                                        :height="table.soDetailHeight"
                                        :show-summary="true"
                                        :summary-method="summarySoDetail"
                                        border
                                        highlight-current-row
                                        size="mini"
                                        @row-click="onRowClick">
                                        <template v-for="(column,index) in table.soDetailColumnList">
                                            <el-table-column
                                                v-if="!column.hide"
                                                :key="index"
                                                show-overflow-tooltip
                                                v-bind="column">
                                            </el-table-column>
                                        </template>
                                        <el-table-column align="center" fixed="right" label="操作" width="100">
                                            <template v-slot="{row}">
                                                <el-button size="small" type="text" @click.stop="onAdjust(row)">
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
                                        <el-table
                                            ref="soPickPlanTable"
                                            :data="table.soPickPlanData"
                                            :height="table.soPickPlanHeight"
                                            :show-summary="true"
                                            :span-method="objectSpanMethod"
                                            :summary-method="summarySoPickPlan"
                                            border
                                            highlight-current-row
                                            size="mini">
                                            <el-table-column v-if="false" fixed type="selection" width="50">
                                            </el-table-column>
                                            <template v-for="(column,index) in table.soPickPlanColumnList">
                                                <el-table-column
                                                    :key="index"
                                                    show-overflow-tooltip
                                                    v-bind="column"
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
        <el-dialog
            :append-to-body="true"
            :close-on-click-modal="false"
            :show-close="true"
            :title="dialog.title"
            :visible.sync="dialog.dialogTableVisible"
            custom-class="dialogClass"
            @close="closeDialog">
            <el-table
                ref="dialogTable"
                :data="dialog.dialogData"
                :height="dialog.dialogTableHeight"
                :span-method="objectSpanMethod"
                :summary-method="getSummaries"
                border
                highlight-current-row
                show-summary>
                <template style="margin-top: 10px">
                    <el-table-column label="箱码" prop="boxCode" width="120"></el-table-column>
                    <el-table-column label="LPN" prop="lpnCode" width="120"></el-table-column>
                    <el-table-column label="库位" prop="locCode" width="120"></el-table-column>
                    <el-table-column label="库区" prop="zoneCode" width="120"></el-table-column>
                    <el-table-column label="余额" prop="stockBalance"></el-table-column>
                    <el-table-column label="可用量" prop="stockEnable"></el-table-column>
                    <el-table-column label="本次分配量" prop="pickQty" width="150">
                        <template v-slot="{row}">
                            <el-input
                                v-model="row.pickQty"
                                maxlength="9"
                                oninput="value=value.replace(/[^\d]/g,'')"
                                placeholder="请输入分配数量"
                                size="medium"
                                style="width: 100%"
                                @input="val=>changePickQty(val,row)">
                            </el-input>
                        </template>
                    </el-table-column>
                    <el-table-column label="物品编码" prop="skuCode" width="120"></el-table-column>
                    <el-table-column label="批次号" prop="lotNumber"></el-table-column>
                    <el-table-column label="库存状态" prop="stockStatus"></el-table-column>
                    <el-table-column label="生产批次" prop="skuLot1" width="120"></el-table-column>
                    <el-table-column label="规格型号" prop="skuLot2"></el-table-column>
                    <el-table-column label="收货日期" prop="skuLot3" width="120"></el-table-column>
                    <el-table-column label="专用客户" prop="skuLot4"></el-table-column>
                    <el-table-column label="钢背批次" prop="skuLot5"></el-table-column>
                    <el-table-column label="摩擦快批次" prop="skuLot6" width="150"></el-table-column>
                    <el-table-column label="产品标识代码" prop="skuLot7" width="150"></el-table-column>
                    <el-table-column label="是否CRCC验证" prop="skuLot8" width="150"></el-table-column>
                </template>
            </el-table>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="onAdjustSubmit()">确 定</el-button>
                <el-button @click="dialog.dialogTableVisible = false">取 消</el-button>
            </div>
        </el-dialog>

    </basic-container>
</template>

<script>
import {editMixin} from "@/mixins/edit";
import {
    automaticAssign,
    cancelAll,
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
                        label: '实际分配量'
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
                        prop: 'lotNumber',
                        label: '批次号'
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
                        label: '是否CRCC校验'
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
                dialogTableHeight: 0,
                dialogData: [
                    {
                        boxCode: '',
                        location: '',
                        stockBalance: 0,
                        stockEnable: 0,
                        pickQty: 0,
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
        }
    },
    created() {
        window.addEventListener('resize', this.autoTableHeight);
        this.getTableData();
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
                this.dialog.dialogTableHeight = height - 260;
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
            this.getSoPickPlanData(this.soBillId);
        },
        async getSoPickPlanData(soBillId, soDetailId) {
            await getSoPickPlanData(soBillId, soDetailId).then((res) => {
                this.table.soPickPlanData = res.data.data;
                this.handleRefreshTable();
            })
            this.merge(this.table.soPickPlanData);
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
            automaticAssign(this.soHeader.soBillId).then((res) => {
                this.$message.success(res.data.msg);
                this.getSoPickPlanData(this.soHeader.soBillId);
            })
        },
        onIssued() {
            issued(this.soHeader.soBillId).then((res) => {
                this.$message.success(res.data.msg);
            })
        },
        onCancelAll() {
            cancelAll(this.soHeader.soBillId).then((res) => {
                this.$message.success(res.data.msg);
                this.getTableData()
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
            await getStockByDistributeAdjust(row.skuId, row.skuLot1, row.skuLot4, this.soHeader.soBillId)
                .then((res) => {
                    this.dialog.dialogData = res.data.data;
                    this.dialog.dialogData.forEach(item =>{
                        item.oldStockEnable = item.stockEnable;
                        item.oldPickQty = item.pickQty
                    })
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
                    .map(y => Number(y.pickQty))
                    .reduce((pre, cur) => pre + cur, 0)
                this.dialog.unDistributeQty = this.dialog.currentRow.surplusQty - currentSkuPickPlanQty;
            } else {
                this.dialog.unDistributeQty = this.dialog.currentRow.surplusQty
            }
        },
        changePickQty(val, row) {
            console.log(val);
            console.log(row);
            if (val > row.oldStockEnable){
                this.$message.warning("分配量不能大于可用量");
                row.pickQty = row.oldPickQty;
            }

            this.computeUnDistributeQty();
            this.setDialogTitle()
        },
        setDialogTitle() {
            this.dialog.title =
                `当前物品：${this.dialog.currentRow.skuCode} ${this.dialog.currentRow.skuName}        未分配：${this.dialog.unDistributeQty}`
        },
        onAdjustSubmit() {
            let dialogData = this.dialog.dialogData;
            if(func.isEmpty(dialogData)){
                this.$message.warning("没有可保存的数据");
                this.dialog.dialogTableVisible = false;
                return;
            }
            let data = this.dialog.dialogData.filter(item => this.filterRowBySoPickPlan(item));
            for (const i in data) {
                if (data[i].pickQty > data[i].stockEnable) {
                    this.$message.warning(`第${i}行，物品 ${data[i].skuCode}，批次${data[i].skuLot1} 的分配量不能大于可用量`)
                    return;
                }
            }
            let stockIdAndSoPickPlanQtyList = data.map(item => {
                return Object.assign({}, {'stockId': item.stockId, 'soPickPlanQty': item.planQty})
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
                    this.$message.success(res.data.msg)
                    this.resetMerge(this.getSoPickPlanData(this.soHeader.soBillId));
                    this.dialog.dialogTableVisible = false;
                })
        },
        // 过滤未填写本次分配量的行
        filterRowBySoPickPlan(row) {
            return !(row.pickQty === 0 || func.isEmpty(row.pickQty));
        },
        getSummaries(param) {
            const {columns, data} = param;
            const sums = [];
            columns.forEach((column, index) => {
                const values = data.map(item => Number(item[column.property]));
                if (!values.every(value => isNaN(value))) {
                    if (column.property === 'stockBalance'
                        || column.property === 'stockEnable'
                        || column.property === 'pickRealQty')
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

/deep/ .dialogClass {
    margin-right: 13px;
    width: 88%;
    margin-top: 14vh !important;
}
</style>
