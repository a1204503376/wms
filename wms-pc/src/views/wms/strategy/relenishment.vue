<template>
    <basic-container>
        <nodes-crud
            ref="table"
            v-model="form"
            :option="option"
            :data="data"
            :table-loading="loading"
            :before-open="beforeOpen"
            :permission="permissionList"
            @on-load="onLoad"
            @row-save="rowSave"
            @on-del="onDel"
            @on-multi-del="onMultiDel"
            @selection-change="selectionChange"
        ></nodes-crud>
    </basic-container>
</template>

<script>
import {getPage, remove, add, getDetail} from "@/api/wms/strategy/relenishment";
import {getParamValue} from "../../../util/param";
import {group as group_1} from "./relenishment/group_1";
import {group as group_2} from "./relenishment/group_2";
import {mapGetters} from "vuex";

export default {
    name: "relenishment",
    data() {
        return {
            loading: false,
            data: [],
            selectionList: [],
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
                        label: "补货策略编码",
                        prop: "ssiCode",
                        search: true,
                        view: true,
                        sortable: true,
                    },
                    {
                        label: "补货策略名称",
                        prop: "ssiName",
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "库房名称",
                        prop: "whId",
                        sortable: true,
                        sortProp: 'whId',
                        type: "select",
                        dicUrl: "/api/wms/warehouse/warehouse/list",
                        props: {
                            label: "whName",
                            value: "whId"
                        }
                    }
                ],
                group: []
            },
            form: {}
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                add: this.vaildData(this.permission.relenishment_add, false),
                view: this.vaildData(this.permission.relenishment_view, false),
                delete: this.vaildData(this.permission.relenishment_delete, false),
                edit: this.vaildData(this.permission.relenishment_edit, false),
            }
        },

        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
                ids.push(ele.ssiId);
            });
            return ids.join(",");
        }
    },
    mounted() {
        this.option.group.push(group_1);
        this.option.group.push(group_2);
    },
    methods: {
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(
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
            add(row).then((res) => {
                loading();
                this.$refs.table.onLoad();
                this.$message.success("操作成功！");
            }).catch(()=>{
                done();
            });
        },
        onDel(row, index) {
            this.$confirm("确定删除当前数据？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(() => {
                remove(row.ssiId).then(() => {
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
        selectionChange(val) {
            this.selectionList = val;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.table.toggleSelection();
        },
        beforeOpen(done, type, finish) {
            if (["edit", "view"].includes(type)) {
                getDetail(this.form.ssiId)
                    .then(res => {
                        this.form = res.data.data;
                    })
                    .finally(() => {
                        done();
                    });
            }
        }
    }
};
</script>

<style scoped>
</style>
