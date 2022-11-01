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
                                    :default-value="true"
                                    io-type="O"
                                    size="medium">
                                </nodes-bill-type>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="所属库房" prop="whId">
                                <nodes-warehouse
                                    :default-value="true"
                                    v-model="form.params.whId"
                                    size="medium">
                                </nodes-warehouse>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="所属货主" prop="woId">
                                <nodes-owner
                                    v-model="form.params.woId"
                                    :default-value="true"
                                    size="medium">
                                </nodes-owner>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row type="flex">
                        <el-col :span="8">
                            <el-form-item label="客户" prop="customer">
                                <nodes-customer
                                    v-model="form.params.customer"
                                    size="medium">
                                </nodes-customer>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="出库方式" prop="outstockType">
                                <nodes-dictionary
                                    :default-value="true"
                                    v-model="form.params.outstockType"
                                    code="outstore_type"
                                    size="medium">
                                </nodes-dictionary>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="发货方式" prop="transportCode">
                                <nodes-dictionary
                                    :default-value="true"
                                    v-model="form.params.transportCode"
                                    code="so_transport_code"
                                    size="medium">
                                </nodes-dictionary>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row type="flex">
                        <el-col :span="8">
                            <el-form-item label="备注" prop="soBillRemark">
                                <el-input
                                    v-model="form.params.soBillRemark"
                                    :rows=2
                                    placeholder="请输入内容"
                                    size="medium"
                                    style="width: 1171px"
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
                                    prop="sku"
                                    width="200">
                                    <template slot="header">
                                        <span class="d-table-header-required">物品编码</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <nodes-sku
                                            v-model="row.sku"
                                            :disabled="true">
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
                                <el-table-column prop="umCode" width="122">
                                    <template slot="header">
                                        <span class="d-table-header-required">计量单位</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <nodes-sku-um
                                            v-model="row.umCode"
                                            :disabled="true"
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
                                            :disabled="true"
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
                                            size="mini"></el-input-number>
                                    </template>
                                </el-table-column>
                                <el-table-column :align="'left'" prop="生产批次">
                                    <template slot="header">
                                        <span>生产批次</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model="row.skuLot1"
                                            :disabled="true"
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
                                            :disabled="true"
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
import NodesDictionary from "@/components/wms/select/NodesDictionary"
import NodesCustomer from "@/components/wms/select/NodesCustomer"
import NodesSku from "@/components/wms/select/NodesSku";
import NodesLineNumber from "@/components/wms/table/NodesLineNumber";
import {editDetailMixin} from "@/mixins/editDetail";
import func from "@/util/func";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import NodesBillType from "@/components/wms/select/NodesBillType";
import NodesOwner from "@/components/wms/select/NodesOwner";
import {add} from "@/api/wms/outstock/soHeader";
import NodesSkuUm from "@/components/wms/select/NodesSkuUm";
import NodesSkuSpec from "@/components/wms/select/NodesSkuSpec";

export default {
    name: "new",
    components: {
        NodesSkuUm, NodesOwner, NodesCustomer, NodesSkuSpec,
        NodesBillType, NodesWarehouse, NodesLineNumber,
        NodesSku, NodesDictionary
    },
    mixins: [editDetailMixin],
    props: {
        receiveLogs: {type: String},
    },
    data() {
        return {
            form: {
                params: {
                    billTypeCd: '',
                    whId: '',
                    woId: '',
                    customer: {},
                    transportCode: '',
                    outstockType: '',
                    soBillRemark: '',
                    soDetailList: []
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
            }
        }
    },
    created() {
        this.initializeData();
    },
    watch: {
        receiveLogs() {
            this.initializeData();
        }
    },
    methods: {
        // 过滤空白行
        filterBlankRow(row) {
            return !(
                (func.isEmpty(row.sku.skuId)
                    && func.isEmpty(row.sku.skuCode)
                    && func.isEmpty(row.sku.skuName)
                    && func.isEmpty(row.sku.skuSpec))
                && row.planQty === 0
            );
        },
        getDescriptor() {
            const skuErrorMsg = '请选择物品';
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
                planQty: {
                    required: true,
                    type: 'Number',
                    validator: (rule, value) => value > 0, message: '计划数量不能为0',
                    trigger: 'blur'
                }
            };
        },
        initializeData: function () {
            this.table.data = JSON.parse(this.receiveLogs);
            let i = 1;
            this.table.data.forEach(row => {
                row.soLineNo = i++ * 10;
                row.sku = {
                    skuId: row.skuId,
                    skuCode: row.skuCode,
                    skuName: row.skuName,
                    skuSpec: row.skuLot2
                };
                row.planQty = row.qty;
                row.umCode = row.wsuCode;
            })
        },
        createRowObj() {
            return {
                lineNumber: '',
                sku: {},
                umCode: '',
                planQty: 0,
                remark: '',
                skuLot1: '',
                skuLot4: '',
                skuLot5: '',
                skuLot6: '',
                skuLot8: '',
            }
        },
        deleteRow(index, rows) {
            this.$confirm("确定删除当前行？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                rows.splice(index, 1);
            })
        },
        submitFormParams() {
            let params = this.form.params;
            params.soDetailList = this.table.postData.map((value) => {
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
            params.customerId = params.customer.id;
            return add(params)
                .then(res => {
                    return {
                        msg: res.data.msg,
                        router: {
                            path: '/wms/instock/receiveLog',
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
