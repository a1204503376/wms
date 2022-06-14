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
                        <h3>收货单-{{ isEdit ? '编辑' : '新增' }}</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="库房" >
                                 <nodes-warehouse v-model="form.params.newReceiveHeaderRequest.whId" :default-value="true"></nodes-warehouse>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="单据类型"  prop="billTypeCd" >
                                <nodes-bill-type v-model="form.params.newReceiveHeaderRequest.billTypeCd" ></nodes-bill-type>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="供应商">
                                <nodes-supplier v-model="form.params.newReceiveHeaderRequest.supplier" style="width: 200px"></nodes-supplier>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                            <el-col :span="8">
                            <el-form-item label="入库方式" >
                                <nodes-in-store-mode
                                    v-model="form.params.newReceiveHeaderRequest.inStoreType"
                                    :multiple="false" :default-value="true">
                                </nodes-in-store-mode>
                            </el-form-item>
                        </el-col>

                        <el-col :span="8">
                            <el-form-item label="货主">
                                <nodes-owner v-model="form.params.newReceiveHeaderRequest.woId" :default-value="true"></nodes-owner>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="备注" >
                                <el-input type="textarea" v-model="form.params.newReceiveHeaderRequest.remark"  style="width: 1055px"></el-input>
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
                                    width="230"
                                >
                                    <template slot="header">
                                        <span class="d-table-header-required">物品编码</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <nodes-sku
                                            v-model="row.sku"
                                            style="width: 180px;"
                                            @selectValChange="onChangeSku(row)"
                                        >

                                        </nodes-sku>

                                    </template>
                                </el-table-column>
                                <el-table-column>
                                <template slot="header">
                                    <span class="d-table-header-required">物料名称</span>
                                </template>
                                <template v-slot="{row}">
                                    <el-input
                                        size=mini
                                        v-model="row.sku.skuName"
                                        :disabled="true">
                                    </el-input>
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
                                            :min="0"
                                            controls-position="right"
                                            size="mini"></el-input-number>
                                    </template>
                                </el-table-column>

                                <el-table-column
                                    :align="'left'"
                                    prop="skuCode"

                                >
                                    <template slot="header">
                                        <span class="d-table-header-required">计量单位</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <nodes-sku-um v-model="row.umCode"  :sku="row.sku"></nodes-sku-um>
                                    </template>
                                </el-table-column>

                                <el-table-column
                                    :align="'left'"
                                    prop="skuCode"
                                >
                                    <template slot="header">
                                        <span class="d-table-header-required">规格</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            size=mini
                                            v-model="row.sku.skuSpec"
                                            :disabled="true">
                                        </el-input>
                                    </template>
                                </el-table-column>

                                <el-table-column>
                                    <template slot="header">
                                        <span class="d-table-header-required">备注</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input v-model="row.remark" size="mini"></el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column  width="100">
                                    <template slot="header">
                                        <span class="d-table-header-required">操作</span>
                                    </template>
                                <template slot-scope="scope">
                                    <el-button
                                        @click.native.prevent="deleteRow(scope.$index, table.data)"
                                        type="text"
                                        size="small">
                                        删除
                                    </el-button>
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
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import NodesBillType from "@/components/wms/select/NodesBillType";
import NodesSupplier from "@/components/wms/select/NodesSupplier";
import NodesOwner from "@/components/wms/select/NodesOwner";
import {addReceive} from "@/api/wms/instock/receive";
import NodesSkuUm from "@/components/wms/select/NodesSkuUm";

export default {
    name: "edit",
    components: {
        NodesSkuUm,
        NodesOwner,
        NodesSupplier,
        NodesBillType, NodesWarehouse, NodesLineNumber, NodesSku, NodesInStoreType, NodesInStoreMode},
    mixins: [editDetailMixin],
    data() {
        return {
            refresh:true,
             form: {
                params: {
                    newReceiveHeaderRequest: {
                        inStoreType: null,
                        billTypeCd: '',
                        whId: '',
                        supplier:{
                            id:'',
                            code:'',
                            name:''
                        },
                        woId: '',
                        remark: '',
                    },
                    newReceiveDetailRequestList:[]

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
            );
        },
        getDescriptor() {
            const skuErrorMsg = '请选择物品编码';
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
                planQty:{type:'Number',validator: (rule, value) => value>0, message:'计划数量不能为0'}

            };
        },
        createRowObj() {
            return {
                lineNumber:'',
                sku: {
                    skuId: '',
                    skuCode: '',
                    skuName: '',
                    skuSpec:''
                },
                umCode:'',
                planQty: 0,
                remark:'',
                skuSpec:''
            }
        },
        deleteRow(index, rows) {
            this.$confirm("确定删除当前行？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                rows.splice(index, 1);
                let a;
                // for(a=index;a<rows.length;a++){
                //     rows[a].lineNumber = rows[a].lineNumber -10
                // }
            })

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
