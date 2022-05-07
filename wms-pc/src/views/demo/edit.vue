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
                        <h3>收货单-{{ isEdit ? '编辑' : '新增' }}</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="单据编码" prop="asnBillNo">
                                <el-input v-model="form.params.asnBillNo"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="入库方式" prop="inStorageMode">
                                <nodes-in-store-mode
                                    v-model="form.params.inStorageMode"
                                    :multiple="false">

                                </nodes-in-store-mode>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="单据类型">
                                <nodes-in-store-type
                                    v-model="form.params.billTypeCd"
                                    :multiple="false">

                                </nodes-in-store-type>
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
                                    prop="skuCode"
                                    width="500"
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
                                    prop="skuLot1"
                                >
                                    <template slot="header">
                                        <span class="d-table-header-required">批属性</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input v-model="row.skuLot1" size="mini"></el-input>
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

export default {
    name: "edit",
    components: {NodesLineNumber, NodesSku, NodesInStoreType, NodesInStoreMode},
    mixins: [editDetailMixin],
    data() {
        return {
            form: {
                params: {
                    asnBillNo: '',
                    inStorageMode: '',
                    billTypeCd: '',
                },
                rules: {
                    asnBillNo: [
                        {
                            required: true,
                            message: '请输入活动名称',
                            trigger: 'blur'
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

    },
    methods: {
        // 过滤空白行
        filterBlankRow(row) {
            return !(
                (func.isEmpty(row.sku.skuId)
                    && func.isEmpty(row.sku.skuCode)
                    && func.isEmpty(row.sku.skuName))
                && row.planQty === 0
                && row.skuLot1 === '100'
            );
        },
        getDescriptor() {
            const skuErrorMsg = '请选择物料123';
            return {
                sku: {
                    type: 'object',
                    required: true,
                    fields: {
                        skuId: { required: true, message: skuErrorMsg},
                        skuCode: { required: true, message: skuErrorMsg},
                        skuName: { required: true, message: skuErrorMsg},
                    }
                },
                skuLot1: {
                    required: true,
                    message: '批属性不允许为空'
                }
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
                planQty: 0,
                skuLot1: '100'
            }
        },
        onChangeSku(val) {
            console.log(val)
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
