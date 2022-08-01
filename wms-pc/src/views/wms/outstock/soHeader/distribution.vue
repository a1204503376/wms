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
                        <el-row :gutter="10" type="flex">
                            <el-col :span="4">
                                <el-form-item label="发货单编码：" label-width="100px">
                                    <label>{{ soHeader.soBillNo }}</label>
                                </el-form-item>
                            </el-col>
                            <el-col :span="4">
                                <el-form-item label="上游编码：" label-width="120px">
                                    {{ soHeader.orderNo }}
                                </el-form-item>
                            </el-col>
                            <el-col :offset="10" :span="2">
                                <el-button class="top_button" size="medium"
                                           type="primary" @click="onRefresh">
                                    刷新
                                </el-button>
                            </el-col>
                            <el-col :span="2">
                                <el-button class="top_button" size="medium"
                                           type="primary" @click="onAssign()">
                                    自动分配
                                </el-button>
                            </el-col>
                            <el-col :span="2">
                                <el-button class="top_button" size="medium"
                                           type="primary" @close="onCancelAll">
                                    全部取消
                                </el-button>
                            </el-col>
                            <el-col :span="2">
                                <el-button class="top_button" size="medium"
                                           type="primary" @click="onIssued">
                                    确认下发
                                </el-button>
                            </el-col>
                            <el-col :span="2">
                                <el-button :loading="loading" class="top_button"
                                           plain size="medium" @click="onClose">关 闭
                                </el-button>
                            </el-col>
                        </el-row>
                    </el-row>
                    <el-row style="margin-bottom: 20px">
                        <el-col>
                            <el-tabs>
                                <el-tab-pane label="发货明细">
                                    <el-table
                                        ref="table"
                                        :data="table.soDetailData"
                                        border
                                        highlight-current-row
                                        max-height="148"
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
                                            <template v-slot="scope">
                                                <el-button size="small" type="text" @click="onAdjust(scope.row)">
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
                                    <el-tab-pane label="按箱分配">
                                        <el-table
                                            :data="table.soPickPlanData"
                                            :span-method="objectSpanMethod"
                                            border
                                            highlight-current-row
                                            max-height="310"
                                            size="mini">
                                            <template v-for="(column,index) in table.soPickPlanColumnList">
                                                <el-table-column
                                                    :key="index"
                                                    show-overflow-tooltip
                                                    v-bind="column">
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
        <template>
            <el-dialog
                :append-to-body="true"
                :close-on-click-modal="false"
                :custom-class="dialog.isFullScreen ? 'maxDialog' : '' "
                :show-close="true"
                :visible.sync="dialog.dialogTableVisible"
                @close="refreshData">
                <span slot="title" class="dialog-footer">
                        <div class="icon">
                            <span>{{ dialog.title }}</span>
                            <el-button class="button_telescopic" type="text">
                                <i :class="dialog.isFullScreen ? 'icon-tuichuquanping' : 'icon-quanping'"
                                   @click="telescopic">
                                </i>
                            </el-button>
                        </div>
                    </span>
                <el-table
                    ref="dialogTable"
                    :data="dialog.dialogData"
                    :span-method="objectSpanMethod"
                    :summary-method="getSummaries"
                    :height="dialog.isFullScreen ? 530 : 450"
                    border
                    highlight-current-row
                    show-summary>
                    <div style="margin-top: 10px">
                        <el-table-column label="可用量" prop="stockEnable"></el-table-column>
                        <el-table-column label="余额" prop="stockBalance"></el-table-column>
                        <el-table-column label="本次分配量" prop="soPickPlanQty" width="150">
                            <template v-slot="{row}">
                                <el-input
                                    v-model="row.soPickPlanQty"
                                    maxlength="9"
                                    oninput="value=value.replace(/[^\d]/g,'')"
                                    placeholder="请输入分配数量"
                                    size="medium"
                                    style="width: 100%"
                                ></el-input>
                            </template>
                        </el-table-column>
                        <el-table-column label="箱码" prop="boxCode"></el-table-column>
                        <el-table-column label="库位" prop="locName"></el-table-column>
                        <el-table-column label="库区" prop="zoneName"></el-table-column>
                        <el-table-column label="LPN" prop="lpnCode"></el-table-column>
                        <el-table-column label="物品编码" prop="skuCode"></el-table-column>
                        <el-table-column label="批次" prop="lotNumber"></el-table-column>
                        <el-table-column label="状态" prop="stockState"></el-table-column>
                        <el-table-column label="生产批次" prop="skuLot1"></el-table-column>
                        <el-table-column label="规格型号" prop="skuLot2"></el-table-column>
                        <el-table-column label="收货日期" prop="skuLot3"></el-table-column>
                        <el-table-column label="专用客户" prop="skuLot4"></el-table-column>
                        <el-table-column label="钢背批次" prop="skuLot5"></el-table-column>
                        <el-table-column label="摩擦快批次" prop="skuLot6" width="150"></el-table-column>
                        <el-table-column label="产品标识代码" prop="skuLot7" width="150"></el-table-column>
                        <el-table-column label="是否CRCC验证" prop="skuLot8" width="150"></el-table-column>
                    </div>
                </el-table>
                <div slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="onAdjustSubmit()">确 定</el-button>
                    <el-button @click="dialog.dialogTableVisible = false">取 消</el-button>
                </div>
            </el-dialog>
        </template>
    </basic-container>
</template>

<script>
import {editMixin} from "@/mixins/edit";
import {
    automaticAssign,
    cancelAll,
    getEnableStockBySkuId,
    getSoBillDataByDistribution,
    getSoPickPlanData,
    issued,
    saveAssign
} from "@/api/wms/outstock/soHeader"
import func from "@/util/func";
import "../../../../../public/cdn/iconfont/avue/iconfont.css"

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
                        prop: 'stockBalance',
                        label: '剩余量',
                        align: 'center'
                    },
                    {
                        prop: 'distributeQty',
                        label: '分配量'
                    },
                    {
                        prop: 'planQty',
                        label: '计划量',
                        align: 'center'
                    },
                    {
                        prop: 'umCode',
                        label: '计量单位编码',
                        width: 100,
                        align: 'center'
                    },
                ],
                soDetailData: [],
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
                        label: '分配量'
                    },
                    {
                        prop: 'skuCode',
                        label: '物品编码'
                    },
                    {
                        prop: 'lotNumber',
                        label: '批次'
                    },
                    {
                        prop: 'stockEnable',
                        label: '可用量'
                    },
                    {
                        prop: 'stockBalance',
                        label: '余额'
                    },
                    {
                        prop: 'stockStatusValue',
                        label: '状态'
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
            },
            mergedCell: {
                mergedArray: [],
                mergedIndex: 0,
            },
            dialog: {
                dialogTableVisible: false,
                isFullScreen: false,
                title: "",
                dialogData: [
                    {
                        boxCode: '',
                        soPickPlanQty: 0,
                        location: '',
                        lpn: '',
                        skuCode: '',
                        stockEnable: 0,
                        stockBalance: 0,
                        stockState: '',
                        skuLot1: '',
                    }
                ],
                soDetailId: '',
            },

        }
    },
    created() {
        this.getTableData();
    },
    watch: {
        soBillId() {
            this.refreshTable();
        }
    },
    methods: {
        resetMerge(data) {
            this.mergedCell = {
                mergedArray: [],
                mergedIndex: 0,
            };
            this.merge(data);
        },
        refreshData() {
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
                data.soDetailList.forEach(item => {
                    item.distributeQty = 0;
                })
                this.table.soDetailData = data.soDetailList;
            })
            this.getSoPickPlanData(this.soBillId);
        },
        async getSoPickPlanData(soBillId, soDetailId) {
            await getSoPickPlanData(soBillId, soDetailId).then((res) => {
                this.table.soPickPlanData = res.data.data;
            })
            console.log(this.table.soPickPlanData);
            this.merge(this.table.soPickPlanData);
        },
        onRowClick(row) {
            this.getSoPickPlanData(this.soHeader.soBillId, row.soDetailId)
        },
        onRefresh() {
            this.getSoPickPlanData(this.soHeader.soBillId);
        },
        onAssign() {
            automaticAssign(this.soHeader.soBillId).then((res) => {

            })
        },
        onIssued() {
            issued(this.soHeader.soBillId).then((res) => {

            })
        },
        onCancelAll() {
            cancelAll(this.soHeader.soBillId).then((res) => {

            })
        },
        telescopic() {
            this.dialog.isFullScreen = !this.dialog.isFullScreen;
        },
        async onAdjust(row) {
            this.dialog.soDetailId = row.soDetailId;
            this.dialog.title = `分配调整  ${this.soHeader.soBillNo}  计划数量：${row.planQty}`
            await getEnableStockBySkuId(row.skuId).then((res) => {
                this.dialog.dialogData = res.data.data;
            })
            // 重置拣货计划的合并数据,并重新赋值
            this.resetMerge(this.dialog.dialogData);
            this.dialog.dialogTableVisible = true;
        },
        onAdjustSubmit() {
            let data = this.dialog.dialogData.filter(item => this.filterRowBySoPickPlan(item));
            for (const i in data) {
                if (data[i].soPickPlanQty > data[i].stockEnable) {
                    this.$message.warning(`物品 ${data[i].skuCode}，批次${data[i].skuLot1} 的分配量不能大于可用量`)
                    return;
                }
            }
            let stockIdAndSoPickPlanQtyList = data.map(item => {
                return Object.assign({}, {'stockId': item.stockId, 'soPickPlanQty': item.soPickPlanQty})
            })
            saveAssign(this.soHeader.soBillId, this.dialog.soDetailId, stockIdAndSoPickPlanQtyList).then((res) => {
                this.resetMerge(this.getSoPickPlanData());
            })

        },
        // 过滤未填写本次分配量的行
        filterRowBySoPickPlan(row) {
            return !(row.soPickPlanQty === 0 || func.isEmpty(row.soPickPlanQty));
        },
        getSummaries(param) {
            const {columns, data} = param;
            const sums = [];
            columns.forEach((column, index) => {
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
                }
            });
            return sums;
        },
        createRowObj() {
            // 覆盖混入的方法
        }
    }
}
</script>

<style scoped>
.d-table-header-required:before {
    content: "*";
    color: #F56C6C;
    margin-right: 4px;
}

.top_button {
    float: right;
}

.button_telescopic {
    color: #909399;
    float: right;
    line-height: 22px;
    margin-right: 22px;
    font-size: 16px;
}

/deep/ .maxDialog {
    margin-right: 13px;
    width: 88%;
    margin-top: 14vh !important;
    height: 79%;
}

/deep/ .maxDialog .el-dialog__body {
    max-height: 82% !important;
}
</style>
