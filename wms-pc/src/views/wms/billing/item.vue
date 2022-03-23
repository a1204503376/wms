<template>
    <basic-container>
        <nodes-crud ref="curd"
                    v-model="form"
                    :option="option"
                    :data="data"
                    :table-loading="loading"
                    :before-open="beforeOpen"
                    :permission="permissionList"
                    @on-load="onLoad"
                    @row-save="rowSave"
                    @on-del="rowDel"
                    @on-multi-del="onMultiDel"
                    @selection-change="selectionChange">
            <template slot="menuLeft">

            </template>
            <template slot="menuRight">

            </template>
            <template slot="menu">

            </template>
        </nodes-crud>
    </basic-container>
</template>

<script>
    import {getPage, getDetail, submit, remove} from "@/api/wms/billing/item";
    import {mapGetters} from "vuex";

    export default {
        data() {
            return {
                form: {},
                query: {},
                loading: true,
                data: [],
                selectionList: [],
                option: {
                    newBtn: true,
                    multiDelBtn: true,
                    editBtn: true,
                    delBtn: true,
                    menu: true,
                    custom: false,
                    column: [
                        {
                            label: "编码",
                            prop: "code",
                            sortable: true,
                            search: true,
                            rules: [{required: true, message: "项目编码不能为空", trigger: "blur"}],
                        },
                        {
                            label: "项目",
                            prop: "name",
                            sortable: true,
                            search: true,
                            rules: [{required: true, message: "项目名称不能为空", trigger: "blur"}],
                        },
                        {
                            label: "备注",
                            prop: "remark",
                            type:'textarea',
                        },
                    ],
                    group: []
                },
            };
        },
        computed: {
            ...mapGetters(["permission"]),
            permissionList() {
                return {
                    add: this.vaildData(this.permission.billing_item_add, false),
                    view: this.vaildData(this.permission.billing_item_view, false),
                    delete: this.vaildData(this.permission.billing_item_delete, false),
                    edit: this.vaildData(this.permission.billing_item_edit, false),
                };
            },
            ids() {
                let ids = [];
                this.selectionList.forEach(ele => {
                    ids.push(ele.id);
                });
                return ids.join(",");
            }
        },
        mounted() {
        },
        methods: {
            rowSave(row, loading, done, type) {
                submit(row).then(() => {
                    loading();
                    this.$refs.curd.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!"
                    });
                }, error => {
                    done();
                    console.log(error);
                });
            },
            rowDel(row, index) {
                this.$confirm("确定将选择数据删除?", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    remove(row.id).then(() => {
                        this.$refs.curd.onLoad();
                        this.$message({
                            type: "success",
                            message: "操作成功!"
                        });
                    });
                });
            },
            onMultiDel() {
                if (!this.selectionList || this.selectionList.length === 0) {
                    this.$message.warning("请选择至少一条数据");
                    return;
                }
                this.$confirm("确定将选择数据删除?", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    remove(this.ids).then(() => {
                        this.$refs.curd.onLoad();
                        this.$message({
                            type: "success",
                            message: "操作成功!"
                        });
                        this.$refs.curd.toggleSelection();
                    });
                });
            },
            beforeOpen(done, type, finish) {
                if (["edit", "view"].includes(type)) {
                    getDetail(this.form.id).then(res => {
                        this.form = res.data.data;
                    }).finally(() => {
                        done();
                    });
                }
            },
            selectionChange(list) {
                this.selectionList = list;
            },
            selectionClear() {
                this.selectionList = [];
                this.$refs.curd.toggleSelection();
            },
            onLoad(page, params = {}) {
                this.loading = true;
                getPage(page.currentPage, page.pageSize, params).then(res => {
                    const data = res.data.data;
                    page.total = data.total;
                    this.data = data.records;
                    this.loading = false;
                    this.selectionClear();
                });
            }
        }
    };
</script>

<style>
</style>
