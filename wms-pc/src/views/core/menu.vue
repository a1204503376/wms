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
            @load-List="loadList"
            :before-open="beforeOpen"
            @on-multi-del="onMultiDel"
            @selection-change="selectionChange"
            @menu-command="menuCommand"
        ></nodes-crud>
    </basic-container>
</template>
<script>
import {
    getList,
    getPage,
    add,
    remove,
    getDetail,
    changeVisible
} from "@/api/core/menu";
import {mapGetters} from "vuex";

export default {
    name: "menuName",
    data() {
        return {
            maps: new Map(),
            titleText: "",
            loading: false,
            isView: false,
            isShow: false,
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
                rowKey: "id",
                page: true,
                custom: false,
                lazy: true,
                menuItem: [
                    {
                        label: "添加子级",
                        command: 1,
                        divided: true
                    }
                ],
                group: [
                    {
                        column: [
                            {
                                label: "菜单名称",
                                prop: "name",
                                maxlength: 255,
                                rules: [
                                    {
                                        required: true,
                                        message: "菜单名称不能为空",
                                        trigger: "blur",
                                        pattern: /\S/
                                    }
                                ]
                            },
                            {
                                label: "路由地址",
                                prop: "path",
                                maxlength: 500,
                            },
                            {
                                label: "上级菜单",
                                prop: "parentId",
                                type: "select-tree",
                                dicUrl: "/api/blade-system/menu/tree",
                                props: {
                                    label: "title",
                                    value: "id",
                                    children: "children"
                                },
                                clearable: true,
                                show:['parentName']
                            },
                            {
                                label: "菜单图标",
                                prop: "source",
                                type: "icon",
                            },
                            {
                                label: "菜单编号",
                                prop: "code",
                                maxlength: 255,
                                rules: [
                                    {
                                        required: true,
                                        message: "菜单编号不能为空",
                                        trigger: "blur",
                                        pattern: /\S/
                                    }
                                ]
                            },
                            {
                                label: "菜单别名",
                                prop: "alias",
                                maxlength: 255,
                                rules: [
                                    {
                                        required: true,
                                        message: "菜单别名不能为空",
                                        trigger: "blur",
                                        pattern: /\S/
                                    }
                                ]
                            },
                            {
                                label: "菜单排序",
                                prop: "sort",
                                type: "inputNumber",
                                rules: [
                                    {
                                        required: true,
                                        message: "菜单排序不能为空",
                                        trigger: "blur"
                                    }
                                ]
                            },
                            {
                                label: "菜单类型",
                                prop: "category",
                                type: "radio",
                                dicData: [
                                    {
                                        value: 1,
                                        label: "菜单"
                                    },
                                    {
                                        value: 2,
                                        label: "按钮"
                                    }
                                ],
                                default: 1,
                                rules: [
                                    {
                                        required: true,
                                        message: "菜单类型不能为空",
                                        trigger: "blur"
                                    }
                                ],
                                show: ['categoryName']
                            },
                            {
                                label: "系统类型",
                                prop: "systemType",
                                type: "select",
                                dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.systemType,
                                props: {
                                    label: "dictValue",
                                    value: "dictKey"
                                },
                                show:['systemTypeName']
                            },
                            {
                                label: "菜单备注",
                                prop: "remark",
                                maxlength: 255,
                            }
                        ]
                    }
                ],
                column: [
                    {
                        label: "菜单编号",
                        prop: "code",
                        search: true,
                        width: "130px"
                    },
                    {
                        label: "菜单名称",
                        prop: "name",
                        view: true,
                        search: true
                    },
                    {
                        label: "菜单别名",
                        prop: "alias"
                    },
                    {
                        label: "请求资源",
                        prop: "path"
                    },
                    {
                        label: "菜单资源",
                        prop: "source"
                    },
                    {
                        label: "菜单排序",
                        prop: "sort"
                    },
                    {
                        label: "系统类型",
                        prop: "systemType",
                        search: true,
                        type: "select",
                        dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.systemType,
                        props: {
                            label: "dictValue",
                            value: "dictKey"
                        }
                    },
                    {
                        label: "是否显示",
                        prop: "isVisible",
                        type: "select",
                        hide: function (self) {
                            return self.$store.getters.userInfo
                                && self.$store.getters.userInfo.role_name === "developer";
                        },
                        dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.isVisible,
                        props: {
                            label: "dictValue",
                            value: "dictKey"
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
                add: this.vaildData(this.permission.menu_add, false),
                view: this.vaildData(this.permission.menu_view, false),
                delete: this.vaildData(this.permission.menu_delete, false),
                edit: this.vaildData(this.permission.menu_edit, false),
                添加子集: this.vaildData(this.permission.menu_addChild, false),
                显隐菜单: this.vaildData(this.permission.menu_showHidden, false),
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
    mounted() {
        if (this.$store.getters.userInfo && this.$store.getters.userInfo.role_name === "developer") {
            this.option.menuItem.push({
                label: "显隐菜单",
                command: 2,
                divided: true
            });
            // this.option.column.push();
        }
    },
    methods: {
        loadList(tree, treeNode, resolve) {
            const pid = tree.id;
            this.maps.set(pid, {tree, treeNode, resolve})
            getList({parentId: pid}).then(res => {
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
        rowSave(row, loading, done, type) {
            add(row).then(
                () => {
                    const {parentId} = row;
                    if (this.maps.has(parentId)) {
                        const {tree, treeNode, resolve} = this.maps.get(parentId);
                        this.$set(this.$refs.table.$refs.table.store.states.lazyTreeNodeMap, parentId, []);
                        this.loadList(tree, treeNode, resolve)
                    }
                    ;

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
                getDetail(this.form.id)
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
                remove(row.id).then(() => {
                    this.$refs.table.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!"
                    });
                    const {parentId} = row
                    if (parentId != '0' && this.maps.has(parentId)) {
                        const {tree, treeNode, resolve} = this.maps.get(parentId);
                        this.$set(this.$refs.table.$refs.table.store.states.lazyTreeNodeMap, parentId, []);
                        this.loadList(tree, treeNode, resolve)
                    }
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
                    this.selectionList.forEach(res=>{
                        const {parentId} = res
                        if (parentId != '0' && this.maps.has(parentId)) {
                            const {tree, treeNode, resolve} = this.maps.get(parentId);
                            this.$set(this.$refs.table.$refs.table.store.states.lazyTreeNodeMap, parentId, []);
                            this.loadList(tree, treeNode, resolve)
                        }
                    })
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
        addChild(row) {
            this.$refs.table.rowAdd({
                parentId: row.id,
                category: row.category,
                systemType: row.systemType,
                _parentId: row.parentId,
                hasChildren: row.hasChildren
            });
        },
        menuCommand(cmd, row, index) {
            switch (cmd) {
                case 1:
                    this.addChild(row);
                    break;
                case 2:
                    let msg = "隐藏";
                    if (row.isVisible == 1) msg = "显示";
                    this.$confirm("确定" + msg + "当前菜单吗？", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    })
                        .then(() => {
                            return changeVisible(row.id, 1 - row.isVisible);
                        })
                        .then(() => {
                            if (this.maps.has(row.parentId)) {
                                const {tree, treeNode, resolve} = this.maps.get(row.parentId);
                                this.$set(this.$refs.table.$refs.table.store.states.lazyTreeNodeMap, row.parentId, []);
                                this.loadList(tree, treeNode, resolve)
                            }
                            ;
                            this.$message({
                                type: "success",
                                message: "操作成功!"
                            });
                        });
                    break;
            }
        }
    }
};
</script>
