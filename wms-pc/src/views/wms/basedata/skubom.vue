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
            @on-multi-del="onMultiDel"
            @selection-change="selectionChange"
            @menu-command="menuCommand"
            @search-change="searchChange"
            :menuWidth="option.menuWidth"
            :before-open="beforeOpen"
        >
        </nodes-crud>
    </basic-container>
</template>

<script>

import {getPage, getDetail, submit, remove} from "@/api/wms/basedata/skubom";
import {group as group_1} from "./skubom/group_1.js";
import {mapGetters} from "vuex";

export default {
    name: "sku",
    components: {
    },
    data() {
        return {
            form: {},
            loading: false,
            data: [],
            selectionList: [], //选中的数据
            option: {
                newBtn: true,
                multiDelBtn: true,
                viewBtn: true,
                editBtn: true,
                delBtn: true,
                menu: true,
                custom: false,
                column: [
                    {
                        prop: "woId",
                        label: "所属货主",
                        search: true,
                        type: "select",
                        dicUrl: "/api/wms/basedata/owner/list",
                        props: {
                            label: "ownerName",
                            value: "woId",
                        },
                    },
                    {
                        prop: "skuCode",
                        label: "物品编码",
                        search: true,
                        width: 100,
                        sortable: true,
                    },
                    {
                        prop: "skuName",
                        label: "物品名称",
                        search: true,
                        width: 100,
                        sortable: true,
                    },
                    {
                        prop: "wsuName",
                        label: "主单位",
                        width: 100,
                        sortable: false,
                        sortProp: "wsuName",
                    },
                    {
                        prop: "joinSkuCode",
                        label: "组合物品编码",
                        width: 100,
                        sortable: true,
                        sortProp: "joinSkuCode",
                    },
                    {
                        prop: "joinSkuName",
                        label: "组合物品名称",
                        width: 100,
                        sortable: true,
                        sortProp: "joinSkuName",
                    },
                    {
                        prop: "qty",
                        label: "组合物品数量",
                        width: 100,
                        sortable: false,
                        sortProp: "qty",
                    },
                    {
                        prop: "joinWsuName",
                        label: "组合单位",
                        width: 100,
                        sortable: false,
                        sortProp: "joinWsuName",
                    },
                ],
                group: [],
            },
            searchData: {},
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                add: this.vaildData(this.permission.skubom_save, false),
                delete: this.vaildData(this.permission.skubom_delete, false),
                edit: this.vaildData(this.permission.skubom_edit, false),
                view: this.vaildData(this.permission.skubom_view, false),
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
    mounted() {
        this.option.group.push(group_1);
    },
    methods: {
        //列表
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(
                page.currentPage,
                page.pageSize,
                Object.assign(params, this.query)
            ).then((res) => {
                const data = res.data.data;
                page.total = data.total;
                this.data = data.records;
                this.loading = false;
                this.selectionClear();
            });
        },
        //保存
        rowSave(row, loading, done, type) {
            submit(this.form).then(res => {
                if (res.data.success) {
                        loading();
                        this.$refs.table.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!"
                    });
                } else {
                    this.$message({
                        type: "error",
                        message: "触发"
                    });
                }
            },
                error => {
                window.console.log(error);
                done();
            })
        },
        //行单删
        onDel(row, index) {
            this.$confirm("确定删除当前数据？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
                .then(() => {
                    remove(row.id).then(() => {
                        this.$refs.table.onLoad();
                        this.$message({
                            type: "success",
                            message: "操作成功!",
                        });
                    });
                })
                .catch(() => {
                });
        },
        //单、多删
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
        //选中的数据
        selectionChange(list) {
            this.selectionList = list;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.table.toggleSelection();
        },
        menuCommand(cmd, row, index) {
            switch (cmd) {
                case 1:
                    this.outibrary(row, index);
                    break;
                case 2:
                    this.openInstock(row, index);
                    break;
            }
        },
        searchChange(data) {
            this.searchData = data;
        },
    },
};
</script>
<style lang="scss">
</style>
