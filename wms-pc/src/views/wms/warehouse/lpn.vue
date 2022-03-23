import fileDownload from "js-file-download";
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
                :before-open="beforeOpen"
                @on-multi-del="onMultiDel"
                @selection-change="selectionChange">

        <template slot="menuLeft">
            <el-dropdown trigger="click" @command="handleCommand">
                <el-button type="primary" size="mini">
                    <i class="el-icon-edit-outline"></i>
                    操作
                    <i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item v-if="permission.lpn_import" command="1" icon="el-icon-upload2" divided>导入</el-dropdown-item>
                    <el-dropdown-item v-if="permission.lpn_export" command="2" icon="el-icon-download">导出</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </template>
        </nodes-crud>
        <file-upload
            :visible="fileUpload.visible"
            template-url="/api/wms/warehouse/lpn/export-template"
            file-name="容器"
            @callback="callbackFileUpload"
        ></file-upload>
        <data-verify
            :visible="dataVerify.visible"
            :dataSource="dataVerify.dataSource"
            uploadUrl="/api/wms/warehouse/lpn/import-data"
            dataVerifyUrl="/api/wms/warehouse/lpn/import-valid"
            @callback="callbackDataVerify"
        ></data-verify>
    </basic-container>
</template>
<script>
    import {getPage, add, remove, getDetail, exportFile} from "@/api/wms/warehouse/lpn";
    import fileUpload from "@/components/nodes/fileUpload";
    import dataVerify from "@/components/nodes/dataVerify";
    import fileDownload from "js-file-download";
    import {mapGetters} from "vuex";
    export default {
        name: "lpn",
        components: {
            fileUpload,
            dataVerify
        },
        data() {
            return {
                loading: false,
                tableColumnVisible: false,
                selectionList: [], //选中的数据
                data: [],
                fileUpload: {
                    visible: false
                },
                dataVerify: {
                    visible: false,
                    dataSource: {}
                },
                form: {},
                option: {
                    newBtn: true,
                    multiDelBtn: true,
                    viewBtn: true,
                    editBtn: true,
                    delBtn: true,
                    menu: true,
                    custom: false,
                    group: [
                        {
                            column: [
                                {
                                    label: "容器编码",
                                    prop: "lpnCode",
                                    maxlength: 30,
                                    rules: [
                                        {
                                            required: true,
                                            message: "容器编码不能为空",
                                            trigger: "blur",
                                            pattern: /\S/,
                                        }
                                    ]
                                },
                                {
                                    label: "容器名称",
                                    prop: "lpnName",
                                    maxlength: 50,
                                    rules: [
                                        {
                                            required: true,
                                            message: "容器名称不能为空",
                                            trigger: "blur",
                                            pattern: /\S/,
                                        }
                                    ]
                                },
                                {
                                    label: "容器类型",
                                    prop: "lpnType",
                                    type: "select",
                                    dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.lpnType,
                                    props: {
                                        label: "dictValue",
                                        value: "dictKey"
                                    },
                                    rules: [
                                        {
                                            required: true,
                                            message: "容器类型不能为空",
                                            trigger: "blur"
                                        }
                                    ],
                                    show:['lpnTypeDesc']
                                },

                                {
                                    label: "容器重量",
                                    prop: "lpnWeight",
                                    type: 'inputNumber',
                                    precision: 2,
                                }
                            ]
                        }
                    ],
                    column: [
                        {
                            label: "容器编码",
                            prop: "lpnCode",
                            search: true,
                            view: true,
                            sortable: true,
                        },
                        {
                            label: "容器名称",
                            prop: "lpnName",
                            search: true,
                            sortable: true,
                        },
                        {
                            label: "容器类型",
                            prop: "lpnType",
                            search: true,
                            type: "select",
                            dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.lpnType,
                            props: {
                                label: "dictValue",
                                value: "dictKey"
                            }
                        },
                        {
                            label: "容器重量",
                            prop: "lpnWeight",
                        }
                    ]
                }
            };
        },
        computed: {
            ...mapGetters(["permission"]),
            permissionList() {
                return {
                    add: this.vaildData(this.permission.lpn_add, false),
                    view: this.vaildData(this.permission.lpn_view, false),
                    delete: this.vaildData(this.permission.lpn_delete, false),
                    edit: this.vaildData(this.permission.lpn_edit, false),
                    导入: this.vaildData(this.permission.lpn_import, false),
                    导出: this.vaildData(this.permission.lpn_export, false),
                }
            },
            ids() {
                let ids = [];
                this.selectionList.forEach(ele => {
                    ids.push(ele.lpnId);
                });
                return ids.join(",");
            }
        },
        methods: {
            //默认渲染数据
            onLoad(page, params = {}) {
                this.loading = true;
                getPage(
                    page.currentPage,
                    page.pageSize,
                    Object.assign(params, this.query)
                ).then(res => {
                    var data = res.data.data;
                    page.total = data.total;
                    this.data = data.records;
                    this.loading = false;
                    this.selectionClear();
                });
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
            beforeOpen(done, type, finish) {
                if (["edit", "view"].includes(type)) {
                    getDetail(this.form.lpnId)
                        .then(res => {
                            this.form = res.data.data;
                        })
                        .finally(() => {
                            done();
                        });
                }
            },
            onDel(row, index) {
                this.$confirm("确定删除当前数据？", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    remove(row.lpnId).then(() => {
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
            //选中的数据
            selectionChange(list) {
                this.selectionList = list;
            },
            selectionClear() {
                this.selectionList = [];
                this.$refs.table.toggleSelection();
            },
            handlePrint() {
                if (!this.selectionList || this.selectionList.length == 0) {
                    this.$message.warning("至少选择一条数据！");
                    return;
                }
                this.loading = true;
                print(this.ids).then(res => {
                    this.$message.success("操作成功！");
                }).finally(() => {
                    this.loading = false;
                });
            },
            handleCommand(cmd) {
                switch (parseInt(cmd)) {
                    case 0:
                        this.handlePrint();
                        break;
                    case 1:
                        this.fileUpload.visible = true;
                        break;
                    case 2:
                        this.loading = true;
                        exportFile(this.searchData).then(res => {
                            this.$message.success('操作成功，正在下载中...');
                            fileDownload(res.data, "容器.xlsx");
                        }).catch(() => {
                            this.$message.error("系统模板目录配置有误或文件不存在");
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
<style lang="scss">
</style>
