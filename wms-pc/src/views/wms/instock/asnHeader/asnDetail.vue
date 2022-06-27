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
                        <h3>ASN单</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="ASN单编码：">
                                {{form.params.asnBillNo}}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="单据类型：">
                                {{form.params.billTypeName}}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="供应商：">
                                {{form.params.supplierName}}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="仓库：">
                                {{form.params.whName}}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="货主：">
                                {{form.params.ownerName}}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="备注：">
                                {{form.params.asnBillRemark}}
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <h3>ASN单明细</h3>
                    </el-row>
                    <el-row style="overflow-y: auto">
                        <el-col>
                            <el-table
                                ref="table"
                                :data="table.data"
                                border
                                highlight-current-row
                                size="mini">
                                <el-table-column
                                    fixed
                                    type="index">
                                    <template slot="header">
                                        #
                                    </template>
                                </el-table-column>
                                <template v-for="(column,index) in table.columnList">
                                    <el-table-column
                                        v-if="!column.hide"
                                        :key="index"
                                        show-overflow-tooltip
                                        v-bind="column">
                                    </el-table-column>
                                </template>
                            </el-table>
                        </el-col>
                    </el-row>
                    <el-row>
                        <template>
                            <el-tabs v-model="activeName" @tab-click="handleClick" type="border-card">
                                <el-tab-pane :label="item.lable" :name="item.name" v-for="(item, index) in tabList" :key="index">
                                    <el-table
                                        :data="publicTable.data"
                                        border
                                        highlight-current-row
                                        size="mini">
                                        <el-table-column
                                            fixed
                                            type="index">
                                            <template slot="header">
                                                #
                                            </template>
                                        </el-table-column>
                                        <template v-for="(column,index) in publicTable.columnList">
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
                        </template>
                    </el-row>
                </el-form>
            </el-main>
            <el-footer>
                <el-row style="margin-top: 10px;line-height:60px;text-align:right;">
                    <el-button
                        :loading="loading"
                        @click="onClose"
                    >关 闭
                    </el-button>
                </el-row>
            </el-footer>
        </el-container>
    </basic-container>
</template>

<script>
import {editDetailMixin} from "@/mixins/editDetail";
import {detail, getLog} from "@/api/wms/instock/asnHeader"

export default {
    name: "selectDetails",
    components: {},
    mixins: [editDetailMixin],
    props: {
        asnBillId: {type: String, required: true},
    },
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
                    asnBillId: '',
                    asnBillNo: '',
                    billTypeName: '',
                    supplierCode: '',
                    supplierName: '',
                    createTime: '',
                    createUser: '',
                    whCode: '',
                    whName: '',
                    ownerName: '',
                    asnBillRemark: '',
                }
            },
            table: {
                columnList: [
                    {
                        prop: 'skuCode',
                        label: '物品编码',
                        width: 140,
                    },
                    {
                        prop: 'skuName',
                        width: 230,
                        label: '物品名称'
                    },
                    {
                        prop: 'skuSpec',
                        width: 100,
                        label: '规格',
                        align: 'center'
                    },
                    {
                        prop: 'umName',
                        width: 100,
                        label: '计量单位',
                        align: 'center'
                    },
                    {
                        prop: 'planQty',
                        width: 100,
                        label: '计划数量',
                        align: 'center'
                    },
                    {
                        prop: 'scanQty',
                        width: 100,
                        label: '实收数量',
                        align: 'center'
                    },
                    {
                        prop: 'remark',
                        label: '备注'
                    },
                ]
            },
            //标签页list集合，根据这个集合循环出来Tab标签
            tabList:[
                {lable:'日志',name:'log'},
                {lable:'收货记录',name:'receivingRecord'}
            ],
            //tab标签默认打开第一个
            activeName:'log',
            //Tab标签所有的数据来源
            publicTable: {
                page: {
                    total: 0,
                    size: 20,
                    current: 1,
                    ascs: "", //正序字段集合
                    descs: "", //倒序字段集合
                },
            },
            //log日志的行对象
            logColumnList: [
                {
                    prop: 'userAccount',
                    label: '操作人账号',
                    width: 300,
                },
                {
                    prop: 'userRealName',
                    label: '操作人名称'
                },
                {
                    prop: 'type',
                    label: '操作类型',
                    align: 'right'
                },
                {
                    prop: 'billId',
                    label: '目标单据id'
                },
                {
                    prop: 'billNo',
                    label: '目标单据编码'
                },
                {
                    prop: 'log',
                    label: '操作内容'
                },
                {
                    prop: 'updateTime',
                    label: '变更时间'
                },
            ],
            //收货记录的行对象
            receivingRecordColumnList: [
                {
                    prop: 'name',
                    label: '姓名',
                    width: 300,
                },
                {
                    prop: 'date',
                    label: '日期'
                },
                {
                    prop: 'wages',
                    label: '工资',
                    // left/center/right
                    align: 'right'
                },
                {
                    prop: 'date',
                    label: '日期'
                },
                {
                    prop: 'address',
                    label: '地址'
                },
                {
                    prop: 'address',
                    label: '接收记录'
                },
            ]
        }
    },
    mounted() {

    },
    created() {
        this.getTableData();
        //初始化选择Tab行对象是Log日志行对象
        this.publicTable.columnList=this.logColumnList;
        //获取日志的数据
        this.getLog();
    },
    watch: {
        asnBillId() {
            this.refreshTable();
        }
    },
    methods: {
        refreshTable() {
            this.getTableData();
        },
        getTableData() {
            // API调用:post(this.searchFrom)
            let asnBillIdObj = {
                asnBillId: this.asnBillId,
            }
            detail(asnBillIdObj)
                .then((res)=>{
                    let asnHeader = res.data.data.asnHeaderViewResponse;
                    let asnDetailList = res.data.data.asnDetailViewResponse;
                    // let asnReceiveList = res.data.data.asnReceiveViewResponse;
                    this.form.params = asnHeader;
                    this.table.data = asnDetailList
                })
        },
        //获取日志数据---跟后台交互
        getLog() {
            let asnBillIdObj = {
                asnBillId: this.asnBillId
            }
            getLog(asnBillIdObj)
                .then((res)=>{
                    this.publicTable.data = res.data.data;
                })
        },
        //获取收货记录数据---跟后台交互
        getReceivingRecord() {
            // API调用:post(this.searchFrom)

        },
        //点击Tab的时候进行判断，然后获取对应数据及行对象
        handleClick(tab) {
            if(tab.name===this.tabList[0].name)
            {
                this.publicTable.columnList=this.logColumnList;
                this.getLog();

            }else {
                this.publicTable.columnList=this.receivingRecordColumnList;
                this.getReceivingRecord();
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
