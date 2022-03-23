<template>
    <el-dialog
        title="关联标签"
        :visible.sync="visible"
        :close-on-click-modal="false"
        @open="dialogOpen"
        :before-close="dialogClose"
        v-dialogDrag="true"
        width="80%"
        top="3vh"
        class="dialogs"
        append-to-body
    >
        <div class="dialog-body">
            <el-row :gutter="20">
                <el-col :span="24"></el-col>
            </el-row>
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-table
                        class="table-style"
                        ref="dynamicTable"
                        :data="tableData"
                        border
                        height="500px"
                        style="width: 100%"
                        show-overflow-tooltip
                        v-loading="loading"
                        @selection-change="selectionChange"
                    >
                        <el-table-column type="index" width="80" align="center">
                            <template slot="header">
                                <el-button
                                    style="padding:4px;"
                                    type="primary"
                                    icon="el-icon-plus"
                                    size="mini"
                                    circle
                                    @click="rowAdd"
                                ></el-button>
                                <el-button
                                    style="padding:4px;"
                                    type="danger"
                                    icon="el-icon-delete"
                                    size="mini"
                                    circle
                                    @click="rowMultiDel"
                                ></el-button>
                            </template>
                        </el-table-column>
                        <el-table-column type="selection" width="60" align="center"></el-table-column>
                        <el-table-column
                            prop="labelName"
                            label="标签名称"
                            min-width="80"
                            width="200"
                            :show-overflow-tooltip="true"
                        ></el-table-column>
                        <el-table-column
                            prop="skuLevelName"
                            label="包装层级"
                            min-width="80"
                            :show-overflow-tooltip="true"
                        ></el-table-column>
                        <el-table-column
                            prop="isActiveName"
                            label="是否启用"
                            min-width="80"
                            :show-overflow-tooltip="true"
                        ></el-table-column>
                        <el-table-column fixed="right" label="操作" width="80" :key="Math.random()">
                            <template slot-scope="scope">
                                <el-tooltip
                                    class="item"
                                    :enterable="false"
                                    effect="dark"
                                    content="删除"
                                    placement="top"
                                >
                                    <el-button type="text" @click="rowDel(scope)">
                                        <i class="el-icon-delete"></i>
                                    </el-button>
                                </el-tooltip>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-col>
            </el-row>
            <select-dialog
                :visible="selectDialog.visible"
                :title="selectDialog.title"
                :multiple="selectDialog.multiple"
                :queryUrl="selectDialog.queryUrl"
                :saveUrl="selectDialog.saveUrl"
                :saveObj="selectDialog.saveObj"
                :params="selectDialog.params"
                :table="selectDialog.table"
                @callback="callbackSelectDialog"
            ></select-dialog>
        </div>
        <div slot="footer" class="dialog-footer">
            <el-button @click="dialogClose">关 闭</el-button>
        </div>
    </el-dialog>
</template>

<script>
import {getList, remove} from "@/api/wms/basedata/labelEnterprise";
import selectDialog from "@/components/nodes/selectDialog";

export default {
    name: "bindLabelData",
    props: {
        visible: {type: Boolean, default: false},
        dataId: {type: [String, Number], default: null},
    },
    components: {
        selectDialog,
    },
    data() {
        return {
            loading: false,
            selectionList: [],
            tableData: [],
            callback: {
                visible: false,
            },
            selectDialog: {
                visible: false,
                title: "选择标签数据",
                multiple: true,
                queryUrl: "/api/basedata/label/page",
                params: {isActive: 1},
                saveUrl: "/api/basedata/labelenterprise/submit",
                saveObj: {
                    peId: null,
                    wlId: null,
                },
                table: {
                    column: [
                        {
                            label: "标签名称",
                            prop: "labelName",
                            search: true,
                            placeholder: "支持模糊搜索",
                        },
                        {
                            label: "包装层级",
                            prop: "skuLevelName",
                        },
                        {
                            label: "包装层级",
                            prop: "skuLevel",
                            search: true,
                            hide: true,
                            type: "select",
                            dictUrl:
                                "/api/blade-system/dict/dictionary?code=" + this.$dict.skuLevel,
                            props: {
                                label: "dictValue",
                                value: "dictKey",
                            },
                        },
                        {
                            label: "是否启用",
                            prop: "isActiveName",
                        },
                    ],
                },
            },
        };
    },
    computed: {
        ids() {
            let ids = [];
            this.selectionList.forEach((ele) => {
                ids.push(ele.wleId);
            });
            return ids.join(",");
        },
    },
    methods: {
        dialogOpen() {
            this.onLoad();
        },
        dialogClose() {
            this.callback.visible = false;
            this.$emit("callback", this.callback);
        },
        onLoad() {
            this.loading = true;
            getList(this.dataId)
                .then((res) => {
                    this.tableData = res.data.data;
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        selectionChange(val) {
            this.selectionList = val;
        },
        rowAdd(scope) {
            this.selectDialog.saveObj.peId = this.dataId;
            this.selectDialog.visible = true;
        },
        rowDel(scope) {
            this.$confirm("确定删除当前数据？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                remove(scope.row.wleId).then(() => {
                    this.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!",
                    });
                });
            });
        },
        rowMultiDel() {
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
                    this.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!",
                    });
                    this.$refs.table.toggleSelection();
                });
            });
        },
        callbackSelectDialog(res) {
            this.selectDialog.visible = res.visible;
            if (res.success) {
                this.onLoad();
            }
        },
    },
};
</script>

<style scoped>
</style>
