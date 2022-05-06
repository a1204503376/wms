import fileDownload from "js-file-download";
<template>
    <div id='customer'>
        <nodes-master-page :configure="masterConfig" v-on="form.events" :permission="permissionObj">
            <template v-slot:searchFrom>
                <el-form-item label="客户编码">
                    <el-input v-model="form.params.code" class="d-input"></el-input>
                </el-form-item>
                <el-form-item label="客户名称">
                    <el-input v-model="form.params.name" class="d-input"></el-input>
                </el-form-item>
                <el-form-item label="客户简称">
                    <el-input v-model="form.params.simpleName" class="d-input"></el-input>
                </el-form-item>

            </template>
            <template v-slot:expandSearch>
                <el-row type="flex">
                    <el-col :span="24">
                        <el-form-item label="创建日期">
                            <nodes-date-range v-model="form.params.createTimeDateRange"></nodes-date-range>
                        </el-form-item>

                        <el-form-item label="更新日期">
                            <nodes-date-range v-model="form.params.updateTimeDateRange"></nodes-date-range>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-slot:batchBtn>
                <el-button v-if="permissionObj.add" icon="el-icon-plus" size="mini" type="primary">新增</el-button>
                <el-button v-if="permissionObj.delete" size="mini" type="danger" @click="onRemove" icon="el-icon-delete"
                           plain>删除
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
                    <el-button
                        circle
                        icon="el-icon-refresh"
                        size="mini"
                        @click="onRefresh"
                    ></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="显隐"
                    effect="dark"
                    placement="top"
                >
                    <el-button
                        circle
                        icon="el-icon-s-operation"
                        size="mini"
                        @click="onColumnShowHide"
                    ></el-button>
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
                    <el-button circle icon="el-icon-download" size="mini" @click="exportData"></el-button>
                </el-tooltip>
            </template>
            <template v-slot:table>
                <el-table
                    ref="table"
                    :data="table.data"
                    :summary-method="getSummaries"
                    border
                    highlight-current-row
                    show-summary
                    size="mini"
                    style="width: 100%"
                    @sort-change="onSortChange"
                    row-key="id"
                >
                    <el-table-column fixed type="selection" width="50"></el-table-column>
                    <el-table-column fixed sortable type="index">
                        <template slot="header"> #</template>
                    </el-table-column>
                    <template v-for="(column, index) in table.columnList">
                        <el-table-column
                            v-if="!column.hide"
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column"
                        >
                        </el-table-column>
                    </template>
                    <el-table-column prop="status" label="启用" width="100"
                                     :filters="[{ text: '是', value: 1 }, { text: '否', value: -1 }]" :filter-method="filterTag"
                                     filter-placement="bottom-end">
                        <template slot-scope="scope">
                            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" disable-transitions>{{ scope.row.status === 1 ? '是' : '否' }}
                            </el-tag>
                        </template>
                    </el-table-column>
                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination
                    :current-page="page.current"
                    :hide-on-single-page="hideOnSinglePage"
                    :page-size="page.size"
                    :page-sizes="[20, 50, 100]"
                    :total="page.total"
                    background
                    v-bind="page"
                    layout="total, sizes, prev, pager, next, jumper"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                >
                </el-pagination>
            </template>
        </nodes-master-page>
        <dialog-column v-bind="columnShowHide" @close="onColumnShowHide">
        </dialog-column>
    </div>
</template>

<script>

    import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
    import NodesAsnBillState from "@/components/wms/select/NodesAsnBillState";
    import NodesInStoreMode from "@/components/wms/select/NodesInStoreMode";
    import NodesDateRange from "@/components/wms/general/NodesDateRange";
    import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
    import DialogColumn from "@/components/element-ui/crud/dialog-column";
    import {listMixin} from "@/mixins/list";
    import {page, remove, exportFile} from "@/api/wms/basics/customer";
    import fileDownload from "js-file-download";


    export default {
        name: "customer",
        components: {
            DialogColumn,
            NodesSearchInput,
            NodesInStoreMode,
            NodesAsnBillState,
            NodesMasterPage,
            NodesDateRange,
        },
        mixins: [listMixin],
        data() {
            return {
                masterConfig: {
                    showExpandBtn: true,
                },
                form: {
                    params: {
                        code: "",
                        name: "",
                        simpleName: "",
                        createTimeDateRange: "",
                        updateTimeDateRange: "",
                    }
                },
                ids: {
                    ids: [],
                },
                page: {
                    total: 0,
                    size: 20,
                    current: 1,
                    ascs: "", //正序字段集合
                    descs: "", //倒序字段集合
                },

                table: {
                    columnList: [
                        {
                            prop: "code",
                            label: "客户编码",
                            width: 300,
                            sortable: "custom",
                        },
                        {
                            prop: "name",
                            label: "客户名称",
                        },
                        {
                            prop: "simpleName",
                            label: "客户简称",
                        },
                        {
                            prop: "woId",
                            label: "货主",
                        },
                        {
                            prop: "country",
                            label: "国家",
                        },
                        {
                            prop: "province",
                            label: "省",
                        },
                        {
                            prop: "city",
                            label: "城市",
                        },
                        {
                            prop: "address",
                            label: "街道",
                        },
                        {
                            prop: "zipCode",
                            label: "邮编",
                        },
                        {
                            prop: "status",
                            label: "启用",
                        },
                        {
                            prop: "remark",
                            label: "备注",
                        },
                        {
                            prop: "createTime",
                            label: "创建时间",
                        },
                        {
                            prop: "createUser",
                            label: "创建人",
                        },
                        {
                            prop: "updateTime",
                            label: "更新时间",
                        },
                    ],
                },
            };
        },
        computed: {
            permissionObj() {
                return {
                    search: this.vaildData(this.permission.customer_view, false),
                    add: this.vaildData(this.permission.customer_add, false),
                    delete: this.vaildData(this.permission.customer_delete, false),
                }
            }
        },

        created() {
        },
        methods: {
            getTableData() {
                // API调用:post(this.form.params)
                page(this.page,this.form.params)
                    .then((res) => {
                        let pageObj = res.data.data;
                        this.table.data = pageObj.records;
                        this.page.total = pageObj.total;
                    })
                    .catch(() => {
                    });
            },

            exportData() {
                this.loading = true;
                exportFile(this.form.params)
                    .then((res) => {
                        this.$message.success("操作成功，正在下载中...");
                        fileDownload(res.data, "客户列表.xlsx");
                    })
                    .catch(() => {
                        this.$message.error("系统模板目录配置有误或文件不存在");
                    })
                    .finally(() => {
                        this.loading = false;
                    });
            },
            onSubmit() {
                this.getTableData();
            },
            onReset() {
                this.form.params = {};
                console.log('重置表单');
            },


            onRemove() {

                this.$confirm("确定删除当前数据？", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning",
                }).then(() => {

                    this.$refs.table.selection.forEach(e => {
                        this.ids.ids.push(e.id)
                    })

                    remove(this.ids)
                        .then(() => {
                            this.$message.success('删除成功');
                            this.getTableData();
                        })
                        .catch(() => {
                        });


                })
            },

        },
    };
</script>
