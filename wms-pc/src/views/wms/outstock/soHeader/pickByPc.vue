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
                    <el-row>
                        <h3>出库明细</h3>
                    </el-row>
                    <el-row>
                        <template>
                            <el-table
                                :data="tableData"
                                style="width: 100%">
                                <el-table-column
                                    prop="date"
                                    label="日期"
                                    width="180">
                                </el-table-column>
                                <el-table-column
                                    prop="name"
                                    label="姓名"
                                    width="180">
                                </el-table-column>
                                <el-table-column
                                    prop="address"
                                    label="地址">
                                </el-table-column>
                            </el-table>
                        </template>
                    </el-row>
                    <el-row>
                        <h3>明细</h3>
                    </el-row>
                    <el-row style="overflow-y: auto">
                        <el-col>
                            <el-table
                                ref="table"
                                :data="table.data"
                                border
                                size="mini">
                                <el-table-column
                                    width="53"
                                >
                                    <template slot="header">
                                        <el-button circle
                                                   icon="el-icon-plus"
                                                   size="mini"
                                                   type="primary"
                                                   @click="onAddBatchRow">
                                        </el-button>
                                    </template>
                                </el-table-column>
                                <el-table-column width="100">
                                    <template slot="header">
                                        <span>行号</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            size=mini
                                            v-model="row.lineNumber"
                                            @blur="getDetailData(row)"
                                            @focus="getFocus(row)"
                                            @keyup.enter.native="getDetailData(row,$event)"
                                        >
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                    :align="'left'"
                                    prop="skuCode"
                                    width="195"
                                >

                                    <template slot="header">
                                        <span class="d-table-header-required">物品编码</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            size=mini
                                            v-model="row.skuCode"
                                            :disabled="true">
                                        </el-input>

                                    </template>
                                </el-table-column>
                                <el-table-column
                                    prop="planQty"
                                    width="120"
                                >
                                    <template slot="header">
                                        <span class="d-table-header-required">本次收货量</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input-number
                                            v-model="row.scanQty"
                                            :disabled="exist(row)"
                                            style="width: 80px"
                                            :min="0"
                                            :max="row.surplusQty+row.scanQty"
                                            controls-position="right"
                                            size="mini"
                                            @change="(val,oldValue)=>onChange(val,oldValue,row)">
                                            >
                                        </el-input-number>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                    :align="'left'"
                                    prop="skuCode"
                                    width="100"
                                >

                                    <template slot="header">
                                        <span class="d-table-header-required">剩余数量</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            size=mini
                                            v-model="row.surplusQty"
                                            :disabled="true">
                                        </el-input>

                                    </template>
                                </el-table-column>

                                <el-table-column
                                    :align="'left'"
                                    prop="skuCode"
                                    width="110"
                                >
                                    <template slot="header">
                                        <span class="d-table-header-required">计量单位</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            size=mini
                                            v-model="row.umCode"
                                            :disabled="true">
                                        </el-input>
                                    </template>
                                </el-table-column>

                                <el-table-column
                                    :align="'left'"
                                    prop="skuCode"
                                    width="180"
                                >
                                    <template slot="header">
                                        <span class="d-table-header-required">库位编码</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <nodes-location v-model="row.locId"></nodes-location>
                                    </template>
                                </el-table-column>
                                <el-table-column width="130">
                                    <template slot="header">
                                        <span>箱码</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            size=mini
                                            v-model="row.boxCode"
                                        >
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column width="130">
                                    <template slot="header">
                                        <span>LPN</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            size=mini
                                            v-model="row.lpnCode"
                                        >
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column width="130">
                                    <template slot="header">
                                        <span>序列号</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            size=mini
                                            v-model="row.snCode"
                                        >
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column width="130">
                                    <template slot="header">
                                        <span>生产批次</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            size=mini
                                            v-model="row.skuLot1"
                                        >
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column width="130">
                                    <template slot="header">
                                        <span>客户</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            size=mini
                                            v-model="row.skuLot4"
                                        >
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column width="130">
                                    <template slot="header">
                                        <span>钢背批次</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            size=mini
                                            v-model="row.skuLot5"
                                        >
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column width="130">
                                    <template slot="header">
                                        <span>摩擦块批次</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            size=mini
                                            v-model="row.skuLot6"
                                        >
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column width="130">
                                    <template slot="header">
                                        <span>CRCC</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            size=mini
                                            v-model="row.skuLot8"
                                        >
                                        </el-input>
                                    </template>
                                </el-table-column>


                            </el-table>
                        </el-col>
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
import {getReceiveDetailByPc, ReceiveByPc} from "@/api/wms/instock/receive";
import NodesLocation from "@/components/wms/select/NodesLocation";
import {getSoHeaderByPickPc} from "@/api/wms/outstock/soHeader";
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
            alert("ddgdgdgd")
        }
    },
    created() {
        this.getTableData()
    },
    methods: {
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
        getDetailData(row, $event) {
            if (func.isEmpty(row.lineNumber)) {
                return
            }
            let a = this.rowData
            let column = a.find(u => {
                if (u.lineNo === row.lineNumber) {
                    return u
                }
            });

            if (func.isNotEmpty(this.rowObject) && row.lineNumber != this.rowObject.lineNumber) {
                let data = this.table.data
                data.find(u => {
                    if (u.lineNumber === this.rowObject.lineNumber) {
                        u.surplusQty = u.surplusQty + this.rowObject.scanQty
                    }
                });
                this.rowObject.lineNumber = row.lineNumber
                this.rowObject.scanQty = 0

            }

            if (func.isNotEmpty(column)) {
                if (column.surplusQty === 0) {
                    this.$message.error('该行号剩余数量为0');
                    row.lineNumber = ''
                    return
                }
                row.receiveDetailId = column.receiveDetailId
                row.skuCode = column.skuCode
                row.surplusQty = column.surplusQty
                row.umCode = column.umCode
                row.skuLot1 = column.skuLot1
                row.skuLot4 = column.skulot4
                row.skuLot5 = column.skuLot5
                row.skuLot6 = column.skuLot6
                row.skuLot8 = column.skuLot8
                return
            }

            let skuUmSelectQuery = {
                lineNumber: row.lineNumber,
                receiveId: this.receiveId
            };

            getReceiveDetailByPc(skuUmSelectQuery)
                .then((res) => {
                    let item = res.data.data
                    if (func.isEmpty(item.lineNo)) {
                        this.$message.error('没有搜索到该行号');
                        Object.keys(row).forEach(key => {
                            row[key] = ''
                        })
                        return
                    }
                    row.receiveDetailId = item.receiveDetailId
                    row.skuCode = item.skuCode
                    row.surplusQty = item.surplusQty
                    row.umCode = item.umCode
                    row.scanQty = 0
                    row.skuLot1 = item.skuLot1
                    row.skuLot4 = item.skulot4
                    row.skuLot5 = item.skuLot5
                    row.skuLot6 = item.skuLot6
                    row.skuLot8 = item.skuLot8
                    this.rowData.push(item)
                })

        },
        getTableData() {
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
        createRowObj() {
            return {
                receiveDetailId: '',
                skuCode: '',
                lineNumber: '',
                scanQty: 0,
                surplusQty: 0,
                umCode: '',
                locId: '',
                boxCode: '',
                lpnCode: '',
                snCode: '',
                skuLot1: '',
                skuLot4: '',
                skuLot5: '',
                skuLot6: '',
                skuLot8: '',
            }
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
