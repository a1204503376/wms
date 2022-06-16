<template>
    <basic-container>
        <el-container id="container" v-loading="loading">
            <el-main style="overflow: hidden;overflow-y: scroll;">
                <el-form ref="form"
                         :model="form.params"
                         :rules="form.rules"
                         label-position="right"
                         label-width="120px"
                         size="mini"
                         style="margin-left:10px;margin-right:10px;"
                >
                    <el-row>
                        <h3>ASN单编辑</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="ASN单编码" prop="asnBillNo">
                                <el-input v-model="form.params.asnBillNo"
                                          :readonly="true"
                                          style="width: 250px;"
                                >
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="单据类型" prop="billTypeCd">
                                <nodes-bill-type v-model="form.params.billTypeCd" style="width: 250px">
                                </nodes-bill-type>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="供应商" prop="supplier">
                                <nodes-supplier
                                    v-model="form.params.supplier"
                                    :multiple="false"
                                    style="width: 250px">
                                </nodes-supplier>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="仓库" prop="whId">
                                <nodes-warehouse
                                    v-model="form.params.whId"
                                    :multiple="false"
                                    style="width: 250px;"
                                ></nodes-warehouse>
                            </el-form-item>
                        </el-col>

                        <el-col :span="8">
                            <el-form-item label="货主" prop="ownerId">
                                <nodes-owner
                                    v-model="form.params.woId"
                                    :default-value="true"
                                    :multiple="false"
                                    style="width: 250px;"
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
                                    placeholder="请输入内容"
                                    style="width: 1070px"
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
                                    prop="skuCode"
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
                                            v-model="row.sku.skuName"
                                            :disabled="true"
                                            size=mini
                                        >
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column :align="'left'" prop="skuSpec">
                                    <template slot="header">
                                        <span class="d-table-header-required">规格</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model="row.sku.skuSpec"
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
                                            :sku="row.sku"
                                            style="width: 100px"
                                        ></nodes-sku-um>
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
                                            size="mini"
                                        ></el-input-number>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="scanQty" width="120">
                                    <template slot="header">
                                        <span>实收数量</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input v-model="row.scanQty" :readonly="true" size="mini"></el-input>
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
                                    <template v-slot="{row}">
                                        <el-link type="primary"
                                                 @click.native.prevent="deleteRow(row.$index, table.data, row)">删除
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
import NodesInStoreMode from "@/components/wms/select/NodesInStoreMode";
import NodesInStoreType from "@/components/wms/select/NodesInStoreType";
import NodesSku from "@/components/wms/select/NodesSku";
import NodesLineNumber from "@/components/wms/table/NodesLineNumber";
import {editDetailMixin} from "@/mixins/editDetail";
import func from "@/util/func";
import NodesAsnBillState from "@/components/wms/select/NodesAsnBillState";
import {detailByEdit, edit} from "@/api/wms/instock/asnHeader"
import NodesBillType from "@/components/wms/select/NodesBillType";
import NodesSupplier from "@/components/wms/select/NodesSupplier";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import NodesOwner from "@/components/wms/select/NodesOwner";
import NodesSkuUm from "@/components/wms/select/NodesSkuUm";

export default {
    name: "edit",
    components: {
        NodesSkuUm,
        NodesOwner,
        NodesWarehouse,
        NodesSupplier,
        NodesBillType, NodesAsnBillState, NodesLineNumber, NodesSku, NodesInStoreType, NodesInStoreMode
    },
    mixins: [editDetailMixin],
    props: {
        asnBillId: {type: String, required: true},
    },
    data() {
        return {
            removeRowId: [],
            form: {
                params: {
                    asnBillNo: '',
                    billTypeCd: '',
                    supplier: {
                        id: '',
                        code: '',
                        name: ''
                    },
                    whId: '',
                    woId: '',
                    asnBillRemark: ''
                },
                rules: {
                    billTypeCd: [
                        {
                            required: true,
                            message: '请选择单据类型',
                            trigger: 'change'
                        }
                    ],
                    inStorageMode: [
                        {
                            type: 'number',
                            required: true,
                            message: '请选择入库方式',
                            trigger: 'change'
                        }
                    ]
                }
            }
        }
    },
    created() {
        this.getDataSource();
    },
    watch: {
        asnBillId() {
            this.refreshTable();
        }
    },
    methods: {
        // 过滤空白行
        filterBlankRow(row) {
            return !(
                (func.isEmpty(row.sku.skuId)
                    && func.isEmpty(row.sku.skuCode)
                    && func.isEmpty(row.sku.skuName))
                && row.planQty === 0
            );
        },
        getDescriptor() {
            const skuErrorMsg = '请选择物料';
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
                planQty: {
                    required: true,
                    type: 'Number',
                    validator: (rule, value) => value > 0, message: '计划数量不能为0',
                    trigger: 'blur'
                },
            };
        },
        deleteRow(index, rows, row) {
            this.$confirm('确定删除该条记录吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                if (func.isEmpty(row.asnDetailId)) {
                    rows.splice(index, 1);
                } else {
                    this.table.data = rows.filter(function (value) {
                        return value !== row;
                    })
                    this.removeRowId.push(row.asnDetailId)
                    console.log(this.table.data);
                }
                this.$message({
                    type: 'success',
                    message: '删除成功!'
                });
            })
        },
        getDataSource() {
            if (func.isEmpty(this.asnBillId)) {
                return;
            }
            let asdBillIdObj = {
                asnBillId: this.asnBillId
            }
            detailByEdit(asdBillIdObj)
                .then((res) => {
                    let headerData = res.data.data.asnHeaderEditResponse;
                    let detailData = res.data.data.asnDetailEditResponseList;
                    this.form.params.asnBillNo = headerData.asnBillNo;
                    this.form.params.billTypeCd = headerData.billTypeCd;
                    this.form.params.supplier = headerData.supplierSelectResponse;
                    this.form.params.whId = headerData.whId;
                    this.form.params.asnBillRemark = headerData.asnBillRemark;
                    let newDetailData = [];
                    detailData.forEach((value) => {
                            let detail = {
                                asnDetailId: value.asnDetailId,
                                asnLineNo: value.asnLineNo,
                                sku: value.skuSelectResponse,
                                planQty: value.planQty,
                                scanQty: value.scanQty,
                                remark: value.remark
                            }
                            newDetailData.push(detail);
                        }
                    )
                    this.table.data = newDetailData;
                })
        },
        createRowObj() {
            return {
                asnDetailId: '',
                asnLineNo: '',
                sku: {
                    skuId: '',
                    skuCode: '',
                    skuName: '',
                    skuSpec: '',
                },
                planQty: 0,
                sScanQty: 0,
                remark: '',
            }
        },
        onChangeSku() {
        },
        refreshTable() {
            this.getDataSource();
        },
        submitFormParams() {
            let asnDetailArray = [];
            let postData = this.table.postData;
            let params = this.form.params;
            postData.forEach((value) => {
                asnDetailArray.push({
                    asnDetailId: value.asnDetailId,
                    asnLineNo: value.lineNumber,
                    skuId: value.sku.skuId,
                    umCode: value.umCode,
                    planQty: value.planQty,
                    remark: value.remark,
                })
            })
            let data = {
                asnBillId: this.asnBillId,
                asnBillNo: params.asnBillNo,
                billTypeCd: params.billTypeCd,
                supplierId: params.supplier.id,
                whId: params.whId,
                woId: params.woId,
                asnBillRemark: params.asnBillRemark,
                removeIdList: this.removeRowId,
                asnDetailList: asnDetailArray,
            }
            return edit(data)
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
