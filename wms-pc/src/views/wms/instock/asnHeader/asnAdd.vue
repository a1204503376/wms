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
                         style="margin-left:10px;margin-right:10px;"
                >
                    <el-row>
                        <h3>ASN单新增</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="单据类型" prop="billTypeCd">
                                <nodes-bill-type
                                    v-model="form.params.billTypeCd"
                                    :multiple="false"
                                    io-type="I"
                                    :default-value="true"
                                    size="medium"
                                ></nodes-bill-type>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="供应商" prop="supplier">
                                <nodes-supplier
                                    v-model="form.params.supplier"
                                    size="medium"
                                    >
                                </nodes-supplier>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="仓库" prop="whId">
                                <nodes-warehouse
                                    v-model="form.params.whId"
                                    size="medium"
                                ></nodes-warehouse>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="货主" prop="ownerId">
                                <nodes-owner
                                    v-model="form.params.woId"
                                    size="medium"
                                    :default-value="true"
                                ></nodes-owner>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="备注" prop="asnBillRemark">
                                <el-input
                                    v-model="form.params.asnBillRemark"
                                    :rows=2
                                    maxLength="66"
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
                                <el-table-column
                                    label="行号"
                                    prop="lineNumber"
                                    show-overflow-tooltip
                                    type="index"
                                    width="60"
                                >
                                    <template v-slot="scope">
                                        <nodes-line-number
                                            :index="scope.$index"
                                            @change="(val)=>{ scope.row.lineNumber = val; }"
                                        >
                                        </nodes-line-number>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                    :align="'left'"
                                    prop="sku"
                                    width="200"
                                >
                                    <template slot="header">
                                        <span class="d-table-header-required">物品编码</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <nodes-sku
                                            v-model="row.sku"
                                            @selectValChange="onChangeSku"
                                        >
                                        </nodes-sku>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                    :align="'left'"
                                    prop="skuName"
                                    width="200"
                                >
                                    <template slot="header">
                                        <span>物品名称</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            size=mini
                                            v-model="row.sku.skuName"
                                            :disabled="true"
                                        >
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
                                            style="width: 100px"
                                        ></nodes-sku-um>
                                    </template>
                                </el-table-column>
                                <el-table-column :align="'left'" prop="skuSpec">
                                    <template slot="header">
                                        <span>规格</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            size=mini
                                            v-model="row.sku.skuSpec"
                                            :disabled="true">
                                        </el-input>
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
                                <el-table-column
                                    prop="remark"
                                >
                                    <template slot="header">
                                        <span>备注</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input v-model.trim="row.remark" maxLength="66" size="mini"></el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column width="100px" align="center">
                                    <template slot="header" >
                                        <span>操作</span>
                                    </template>
                                    <template v-slot="scope">
                                        <el-link  @click.native.prevent="deleteRow(scope.$index, table.data)" type="primary">删除</el-link>
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
import NodesSku from "@/components/wms/select/NodesSku";
import NodesLineNumber from "@/components/wms/table/NodesLineNumber";
import {editDetailMixin} from "@/mixins/editDetail";
import func from "@/util/func";
import NodesBillType from "@/components/wms/select/NodesBillType";
import NodesSupplier from "@/components/wms/select/NodesSupplier";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import {add} from '@/api/wms/instock/asnHeader';
import NodesSkuUm from "@/components/wms/select/NodesSkuUm";
import NodesOwner from "@/components/wms/select/NodesOwner";

export default {
    name: "edit",
    components: {
        NodesOwner,
        NodesSkuUm,
        NodesWarehouse, NodesSupplier, NodesBillType, NodesLineNumber, NodesSku
    },
    mixins: [editDetailMixin],
    data() {
        return {
            form: {
                params: {
                    billTypeCd: '',
                    supplier: {
                        id: '',
                        code: '',
                        name: '',
                    },
                    whId: '',
                    woId: '',
                    asnBillRemark: '',
                },
                rules: {
                    billTypeCd: [
                        {
                            required: true,
                            message: '请选择单据类型',
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
                        skuName: {required: true, message: skuErrorMsg}
                    }
                },
                planQty: {
                    required: true,
                    type: 'Number',
                    validator: (rule, value) => value>0, message:'计划数量不能为0',
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
                    skuName: '',
                    skuSpec: '',
                },
                umCode: '',
                planQty: 0,
                remark: '',
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
        onChangeSku() {
        },
        submitFormParams() {
            let asnDetailArray = [];
            let postData = this.table.postData;
            let params = this.form.params;
            postData.forEach((value)=>{
                asnDetailArray.push({
                    asnLineNo: value.lineNumber,
                    skuId: value.sku.skuId,
                    umCode: value.umCode,
                    planQty: value.planQty,
                    remark: value.remark,
                })
            })
            let data = {
                billTypeCd: params.billTypeCd,
                supplierId: params.supplier.id,
                whId: params.whId,
                woId: params.woId,
                asnBillRemark: params.asnBillRemark,
                asnDetailList: asnDetailArray,
            }
            return add(data)
                .then(res => {
                    return {
                        msg: res.data.msg,
                        router: {
                            path: '/wms/instock/asnHeader',
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
