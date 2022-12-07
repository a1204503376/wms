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
                        <h3>发货单编辑</h3>
                    </el-row>
                    <el-row type="flex">
                        <el-col :span="8">
                            <el-form-item label="发货单编码" prop="soBillNo">
                                <el-input
                                    v-model="form.params.soBillNo"
                                    :disabled="true"
                                    size="medium"
                                    style="width: 210px">
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="单据类型" prop="billTypeCd">
                                <nodes-bill-type
                                    :disabled="billTypeDisable"
                                    :filter-types="filterBillType"
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
                                    size="medium">
                                </nodes-warehouse>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row type="flex">
                        <el-col :span="8">
                            <el-form-item label="所属货主" prop="woId">
                                <nodes-owner
                                    v-model="form.params.woId"
                                    :default-value="true"
                                    size="medium">
                                </nodes-owner>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item
                                :label="isLend ? '借用人' : '客户'"
                                :prop="isLend ? 'contact' : 'customer'">
                                <nodes-customer
                                    v-if="!isLend"
                                    v-model="form.params.customer"
                                    size="medium">
                                </nodes-customer>
                                <el-input
                                    v-else
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
                    </el-row>
                    <el-row type="flex">
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
                    <el-row type="flex">
                        <el-col :span="24" style="width: 91%;">
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
                                    :align="'left'"
                                    prop="skuCode"
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
                                    :align="'left'"
                                    prop="skuName"
                                    width="200">
                                    <template slot="header">
                                        <span>物品名称</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model="row.sku.skuName"
                                            :disabled="true"
                                            size=mini>
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="umCode" width="121">
                                    <template slot="header">
                                        <span class="d-table-header-required">计量单位</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <nodes-sku-um
                                            v-model="row.umCode"
                                            :sku="row.sku">
                                        </nodes-sku-um>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="skuSpec" width="122">
                                    <template slot="header">
                                        <span class="d-table-header-required">物品规格</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <nodes-sku-spec
                                            v-model="row.sku.skuSpec"
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
                                <el-table-column :align="'left'" prop="skuLot1" width="132">
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
                                <el-table-column :align="'left'" prop="skuLot4">
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
                                        <el-input v-model="row.remark" size="mini"></el-input>
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
                        @click="onSubmit">保 存
                    </el-button>
                    <el-button
                        :loading="loading"
                        @click="onClose">关 闭
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
import {detailByEdit, edit} from "@/api/wms/outstock/soHeader"
import NodesBillType from "@/components/wms/select/NodesBillType";
import NodesCustomer from "@/components/wms/select/NodesCustomer";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import NodesOwner from "@/components/wms/select/NodesOwner";
import NodesSkuUm from "@/components/wms/select/NodesSkuUm";
import NodesSkuSpec from "@/components/wms/select/NodesSkuSpec";
import NodesDictionary from "@/components/wms/select/NodesDictionary";

export default {
    name: "edit",
    components: {
        NodesSkuUm, NodesOwner, NodesWarehouse,
        NodesCustomer, NodesDictionary, NodesSkuSpec,
        NodesBillType, NodesLineNumber, NodesSku,
    },
    mixins: [editDetailMixin],
    data() {
        return {
            removeIdList: [],
            billTypeDisable: false,
            filterBillType: [], //默认没有过滤的选项
            isLend: false,
            form: {
                params: {
                    soBillId: '',
                    soBillNo: '',
                    billTypeCd: '',
                    whId: '',
                    woId: '',
                    customer: null,
                    contact: null,
                    outstockType: '',
                    transportCode: '',
                    soBillRemark: ''
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
                },
            }
        }
    },
    methods: {
        // 过滤空白行
        filterBlankRow(row) {
            return !(
                func.isEmpty(row.sku.skuId) &&
                func.isEmpty(row.sku.skuCode) &&
                func.isEmpty(row.sku.skuName) &&
                func.isEmpty(row.sku.skuSpec) &&
                row.planQty === 0 &&
                func.isEmpty(row.umCode)
            );
        },
        getDescriptor() {
            const skuErrorMsg = '请选择物物品';
            return {
                sku: {
                    type: 'object',
                    required: true,
                    fields: {
                        skuId: {required: true, message: skuErrorMsg},
                        skuCode: {required: true, message: skuErrorMsg},
                        skuName: {required: true, message: skuErrorMsg},
                        skuSpec: {required: true, message: skuErrorMsg},
                    }
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
        deleteRow(index, rows) {
            this.$confirm('确定删除该条记录吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.removeIdList.push(rows[index].soDetailId);
                rows.splice(index, 1)
            })
        },
        async initTableData() {
            if (func.isEmpty(this.id)) {
                return;
            }
            await detailByEdit(this.id).then((res) => {
                let data = res.data.data;
                this.form.params = data.soHeader;
                data.soDetailList.map(item => {
                    return {
                        soDetailId: item.soDetailId,
                        lineNumber: item.soLineNo,
                        sku: item.sku,
                        umCode: item.umCode,
                        planQty: item.planQty,
                        skuLot1: item.skuLot1,
                        skuLot4: item.skuLot4,
                        remark: item.remark
                    }
                })
                this.table.data = data.soDetailList;
                this.isLend = this.form.params.billTypeCd === this.$commonConst.BILL_TYPE_LEND;
                // 当初始化时，单据类型不为借用出入，则过滤掉组件中的借用出库选项。是借用出库，则禁用组件
                if (!this.isLend) {
                    this.filterBillType = [this.$commonConst.BILL_TYPE_LEND]
                    this.billTypeDisable = false
                } else {
                    this.filterBillType = []
                    this.billTypeDisable = true
                }
            })
        },
        submitFormParams() {
            let params = this.form.params;
            let soDetailList = this.table.postData.map((value) => {
                return {
                    soDetailId: value.soDetailId,
                    soLineNo: value.lineNumber,
                    skuId: value.sku.skuId,
                    skuSpec: value.sku.skuSpec,
                    umCode: value.umCode,
                    planQty: value.planQty,
                    skuLot1: value.skuLot1,
                    skuLot4: value.skuLot4,
                    remark: value.remark,
                }
            })
            params.removeIdList = this.removeIdList;
            params.soDetailList = soDetailList;
            return edit(params).then(res => {
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
        },
        createRowObj() {
            return {
                lineNumber: '',
                sku: {
                    skuId: '',
                    skuCode: '',
                    skuName: '',
                    skuSpec: '',
                },
                umCode: '',
                planQty: 0,
                skuLot1: '',
                skuLot4: '',
                remark: '',
            }
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
