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
                                <span>{{form.params.asnBillNo}}</span>
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
                                size="mini"
                                @sort-change="onSortChange">
                                <el-table-column
                                    fixed
                                    type="selection"
                                    width="50">
                                </el-table-column>
                                <el-table-column
                                    fixed
                                    sortable
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
                                        size="mini"
                                        @sort-change="onSortChange">
                                        <el-table-column
                                            fixed
                                            type="selection"
                                            width="50">
                                        </el-table-column>
                                        <el-table-column
                                            fixed
                                            sortable
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
                                    <el-pagination
                                        :page-sizes="[20, 50, 100]"
                                        background
                                        layout="total, sizes, prev, pager, next, jumper"
                                        v-bind="page"
                                        @size-change="handleSizeChange"
                                        @current-change="handleCurrentChange" style="margin-top: 10px;text-align:right;">
                                    </el-pagination>
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
import {detail} from "@/api/wms/instock/asnHeader"

export default {
    name: "selectDetails",
    components: {NodesLineNumber, NodesSku, NodesInStoreMode},
    mixins: [editDetailMixin,listMixin],
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
                        width: 300,
                        sortable: 'custom'
                    },
                    {
                        prop: 'skuName',
                        label: '物品名称'
                    },
                    {
                        prop: 'skuSpec',
                        label: '规格',
                        align: 'right'
                    },
                    {
                        prop: 'umName',
                        label: '计量单位'
                    },
                    {
                        prop: 'planQty',
                        label: '计划数量'
                    },
                    {
                        prop: 'scanQty',
                        label: '实收数量'
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
                    prop: 'name',
                    label: '姓名',
                    width: 300,
                    sortable: 'custom'
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
                    label: '日志'
                },
            ],
            //收货记录的行对象
            receivingRecordColumnList: [
                {
                    prop: 'name',
                    label: '姓名',
                    width: 300,
                    sortable: 'custom'
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
    },mounted() {

    },
    created() {
        this.getTableData();
        //初始化选择Tab行对象是Log日志行对象
        this.publicTable.columnList=this.logColumnList;
        //获取日志的数据
        this.getLog();
    },
    methods: {
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
        createRowObj() {

        },
        //获取日志数据---跟后台交互
        getLog() {
            // API调用:post(this.searchFrom)
            function getRandomInt(min, max) {
                min = Math.ceil(min);
                max = Math.floor(max);
                return Math.floor(Math.random() * (max - min)) + min; //不含最大值，含最小值
            }

            let fill = [];
            for (let i = 0; i < 101; i++) {
                // 模拟表格数据
                let item = {
                    id: i + 1,
                    date: `${getRandomInt(2018, 2022)}-${getRandomInt(1, 12)}-${getRandomInt(1, 28)}`,
                    name: "王小虎日志" + getRandomInt(1, 101),
                    wages: getRandomInt(3000, 15000),
                    address: `上海市普陀区金沙江路 ${getRandomInt(100, 2000)} 弄`
                };
                fill.push(item);
            }
            let length = fill.length;
            this.publicTable.page.total = length;
            let offset = (this.publicTable.page.current - 1) * this.publicTable.page.size;
            let number = offset + this.publicTable.page.size;
            this.publicTable.data = (number >= length)
                ? fill.slice(offset, length)
                : fill.slice(offset, number);
        },
        //获取收货记录数据---跟后台交互
        getReceivingRecord() {
            // API调用:post(this.searchFrom)
            function getRandomInt(min, max) {
                min = Math.ceil(min);
                max = Math.floor(max);
                return Math.floor(Math.random() * (max - min)) + min; //不含最大值，含最小值
            }

            let fill = [];
            for (let i = 0; i < 101; i++) {
                // 模拟表格数据
                let item = {
                    id: i + 1,
                    date: `${getRandomInt(2018, 2022)}-${getRandomInt(1, 12)}-${getRandomInt(1, 28)}`,
                    name: "王小虎接收记录" + getRandomInt(1, 101),
                    wages: getRandomInt(3000, 15000),
                    address: `上海市普陀区金沙江路 ${getRandomInt(100, 2000)} 弄`
                };
                fill.push(item);
            }
            let length = fill.length;
            this.publicTable.page.total = length;
            let offset = (this.publicTable.page.current - 1) * this.publicTable.page.size;
            let number = offset + this.publicTable.page.size;
            this.publicTable.data = (number >= length)
                ? fill.slice(offset, length)
                : fill.slice(offset, number);
        },
        //点击Tab的时候进行判断，然后获取对应数据及行对象
        handleClick(tab) {
            if(tab.name==this.tabList[0].name)
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
