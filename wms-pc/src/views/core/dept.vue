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
            @load-List="loadList"
        ></nodes-crud>
    </basic-container>
</template>
<script>
import {getList, getPage, add, getDetail, remove} from "@/api/core/dept";
import {clearCache} from "@/api/user";
import {mapGetters} from "vuex";

export default {
    name: "dept",
    data() {
        return {
            maps: new Map(),
            titleText: "",
            loading: false,
            isView: false,
            isShow: false,
            selectionList: [], //选中的数据
            form: {},
            data: [],
            option: {
                newBtn: true,
                multiDelBtn: true,
                viewBtn: true,
                editBtn: true,
                delBtn: true,
                rowKey: "id",
                menu: true,
                page: false,
                custom: false,
                menuItem: [
                    {
                        label: "添加子级",
                        command: 1,
                        divided: true,
                    },
                ],
                lazy: true,
                load: function (row, treeNode, resolve) {
                    getList({parentId: row.id}).then((res) => {
                        resolve(res.data.data);
                    });
                },
                column: [
                    {
                        label: "机构名称",
                        prop: "deptName",
                        search: true,
                        width: 200,
                    },
                    {
                        label: "机构编码",
                        prop: "deptCode",
                        search: true,
                        view: true,
                    },
                    {
                        label: "所属租户",
                        prop: 'tenantId',
                        search: true,
                        type: 'select',
                        dicUrl: '/api/blade-system/tenant/select',
                        props: {
                            label: 'tenantName',
                            value: 'tenantId'
                        }
                    },
                    {
                        label: "机构全称",
                        prop: "fullName",
                        search: true,
                    },
                    {
                        label: "机构类型",
                        prop: "deptCategory",
                        width: 150,
                        sortable: true,
                        type: "select",
                        dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.orgCategory,
                        props: {
                            label: "dictValue",
                            value: "dictKey"
                        },
                        clearable: true
                    },
                    {
                        label: "排序",
                        prop: "sort",
                    },
                ],
                group: [
                    {
                        column: [
                            {
                                label: "机构编码",
                                prop: "deptCode",
                                maxlength: 45,
                                rules: [
                                    {
                                        required: true,
                                        message: "机构编码不能为空",
                                        trigger: "blur",
                                        // pattern: /^[0-9]*[1-9][0-9]*$/,
                                    },
                                    {min: 1, max: 45, message: "", trigger: "blur"},
                                ],
                            },
                            {
                                label: "机构名称",
                                prop: "deptName",
                                maxlength: 45,
                                rules: [
                                    {
                                        required: true,
                                        message: "机构名称不能为空",
                                        trigger: "blur",
                                        pattern: /\S/,
                                    },
                                ],
                            },
                            {
                                label: "机构全称",
                                prop: "fullName",
                                maxlength: 45,
                                rules: [
                                    {
                                        required: true,
                                        message: "机构全称不能为空",
                                        trigger: "blur",
                                        pattern: /\S/,
                                    },
                                ],
                            },
                            {
                                ref: "select",
                                prop: "parentId",
                                label: "上级机构",
                                type: "select-tree",
                                dicUrl: "/api/blade-system/dept/tree",
                                props: {
                                    label: "title",
                                    value: "id",
                                    children: "children",
                                },
                                clearable: true,
                            },
                            {
                                label: "机构类型",
                                prop: "deptCategory",
                                type: "select",
                                dicUrl:
                                    "/api/blade-system/dict/dictionary?code=" +
                                    this.$dict.orgCategory,
                                props: {
                                    label: "dictValue",
                                    value: "dictKey",
                                },
                            },
                            {
                                label: "排序",
                                prop: "sort",
                                type: "inputNumber",
                                min: 0,
                            },
                            {
                                label: "备注",
                                prop: "remark",
                                maxlength: 255,
                            },
                        ],
                    },
                ],
            },
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                add: this.vaildData(this.permission.dept_add, false),
                view: this.vaildData(this.permission.dept_view, false),
                delete: this.vaildData(this.permission.dept_delete, false),
                edit: this.vaildData(this.permission.dept_edit, false),
                添加子集: this.vaildData(this.permission.role_addChild, false),
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
    methods: {
        loadList(tree, treeNode, resolve) {
            const pid = tree.id;
            this.maps.set(pid, {tree, treeNode, resolve});
            getList({parentId: pid}).then((res) => {
                resolve(res.data.data);
                this.$refs.table.doLayout();
            });
        },
        //默认渲染数据
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(
                page.currentPage,
                page.pageSize,
                Object.assign(params, this.query)
            ).then(res => {
                let data = res.data.data;
                page.total = data.total;
                this.data = data.records;
                this.loading = false;
                this.selectionClear();
            });
        },
        beforeOpen(done, type, finish, data) {
            if (["edit", "view"].includes(type)) {
                let temp = this.option.group[0].column;
                temp.forEach((val) => {
                    if (val.prop == "parentId") {
                        val.dicUrl = "/api/blade-system/dept/tree?id=" + this.form.id;
                    }
                });
                getDetail(this.form.id)
                    .then((res) => {
                        this.form = res.data.data;
                    })
                    .finally(() => {
                        done();
                    });
            }
        },
        rowSave(row, loading, done, type) {
            add(row).then(
                () => {
                    if (this.maps.has(row.parentId)) {
                        const {tree, treeNode, resolve} = this.maps.get(row.parentId);
                        this.$set(this.$refs.table.$refs.table.store.states.lazyTreeNodeMap, row.parentId, []);
                        this.loadList(tree, treeNode, resolve)
                    };
                    loading();
                    this.$refs.table.onLoad();
                    this.$message.success("操作成功！");
                    clearCache();
                },
                (error) => {
                    done();
                }
            );
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
            this.$refs.deptDialogName.$refs.deptForm.resetFields(); //清空子组件表单里内容
            this.dialogVisible = false;
        },
        changeIsShowDialog(val) {
            this.dialogVisible = val; //监听变化时触发的函数修改父组件的是否显示状态
        },
        addChild(row) {
            this.$refs.table.rowAdd({
                parentId: row.id,
            });
        },
        menuCommand(cmd, row, index) {
            switch (cmd) {
                case 1:
                    this.addChild(row);
                    break;
            }
        },
    },
};
</script>
