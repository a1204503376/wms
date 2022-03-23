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
        >
            <template slot="menuLeft">
                <el-button
                    plain
                    size="mini"
                    class="Printing"
                    @click="jurisdiction"
                    v-if="permission.topmenu_setting"
                >菜单配置
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
    import {getList, add, remove, getDetail} from "@/api/core/topMenu";
    import roleJurisDialog from "./topmenu/roleJurisDialog";
    import {mapGetters} from "vuex";

    export default {
        name: "topMenu",
        components: {
            roleJurisDialog,
        },
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
                    newBtn: true,
                    multiDelBtn: true,
                    viewBtn: true,
                    editBtn: true,
                    delBtn: true,
                    menu: true,
                    page: false,
                    rowKey: "id",
                    custom: false,
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
                                            pattern: /\S/,
                                        }
                                    ]
                                },
                                {
                                    label: "菜单图标",
                                    prop: "source",
                                    type: 'icon',
                                    rules: [
                                        {
                                            required: true,
                                            message: "菜单图标不能为空",
                                            trigger: "blur",
                                            pattern: /\S/,
                                        }
                                    ]
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
                                            pattern: /\S/,
                                        }
                                    ]
                                },
                                {label: "菜单排序", prop: "sort", type: 'inputNumber'}
                            ]
                        }
                    ],
                    column: [
                        {
                            label: "菜单名称",
                            prop: "name",
                            search: true,
                            view: true,
                            sortable: true,
                        },
                        {label: "菜单图标", prop: "source", sortable: true,},
                        {label: "菜单编号", prop: "code", search: true, sortable: true,},
                        {label: "菜单排序", prop: "sort", sortable: true,}
                    ]
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
                    add: this.vaildData(this.permission.topmenu_add, false),
                    view: this.vaildData(this.permission.topmenu_view, false),
                    delete: this.vaildData(this.permission.topmenu_delete, false),
                    edit: this.vaildData(this.permission.topmenu_edit, false),
                    菜单配置: this.vaildData(this.permission.topmenu_setting, false),
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
            onLoad(page, params = {}) {
                this.loading = true;
                getList(
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
            //权限设置
            jurisdiction() {
                if (this.selectionList.length === 0) {
                    this.$message.warning("请选择至少一条数据");
                    return;
                }
                this.dialogJuris.dataSource = this.ids;
                this.dialogJuris.visible = true;
            },
            callbackJuris(res) {
                this.dialogJuris.visible = res.visible;
                if (res.result) {
                    this.$refs.table.onLoad();
                }
            },
        }
    };
</script>
