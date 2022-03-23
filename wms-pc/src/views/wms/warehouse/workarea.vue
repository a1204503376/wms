<template>
    <basic-container>
        <nodes-crud
                ref="table"
                :option="option"
                :data="data"
                v-model="form"
                :table-loading="loading"
                :permission="permissionList"
                @on-load="onLoad"
                @on-del="onDel"
                @on-multi-del="onMultiDel"
                @on-search="dialog"
                @row-save="rowSave"
                :before-open="beforeOpen"
                @selection-change="selectionChange"
        ></nodes-crud>
    </basic-container>
</template>
<script>
    import {getPage, add, remove, getDetail} from "@/api/wms/warehouse/workarea";
    import {mapGetters} from "vuex";
    export default {
        name: "workarea",
        data() {
            return {
                loading: false,
                tableColumnVisible: false,
                selectionList: [], //选中的数据
                data: [],
                form: {},
                option: {
                    newBtn: true,
                    multiDelBtn: true,
                    viewBtn: true,
                    editBtn: true,
                    delBtn: true,
                    menu: true,
                    custom: false,
                    group: [
                        {
                            column: [
                                {
                                    prop: "whId",
                                    label: "所属库房",
                                    type: "select",
                                    dicUrl: "/api/wms/warehouse/warehouse/list",
                                    props: {
                                        label: "whName",
                                        value: "whId"
                                    },
                                    rules: [
                                        {
                                            required: true,
                                            message: "所属库房不能为空",
                                            trigger: "blur"
                                        }
                                    ]
                                },
                                {
                                    prop: "wwaName",
                                    label: "工作区",
                                    maxlength: 50,
                                    rules: [
                                        {
                                            required: true,
                                            message: "工作区不能为空",
                                            trigger: "blur",
                                            pattern: /\S/,
                                        }
                                    ]
                                },
                                {
                                    prop: "remark",
                                    label: "说明",
                                    maxlength: 1000
                                },
                                // {
                                //     prop: "status",
                                //     label: "是否启用",
                                //     type: "radio",
                                //     dicData: [
                                //         {
                                //             value: 0,
                                //             label: "是"
                                //         },
                                //         {
                                //             value: 1,
                                //             label: "否"
                                //         }
                                //     ],
                                //     default: 0
                                // }
                            ]
                        }
                    ],
                    column: [
                        {
                            prop: "wwaName",
                            label: "工作区",
                            search: true,
                            view: true,
                            sortable: true,
                        },
                        {
                            prop: "remark",
                            label: "说明"
                        },
                        // {
                        //     prop: "statusDesc",
                        //     label: "是否启用",
                        //     sortable: true,
                        // },
                        {
                            label: "所属库房",
                            prop: "whId",
                            search: true,
                            type: "select",
                            dicUrl: "/api/wms/warehouse/warehouse/list",
                            props: {
                                label: "whName",
                                value: "whId"
                            }
                        }
                    ]
                }
            };
        },
        computed: {
            ...mapGetters(["permission"]),
            permissionList() {
                return {
                    add: this.vaildData(this.permission.workarea_add, false),
                    view: this.vaildData(this.permission.workarea_view, false),
                    delete: this.vaildData(this.permission.workarea_delete, false),
                    edit: this.vaildData(this.permission.workarea_edit, false),
                }
            },
            ids() {
                let ids = [];
                this.selectionList.forEach(ele => {
                    ids.push(ele.wwaId);
                });
                return ids.join(",");
            }
        },
        methods: {
            //默认渲染数据
            onLoad(page, params = {}) {
                this.loading = true;
                getPage(
                    page.currentPage,
                    page.pageSize,
                    Object.assign(params, this.query)
                ).then(res => {
                    var data = res.data.data;
                    page.total = data.total;
                    this.data = data.records;
                    this.loading = false;
                    this.selectionClear();
                });
            },
            rowSave(row, loading, done, type) {
                add(row).then(
                    () => {
                        loading();
                        this.$refs.table.onLoad();
                        this.$message.success("操作成功！");
                    },
                    error => {
                        done();
                    }
                );
            },
            beforeOpen(done, type, finish) {
                if (["edit", "view"].includes(type)) {
                    getDetail(this.form.wwaId)
                        .then(res => {
                            this.form = res.data.data;
                        })
                        .finally(() => {
                            done();
                        });
                }
            },
            onDel(row, index) {
                this.$confirm("确定删除当前数据？", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    remove(row.wwaId).then(() => {
                        this.$refs.table.onLoad();
                        this.$message({
                            type: "success",
                            message: "操作成功!"
                        });
                    });
                });
            },
            onMultiDel() {
                if (!this.selectionList || this.selectionList.length == 0) {
                    this.$message.warning("至少选择一条数据！");
                    return;
                }
                this.$confirm("确定将选择数据删除?", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    remove(this.ids).then(() => {
                        this.$refs.table.onLoad();
                        this.$message({
                            type: "success",
                            message: "操作成功!"
                        });
                        this.$refs.table.toggleSelection();
                    });
                });
            },
            //选中的数据
            selectionChange(list) {
                this.selectionList = list;
            },
            selectionClear() {
                this.selectionList = [];
                this.$refs.table.toggleSelection();
            },
            //搜索
            onSubmit() {
            },
            dialog() {
                this.dialogs = true;
            }
        }
    };
</script>
<style lang="scss">
</style>
