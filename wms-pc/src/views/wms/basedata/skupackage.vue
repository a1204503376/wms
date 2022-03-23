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
            @search-change="searchChange"
            class="skutable"
        >
            <template slot="menuLeft">
                <el-dropdown trigger="click" @command="handleCommand">
                    <el-button type="primary" size="mini">
                        <i class="el-icon-edit-outline"></i>
                        操作
                        <i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item v-if="permission.skupackage_import" command="1" icon="el-icon-upload2"
                        >导入
                        </el-dropdown-item>
                        <el-dropdown-item v-if="permission.skupackage_export" command="2" icon="el-icon-download"
                        >导出
                        </el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </template>
        </nodes-crud>

        <file-upload
            :visible="fileUpload.visible"
            template-url="/api/wms/basedata/skupackage/export-template"
            file-name="物品包装"
            @callback="callbackFileUpload"
        ></file-upload>
        <data-verify
            :visible="dataVerify.visible"
            :dataSource="dataVerify.dataSource"
            uploadUrl="/api/wms/basedata/skupackage/import-data"
            dataVerifyUrl="/api/wms/basedata/skupackage/import-valid"
            @callback="callbackDataVerify"
        ></data-verify>
    </basic-container>
</template>

<script>
import {
    getPage,
    add,
    remove,
    getDetail,
    exportFile,
} from "@/api/wms/basedata/skupackage.js";
import {group as group_1} from "./skupackage/group_1";
import {group as group_2} from "./skupackage/group_2";
import fileUpload from "@/components/nodes/fileUpload";
import dataVerify from "@/components/nodes/dataVerify";
import fileDownload from "js-file-download";
import {mapGetters} from "vuex";

export default {
    name: "outstock",
    components: {
        fileUpload,
        dataVerify,
    },
    data() {
        return {
            loading: false,
            form: {},
            data: [],
            selectionList: [],
            fileUpload: {
                visible: false,
            },
            dataVerify: {
                visible: false,
                dataSource: {},
                dataVerifyUrl: "/api/basedata/skupackage/dataVerify",
                uploadUrl: "/api/basedata/skupackage/upload/save",
            },
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
                        label: "包装名称",
                        prop: "wspName",
                        search: true,
                        placeholder: "支持模糊查询",
                        width: 200,
                        view: true,
                        sortable: true,
                    },
                    {
                        label: "每层箱数",
                        prop: "palletBoxLevel",
                        width: 100,
                        search: true,
                    },
                    {
                        label: "每托层数",
                        prop: "palletLevel",
                        width: 100,
                        search: true,
                    },
                    {
                        label: "每托长度(cm)",
                        prop: "lpnLength",
                        width: 100,
                        search: true,
                    },
                    {
                        label: "每托宽度(cm)",
                        prop: "lpnWidth",
                        width: 100,
                        search: true,
                    },
                    {
                        label: "每托高度(cm)",
                        prop: "lpnHeight",
                        width: 100,
                        search: true,
                    },
                    {
                        label: "每托重量(kg)",
                        prop: "lpnWeight",
                        width: 100,
                        search: true,
                    },
                    {
                        label: "规格",
                        prop: "spec",
                        width: 120,
                        sortable: true,
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
                add: this.vaildData(this.permission.skupackage_add, false),
                view: this.vaildData(this.permission.skupackage_view, false),
                delete: this.vaildData(this.permission.skupackage_delete, false),
                edit: this.vaildData(this.permission.skupackage_edit, false),
                导入: this.vaildData(this.permission.skupackage_import, false),
                导出: this.vaildData(this.permission.skupackage_export, false),
            }
        },
        ids() {
            let ids = [];
            this.selectionList.forEach((ele) => {
                ids.push(ele.wspId);
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
            if (["edit", "view"].includes(type)) {
                getDetail(this.form.wspId)
                    .then((res) => {
                        this.form = res.data.data;
                    })
                    .finally(() => {
                        done();
                    });
            }
        },
        rowSave(row, loading, done, type) {
            if (row.skuPackageDetailVOList) {
                for (let i = 0; i < row.skuPackageDetailVOList.length; i++) {
                    let item = row.skuPackageDetailVOList[i];
                    let count = row.skuPackageDetailVOList.filter(
                        (u) => u.wsuName === item.wsuName
                    ).length;
                    if (count > 1) {
                        this.$message.warning(
                            "明细计量单位 [" + item.wsuName + "] 存在多条！"
                        );
                        done();
                        return;
                    }
                }
            }
            this.$set(row, "skuPackageDetailDTOList", row.skuPackageDetailVOList);
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
                remove(row.wspId).then(() => {
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
        handleCommand(cmd) {
            switch (parseInt(cmd)) {
                case 1:
                    this.fileUpload.visible = true;
                    break;
                case 2:
                    this.loading = true;
                    exportFile(this.searchData)
                        .then((res) => {
                            this.$message.success("操作成功，正在下载中...");
                            fileDownload(res.data, "物品包装.xlsx");
                        })
                        .catch(() => {
                            this.$message.error("系统模板目录配置有误或文件不存在");
                        })
                        .finally(() => {
                            this.loading = false;
                        });
                    break;
            }
        },
        callbackFileUpload(res) {
            this.fileUpload.visible = res.visible;
            if (res.result) {
                this.dataVerify.dataSource = res.data;
                this.dataVerify.visible = true;
            }
        },
        callbackDataVerify(res) {
            this.dataVerify.visible = res.visible;
            if (res.result) {
                this.$message.success("导入成功！");
                this.$refs.table.onLoad();
            }
        },
        searchChange(data) {
            this.searchData = data;
        },
    },
};
</script>

<style scoped>
</style>
