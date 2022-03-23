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
                @row-save="rowSave"
                :before-open="beforeOpen"
                @selection-change="selectionChange"
        ></nodes-crud>
    </basic-container>
</template>
<script>
    import {getList, add, remove, getDetail} from "@/api/wms/core/barcodeRule";
    import {mapGetters} from "vuex";

    export default {
        name: "barcodeRule",
        data() {
            return {
                titleText: "",
                dialogVisible: false,
                loading: false,
                dialogs: false, //搜索界面的显隐
                isView: false,
                isShow: false,
                selectionList: [], //选中的数据
                data: [], //列表数据
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
                                    label: "库房",
                                    type: "select",
                                    dicUrl: "/api/wms/warehouse/warehouse/list",
                                    props: {
                                        label: "whName",
                                        value: "whId"
                                    },
                                    rules: [
                                        {required: true, message: "库房不能为空", trigger: "blur"}
                                    ]
                                },
                                {
                                    label: "条码规则",
                                    prop: "barcodeRule",
                                    maxlength: 288,
                                    rules: [
                                        {
                                            required: true,
                                            message: "条码规则不能为空",
                                            trigger: "blur",
                                            pattern: /\S/,
                                        }
                                    ]
                                },
                                {
                                    label: "条码类型",
                                    prop: "barcodeType",
                                    type: "select",
                                    dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.barcodeType,
                                    props: {
                                        label: "dictValue",
                                        value: "dictKey"
                                    },
                                    rules: [
                                        {
                                            required: true,
                                            message: "条码类型不能为空",
                                            trigger: "blur"
                                        }
                                    ]
                                }
                            ]
                        }
                    ],
                    column: [

                        {
                            label: "库房",
                            prop: "whId",
                            search: true,
                            type: "select",
                            dicUrl: "/api/wms/warehouse/warehouse/list",
                            props: {
                                label: "whName",
                                value: "whId"
                            },
                        },
                        {
                            label: "条码规则",
                            prop: "barcodeRule",
                            search: true,
                        },
                        {
                            label: "条码类型",
                            prop: "barcodeType",
                            type: "select",
                            search: true,
                            dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.barcodeType,
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
                    addBtn: this.vaildData(this.permission.barcoderule_add, false),
                    viewBtn: this.vaildData(this.permission.barcoderule_view, false),
                    delBtn: this.vaildData(this.permission.barcoderule_delete, false),
                    editBtn: this.vaildData(this.permission.barcoderule_edit, false),
                    cmdBtn: this.vaildData(this.permission.barcoderule_command, false),
                }
            },
            ids() {
                let ids = [];
                this.selectionList.forEach(ele => {
                    ids.push(ele.sbrId);
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
            //默认渲染数据
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
                    getDetail(this.form.sbrId)
                        .then(res => {
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
                    type: "warning"
                }).then(() => {
                    remove(row.sbrId).then(() => {
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
        }
    };
</script>
<style lang="scss">

</style>
