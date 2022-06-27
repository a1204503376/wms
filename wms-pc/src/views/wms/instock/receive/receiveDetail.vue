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
                </el-form>
                <el-tabs v-model="activeName" @tab-click="handleClick" type="border-card">
                    <el-tab-pane :label="item.lable" :name="item.name" v-for="(item, index) in tabList" :key="index">
                        <el-table
                            :data="table.data"
                            border
                            highlight-current-row
                            size="mini">

                            <template v-for="(column,index) in table.columnList">
                                <el-table-column
                                    v-if="!column.hide"
                                    :key="index"
                                    show-overflow-tooltip
                                    v-bind="column">
                                </el-table-column>
                            </template>
                        </el-table>

                    </el-tab-pane>
                </el-tabs>
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
import {getLogList, getReceiveDetailById, getReceiveLogList} from "@/api/wms/instock/receive";
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
            tabList:[
                {lable:'收货单明细',name:'receiveDetail'},
                {lable:'清点记录',name:'receiveLog'},
                {lable:'操作日志',name:'Log'}
            ],
            //tab标签默认打开第一个
            activeName:'receiveDetail',
            receiveLogData:[],
            receiveDetailData:[],
            LogData:[],
            receiveLogList:[
                {
                    prop: 'lineNo',
                    label: '行号',
                },
                {
                    prop: 'qty',
                    label: '数量',
                },
                {
                    prop: 'ipnCode',
                    label: 'lpn编码',
                },
                {
                    prop: 'boxCode',
                    label: '箱码',
                },
                {
                    prop: 'snCode',
                    label: '序列号',
                },
                {
                    prop: 'locCode',
                    label: '库位编码',
                },
                {
                    prop: 'skuCode',
                    label: '物品编码',
                },
                {
                    prop: 'skuName',
                    label: '物品名称',
                },
                {
                    prop: 'skuLevel',
                    label: '包装层级',
                },
                {
                    prop: 'skuSpec',
                    label: '规格',
                },
                {
                    prop: 'whCode',
                    label: '库房编码',
                },
                {
                    prop: 'ownerCode',
                    label: '货主编码',
                },
                {
                    prop: 'createUser',
                    label: '创建人',
                },
                {
                    prop: 'createTime',
                    label: '创建时间',
                },
            ],
            logList:[
                {
                    prop: 'userAccount',
                    label: '操作人员账号',
                    align: 'center'
                },
                {
                    prop: 'userRealName',
                    label: '操作人员姓名',
                    align: 'center'
                },
                {
                    prop: 'log',
                    label: '操作内容',
                    align: 'center'
                },
                {
                    prop: 'createTime',
                    label: '操作时间',
                    align: 'center'
                },
            ],
            receiveDetailList: [
                {
                    prop: 'lineNo',
                    label: '行号',
                },
                {
                    prop: 'skuCode',
                    label: '物品编码',
                    width: 150,
                },
                {
                    prop: 'skuName',
                    label: '物品名称',
                    // left/center/right
                    width: 130,

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
                    prop: 'skuLot1',
                    width: 120,
                    label: '生产批次'
                },
                {
                    prop: 'skuLot4',
                    width: 120,
                    label: '客户'
                },
                {
                    prop: 'skuLot5',
                    width: 120,
                    label: '钢背批次'
                },
                {
                    prop: 'skuLot6',
                    width: 120,
                    label: '摩擦块批次'
                },
                {
                    prop: 'skuLot8',
                    width: 120,
                    label: 'CRCC'
                },
                {
                    prop: 'remark',
                    width: 120,
                    label: '备注'
                },
            ],
            table: {
                columnList: []
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
        handleClick(tab) {
            if(tab.name === 'receiveDetail'){
                this.table.columnList = this.receiveDetailList
                this.table.data = this.receiveDetailData
            }else if(tab.name === 'receiveLog'){
                this.getReceiveLogList()
            }else{
                this.getLogList();
            }
        },
        getTableData() {
            this.table.columnList = this.receiveDetailList;
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
                    this.receiveDetailData = obj.detailList;
                    this.table.data = obj.detailList;
                })
        },
        getReceiveLogList(){
            this.table.columnList = this.receiveLogList
            if(func.isEmpty(this.receiveLogData)){
                getReceiveLogList(this.receiveId)
                    .then((res) =>{
                            this.receiveLogData = res.data.data
                            this.table.data = res.data.data
                        }
                    )
            }else{
                this.table.data = this.receiveLogData
            }

        },
        getLogList(){
            this.table.columnList = this.logList
            if(func.isEmpty(this.LogData)){
                getLogList(this.receiveId)
                    .then((res) =>{
                            this.LogData = res.data.data
                            this.table.data = res.data.data
                        }
                    )
            }else{
                this.table.data = this.LogData
            }

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
