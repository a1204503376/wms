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
                    <el-descriptions
                        :column="4"
                        border
                        class="margin-top"
                        style="margin-bottom: 20px">
                        <el-descriptions-item
                            label-class-name="descriptions-label"
                            content-class-name="descriptions-content">
                            <template slot="label">
                                <i class="el-icon-user"></i>
                                ASN单编码
                            </template>
                            {{ form.params.asnBillNo }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            label-class-name="descriptions-label"
                            content-class-name="descriptions-content">
                            <template slot="label">
                                <i class="el-icon-mobile-phone"></i>
                                单据类型
                            </template>
                            {{ form.params.billTypeName }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            label-class-name="descriptions-label"
                            content-class-name="descriptions-content">
                            <template slot="label">
                                <i class="el-icon-location-outline"></i>
                                供应商
                            </template>
                            {{ form.params.supplierName }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            label-class-name="descriptions-label"
                            content-class-name="descriptions-content">
                            <template slot="label">
                                <i class="el-icon-tickets"></i>
                                仓库
                            </template>
                            <el-tag size="small">{{ form.params.whName }}</el-tag>
                        </el-descriptions-item>
                        <el-descriptions-item
                            label-class-name="descriptions-label"
                            content-class-name="descriptions-content">
                            <template slot="label">
                                <i class="el-icon-office-building"></i>
                                货主
                            </template>
                            {{ form.params.ownerName }}
                        </el-descriptions-item>
                        <el-descriptions-item
                            label-class-name="descriptions-label"
                            content-class-name="descriptions-content">
                            <template slot="label">
                                <i class="el-icon-tickets"></i>
                                备注
                            </template>
                            <i>{{ form.params.asnBillRemark }}</i>
                        </el-descriptions-item>
                    </el-descriptions>
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
                        @click="onClose"
                    >关 闭
                    </el-button>
                </el-row>
            </el-footer>
        </el-container>
    </basic-container>
</template>

<script>
import {editMixin} from "@/mixins/edit";
import {listMixin} from "@/mixins/list";
import {getAsnDetail, getAsnHeader, getAsnLog, getReceive} from "@/api/wms/instock/asnHeader"

export default {
    name: "selectDetails",
    components: {},
    mixins: [editMixin,listMixin],
    props: {
        asnBillId: {type: String, required: true},
    },
    data() {
        return {
            that: this,
            form: {
                params: {}
            },
            tabList: [
                {lable: 'ASN单明细', name: 'detail'},
                {lable: '收货记录', name: 'receive'},
                {lable: '日志', name: 'log'},
            ],
            activeName: 'detail',
            publicTable: {
                data: [],
                columnList: [],
            },
            detailColumnList: [
                {
                    prop: 'asnLineNo',
                    label: '行号',
                    sortable: 'custom',
                    align: 'center'
                },
                {
                    prop: 'skuCode',
                    label: '物品编码',
                    sortable: 'custom',
                    align: 'center'
                },
                {
                    prop: 'skuName',
                    label: '物品名称',
                    sortable: 'custom',
                    align: 'center'
                },
                {
                    prop: 'umName',
                    label: '计量单位',
                    sortable: 'custom',
                    align: 'center'
                },
                {
                    prop: 'planQty',
                    label: '计划数量',
                    sortable: 'custom',
                    align: 'center'
                },
                {
                    prop: 'scanQty',
                    label: '实收数量',
                    sortable: 'custom',
                    align: 'center'
                },
                {
                    prop: 'detailStatus',
                    label: '状态',
                    sortable: 'custom',
                    align: 'center'
                },
                {
                    prop: 'remark',
                    label: '备注',
                    sortable: 'custom',
                    align: 'center'
                },
            ],
            receiveColumnList: [
                {
                    prop: 'receiveNo',
                    label: '收货单编码',
                    sortable: 'custom',
                    align: 'center'
                },
                {
                    prop: 'billState',
                    label: '收货单状态',
                    sortable: 'custom',
                    align: 'center'
                },
                {
                    prop: 'remark',
                    label: '备注',
                    sortable: 'custom',
                    align: 'center'
                },
                {
                    prop: 'createTime',
                    label: '创建时间',
                    sortable: 'custom',
                    align: 'center'
                },
                {
                    prop: 'createUser',
                    label: '创建人',
                    sortable: 'custom',
                    align: 'center'
                },
                {
                    prop: 'updateTime',
                    label: '创建时间',
                    sortable: 'custom',
                    align: 'center'
                },
                {
                    prop: 'updateUser',
                    label: '创建人',
                    sortable: 'custom',
                    align: 'center'
                },
            ],
            logColumnList: [
                {
                    prop: 'userAccount',
                    label: '操作人员账号',
                    sortable: 'custom',
                    align: 'center'
                },
                {
                    prop: 'userRealName',
                    label: '操作人员姓名',
                    sortable: 'custom',
                    align: 'center'
                },
                {
                    prop: 'log',
                    label: '操作内容',
                    sortable: 'custom',
                    align: 'center'
                },
                {
                    prop: 'createTime',
                    label: '操作时间',
                    align: 'center',
                    sortable: 'custom',
                },
            ]
        }
    },
    created() {
        this.refreshTable();
    },
    watch: {
        asnBillId() {
            this.refreshTable();
        }
    },
    methods: {
        refreshTable() {
            this.getAsnHeader();
            this.getTableData();
        },
        getTableData() {
            if (this.activeName === 'log') {
                this.publicTable.columnList = this.logColumnList;
                this.getAsnLog();
            } else if (this.activeName === 'receive'){
                this.publicTable.columnList = this.receiveColumnList;
                this.getReceive();
            } else {
                this.publicTable.columnList = this.detailColumnList;
                this.getAsnDetail();
            }
        },
        getAsnHeader() {
            getAsnHeader(this.asnBillId).then((res) => {
                this.form.params = res.data.data;
            })
        },
        getAsnDetail() {
            getAsnDetail(this.page, this.asnBillId).then((res) => {
                this.publicTable.data = res.data.data.records;
                this.page.total = res.data.data.total;
            })
        },
        getReceive() {
            getReceive(this.page, this.asnBillId).then((res) => {
                this.publicTable.data = res.data.data.records;
                this.page.total = res.data.data.total;
            })
        },
        getAsnLog() {
            getAsnLog(this.page, this.asnBillId)
                .then((res) => {
                    this.publicTable.data = res.data.data.records;
                    this.page.total = res.data.data.total;
                })
        },
        handleClick(tab) {
            this.activeName = tab.name;
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
        getCrudColumnList() {
            // 覆盖混入的list中的方法
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

/deep/ .descriptions-label {
    width: 150px;
    color: #00a680;
}

/deep/ .descriptions-content {
    width: 250px;
}
</style>
