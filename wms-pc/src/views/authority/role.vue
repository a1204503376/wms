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
        >
            <template slot="menuLeft">
                <el-button
                    plain
                    size="mini"
                    class="Printing"
                    v-if="permission.role_permission"
                    @click="jurisdiction"
                >权限设置
                </el-button>
            </template>
        </nodes-crud>
        <roleJurisDialog
            :visible="dialogJuris.visible"
            :dataSource="dialogJuris.dataSource"
            @callback="callbackJuris"
        ></roleJurisDialog>
    </basic-container>
</template>
<script>
import roleJurisDialog from "./role/roleJurisDialog";
import {
    getList,
    added,
    remove,
    grantTree,
    getTopTree,
    getTopselect,
    grant,
    getRole,
    getDetail,
} from "@/api/authority/role";
import {mapGetters} from "vuex";

export default {
    name: "role",
    components: {
        roleJurisDialog,
    },
    data() {
        return {
            operation: false,
            operationButton: [],
            maps: new Map(),
            labelPosition: "right",
            loading: false,
            selectionList: [], //选中的数据
            query: {},
            data: [],
            form: {},
            option: {
                newBtn: true,
                multiDelBtn: true,
                viewBtn: true,
                editBtn: true,
                delBtn: true,
                menu: true,
                rowKey: "id",
                page: false,
                custom: false,
                menuItem: [
                    {
                        label: "添加子级",
                        command: 1,
                        divided: true,
                    },
                ],
                group: [
                    {
                        column: [
                            {
                                prop: "roleName",
                                label: "角色名称",
                                maxlength: 255,
                                rules: [
                                    {
                                        required: true,
                                        message: "角色名称不能为空",
                                        trigger: "blur",
                                        pattern: /\S/,
                                    },
                                ],
                            },
                            {
                                prop: "roleAlias",
                                label: "角色别名",
                                maxlength: 255,
                                rules: [
                                    {
                                        required: true,
                                        message: "角色别名不能为空",
                                        trigger: "blur",
                                    },
                                ],
                            },
                            // {
                            //   prop: "parentId",
                            //   label: "上级角色",
                            //   type: "select-tree",
                            //   dicUrl: "/api/blade-system/role/tree",
                            //   props: {
                            //     label: "title",
                            //     value: "id",
                            //     children: "children",
                            //   },
                            //   clearable: true,
                            // },
                            {
                                prop: "sort",
                                label: "角色排序",
                                type: "inputNumber",
                                rules: [
                                    {
                                        pattern: /^[0-9]*$/,
                                    },
                                ],
                            },
                            {
                                prop: "taskType",
                                label: "任务类型",
                                type: "select",
                                props: {
                                    label: "dictValue",
                                    value: "dictKey",
                                },
                                dicUrl:
                                    "/api/blade-system/dict/dictionary?code=" +
                                    this.$dict.taskType,
                                multiple: true,
                            },
                        ],
                    },
                ],
                column: [
                    {
                        prop: "roleName",
                        label: "角色名称",
                        search: true,
                        view: true,
                    },
                    {
                        prop: "roleAlias",
                        label: "角色别名",
                        search: true,
                    },
                    // {
                    //     prop: "parentId",
                    //     label: "上级角色",
                    //     search: true,
                    //     type: "select-tree",
                    //     dicUrl: "/api/blade-system/role/tree",
                    //     props: {
                    //         label: "title",
                    //         value: "id",
                    //         children: "children",
                    //     },
                    //     clearable: true,
                    // },
                    // {
                    //     prop: "tenantName",
                    //     label: "所属租户",
                    //     search: true
                    // },
                    {
                        prop: "sort",
                        label: "角色排序",
                        search: true,
                    },
                ],
            },
            dialogJuris: {
                visible: false,
                dataSource: "",
            },
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                add: this.vaildData(this.permission.role_add, false),
                view: this.vaildData(this.permission.role_view, false),
                delete: this.vaildData(this.permission.role_delete, false),
                edit: this.vaildData(this.permission.role_edit, false),
                权限设置: this.vaildData(this.permission.role_permission, false),
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
        //列表
        onLoad(page, params = {}) {
            this.loading = true;
            getList(params).then((res) => {
                const data = res.data.data;
                this.data = data;
                this.loading = false;
                this.selectionClear();
            });
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
                    const {parentId} = row;
                    if (parentId != "0" && this.maps.has(parentId)) {
                        const {tree, treeNode, resolve} = this.maps.get(parentId);
                        this.$set(
                            this.$refs.table.$refs.table.store.states.lazyTreeNodeMap,
                            parentId,
                            []
                        );
                        this.loadList(tree, treeNode, resolve);
                    }
                });
            });
        },
        rowSave(row, loading, done, type) {
            added(row).then(
                () => {
                    if (row._parentId && !row.hasChildren) {
                        //无子级
                        const {_parentId} = row;
                        if (this.maps.has(_parentId)) {
                            const {tree, treeNode, resolve} = this.maps.get(_parentId);
                            this.$set(
                                this.$refs.table.$refs.table.store.states.lazyTreeNodeMap,
                                _parentId,
                                []
                            );
                            this.loadList(tree, treeNode, resolve);
                        }
                    } else {
                        //有子级
                        const {parentId} = row;
                        if (parentId != "0" && this.maps.has(parentId)) {
                            const {tree, treeNode, resolve} = this.maps.get(parentId);
                            this.$set(
                                this.$refs.table.$refs.table.store.states.lazyTreeNodeMap,
                                parentId,
                                []
                            );
                            this.loadList(tree, treeNode, resolve);
                        }
                    }
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
                getDetail(this.form.id)
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
        //高级搜索
        select() {
            getTopselect().then((res) => {
                this.query = res.data;
            });
        },
        //权限设置
        jurisdiction() {
            if (this.selectionList.length === 0) {
                this.$message.warning("请选择至少一条数据");
                return;
            }
            this.dialogJuris.dataSource = this.ids;
            this.dialogJuris.visible = true;
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
        callbackJuris(res) {
            this.dialogJuris.visible = res.visible;
            if (res.result) {
                this.$refs.table.onLoad();
            }
        },
    },
};
</script>
<style lang="scss" scoped>
</style>
