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
        >
            <template slot="menuLeft">
                <el-dropdown trigger="click" @command="handleCommand">
                    <el-button type="primary" size="mini">
                        <i class="el-icon-edit-outline"></i>
                        操作
                        <i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item v-if="permission.warehouse_import" command="1" icon="el-icon-upload2">导入</el-dropdown-item>
                        <el-dropdown-item v-if="permission.warehouse_export" command="2" icon="el-icon-download">导出</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </template>
        </nodes-crud>
        <file-upload
            :visible="fileUpload.visible"
            template-url="/api/wms/warehouse/warehouse/export-template"
            file-name="库房"
            @callback="callbackFileUpload"
        ></file-upload>
        <data-verify
            :visible="dataVerify.visible"
            :dataSource="dataVerify.dataSource"
            uploadUrl="/api/wms/warehouse/warehouse/import-data"
            dataVerifyUrl="/api/wms/warehouse/warehouse/import-valid"
            @callback="callbackDataVerify"
        ></data-verify>
    </basic-container>
</template>

<script>
    import {getPage, add, remove, getDetail, exportFile} from "@/api/wms/warehouse/warehouse.js";
    import {group as group_1} from "./warehouse/group_1";
    import {group as group_2} from "../common/address";
    import {group as group_3} from "../common/contacts";
    import fileUpload from "@/components/nodes/fileUpload";
    import dataVerify from "@/components/nodes/dataVerify";
    import fileDownload from "js-file-download";
    import {mapGetters} from "vuex";
    export default {
        name: "outstock",
        components: {
            fileUpload,
            dataVerify
        },
        data() {
            return {
                loading: false,
                form: {},
                data: [],
                selectionList: [],
                fileUpload: {
                    visible: false
                },
                dataVerify: {
                    visible: false,
                    dataSource: {}
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
                            label: "仓库编码",
                            prop: "whCode",
                            search: true,
                            view: true,
                            sortable: true
                        },
                        {
                            label: "仓库名称",
                            prop: "whName",
                            search: true,
                            sortable: true
                        },
                        {
                            label: '所属机构',
                            prop: 'deptId',
                            sortable: true,
                            type: "select-tree",
                            dicUrl: "/api/blade-system/dept/tree",
                            props: {
                                label: "title",
                                value: "id",
                                children: "children"
                            }
                        },
                        {
                            label: "国家",
                            prop: "country",
                            search: true,
                            sortable: true
                        },
                        {
                            label: "省份",
                            prop: "province",
                            search: true,
                            sortable: true
                        },
                        {
                            label: "城市",
                            prop: "city",
                            search: true,
                            sortable: true
                        },
                        {
                            label: "邮编",
                            prop: "zipCode"
                        }
                    ],
                    group: []
                },
                searchData: {},
            };
        },
        mounted() {
            this.option.group.push(group_1);
            this.option.group.push(group_2);
            this.option.group.push(group_3);
        },
        computed: {
            ...mapGetters(["permission"]),
            permissionList() {
                return {
                    add: this.vaildData(this.permission.warehouse_add, false),
                    view: this.vaildData(this.permission.warehouse_view, false),
                    delete: this.vaildData(this.permission.warehouse_delete, false),
                    edit: this.vaildData(this.permission.warehouse_edit, false),
                    导入: this.vaildData(this.permission.warehouse_import, false),
                    导出: this.vaildData(this.permission.warehouse_export, false),
                }
            },
            ids() {
                let ids = [];
                this.selectionList.forEach(ele => {
                    ids.push(ele.whId);
                });
                return ids.join(",");
            },
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
            beforeOpen(done, type, finish) {
                if (["edit", "view"].includes(type)) {
                    getDetail(this.form.whId)
                        .then(res => {
                            this.form = res.data.data;
                        })
                        .finally(() => {
                            done();
                        });
                }
            },
            rowSave(row, loading, done, type) {
                add(row).then(
                    () => {
                        loading();
                        this.$refs.table.onLoad();
                        this.$message.success("操作成功！");
                    },
                    error => {
                        done();
                    }
                );
            },
            onDel(row, index) {
                this.$confirm("确定删除当前数据？", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    remove(row.whId).then(() => {
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
                    })
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
                        exportFile(this.searchData).then(res => {
                            this.$message.success('操作成功，正在下载中...');
                            fileDownload(res.data, "库房.xlsx");
                        }).catch(() => {
                            this.$message.error("系统模板目录配置有误或文件不存在")
                        }).finally(() => {
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
            }
        }
    };
</script>

<style scoped>
</style>
