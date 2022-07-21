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
                                style="width: 100%">
                                <el-table-column
                                    prop="soLineNo"
                                    label="行号"
                                    width="180">
                                    <el-input
                                        size=mini
                                        :disabled="true">
                                    </el-input>
                                </el-table-column>
                                <el-table-column
                                    prop="skuCode"
                                    label="物品编码"
                                    width="180">
                                    <el-input
                                        size=mini
                                        :disabled="true">
                                    </el-input>
                                </el-table-column>
                                <el-table-column
                                    prop="planQty"
                                    label="计划量">
                                    <el-input
                                        size=mini
                                        :disabled="true">
                                    </el-input>
                                </el-table-column>
                                <el-table-column
                                    prop="address"
                                    label="已出量">
                                    <el-input
                                        size=mini
                                        :disabled="true">
                                    </el-input>
                                </el-table-column>
                                <el-table-column
                                    prop="address"
                                    label="剩余量">
                                    <el-input
                                        size=mini
                                        :disabled="true">
                                    </el-input>
                                </el-table-column>
                                <el-table-column
                                    prop="address"
                                    label="计量单位">
                                    <el-input
                                        size=mini
                                        :disabled="true">
                                    </el-input>
                                </el-table-column>
                                <el-table-column
                                    prop="address"
                                    label="批次号">
                                    <el-input
                                        size=mini
                                        :disabled="true">
                                    </el-input>
                                </el-table-column>
                            </el-table>
                        </template>
                    </el-row>

                    <el-row style="overflow-y: auto">
                        <el-tabs v-model="activeName" @tab-click="handleClick" type="border-card">
                            <el-tab-pane :label="item.lable" :name="item.name" v-for="(item, index) in tabList"
                                         :key="index">
                                <el-table
                                    :data="table.data"
                                    border
                                    highlight-current-row
                                    size="mini">
                                    <el-table-column
                                        :align="'left'"
                                        prop="skuCode"
                                        width="150"
                                    >

                                        <template slot="header">
                                            <span>本次出库</span>
                                        </template>
                                        <template v-slot="{row}">
                                            <el-input-number
                                                v-model="row.outStockQty"
                                                style="width:130px"
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
import func from "@/util/func";
import {ReceiveByPc} from "@/api/wms/instock/receive";
import NodesLocation from "@/components/wms/select/NodesLocation";
import {getSoDetailAndStock, getSoHeaderByPickPc} from "@/api/wms/outstock/soHeader";
import NodesOutStock from "@/components/wms/select/NodesOutStock";

export default {
    props: {
        soBillId: {type: String},
    },
    name: "edit",
    components: {
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
                        prop: 'stockEnableQty',
                        label: '可用库存',
                    },
                    {
                        prop: 'stockBalanceQty',
                        label: '余额',
                    },
                    {
                        prop: 'skuCode',
                        label: '物品编码',
                    },
                    {
                        prop: 'lotNumber',
                        label: '批次号',
                    },
                    {
                        prop: 'lotNumber',
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
        handleClick() {

        },
        getFocus(row) {
            this.rowObject.lineNumber = row.lineNumber
            this.rowObject.scanQty = row.scanQty
        },
        exist(row) {
            if (func.isEmpty(row.lineNumber)) {
                return true
            }
            return false
        },
        onChange(val, oldVal, row) {
            let data = this.table.data
            let a = val - oldVal
            row.surplusQty = row.surplusQty - a
            data.find(u => {
                if (u.lineNumber === row.lineNumber) {
                    u.surplusQty = row.surplusQty
                }

            });
            this.rowData.find(u => {
                if (u.lineNo === row.lineNumber) {
                    u.surplusQty = row.surplusQty
                }

            });
        },

        // 过滤空白行
        filterBlankRow(row) {
            return !(
                func.isEmpty(row.lineNumber)
            );
        },
        getDetailData(lineNo) {
            let soDetailAndStockRequest = {
                soBillId: this.soBillId,
                soLineNo: lineNo
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
        getDescriptor() {
            return {
                scanQty: {type: 'Number', validator: (rule, value) => value > 0, message: '计划数量不能为0'},
                locId: {required: true, message: '库位不能为空'}
            };
        },


        submitFormParams() {
            let detailRequestList = this.table.postData
            let receiveByPcRequest = {receiveId: this.receiveId, detailRequestList}
            return ReceiveByPc(receiveByPcRequest)
                .then(res => {
                    return {
                        msg: res.data.msg,
                        router: {
                            path: '/wms/instock/receive',
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
