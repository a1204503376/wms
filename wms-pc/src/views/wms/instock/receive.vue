<template>
    <div id="receive">
        <nodes-master-page v-on="form.events">
            <template v-slot:searchFrom>
                <el-row class="search-elRow" type="flex">
                    <el-col :span="6">
                        <el-form-item label="收货单编码" label-width="90px">
                            <el-input :clearable="true" class="search-input" placeholder="请输入收货单编码"
                                      v-model="form.params.receiveNo">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="单据状态" label-width="90px">
                            <nodes-receive-bill-state class="search-input" v-model="form.params.billStateList">
                            </nodes-receive-bill-state>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="物品编码" label-width="90px">
                            <nodes-sku-by-query class="search-input" v-model="form.params.skuIds">
                            </nodes-sku-by-query>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="上游编码" label-width="90px">
                            <el-input :clearable="true" class="search-input" placeholder="请输入上游编码"
                                      v-model="form.params.externalOrderNo">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row type="flex">
                    <el-col :span="6" v-if="false">
                        <el-form-item label="ASN单编码" label-width="90px">
                            <el-input :clearable="true" class="search-input" placeholder="请输入ASN单编码"
                                      v-model="form.params.asnBillNo">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="仓库编码" label-width="90px">
                            <nodes-warehouse :multiple="true" class="search-input" v-model="form.params.whIds">
                            </nodes-warehouse>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="上游创建人" label-width="90px">
                            <el-input :clearable="true" class="search-input" placeholder="请输入上游创建人"
                                      v-model="form.params.externalCreateUser">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="供应商编码" label-width="90px">
                            <el-input :clearable="true" class="search-input" placeholder="请输入供应商编码"
                                      v-model="form.params.supplierCode">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="创建日期" label-width="90px">
                            <nodes-date-range v-model="form.params.createTimeDateRange">
                            </nodes-date-range>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-slot:batchBtn>
                <el-button @click="onAdd" icon="el-icon-plus" size="mini" type="primary" v-if="permissionObj.add">新增
                </el-button>
                <el-button :plain="true" @click="onRemove" icon="el-icon-delete" size="mini" type="danger"
                           v-if="permissionObj.delete">删除
                </el-button>
            </template>
            <template v-slot:tableTool>
                <el-tooltip :enterable="false" class="item" content="刷新" effect="dark" placement="top">
                    <el-button @click="onRefresh" circle icon="el-icon-refresh" size="mini"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="显隐" effect="dark" placement="top">
                    <el-button @click="onColumnShowHide" circle icon="el-icon-s-operation" size="mini"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="全量导出" effect="dark" placement="top">
                    <el-button @click="exportData" circle icon="el-icon-download" size="mini"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="当前页导出" effect="dark" placement="top">
                    <excel-export :filename="exportExcelName" :sheet="exportExcelSheet"
                                  style="display: inline-block;margin-left: 10px">
                        <el-button @click="onExportLocalData" circle icon="el-icon-bottom" size="mini"/>
                    </excel-export>
                </el-tooltip>
            </template>
            <template v-slot:table>
                <el-table :data="table.data" :height="table.height" @sort-change="onSortChange" border
                          highlight-current-row
                          ref="table" row-key="receiveId" size="mini" style="width: 100%">
                    <el-table-column fixed type="selection" width="50">
                    </el-table-column>
                    <template v-for="(column,index) in table.columnList">
                        <el-table-column :key="index" min-width="150" show-overflow-tooltip v-bind="column"
                                         v-if="!column.hide">
                            <template v-if="column.prop === 'receiveNo'" v-slot="scope">
                                <el-link @click="onViewDetails(scope.row.receiveId)" target="_blank" type="primary">
                                    {{ scope.row.receiveNo }}
                                </el-link>
                            </template>
                        </el-table-column>
                    </template>
                    <el-table-column align="center" fixed="right" label="操作" v-if="showActionBar" width="150">
                        <template v-slot="scope">
                            <el-button @click="handleClick(scope.row)" size="small" type="text"
                                       v-if="permissionObj.edit">编辑
                            </el-button>
                            <el-button @click="onClose(scope.row)" size="small" type="text" v-if="permissionObj.close">
                                关闭
                            </el-button>
                            <el-button @click="onReceive(scope.row)" size="small" type="text"
                                       v-if="permissionObj.receive">PC收货
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination :current-page="page.current" :page-size="page.size" :page-sizes="[20, 50, 100]"
                               :total="page.total" @current-change="handleCurrentChange" @size-change="handleSizeChange"
                               background
                               layout="total, sizes, prev, pager, next, jumper" v-bind="page">
                </el-pagination>
            </template>
        </nodes-master-page>
        <div v-if="columnShowHide.visible">
            <dialog-column @close="onColumnShowHide" v-bind="columnShowHide">
            </dialog-column>
        </div>
    </div>
</template>

<script>


    import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
    import NodesAsnBillState from "@/components/wms/select/NodesAsnBillState";
    import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
    import NodesDateRange from "@/components/wms/general/NodesDateRange";
    import DialogColumn from "@/components/element-ui/crud/dialog-column";
    import {close, exportFile, page, remove} from "@/api/wms/instock/receive";
    import {listMixin} from "@/mixins/list";
    import fileDownload from "js-file-download";
    import NodesReceiveBillState from "../../../components/wms/select/NodesReceiveBillState";
    import NodesSkuByQuery from "@/components/wms/select/NodesSkuByQuery";
    import {ExcelExport} from 'pikaz-excel-js';
    import {nowDateFormat} from "@/util/date";
    import func from "@/util/func";


    export default {
        name: "list",
        components: {
            NodesSkuByQuery,
            NodesReceiveBillState,
            DialogColumn,
            NodesWarehouse,
            NodesAsnBillState,
            NodesMasterPage,
            NodesDateRange,
            ExcelExport
        },
        mixins: [listMixin],
        data() {
            return {
                form: {
                    params: {
                        code: [],
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
                billState: "",
                nums: {
                    receiveIdList: [],
                },

                table: {
                    columnList: [
                        {
                            prop: 'receiveNo',
                            label: '收货单编码',
                            sortable: 'custom'
                        },
                        {
                            prop: 'billState',
                            label: '单据状态',
                            sortable: 'custom',
                        },
                        {
                            prop: 'asnBillNo',
                            label: 'ASN单编码',
                            sortable: 'custom',
                            hide: true,
                        },
                        {
                            prop: 'externalOrderNo',
                            label: '上游编码',
                            sortable: 'custom',
                        },
                        {
                            prop: 'billTypeCd',
                            label: '单据类型',
                            sortable: 'custom',
                        },
                        {
                            prop: 'supplierCode',
                            label: '供应商编码',
                            sortable: 'custom',
                        },
                        {
                            prop: 'supplierName',
                            sortable: 'custom',
                            label: '供应商名称'
                        },
                        {
                            prop: 'whCode',
                            sortable: 'custom',
                            label: '仓库编码'
                        },
                        {
                            prop: 'createTime',
                            sortable: 'custom',
                            label: '创建日期'
                        },
                        {
                            prop: 'createUser',
                            sortable: 'custom',
                            label: '创建人'
                        },
                        {
                            prop: 'externalPreCreateDate',
                            sortable: 'custom',
                            label: '上游创建时间'
                        },
                        {
                            prop: 'externalCreateUser',
                            sortable: 'custom',
                            label: '上游创建人'
                        },
                        {
                            prop: 'updateUser',
                            sortable: 'custom',
                            label: '更新人'
                        },
                        {
                            prop: 'updateTime',
                            sortable: 'custom',
                            label: '更新时间'
                        },
                        {
                            prop: 'remark',
                            sortable: 'custom',
                            label: '消息'
                        },
                    ]
                },
            }
        },
        computed: {
            permissionObj() {
                return {
                    add: this.vaildData(this.permission.receive_add, false),
                    delete: this.vaildData(this.permission.receive_delete, false),
                    edit: this.vaildData(this.permission.receive_edit, false),
                    close: this.vaildData(this.permission.receive_close, false),
                    receive: this.vaildData(this.permission.receive_receive, false),
                }
            },
            showActionBar() {
                return this.vaildData(this.permission.receive_edit, false)
                    || this.vaildData(this.permission.receive_close, false)
                    || this.vaildData(this.permission.receive_receive, false)
            }
        },
        watch: {
            $route(to) {
                if (to.query && to.query.isRefresh === 'true') {
                    this.getTableData();
                }
            }
        },
        created() {
            this.getTableData();
        },
        methods: {
            onExportLocalData() {
                this.exportCurrentDataToExcel("收货单", "收货单");
            },
            onViewDetails(id) {
                this.$router.push({
                    name: '收货单详情',
                    params: {
                        receiveId: id.toString()
                    }
                });
            },
            onReceive(row) {
                let state = row.billState;
                if (state === "关闭") {
                    this.$message.error('该收货单已关闭，不能进行收货');
                    return
                }
                if (state === "全部收货") {
                    this.$message.error('该收货单已全部收货，不能进行收货');
                    return
                }
                if (state === "已取消") {
                    this.$message.error('该收货单已取消，不能进行收货');
                    return
                }
                if (row.billTypeCd === "销售退回" && func.isEmpty(row.udf1)) {
                    this.$message.error('该销售退还单的文件编码为空，不能进行收货');
                    return
                }
                this.$router.push({
                    name: 'PC收货',
                    params: {
                        receiveId: row.receiveId,
                        billTypeCd: row.billTypeCd
                    }
                });
            },
            getTableData() {
                page(this.page, this.form.params).then((res) => {
                    let pageObj = res.data.data;
                    this.table.data = pageObj.records;
                    this.page.total = pageObj.total;
                    this.handleRefreshTable();
                })
            },
            onAdd() {
                this.$router.push({
                    name: '新增收货单',
                    params: {
                        id: '0'
                    }
                });
            },
            handleClick(row) {
                let state = row.billState;
                if (state !== "未收货") {
                    this.$message.error('当前收货单不允许编辑');
                    return
                }
                this.$router.push({
                    name: '编辑收货单',
                    params: {
                        id: row.receiveId
                    }
                });
            },
            onRemove: function () {
                this.$confirm("确定删除当前数据？", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                }).then(() => {
                    this.nums.receiveIdList = this.$refs.table.selection.map(e => e.receiveId)
                    remove(this.nums).then(() => {
                        this.$message.success('删除成功');
                        this.getTableData();
                    })
                })
            },
            onClose(row) {
                this.$confirm("确定关闭当前收货单？", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                }).then(() => {
                    let state = row.billState;
                    if (state === "关闭") {
                        this.$message.error('该收货单已关闭，请勿重复点击');
                        throw new Error('该收货单已关闭，请勿重复点击');
                    }
                    this.nums.receiveId = row.receiveId;
                    close(this.nums)
                        .then(() => {
                            this.$message.success('关闭成功');
                            this.getTableData();
                        })
                })
            },
            exportData() {
                this.loading = true;
                exportFile(this.form.params)
                    .then((res) => {
                        this.$message.success("操作成功，正在下载中...");
                        fileDownload(res.data, `收货管理列表${nowDateFormat("yyyyMMddhhmmss")}.xlsx`);
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
