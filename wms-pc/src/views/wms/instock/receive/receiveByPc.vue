<template>
    <basic-container>
        <el-container id="container" v-loading="loading">
            <el-main style="overflow: hidden;overflow-y: scroll;">
                <el-form ref="form"
                         :model="form.params.newReceiveHeaderRequest"
                         :rules="form.rules"
                         label-position="right"
                         label-width="120px"
                         size="mini"
                         style="margin-left:10px;margin-right:10px;"
                >
                    <el-row>
                        <el-col :span="8">
                            收货单编码: {{form.params.receiveNo}}
                        </el-col>
                        <el-col :span="8">
                            上游单据编码： {{form.params.externalOrderNo}}
                        </el-col>
                        <el-col :span="8">
                            仓库编码:{{form.params.whCode}}
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
                                            v-model="row.skuLot5"
                                            @keyup.enter.native="aa"
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
                                            v-model="row.sku.skuSpec"
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
                                            v-model="row.planQty"
                                            style="width: 80px"
                                            :min="0"
                                            controls-position="right"
                                            size="mini"></el-input-number>
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
                                            v-model="row.sku.skuSpec"
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
                                            v-model="row.sku.skuSpec"
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
                                        <nodes-location v-model="row.skuLot1"></nodes-location>
                                    </template>
                                </el-table-column>
                                <el-table-column width="130">
                                    <template slot="header">
                                        <span>箱码</span>
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
                                        <span>LPN</span>
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
                                        <span>序列号</span>
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

                                <el-table-column width="130">
                                    <template slot="header">
                                        <span class="d-table-header-required">备注</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input v-model="row.remark" size="mini"></el-input>
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
import {addReceive, getEditReceiveById, getReceiveByPc} from "@/api/wms/instock/receive";
import NodesLocation from "@/components/wms/select/NodesLocation";

export default {
    props: {
        receiveId: {type: String},
    },
    name: "edit",
    components: {
        NodesLocation,
    },
    mixins: [editDetailMixin],
    data() {
        return {
            refresh: true,
            form: {
                params: {
                   receiveNo:'',
                    externalOrderNo:'',
                    whCode:''
                },

            }
        }
    },
    created() {
         this.getTableData()
    },
    methods: {
        aa() {
            alert("回车事件")
        },

        // 过滤空白行
        filterBlankRow(row) {
            return !(
                (func.isEmpty(row.sku.skuId)
                    && func.isEmpty(row.sku.skuCode)
                    && func.isEmpty(row.sku.skuName))
                && row.planQty === 0
            );
        },
        getTableData() {
            if (func.isEmpty(this.receiveId)){
                return;
            }

            let skuUmSelectQuery = {
                receiveId: this.receiveId
            };
            getReceiveByPc(skuUmSelectQuery)
                .then((res) => {
                    let pageObj = res.data.data;
                    this.form.params = pageObj.receiveHeaderResponse;
                })

        },
        getDescriptor() {
            const skuErrorMsg = '请选择物品编码';
            return {
                sku: {
                    type: 'object',
                    required: true,
                    fields: {
                        skuId: {required: true, message: skuErrorMsg},
                        skuCode: {required: true, message: skuErrorMsg},
                        skuName: {required: true, message: skuErrorMsg},

                    }
                },
                planQty: {type: 'Number', validator: (rule, value) => value > 0, message: '计划数量不能为0'}

            };
        },
        createRowObj() {
            return {
                skuCode:'',
                lineNumber: '',
                sku: {
                    skuId: '',
                    skuCode: '',
                    skuName: '',
                    skuSpec: ''
                },
                umCode: '',
                planQty: 0,
                remark: '',
                skuSpec: '',
                skuLot1: '',
                skuLot4: '',
                skuLot5: '',
                skuLot6: '',
                skuLot8: '',
            }
        },


        onChangeSku(row) {

        },
        submitFormParams() {
            this.form.params.newReceiveDetailRequestList = this.table.postData
            return addReceive(this.form.params)
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
