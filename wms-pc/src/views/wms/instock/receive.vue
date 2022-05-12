<template>
    <div id="list">
        <nodes-master-page :configure="masterConfig" :permission="permissionObj" v-on="form.events">
            <template v-slot:searchFrom>
                <el-form-item label="收货单编码">
                    <el-input v-model="form.params.receiveNo" class="d-input"></el-input>
                </el-form-item>
                <el-form-item label="单据状态">
                    <nodes-receive-bill-state v-model="form.params.billStateList"></nodes-receive-bill-state>
                </el-form-item>
                <el-form-item label="物品编码">
                    <nodes-sku v-model="form.params.skuIds" style="width: 200px"  :multiple="true"></nodes-sku>
                </el-form-item>

                <el-form-item label="上游编码">
                    <el-input v-model="form.params.externalOrderNo" class="d-input"></el-input>
                </el-form-item>
            </template>
            <template v-slot:expandSearch>
                <el-row type="flex">
                    <el-col :span="24">
                        <el-form-item label="ASN单编码">
                            <el-input v-model="form.params.asnBillNo" class="d-input"></el-input>
                        </el-form-item>
                        <el-form-item label="仓库编码">
                              <nodes-warehouse v-model="form.params.whIds" :multiple="true"></nodes-warehouse>
                        </el-form-item>
                        <el-form-item label="上游创建人">
                            <el-input v-model="form.params.externalCreateUser" class="d-input"></el-input>
                        </el-form-item>
                        <el-form-item label="供应商编码">
                            <el-input v-model="form.params.supplierCode" class="d-input"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row type="flex">
                    <el-col :span="24">

                        <el-form-item label="创建日期">
                            <nodes-date-range v-model="form.params.createTimeDateRange"></nodes-date-range>
                        </el-form-item>

                    </el-col>
                </el-row>

            </template>
            <template v-slot:batchBtn>
                <el-button  icon="el-icon-plus" size="mini" type="primary">新增</el-button>
                <el-button  size="mini" type="danger" @click="onRemove" icon="el-icon-delete"
                           :plain="false">删除
                </el-button>
            </template>
            <template v-slot:tableTool>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="刷新"
                    effect="dark"
                    placement="top"
                >
                    <el-button circle icon="el-icon-refresh" size="mini" @click="onRefresh"></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="显隐"
                    effect="dark"
                    placement="top"
                >
                    <el-button circle icon="el-icon-s-operation" size="mini" @click="onColumnShowHide"></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="本地导出"
                    effect="dark"
                    placement="top"
                >
                    <el-button circle icon="el-icon-bottom" size="mini"></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="服务端导出"
                    effect="dark"
                    placement="top"
                >
                    <el-button circle icon="el-icon-download" @click="exportData" size="mini"></el-button>
                </el-tooltip>
            </template>
            <template v-slot:table>
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
                            v-if="!column.hide"
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column">
                        </el-table-column>
                    </template>
                    <el-table-column
                        fixed="right"
                        label="操作"
                        width="100">
                        <template slot-scope="scope">
                            <el-button @click="handleClick(scope.row)" type="text" size="small">编辑</el-button>
                            <el-button @click="onClose(scope.row)" type="text" size="small" >关闭</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination
                    :current-page="page.current"
                    :page-size="page.size"
                    :page-sizes="[20, 50, 100]"
                    :total="page.total"
                    background
                    layout="total, sizes, prev, pager, next, jumper"
                    v-bind="page"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange">
                </el-pagination>
            </template>
        </nodes-master-page>
        <div v-if="columnShowHide.visible">
            <dialog-column
                v-bind="columnShowHide"
                @close="onColumnShowHide">
            </dialog-column>
        </div>
    </div>
</template>

<script>


import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import NodesAsnBillState from "@/components/wms/select/NodesAsnBillState";
import NodesInStoreMode from "@/components/wms/select/NodesInStoreMode";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import {page,remove,close,exportFile} from "@/api/wms/instock/receive";
import {listMixin} from "@/mixins/list";
import fileDownload from "js-file-download";
import NodesReceiveBillState from "../../../components/wms/select/NodesReceiveBillState";
import NodesCustomer from "@/components/wms/select/NodesCustomer";
import NodesLocation from "@/components/wms/select/NodesLocation";
import NodesSku from "@/components/wms/select/NodesSku";



export default {
    name: "list",
    components: {
        NodesSku,
        NodesLocation,
        NodesCustomer,
        NodesReceiveBillState,
        DialogColumn,
        NodesInStoreMode,
        NodesWarehouse,
        NodesAsnBillState,
        NodesMasterPage,
        NodesDateRange
    },
    mixins: [listMixin],
    data() {
        return {
            masterConfig: {
                showExpandBtn: true,
                showPage: true
            },
            form: {
                params: {
                    code:[],
                    receiveNo: '',
                    billStateList: [],
                    skuIds: [],
                    externalOrderNo: '',
                    asnBillNo: '',
                    whIds: [],
                    externalCreateUser: '',
                    supplierCode: '',
                    createTimeDateRange: '',
                }
            },
            billState:"",
            nums:{
                receiveId:'',
            },

            table: {
                columnList: [
                    {
                        prop: 'receiveNo',
                        label: '收货单编码',
                        width: 120,
                        sortable: 'custom'
                    },
                    {
                        prop: 'billStateDesc',
                        label: '单据状态'
                    },

                    {
                        prop: 'asnBillNo',
                        width: 100,
                        label: 'ASN单编码'
                    },
                    {
                        prop: 'externalOrderNo',
                        label: '上游编码',
                        align: 'right'
                    },
                    {
                        prop: 'supplierCode',
                        width: 100,
                        label: '供应商编码'
                    },
                    {
                        prop: 'supplierName',
                        width: 100,
                        label: '供应商名称'
                    },
                    {
                        prop: 'whCode',
                        label: '仓库编码'
                    },
                    {
                        prop: 'createTime',
                        label: '创建日期'
                    },
                    {
                        prop: 'createUser',
                        label: '创建人'
                    },
                    {
                        prop: 'externalPreCreateDate',
                        width: 120,
                        label: '上游创建时间'
                    },
                    {
                        prop: 'externalCreateUser',
                        width: 100,
                        label: '上游创建人'
                    },
                    {
                        prop: 'updateUser',
                        label: '更新人'
                    },
                    {
                        prop: 'updateTime',
                        label: '更新时间'
                    },
                    {
                        prop: 'remark',
                        label: '消息'
                    },

                ]
            },
        }
    },
    created() {

    },
    methods: {
        getTableData() {
            page(this.page, this.form.params)
                .then((res) => {
                    let pageObj = res.data.data;
                    this.table.data = pageObj.records;
                    this.page.total = pageObj.total;
                })
                .catch(() => {
                });
        },
        onAdd() {
            let requestParams = {
                type: 'NEW',
                id: 0,
                parent: {
                    path: this.$route.path,
                    name: this.$route.name
                }
            };
            this.$router.push({
                name: 'demoEdit',
                params: requestParams,
                meta: {
                    parent: this
                }
            });
        },
        handleClick(row){

},
        onRemove() {
            this.$confirm("确定删除当前数据？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                let a = 1;
                this.$refs.table.selection.forEach(e => {
                    if(a>1) {
                        this.$message.error('暂不支持批量删除');
                        throw new Error('暂不支持批量删除');
                    }
                    this.nums.receiveId = e.receiveId
                    a++;
                })
                remove(this.nums)
                    .then(() => {
                        this.$message.success('删除成功');
                        this.getTableData();
                    })
                    .catch(() => {
                    });


            })
        },
        onClose(row) {
            this.$confirm("确定关闭当前收货单？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
               let state = row.billState;
                if(state===40){
                    this.$message.error('该收货单已关闭，请勿重复点击');
                    throw new Error('该收货单已关闭，请勿重复点击');
                }

               if(state!==30){
                   this.$message.error('关闭失败,请先完成收货');
                   throw new Error('关闭失败,请先完成收货');
               }

                this.nums.receiveId = row.receiveId;
                close(this.nums)
                    .then(() => {
                        this.$message.success('关闭成功');

                        this.getTableData();
                    })
                    .catch(() => {
                    });
            })
        },
        exportData() {
            this.loading = true;
            exportFile(this.form.params)
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, "收货管理列表.xlsx");
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        onReset() {
            this.form.params = {};
        },

    }
}
</script>
