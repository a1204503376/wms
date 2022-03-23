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
                    @selection-change="selectionChange">
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
import {getPage, getDetail, submit, remove} from "@/api/crontab/task";
import {group as group_1} from "./task/group_1";
import {mapGetters} from "vuex";

export default {
    data() {
        return {
            form: {},
            query: {},
            loading: true,
            data: [],
            selectionList: [],
            option: {
                newBtn: true,
                multiDelBtn: true,
                editBtn: true,
                delBtn: true,
                menu: true,
                custom: false,
                column: [
                    {
                        label: "任务名",
                        prop: "crontabTaskName",
                        sortable: true,
                        search: true,
                    },
                    {
                        label: "AppId",
                        prop: "appid",
                        sortable: true,
                        search: true
                    },
                    {
                        label: "url",
                        prop: "url",
                        sortable: true,
                        search: true,
                    },
                    {
                        label: "请求方法",
                        prop: "httpMethod",
                        search: true
                    },
                    {
                        label: "请求参数",
                        prop: "params",
                        search: true,
                    },
                ],
                group: []
            },
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                add: this.vaildData(this.permission.crontabTask_add, false),
                view: this.vaildData(this.permission.crontabTask_view, false),
                delete: this.vaildData(this.permission.crontabTask_delete, false),
                edit: this.vaildData(this.permission.crontabTask_edit, false),
            }
        },
        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
            });
            return ids.join(",");
        }
    },
    mounted() {
        this.option.group.push(group_1);
    },
    methods: {
        rowSave(row, loading, done, type) {
            submit(row).then(() => {
                loading();
                this.$refs.curd.onLoad();
                this.$message({
                    type: "success",
                    message: "操作成功!"
                });
            }, error => {
                done();
            });
        },
        rowDel(row, index) {
            this.$confirm("确定将选择数据删除?", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(() => {
                remove(row.id).then(() => {
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
            if (["edit", "view"].includes(type)) {
                getDetail(this.form.id).then(res => {
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
                this.$refs.curd.page.total = data.total;
                this.data = data.records;
                this.loading = false;
                this.selectionClear();
            });
        }
    }
};
</script>

<style>
</style>
