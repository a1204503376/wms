<template>
    <basic-container>
        <el-container id="container" v-loading="loading">
            <el-main style="overflow: hidden;overflow-y: scroll;">
                <el-form ref="form"
                         :model="form.params"
                         :rules="form.rules"
                         label-position="right"
                         label-width="120px"
                         size="medium"
                         style="margin-left:10px;margin-right:10px;">
                    <el-row>
                        <h3>发货单新增</h3>
                    </el-row>
                    <el-row type="flex">
                        <el-col :span="8">
                            <el-form-item label="单据类型" prop="billTypeCd">
                                <nodes-bill-type
                                    v-model="form.params.billTypeCd"
                                    io-type="O"
                                    size="medium">
                                </nodes-bill-type>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="所属库房" prop="whId">
                                <nodes-warehouse
                                    v-model="form.params.whId"
                                    size="medium"
                                ></nodes-warehouse>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="所属货主" prop="woId">
                                <nodes-owner
                                    v-model="form.params.woId"
                                    :default-value="true"
                                    size="medium"
                                ></nodes-owner>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row type="flex">
                        <el-col :span="8">
                            <el-form-item
                                :label="form.params.billTypeCd !== this.$commonConst.BILL_TYPE_LEND ? '客户' : '借用人'"
                                :prop="form.params.billTypeCd !== this.$commonConst.BILL_TYPE_LEND ? 'customer' : 'contact'">
                                <nodes-customer
                                    v-if="form.params.billTypeCd !== this.$commonConst.BILL_TYPE_LEND"
                                    v-model="form.params.customer"
                                    size="medium">
                                </nodes-customer>
                                <el-input
                                    v-if="form.params.billTypeCd === this.$commonConst.BILL_TYPE_LEND"
                                    v-model="form.params.contact"
                                    :clearable="true"
                                    placeholder="请输入借用人"
                                    size="medium"
                                    style="width: 210px">
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="出库方式" prop="outstockType">
                                <nodes-dictionary
                                    v-model="form.params.outstockType"
                                    code="outstore_type"
                                    size="medium">
                                </nodes-dictionary>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="发货方式" prop="transportCode">
                                <nodes-dictionary
                                    v-model="form.params.transportCode"
                                    code="so_transport_code"
                                    size="medium">
                                </nodes-dictionary>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row style="width: 91%;" type="flex">
                        <el-col :span="24">
                            <el-form-item label="备注" prop="soBillRemark">
                                <el-input
                                    v-model="form.params.soBillRemark"
                                    :rows=2
                                    placeholder="请输入内容"
                                    type="textarea">
                                </el-input>
                            </el-form-item>
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
                                <el-table-column width="53">
                                    <template slot="header">
                                        <el-button
                                            circle
                                            icon="el-icon-plus"
                                            size="mini"
                                            type="primary"
                                            @click="onAddBatchRow">
                                        </el-button>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                    label="行号"
                                    prop="lineNumber"
                                    show-overflow-tooltip
                                    type="index"
                                    width="60">
                                    <template v-slot="scope">
                                        <nodes-line-number
                                            :index="scope.$index"
                                            @change="(val)=>{ scope.row.lineNumber = val; }">
                                        </nodes-line-number>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                    prop="sku"
                                    width="200">
                                    <template slot="header">
                                        <span class="d-table-header-required">物品编码</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <nodes-sku
                                            v-model="row.sku">
                                        </nodes-sku>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                    prop="skuName"
                                    width="200">
                                    <template slot="header">
                                        <span class="d-table-header-required">物品名称</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model="row.sku.skuName"
                                            :disabled="true"
                                            size=mini>
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="umCode" width="122">
                                    <template slot="header">
                                        <span class="d-table-header-required">计量单位</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <nodes-sku-um
                                            v-model="row.umCode"
                                            :sku="row.sku"
                                            style="width: 100px">
                                        </nodes-sku-um>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="skuSpec" width="122">
                                    <template slot="header">
                                        <span class="d-table-header-required">物品规格</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <nodes-sku-spec
                                            v-model="row.skuSpec"
                                            :sku="row.sku"
                                            style="width: 100px">
                                        </nodes-sku-spec>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="planQty" width="152">
                                    <template slot="header">
                                        <span class="d-table-header-required">计划数量</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input-number
                                            v-model="row.planQty"
                                            :min="0"
                                            controls-position="right"
                                            size="mini">
                                        </el-input-number>
                                    </template>
                                </el-table-column>
                                <el-table-column :align="'left'" prop="生产批次">
                                    <template slot="header">
                                        <span>生产批次</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model="row.skuLot1"
                                            size=mini>
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column :align="'left'" prop="专用客户">
                                    <template slot="header">
                                        <span>专用客户</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model="row.skuLot4"
                                            size=mini>
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="remark">
                                    <template slot="header">
                                        <span>备注</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input v-model.trim="row.remark" size="mini"></el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column align="center" width="100px">
                                    <template slot="header">
                                        <span>操作</span>
                                    </template>
                                    <template v-slot="scope">
                                        <el-link type="primary"
                                                 @click.native.prevent="deleteRow(scope.$index, table.data)">删除
                                        </el-link>
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
                        @click="onSubmit">
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

import NodesSku from "@/components/wms/select/NodesSku";
import NodesLineNumber from "@/components/wms/table/NodesLineNumber";
import {editDetailMixin} from "@/mixins/editDetail";
import func from "@/util/func";
import NodesBillType from "@/components/wms/select/NodesBillType";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import {add} from '@/api/wms/outstock/soHeader';
import NodesSkuUm from "@/components/wms/select/NodesSkuUm";
import NodesSkuSpec from "@/components/wms/select/NodesSkuSpec";
import NodesOwner from "@/components/wms/select/NodesOwner";
import NodesCustomer from "@/components/wms/select/NodesCustomer";
import NodesDictionary from "@/components/wms/select/NodesDictionary";

export default {
    name: "soBillAdd",
    components: {
        NodesDictionary, NodesCustomer, NodesOwner, NodesSkuUm, NodesSkuSpec,
        NodesWarehouse, NodesBillType, NodesLineNumber, NodesSku
    },
    mixins: [editDetailMixin],
    data() {
        return {
            form: {
                params: {
                    billTypeCd: '',
                    whId: '',
                    woId: '',
                    customer: {},
                    contact: '',
                    transportCode: '',
                    outstockType: '',
                    soBillRemark: '',
                },
                rules: {
                    billTypeCd: [
                        {
                            required: true,
                            message: '请选择单据类型',
                            trigger: 'change'
                        }
                    ],
                    whId: [
                        {
                            required: true,
                            message: '请选择库房',
                            trigger: 'change'
                        }
                    ],
                    woId: [
                        {
                            required: true,
                            message: '请选择货主',
                            trigger: 'change'
                        }
                    ],
                    contact: [
                        {
                            required: true,
                            message: '请输入借用人',
                            trigger: 'blur'
                        }
                    ],
                    transportCode: [
                        {
                            required: true,
                            message: '请选择发货方式',
                            trigger: 'change'
                        }
                    ],
                    outstockType: [
                        {
                            required: true,
                            message: '请选择出库方式',
                            trigger: 'change'
                        }
                    ],
                }
            },
        }
    },
    methods: {
        // 过滤空白行
        filterBlankRow(row) {
            return !(
                func.isEmpty(row.sku.skuId) &&
                func.isEmpty(row.sku.skuCode) &&
                func.isEmpty(row.sku.skuName) &&
                func.isEmpty(row.skuSpec) &&
                row.planQty === 0 &&
                func.isEmpty(row.umCode)
            );
        },
        getDescriptor() {
            let skuErrorMsg = '请选择物品';
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
                skuSpec: {
                    type: 'string',
                    required: true,
                    message: '物品规格不能为空'
                },
                umCode: {
                    type: 'string',
                    required: true,
                    message: '计量单位不能为空'
                },
                planQty: {
                    required: true,
                    type: 'Number',
                    validator: (rule, value) => value > 0, message: '计划数量不能为0',
                    trigger: 'blur'
                },
            };
        },
        createRowObj() {
            return {
                lineNumber: '',
                sku: {
                    skuId: '',
                    skuCode: '',
                    skuName: ''
                },
                umCode: '',
                skuSpec: '',
                planQty: 0,
                skuLot1: '',
                skuLot4: '',
                remark: '',
            }
        },
        deleteRow(index, rows) {
            this.$confirm("确定删除当前行？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                rows.splice(index, 1)
            })
        },
        submitFormParams() {
            let postData = this.table.postData;
            let params = this.form.params;
            let soDetailList = postData.map(value => {
                return {
                    soLineNo: value.lineNumber,
                    skuId: value.sku.skuId,
                    skuSpec: value.skuSpec,
                    umCode: value.umCode,
                    planQty: value.planQty,
                    skuLot1: value.skuLot1,
                    skuLot4: value.skuLot4,
                    remark: value.remark,
                }
            })
            let data = {
                billTypeCd: params.billTypeCd,
                whId: params.whId,
                woId: params.woId,
                customerId: params.customer.id,
                contact: params.contact,
                transportCode: params.transportCode,
                outstockType: params.outstockType,
                soBillRemark: params.soBillRemark,
                soDetailList: soDetailList,
            }
            return add(data).then(res => {
                return {
                    msg: res.data.msg,
                    router: {
                        path: '/wms/outstock/soHeader',
                        query: {
                            isRefresh: 'true'
                        }
                    }
                }
            })
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
</style>
