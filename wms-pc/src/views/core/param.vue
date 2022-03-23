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
            @row-save="rowSave"
            :before-open="beforeOpen"
            @on-multi-del="onMultiDel"
            @selection-change="selectionChange"
            @menu-command="menuCommand"
        ></nodes-crud>
    </basic-container>
</template>
<script>
import {
    getPage,
    add,
    remove,
    detail,
    changeVisible,
} from "@/api/core/param.js";
import {mapGetters} from "vuex";

export default {
    name: "paramName",
    data() {
        return {
            titleText: "",
            dialogVisible: false,
            isView: false,
            isShow: false,
            selectionList: [], //选中的数据
            loading: false,
            data: [],
            form: {},
            option: {
                // columnFilter:true,   //行搜索
                newBtn: true,
                multiDelBtn: true,
                viewBtn: true,
                editBtn: true,
                delBtn: true,
                menu: true,
                rowKey: "id",
                page: true,
                custom: false,
                menuItem: [],
                group: [
                    {
                        column: [
                            {
                                label: "参数名称",
                                prop: "paramName",
                                search: true,
                                maxlength: 255,
                                rules: [
                                    {
                                        required: true,
                                        message: "参数名称不能为空",
                                        trigger: "blur",
                                        pattern: /\S/,
                                    },
                                ],
                            },
                            {
                                label: "参数键名",
                                prop: "paramKey",
                                search: true,
                                maxlength: 255,
                                rules: [
                                    {
                                        required: true,
                                        message: "参数键名不能为空",
                                        trigger: "blur",
                                        pattern: /\S/,
                                    },
                                ],
                            },
                            {
                                label: "参数键值",
                                prop: "paramValue",
                                type: "textarea",
                                maxlength: 2000,
                                rules: [
                                    {
                                        required: true,
                                        message: "参数键值不能为空",
                                        trigger: "blur",
                                        pattern: /\S/,
                                    },
                                ],
                            },
                            {
                                prop: "tenantId",
                                label: "租户",
                                type: "select",
                                dicUrl: "/api/blade-system/tenant/select",
                                props: {
                                    label: "tenantName",
                                    value: "tenantId",
                                },
                            },
                            {
                                label: "备注",
                                prop: "remark",
                                type: "textarea",
                                maxlength: 255,
                            },
                        ],
                    },
                ],
                column: [
                    {
                        label: "参数名称",
                        prop: "paramName",
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "参数键名",
                        prop: "paramKey",
                        search: true,
                    },
                    {
                        label: "参数键值",
                        prop: "paramValue",
                    },
                    {
                        label: "租户",
                        prop: "tenantId",
                        type: "select",
                        dicUrl: "/api/blade-system/tenant/select",
                        props: {
                            label: "tenantName",
                            value: "tenantId",
                        },
                    },
                    {
                        label: "备注",
                        prop: "remark",
                    },
                ],
            },
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                add: this.vaildData(this.permission.param_add, false),
                view: this.vaildData(this.permission.param_view, false),
                delete: this.vaildData(this.permission.param_delete, false),
                edit: this.vaildData(this.permission.param_edit, false),
                显隐参数: this.vaildData(this.permission.param_showHidden, false),
            }
        },
        ids() {
            let ids = [];
            this.selectionList.forEach((ele) => {
                ids.push(ele.id);
            });
            return ids.join(",");
        },
    },
    mounted() {
        if (this.$store.getters.userInfo && this.$store.getters.userInfo.role_name === "developer") {
            this.option.menuItem.push({
                label: "显隐参数",
                command: 2,
                divided: true,
            });
            this.option.column.push({
                label: "是否显示",
                prop: "isVisible",
                type:'select',
                dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.isVisible,
                props: {
                    label: 'dictValue',
                    value: 'dictKey'
                }
            });
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
            ).then((res) => {
                const data = res.data.data.records;
                page.total = res.data.data.total;
                this.data = data;
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
                (error) => {
                    done();
                }
            );
        },
        beforeOpen(done, type, finish) {
            if (["edit", "view"].includes(type)) {
                detail(this.form.id)
                    .then((res) => {
                        this.form = res.data.data;
                    })
                    .catch(() => {
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
                type: "warning",
            }).then(() => {
                remove(row.id).then(() => {
                    this.$refs.table.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!",
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
                type: "warning",
            }).then(() => {
                remove(this.ids).then(() => {
                    this.$refs.table.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!",
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
        //成功回调
        childSuccess() {
            this.$refs.table.onLoad();
            this.dialogVisible = false;
        },
        //取消回调
        childCancel() {
            this.dialogVisible = false;
        },
        changeIsShowDialog(val) {
            this.dialogVisible = val; //监听变化时触发的函数修改父组件的是否显示状态
        },
        menuCommand(cmd, row, index) {
            switch (cmd) {
                case 2:
                    let msg = "隐藏";
                    if (row.isVisible == 1) msg = "显示";
                    this.$confirm("确定" + msg + "当前参数吗？", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning",
                    })
                        .then(() => {
                            return changeVisible(row.id, 1 - row.isVisible);
                        })
                        .then(() => {
                            this.$refs.table.onLoad();
                            this.$message({
                                type: "success",
                                message: "操作成功!",
                            });
                        });
                    break;
            }
        },
    },
};
</script>
