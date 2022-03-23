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
            @load-List="loadList"
        >
            <template slot="isSealed" slot-scope="row">
                <span v-if="row.isSealed==0">否</span>
                <span v-else>是</span>
            </template>
        </nodes-crud>
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
} from "@/api/core/dict";
import {mapGetters} from "vuex";

export default {
    name: "dict",
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
                custom: false,
                menuItem: [
                    {
                        label: "添加子级",
                        command: 1,
                        divided: true
                    }
                ],
                lazy: true,
                group: [
                    {
                        column: [
                            {
                                label: "字典编号",
                                prop: "code",
                                maxlength: 30,
                                rules: [
                                    {
                                        required: true,
                                        message: "字典编号不能为空",
                                        trigger: "blur",
                                        pattern: /\S/
                                    }
                                ]
                            },
                            {
                                label: "字典名称",
                                prop: "dictValue",
                                maxlength: 255,
                                rules: [
                                    {
                                        required: true,
                                        message: "字典名称不能为空",
                                        trigger: "blur",
                                        pattern: /\S/
                                    }
                                ]
                            },
                            {
                                label: "上级字典",
                                prop: "parentId",
                                type: "select-tree",
                                dicUrl: "/api/blade-system/dict/tree",
                                props: {
                                    label: "title",
                                    value: "id",
                                    children: "children"
                                },
                                clearable: true,
                                change: (val, obj, col, data) => {
                                    getDetail(val).then(res => {
                                        data.code = res.data.data.code;
                                        data.parentId = res.data.data.id;
                                    });
                                }
                            },
                            {
                                label: "字典键值",
                                prop: "dictKey",
                                type: "inputNumber",
                                min: -1,
                                rules: [
                                    {
                                        required: true,
                                        message: "字典键值不能为空",
                                        trigger: "blur"
                                    }
                                ]
                            },
                            {
                                label: "字典排序",
                                prop: "sort",
                                type: "inputNumber",
                                rules: [
                                    {
                                        required: true,
                                        message: "字典排序不能为空",
                                        trigger: "blur"
                                    }
                                ]
                            },
                            {
                                label: "是否封存",
                                prop: "isSealed",
                                type: "select",
                                props: {label: "label", value: "value"},
                                dicData: [
                                    {label: "否", value: 0},
                                    {label: "是", value: 1}
                                ],
                                rules: [
                                    {
                                        required: true,
                                        message: "是否封存不能为空",
                                        trigger: "blur"
                                    }
                                ]
                            },
                            {
                                label: "字典备注",
                                prop: "remark",
                                span: 24,
                                maxlength: 255
                            }
                        ]
                    }
                ],
                column: [
                    {
                        label: "字典编号",
                        prop: "code",
                        search: true
                    },
                    {
                        label: "字典名称",
                        prop: "dictValue",
                        search: true,
                        view: true
                    },
                    {
                        label: '租户',
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
                        label: "字典键值",
                        prop: "dictKey"
                    },
                    {
                        label: "字典排序",
                        prop: "sort"
                    },
                    {
                        label: "是否封存",
                        prop: "isSealed",
                        slot: true
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
    watch: {
        form: {
            handler(val, oldVal) {
                if (val.parentId) {
                    this.option.group[0].column[0].disabled = true
                } else {
                    this.option.group[0].column[0].disabled = false
                }
            },
            deep: true //true 深度监听
        }
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                add: this.vaildData(this.permission.dict_add, false),
                view: this.vaildData(this.permission.dict_view, false),
                delete: this.vaildData(this.permission.dict_delete, false),
                edit: this.vaildData(this.permission.dict_edit, false),
                添加子集: this.vaildData(this.permission.dict_addChild, false),
                显隐菜单: this.vaildData(this.permission.dict_showHidden, false),
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
                label: "显隐字典",
                command: 2,
                divided: true
            });
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
            add(row).then(res => {
                if (this.maps.has(row.parentId)) {
                    const {tree, treeNode, resolve} = this.maps.get(row.parentId);
                    this.$set(this.$refs.table.$refs.table.store.states.lazyTreeNodeMap, row.parentId, []);
                    this.loadList(tree, treeNode, resolve)
                };
                this.$refs.table.onLoad();
                this.$message.success("操作成功！");
                loading();
            }).finally(() => {
                done();
            });
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
                    this.$refs.table.toggleSelection();
                });
            });
        },
        addChild(row) {
            this.$refs.table.rowAdd({
                parentId: row.id,
                code: row.code
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
                case 1:
                    this.addChild(row);
                    break;
                case 2:
                    let msg = "隐藏";
                    if (row.isVisible == 1) msg = "显示";
                    this.$confirm("确定" + msg + "当前字典吗？", {
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
