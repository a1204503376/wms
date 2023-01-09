<template>
    <basic-container>
        <el-container id="container" v-loading="loading">
            <el-main style="overflow: hidden;overflow-y: scroll;">
                <el-form ref="form"
                         :model="form.params"
                         :rules="form.rules"
                         label-position="right"
                         label-width="150px"
                         size="medium"
                         style="margin-left:10px;margin-right:10px;">
                    <el-descriptions
                        :column="4"
                        border
                        class="margin-top"
                        style="margin-bottom: 20px">
                        <el-descriptions-item
                            content-class-name="descriptions-content"
                            label-class-name="descriptions-label"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                                出库单编码
                            </template>
                            {{ form.params.soBillNo }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            content-class-name="descriptions-content"
                            label-class-name="descriptions-label"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                                单据类型
                            </template>
                            {{ form.params.billTypeName }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            content-class-name="descriptions-content"
                            label-class-name="descriptions-label"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                                所属库房
                            </template>
                            {{ form.params.whName }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            content-class-name="descriptions-content"
                            label-class-name="descriptions-label"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                                所属货主
                            </template>
                            {{ form.params.ownerName }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            v-if="form.params.billTypeCd !== this.$commonConst.BILL_TYPE_LEND"
                            content-class-name="descriptions-content"
                            label-class-name="descriptions-label"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                                客户
                            </template>
                            {{ form.params.customerName }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            v-if="form.params.billTypeCd === this.$commonConst.BILL_TYPE_LEND"
                            content-class-name="descriptions-content"
                            label-class-name="descriptions-label"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                                借用人
                            </template>
                            {{ form.params.contact }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            content-class-name="descriptions-content"
                            label-class-name="descriptions-label"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                                出库方式
                            </template>
                            {{ form.params.outstockType }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            content-class-name="descriptions-content"
                            label-class-name="descriptions-label"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                                发货方式
                            </template>
                            {{ form.params.transportType }}
                        </el-descriptions-item>
                         <el-descriptions-item
                            content-class-name="descriptions-content"
                            label-class-name="descriptions-label"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                               合同编号
                            </template>
                            {{ form.params.expressCode }}
                        </el-descriptions-item>
                         <el-descriptions-item
                            content-class-name="descriptions-content"
                            label-class-name="descriptions-label"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                               收货地址
                            </template>
                            {{ form.params.address }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            content-class-name="descriptions-content"
                            label-class-name="descriptions-label"
                            :label-style="{'text-align': 'right'}">
                            <template slot="label">
                                备注
                            </template>
                            {{ form.params.soBillRemark }}
                        </el-descriptions-item>
                    </el-descriptions>
                    <el-row>
                        <template>
                            <el-tabs v-model="activeName" type="border-card" @tab-click="handleClick">
                                <el-tab-pane v-for="(item, index) in tabList" :key="index" :label="item.lable"
                                             :name="item.name">
                                    <el-table
                                        v-loading="tableLoading"
                                        :data="publicTable.data"
                                        border
                                        highlight-current-row
                                        size="mini"
                                        @sort-change="onSortChange">
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
                                    <el-pagination
                                        :background="true"
                                        :hide-on-single-page="false"
                                        :page-sizes="[20, 50, 100]"
                                        layout="total, sizes, prev, pager, next, jumper"
                                        style="margin-top: 10px;text-align:right;"
                                        v-bind="page"
                                        @size-change="handleSizeChange"
                                        @current-change="handleCurrentChange">
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
                        @click="onClose">关 闭
                    </el-button>
                </el-row>
            </el-footer>
        </el-container>
    </basic-container>
</template>

<script>
import {editDetailMixin} from "@/mixins/editDetail";
import {listMixin} from "@/mixins/list";
import {
    getDetailForDetail,
    getHeaderForDetail,
    getLogSoPickForDetail,
    getSoLogForDetail,
    getHistorySoPickPlanData,
} from "@/api/wms/outstock/soHeader"
import func from "@/util/func";

export default {
    name: "detail",
    mixins: [editDetailMixin, listMixin],
    props: {
        soBillId: {type: String, required: true},
    },
    data() {
        return {
            form: {
                params: {
                    soBillNo: '',
                    billTypeName: '',
                    whName: '',
                    ownerName: '',
                    customerName: '',
                    outstockType: '',
                    transportType: '',
                    soBillRemark: '',
                    address:'',
                    expressCode:''
                }
            },
            table: {
                columnList: []
            },
            tabList: [
                {lable: '出库单明细', name: 'soDetail'},
                {lable: '拣货记录', name: 'soRecord'},
                {lable: '分配记录', name: 'soPickPlan'},
                {lable: '日志', name: 'soLog'},
            ],
            activeName: 'soDetail',
            publicTable: {
                data: [],
            },
            tableLoading: false,
            soDetailColumnList: [
                {
                    prop: 'soLineNo',
                    sortable: 'custom',
                    label: '行号',
                },
                {
                    prop: 'skuCode',
                    sortable: 'custom',
                    label: '物品编码'
                },
                {
                    prop: 'skuName',
                    sortable: 'custom',
                    label: '物品名称'
                },
                {
                    prop: 'umCode',
                    sortable: 'custom',
                    label: '计量单位编码'
                },
                {
                    prop: 'umName',
                    sortable: 'custom',
                    label: '计量单位名称'
                },
                {
                    prop: 'planQty',
                    sortable: 'custom',
                    label: '计划数量'
                },
                {
                    prop: 'scanQty',
                    sortable: 'custom',
                    label: '实发数量'
                },
                {
                    prop: 'surplusQty',
                    sortable: 'custom',
                    label: '剩余数量'
                },
                {
                    prop: 'skuLot1',
                    sortable: 'custom',
                    label: '生产批次'
                },
                {
                    prop: 'skuLot4',
                    sortable: 'custom',
                    label: '专用客户'
                },
                {
                    prop: 'remark',
                    sortable: 'custom',
                    label: '备注'
                }
            ],
            soRecordColumnList: [
                {
                    prop: 'procTime',
                    sortable: 'custom',
                    label: '业务发生时间',
                },
                {
                    prop: 'locCode',
                    sortable: 'custom',
                    label: '库位编码'
                },
                {
                    prop: 'boxCode',
                    sortable: 'custom',
                    label: '箱码'
                },
                {
                    prop: 'skuCode',
                    sortable: 'custom',
                    label: '物品编码'
                },
                {
                    prop: 'skuName',
                    sortable: 'custom',
                    label: '物品名称'
                },
                {
                    prop: 'lotNumber',
                    sortable: 'custom',
                    label: '批次号'
                },
                {
                    prop: 'pickRealQty',
                    sortable: 'custom',
                    label: '拣货量'
                },
                {
                    prop: 'wspName',
                    sortable: 'custom',
                    label: '包装名称'
                },
                {
                    prop: 'wsuName',
                    sortable: 'custom',
                    label: '计量单位名称'
                },
                {
                    prop: 'skuLot1',
                    sortable: 'custom',
                    label: '生产批次'
                },
                {
                    prop: 'skuLot4',
                    sortable: 'custom',
                    label: '专用客户'
                }
            ],
            soLogColumnList: [
                {
                    prop: 'userAccount',
                    label: '操作人员账号',
                    sortable: 'custom',
                },
                {
                    prop: 'userRealName',
                    label: '操作人员姓名',
                    sortable: 'custom',
                },
                {
                    prop: 'log',
                    label: '操作内容',
                    sortable: 'custom',
                },
                {
                    prop: 'createTime',
                    label: '操作时间',
                    sortable: 'custom',
                },
            ],
            soPickPlanColumnList: [
                {
                    prop: 'boxCode',
                    label: '箱码',
                    sortable: 'custom',
                },
                {
                    prop: 'zoneCode',
                    label: '库区',
                    sortable: 'custom',
                },
                {
                    prop: 'locCode',
                    label: '库位',
                    sortable: 'custom',
                },
                {
                    prop: 'lpnCode',
                    label: 'LPN',
                    sortable: 'custom',
                },
                {
                    prop: 'pickPlanQty',
                    label: '计划分配量',
                    sortable: 'custom',
                },
                {
                    prop: 'pickRealQty',
                    label: '已拣量',
                    sortable: 'custom',
                },
                {
                    prop: 'skuCode',
                    label: '物品编码',
                    sortable: 'custom',
                },
                {
                    prop: 'skuName',
                    label: '物品名称',
                    sortable: 'custom',
                },
                {
                    prop: 'stockStatus',
                    label: '库存状态',
                    sortable: 'custom',
                },
                {
                    prop: 'skuLot1',
                    label: '生产批次',
                    sortable: 'custom',
                },
                {
                    prop: 'skuLot2',
                    label: '规格型号',
                    sortable: 'custom',
                },
                {
                    prop: 'skuLot3',
                    label: '收货日期',
                    sortable: 'custom',
                },
                {
                    prop: 'skuLot4',
                    label: '专用客户',
                    sortable: 'custom',
                },
                {
                    prop: 'skuLot5',
                    label: '钢背批次',
                    sortable: 'custom',
                },
                {
                    prop: 'skuLot6',
                    width: 100,
                    label: '摩擦快批次',
                    sortable: 'custom',
                },
                {
                    prop: 'skuLot7',
                    width: 100,
                    label: '产品标识代码',
                    sortable: 'custom',
                },
                {
                    prop: 'skuLot8',
                    width: 100,
                    label: '适用速度等级',
                    sortable: 'custom',
                },
            ],
        }
    },
    created() {
        this.getInitializeData()
    },
    watch: {
        soBillId() {
            this.getInitializeData();
        }
    },
    methods: {
        getInitializeData() {
            this.getHeader();
            this.getTableData();
        },
        getHeader() {
            getHeaderForDetail(this.soBillId)
                .then((res) => {
                    this.form.params = res.data.data;
                })
        },
        getDetail() {
            this.tableLoading = true;
            this.publicTable.columnList = this.soDetailColumnList;
            if (func.isEmpty(this.soBillId)) {
                return;
            }
            getDetailForDetail(this.page, this.soBillId)
                .then((res) => {
                    this.publicTable.data = res.data.data.records;
                    this.page.total = res.data.data.total;
                    this.tableLoading = false;
                })
        },
        getSoRecord() {
            this.tableLoading = true;
            this.publicTable.columnList = this.soRecordColumnList;
            if (func.isEmpty(this.soBillId)) {
                return;
            }
            getLogSoPickForDetail(this.page, this.soBillId)
                .then((res) => {
                    this.publicTable.data = res.data.data.records;
                    this.page.total = res.data.data.total;
                    this.tableLoading = false;
                })
        },
        getSoLog() {
            this.tableLoading = true;
            this.publicTable.columnList = this.soLogColumnList;
            if (func.isEmpty(this.soBillId)) {
                return;
            }
            getSoLogForDetail(this.page, this.soBillId)
                .then((res) => {
                    this.publicTable.data = res.data.data.records;
                    this.page.total = res.data.data.total;
                    this.tableLoading = false;
                })
        },
        getSoPickPlanList(){
            this.tableLoading = true;
            this.publicTable.columnList = this.soPickPlanColumnList;
            if (func.isEmpty(this.soBillId)) {
                return;
            }
            getHistorySoPickPlanData(this.soBillId).then((res) => {
                this.publicTable.data = res.data.data;
                this.page.total = res.data.data.length;
                this.tableLoading = false;
            })
        },
        //点击Tab的时候进行判断，然后获取对应数据及行对象
        handleClick(tab) {
            this.form.activeName = tab.name;
            this.resetPage();
            this.getTableData();
        },
        // 重置分页对象
        resetPage() {
            this.page = {
                total: 0,
                size: 20,
                current: 1,
                ascs: "",
                descs: "",
            }
        },
        createRowObj() {
        },
        getCrudColumnList() {
            // 覆盖混入的list中的方法
        },
        getTableData() {
            if (this.form.activeName === 'soRecord') {
                this.getSoRecord();
            } else if (this.form.activeName === 'soLog') {
                this.getSoLog();
            } else if(this.form.activeName === 'soPickPlan'){
                this.getSoPickPlanList();
            }else {
                this.getDetail();
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

/deep/ .descriptions-label {
    width: 150px;
    text-align: center;
}

/deep/ .el-descriptions-item__label{
    text-align: center;
}

/deep/ .descriptions-content {
    width: 250px;
}
</style>
