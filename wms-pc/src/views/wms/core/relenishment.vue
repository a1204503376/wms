<template>
    <basic-container>
        <el-row>
            <el-col :span="24">
                <nodes-crud
                    ref="table"
                    :option="option"
                    :data="data"
                    :table-loading="loading"
                    :permission="permissionList"
                    @on-load="onLoad"
                    @on-view="onView"
                    @on-multi-del="onMultiDel"
                    @selection-change="selectionChange"
                    @menu-command="menuCommand"
                    @search-change="searchChange"
                >
                    <template slot="menuLeft">
                        <el-button
                            type="primary"
                            icon="el-icon-plus"
                            size="mini"
                            @click="relTask"
                        >下发补货任务
                        </el-button>
                    </template>
                </nodes-crud>
            </el-col>
        </el-row>

    </basic-container>
</template>
<script>
import editDialog from "@/components/nodes/editDialog";
import {getPage, getDetail, add, canEdit, relTask, remove, checkTask} from "@/api/wms/core/relenishment";
import {getParamValue} from "../../../util/param";
import {getList as getZoneList} from "@/api/wms/warehouse/zone";
import {group as group_1} from "./relenishment/group_1.js";
import {group as group_2} from "./relenishment/group_2.js";
import {mapGetters} from "vuex";

export default {
    name: "relenishment",
    components: {
        editDialog
    },
    data() {
        return {
            loading: false,
            isView: false,
            isShow: false,

            makeupSource: [],
            selectionList: [],
            data: [],
            searchData: {},
            option: {
                custem: 'undefined',
                newBtn: false,
                multiDelBtn: true,
                addBtn: false,
                viewBtn: false,
                editBtn: false,
                delBtn: false,
                menu: true,
                menuItem: [
                    {
                        label: '编辑',
                        command: 1,
                        divided: true
                    },
                    {
                        label: '删除',
                        command: 2,
                        divided: true
                    }
                ],
                column: [
                    {
                        label: '补货单编码',
                        prop: 'relBillNo',
                        search: true,
                        view: true,
                        width: 180
                    },
                    {
                        label: '执行状态',
                        prop: 'relBillState',
                        search: true,
                        type: "select",
                        dicUrl:
                            "/api/blade-system/dict/dictionary?code=" + this.$dict.relBillState,
                        props: {
                            label: "dictValue",
                            value: "dictKey",
                        },
                    },
                    {
                        label: '生成时间',
                        prop: 'createTime',
                        search: true,
                        type: 'date-picker'
                    },
                    {
                        label: '创建用户',
                        prop: 'createUser',
                        search: true,
                        type: 'select-table-user',
                        dicUrl: '/api/blade-user/list',
                        props: {
                            value: 'id',
                            label: 'realName'
                        }
                    },
                    {
                        label: '完成时间',
                        prop: 'finishDate',
                        search: true,
                        type: 'date-picker'
                    },
                ]
            }
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                addBtn: this.vaildData(this.permission.relenishment_add, false),
                viewBtn: this.vaildData(this.permission.relenishment_view, false),
                delBtn: this.vaildData(this.permission.relenishment_delete, false),
                editBtn: this.vaildData(this.permission.relenishment_edit, false),
                cmdBtn: this.vaildData(this.permission.relenishment_command, false),
            }
        },
        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
                ids.push(ele.relBillId);
            });
            return ids.join(",");
        },
    },

    methods: {
        relTask() {
            if (!this.selectionList || this.selectionList.length == 0) {
                this.$message.warning("至少选择一条数据！");
                return;
            }
            this.loading = true;
            let self = this;
            checkTask(
                this.ids
            ).then(data => {
                if (data.data.data) {
                    this.relTaskRequst();
                } else {
                    this.$confirm("单据中存在需要人工干预的明细,是否继续下发任务吗？", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning",
                    })
                        .then(() => {
                            this.relTaskRequst();
                        })
                }

            });

        },
        relTaskRequst() {
            relTask(
                this.ids
            ).then(data => {
                if (data.data.success) {
                    debugger
                    this.$message.success("任务下发成功！");
                    this.$refs.table.onLoad();
                } else {
                    this.$message.warning("任务下发失败！");
                }
            }).finally(() => {
                this.loading = false;
            });
        },
        onView(row) {
            this.openRelenishmentDialog(row, 'view');
        },
        //取消回调
        childCancel() {
            this.trace.visible = false;
        },
        openRelenishmentDialog(row, type) {
            if (type == 'view') {
                this.$refs.table.$refs.dialogEdit.isView = true;
                this.$refs.table.$refs.dialogEdit.isEdit = false;
            } else if (type == 'edit') {
                this.$refs.table.$refs.dialogEdit.isView = false;
                this.$refs.table.$refs.dialogEdit.isEdit = true;
            }
            this.$refs.table.$refs.dialogEdit.isNew = false;
            this.$refs.table.$refs.dialogEdit.width = "80%";
            this.$refs.table.$refs.dialogEdit.group = [
                group_1,
                group_2
            ];
            this.$refs.table.$refs.dialogEdit.visible = true;
            let self = this;
            this.$refs.table.$refs.dialogEdit.$on('callback', function (callback) {
                let dialogEdit = self.$refs.table.$refs.dialogEdit;
                if (callback.success) {
                    if (dialogEdit.isEdit) {
                        add(callback.data).then(
                            () => {
                                callback.loading();
                                self.onLoad(self.$refs.table.page);
                                this.$message.success("操作成功！");
                            },
                            error => {
                                done();
                            }
                        );
                    }
                } else {
                    dialogEdit.visible = callback.visible;
                }
                dialogEdit.$off('callback');
            });
            this.$refs.table.$refs.dialogEdit.$on('before-open', function (done, type, finish) {
                let dialogEdit = self.$refs.table.$refs.dialogEdit;
                if (["edit", "view", undefined].includes(type)) {
                    if (type === "edit") {
                        canEdit(row.relBillId)
                            .then(res => {
                                if (res.data.data) {
                                    getDetail(row.relBillId)
                                        .then(res => {
                                            dialogEdit.dataSource = res.data.data;
                                        })
                                        .finally(() => {
                                            done();
                                        });
                                } else {
                                    this.$message.warning("当前补货单不允许编辑！");
                                    finish();
                                }
                            })
                            .catch(() => {
                                finish();
                            });
                    } else {
                        getDetail(row.relBillId)
                            .then(res => {
                                dialogEdit.dataSource = res.data.data;
                            })
                            .finally(() => {
                                done();
                            });
                    }
                }
                dialogEdit.$off('before-open');
            });
        },
        //默认渲染数据
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
        //选中的数据
        selectionChange(list) {
            this.selectionList = list;
        },
        selectionClear() {
            this.selectionList = [];
        },
        searchChange(data) {
            this.searchData = data;
        },
        onDel(row, index) {
            this.$confirm("确定删除当前数据？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(() => {
                remove(row.relBillId).then(() => {
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
        menuCommand(cmd, row, index) {
            switch (cmd) {
                case 1:
                    this.openRelenishmentDialog(row, 'edit');
                    break;
                case 2:
                    this.onDel(row, index)
                    break;
            }
        }
    }
};
</script>
<style lang="scss">
</style>
