<template>
    <basic-container>
        <el-container id="container" v-loading="loading">
            <el-main style="overflow: hidden;overflow-y: scroll;">
                <el-form ref="form"
                         :model="form.params"
                         label-position="right"
                         label-width="120px"
                         size="mini"
                         style="margin-left:10px;margin-right:10px;">
                    <el-row>
                        <el-col :span="8">
                            收货单编码: {{ form.params.receiveNo }}
                        </el-col>
                        <el-col :span="8">
                            上游单据编码： {{ form.params.externalOrderNo }}
                        </el-col>
                        <el-col :span="8">
                            仓库编码:{{ form.params.whCode }}
                        </el-col>
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
                                    width="53">
                                    <template slot="header">
                                        <el-button
                                            circle
                                            icon="el-icon-plus"
                                            size="mini"
                                            type="primary"
                                            @click="onAddBatchRow">
                                        </el-button>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-button size="small" type="text" @click="onReset(row)">清空</el-button>
                                    </template>
                                </el-table-column>
                                <el-table-column width="100">
                                    <template slot="header">
                                        <span>行号</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model="row.lineNumber"
                                            size=mini
                                            @blur="getDetailData(row)"
                                            @focus="getFocus(row)"
                                            @keyup.enter.native="getDetailData(row)">
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                    prop="skuCode"
                                    width="195">
                                    <template slot="header">
                                        <span class="d-table-header-required">物品编码</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model="row.skuCode"
                                            :disabled="true"
                                            size=mini>
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column width="130">
                                    <template slot="header">
                                        <span class="d-table-header-required">规格</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model.trim="row.skuLot2"
                                            :disabled="true"
                                            size=mini>
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                    prop="planQty"
                                    width="160">
                                    <template slot="header">
                                        <span class="d-table-header-required">本次收货量</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input-number
                                            v-model="row.scanQty"
                                            :disabled="exist(row)"
                                            :max="Number(row.surplusQty+row.scanQty)"
                                            :min="0"
                                            controls-position="right"
                                            size="mini"
                                            style="width:130px"
                                            @change="(val,oldValue)=>onChange(val,oldValue,row)">
                                        </el-input-number>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                    :align="'left'"
                                    prop="skuCode"
                                    width="150">
                                    <template slot="header">
                                        <span class="d-table-header-required">剩余数量</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model.trim="row.surplusQty"
                                            :disabled="true"
                                            size=mini>
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                    :align="'left'"
                                    prop="skuCode"
                                    width="110">
                                    <template slot="header">
                                        <span class="d-table-header-required">计量单位</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model.trim="row.umCode"
                                            :disabled="true"
                                            size=mini>
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                    :align="'left'"
                                    prop="skuCode"
                                    width="180">
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
                                            v-model.trim="row.boxCode"
                                            size=mini>
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column width="130">
                                    <template slot="header">
                                        <span>LPN</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model.trim="row.lpnCode"
                                            size=mini>
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column width="130">
                                    <template slot="header">
                                        <span class="d-table-header-required">生产批次</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model.trim="row.skuLot1"
                                            size=mini>
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column width="130">
                                    <template slot="header">
                                        <span>客户</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model.trim="row.skuLot4"
                                            size=mini>
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column width="130">
                                    <template slot="header">
                                        <span>钢背批次</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model.trim="row.skuLot5"
                                            size=mini>
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column width="130">
                                    <template slot="header">
                                        <span>摩擦块批次</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model.trim="row.skuLot6"
                                            size=mini>
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column width="130">
                                    <template slot="header">
                                        <span>适用速度等级</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model.trim="row.skuLot8"
                                            size=mini>
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column width="700">
                                    <template slot="header">
                                        <span>序列号</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input v-model.trim="row.snCode"
                                                  size=mini v-if="billTypeCd !=='归还入库' || row.snNumber===''">
                                        </el-input>

                                        <nodes-serial-query v-model="row.snCodeList"
                                                            :receiveDetailId="row.receiveDetailId"
                                                            v-if="billTypeCd ==='归还入库' && row.snNumber!==''">
                                        </nodes-serial-query>
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
                        @click="onSave">
                        保 存
                    </el-button>
                    <el-button
                        :loading="loading"
                        @click="onClose">
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
import {getReceiveByPc, getReceiveDetailByPc, ReceiveByPc} from "@/api/wms/instock/receive";
import NodesLocation from "@/components/wms/select/NodesLocation";
import NodesSerialQuery from "@/components/wms/select/NodesSerialQuery";

export default {
    props: {
        receiveId: {type: String},
        billTypeCd: {type: String}
    },
    name: "edit",
    components: {
        NodesLocation,
        NodesSerialQuery
    },
    mixins: [editDetailMixin],
    data() {
        return {
            rowObject: {
                lineNumber: '',
                scanQty: 0,
            },
            rowData: [],
            refresh: true,
            form: {
                params: {
                    receiveId: "",
                    receiveNo: '',
                    externalOrderNo: '',
                    whCode: ''
                },

            },
            rowLineArr: [],
        }
    },
    created() {
        this.getTableData()
    },
    watch: {
        receiveId() {
            this.table.data.find(u => {
                Object.keys(u).forEach(key => {
                    u[key] = ''
                })
            });
            this.rowData = []
            this.rowObject = {lineNumber: '', scanQty: 0}
            this.getTableData()
        }
    },
    methods: {
        getFocus(row) {
            this.rowObject.lineNumber = row.lineNumber
            this.rowObject.scanQty = row.scanQty
        },
        exist(row) {
            return func.isEmpty(row.lineNumber);
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
            if (row.snCodeList.length > 0) {
                row.snCode = row.snCodeList.join(',')
            }
            return !(
                func.isEmpty(row.lineNumber)
            );
        },
        getDetailData(row) {
            if (func.isEmpty(row.lineNumber) && func.isNotEmpty(row.skuCode)) {
                row.lineNumber = this.rowObject.lineNumber
                return
            }
            if (func.isEmpty(row.lineNumber)) {
                return
            }
            let a = this.rowData
            let column = a.find(u => {
                if (u.lineNo === row.lineNumber) {
                    return u
                }
            });
            if (func.isNotEmpty(this.rowObject) && row.lineNumber !== this.rowObject.lineNumber) {
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
                row.snNumber = column.snNumber
                row.skuLot1 = column.skuLot1
                row.skuLot2 = column.skuLot2
                row.skuLot4 = column.skuLot4
                row.skuLot5 = column.skuLot5
                row.skuLot6 = column.skuLot6
                row.skuLot8 = column.skuLot8
                row.snCodeList = []
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
                    row.snNumber = item.snNumber
                    row.skuLot1 = item.skuLot1
                    row.skuLot2 = item.skuLot2
                    row.skuLot4 = item.skuLot4
                    row.skuLot5 = item.skuLot5
                    row.skuLot6 = item.skuLot6
                    row.skuLot8 = item.skuLot8
                    row.snCodeList = []
                    this.rowData.push(item)
                })
        },
        getTableData() {
            let skuUmSelectQuery = {
                receiveId: this.receiveId
            };
            getReceiveByPc(skuUmSelectQuery)
                .then((res) => {
                    this.form.params = res.data.data
                })
        },
        onReset(row) {
            this.rowData.find(u => {
                if (u.lineNo === row.lineNumber) {
                    u.surplusQty = u.surplusQty + row.scanQty
                }
            });
            let data = this.table.data
            data.find(u => {
                if (u.lineNumber === row.lineNumber) {
                    u.surplusQty = u.surplusQty + row.scanQty
                }
            });
            Object.keys(row).forEach(key => {
                row[key] = ''
                row.scanQty = 0
                row.surplusQty = 0
            })
        },
        getDescriptor() {
            return {
                scanQty: {type: 'Number', validator: (rule, value) => value > 0, message: '本次收货数量不能为0'},
                locId: {required: true, message: '库位不能为空'},
                skuLot2: {required: true, message: '规格不能为空'}
            }
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
                snNumber: '',
                snCodeList: [],
                skuLot1: '',
                skuLot2: '',
                skuLot4: '',
                skuLot5: '',
                skuLot6: '',
                skuLot8: '',
            }
        },
        onSave() {
            let detailList = this.table.data.filter(d => this.filterBlankRow(d));
            for (const key in detailList) {
                let filterList = detailList
                    .filter(x => x.boxCode === detailList[key].boxCode && x.locId !== detailList[key].locId);
                if (filterList.length > 0) {
                    this.$message.warning("相同箱码只能放在同一库位上");
                    return
                }
            }
            let detailRequestList = detailList
            for (let y = 0; y < detailRequestList.length; y++) {
                if (this.billTypeCd === '归还入库' && detailRequestList[y].snNumber !== '') {
                    if (detailRequestList[y].snCodeList.length != detailRequestList[y].scanQty) {
                        this.$message.warning("行号" + detailRequestList[y].lineNumber + "序列号选择数量与收货数量不一致");
                        return
                    }
                    if (this.rowLineArr.indexOf(detailRequestList[y].lineNumber) == -1) {
                        let b = []
                        b = detailList.filter(x => x.lineNumber === detailRequestList[y].lineNumber)
                            .map(item => item.snCodeList).reduce(function (a, b) {
                                return a.concat(b)
                            });
                        if (new Set(b).size != b.length) {
                            this.$message.warning("行号" + detailRequestList[y].lineNumber + "序列号重复");
                            return
                        }
                    }
                }
                this.rowLineArr.push(detailRequestList[y].lineNumber)
            }
            this.onSubmit();
        },
        submitFormParams() {
            let detailRequestList = this.table.postData
            let receiveByPcRequest = {receiveId: this.receiveId, detailRequestList}
            return ReceiveByPc(receiveByPcRequest).then(res => {
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
