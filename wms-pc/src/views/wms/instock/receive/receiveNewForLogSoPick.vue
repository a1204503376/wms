<template>
    <basic-container>
        <el-container id="container" v-loading="loading">
            <el-main style="overflow: hidden;overflow-y: scroll;">
                <el-form ref="form"
                         :model="form.params.newReceiveHeaderRequest"
                         :rules="form.rules"
                         label-position="right"
                         label-width="120px"
                         size="medium"
                         style="margin-left:10px;margin-right:10px;"
                >
                    <el-row>
                        <h3>收货单新增</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="库房">
                                <nodes-warehouse
                                    v-model="form.params.newReceiveHeaderRequest.whId"
                                    :default-value="true"
                                    size="medium">
                                </nodes-warehouse>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="单据类型" prop="billTypeCd">
                                <nodes-bill-type
                                    v-model="form.params.newReceiveHeaderRequest.billTypeCd"
                                    io-type="I"
                                    size="medium">
                                </nodes-bill-type>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="供应商">
                                <nodes-supplier
                                    v-model="form.params.newReceiveHeaderRequest.supplier"
                                    size="medium">
                                </nodes-supplier>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="入库方式">
                                <nodes-in-store-mode
                                    v-model="form.params.newReceiveHeaderRequest.inStoreType"
                                    :default-value="true"
                                    :multiple="false"
                                    size="medium">
                                </nodes-in-store-mode>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="货主">
                                <nodes-owner
                                    v-model="form.params.newReceiveHeaderRequest.woId"
                                    :default-value="true"
                                    size="medium">
                                </nodes-owner>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="备注">
                                <el-input v-model="form.params.newReceiveHeaderRequest.remark" style="width: 1070px"
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
                                    fixed="left"
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
                                    prop="skuCode"
                                    width="195">
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
                                <el-table-column width="125">
                                    <template slot="header">
                                        <span class="d-table-header-required">物料名称</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model="row.sku.skuName"
                                            :disabled="true"
                                            size=mini>
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                    prop="planQty"
                                    width="152">
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
                                <el-table-column
                                    prop="umCode"
                                    width="110">
                                    <template slot="header">
                                        <span class="d-table-header-required">计量单位</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <nodes-sku-um
                                            v-model="row.umCode"
                                            :disabled="true">
                                        </nodes-sku-um>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                    prop="skuSpec">
                                    <template slot="header">
                                        <span class="d-table-header-required">规格</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <nodes-sku-spec
                                            v-model="row.skuSpec"
                                            :sku="row.sku"
                                            :disabled="true"
                                            size=mini>
                                        </nodes-sku-spec>
                                    </template>
                                </el-table-column>
                                <el-table-column width="130">
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
                                <el-table-column width="130">
                                    <template slot="header">
                                        <span>专属客户</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model="row.skuLot4"
                                            :disabled="true"
                                            size=mini>
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column width="130">
                                    <template slot="header">
                                        <span>钢背批次</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model="row.skuLot5"
                                            :disabled="true"
                                            size=mini>
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column width="130">
                                    <template slot="header">
                                        <span>摩擦块批次</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model="row.skuLot6"
                                            :disabled="true"
                                            size=mini>
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column width="130">
                                    <template slot="header">
                                        <span>适用速度等级</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input
                                            v-model="row.skuLot8"
                                            :disabled="true"
                                            size=mini>
                                        </el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column width="130">
                                    <template slot="header">
                                        <span>备注</span>
                                    </template>
                                    <template v-slot="{row}">
                                        <el-input v-model="row.remark" size="mini"></el-input>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                    fixed="right"
                                    width="100">
                                    <template slot="header">
                                        <span>操作</span>
                                    </template>
                                    <template v-slot="scope">
                                        <el-button
                                            size="small"
                                            type="text"
                                            @click.native.prevent="deleteRow(scope.$index, table.data)">
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
import NodesSkuSpec from "@/components/wms/select/NodesSkuSpec";

export default {
    name: "new",
    components: {
        NodesSkuSpec,
        NodesSkuUm, NodesOwner, NodesSupplier,
        NodesBillType, NodesWarehouse, NodesLineNumber,
        NodesSku, NodesInStoreType, NodesInStoreMode
    },
    mixins: [editDetailMixin],
    props: {
        logSoPicks: {type: String},
    },
    data() {
        return {
            refresh: true,
            form: {
                params: {
                    newReceiveHeaderRequest: {
                        inStoreType: null,
                        billTypeCd: '',
                        whId: '',
                        supplier: {},
                        woId: '',
                        remark: '',
                    },
                    newReceiveDetailRequestList: []
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
        this.initializeData();
    },
    watch: {
        logSoPicks() {
            this.initializeData();
        }
    },
    methods: {
        // 过滤空白行
        filterBlankRow(row) {
            return !(func.isEmpty(row.sku.skuId)
                && func.isEmpty(row.sku.skuCode)
                && func.isEmpty(row.sku.skuName)
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
                        skuId: {required: true, message: skuErrorMsg},
                        skuCode: {required: true, message: skuErrorMsg},
                        skuName: {required: true, message: skuErrorMsg}
                    }
                },
                planQty: {type: 'Number', validator: (rule, value) => value > 0, message: '计划数量不能为0'}
            };
        },
        initializeData() {
            this.table.data = JSON.parse(this.logSoPicks);
            let i = 1;
            this.table.data.forEach(row => {
                row.lineNumber = i * 10;
                row.sku = {
                    skuId: row.skuId,
                    skuCode: row.skuCode,
                    skuName: row.skuName,
                };
                row.skuSpec = row.skuLot2,
                row.planQty = row.pickRealQty;
                row.umCode = row.wsuCode;
                i++;
            })
        },
        createRowObj() {
            return {
                lineNumber: '',
                sku: {
                    skuId: '',
                    skuCode: '',
                    skuName: '',
                },
                umCode: '',
                skuSpec: '',
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
            this.form.params.newReceiveDetailRequestList = this.table.postData
            return addReceive(this.form.params)
                .then(res => {
                    return {
                        msg: res.data.msg,
                        router: {
                            path: '/wms/outstock/logSoPick',
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
