<template>
    <basic-container>
        <el-container id="container" v-loading="loading">
            <el-main style="overflow: hidden;overflow-y: scroll;height: 100%">
                <el-form ref="form"
                         :model="form.params"
                         label-position="right"
                         label-width="120px"
                         size="mini"
                         style="margin-left:10px;margin-right:10px;"
                >
                    <el-row>
                        <h3>收货订单</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="单据编码:">
                                {{form.params.receiveNo}}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="单据状态:">
                                {{form.params.billStateDesc}}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="库房:">
                                {{form.params.whCode}}
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="货主:">
                                {{form.params.ownerCode}}
                            </el-form-item>
                        </el-col>


                        <el-col :span="8">
                            <el-form-item label="单据类型:">
                                {{form.params.billTypeName}}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="入库方式:">
                                {{form.params.inStoreTypeDesc}}
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="ASN编码:">
                                {{form.params.asnBillNo}}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="供应商编码:">
                                {{form.params.supplierCode}}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="供应商名称:">
                                {{form.params.supplierName}}
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="备注:">
                                {{form.params.remark}}
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <h3>收货订单订单明细</h3>
                    </el-row>
                    <el-row style="overflow-y: auto">
                        <el-col>
                            <el-table
                                ref="table"
                                :data="table.data"
                                border
                                highlight-current-row
                                size="mini"
                                row-key="receiveId"
                                @sort-change="onSortChange">
                                <el-table-column
                                    fixed
                                    type="selection"
                                    width="50">
                                </el-table-column>

                                <template v-for="(column,index) in table.columnList">

                                    <el-table-column
                                        :key="index"
                                        show-overflow-tooltip
                                        v-bind="column"
                                    >
                                    </el-table-column>
                                </template>
                            </el-table>

                        </el-col>
                    </el-row>
                </el-form>
            </el-main>
            <el-footer>
                <el-row style="margin-top: 10px;line-height:60px;text-align:right;">
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
import NodesSku from "@/components/wms/select/NodesSku";
import NodesLineNumber from "@/components/wms/table/NodesLineNumber";
import {editDetailMixin} from "@/mixins/editDetail";
// eslint-disable-next-line no-unused-vars
import {listMixin} from "@/mixins/list";
import {getReceiveDetailById} from "@/api/wms/instock/receive";
import func from "@/util/func";

export default {
    props: {
        receiveId: {type: String},
    },
    name: "receiveDetails",
    components: {NodesLineNumber, NodesSku, NodesInStoreMode},
    mixins: [editDetailMixin,listMixin],
    data() {
        return {
            that:this,
            page: {
                total: 0,
                size: 20,
                current: 1,
                ascs: "", //正序字段集合
                descs: "", //倒序字段集合
            },
            form:{
                params:{
                    receiveNo:'',
                    asnBillNo:'',
                    whCode:'',
                    billStateDesc:'',
                    ownerCode:'',
                    inStoreTypeDesc:'',
                    remark:'',
                    supplierCode:'',
                    supplierName:'',
                    billTypeCd:'',
                    billTypeName:'',
                }
            },
            receiveDetailList: [
                {
                    prop: 'lineNo',
                    label: '行号',
                },
                {
                    prop: 'skuCode',
                    label: '物品编码'
                },
                {
                    prop: 'skuName',
                    label: '物品名称',
                    // left/center/right
                    align: 'right'
                },
                {
                    prop: 'planQty',
                    label: '计划数量'
                },
                {
                    prop: 'scanQty',
                    label: '已收数量'
                },
                {
                    prop: 'surplusQty',
                    label: '剩余数量'
                },
                {
                    prop: 'umName',
                    label: '计量单位'
                },
                {
                    prop: 'detailStatus',
                    label: '状态'
                },
                {
                    prop: 'skuSpec',
                    label: '包装规格'
                },
                {
                    prop: 'remark',
                    label: '备注'
                },
            ],
            table: {
                columnList: [
                    {
                        prop: 'lineNo',
                        label: '行号',
                    },
                    {
                        prop: 'skuCode',
                        label: '物品编码'
                    },
                    {
                        prop: 'skuName',
                        label: '物品名称',
                        // left/center/right
                        align: 'right'
                    },
                    {
                        prop: 'planQty',
                        label: '计划数量'
                    },
                    {
                        prop: 'scanQty',
                        label: '已收数量'
                    },
                    {
                        prop: 'surplusQty',
                        label: '剩余数量'
                    },
                    {
                        prop: 'umName',
                        label: '计量单位'
                    },
                    {
                        prop: 'detailStatus',
                        label: '状态'
                    },
                    {
                        prop: 'skuSpec',
                        label: '包装规格'
                    },
                    {
                        prop: 'remark',
                        label: '备注'
                    },
                ]
            },

        }
    },
    watch:{
       receiveId(){
           this.getTableData();
       }
    },
    created() {
        this.getTableData();
    },
    methods: {
        getTableData() {
            if(func.isEmpty(this.receiveId)){
                return;
            }
           let ReceiveIdRequest  = {
                receiveId: this.receiveId
            }
            getReceiveDetailById(ReceiveIdRequest)
                .then((res) => {
                    let obj = res.data.data;
                    this.form.params = obj.receiveHeaderResponse;
                    this.table.data = obj.detailList;
                })
                .catch(() => {
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
