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
import {getPage, getDetail, submit, remove} from "@/api/crontab/scheme";
import {group as group_1} from "./scheme/group_1";
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
                        label: "库房",
                        prop: "whId",
                        sortable: true,
                        search: true,
                        width: 180,
                        type: "select",
                        dicUrl: "/api/wms/warehouse/warehouse/list",
                        props: {
                            label: "whName",
                            value: "whId"
                        }
                    },
                    {
                        label: "任务",
                        prop: "crontabTaskName",
                        sortable: true,
                        search: true,
                        width: 180
                    },
                    {
                        label: "年",
                        prop: "years",
                        sortable: true,
                        search: true
                    },
                    {
                        label: "月",
                        prop: "months",
                        sortable: true,
                        search: true
                    },
                    {
                        label: "日",
                        prop: "days",
                        sortable: true,
                        search: true
                    },
                    {
                        label: "周",
                        prop: "weekdays",
                        sortable: true,
                        search: true
                    },
                    {
                        label: "时",
                        prop: "hours",
                        sortable: true,
                        search: true
                    },
                    {
                        label: "分",
                        prop: "minutes",
                        sortable: true,
                        search: true
                    },
                    {
                        label: "秒",
                        prop: "seconds",
                        sortable: true,
                        search: true
                    },
                    {
                        label: "时间范围(秒)",
                        prop: "secondRange",
                        sortable: true,
                        search: true,
                        width: 180
                    },
                    {
                        label: '是否启用',
                        prop: 'statusDesc',
                    }
                ],
                group: []
            },
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                add: this.vaildData(this.permission.scheme_add, false),
                delete: this.vaildData(this.permission.scheme_delete, false),
                edit: this.vaildData(this.permission.scheme_edit, false),
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
                console.log(error);
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
