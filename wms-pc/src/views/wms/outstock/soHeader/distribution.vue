<template>
    <basic-container>
        <el-container id="container" v-loading="loading">
            <el-main style="overflow: hidden;overflow-y: scroll;height: 100%">
                <el-form ref="form"
                         :model="form.params"
                         label-position="right"
                         label-width="120px"
                         size="mini"
                         style="margin-left:10px;margin-right:10px;">
                    <el-row>
                        <el-row :gutter="10" type="flex">
                            <el-col :span="4">
                                <el-form-item label="发货单编码：" label-width="100px">
                                    <label>{{ form.params.soBillNo }}</label>
                                </el-form-item>
                            </el-col>
                            <el-col :span="4">
                                <el-form-item label="上游编码：" label-width="120px">
                                    {{ form.params.orderNo }}
                                </el-form-item>
                            </el-col>
                            <el-col :offset="8" :span="2">
                                <el-button class="top_button" size="medium" type="primary">
                                    自动分配
                                </el-button>
                            </el-col>
                            <el-col :span="2">
                                <el-button class="top_button" size="medium" type="primary">
                                    全部取消
                                </el-button>
                            </el-col>
                            <el-col :span="2">
                                <el-button class="top_button" size="medium" type="primary">
                                    确认下发
                                </el-button>
                            </el-col>
                            <el-col :span="2">
                                <el-button :loading="loading" class="top_button"
                                           plain size="medium" @click="onClose">关 闭
                                </el-button>
                            </el-col>
                        </el-row>
                    </el-row>
                    <el-row :gutter="10" style="margin-bottom: 20px; margin-top: 20px">
                        <el-col :span="8">
                            <el-row>
                                <el-col>
                                    <div style="line-height: 53px ;height:53px ; width: auto">收货单明细</div>
                                </el-col>
                            </el-row>
                            <el-table
                                ref="table"
                                :data="table.data"
                                border
                                highlight-current-row
                                size="mini"
                                title="发货单明细">
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
                        <el-col :span="16">
                            <template>
                                <el-tabs v-model="activeName" type="border-card" @tab-click="handleClick">
                                    <el-tab-pane v-for="(item, index) in tabList" :key="index" :label="item.lable"
                                                 :name="item.name">
                                        <el-table
                                            :data="publicTable.data"
                                            :span-method="objectSpanMethod"
                                            border
                                            highlight-current-row
                                            size="mini">
                                            <template v-for="(column,index) in publicTable.columnList">
                                                <el-table-column
                                                    v-if="!column.hide"
                                                    :key="index"
                                                    show-overflow-tooltip
                                                    v-bind="column">
                                                </el-table-column>
                                            </template>
                                            <el-table-column align="center" fixed="right" label="操作" width="100">
                                                <template v-slot="scope">
                                                    <el-button size="small" type="text" @click="onEdit(scope.row)">
                                                        编辑
                                                    </el-button>
                                                    <el-button size="small" type="text" @click="onCancel(scope.row)">
                                                        取消
                                                    </el-button>
                                                </template>
                                            </el-table-column>
                                        </el-table>
                                    </el-tab-pane>
                                </el-tabs>
                            </template>
                        </el-col>
                    </el-row>
                </el-form>
            </el-main>
        </el-container>
    </basic-container>
</template>


<script>
import {editMixin} from "@/mixins/edit";
import {getSoBillDataByDistribution} from "@/api/wms/outstock/soHeader"
import {} from "@/api/wms/stock/stock"

export default {
    name: "distribution",
    components: {},
    mixins: [editMixin],
    props: {
        soBillId: {type: String, required: true},
    },
    data() {
        return {
            that: this,
            loading: false,
            page: {
                total: 0,
                size: 20,
                current: 1,
                ascs: "", //正序字段集合
                descs: "", //倒序字段集合
            },
            form: {
                params: {
                    soBillId: '',
                    soBillNo: '',
                    orderNo: ''
                }
            },
            table: {
                columnList: [
                    {
                        prop: 'soLineNo',
                        width: 50,
                        label: '行号'
                    },
                    {
                        prop: 'skuCode',
                        label: '物品编码'
                    },
                    {
                        prop: 'surplusQty',
                        label: '剩余量',
                        align: 'center'
                    },
                    {
                        prop: 'distributeQty',
                        label: '分配量'
                    },
                    {
                        prop: 'planQty',
                        label: '计划量',
                        align: 'center'
                    },
                    {
                        prop: 'umCode',
                        label: '计量单位编码',
                        width: 100,
                        align: 'center'
                    },
                ]
            },
            //标签页list集合，根据这个集合循环出来Tab标签
            tabList: [
                {lable: '按件', name: 'piece'},
                {lable: '按箱', name: 'box'}
            ],
            activeName: 'piece',
            publicTable: {
                page: {
                    total: 0,
                    size: 20,
                    current: 1,
                    ascs: '',
                    descs: '',
                },
            },
            pieceColumnList: [
                {
                    prop: 'skuCode',
                    label: '物品编码'
                },
                {
                    prop: 'lotNumber',
                    label: '批次'
                },
                {
                    prop: 'distributeQty',
                    label: '分配'
                },
                {
                    prop: 'billId',
                    label: '可用'
                },
                {
                    prop: 'billNo',
                    width: 100,
                    label: '目标单据编码'
                },
                {
                    prop: 'log',
                    label: '余额'
                },
                {
                    prop: 'updateTime',
                    label: '库位'
                },
                {
                    prop: 'updateTime',
                    label: '库区'
                },
                {
                    prop: 'updateTime',
                    label: '箱码'
                },
                {
                    prop: 'updateTime',
                    label: 'LPN'
                },
                {
                    prop: 'updateTime',
                    label: '状态'
                },
                {
                    prop: 'skuLot1',
                    label: '生产批次'
                },
                {
                    prop: 'skuLot2',
                    label: '规格型号'
                },
                {
                    prop: 'skuLot3',
                    label: '收货日期'
                },
                {
                    prop: 'skuLot4',
                    label: '专用客户'
                },
                {
                    prop: 'skuLot5',
                    label: '钢背批次'
                },
                {
                    prop: 'skuLot6',
                    width: 100,
                    label: '摩擦快批次'
                },
                {
                    prop: 'skuLot7',
                    width: 100,
                    label: '产品标识代码'
                },
                {
                    prop: 'skuLot8',
                    width: 100,
                    label: '是否CRCC校验'
                },
            ],
            boxColumnList: [
                {
                    prop: 'name',
                    label: '箱码',
                },
                {
                    prop: 'date',
                    label: '分配'
                },
                {
                    prop: 'wages',
                    label: '库区',
                },
                {
                    prop: 'date',
                    label: '库位'
                },
                {
                    prop: 'address',
                    label: 'LPN'
                },
                {
                    prop: 'address',
                    label: '物品编码'
                },
                {
                    prop: 'date',
                    label: '批次'
                },
                {
                    prop: 'address',
                    label: '可用'
                },
                {
                    prop: 'address',
                    label: '约'
                },
                {
                    prop: 'date',
                    label: '状态'
                },
                {
                    prop: 'skuLot1',
                    label: '生产批次'
                },
                {
                    prop: 'skuLot2',
                    label: '规格型号'
                },
                {
                    prop: 'skuLot3',
                    label: '收货日期'
                },
                {
                    prop: 'skuLot4',
                    label: '专用客户'
                },
                {
                    prop: 'skuLot5',
                    label: '钢背批次'
                },
                {
                    prop: 'skuLot6',
                    width: 100,
                    label: '摩擦快批次'
                },
                {
                    prop: 'skuLot7',
                    width: 100,
                    label: '产品标识代码'
                },
                {
                    prop: 'skuLot8',
                    width: 100,
                    label: '是否CRCC校验'
                },
            ]
        }
    },
    mounted() {

    },
    created() {
        this.getTableData();
    },
    watch: {
        soBillId() {
            this.refreshTable();
        }
    },
    methods: {
        objectSpanMethod({ row, column, rowIndex, columnIndex }){

        },
        refreshTable() {
            this.getTableData();
        },
        getTableData() {
            getSoBillDataByDistribution(this.soBillId).then((res) => {
                let data = res.data.data;
                let params = this.form.params;
                params.soBillId = data.soBillId;
                params.soBillNo = data.soBillId;
                params.orderNo = data.orderNo;
                data.soDetailList.forEach(item => {
                    item.distributeQty = 0;
                })
                this.table.data = data.soDetailList;
            })
            this.publicTable.columnList = this.pieceColumnList;
            this.getTablePaneDataByPiece();
        },
        getTablePaneDataByPiece() {

        },
        getTablePaneDataByBox() {

        },
        handleClick(tab) {
            if (tab.name === this.tabList[0].name) {
                this.publicTable.columnList = this.pieceColumnList;
                this.getTablePaneDataByPiece();

            } else {
                this.publicTable.columnList = this.boxColumnList;
                this.getTablePaneDataByBox();
            }
        },
        onEdit() {

        },
        onCancel() {

        },
        createRowObj() {
            // 覆盖混入的方法
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

.top_button {
    float: right;
}
</style>
