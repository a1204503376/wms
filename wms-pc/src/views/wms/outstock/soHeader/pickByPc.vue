<template>
    <basic-container>
        <el-container id="container" v-loading="loading">
            <el-main style="overflow: hidden;overflow-y: scroll;">
                <el-form ref="form"
                         :model="form.params"
                         label-position="right"
                         label-width="120px"
                         size="mini"
                         style="margin-left:10px;margin-right:10px;"
                >
                    <el-row>
                        <el-col :span="8">

                            发货单编码: {{ form.params.soBillNo }}
                        </el-col>
                        <el-col :span="8">
                            上游单据编码： {{ form.params.orderNo }}
                        </el-col>
                        <el-col :span="8">
                            仓库编码:{{ form.params.whCode }}
                        </el-col>
                    </el-row>
                    <br>
                    <el-row>选择出库明细
                        <nodes-out-stock v-model="lineNo" :so-bill-id="this.soBillId"></nodes-out-stock>
                    </el-row>
                    <br>
                    <el-row>
                        <h3>出库明细</h3>
                    </el-row>
                    <el-row>
                        <template>
                            <el-table
                                :data="detailData"
                                border
                                highlight-current-row
                                style="width: 100%">
                                <el-table-column
                                    prop="soLineNo"
                                    label="行号"
                                    width="180">
                                </el-table-column>
                                <el-table-column
                                    prop="skuCode"
                                    label="物品编码"
                                    width="180">
                                </el-table-column>
                                <el-table-column
                                    prop="planQty"
                                    label="计划量">
                                </el-table-column>
                                <el-table-column
                                    prop="scanQty"
                                    label="已出量">
                                </el-table-column>
                                <el-table-column
                                    prop="surplusQty"
                                    label="剩余量">
                                </el-table-column>
                                <el-table-column
                                    prop="wsuCode"
                                    label="计量单位">

                                </el-table-column>
                                <el-table-column
                                    prop="skuLot1"
                                    label="批次号">

                                </el-table-column>
                                <el-table-column
                                    prop="skuLot2"
                                    label="专用客户">

                                </el-table-column>
                            </el-table>
                        </template>
                    </el-row>
                    <br>
                    <el-row style="overflow-y: auto">
                        <el-tabs v-model="activeName" @tab-click="handleClick" type="border-card">
                            <el-tab-pane :label="item.lable" :name="item.name" v-for="(item, index) in tabList"
                                         :key="index">
                                <el-table
                                    :data="table.data"
                                    show-summary
                                    :summary-method="getSummaries"
                                    border
                                    highlight-current-row
                                    size="mini">
                                    <el-table-column
                                        :align="'left'"
                                        width="160"
                                    >

                                        <template slot="header">
                                            <span>本次出库</span>
                                        </template>
                                        <template v-slot="{row}">
                                            <el-input-number
                                                v-model="row.outStockQty"
                                                style="width:130px"
                                                :max="getMax(row)"
                                                :min="0"
                                                controls-position="right"
                                                size="mini"
                                                @change="(val,oldValue)=>onChange(val,oldValue,row)">
                                                >
                                            </el-input-number>
                                        </template>
                                    </el-table-column>
                                    <template v-for="(column,index) in table.columnList">
                                        <el-table-column
                                            v-if="!column.hide"
                                            :key="index"
                                            show-overflow-tooltip
                                            v-bind="column">
                                        </el-table-column>
                                    </template>
                                    <el-table-column
                                        :align="'left'"
                                        width="200"
                                    >
                                        <template slot="header">
                                            <span class="d-table-header-required">序列号</span>
                                        </template>
                                        <template v-slot="{row}">
                                            <nodes-serial v-model="row.serailList" :stockId="row.stockId">
                                            </nodes-serial>
                                        </template>
                                    </el-table-column>
                                </el-table>
                            </el-tab-pane>
                        </el-tabs>
                    </el-row>
                </el-form>
            </el-main>
            <el-footer>
                <el-row style="margin-top: 10px;line-height:60px;text-align:right;">
                    <el-button
                        :loading="loading"
                        type="primary"
                        @click="onSubmit"
                    >
                        保 存
                    </el-button>
                    <el-button
                        :loading="loading"
                        @click="onClose"
                    >
                        关 闭
                    </el-button>
                </el-row>
            </el-footer>
        </el-container>
    </basic-container>
</template>

<script>
import {editDetailMixin} from "@/mixins/editDetail";
import NodesLocation from "@/components/wms/select/NodesLocation";
import {getSoDetailAndStock, getSoHeaderByPickPc, pickByPc} from "@/api/wms/outstock/soHeader";
import NodesOutStock from "@/components/wms/select/NodesOutStock";
import NodesSerial from "@/components/wms/select/NodesSerial";

export default {
    props: {
        soBillId: {type: String},
    },
    name: "edit",
    components: {
        NodesSerial,
        NodesOutStock,
        NodesLocation,
    },
    mixins: [editDetailMixin],
    data() {
        return {
            tabList: [
                {lable: '按件拣货', name: 'pickByPiece'},
                {lable: '按箱拣货', name: 'pickByBox'},
            ],
            activeName: 'pickByPiece',
            table: {
                columnList: [
                    {
                        prop: 'stockEnable',
                        label: '可用库存',
                    },
                    {
                        prop: 'stockBalance',
                        label: '余额',
                    },
                    {
                        prop: 'skuCode',
                        label: '物品编码',
                    },
                    {
                        prop: 'skuLot1',
                        label: '批次',
                    },
                    {
                        prop: 'locCode',
                        label: '库位',
                    },
                    {
                        prop: 'zoneCode',
                        label: '库区',
                    },
                    {
                        prop: 'whCode',
                        label: '库房',
                    },
                    {
                        prop: 'boxCode',
                        label: '箱码',
                    },
                    {
                        prop: 'lpnCode',
                        label: 'lpn',
                    },


                ]
            },


            detailData: [],
            stockData: [],
            lineNo: '',

            rowObject: {
                lineNumber: '',
                scanQty: 0,
            },
            rowData: [],
            refresh: true,
            form: {
                params: {
                    soBillNo: '',
                    orderNo: '',
                    whCode: ''
                },

            }
        }
    },
    watch: {
        lineNo(newVal) {
            this.getDetailData(newVal)
        }
    },
    created() {
        this.getHeaderData()
    },
    methods: {
        getDescriptor() {
            return {};
        },
        handleClick() {

        },
        getMax(row) {
            if (this.detailData[0].surplusQty + row.outStockQty < row.stockBalance + row.outStockQty) {
                return this.detailData[0].surplusQty + row.outStockQty
            }
            return row.stockEnable + row.outStockQty
        },

        onChange(val, oldVal, row) {
            let a = val - oldVal
            row.stockEnable = row.stockEnable - a
            this.detailData[0].scanQty = this.detailData[0].scanQty + a
            this.detailData[0].surplusQty = this.detailData[0].surplusQty - a
        },


        // 过滤空白行
        filterBlankRow(row) {
            return !(
                row.outStockQty === 0
            );
        },
        checkDetails() {
            let tableData = this.table.data.filter(d => this.filterBlankRow(d));
            if (tableData.length === 0) {
                this.$message.warning("没有可以提交的数据");
                return;
            }
            for (const row of tableData) {
                if (!row.hasSerail) {
                    continue;
                }
                if (row.outStockQty !== row.serailList.length) {
                    this.$message.warning("出库数量与序列号选择数量不一致");
                    return false;
                }
            }
            this.table.postData = tableData;
            return true;
        },
        getSummaries(param) {
            const {columns, data} = param;
            const sums = [];
            columns.forEach((column, index) => {
                if (index === 0) {
                    sums[index] = '合计';
                    return;
                }
                if (index === 1 || index === 2) {
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

        getDetailData(lineNo) {
            let soDetailAndStockRequest = {
                soBillId: this.soBillId,
                soLineNo: lineNo,
                whId: this.form.params.whId
            };
            getSoDetailAndStock(soDetailAndStockRequest)
                .then((res) => {
                    let detailObj = res.data.data
                    this.detailData.length = 0
                    this.detailData.push(detailObj.pickByPcSoDetailResponse)
                    this.table.data = detailObj.stockResponseList;
                })
        },
        getHeaderData() {
            let skuUmSelectQuery = {
                soBillId: this.soBillId
            };
            getSoHeaderByPickPc(skuUmSelectQuery)
                .then((res) => {
                    let pageObj = res.data.data;
                    this.form.params = pageObj
                })

        },


        submitFormParams() {
            let pickByPcStockDtoList = this.table.postData
            let pickByPcRequest = {soBillId: this.soBillId, pickByPcStockDtoList}
            return pickByPc(pickByPcRequest)
                .then(res => {
                    return {
                        msg: res.data.data,
                        router: {
                            path: '/wms/outstock/soHeader',
                            query: {
                                isRefresh: 'true'
                            }
                        }
                    };
                });
        },

    }
}
</script>

<style scoped>
.d-table-header-required:before {
    content: "*";
    color: #F56C6C;
    margin-right: 4px;
}
</style>
