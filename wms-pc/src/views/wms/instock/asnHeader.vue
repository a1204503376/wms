<template>
    <div id="asnHeader">
        <nodes-master-page :configure="masterConfig" :permission="permissionObj" v-on="form.events">
            <template v-slot:searchFrom>
                <el-form-item label="ASN单编码">
                    <el-input v-model="form.params.asnBillNo" class="d-input"></el-input>
                </el-form-item>
                <el-form-item label="物品编码">
                    <nodes-sku v-model="form.params.sku" style="width: 120px"></nodes-sku>
                </el-form-item>
                <el-form-item label="状态">
                    <nodes-asn-bill-state v-model="form.params.asnBillState"></nodes-asn-bill-state>
                </el-form-item>
            </template>
            <template v-slot:expandSearch>
                <el-row type="flex">
                    <el-col :span="24">
                        <el-form-item label="创建日期">
                            <nodes-date-range v-model="form.params.createTimeDateRange"></nodes-date-range>
                        </el-form-item>
                        <el-form-item label="供应商">
                            <el-input v-model="form.params.supplier" class="d-input"></el-input>
                        </el-form-item>
                        <el-form-item label="上游编码">
                            <el-input v-model="form.params.externalOrderNo" class="d-input"></el-input>
                        </el-form-item>
                        <el-form-item label="上游创建人">
                            <el-input v-model="form.params.externalCreateUser" class="d-input"></el-input>
                        </el-form-item>
                        <el-form-item label="仓库编码">
                            <nodes-warehouse v-model="form.params.whCodeList" ></nodes-warehouse>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-slot:batchBtn>
                <el-button size="mini" type="primary" icon="el-icon-plus" @click="onAdd">新增</el-button>
                <el-button size="mini" type="danger" plain icon="el-icon-delete" @click="onRemove">删除</el-button>
            </template>
            <template v-slot:tableTool>
                <el-tooltip :enterable="false" class="item" content="刷新" effect="dark" placement="top">
                    <el-button circle icon="el-icon-refresh" size="mini" @click="onRefresh"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="显隐" effect="dark" placement="top">
                    <el-button circle icon="el-icon-s-operation" size="mini" @click="onColumnShowHide"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="本地导出" effect="dark" placement="top">
                    <el-button circle icon="el-icon-bottom" size="mini"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="服务端导出" effect="dark" placement="top">
                    <el-button circle icon="el-icon-download" size="mini" @click="exportData"></el-button>
                </el-tooltip>
            </template>
            <template v-slot:table>
                <el-table ref="table" :data="table.data" :summary-method="getSummaries" border highlight-current-row
                    show-summary size="mini" @sort-change="onSortChange">
                    <el-table-column fixed type="selection" width="50">
                    </el-table-column>
                    <template v-for="(column, index) in table.columnList">
                        <el-table-column v-if="!column.hide" :key="index" show-overflow-tooltip v-bind="column">
                        </el-table-column>
                    </template>
                    <el-table-column fixed="right" label="操作" width="100">
                        <template slot-scope="scope">
                            <el-button @click="view(scope.row)" type="text" size="small">查看</el-button>
                            <el-button type="text" size="small">编辑</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination :current-page="page.current" :page-size="page.size" :page-sizes="[20, 50, 100]"
                    :total="page.total" background layout="total, sizes, prev, pager, next, jumper" v-bind="page"
                    @size-change="handleSizeChange" @current-change="handleCurrentChange">
                </el-pagination>
            </template>
        </nodes-master-page>
        <div v-if="columnShowHide.visible">
            <dialog-column v-bind="columnShowHide" @close="onColumnShowHide">
            </dialog-column>
        </div>
    </div>
</template>

<script>


import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import NodesAsnBillState from "@/components/wms/select/NodesAsnBillState";
import NodesInStoreMode from "@/components/wms/select/NodesInStoreMode";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import NodesSku from "@/components/wms/select/NodesSku";
import { listMixin } from "@/mixins/list";
import { getPage, remove, exportFile } from "@/api/wms/instock/asnHeader";
import fileDownload from "js-file-download";

export default {
    name: "asnHeader",
    components: {
        NodesSku,
        NodesWarehouse,
        DialogColumn,
        NodesInStoreMode,
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
                    asnBillNo: '',
                    sku: {
                        skuId: '',
                        skuCode: '',
                        skuName: ''
                    },
                    asnBillState: [10, 20, 30],
                    createTimeDateRange: ['', ''],
                    supplier: '',
                    externalOrderNo: '',
                    externalCreateUser: '',
                    whCodeList: []
                }
            },
            table: {
                columnList: [
                    {
                        prop: 'asnBillNo',
                        label: 'ASN单编码',
                        width: 140,
                        sortable: 'custom'
                    },
                    {
                        prop: 'billTypeName',
                        label: '单据类型'
                    },
                    {
                        prop: 'supplierCode',
                        width: 100,
                        label: '供应商编码'
                    },
                    {
                        prop: 'supplierName',
                        width: 100,
                        label: '供应商名称',
                    },
                    {
                        prop: 'externalOrderNo',
                        label: '上游编码'
                    },
                    {
                        prop: 'externalCreateUser',
                        width: 100,
                        label: '上游创建人'
                    },
                    {
                        prop: 'whName',
                        label: '仓库编码'
                    },
                    {
                        prop: 'asnBillRemark',
                        label: '备注'
                    },
                    {
                        prop: 'createTime',
                        width: 130,
                        label: '创建时间'
                    },
                    {
                        prop: 'createUser',
                        label: '创建人'
                    },
                    {
                        prop: 'updateTime',
                        width: 130,
                        label: '更新时间'
                    },
                     {
                        prop: 'asnBillStateValue',
                        label: '状态'
                    },
                ]
            },
        }
    },
    created() {

    },
    methods: {
        getTableData() {
            getPage(this.page, this.form.params)
                .then((res) => {
                    let pageObj = res.data.data;
                    this.table.data = pageObj.records;
                    this.page.total = pageObj.total;
                })
                .catch((e) => {
                    console.log(e);
                });
        },
        view() {

        },
        getSummaries() {

        },
        onRemove() {
            let rows = this.$refs.table.selection;
            if (rows.length <= 0) {
                this.$message({
                    message: "警告，至少选择一条记录",
                    type: "warning",
                });
                return;
            }
            this.$confirm("此操作将删除, 是否删除?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
                .then(() => {
                    let removeObj = {
                        asnBillIds: []
                    };
                    rows.forEach((item) => {
                        removeObj.asnBillIds.push(item.asnBillId);
                    });
                    remove(removeObj)
                        .then((res) => {
                            this.$message({
                                type: "success",
                                message: res.data.msg,
                            });
                            this.getTableData();
                        })
                        .catch((e) => {
                            console.log(e);
                        });
                })
        },
        exportData() {
            this.loading = true;
            exportFile(this.form.params)
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, "ASN单.xlsx");
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        filterTag(value, row) {
            return row.status === value;
        },
        onReset() {
            console.log("重置了。。。");
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
    }
}
</script>
