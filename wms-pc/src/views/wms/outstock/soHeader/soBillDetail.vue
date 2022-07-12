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
                         style="margin-left:10px;margin-right:10px;"
                >
                    <el-row>
                        <h3>出库订单</h3>
                    </el-row>
                    <el-row type="flex">
                        <el-col :span="8">
                            <el-form-item label="出库单编码：">
                                {{ form.params.soBillNo }}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="单据类型：">
                                {{ form.params.billType }}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="所属库房：">
                                {{ form.params.whName }}
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row type="flex">
                        <el-col :span="8">
                            <el-form-item label="所属货主：">
                                {{ form.params.ownerName }}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="客户：">
                                {{ form.params.customerName }}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="出库方式：">
                                {{ form.params.outstockType }}
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row type="flex">
                        <el-col :span="8">
                            <el-form-item label="发货方式：">
                                {{ form.params.transportType }}
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row type="flex">
                        <el-col :span="8">
                            <el-form-item label="备注：">
                                {{ form.params.soBillRemark }}
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <template>
                            <el-tabs v-model="activeName" type="border-card" @tab-click="handleClick">
                                <el-tab-pane v-for="(item, index) in tabList" :key="index" :label="item.lable"
                                             :name="item.name">
                                    <el-table
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
import NodesSku from "@/components/wms/select/NodesSku";
import NodesLineNumber from "@/components/wms/table/NodesLineNumber";
import {editDetailMixin} from "@/mixins/editDetail";
import {listMixin} from "@/mixins/list";
import {getDetailForDetail, getHeaderForDetail, getLogSoPickForDetail} from "@/api/wms/outstock/soHeader"
import NodesBillType from "@/components/wms/select/NodesBillType";
import NodesCustomer from "@/components/wms/select/NodesCustomer";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import NodesOwner from "@/components/wms/select/NodesOwner";
import NodesSkuUm from "@/components/wms/select/NodesSkuUm";
import NodesDictionary from "@/components/wms/select/NodesDictionary";
import func from "@/util/func";

export default {
    name: "detail",
    components: {
        NodesSkuUm, NodesOwner, NodesWarehouse,
        NodesCustomer, NodesDictionary,
        NodesBillType, NodesLineNumber, NodesSku,
    },
    mixins: [editDetailMixin, listMixin],
    props: {
        soBillId: {type: String, required: true},
    },
    data() {
        return {
            form: {
                params: {
                    soBillNo: '',
                    billType: '',
                    whName: '',
                    ownerName: '',
                    customerName: '',
                    outstockType: '',
                    transportType: '',
                    soBillRemark: ''
                }
            },
            table: {
                columnList: []
            },
            //标签页list集合，根据这个集合循环出来Tab标签
            tabList: [
                {lable: '出库单明细', name: 'soDetail'},
                {lable: '拣货记录', name: 'soRecord'},
            ],
            //tab标签默认打开第一个
            activeName: 'soDetail',
            //Tab标签所有的数据来源
            publicTable: {
                data: [],
            },
            //明细的行对象
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
            //出库记录的行对象
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
            this.publicTable.columnList = this.soDetailColumnList;
            if (func.isEmpty(this.soBillId)) {
                return;
            }
            getDetailForDetail(this.page, this.soBillId)
                .then((res) => {
                    this.publicTable.data = res.data.data.records;
                    this.page.total = res.data.data.total;
                })
        },
        getSoRecord() {
            this.publicTable.columnList = this.soRecordColumnList;
            if (func.isEmpty(this.soBillId)) {
                return;
            }
            getLogSoPickForDetail(this.page, this.soBillId)
                .then((res) => {
                    this.publicTable.data = res.data.data.records;
                    this.page.total = res.data.data.total;
                })
        },
        //点击Tab的时候进行判断，然后获取对应数据及行对象
        handleClick(tab) {
            this.form.activeName = tab.name;
            this.resetPage();
            this.getTableData();
        },
        // 重置分页对象
        resetPage(){
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
            } else {
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
</style>
