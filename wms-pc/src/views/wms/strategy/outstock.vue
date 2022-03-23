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
import {getPage, add, getDetail, remove} from "@/api/wms/strategy/outstock";
import {getParamValue} from "../../../util/param";
import {group as group_1} from "./outstock/group_1";
import {group as group_2} from "./outstock/group_2";
import {mapGetters} from "vuex";
export default {
    name: "outstock",
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
                custom: false,
                column: [
                    {
                        label: "策略编码",
                        prop: "ssoCode",
                        search: true,
                        view: true,
                        sortable: true,
                    },
                    {
                        label: "策略名称",
                        prop: "ssoName",
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "库房名称",
                        prop: "whId",
                        sortable: true,
                        sortProp: "whId",
                        type: "select",
                        dicUrl: "/api/wms/warehouse/warehouse/list",
                        props: {
                            label: "whName",
                            value: "whId"
                        }
                    },
                ],
                group: [],
            },
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                add: this.vaildData(this.permission.outstock_add, false),
                view: this.vaildData(this.permission.outstock_view, false),
                delete: this.vaildData(this.permission.outstock_delete, false),
                edit: this.vaildData(this.permission.outstock_edit, false),
            }
        },
        ids() {
            let ids = [];
            this.selectionList.forEach((ele) => {
                ids.push(ele.ssoId);
            });
            return ids.join(",");
        },
    },
    mounted() {
        this.option.group.push(group_1);
        this.option.group.push(group_2);
        // 加载批属性数量
        let lotCount = getParamValue(this.$param.system.lotCount);
        let column = group_2.column[0];
        let skuUdfSource =
            group_2.column[0].column[2].column[0].children[0].dicData;
        let skuLotSource =
            group_2.column[0].column[3].column[0].children[0].dicData;
        for (let i = 0; i < lotCount; i++) {
            let index = i + 1;
            let filterList = skuUdfSource.filter((child) => {
                return child.value === index;
            });
            if (!filterList || filterList.length === 0) {
                skuUdfSource.push({
                    label: "自定义属性" + index,
                    value: index,
                });
            }
            filterList = skuLotSource.filter((child) => {
                return child.value === index;
            });
            if (!filterList || filterList.length === 0) {
                skuLotSource.push({
                    label: "批属性" + index,
                    value: index,
                });
            }
        }
    },
    methods: {
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
        beforeOpen(done, type, finish) {
            if (["edit", "view"].includes(type)) {
                getDetail(this.form.ssoId)
                    .then((res) => {
                        const data = res.data.data;
                        data.outstockDetailVO.forEach(detail => {
                            let strList = detail.outstockFunction.split(',');
                            let intList = [];
                            strList.forEach(str=>intList.push(parseInt(str)));

                            detail.outstockFunction = intList;
                        });
                        this.form = res.data.data;
                    })
                    .finally(() => {
                        done();
                    });
            }
        },
        rowSave(row, loading, done, type) {
            this.$set(row, "outstockDetailDTO", row.outstockDetailVO);
            row.outstockDetailVO.forEach(detail => {
                detail.outstockFunction = detail.outstockFunction.join(',');
            });
            add(row).then(
                () => {
                    loading();
                    this.$refs.table.onLoad();
                    this.$message.success("操作成功！");
                },
                (error) => {
                    done();
                }
            );
        },
        onDel(row, index) {
            this.$confirm("确定删除当前数据？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                remove(row.ssoId).then(() => {
                    this.$refs.table.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!",
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
        selectionChange(val) {
            this.selectionList = val;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.table.toggleSelection();
        },
    },
};
</script>

<style scoped>
</style>
