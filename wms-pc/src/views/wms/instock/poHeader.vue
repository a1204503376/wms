<template>
    <basic-container>
        <nodes-crud ref="curd"
                    v-model="form"
                    :option="option"
                    :data="data"
                    :table-loading="loading"
                    :before-open="beforeOpen"
                    :permission="permissionList"
                    @on-load="onLoad"
                    @row-save="rowSave"
                    @on-del="rowDel"
                    @on-multi-del="onMultiDel"
                    @selection-change="selectionChange"
                    @menu-command="menuCommand">
            <template slot="menuLeft">
            </template>
            <template slot="menuRight">

            </template>
            <template slot="menu">

            </template>
        </nodes-crud>
    </basic-container>
</template>

<script>
import {getPage, getDetail, submit, remove, createAsn} from "@/api/wms/instock/poHeader";
import {option} from "./poHeader/option.js";
import {mapGetters} from "vuex";

export default {
    data() {
        return {
            form: {},
            query: {},
            loading: true,
            data: [],
            selectionList: [],
            option: option
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                add: this.vaildData(this.permission.poHeader_add, false),
                view: this.vaildData(this.permission.poHeader_view, false),
                delete: this.vaildData(this.permission.poHeader_delete, false),
                edit: this.vaildData(this.permission.poHeader_edit, false),
            }
            },
        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
                ids.push(ele.poBillId);
            });
            return ids.join(",");
        }
    },
    mounted() {

    },
    methods: {
        rowSave(row, loading, done, type) {
            const title = this.$refs.curd.$refs.dialogEdit.title;
            if (title === '生成收货单') {
                createAsn(row).then(() => {
                    loading();
                    this.$refs.curd.onLoad();
                    this.$message({
                        type: 'success',
                        message: '操作成功!'
                    });
                }, error => {
                    done();
                    console.log(error);
                })
            } else {
                submit(row).then(() => {
                    loading();
                    this.$refs.curd.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!"
                    });
                }, error => {
                    done();
                    console.log(error);
                });
            }
        },
        rowDel(row, index) {
            this.$confirm("确定将选择数据删除?", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(() => {
                remove(row.poBillId).then(() => {
                    this.$refs.curd.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!"
                    });
                });
            });
        },
        onMultiDel() {
            if (!this.selectionList || this.selectionList.length === 0) {
                this.$message.warning("请选择至少一条数据");
                return;
            }
            this.$confirm("确定将选择数据删除?", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(() => {
                remove(this.ids).then(() => {
                    this.$refs.curd.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!"
                    });
                    this.$refs.curd.toggleSelection();
                });
            });
        },
        beforeOpen(done, type, finish) {
            if (["edit", "view", undefined].includes(type)) {
                getDetail(this.form.poBillId).then(res => {
                    this.form = res.data.data;
                }).finally(() => {
                    done();
                });
            }
        },
        selectionChange(list) {
            this.selectionList = list;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.curd.toggleSelection();
        },
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(page.currentPage, page.pageSize, params).then(res => {
                const data = res.data.data;
                page.total = data.total;
                this.data = data.records;
                this.loading = false;
                this.selectionClear();
            });
        },
        menuCommand(cmd, row, index) {
            switch (cmd) {
                case 1:
                    this.$refs.curd.onShowData(row, index, '生成收货单');
                    break;
            }
        }
    }
};
</script>

<style>
</style>
