<template>
    <basic-container>
        <nodes-crud
                ref="table"
                v-model="form"
                :option="option"
                :data="data"
                :table-loading="loading"
                :before-open="beforeOpen"
                :permission="permissionList"
                @on-load="onLoad"
                @row-save="rowSave"
                @on-del="onDel"
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
        getUser,
        remove,
        resetPassword
    } from "@/api/core/user";
    import {mapGetters} from "vuex";

    export default {
        name: "user",
        data() {
            return {
                loading: false,
                form: {},
                data: [],
                selectionList: [],
                option: {
                    newBtn: true,
                    multiDelBtn: true,
                    viewBtn: true,
                    editBtn: true,
                    delBtn: true,
                    menu: true,
                    custom: false,
                    menuItem: [
                        {
                            label: "重置密码",
                            command: 1,
                            divided: true
                        }
                    ],
                    column: [
                        {
                            prop: "account",
                            label: "登录账号",
                            search: true,
                            view: true,
                            sortable: true,
                            width: 120
                        },
                        {
                            prop: "name",
                            label: "用户昵称",
                            search: true,
                            sortable: true,
                            width: 120
                        },
                        {
                            prop: "realName",
                            label: "用户姓名",
                            search: true,
                            sortable: true,
                            width: 120
                        },
                        {
                            prop: "roleId",
                            label: "所属角色",
                            sortable: true,
                            width: 120,
                            type: "select",
                            dicUrl:
                                "/api/blade-user/list",
                            props: {
                                label: "roleName",
                                value: "roleId",
                            },
                        },
                        {
                            prop: "deptId",
                            label: "所属机构",
                            sortable: true,
                            width: 120,
                            type: "select-tree",
                            dicUrl: "/api/blade-system/dept/tree",
                            props: {
                                label: "title",
                                value: "id",
                                children: "children"
                            },
                            clearable: true
                        },
                        {
                            prop: "phone",
                            label: "手机号码"
                        }
                    ],
                    group: [
                        {
                            column: [
                                {
                                    prop: "account",
                                    label: "登录账号",
                                    maxlength: 45,
                                    rules: [
                                        {required: true, message: "账号不能为空", trigger: "blur", pattern: /\S/,}
                                    ]
                                },
                                {
                                    prop: "name",
                                    label: "用户昵称",
                                    maxlength: 20,
                                    rules: [
                                        {required: true, message: "昵称不能为空", trigger: "change", pattern: /\S/,}
                                    ]
                                },
                                {
                                    prop: "realName",
                                    label: "用户姓名",
                                    maxlength: 10,
                                    rules: [
                                        {required: true, message: "姓名不能为空", trigger: "change", pattern: /\S/,}
                                    ]
                                },
                                {
                                    prop: "roleId",
                                    label: "所属角色",
                                    type: "select",
                                    dicUrl: "/api/blade-system/role/list",
                                    props: {
                                        label: "roleName",
                                        value: "id",
                                    },
                                },
                                {
                                    prop: "deptId",
                                    label: "所属机构",
                                    type: "select-tree",
                                    dicUrl: "/api/blade-system/dept/tree",
                                    props: {
                                        label: "title",
                                        value: "id",
                                        children: "children"
                                    },
                                    rules: [
                                        {required: true, message: "请选择机构", trigger: "change"}
                                    ]
                                },
                                {
                                    prop: "phone",
                                    label: "手机号码",
                                    maxlength: 45,
                                    rules: [
                                        {
                                            required: false,
                                            pattern: /^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0-2,5-9])|(17[0-2,5-9]))\d{8}$/g,
                                            trigger: "blur",
                                            message: "请输入正确手机号"
                                        }
                                    ]
                                },
                                {
                                    prop: "email",
                                    label: "电子邮箱",
                                    maxlength: 45,
                                    rules: [
                                        {
                                            required: false,
                                            pattern: /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/,
                                            trigger: "blur",
                                            message: "请输入电子邮箱"
                                        }
                                    ]
                                },
                                {
                                    prop: "sex",
                                    label: "用户性别",
                                    type: "select",
                                    dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.sex,
                                    props: {
                                        label: "dictValue",
                                        value: "dictKey"
                                    }
                                },
                                {
                                    prop: "birthday",
                                    label: "用户生日",
                                    type: "date"
                                }
                            ]
                        }
                    ]
                },
                dialogEdit: {
                    visible: false,
                    isView: false,
                    addOrUpdata: false,
                    dataSource: null
                }
            };
        },
        computed: {
            ...mapGetters(["permission"]),
            permissionList() {
                return {
                    add: this.vaildData(this.permission.user_add, false),
                    view: this.vaildData(this.permission.user_view, false),
                    delete: this.vaildData(this.permission.user_delete, false),
                    edit: this.vaildData(this.permission.user_edit, false),
                    重置密码: this.vaildData(this.permission.user_resetPassword, false),
                }
            },
            ids() {
                let ids = [];
                this.selectionList.forEach(ele => {
                    ids.push(ele.id);
                });
                return ids.join(",");
            }
        },
        methods: {
            onLoad(page, params = {}) {
                this.loading = true;
                getPage(
                    page.currentPage,
                    page.pageSize,
                    Object.assign(params, this.query)
                ).then(res => {
                    const data = res.data.data;
                    page.total = data.total;
                    this.data = data.records;
                    this.loading = false;
                    this.selectionClear();
                });
            },
            beforeOpen(done, type, finish) {
                let result = this.option.group[0].column.filter(item => {
                    return item.prop == "password" || item.prop == "password2";
                });
                if (["edit", "view"].includes(type)) {
                    if (result && result.length > 0) {
                        result.forEach(element => {
                            element.hide = true;
                        });
                    }
                    getUser(this.form.id)
                        .then(res => {
                            this.form = res.data.data;
                        })
                        .finally(() => {
                            done();
                        });
                } else {
                    if (result && result.length > 0) {
                        result.forEach(element => {
                            element.hide = false;
                        });
                    }
                }
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
            resetPwd(row, index) {
                this.$confirm("确定重置该用户密码？", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                })
                    .then(() => {
                        return resetPassword(row.id);
                    })
                    .then(() => {
                        this.$refs.table.onLoad();
                        this.$message({
                            type: "success",
                            message: "操作成功!"
                        });
                    });
            },
            onDel(row, index) {
                this.$confirm("确定删除当前数据？", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    remove(row.id).then(() => {
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
            selectionChange(val) {
                this.selectionList = val;
            },
            selectionClear() {
                this.selectionList = [];
                this.$refs.table.toggleSelection();
            },
            dialogResult(result) {
                if (result.result) {
                    this.$refs.table.onLoad();
                }
                this.dialogEdit.visible = result.visible;
            },
            menuCommand(cmd, row, index) {
                switch (cmd) {
                    case 1:
                        this.resetPwd(row, index);
                        break;
                }
            }
        }
    };
</script>

<style scoped>
.user{
    height: 100%;
}
</style>
