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
            @table-row-before-edit="tableRowBeforeEdit"
            @table-row-saved="tableRowSaved"
            @menu-command="menuCommand"
        >
            <template slot="menuLeft">
                <el-dropdown trigger="click" @command="handleCommand">
                    <el-button type="primary" size="mini">
                        <i class="el-icon-edit-outline"></i>
                        操作
                        <i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item v-if="permission.allot_header_cancelReceipt" command="1">取消单据
                        </el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </template>
        </nodes-crud>
    </basic-container>
</template>
<script>
import {
    getPage,
    add,
    remove,
    getDetail,
    cancel,
    canEdit
} from "@/api/wms/allot/allotHeader.js";
import {group as group_1} from "./allotHeader/group_1";
import {group as group_2} from "./allotHeader/group_2";
import {mapGetters} from "vuex";

export default {
    name: "allot",
    data() {
        return {
            loading: false,
            selectionList: [], //选中的数据
            form: {},
            data: [],
            option: {
                newBtn: true,
                multiDelBtn: true,
                viewBtn: true,
                delBtn: true,
                menu: true,
                editBtn: true,
                custom: false,
                rowKey: "id",
                menuItem: [
                    {
                        label: '复制',
                        command: 1,
                        divided: true
                    }
                ],
                column: [
                    {
                        prop: "allotBillNo",
                        label: "单据编码",
                        search: true,
                        width: 180,
                        view: true,
                        sortable: true,
                    },
                    {
                        prop: "allotBillState",
                        label: "单据状态",
                        width: 120,
                        sortable: true,
                        type: "select",
                        dicUrl:
                            "/api/blade-system/dict/dictionary?code=" + this.$dict.allotBillState,
                        props: {
                            label: "dictValue",
                            value: "dictKey",
                        },
                    },
                    {
                        prop: "orderNo",
                        label: "上位系统单编号",
                        width: 160,
                        sortable: true,
                    },
                    {
                        prop: "allotRemark",
                        label: "上位系统备注",
                        width: 200,
                        sortable: true,
                    },
                    {
                        prop: "lastUpdateDate",
                        label: "上位系统最后更新时间",
                        width: 180,
                        sortable: true,
                        type: 'date-picker'
                    },
                    {
                        prop: "preCreateDate",
                        label: "上位系统订单创建时间",
                        width: 180,
                        sortable: true,
                        type: 'date-picker'
                    },
                    {
                        prop: "createTime",
                        label: "单据下发时间",
                        width: 160,
                        sortable: true,
                        type: 'date-picker'
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
                add: this.vaildData(this.permission.allot_header_add, false),
                view: this.vaildData(this.permission.allot_header_view, false),
                delete: this.vaildData(this.permission.allot_header_delete, false),
                edit: this.vaildData(this.permission.allot_header_edit, false),
                取消单据: this.vaildData(this.permission.allot_header_cancelReceipt, false),
            }
        },
        ids() {
            let ids = [];
            this.selectionList.forEach((ele) => {
                ids.push(ele.allotBillId);
            });
            return ids.join(",");
        },
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
            ).then((res) => {
                const data = res.data.data;
                page.total = data.total;
                this.data = data.records;
                this.loading = false;
                this.selectionClear();
            });
        },
        beforeOpen(done, type, finish) {
            if (["edit", "view", undefined].includes(type)) {
                if (type === "edit") {
                    canEdit(this.form.allotBillId).then(res => {
                        this.getDetail(done, type, finish);
                    }).catch(() => {
                        finish();
                    });
                }
                if (type === "view") {
                    this.getDetail(done, type, finish);
                }
            }
        },
        getDetail(done, type, finish) {
            getDetail(this.form.allotBillId)
                .then((res) => {
                    debugger
                    if (res.data.data.detailList && res.data.data.detailList.length > 0) {
                        // 替换所有物品显示数量
                        res.data.data.detailList.forEach((detail) => {
                            // 备份原始（不带单位）数量
                            this.$set(detail, "allotPlanQtyCopy", detail.allotPlanQty);
                            // 替换显示的数量
                            detail.allotPlanQty = detail.allotPlanQtyName;
                        });
                    }
                    if (type === undefined) {
                        res.data.data.allotBillId = undefined;
                    }
                    this.form = res.data.data;
                })
                .finally(() => {
                    done();
                });
        },
        rowSave(row, loading, done, type) {
            if (row.detailList && row.detailList.length > 0) {
                if (["edit", undefined].includes(type)) {
                    row.detailList.forEach((detail) => {
                        if (detail.allotDetailId) {
                            detail.allotPlanQty = detail.allotPlanQtyCopy;
                        }
                        if (type === undefined) {
                            detail.allotDetailId = undefined;
                        }
                    });
                }
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
            } else {
                this.$message.warning("至少有一条明细！");
                done();
            }
        },
        tableRowBeforeEdit(row, prop) {
            // 还原显示数量
            if (prop === "detailList") {
                if (row.allotDetailId) {
                    // 还原明细显示的数量
                    row.allotPlanQty = row.allotPlanQtyCopy;
                }
            }
        },
        tableRowSaved(row, prop) {
            // 数据保存后替换备份数据
            if (prop === "detailList") {
                if (row.allotDetailId) {
                    row.allotPlanQtyCopy = row.allotPlanQty;
                }
            }
        },
        onDel(row, index) {
            this.$confirm("确定删除当前数据？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                remove(row.allotBillId).then(() => {
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
        //选中的数据
        selectionChange(list) {
            this.selectionList = list;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.table.toggleSelection();
        },
        handleCommand(cmd) {
            if (!this.selectionList || this.selectionList.length == 0) {
                this.$message.warning("至少选择一张单据才能执行此操作！");
                return;
            }
            let tag = parseInt(cmd);
            this.loading = true;
            switch (tag) {
                case 1: // 取消单据
                    cancel(this.ids).then(res => {
                        if (res.data.code === 200) {
                            this.$refs.table.onLoad();
                            this.$message.success('操作成功');
                        }
                    }).finally(() => {
                        this.loading = false;
                    })
                    break;
            }
        },
        menuCommand(cmd, row, index) {
            switch (cmd) {
                case 1:
                    this.$refs.table.onShowData(row, index, '复制');
                    break;
            }
        }
    },
};
</script>
<style lang="scss">
</style>
