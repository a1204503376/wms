<template>
    <basic-container>
        <nodes-crud
            ref="crud"
            v-model="form"
            :option="option"
            :data="data"
            :table-loading="loading"
            :before-open="beforeOpen"
            :permission="permissionList"
            @on-load="onLoad"
            @row-save="rowSave"
            @on-del="rowDel"
            @load-List="loadList"
            @on-multi-del="onMultiDel"
            @selection-change="selectionChange"
            @search-change="searchChange"
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
                        <el-dropdown-item v-if="permission.skutype_import" command="1" icon="el-icon-upload2"
                        >导入
                        </el-dropdown-item>
                        <el-dropdown-item v-if="permission.skutype_export" command="2" icon="el-icon-download"
                        >导出
                        </el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </template>
            <template slot="menuRight"></template>
            <template slot="menu"></template>
        </nodes-crud>
        <file-upload
            :visible="fileUpload.visible"
            template-url="/api/wms/basedata/skutype/export-template"
            file-name="物品分类"
            @callback="callbackFileUpload"
        ></file-upload>
        <data-verify
            :visible="dataVerify.visible"
            :dataSource="dataVerify.dataSource"
            uploadUrl="/api/wms/basedata/skutype/import-data"
            dataVerifyUrl="/api/wms/basedata/skutype/import-valid"
            @callback="callbackDataVerify"
        ></data-verify>
    </basic-container>
</template>

<script>
import {
    getList,
    getPage,
    getDetail,
    update as submit,
    remove,
    exportFile,
} from "@/api/wms/basedata/skutype";
import {group as group_1} from "./skutype/group_1";
import fileUpload from "@/components/nodes/fileUpload";
import dataVerify from "@/components/nodes/dataVerify";
import fileDownload from "js-file-download";
import {mapGetters} from "vuex";

export default {
    components: {
        fileUpload,
        dataVerify,
    },
    data() {
        return {
            form: {},
            query: {},
            maps: new Map(),
            loading: false,
            skuLoading: false,
            data: [],
            skuData: [],
            selectionList: [],
            fileUpload: {
                visible: false,
            },
            dataVerify: {
                visible: false,
                dataSource: {},
                dataVerifyUrl: "/api/basedata/skutype/dataVerify",
                uploadUrl: "/api/basedata/skutype/upload/save",
            },
            option: {
                newBtn: true,
                multiDelBtn: true,
                editBtn: true,
                delBtn: true,
                menu: true,
                custom: false,
                page: true,
                rowKey: "id",
                lazy: true,
                menuItem: [
                    {
                        label: "添加子级",
                        command: 1,
                        divided: true
                    }
                ],
                column: [
                    {
                        label: "分类编码",
                        prop: "typeCode",
                        sortable: true,
                        search: true,
                        view: true,
                    },
                    {
                        label: "分类名称",
                        prop: "typeName",
                        sortable: true,
                        search: true,
                    },
                    {
                        label: "货主",
                        prop: "woId",
                        search: true,
                        type: "select",
                        dicUrl: "/api/wms/basedata/owner/list",
                        props: {
                            label: "ownerName",
                            value: "woId",
                        },
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
                add: this.vaildData(this.permission.skutype_add, false),
                view: this.vaildData(this.permission.skutype_view, false),
                delete: this.vaildData(this.permission.skutype_delete, false),
                edit: this.vaildData(this.permission.skutype_edit, false),
                导入: this.vaildData(this.permission.skutype_import, false),
                导出: this.vaildData(this.permission.skutype_export, false),
            }
        },
        ids() {
            let ids = [];
            this.selectionList.forEach((ele) => {
                ids.push(ele.skuTypeId);
            });
            return ids.join(",");
        },
    },
    mounted() {
        this.option.group.push(group_1);
    },
    methods: {
        loadList(tree, treeNode, resolve) {
            const pid = tree.skuTypeId;
            this.maps.set(pid, {tree, treeNode, resolve});
            getList({typePreId: pid}).then((res) => {
                resolve(res.data.data);
                this.$refs.crud.doLayout();
            });
        },
        rowSave(row, loading, done, type) {
            submit(row).then(
                () => {
                    loading();
                    this.$refs.crud.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!",
                    });
                },
                (error) => {
                    done();
                }
            );
        },
        rowDel(row, index) {
            this.$confirm("确定将选择数据删除?", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                remove(row.id).then(() => {
                    this.$refs.crud.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!",
                    });
                    const {parentId} = row;
                    if (parentId != "0" && this.maps.has(parentId)) {
                        const {tree, treeNode, resolve} = this.maps.get(parentId);
                        this.$set(
                            this.$refs.table.$refs.table.store.states.lazyTreeNodeMap,
                            parentId,
                            []
                        );
                        this.loadList(tree, treeNode, resolve);
                    }
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
                type: "warning",
            }).then(() => {
                remove(this.ids).then(() => {
                    this.$refs.crud.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!",
                    });
                    this.$refs.crud.toggleSelection();
                });
            });
        },
        beforeOpen(done, type, finish) {
            if (["edit", "view"].includes(type)) {
                getDetail(this.form.id)
                    .then((res) => {
                        this.form = res.data.data;
                    })
                    .finally(() => {
                        done();
                    });
            }
        },
        selectionChange(list) {
            this.selectionList = list;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.crud.toggleSelection();
        },
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(page.currentPage, page.pageSize, params).then((res) => {
                this.data = res.data.data.records;
                page.total = res.data.data.total;
                this.loading = false;
                this.selectionClear();
            });
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
                            fileDownload(res.data, "物品分类.xlsx");
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
        menuCommand(cmd, row, index) {
            switch (cmd) {
                case 1:
                    this.addChild(row);
                    break;
            }
        },
        addChild(row) {
            this.$refs.crud.rowAdd({
                typePreId: row.skuTypeId,
                woId: row.woId,
            });
        },
    },
};
</script>

<style lang="scss">
</style>
