<template>
    <basic-container>
        <el-container id="container" v-loading="loading">
            <el-main style="overflow: hidden;overflow-y: scroll;height: 100%">
                <el-form ref="form"
                         :model="form.params"
                         label-position="right"
                         label-width="120px"
                         size="mini"
                         style="margin-left:10px;margin-right:10px;" >
                    <el-row>
                        <h3>收货订单</h3>
                    </el-row>
                    <el-descriptions
                        :column="4"
                        border
                        class="margin-top"
                        style="margin-bottom: 20px">
                        <el-descriptions-item
                            label-class-name="descriptions-label"
                            content-class-name="descriptions-content"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                                单据编码
                            </template>
                            {{ form.params.receiveNo }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            label-class-name="descriptions-label"
                            content-class-name="descriptions-content"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                                单据状态
                            </template>
                            {{ form.params.billStateDesc }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            label-class-name="descriptions-label"
                            content-class-name="descriptions-content"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                                库房
                            </template>
                            {{ form.params.whCode }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            label-class-name="descriptions-label"
                            content-class-name="descriptions-content"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                                货主
                            </template>
                            {{ form.params.ownerName }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            label-class-name="descriptions-label"
                            content-class-name="descriptions-content"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                                单据类型
                            </template>
                            {{ form.params.billTypeName }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            label-class-name="descriptions-label"
                            content-class-name="descriptions-content"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                                入库方式
                            </template>
                            {{ form.params.inStoreTypeDesc }}
                        </el-descriptions-item>
                        <el-descriptions-item v-if="false"
                            label-class-name="descriptions-label"
                            content-class-name="descriptions-content"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                                ASN编码
                            </template>
                            {{ form.params.asnBillNo }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            v-if="form.params.billTypeCd !== this.$commonConst.BILL_TYPE_RETURN"
                            label-class-name="descriptions-label"
                            content-class-name="descriptions-content"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                                供应商编码
                            </template>
                            {{ form.params.supplierCode }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            v-if="form.params.billTypeCd !== this.$commonConst.BILL_TYPE_RETURN"
                            label-class-name="descriptions-label"
                            content-class-name="descriptions-content"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                                供应商名称
                            </template>
                            {{ form.params.supplierName }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            v-if="form.params.billTypeCd === this.$commonConst.BILL_TYPE_RETURN"
                            label-class-name="descriptions-label"
                            content-class-name="descriptions-content"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                                归还人
                            </template>
                            {{ form.params.supplierContact }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            :span="4"
                            label-class-name="descriptions-label"
                            content-class-name="descriptions-content"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                                文件编码
                            </template>
                            {{ form.params.udf1 }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            label-class-name="descriptions-label"
                            content-class-name="descriptions-content"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                                备注
                            </template>
                            {{ form.params.remark }}
                        </el-descriptions-item>
                    </el-descriptions>
                </el-form>
                <el-tabs v-model="activeName" @tab-click="handleClick" type="border-card">
                    <el-tab-pane :label="item.lable" :name="item.name" v-for="(item, index) in tabList" :key="index">
                        <el-table
                            :data="table.data"
                            border
                            highlight-current-row
                            size="mini">
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
            </el-main>
            <el-footer>
                <el-row style="margin-top: 10px;line-height:60px;text-align:right;">
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
import NodesInStoreMode from "@/components/wms/select/NodesInStoreMode";
import NodesSku from "@/components/wms/select/NodesSku";
import NodesLineNumber from "@/components/wms/table/NodesLineNumber";
import {editMixin} from "@/mixins/edit";
import {listMixin} from "@/mixins/list";
import {getLogList, getReceiveDetailById, getReceiveLogList} from "@/api/wms/instock/receive";
import func from "@/util/func";

export default {
    props: {
        receiveId: {type: String},
    },
    name: "receiveDetails",
    components: {NodesLineNumber, NodesSku, NodesInStoreMode},
    mixins: [editMixin, listMixin],
    data() {
        return {
            that: this,
            page: {
                total: 0,
                size: 20,
                current: 1,
                ascs: "", //正序字段集合
                descs: "", //倒序字段集合
            },
            form: {
                params: {
                    receiveNo: '',
                    asnBillNo: '',
                    whCode: '',
                    billStateDesc: '',
                    ownerCode: '',
                    inStoreTypeDesc: '',
                    remark: '',
                    supplierCode: '',
                    supplierName: '',
                    billTypeCd: '',
                    billTypeName: '',
                    udf1: null,
                    supplierContact: '',
                }
            },
            tabList: [
                {lable: '收货单明细', name: 'receiveDetail'},
                {lable: '清点记录', name: 'receiveLog'},
                {lable: '操作日志', name: 'Log'}
            ],
            //tab标签默认打开第一个
            activeName: 'receiveDetail',
            receiveLogData: [],
            receiveDetailData: [],
            LogData: [],
            receiveLogList: [
                {
                    prop: 'lineNo',
                    label: '行号',
                },
                {
                    prop: 'qty',
                    label: '数量',
                },
                {
                    prop: 'lpnCode',
                    label: 'lpn编码',
                },
                {
                    prop: 'boxCode',
                    label: '箱码',
                },
                {
                    prop: 'snCode',
                    label: '序列号',
                },
                {
                    prop: 'locCode',
                    label: '库位编码',
                },
                {
                    prop: 'skuCode',
                    label: '物品编码',
                },
                {
                    prop: 'skuName',
                    label: '物品名称',
                },
                {
                    prop: 'skuLevel',
                    label: '包装层级',
                },
                {
                    prop: 'skuSpec',
                    label: '规格',
                },
                {
                    prop: 'whCode',
                    label: '库房编码',
                },
                {
                    prop: 'ownerCode',
                    label: '货主编码',
                },
                {
                    prop: 'createUser',
                    label: '创建人',
                },
                {
                    prop: 'createTime',
                    label: '创建时间',
                },
            ],
            logList: [
                {
                    prop: 'userAccount',
                    label: '操作人员账号',
                    align: 'center'
                },
                {
                    prop: 'userRealName',
                    label: '操作人员姓名',
                    align: 'center'
                },
                {
                    prop: 'log',
                    label: '操作内容',
                    align: 'center'
                },
                {
                    prop: 'createTime',
                    label: '操作时间',
                    align: 'center'
                },
            ],
            receiveDetailList: [
                {
                    prop: 'lineNo',
                    label: '行号',
                },
                {
                    prop: 'skuCode',
                    label: '物品编码',
                    width: 150,
                },
                {
                    prop: 'skuName',
                    label: '物品名称',
                    width: 130,
                },
                {
                    prop: 'planQty',
                    label: '计划数量',
                },
                {
                    prop: 'scanQty',
                    label: '已收数量',
                },
                {
                    prop: 'surplusQty',
                    label: '剩余数量',
                },
                {
                    prop: 'umName',
                    label: '计量单位'
                },
                {
                    prop: 'detailStatus',
                    label: '状态'
                },
                {
                    prop: 'skuLot1',
                    width: 120,
                    label: '生产批次'
                },
                {
                    prop: 'skuLot4',
                    width: 120,
                    label: '客户'
                },
                {
                    prop: 'skuLot5',
                    width: 120,
                    label: '钢背批次'
                },
                {
                    prop: 'skuLot6',
                    width: 120,
                    label: '摩擦块批次'
                },
                {
                    prop: 'skuLot8',
                    width: 120,
                    label: '适用速度等级'
                },
                {
                    prop: 'remark',
                    width: 120,
                    label: '备注'
                },
            ],
            table: {
                columnList: []
            },
        }
    },
    watch: {
        receiveId() {
            this.getTableData();
        }
    },
    created() {
        this.getTableData();
    },
    methods: {
        handleClick(tab) {
            if (tab.name === 'receiveDetail') {
                this.table.columnList = this.receiveDetailList
                this.table.data = this.receiveDetailData
            } else if (tab.name === 'receiveLog') {
                this.getReceiveLogList()
            } else {
                this.getLogList();
            }
        },
        getTableData() {
            this.table.columnList = this.receiveDetailList;
            if (func.isEmpty(this.receiveId)) {
                return;
            }
            let ReceiveIdRequest = {
                receiveId: this.receiveId
            }
            getReceiveDetailById(ReceiveIdRequest)
                .then((res) => {
                    let obj = res.data.data;
                    this.form.params = obj.receiveHeaderResponse;
                    this.receiveDetailData = obj.detailList;
                    this.table.data = obj.detailList;
                })
        },
        getReceiveLogList() {
            this.table.columnList = this.receiveLogList
            if (func.isEmpty(this.receiveLogData)) {
                getReceiveLogList(this.receiveId)
                    .then((res) => {
                            this.receiveLogData = res.data.data
                            this.table.data = res.data.data
                        }
                    )
            } else {
                this.table.data = this.receiveLogData
            }
        },
        getLogList() {
            this.table.columnList = this.logList
            if (func.isEmpty(this.LogData)) {
                getLogList(this.receiveId)
                    .then((res) => {
                            this.LogData = res.data.data
                            this.table.data = res.data.data
                        }
                    )
            } else {
                this.table.data = this.LogData
            }

        },
        getCrudColumnList(){

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

/deep/ .descriptions-label {
    width: 150px;
}

/deep/ .descriptions-content {
    width: 250px;
}
</style>
