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
import {getPage, remove, add, getDetail} from "@/api/wms/strategy/instock";
import {getParamValue} from "../../../util/param";
import {group as group_1} from "./instock/group_1";
import {group as group_2} from "./instock/group_2";
import {mapGetters} from "vuex";
export default {
    name: "instock",
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
                        label: "上架策略编码",
                        prop: "ssiCode",
                        search: true,
                        view: true,
                        sortable: true,
                    },
                    {
                        label: "上架策略名称",
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
                add: this.vaildData(this.permission.instock_add, false),
                view: this.vaildData(this.permission.instock_view, false),
                delete: this.vaildData(this.permission.instock_delete, false),
                edit: this.vaildData(this.permission.instock_edit, false),
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
        // 加载批属性数量
        let lotCount = getParamValue(this.$param.system.lotCount);
        let column = group_2.column[0];
        let childColumn = column.column[0];
        let skuLotSource = group_2.column[0].column[2].column[0].children[0].dicData;
        for (let i = 0; i < lotCount; i++) {
            let index = i + 1;
            let obj = {
                label: "混合存放" + index,
                prop: "skuLotMix" + index + "Desc",
                width: 120
            };
            let filterList = column.children.filter(child => {
                return child.prop === obj.prop;
            });
            if (!filterList || filterList.length === 0) {
                column.children.push(obj);
            }
            filterList = childColumn.column.filter(child => {
                return child.prop === "skuLotMix" + index;
            });
            if (!filterList || filterList.length === 0) {
                childColumn.column.push({
                    label: "混合存放" + index,
                    prop: "skuLotMix" + index,
                    type: "select",
                    dicData: [
                        {value: "0", label: "不允许"},
                        {value: "1", label: "允许"}
                    ],
                    default: "1",
                    change: function (val, obj, col, data) {
                        if (obj) {
                            data[col.prop + "Desc"] = obj.label;
                        }
                    }
                });
            }
            filterList = skuLotSource.filter(child => {
                return child.prop === index;
            });
            if (!filterList || filterList.length === 0) {
                skuLotSource.push({
                    label: "批属性" + index,
                    value: index
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
