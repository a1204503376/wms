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
                        <h3>ASN单-{{ isEdit ? '编辑' : '新增' }}</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="单据类型" prop="billTypeCd">
                                <nodes-bill-type
                                    v-model="form.params.billTypeCd"
                                    :multiple="false"
                                    io-type="I"
                                ></nodes-bill-type>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="供应商" prop="supplierId">
                                <nodes-supplier
                                    v-model="form.params.supplierId"
                                    :multiple="false"
                                    style="width: 300px">
                                </nodes-supplier>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="仓库" prop="whId">
                                <nodes-warehouse
                                    v-model="form.params.whId"
                                    :multiple="false"
                                    style="width: 300px"
                                ></nodes-warehouse>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="备注" prop="asnBillRemark">
                                <el-input
                                    v-model="form.params.asnBillRemark"
                                    :rows=2
                                    style="width: 300px"
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
                                    prop="asnLineNo"
                                    show-overflow-tooltip
                                    type="index"
                                    width="60"
                                >
                                    <template v-slot="scope">
                                        <nodes-line-number
                                            :index="scope.$index"
                                            @change="(val)=>{ scope.row.asnLineNo = val; }"
                                        >
                                        </nodes-line-number>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                    :align="'left'"
                                    prop="skuId"
                                    width="200"
                                >
                                    <template slot="header">
                                        <span class="d-table-header-required">物品</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <nodes-sku
                                            style="width: 180px"
                                            v-model="row.skuId"
                                            @selectValChange="onChangeSku"
                                        >
                                        </nodes-sku>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                    prop="umCode"
                                >
                                    <template slot="header">
                                        <span class="d-table-header-required">计量单位</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input-number
                                            v-model="row.umCode"
                                            controls-position="right"
                                            size="mini"></el-input-number>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                    prop="planQty"
                                >
                                    <template slot="header">
                                        <span class="d-table-header-required">计划数量</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input-number
                                            v-model="row.planQty"
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
import NodesSku from "@/components/wms/select/NodesSku";
import NodesLineNumber from "@/components/wms/table/NodesLineNumber";
import {editDetailMixin} from "@/mixins/editDetail";
import func from "@/util/func";
import NodesBillType from "@/components/wms/select/NodesBillType";
import NodesSupplier from "@/components/wms/select/NodesSupplier";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import {add} from '@/api/wms/instock/asnHeader';

export default {
    name: "edit",
    components: {
        NodesWarehouse, NodesSupplier, NodesBillType, NodesLineNumber, NodesSku
    },
    mixins: [editDetailMixin],
    data() {
        return {
            form: {
                params: {
                    billTypeCd: '',
                    supplierId: '',
                    whId: '',
                    asnBillRemark: '',
                    asnDetailList: []
                },
                rules: {
                    billTypeCd: [
                        {
                            required: true,
                            message: '请选择单据类型',
                            trigger: 'change'
                        }
                    ],
                    supplierId: [
                        {
                            message: '请选择供应商',
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
                func.isEmpty(row.skuId)
                && row.planQty === 0
                && row.umCode === 0
            );
        },
        getDescriptor() {
            return {
                skuId: {
                    required: true,
                    message: '请选择物品',
                },
                planQty: {
                    required: true,
                    type: 'number',
                    message: '请填写计划',
                    trigger: 'blur'
                }
            };
        },
        createRowObj() {
            return {
                asnLineNo: '',
                skuId: '',
                umCode: '',
                planQty: 0,
                remark: '',
                supplierId: ''
            }
        },
        onChangeSku(val) {
            val;
        },
        submitFormParams() {
            this.form.params.asnDetailList = this.table.postData;

            return add(this.form.params)
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
