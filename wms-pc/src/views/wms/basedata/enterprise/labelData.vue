<template>
    <el-dialog title="标签数据"
               :visible.sync="visible"
               :close-on-click-modal="false"
               @opened="dialogOpen"
               :before-close="dialogClose"
               v-dialogDrag="true"
               width="80%"
               top="3vh"
               class="dialogs"
               append-to-body>
        <div class="dialog-body">
            <nodes-curd
                    ref="table"
                    v-model="form"
                    :option="option"
                    :data="data"
                    height="500px"
                    :table-loading="loading"
                    @on-load="onLoad"
                    @on-new="onNew"
                    @on-edit="onEdit"
                    @on-del="onDel"
                    @on-multi-del="onMultiDel"
                    @selection-change="selectionChange"
                    @menu-command="menuCommand"
                    @search-change="searchChange"
            ></nodes-curd>
            <edit :visible="edit.visible"
                  :dataSource="edit.dataSource"
                  @callback="callbackEdit"></edit>
        </div>
        <div slot="footer" class="dialog-footer">
            <el-button @click="dialogClose">关 闭</el-button>
        </div>
    </el-dialog>
</template>

<script>
    import {getPage, remove, getDetail} from "@/api/wms/basedata/label";
    import edit from "./labelData/edit";

    export default {
        name: "labelData",
        props: {
            visible: {type: Boolean, default: false}
        },
        components: {edit},
        computed: {
            ids() {
                let ids = [];
                this.selectionList.forEach(ele => {
                    ids.push(ele.wlId);
                });
                return ids.join(",");
            }
        },
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
                    custom: true,
                    permission: false,
                    column: [
                        {
                            prop: 'labelName',
                            label: '标签名称',
                        },
                        {
                            prop: 'skuLevelName',
                            label: '包装层级',
                        },
                        {
                            prop: 'isActiveName',
                            label: '是否启用',
                        }
                    ],
                },
                callback: {
                    visible: false
                },
                edit: {
                    visible: false,
                    dataSource: null
                }
            }
        },
        methods: {
            dialogOpen() {
                this.$refs.table.onLoad();
            },
            dialogClose() {
                this.callback.visible = false;
                this.$emit('callback', this.callback);
            },
            onNew() {
                this.edit.dataSource = null;
                this.edit.visible = true;
            },
            onLoad(page, params = {}) {
                this.loading = true;
                getPage(
                    page.currentPage,
                    page.pageSize,
                    Object.assign(params, this.query)
                ).then(res => {
                    const data = res.data.data;
                    this.$refs.table.page.total = data.total;
                    this.data = data.records;
                    this.loading = false;
                    this.selectionClear();
                });
            },
            onEdit(row, index) {
                this.loading = true;
                getDetail(row.wlId).then(res => {
                    this.edit.dataSource = res.data.data;
                    this.edit.visible = true;
                }).finally(() => {
                    this.loading = false;
                })
            },
            onDel(row, index) {
                this.$confirm("确定删除当前数据？", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    remove(row.wlId).then(() => {
                        this.$refs.table.onLoad();
                        this.$message({
                            type: "success",
                            message: "操作成功!"
                        });
                    });
                }).catch(() => {

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
            menuCommand() {

            },
            searchChange() {

            },
            callbackEdit(res) {
                this.edit.visible = res.visible;
                if (res.success) {
                    this.$refs.table.onLoad();
                }
            }
        }
    }
</script>

<style scoped>

</style>
